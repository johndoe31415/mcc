public class IfNode extends TreeNodeImpl {
	public IfNode(ConditionNode cond, BlockNode block, BlockNode elseblock) {
		link(cond);
		link(block);
		if (elseblock != null)
			link(elseblock);
	}
	public ConditionNode getCondition() {
		return (ConditionNode)children.getFirst();
	}

	public BlockNode getAcceptBranch() {
		return (BlockNode)children.get(1);
	}

	public BlockNode getRejectBranch() {
		if (children.size() < 3)
			return null;
		else
			return (BlockNode)children.getLast();
	}

	public String toString() {
		return "if";
	}

	public void acceptVisitor(TreeNodeVisitor visitor) {
		visitor.visit(this);
	}
}
