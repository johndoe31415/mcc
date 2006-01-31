import java.util.*;

public class CILMinRegisterMap extends CILVisitorImpl {
	public CILMinRegisterMap() {
		vregs = new HashMap<VirtualRegister, Variable>();
	}

	public FunctionContainer funcReplacement;
	public ICodeContainer icodesReplacement;
	protected HashMap<VirtualRegister, Variable> vregs;
	
	public FunctionContainer mapVRegs(FunctionContainer oldfuncs) {
		oldfuncs.acceptVisitor(this);	
		return funcReplacement;
	}
	
	Operand convertVReg(Operand op) {
		if (op instanceof VirtualRegister) {
			return convertVReg((VirtualRegister)op);
		}
		else {
			return op;
		}
	}
	
	Operand convertVReg(VirtualRegister vreg) {
		if (vregs.containsKey(vreg)) {
			return vregs.get(vreg);
		}
		
		String newName = new String("__mem");
		if (vreg.getName().substring(0, 4).equals("__vr")) {
			newName += vreg.getName().substring(4);
		} else {
			// __return_*
			newName += vreg.getName();
		}

		Variable newVar = new Variable(newName, vreg.getType(), -1, null);
		newVar.setDepth(-1);	// mark as local variable
		vregs.put(vreg, newVar);
		return newVar;
	}
	
	Operand mapOperand(Operand op, HardwareRegister reg) {
		if (op instanceof VirtualRegister)
			return mapOperand((VirtualRegister)op, reg);
		else if (op instanceof Variable)
			return mapOperand((Variable)op, reg);
		else if (op instanceof HardwareRegister)
			return mapOperand((HardwareRegister)op, reg);
		else if (op instanceof ImmediateOperand)
			return mapOperand((ImmediateOperand)op, reg);

		throw new RuntimeException("No valid operand type found for mapping operand via mapOperand()");
	}
	
	Operand mapOperand(VirtualRegister vreg, HardwareRegister reg) {
		Operand var = convertVReg(vreg);
		icodesReplacement.add(new CAssgn(var, reg));
		return reg;
	}
	
	Operand mapOperand(Variable var, HardwareRegister reg) {
		icodesReplacement.add(new CAssgn(var, reg));
		return reg;
	}
	
	Operand mapOperand(HardwareRegister srcreg, HardwareRegister dstreg) {
		//icodesReplacement.add(new CAssgn(srcreg, dstreg));
		return srcreg;
	}

	Operand mapOperand(ImmediateOperand iop, HardwareRegister reg) {
		//icodesReplacement.add(new CAssgn(iop, dstreg));
		return iop;
	}
	
	public void visit(ICode icode) {
		throw new RuntimeException("Unhandled icode while mapping virtual registers to temporary variables: " + icode);
	}
	
	public void visit(FunctionContainer oldfuncs) {
		funcReplacement = new FunctionContainer();

		for (Iterator<ICodeContainer> i = oldfuncs.iterator(); i.hasNext();) {
			i.next().acceptVisitor(this);
		}
	}
	
	public void visit(ICodeContainer icodes) {
		icodesReplacement = new ICodeContainer(icodes.getName());
		vregs.clear();
		
		for (Iterator<ICode> i = icodes.iterator(); i.hasNext();) {
			i.next().acceptVisitor(this);
		}
		
		funcReplacement.add(icodesReplacement);
		icodesReplacement = null;
	}
	
	public void visit(CAssgn icode) {
		HardwareRegister indirectionRegister;
		if (icode.getSecondOp().getType().getType() == Definitions.Types.INT) {
			indirectionRegister = new HardwareRegister("%eax", Definitions.Types.INT);
		} else {
			indirectionRegister = new HardwareRegister("%float", Definitions.Types.REAL);
		}
		
		Operand op1 = mapOperand(convertVReg(icode.getFirstOp()), indirectionRegister);
		Operand op2 = convertVReg(icode.getSecondOp());
	
		ICode newICode = new CAssgn(op1, op2);
		icodesReplacement.add(newICode);
	}
	
