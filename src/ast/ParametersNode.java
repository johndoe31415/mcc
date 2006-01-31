public class ParametersNode extends TreeNodeImpl {
	
	public ParametersNode() {
	}

	public String toString() {
		return "parameters";
	}

	public void acceptVisitor(TreeNodeVisitor visitor) {
		visitor.visit(this);
	}
}
