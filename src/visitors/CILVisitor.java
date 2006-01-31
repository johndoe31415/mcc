public interface CILVisitor {
	public void visit(ICode icode);
	public void visit(FunctionContainer funcs);
	public void visit(ICodeContainer icodes);

	public void visit(CAdd icode);
	public void visit(CArithmeticCode icode);
	public void visit(CAssgn icode);
	public void visit(CBEQ icode);
	public void visit(CBGE icode);
	public void visit(CBGT icode);
	public void visit(CBLE icode);
	public void visit(CBLT icode);
	public void visit(CBNE icode);
	public void visit(CBRA icode);
	public void visit(CBranchCode icode);
	public void visit(CCall icode);
	public void visit(CDiv icode);
	public void visit(CFunctionCode icode);
	public void visit(CI2R icode);
	public void visit(CLabel icode);
	public void visit(CLoad icode);
	public void visit(CMul icode);
	public void visit(CPush icode);
	public void visit(CR2I icode);
	public void visit(CRet icode);
	public void visit(CStore icode);
	public void visit(CSub icode);
	public void visit(CTypeConversionCode icode);
	public void visit(CVariableCode icode);
	public void visit(CVar icode);
	public void visit(CDBG icode);
	public void visit(CNop icode);
}
