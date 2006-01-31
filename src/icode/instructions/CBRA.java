public class CBRA extends CBranchCode {
	public CBRA(CLabel destination) {
		super(destination);
	}
	
	public void acceptVisitor(CILVisitor visitor) {
		visitor.visit(this);
	}

	public String toString() {
		return "CBRA " + getDestination().getName();
	}
}
