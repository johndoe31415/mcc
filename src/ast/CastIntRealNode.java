public class CastIntRealNode extends TreeNodeImpl {
	public CastIntRealNode() {
	}

	public float getValue() {
		TreeNodeImpl child;
		child=(TreeNodeImpl)(((TreeNodeImpl)(getChildren()).get(0)));
		if (child instanceof ConstantNode) {
			ConstantNode cchild = (ConstantNode)child;
			if (cchild.getType()==Definitions.Types.INT) return (float)cchild.getINT();
			else return cchild.getREAL();
		}			
		if (child instanceof ExpressionNode) {
			ExpressionNode echild = (ExpressionNode)child;
			return (float)echild.getINT();
		}
		throw new RuntimeException("CastIntRealNode without either Constant or Expression node as child!");
	}

	public String toString() {
		return "cast to real";
	}

	public void acceptVisitor(TreeNodeVisitor visitor) {
		visitor.visit(this);
	}
}
