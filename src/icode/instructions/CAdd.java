public class CAdd extends CArithmeticCode {
	public CAdd(Operand op1, Operand op2, Operand target) {
		this.op1 = op1;
		this.op2 = op2;
		this.target = target;
	}
	
	public void acceptVisitor(CILVisitor visitor) {
		visitor.visit(this);
	}

	public String toString() {
		return "CADD " + op1 + ", " + op2 + ", " + target;
	}
}
