public class ReturnNode extends TreeNodeImpl {
	protected ExpressionNode expr;
	
	public ReturnNode(ExpressionNode expr) {
		this.expr = expr;

		link(expr);
	}

	public String toString() {
		return "return";
	}

	public void acceptVisitor(TreeNodeVisitor visitor) {
		visitor.visit(this);
	}
}
