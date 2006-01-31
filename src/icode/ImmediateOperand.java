public class ImmediateOperand extends Operand {
	Constant constant;

	public ImmediateOperand(String name, Constant constant) {
		super(name, new Type(constant.getType()));
		this.constant = constant;
	}
	
	public Constant getConstant() {
		return constant;
	}

	public String toString() {
		return constant.toString();
	}
}
