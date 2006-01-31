public abstract class CILVisitorImpl implements CILVisitor {
	public abstract void visit(ICode icode);
	public abstract void visit(FunctionContainer funcs);
	public abstract void visit(ICodeContainer icodes);
	
	public void visit(CAdd icode) {
		visit((CArithmeticCode)icode);
	}
	
	public void visit(CArithmeticCode icode) {
		visit((ICode)icode);
	}
	
	public void visit(CAssgn icode) {
		visit((CVariableCode)icode);
	}

	public void visit(CBEQ icode) {
		visit((CBranchCode)icode);
	}

	public void visit(CBGE icode) {
		visit((CBranchCode)icode);
	}
	
	public void visit(CBGT icode) {
		visit((CBranchCode)icode);
	}
	
	public void visit(CBLE icode) {
		visit((CBranchCode)icode);
	}
	
	public void visit(CBLT icode) {
		visit((CBranchCode)icode);
	}
	
	public void visit(CBNE icode) {
		visit((CBranchCode)icode);
	}
	
	public void visit(CBRA icode) {
		visit((CBranchCode)icode);
	}
	
	public void visit(CBranchCode icode) {
		visit((ICode)icode);
	}
	
	public void visit(CCall icode) {
		visit((CFunctionCode)icode);
	}
	
	public void visit(CDiv icode) {
		visit((CArithmeticCode)icode);
	}

	public void visit(CFunctionCode icode) {
		visit((ICode)icode);
	}
	
	public void visit(CI2R icode) {
		visit((CTypeConversionCode)icode);
	}

	public void visit(CLabel icode) {
		visit((ICode)icode);
	}
	
	public void visit(CLoad icode) {
		visit((CVariableCode)icode);
	}
	
	public void visit(CMul icode) {
		visit((CArithmeticCode)icode);
	}
	
	public void visit(CPush icode) {
		visit((CFunctionCode)icode);
	}
	
	public void visit(CR2I icode) {
		visit((CTypeConversionCode)icode);
	}
	
	public void visit(CRet icode) {
		visit((CFunctionCode)icode);
	}
	
	public void visit(CStore icode) {
		visit((CVariableCode)icode);
	}
	
	public void visit(CSub icode) {
		visit((CArithmeticCode)icode);
	}
	
	public void visit(CTypeConversionCode icode) {
		visit((ICode)icode);
	}

	public void visit(CVariableCode icode) {
		visit((ICode)icode);
	}

	public void visit(CVar icode) {
		visit((CVariableCode)icode);
	}
	
	public void visit(CDBG icode) {
		visit((ICode)icode);
	}
	
	public void visit(CNop icode) {
		visit((ICode)icode);
	}
}
