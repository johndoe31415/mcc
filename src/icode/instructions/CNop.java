public class CNop extends ICode {
	String comment;
	
	CNop(String comment) {
		this.comment = comment;
	}
	
	public void acceptVisitor(CILVisitor visitor) {
		visitor.visit(this);
	}

	public String toString() {
		if (!comment.equals("")) return "CNop " + comment;
			else return "";
	}
}
