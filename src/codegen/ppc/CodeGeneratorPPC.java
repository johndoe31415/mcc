import java.util.*;
import java.io.PrintStream;

public class CodeGeneratorPPC extends CodeGenerator {
	protected HashSet<Variable> globalVariables;

	public CodeGeneratorPPC() {
		globalVariables = new HashSet<Variable>();
	}

	protected int getRegisterNumber(Operand op) {
		if (!(op instanceof HardwareRegister)) {
			throw new RuntimeException("getRegisterNumber died since it was no hwregister operand.");
		}

		return Integer.valueOf(op.getName());
	}

	protected Operand mapIA32Reg(Operand op) {
		if (!(op instanceof HardwareRegister)) {
			return op;
		}
		
		if (op.getName().equals("%eax"))
			return new HardwareRegister("11", op.getType().getType());
		else if (op.getName().equals("%ebx"))
			return new HardwareRegister("12", op.getType().getType());
		else if (op.getName().equals("%ecx"))
			return new HardwareRegister("13", op.getType().getType());
		else if (op.getName().equals("%ebp"))
			return new HardwareRegister("31", op.getType().getType());	/// @todo ebp != r2
		else if (op.getName().equals("%esp"))
			return new HardwareRegister("1", op.getType().getType());
		else
			throw new RuntimeException("Unmapped register");
	}
	
	protected String baptizeOperandRaw(Operand op, int manualOffset) {
		if (op instanceof ImmediateOperand) {
			return ((ImmediateOperand)op).getConstant().getValue();
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
				return "" + (-(var.getOffset() - manualOffset)) + "(31)";
			}
			else {
				return "" + (-(var.getOffset() + 4 + manualOffset)) + "(31)";
			}
		}
		
