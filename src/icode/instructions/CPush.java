public class CPush extends CFunctionCode {
	protected Operand operand;

	public CPush(Operand o) {
		this.operand = o;
	}
	
	public void acceptVisitor(CILVisitor visitor) {
		visitor.visit(this);
	}

	public String toString() {
		return "CPUSH " + operand;
	}

	public Operand getOperand() {
		return operand;
	}
}
