public class CLabel extends ICode {
	private String name;
	
	public CLabel(String name) {
		this.name = name;
	}
	
	public void acceptVisitor(CILVisitor visitor) {
		visitor.visit(this);
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return "CLABEL " + name;
	}
}
