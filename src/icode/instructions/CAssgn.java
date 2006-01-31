public class CAssgn extends CVariableCode {
	Operand op1;
	Operand op2;
	
	public CAssgn(Operand op1, Operand op2) {
		this.op1 = op1;
		this.op2 = op2;

		if (op1.getType().getType() != op2.getType().getType()) {
			throw new RuntimeException("May not create CAssgn with differentely typed Operands: " + op1 + " and " + op2 + " (" + op1.getType().getType() + " and " + op2.getType().getType() + ")");
		}
	}
	
	public void acceptVisitor(CILVisitor visitor) {
		visitor.visit(this);
	}

	public String toString() {
		return "CASSGN " + op1 + ", " + op2 + "\t# " + op2 + " := " + op1;
	}

	public Operand getFirstOp() {
		return op1;
	}
	
	public Operand getSecondOp() {
		return op2;
	}
}
