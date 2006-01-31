import java.lang.*;
import java.util.*;
import java.io.PrintStream;

public class CodeGeneratorIA32 extends CodeGenerator {
	protected int paramOffset;
	protected HashSet<Variable> globalVariables;

	public CodeGeneratorIA32() {
		globalVariables = new HashSet<Variable>();
	}
	
	public PrintStream indent(int depth) {
		for (int i = 0; i < depth; ++i) {
			ostream.print("	");
		}
		
		return ostream;
	}
	
	protected String baptizeOperandRaw(Operand op, int manualOffset) {
		if (op instanceof ImmediateOperand) {
			return "$" + ((ImmediateOperand)op).getConstant().getValue();
		}
		else if (op instanceof Variable) {
			Variable var = (Variable)op;
			if (var.getOffset() == -1) {
				if (!globalVariables.contains(var)) {
					globalVariables.add(var);
				}

				// global var (has no offset relative to base pointer)
				return var.getName();
			}
			else if (var.getOffset() < 0) {
				return "" + (-(var.getOffset() - manualOffset)) + "(%ebp)";
			}
			else {
				return "" + (-(var.getOffset() + 4 + manualOffset)) + "(%ebp)";
			}
		}
		
		return op.toString();
	}

	protected String baptizeOperand(Operand op) {
		return baptizeOperandRaw(op, 0);
	}
	
	protected String baptizeOperand(Operand op, Operand index, int size) {
		if (op instanceof ImmediateOperand) {
			//return "$" + ((ImmediateOperand)op).getConstant().getValue();
			throw new RuntimeException("bullshit");
		}
		else if (op instanceof Variable) {
			Variable var = (Variable)op;
			if (var.getOffset() < 0) {
				if (!globalVariables.contains(var)) {
					globalVariables.add(var);
				}
				
				// global var (has no offset relative to base pointer)
				return var.getName() + "(, " + index + ", " + size + ")";
			}
			else {
				return "-" + (var.getOffset() + 4) + "(%ebp, " + index + ", " + size + ")";
			}
		}
		
			throw new RuntimeException("bullshit");
		//return op.toString();
	}
	
