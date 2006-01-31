import java.io.*;

class mcc {
	private enum Architecture {
		IA32, PPC
	};
	
	private static void usage() {
		System.out.println("Usage:");
		System.out.println("mcc [Options] <filename>");
		System.out.println();
		System.out.println("Options:");
		System.out.println("    -no                Don't optimize constant expressions (MAY NOT COMPILE!)");
		System.out.println("    -a                 Display AST");
		System.out.println("    -uglyast           Display AST in a ugly way");
		System.out.println("    -s                 Display variable scopes");
		System.out.println("    -nc                Do not cast variables (MAY NOT COMPILE!)");
		System.out.println("    -ncc               Do not cast constant variables (MAY NOT COMPILE!)");
		System.out.println("    -ca                Check AST for inconsistencies");
		System.out.println("    -i <file>          Write icode to file");
		System.out.println("    --nico             No ICode optimizations");
		System.out.println("    -S <file>          Write architecture generated code to file");
		System.out.println("    --arch [ia32|ppc]  Selects the architecture to generate code for");
	}
	
	public static void main(String args[]) throws IOException {
		Yylex lexer;
		Parser parser;
		boolean print_ast = false;
		boolean ugly_ast = false;
		boolean no_optimize = false;
		boolean print_scopes = false;
		boolean no_castings = false;
		boolean no_constcast = false;
		boolean check_ast = false;
		boolean icode = false;
		boolean codegen = false;
		Architecture arch = Architecture.IA32;
		boolean no_icode_optimization = false;
		String icode_file = new String();
		String codegen_file = new String();
		String file = new String();
		
		if (args.length < 1) {
			usage();
			System.exit(1);
		}

		for (int i=0; i<args.length-1; i++) {
			if (args[i].equals("-no")) {
				no_optimize = true;
				continue;
			}
			if (args[i].equals("-a")) {
				print_ast = true;
				continue;
			}
			if (args[i].equals("-s")) {
				print_scopes = true;
				continue;
			}
			if (args[i].equals("-nc")) {
				no_castings = true;
				continue;
			}
			if (args[i].equals("-uglyast")) {
				ugly_ast = true;
				continue;
			}
			if (args[i].equals("-ncc")) {
				no_constcast = true;
				continue;
			}
			if (args[i].equals("-ca")) {
				check_ast = true;
				continue;
			}
			if (args[i].equals("-nico")) {
				no_icode_optimization = true;
				continue;
			}
			if (args[i].equals("-i")) {
				icode = true;
				
				if (++i >= args.length - 1) {
					System.err.println("Missing filename for option -i");
					System.exit(1);
				}

				icode_file = args[i];

				continue;
			}
			if (args[i].equals("-S")) {
				codegen = true;
				
				if (++i >= args.length - 1) {
					System.err.println("Missing filename for option -S");
					System.exit(1);
				}

				codegen_file = args[i];

				continue;
			}
			if (args[i].equals("--arch")) {
				if (++i >= args.length - 1) {
					System.err.println("Missing filename for option --arch");
					System.exit(1);
				}
				
				if (args[i].equals("ia32"))
					arch = Architecture.IA32;
				else if (args[i].equals("ppc"))
					arch = Architecture.PPC;
				else {
					System.err.println("Unknown architecture " + args[i]);
					System.exit(1);
				}
				
				continue;
			}

			System.out.println("Wrong option: " + args[i]);
			usage();
			System.exit(1);
		}
		file=args[args.length-1];
		
		PushbackReader fr = new PushbackReader(new FileReader(file), 1024);
		fr.unread((new String("int writeChar(int char) { } int readChar() { } int writeInt(int nint) { } int readInt() { } int writeReal(real nreal) { } real readReal() { }\n")).toCharArray());
		lexer = new Yylex(fr);
		parser = new Parser(lexer);		
		
		parser.start(file);
		
		GlobalFunctionTable.init(parser.getAST());
		
		CreateSymboltable cs_dry = new CreateSymboltable(true);  
		Symboltable s_dry = cs_dry.generate(parser.getAST());
		cs_dry = null;
		s_dry = null;
		
		if (!no_castings) {
			/* Depends on Symboltable */
			CastVisitor cv = new CastVisitor();		
			cv.insertCastNodes(parser.getAST());
		}
		
		if (!no_optimize) {
			/* Depends on CastVisitor */
			ConstantExprSimplifyAST simplifyast = new ConstantExprSimplifyAST();
			simplifyast.simplify(parser.getAST());
		}
		
		if (!no_constcast) {
			/* Depends on ConstantExprSimplifyAST */
			ConstCast cc = new ConstCast();		
			cc.castConstants(parser.getAST());
		}
		
		/* Depends on ConstCast */
		CreateSymboltable cs = new CreateSymboltable(false);  
		Symboltable s = cs.generate(parser.getAST());
		if (print_scopes) {
			s.display();
		}
		
		if (check_ast) {
			CheckAST checker = new CheckAST();
			checker.checkAST(parser.getAST());
		}
		
		if (print_ast) {
			PrintAST printast = new PrintAST(!ugly_ast);
			printast.print(parser.getAST());
		}
	
		if (icode || codegen) {
			CILGeneratorASTVisitor cilg = new CILGeneratorASTVisitor(parser.getAST());
			FunctionContainer f = cilg.generateICode(parser.getAST());

			if (!no_icode_optimization) {
				LabelBRAOptimizer lbo = new LabelBRAOptimizer(f);
				lbo.optimize();
			}
			
			CILMinRegisterMap cilmrm = new CILMinRegisterMap();
			f = cilmrm.mapVRegs(f);
			
			CILReserveTempVars varreserver = new CILReserveTempVars();
			varreserver.reserveTempVars(f);

			CILPlaceStackVariable varplacer = new CILPlaceStackVariable();
			varplacer.placeVariables(f);
		
			if (icode) {
				File ostream = new File(icode_file);
				if (!isFileWriteable(ostream)) {
					System.err.println("Cannot write to intermediate code file.");
					System.exit(1);
				}
				CILVisitorDisplay cilv = new CILVisitorDisplay(new PrintStream(ostream));
				cilv.visit(f);
			}

			if (codegen) {
				File ostream = new File(codegen_file);
				if (!isFileWriteable(ostream)) {
					System.err.println("Cannot write to executable file.");
					System.exit(1);
				}
				
				CodeGenerator codeGenerator = new CodeGeneratorIA32();
				switch (arch) {
				case IA32:
					break;
				case PPC:
					codeGenerator = new CodeGeneratorPPC();
					break;
				default:
					System.err.println("Unknown architecture.");
					System.exit(1);
				}

				codeGenerator.enableDebug();
				codeGenerator.generateCode(new PrintStream(ostream), f);
			}
		}
	}

	/**
	 * Checks if we can write to a file.
	 *
	 * @return	Returns true if the file can be created and written to otherwise
	 * 			false.
	 */
	private static boolean isFileWriteable(File file) {
		try {
			return file.canWrite() || (!file.exists() && file.createNewFile());
		}
		catch(SecurityException e) {
			return false;
		}
		catch(IOException e) {
			return false;
		}
	}
}
