public class ConditionNode extends TreeNodeImpl {
	protected Definitions.BoolOperations op;

	public ConditionNode() {
	}
	
	public ConditionNode(ConditionNode leftcond, Yytoken op, ConditionNode rightcond) {
		if (op.text().equals("||")) {
			this.op=Definitions.BoolOperations.OR;
		}
		else if (op.text().equals("&&")) {
			this.op=Definitions.BoolOperations.AND;
		}
		else {
			throw new RuntimeException("Invalid boolean operand '" + op.text() + "'.");
		}

		link(leftcond);
		link(rightcond);
	}

	public Definitions.BoolOperations getBoolOperation() {
		return op;
	}

	public ConditionNode getLeftCondition() {
		return (ConditionNode)children.getFirst();
	}

	public ConditionNode getRightCondition() {
		return (ConditionNode)children.getLast();
	}
	
	public String toString() {
		return Definitions.enumToString(op);
	}

	public void acceptVisitor(TreeNodeVisitor visitor) {
		visitor.visit(this);
	}
}