	public void visit(FunctionContainer funcs) {
		ostream.println(".LC0:");
		ostream.println("	.string \"%d\"");
		ostream.println("");
		ostream.println(".LC1:");
		ostream.println("	.string \"%d\\n\"");
		ostream.println("");
		ostream.println(".LC2:");
		ostream.println("	.string \"%lf\"");
		ostream.println("");
		ostream.println(".LC3:");
		ostream.println("	.string \"%lf\\n\"");
		ostream.println("");
		ostream.println("	.global start");
		ostream.println("	.global main");
		ostream.println("	.text");
		ostream.println("start:");
		ostream.println("	call	main");
		/*ostream.println("	movl	%eax, %ebx	# first argument: exit code");
		ostream.println("	movl	$1, %eax	# system call number");
		ostream.println("	int 	$0x80		# trigger system call");*/
		ostream.println("	pushl	%eax");
		ostream.println("	call	exit");
		ostream.println("	");

		for (Iterator<ICodeContainer> i = funcs.iterator(); i.hasNext(); ) {
			ICodeContainer container = i.next();
			if (container.getName().equals("readInt")) {
				ostream.println("readInt:");
				ostream.println("	pushl	%ebp");
				ostream.println("	movl	%esp, %ebp");
				ostream.println("	subl	$24, %esp");
				ostream.println("	leal	-4(%ebp), %eax");
				ostream.println("	movl	%eax, 4(%esp)");
				ostream.println("	movl	$.LC0, (%esp)");
				ostream.println("	call	scanf");
				ostream.println("	movl	-4(%ebp), %eax");
				ostream.println("	leave");
				ostream.println("	ret");
			}
			else if (container.getName().equals("writeInt")) {
				ostream.println("writeInt:");
				ostream.println("	pushl	%ebp");
				ostream.println("	movl	%esp, %ebp");
				ostream.println("	subl	$8, %esp");
				ostream.println("	movl	8(%ebp), %eax");
				ostream.println("	movl	%eax, 4(%esp)");
				ostream.println("	movl	$.LC0, (%esp)");
				ostream.println("	call	printf");
				ostream.println("	leave");
				ostream.println("	ret");
			}
			else if (container.getName().equals("readChar")) {
				ostream.println("readChar:");
				ostream.println("	pushl	%ebp");
				ostream.println("	movl	%esp, %ebp");
				ostream.println("	subl	$8, %esp");
				ostream.println("	call	getchar");
				ostream.println("	leave");
				ostream.println("	ret");
			}
			else if (container.getName().equals("writeChar")) {
				/*ostream.println("writeChar:");
				ostream.println("	pushl	%ebp");
				ostream.println("	movl	%esp, %ebp");
				ostream.println("	pushl	%ebx");
				ostream.println("	movl	$1, %edx		# third argument: message length");
				ostream.println("	movl	8(%ebp), %ecx	# second argument: pointer to message to write");
				ostream.println("	movl	$1, %ebx		# first argument: file handle (stdout)");
				ostream.println("	movl	$4, %eax		# system call number (sys_write)");
				ostream.println("	int		$0x80			# call kernel");
				ostream.println("	popl	%ebx");
				ostream.println("	leave");
				ostream.println("	ret");*/

				ostream.println("writeChar:");
				ostream.println("	pushl	%ebp");
				ostream.println("	movl	%esp, %ebp");
				ostream.println("	subl	$8, %esp");
				ostream.println("	movl	8(%ebp), %eax");
				ostream.println("	movl	%eax, (%esp)");
				ostream.println("	call	putchar");
				ostream.println("	leave");
				ostream.println("	ret");
			}
			else if (container.getName().equals("readReal")) {
				ostream.println("readReal:");
				ostream.println("	pushl	%ebp");
				ostream.println("	movl	%esp, %ebp");
				ostream.println("	subl	$24, %esp");
				ostream.println("	leal	-8(%ebp), %eax");
				ostream.println("	movl	%eax, 4(%esp)");
				ostream.println("	movl	$.LC2, (%esp)");
				ostream.println("	call	scanf");
				ostream.println("	fldl	-8(%ebp)");
				ostream.println("	leave");
				ostream.println("	ret");
			}
			else if (container.getName().equals("writeReal")) {
				ostream.println("writeReal:");
				ostream.println("	pushl	%ebp");
				ostream.println("	movl	%esp, %ebp");
				ostream.println("	subl	$24, %esp");
				ostream.println("	fldl	8(%ebp)");
				ostream.println("	fstpl	-8(%ebp)");
				ostream.println("	fldl	-8(%ebp)");
				ostream.println("	fstpl	4(%esp)");
				ostream.println("	movl	$.LC2, (%esp)");
				ostream.println("	call	printf");
				ostream.println("	leave");
				ostream.println("	ret");
			}
			else {
				container.acceptVisitor(this);
			}
		}
		
		// reserve global vars
		ostream.println("");
		for (Variable v : globalVariables) {
			ostream.println("	.comm	" + v.getName() + ", " + (v.getArraySize() * v.getType().getSize()) + ", " + v.getType().getSize());
		}
	}
	
	public void visit(ICodeContainer icodes) {
		ostream.println(icodes.getName() + ":");

		// save nonvolatile registers
		ostream.println("	pushl	%ebx");
		ostream.println("	pushl	%esi");
		ostream.println("	pushl	%edi");
		ostream.println("	pushl	%ebp");
		ostream.println("	movl	%esp, %ebp");
		paramOffset = 5 * 4;
		
		for (Iterator<ICode> i=icodes.iterator(); i.hasNext(); ) {
			i.next().acceptVisitor(this);
		}
	}
	
	public void visit(CAssgn icode) {
		debug(icode);
		Definitions.Types firstType, secondType;
		firstType = icode.getFirstOp().getType().getType();
		secondType = icode.getSecondOp().getType().getType();

		if ((firstType == Definitions.Types.INT) && (secondType == Definitions.Types.INT)) {
			ostream.println("	movl	" + baptizeOperand(icode.getFirstOp()) + ", " + baptizeOperand(icode.getSecondOp()));
		} else {
			if ((firstType == Definitions.Types.REAL) && (secondType == Definitions.Types.REAL)) {
				if (icode.getFirstOp() instanceof ImmediateOperand) {
					double i = Float.valueOf(icode.getFirstOp().toString());
					RIASConverter c = new RIASConverter(i);
					ostream.println("	movl	 $" + c.getRIASValue1() + ", " + baptizeOperandRaw(icode.getSecondOp(), -4));
					ostream.println("	movl	 $" + c.getRIASValue0() + ", " + baptizeOperand(icode.getSecondOp()));
				} else {
					if (icode.getSecondOp().getName().equals("%float")) {
						/* Store first operand into FPU register from stack (getFirstOp =>) */
						ostream.println("	fldl	" + baptizeOperand(icode.getFirstOp()));
					} else {
						/* Load second operand from FPU register onto stack (=> getSecondOp) */
						ostream.println("	fstpl	" + baptizeOperand(icode.getSecondOp()));
					}
				}
			} else {
				ostream.println("	# Code generation is seriously fucked up. Bravely continuing without generating code...");
				ostream.println("	# Code which annoys me would be:");
				ostream.println("	#movl	" + baptizeOperand(icode.getFirstOp()) + ", " + baptizeOperand(icode.getSecondOp()));
				ostream.println("	# Types: " + firstType + " and " + secondType);
			}
		}
		debug_leave(icode);
	}