	public void handleConditionalBranchCode(CBranchCode icode) {
		Operand op1, op2;
		op1 = icode.getFirstOpIfPossible();
		op2 = icode.getSecondOpIfPossible();

		op1 = convertVReg(op1);
		op2 = convertVReg(op2);

		if (op1.getType().getType() == Definitions.Types.INT) {
			icodesReplacement.add(new CAssgn(op1, new HardwareRegister("%eax", Definitions.Types.INT)));
			icodesReplacement.add(new CAssgn(op2, new HardwareRegister("%ebx", Definitions.Types.INT)));
		} else {
			icodesReplacement.add(new CAssgn(op1, new HardwareRegister("%float", Definitions.Types.REAL)));
			icodesReplacement.add(new CAssgn(op2, new HardwareRegister("%float", Definitions.Types.REAL)));
		}
	}
		
	public void visit(CBEQ icode) {
		handleConditionalBranchCode(icode);
		if (icode.getFirstOp().getType().getType() == Definitions.Types.INT) icodesReplacement.add(new CBEQ(new HardwareRegister("%eax", Definitions.Types.INT), new HardwareRegister("%ebx", Definitions.Types.INT), icode.getDestination()));
			else icodesReplacement.add(new CBEQ(new HardwareRegister("%float", Definitions.Types.REAL), new HardwareRegister("%float", Definitions.Types.REAL), icode.getDestination()));
	}

	public void visit(CBGE icode) {
		handleConditionalBranchCode(icode);
		if (icode.getFirstOp().getType().getType() == Definitions.Types.INT) icodesReplacement.add(new CBGE(new HardwareRegister("%eax", Definitions.Types.INT), new HardwareRegister("%ebx", Definitions.Types.INT), icode.getDestination()));
			else icodesReplacement.add(new CBGE(new HardwareRegister("%float", Definitions.Types.REAL), new HardwareRegister("%float", Definitions.Types.REAL), icode.getDestination()));
	}
	
	public void visit(CBGT icode) {
		handleConditionalBranchCode(icode);
		if (icode.getFirstOp().getType().getType() == Definitions.Types.INT) icodesReplacement.add(new CBGT(new HardwareRegister("%eax", Definitions.Types.INT), new HardwareRegister("%ebx", Definitions.Types.INT), icode.getDestination()));
			else icodesReplacement.add(new CBGT(new HardwareRegister("%float", Definitions.Types.REAL), new HardwareRegister("%float", Definitions.Types.REAL), icode.getDestination()));
	}
	
	public void visit(CBLE icode) {
		handleConditionalBranchCode(icode);
		if (icode.getFirstOp().getType().getType() == Definitions.Types.INT) icodesReplacement.add(new CBLE(new HardwareRegister("%eax", Definitions.Types.INT), new HardwareRegister("%ebx", Definitions.Types.INT), icode.getDestination()));
			else icodesReplacement.add(new CBLE(new HardwareRegister("%float", Definitions.Types.REAL), new HardwareRegister("%float", Definitions.Types.REAL), icode.getDestination()));
	}
	
	public void visit(CBLT icode) {
		handleConditionalBranchCode(icode);
		if (icode.getFirstOp().getType().getType() == Definitions.Types.INT) icodesReplacement.add(new CBLT(new HardwareRegister("%eax", Definitions.Types.INT), new HardwareRegister("%ebx", Definitions.Types.INT), icode.getDestination()));
			else icodesReplacement.add(new CBLT(new HardwareRegister("%float", Definitions.Types.REAL), new HardwareRegister("%float", Definitions.Types.REAL), icode.getDestination()));
	}
	
