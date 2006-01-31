public class ArgumentsNode extends TreeNodeImpl {
	
	public ArgumentsNode() {
	}

	public String toString() {
		return "arguments";
	}

	public void acceptVisitor(TreeNodeVisitor visitor) {
		visitor.visit(this);
	}
}