	public void prepareComparison(CBranchCode icode, String signedTarget, String unsignedTarget) {
		Operand op1, op2;
		op1 = icode.getFirstOpIfPossible();
		op2 = icode.getSecondOpIfPossible();
		if ((op1.getType().getType() == Definitions.Types.INT) && (op2.getType().getType() == Definitions.Types.INT)) {
			ostream.println("\tcmpl\t" + baptizeOperand(op2) + ", "+ baptizeOperand(op1));
			ostream.println("\t" + signedTarget + "\t." + icode.getDestination().getName());
		} else {
			if ((op1.getType().getType() == Definitions.Types.REAL) && (op2.getType().getType() == Definitions.Types.REAL)) {
				ostream.println("\tfucompp		# Real comparison");
				ostream.println("\tfnstsw	%ax	# Store comaparison result at %ax");
				ostream.println("\tsahf			# Load processor flags from %ax");
				ostream.println("\t" + unsignedTarget + "\t." + icode.getDestination().getName());
			} else {
				ostream.println("\t# Code generation is seriously fucked up. Bravely continuing without generating code...");
				ostream.println("\t# Code which annoys me would be:");
				ostream.println("\t#cmp\t" + baptizeOperand(op1) + ", " + baptizeOperand(op2));
				ostream.println("\t# Types: " + op1.getType().getType() + " and " + op2.getType().getType());
			}
		}
	}

	public void visit(CBEQ icode) {
		debug(icode);
		prepareComparison(icode, "je", "je");
		debug_leave(icode);
	}

	public void visit(CBGE icode) {
		debug(icode);
		prepareComparison(icode, "jge", "jae");
		debug_leave(icode);
	}
	
	public void visit(CBGT icode) {
		debug(icode);
		prepareComparison(icode, "jg", "ja");
		debug_leave(icode);
	}
	
	public void visit(CBLE icode) {
		debug(icode);
		prepareComparison(icode, "jle", "jbe");
		debug_leave(icode);
	}
	
	public void visit(CBLT icode) {
		debug(icode);
		prepareComparison(icode, "jl", "jb");
	}
	
	public void visit(CBNE icode) {
		debug(icode);
		prepareComparison(icode, "jne", "jne");
		debug_leave(icode);
	}
	
	public void visit(CBRA icode) {
		debug(icode);
		ostream.println("	jmp	." + icode.getDestination().getName());
		debug_leave(icode);
	}
	
	public void visit(CCall icode) {
		debug(icode);
		ostream.println("	call	" + icode.getName());
		ostream.println("	addl	$" + (icode.getArgsize()) + ", %esp");
		ostream.println("	movl	%eax, " + baptizeOperand(icode.getReturnValue()));
		debug_leave(icode);
	}
	
	public void visit(CFunctionCode icode) {
		debug(icode);
		debug_leave(icode);
	}
	
	public void visit(CI2R icode) {
		debug(icode);
		ostream.println("	fildl	" + baptizeOperand(icode.getSourceOp()));
		debug_leave(icode);
	}

	public void visit(CLabel icode) {
		debug(icode);
		ostream.println("	." + icode.getName() + ":");
		debug_leave(icode);
	}
	
	public void visit(CLoad icode) {
		debug(icode);
		// target := base[index];
		if (icode.getIndex() instanceof ImmediateOperand) {
			ImmediateOperand index = (ImmediateOperand)icode.getIndex();
			if (index.getConstant().getValue().equals("0")) {
				ostream.println("	movl	" + baptizeOperand(icode.getBase()) + ", %eax");
				ostream.println("	movl	%eax, " + baptizeOperand(icode.getTarget()));
			}
			else {
				ostream.println("	movl	" + ((ImmediateOperand)icode.getIndex()).getConstant().getValue() + "(" + baptizeOperand(icode.getBase()) + "), " + baptizeOperand(icode.getTarget()));
			}
		}
		else {
			ostream.println("	movl	" + baptizeOperand(icode.getBase(), icode.getIndex(), 4) + ", %eax");
			ostream.println("	movl	%eax, " + baptizeOperand(icode.getTarget()));
		}
		debug_leave(icode);
	}
	
	public void visit(CPush icode) {
		debug(icode);
		if (icode.getOperand().getType().getType() == Definitions.Types.INT) {
			ostream.println("	pushl	" + icode.getOperand());
		} else {
			ostream.println("	pushl	$0");
			ostream.println("	pushl	$0");
			ostream.println("	fstpl	(%esp)");
		}
		debug_leave(icode);
	}
	
