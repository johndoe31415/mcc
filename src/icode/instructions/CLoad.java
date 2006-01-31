public class CLoad extends CVariableCode {
	protected Operand base, index, target;
	
	public CLoad(Operand base, Operand index, Operand target) {
		this.base = base;
		this.target = target;
		this.index = index;
	}
	
	public void acceptVisitor(CILVisitor visitor) {
		visitor.visit(this);
	}

	public String toString() {
		if (index instanceof ImmediateOperand && ((ImmediateOperand)index).getConstant().getValue().equals("0")) {
			return "CLOAD " + base + ", 0, " + target + "\t# " + getTarget() + " := " + getBase();
		}
		else {
			return "CLOAD " + base + ", " + index + ", " + target + "\t# " + getTarget() + " := " + getBase() + "[" + getIndex() + "]";
		}
	}

	public Operand getBase() {
		return base;
	}
	
	public Operand getTarget() {
		return target;
	}
	
	public Operand getIndex() {
		return index;
	}
}
