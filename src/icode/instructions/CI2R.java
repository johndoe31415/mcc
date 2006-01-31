public class CI2R extends CTypeConversionCode {
	protected Operand src, dst;
	
	public CI2R(Operand src, Operand dst) {
		this.src = src;
		this.dst = dst;
	}

	public Operand getSourceOp() {
		return src;
	}
	
	public Operand getDestinationOp() {
		return dst;
	}
	
	public void acceptVisitor(CILVisitor visitor) {
		visitor.visit(this);
	}

	public String toString() {
		return "Cast INT => REAL";
	}
}
