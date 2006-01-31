public class CBLE extends CBranchCode {
	protected Operand op1, op2;

	public CBLE(Operand op1, Operand op2, CLabel destination) {
		super(destination);
		this.op1 = op1;
		this.op2 = op2;
	}
	
	public void acceptVisitor(CILVisitor visitor) {
		visitor.visit(this);
	}

	public String toString() {
		return "CBLE " + getFirstOp() + ", " + getSecondOp() + ", " + getDestination().getName() + "\t# jump if " + getFirstOp() + " <= " + getSecondOp();
	}
	
	public Operand getFirstOp() {
		return op1;
	}
	
	public Operand getSecondOp() {
		return op2;
	}
}
