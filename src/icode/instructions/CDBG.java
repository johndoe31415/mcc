public class CDBG extends ICode {
	protected String msg;
	public CDBG(String msg) {
		this.msg = msg;
	}
	
	public void acceptVisitor(CILVisitor visitor) {
		visitor.visit(this);
	}

	public String toString() {
		return "# " + msg;
	}
}
