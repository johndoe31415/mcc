public class WhileNode extends TreeNodeImpl {
	public WhileNode(ConditionNode cond, BlockNode block) {
		link(cond);
		link(block);
	}

	ConditionNode getCondition() {
		return (ConditionNode)children.getFirst();
	}

	BlockNode getBlock() {
		return (BlockNode)children.getLast();
	}

	public String toString() {
		return "while";
	}

	public void acceptVisitor(TreeNodeVisitor visitor) {
		visitor.visit(this);
	}
}