		return mapIA32Reg(op).toString();
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
				return "-" + (var.getOffset() + 4) + "(31, " + index + ", " + size + ")";
			}
		}
		
		throw new RuntimeException("bullshit");
		//return mapIA32Reg(op);
	}

	public void generateCall(String label) {
		ostream.println("	bl	" + label);
	}
	
	public void generatePush(int reg) {
        ostream.println("	subi	1, 1, 4");
		ostream.println("	stw		" + reg + ", 0(1)");
	}
	
	public void generatePop(int reg) {
		ostream.println("	lwz		" + reg + ", 0(1)");
        ostream.println("	addi	1, 1, 4");
	}
	
	public void generateLeave() {
		ostream.println("	mr	1, 31	# free local variables");
		generatePop(31);	// restore previous base pointer
	}
	
	public void generateRet() {
		generatePop(0);	// pop return address
		ostream.println("	mtlr	0	# move register 0 to line register");
		ostream.println("	blr");	// branch to return address
	}

	public void switchByteOrder(int reg) {
		ostream.println("	lis 23, 0x0000");
		ostream.println("	ori 23, 23, 0x0000");
		ostream.println("");
		ostream.println("	lis 22, 0xff00");
		ostream.println("	ori 22, 22, 0x0000");
		ostream.println("	and 22, 22, " + reg);
		ostream.println("	srwi 22, 22, 24");
		ostream.println("	or 23, 23, 22		# 23 |= 22");
		ostream.println("");
		ostream.println("	lis 22, 0x00ff");
		ostream.println("	ori 22, 22, 0x0000");
		ostream.println("	and 22, 22, " + reg);
		ostream.println("	srwi 22, 22, 8");
		ostream.println("	or 23, 23, 22		# 23 |= 22");
		ostream.println("	");
		ostream.println("	lis 22, 0x0000");
		ostream.println("	ori 22, 22, 0xff00");
		ostream.println("	and 22, 22, " + reg);
		ostream.println("	slwi 22, 22, 8");
		ostream.println("	or 23, 23, 22		# 23 |= 22");
		ostream.println("	");
		ostream.println("	lis 22, 0x0000");
		ostream.println("	ori 22, 22, 0x00ff");
		ostream.println("	and 22, 22, " + reg);
		ostream.println("	slwi 22, 22, 24");
		ostream.println("	or 23, 23, 22		# 23 |= 22");
		ostream.println("");
		ostream.println("	mr " + reg + ", 23");
	}

	public int le2be(int x) {
		return ((x & 0x000000ff) << 24) | ((x & 0x0000ff00) << 8) | ((x & 0x00ff0000) >>> 8) | ((x & 0xff000000) >>> 24);
	}
	
	public void generateFunctionEntrance(String name) {
		ostream.println(name + ":");

		ostream.println("	mflr	0	# move link register to register 0");
		generatePush(0);	// store return address
		generatePush(31);	// store base pointer
		ostream.println("	mr	31, 1");	// replace base pointer
	}
	
	public void generateFunctionExit() {
		generateLeave();
		generateRet();
	}
	

	public void visit(FunctionContainer funcs) {
		ostream.println("	.text");
		ostream.println("	.global _start");
		ostream.println("	.global start");
		ostream.println("	.global main");
		ostream.println("start:");
		ostream.println("_start:");
		generateCall("main");
		ostream.println("	li      0, 1	# r0 = syscall number (sys_exit)");
		ostream.println("	mr      3, 11	# r3 = status = return code");
		ostream.println("	sc				# call kernel");
		ostream.println("	");

		for (Iterator<ICodeContainer> i = funcs.iterator(); i.hasNext(); ) {
			ICodeContainer container = i.next();
			if (container.getName().equals("readInt")) {
				generateFunctionEntrance("readInt");
				ostream.println("	li		12, 0");
				ostream.println(".L0:");
				generateCall("readChar");
				ostream.println("	cmpwi	7, 11, 47");
				ostream.println("	ble		7, .L1");
				ostream.println("	cmpwi	7, 11, 57");
				ostream.println("	bgt		7, .L1");
				ostream.println("	b		.L2");
				
				ostream.println(".L1:");
				ostream.println("	mr 11, 12");
				generateFunctionExit();
				
				ostream.println(".L2:");
				ostream.println("	mulli	12, 12, 10");
				ostream.println("	addi	11, 11, -48");
				ostream.println("	add		12, 12, 11");
				ostream.println("	b .L0");
			}
			else if (container.getName().equals("writeInt")) {
				ostream.println("writeInt:");
				ostream.println("	lwz		3, 0(1)");			// Pop 3
				ostream.println("	stwu 1,-112(1)");
				ostream.println("	mflr 0");
				ostream.println("	stw 31,108(1)");
				ostream.println("	stw 0,116(1)");
				ostream.println("	mr 31,1");
				ostream.println("	stw 3,8(31)");
				ostream.println("	lwz 0,8(31)");
				ostream.println("	cmpwi 7,0,0");
				ostream.println("	bge 7,.writeInt_L3");
				ostream.println("	lwz 0,8(31)");
				ostream.println("	neg 0,0");
				ostream.println("	stw 0,8(31)");
				ostream.println("	lis 3,0x0000");
				ostream.println("	ori 3,3,0x002d");
				generatePush(3);
				ostream.println("	bl writeChar");
				generatePop(3);
				ostream.println(".writeInt_L3:");
				ostream.println("	li 0,0");
				ostream.println("	stw 0,80(31)");
				ostream.println(".writeInt_L4:");
				ostream.println("	lwz 0,80(31)");
				ostream.println("	cmpwi 7,0,14");
				ostream.println("	bgt 7,.writeInt_L5");
				ostream.println("	lwz 0,80(31)");
				ostream.println("	slwi 9,0,2");
				ostream.println("	addi 0,31,8");
				ostream.println("	add 9,9,0");
				ostream.println("	addi 10,9,8");
				ostream.println("	lwz 9,8(31)");
				ostream.println("	lis 0,0x6666");
				ostream.println("	ori 0,0,26215");
				ostream.println("	mulhw 0,9,0");
				ostream.println("	srawi 11,0,2");
				ostream.println("	srawi 0,9,31");
				ostream.println("	subf 0,0,11");
				ostream.println("	mulli 0,0,10");
				ostream.println("	subf 0,0,9");
				ostream.println("	stw 0,0(10)");
				ostream.println("	lwz 11,8(31)");
				ostream.println("	lis 0,0x6666");
				ostream.println("	ori 0,0,26215");
				ostream.println("	mulhw 0,11,0");
				ostream.println("	srawi 9,0,2");
				ostream.println("	srawi 0,11,31");
				ostream.println("	subf 0,0,9");
				ostream.println("	stw 0,8(31)");
				ostream.println("	lwz 9,80(31)");
				ostream.println("	addi 0,9,1");
				ostream.println("	stw 0,80(31)");
				ostream.println("	b .writeInt_L4");
				ostream.println(".writeInt_L5:");
				ostream.println("	li 0,0");
				ostream.println("	stw 0,84(31)");
				ostream.println("	li 0,14");
				ostream.println("	stw 0,80(31)");
				ostream.println(".writeInt_L7:");
				ostream.println("	lwz 0,80(31)");
				ostream.println("	cmpwi 7,0,0");
				ostream.println("	blt 7,.writeInt_L8");
				ostream.println("	lwz 0,80(31)");
				ostream.println("	slwi 9,0,2");
				ostream.println("	addi 0,31,8");
				ostream.println("	add 9,9,0");
				ostream.println("	addi 9,9,8");
				ostream.println("	lwz 0,0(9)");
				ostream.println("	cmpwi 7,0,0");
				ostream.println("	bne 7,.writeInt_L11");
				ostream.println("	lwz 0,80(31)");
				ostream.println("	cmpwi 7,0,0");
				ostream.println("	bne 7,.writeInt_L10");
				ostream.println(".writeInt_L11:");
				ostream.println("	li 0,1");
				ostream.println("	stw 0,84(31)");
				ostream.println(".writeInt_L10:");
				ostream.println("	lwz 0,84(31)");
				ostream.println("	cmpwi 7,0,0");
				ostream.println("	beq 7,.writeInt_L9");
				ostream.println("	lwz 0,80(31)");
				ostream.println("	slwi 9,0,2");
				ostream.println("	addi 0,31,8");
				ostream.println("	add 9,9,0");
				ostream.println("	addi 9,9,8");
				ostream.println("	lwz 9,0(9)");
				ostream.println("	addi 0,9,48");
				ostream.println("	stw 0,88(31)");
				ostream.println("	lwz 3,88(31)");
				generatePush(3);
				ostream.println("	bl writeChar");
				generatePop(3);
				ostream.println(".writeInt_L9:");
				ostream.println("	lwz 9,80(31)");
				ostream.println("	addi 0,9,-1");
				ostream.println("	stw 0,80(31)");
				ostream.println("	b .writeInt_L7");
				ostream.println(".writeInt_L8:");
				ostream.println("	li 0,0");
				ostream.println("	mr 3,0");
				ostream.println("	lwz 11,0(1)");
				ostream.println("	lwz 0,4(11)");
				ostream.println("	mtlr 0");
				ostream.println("	lwz 31,-4(11)");
				ostream.println("	mr 1,11");
				ostream.println("	blr");
			}
			else if (container.getName().equals("readChar")) {
				generateFunctionEntrance("readChar");
				ostream.println("	li		0, 3		# r0 = syscall number (sys_read)");
        		ostream.println("	subi	1, 1, 4		# reserve local var");
				ostream.println("	li		3, 0		# r3 = fd = stdin = 0");
				ostream.println("	mr		4, 1		# r4 = buf = pointer to local var");
				ostream.println("	li		5, 1		# r5 = count = 1");
				ostream.println("	sc					# call kernel");
				ostream.println("	lbz		11, 0(1)");
				generateFunctionExit();
			}
			else if (container.getName().equals("writeChar")) {
				generateFunctionEntrance("writeChar");
				ostream.println("	li		0, 4		# r0 = syscall number (sys_write)");
				ostream.println("	li		3, 1		# r3 = fd = stdout = 1");
				ostream.println("	lwz		4, 8(31)");
				switchByteOrder(4);
				ostream.println("	stw		4, 8(31)");
				ostream.println("	addi	4, 31, 8	# r4 = buf = pointer to first param");
				ostream.println("	li		5, 1		# r5 = count = 1");
				ostream.println("	sc					# call kernel");
				ostream.println("	mr		11, 3");
				generateFunctionExit();
			}
			else if (container.getName().equals("readReal")) {
				generateFunctionEntrance("readReal");
				generateFunctionExit();
			}
			else if (container.getName().equals("writeReal")) {
				generateFunctionEntrance("writeReal");
				generateFunctionExit();
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
		generateFunctionEntrance(icodes.getName());

		for (Iterator<ICode> i=icodes.iterator(); i.hasNext(); ) {
			i.next().acceptVisitor(this);
		}
	}
	
	public void visit(CAdd icode) {
		debug(icode);
		if (icode.getFirstOp().getType().getType() == Definitions.Types.REAL) {
			ostream.println("	# not implemented for ia32 it is: faddl	" + baptizeOperand(icode.getSecondOp()));
		} else {
			ostream.println("	add	" + baptizeOperand(icode.getTarget()) + ", " + baptizeOperand(icode.getFirstOp()) + ", " + baptizeOperand(icode.getSecondOp()));
		}
		debug_leave(icode);
	}
	
	public void visit(CArithmeticCode icode) {
		visit((ICode)icode);
	}
	
	public void visit(CAssgn icode) {
		debug(icode);
		Definitions.Types firstType, secondType;
		firstType = icode.getFirstOp().getType().getType();
		secondType = icode.getSecondOp().getType().getType();

		if ((firstType == Definitions.Types.INT) && (secondType == Definitions.Types.INT)) {
			if (icode.getSecondOp() instanceof HardwareRegister) {
				// store first op to second
				ostream.println("	lwz	" + baptizeOperand(icode.getSecondOp()) + ", " + baptizeOperand(icode.getFirstOp()));
			}
			else if (icode.getFirstOp() instanceof ImmediateOperand) {
				int number;
				number = Integer.valueOf(((ImmediateOperand)(icode.getFirstOp())).getConstant().getValue());

				int high, low;
				high = (number & 0xffff0000) >>> 16;
				low = number & 0x0000ffff;
				
				ostream.println("	lis	20, 0x" + Integer.toHexString(high));
				ostream.println("	ori	20, 20, 0x" + Integer.toHexString(low));

				ostream.println("	stw	20, " + baptizeOperand(icode.getSecondOp()));
			}	
			else if (icode.getFirstOp() instanceof HardwareRegister &&
					icode.getSecondOp() instanceof HardwareRegister) {
				ostream.println("	mr	20, " + baptizeOperand(icode.getFirstOp()));
				ostream.println("	stw	20, " + baptizeOperand(icode.getSecondOp()));
			}
			else {
				ostream.println("	stw	" + baptizeOperand(icode.getFirstOp()) + ", " + baptizeOperand(icode.getSecondOp()));
			}
		} else {
			if ((firstType == Definitions.Types.REAL) && (secondType == Definitions.Types.REAL)) {
				if (icode.getFirstOp() instanceof ImmediateOperand) {
					double i = Float.valueOf(icode.getFirstOp().toString());
					RIASConverter c = new RIASConverter(i);
					ostream.println("	#movl	 $" + c.getRIASValue1() + ", " + baptizeOperandRaw(icode.getSecondOp(), -4));
					ostream.println("	#movl	 $" + c.getRIASValue0() + ", " + baptizeOperand(icode.getSecondOp()));
				} else {
					if (icode.getSecondOp().getName().equals("%float")) {
						/* Store first operand into FPU register from stack (getFirstOp =>) */
						ostream.println("	#fldl	" + baptizeOperand(icode.getFirstOp()));
					} else {
						/* Load second operand from FPU register onto stack (=> getSecondOp) */
						ostream.println("	#fstpl	" + baptizeOperand(icode.getSecondOp()));
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
			ostream.println("	cmp	cr5, " + baptizeOperand(op2) + ", "+ baptizeOperand(op1));
			ostream.println("	" + signedTarget + "	." + icode.getDestination().getName());
		} else {
			if ((op1.getType().getType() == Definitions.Types.REAL) && (op2.getType().getType() == Definitions.Types.REAL)) {
				ostream.println("\t#Not implemented yet for ia32 it would be:");
				ostream.println("\t#fucompp		# Real comparison");
				ostream.println("\t#fnstsw	%ax	# Store comaparison result at %ax");
				ostream.println("\t#sahf			# Load processor flags from %ax");
				ostream.println("\t#" + unsignedTarget + "\t." + icode.getDestination().getName());
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
		prepareComparison(icode, "beq", "beq");	/// @todo fix
		debug_leave(icode);
	}

	public void visit(CBGE icode) {
		debug(icode);
		prepareComparison(icode, "bge", "bge");	/// @todo fix
		debug_leave(icode);
	}
	
	public void visit(CBGT icode) {
		debug(icode);
		prepareComparison(icode, "bgt", "bgt");	/// @todo fix
		debug_leave(icode);
	}
	
	public void visit(CBLE icode) {
		debug(icode);
		prepareComparison(icode, "ble", "ble");	/// @todo fix
		debug_leave(icode);
	}
	
	public void visit(CBLT icode) {
		debug(icode);
		prepareComparison(icode, "blt", "blt");	/// @todo fix
		debug_leave(icode);
	}
	
	public void visit(CBNE icode) {
		debug(icode);
		prepareComparison(icode, "bne", "bne");	/// @todo fix
		debug_leave(icode);
	}
	
	public void visit(CBRA icode) {
		ostream.println("	b	." + icode.getDestination().getName());
	}
	
	public void visit(CBranchCode icode) {
		visit((ICode)icode);
	}
	
	public void visit(CCall icode) {
		generateCall(icode.getName());
		ostream.println("	addi	1, 1, " + icode.getArgsize());
		ostream.println("	stw		11, " + baptizeOperand(icode.getReturnValue()));
		debug_leave(icode);

	}
	
	public void visit(CDiv icode) {
		debug(icode);
		if (icode.getFirstOp().getType().getType() == Definitions.Types.REAL) {
			ostream.println("	#ia32:fdivl	" + baptizeOperand(icode.getSecondOp()));
		} else {
			ostream.println("	divw	" + baptizeOperand(icode.getTarget()) + ", " + baptizeOperand(icode.getFirstOp()) + ", " + baptizeOperand(icode.getSecondOp()));
		}
		debug_leave(icode);
	}

	public void visit(CFunctionCode icode) {
		visit((ICode)icode);
	}
	
	public void visit(CI2R icode) {
		visit((CTypeConversionCode)icode);
	}

	public void visit(CLabel icode) {
		debug(icode);
		ostream.println("	." + icode.getName() + ":");
		debug_leave(icode);
	}
	
	public void visit(CLoad icode) {
		debug(icode);
//		visit((CVariableCode)icode);
		debug_leave(icode);
	}
	
	public void visit(CMul icode) {
		debug(icode);
		if (icode.getFirstOp().getType().getType() == Definitions.Types.REAL) {
			ostream.println("	#ia32: fmull	" + baptizeOperand(icode.getSecondOp()));
		} else {
			ostream.println("	mullw	" + baptizeOperand(icode.getTarget()) + ", " + baptizeOperand(icode.getFirstOp()) + ", " + baptizeOperand(icode.getSecondOp()));
		}
		debug_leave(icode);
	}
	
	public void visit(CPush icode) {
		debug(icode);
		if (icode.getOperand().getType().getType() == Definitions.Types.INT) {
			generatePush(getRegisterNumber(mapIA32Reg(icode.getOperand())));
		}
		else {
			ostream.println("	stwu	1, -8(1)");
			ostream.println("	#floating point stuff not implemented yet ia32 code would be:");
			ostream.println("	#pushl	$0");
			ostream.println("	#pushl	$0");
			ostream.println("	#fstpl	(%esp)");
		}
		debug_leave(icode);
	}
	
	public void visit(CR2I icode) {
		visit((CTypeConversionCode)icode);
	}
	
	public void visit(CRet icode) {
		generateFunctionExit();
	}
	
	public void visit(CStore icode) {
		debug(icode);
		if (icode.getSource().getType().getType() == Definitions.Types.INT) {
			if (icode.getIndex() instanceof ImmediateOperand) {
				ImmediateOperand index = (ImmediateOperand)icode.getIndex();
				if (index.getConstant().getValue().equals("0")) {
					ostream.println("	stw    " + baptizeOperand(icode.getSource()) + ", " + baptizeOperand(icode.getTarget()));
				}
				else {
					ostream.println("	stw    " + ((ImmediateOperand)icode.getIndex()).getConstant().getValue() + "(" + baptizeOperand(icode.getSource()) + "), " + baptizeOperand(icode.getTarget()));
				}
			}
				else {
				ostream.println("	stw    " + baptizeOperand(icode.getSource()) + ", " + baptizeOperand(icode.getTarget(), icode.getIndex(), 4));
			}
		}
		debug_leave(icode);
	}
	
	public void visit(CSub icode) {
		debug(icode);
		if (icode.getFirstOp().getType().getType() == Definitions.Types.REAL) {
			ostream.println("	#ia32: fsubl	" + baptizeOperand(icode.getSecondOp()));
		} else {
			ostream.println("	subf	" + baptizeOperand(icode.getTarget()) + ", " + baptizeOperand(icode.getFirstOp()) + ", " + baptizeOperand(icode.getSecondOp()));
		}
		debug_leave(icode);
	}
	
	public void visit(CTypeConversionCode icode) {
		visit((ICode)icode);
	}

	public void visit(CVariableCode icode) {
		visit((ICode)icode);
	}

	public void visit(CVar icode) {
		debug(icode);
		int size;
		if (icode.getOperand().getType().getType() == Definitions.Types.INT) {
			Variable v = (Variable)icode.getOperand();
			size = 4 * v.getArraySize();
			ostream.println("	subi	1, 1, " + size + "	# Reserving " + size + " bytes for integer");
		} else {
			Variable v = (Variable)icode.getOperand();
			size = 8 * v.getArraySize();
			ostream.println("	subi	1, 1, " + size + "	# Reserving " + size + " bytes for real");
		}
		debug_leave(icode);
	}

}