	public void visit(CR2I icode) {
		debug(icode);
		ostream.println("	fldl	" + baptizeOperand(icode.getSourceOp()));
		ostream.println("	fistpl	" + baptizeOperand(icode.getDestinationOp()));
		debug_leave(icode);
	}
	
	public void visit(CRet icode) {
		debug(icode);
		// restore nonvolatile registers
		ostream.println("	movl	%ebp, %esp");
		ostream.println("	popl	%ebp");
		ostream.println("	popl	%edi");
		ostream.println("	popl	%esi");
		ostream.println("	popl	%ebx");
		
		ostream.println("	ret");
		debug_leave(icode);
	}
	
	public void visit(CStore icode) {
		debug(icode);
		if (icode.getSource().getType().getType() == Definitions.Types.INT) {
			if (icode.getIndex() instanceof ImmediateOperand) {
				ImmediateOperand index = (ImmediateOperand)icode.getIndex();
				if (index.getConstant().getValue().equals("0")) {
					ostream.println("	movl	" + baptizeOperand(icode.getSource()) + ", " + baptizeOperand(icode.getTarget()));
				}
				else {
					ostream.println("	movl	" + ((ImmediateOperand)icode.getIndex()).getConstant().getValue() + "(" + baptizeOperand(icode.getSource()) + "), " + baptizeOperand(icode.getTarget()));
				}
			}
			else {
				ostream.println("	movl	" + baptizeOperand(icode.getSource()) + ", " + baptizeOperand(icode.getTarget(), icode.getIndex(), 4));
			}
		} else {
			if (icode.getIndex() instanceof ImmediateOperand) {
				ImmediateOperand index = (ImmediateOperand)icode.getIndex();
				if (Integer.valueOf(index.getConstant().getValue()) != 0) {
					ostream.println("# Case of seriously lazy programmer! Not done yet! Arrays of REAL are evil!");
				} else {
					ostream.println("	fstpl " +  baptizeOperand(icode.getTarget()));
				}
			} else {
				ostream.println("# Case of seriously lazy programmer! Not done yet!");
			}
		}
		debug_leave(icode);
	}
	
	public void visit(CAdd icode) {
		debug(icode);
		if (icode.getFirstOp().getType().getType() == Definitions.Types.REAL) {
			ostream.println("	faddl	" + baptizeOperand(icode.getSecondOp()));
		} else {
			ostream.println("	addl	" + baptizeOperand(icode.getFirstOp()) + ", " + baptizeOperand(icode.getSecondOp()));
		}
		debug_leave(icode);
	}
	
	public void visit(CSub icode) {
		debug(icode);
		if (icode.getFirstOp().getType().getType() == Definitions.Types.REAL) {
			ostream.println("	fsubl	" + baptizeOperand(icode.getSecondOp()));
		} else {
			ostream.println("	subl	" + baptizeOperand(icode.getFirstOp()) + ", " + baptizeOperand(icode.getSecondOp()));
		}
		debug_leave(icode);
	}
	
	public void visit(CMul icode) {
		debug(icode);
		if (icode.getFirstOp().getType().getType() == Definitions.Types.REAL) {
			ostream.println("	fmull	" + baptizeOperand(icode.getSecondOp()));
		} else {
			ostream.println("	mull	" + baptizeOperand(icode.getSecondOp()));
		}
		debug_leave(icode);
	}
	
	public void visit(CDiv icode) {
		debug(icode);
		if (icode.getFirstOp().getType().getType() == Definitions.Types.REAL) {
			ostream.println("	fdivl	" + baptizeOperand(icode.getSecondOp()));
		} else {
			ostream.println("	cltd");
			ostream.println("	idivl	%ebx");
		}
		debug_leave(icode);
	}
	
	public void visit(CTypeConversionCode icode) {
		debug(icode);
		debug_leave(icode);
	}

	public void visit(CVariableCode icode) {
		debug(icode);
		debug_leave(icode);
	}

	public void visit(CVar icode) {
		debug(icode);
		int size;
		if (icode.getOperand().getType().getType() == Definitions.Types.INT) {
			Variable v = (Variable)icode.getOperand();
			size = 4 * v.getArraySize();
			ostream.println("	subl $" + size + ", %esp		# Reserving " + size + " bytes for integer");
		} else {
			Variable v = (Variable)icode.getOperand();
			size = 8 * v.getArraySize();
			ostream.println("	subl $" + size + ", %esp		# Reserving " + size + " bytes for real");
		}
		debug_leave(icode);
	}
}


