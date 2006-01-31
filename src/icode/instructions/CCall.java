public class CCall extends CFunctionCode {
	String name;
	Operand returnval;
	int argSize;
	
	public CCall(String name, int argSize, Operand returnval) {
		this.name = name;
		this.returnval = returnval;
		this.argSize = argSize;
	}
	
	public final String getName() {
		return name;
	}

	public int getArgsize() {
		return argSize;
	}

	public Operand getReturnValue() {
		return returnval;
	}
	
	public void acceptVisitor(CILVisitor visitor) {
		visitor.visit(this);
	}

	public String toString() {
		return "CCALL " + name + ", " + argSize + ", " + returnval + "\t# " + returnval + " := " + name + "(...)";
	}
}
