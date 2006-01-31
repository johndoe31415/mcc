public class CRet extends CFunctionCode {
	public void acceptVisitor(CILVisitor visitor) {
		visitor.visit(this);
	}

	public String toString() {
		return "CRET";
	}
}
