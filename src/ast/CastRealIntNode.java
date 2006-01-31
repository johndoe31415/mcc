public class CastRealIntNode extends TreeNodeImpl {
	public CastRealIntNode() {
	}
	
	public int getValue() {
		TreeNodeImpl child;
		child=(TreeNodeImpl)(((TreeNodeImpl)(getChildren().get(0))));
		if (child instanceof ConstantNode) {
			ConstantNode cchild = (ConstantNode)child;
			if (cchild.getType()==Definitions.Types.REAL) return (int)cchild.getREAL();
			else return cchild.getINT();
		}			
		if (child instanceof ExpressionNode) {
			ExpressionNode echild = (ExpressionNode)child;
			return (int)echild.getREAL();
		}
		throw new RuntimeException("CastRealIntNode without either Constant or Expression node as child!");
	}
	
	public String toString() {
		return "cast to int";
	}

	public void acceptVisitor(TreeNodeVisitor visitor) {
		visitor.visit(this);
	}
}
