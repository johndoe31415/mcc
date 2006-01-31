public class IdentifierNode extends TreeNodeImpl {
	protected String id;
	
	public IdentifierNode(Yytoken id) {
		this.id = id.text();
	}

	public String getIdentifier() {
		return id;
	}

	public String toString() {
		return id;
	}

	public void acceptVisitor(TreeNodeVisitor visitor) {
		visitor.visit(this);
	}
}