	public void visit(CBNE icode) {
		handleConditionalBranchCode(icode);
		if (icode.getFirstOp().getType().getType() == Definitions.Types.INT) icodesReplacement.add(new CBNE(new HardwareRegister("%eax", Definitions.Types.INT), new HardwareRegister("%ebx", Definitions.Types.INT), icode.getDestination()));
			else icodesReplacement.add(new CBNE(new HardwareRegister("%float", Definitions.Types.REAL), new HardwareRegister("%float", Definitions.Types.REAL), icode.getDestination()));
	}
	
	public void visit(CBRA icode) {
		icodesReplacement.add(icode);
	}
	
	public void visit(CCall icode) {
		Operand op = convertVReg(icode.getReturnValue());
		
		icodesReplacement.add(new CCall(icode.getName(), icode.getArgsize(), op));
	}

	public void moveArithmeticCodeToRegs(CArithmeticCode icode) {
		Operand op1 = convertVReg(icode.getFirstOp());
		Operand op2 = convertVReg(icode.getSecondOp());

		if (op1.getType().getType() == Definitions.Types.INT) {
			icodesReplacement.add(new CAssgn(op1, new HardwareRegister("%ebx", Definitions.Types.INT)));
			icodesReplacement.add(new CAssgn(op2, new HardwareRegister("%eax", Definitions.Types.INT)));
		} else {
			icodesReplacement.add(new CAssgn(op1, new HardwareRegister("%float", Definitions.Types.REAL)));
		}
	}

	public void visit(CAdd icode) {
		Operand target = convertVReg(icode.getTarget());
		
		moveArithmeticCodeToRegs(icode);
		if (icode.getFirstOp().getType().getType() == Definitions.Types.INT) {
			icodesReplacement.add(new CAdd(new HardwareRegister("%eax", Definitions.Types.INT), new HardwareRegister("%ebx", Definitions.Types.INT), new HardwareRegister("%ebx", Definitions.Types.INT)));
			icodesReplacement.add(new CAssgn(new HardwareRegister("%ebx", Definitions.Types.INT), target));
		} else {
			icodesReplacement.add(new CAdd(new HardwareRegister("%float", Definitions.Types.REAL), convertVReg(icode.getSecondOp()), target));
			icodesReplacement.add(new CAssgn(new HardwareRegister("%float", Definitions.Types.REAL), target));
		}
	}
	
	public void visit(CSub icode) {
		Operand target = convertVReg(icode.getTarget());
		
		moveArithmeticCodeToRegs(icode);
		if (icode.getFirstOp().getType().getType() == Definitions.Types.INT) {
			icodesReplacement.add(new CSub(new HardwareRegister("%eax", Definitions.Types.INT), new HardwareRegister("%ebx", Definitions.Types.INT), new HardwareRegister("%ebx", Definitions.Types.INT)));
			icodesReplacement.add(new CAssgn(new HardwareRegister("%ebx", Definitions.Types.INT), target));
		} else {
			icodesReplacement.add(new CSub(new HardwareRegister("%float", Definitions.Types.REAL), convertVReg(icode.getSecondOp()), target));
			icodesReplacement.add(new CAssgn(new HardwareRegister("%float", Definitions.Types.REAL), target));
		}
	}

	public void visit(CMul icode) {
		Operand target = convertVReg(icode.getTarget());
	
		moveArithmeticCodeToRegs(icode);
		if (icode.getFirstOp().getType().getType() == Definitions.Types.INT) {
			icodesReplacement.add(new CMul(new HardwareRegister("%eax", Definitions.Types.INT), new HardwareRegister("%ebx", Definitions.Types.INT), new HardwareRegister("%eax", Definitions.Types.INT)));
			icodesReplacement.add(new CAssgn(new HardwareRegister("%eax", Definitions.Types.INT), target));
		} else {
			icodesReplacement.add(new CMul(new HardwareRegister("%float", Definitions.Types.REAL), convertVReg(icode.getSecondOp()), target));
			icodesReplacement.add(new CAssgn(new HardwareRegister("%float", Definitions.Types.REAL), target));
		}
	}
	
