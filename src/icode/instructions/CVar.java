public class CVar extends CVariableCode {
	protected Operand operand;

	public CVar(Operand operand) {
		this.operand = operand;
	}
	
	public void acceptVisitor(CILVisitor visitor) {
		visitor.visit(this);
	}

	public Operand getOperand() {
		return operand;
	}

	public String toString() {
		if (getOperand() instanceof Variable) {
			Variable v = (Variable)getOperand();
			return "CVAR " + getOperand() + "\t# allocate variable at stack offset " + v.getOffset();
		}
		else
			return "CVAR " + getOperand();
	}
}
