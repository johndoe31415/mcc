public class CallNode extends ExpressionNode {
	
	public CallNode(IdentifierNode id, ArgumentsNode args) {
		link(id);
		if (args != null)
			link(args);
	}

	public IdentifierNode getIdentifierNode() {
		return (IdentifierNode)children.getFirst();
	}

	public ArgumentsNode getArgumentsNode() {
		if (degree() <= 1) {
			return null;
		}
		
		return (ArgumentsNode)children.getLast();
	}

	public String toString() {
		return "call";
	}

	public void acceptVisitor(TreeNodeVisitor visitor) {
		visitor.visit(this);
	}

	public Definitions.Types getType() {
		FunctionDeclarationNode fd = GlobalFunctionTable.get().lookup(getIdentifierNode().getIdentifier());
		return fd.getTypeNode().getType();
	}
}
