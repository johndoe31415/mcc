public class CompareNode extends ConditionNode {
	Definitions.CmpOperations op;
	
	public CompareNode(ExpressionNode leftexpr, Yytoken op, ExpressionNode rightexpr) {
		if (op.text().equals("=")) this.op = Definitions.CmpOperations.EQ;
		else if (op.text().equals("!=")) this.op = Definitions.CmpOperations.NEQ;
		else if (op.text().equals("<")) this.op = Definitions.CmpOperations.LT;
		else if (op.text().equals(">")) this.op = Definitions.CmpOperations.GT;
		else if (op.text().equals("<=")) this.op = Definitions.CmpOperations.LE;
		else if (op.text().equals(">=")) this.op = Definitions.CmpOperations.GE;
		else throw new RuntimeException("CompareNode got invalid operator '"+op.text()+"'.");
			
		link(leftexpr);
		link(rightexpr);
	}

	public Definitions.CmpOperations getCmpOperation() {
		return op;
	}
	
	public Definitions.CmpOperations getInvertedCmpOperation() {
		switch (getCmpOperation()) {
			case LT:
				return Definitions.CmpOperations.GE;
			case GT:
				return Definitions.CmpOperations.LE;
			case LE:
				return Definitions.CmpOperations.GT;
			case GE:
				return Definitions.CmpOperations.LT;
			case EQ:
				return Definitions.CmpOperations.NEQ;
			case NEQ:
				return Definitions.CmpOperations.EQ;
			default:
				throw new RuntimeException("Invalid compare operation!");
		}
	}

	public ExpressionNode getLeftExpressionNode() {
		return (ExpressionNode)children.getFirst();
	}

	public ExpressionNode getRightExpressionNode() {
		return (ExpressionNode)children.getLast();
	}

	public String toString() {
		return Definitions.enumToString(op);
	}

	public void acceptVisitor(TreeNodeVisitor visitor) {
		visitor.visit(this);
	}
}