	public void visit(CDiv icode) {
		Operand target = convertVReg(icode.getTarget());

		if (icode.getFirstOp().getType().getType() == Definitions.Types.INT) {
			Operand op1 = convertVReg(icode.getFirstOp());
			Operand op2 = convertVReg(icode.getSecondOp());

			icodesReplacement.add(new CAssgn(op1, new HardwareRegister("%eax", Definitions.Types.INT)));
			icodesReplacement.add(new CAssgn(op2, new HardwareRegister("%ebx", Definitions.Types.INT)));

			icodesReplacement.add(new CDiv(new HardwareRegister("%eax", Definitions.Types.INT), new HardwareRegister("%ebx", Definitions.Types.INT), new HardwareRegister("%eax", Definitions.Types.INT)));
			icodesReplacement.add(new CAssgn(new HardwareRegister("%eax", Definitions.Types.INT), target));
		} else {
			moveArithmeticCodeToRegs(icode);
			icodesReplacement.add(new CDiv(new HardwareRegister("%float", Definitions.Types.REAL), convertVReg(icode.getSecondOp()), target));
			icodesReplacement.add(new CAssgn(new HardwareRegister("%float", Definitions.Types.REAL), target));
		}
	}
	
	public void visit(CI2R icode) {
		Operand source = convertVReg(icode.getSourceOp());
		Operand target = convertVReg(icode.getDestinationOp());
		
		icodesReplacement.add(new CI2R(source, target));
		icodesReplacement.add(new CAssgn(new HardwareRegister("%float", Definitions.Types.REAL), target));
	}

	public void visit(CLabel icode) {
		icodesReplacement.add(icode);
	}
	
	public void visit(CLoad icode) {
		// target := base[index];
		Operand base = convertVReg(icode.getBase());
		Operand target = convertVReg(icode.getTarget());
		Operand index = mapOperand(convertVReg(icode.getIndex()), new HardwareRegister("%eax", Definitions.Types.INT));
		
		icodesReplacement.add(new CLoad(base, index, target));
	}
	
	public void visit(CPush icode) {
		Operand op;
		if (icode.getOperand().getType().getType() == Definitions.Types.INT) {
			op = mapOperand(convertVReg(icode.getOperand()), new HardwareRegister("%eax", Definitions.Types.INT));
		} else {
			op = mapOperand(convertVReg(icode.getOperand()), new HardwareRegister("%float", Definitions.Types.REAL));
		}
		
		icodesReplacement.add(new CPush(op));
	}
	
	public void visit(CR2I icode) {
		Operand source = convertVReg(icode.getSourceOp());
		Operand target = convertVReg(icode.getDestinationOp());
		
		icodesReplacement.add(new CR2I(source, target));
	}
	
	public void visit(CRet icode) {
		icodesReplacement.add(icode);
	}
	
	public void visit(CStore icode) {
		HardwareRegister indirectionRegister;

		if (icode.getSource().getType().getType() == Definitions.Types.INT) {
			indirectionRegister = new HardwareRegister("%eax", Definitions.Types.INT);
		} else {
			indirectionRegister = new HardwareRegister("%float", Definitions.Types.REAL);
		}
		
		// target[index] := source;
		Operand source = mapOperand(convertVReg(icode.getSource()), indirectionRegister);
		Operand target = convertVReg(icode.getTarget());
		Operand index = mapOperand(convertVReg(icode.getIndex()), new HardwareRegister("%ecx", Definitions.Types.INT));

		icodesReplacement.add(new CStore(source, target, index));
	}
	
	public void visit(CVar icode) {
		icodesReplacement.add(icode);
	}

	public void visit(CDBG icode) {
		icodesReplacement.add(icode);
	}
	
	public void visit(CNop icode) {
		icodesReplacement.add(icode);
	}
	
}
