public class ICode {
	public void acceptVisitor(CILVisitor visitor) {
		visitor.visit(this);
	}
}
