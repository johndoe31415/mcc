public class AssignNode extends TreeNodeImpl {
	protected Definitions.Types types[] = new Definitions.Types[2];
	protected boolean typesvalid[] = new boolean[2];
	
	public AssignNode(LValueNode lval, ExpressionNode expr) {
		link(lval);
		link(expr);
	}

	public LValueNode getLValueNode() {
		return (LValueNode)children.getFirst();
	}

	public TreeNode getExpressionOrCastNode() {
		return children.getLast();
	}

	public String toString() {
		return ":=";
	}
	
	private boolean allTypesValid() {
		boolean valid=true;
		for (int i=0; i < 2; i++) valid=valid&&typesvalid[i];
		return valid;
	}

	private boolean allTypesEqual() {
		return types[0] == types[1];
	}

	public void yourChildHasType(TreeNode child, Definitions.Types t) {
		int mychild = this.getChildren().indexOf(child);
		if (mychild == -1)
			throw new RuntimeException("Compiler Bug.");
		
		types[mychild] = t;
		typesvalid[mychild] = true;
		if (allTypesValid()) {
			if (allTypesEqual()) {
				/* No cast necessary */
				getParent().yourChildHasType(this, types[0]);
			} else {
				/* Cast to real */
				if (types[0] == Definitions.Types.INT) {
					CastRealIntNode newnode;
					newnode=new CastRealIntNode();
					newnode.parent=this;
					newnode.getChildren().add(getChildren().get(1));
					((TreeNodeImpl)(getChildren().get(1))).parent = newnode;
					getChildren().set(1, newnode);
					getParent().yourChildHasType(this, Definitions.Types.INT);
				} else {
					CastIntRealNode newnode;
					newnode=new CastIntRealNode();
					newnode.parent=this;
					newnode.getChildren().add(getChildren().get(1));
					((TreeNodeImpl)(getChildren().get(1))).parent = newnode;
					getChildren().set(1, newnode);
					getParent().yourChildHasType(this, Definitions.Types.REAL);
				}
			}
		}
	}

	public void acceptVisitor(TreeNodeVisitor visitor) {
		visitor.visit(this);
	}
}
