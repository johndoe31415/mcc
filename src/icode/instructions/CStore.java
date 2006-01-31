public class CStore extends CVariableCode {
	protected Operand target, index, source;
	
	public CStore(Operand source, Operand target, Operand index) {
		this.target = target;
		this.source = source;
		this.index = index;
	}
	
	public void acceptVisitor(CILVisitor visitor) {
		visitor.visit(this);
	}

	public String toString() {
		if (index instanceof ImmediateOperand && ((ImmediateOperand)index).getConstant().getValue().equals("0")) {
			return "CSTORE " + source + ", " + target + ", 0\t# " + getTarget() + " := " + getSource();
		}
		else {
			return "CSTORE " + source + ", " + target + ", " + index + "\t# " + getTarget() + "[" + getIndex() + "] := " + getSource();
		}
	}

	public Operand getSource() {
		return source;
	}
	
	public Operand getTarget() {
		return target;
	}
	
	public Operand getIndex() {
		return index;
	}
}
