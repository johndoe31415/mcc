public class ProgramNode extends TreeNodeImpl {
	
	public ProgramNode() {
	}

	
	public String toString() {
		return "program";
	}

	public void acceptVisitor(TreeNodeVisitor visitor) {
		visitor.visit(this);
	}
}
