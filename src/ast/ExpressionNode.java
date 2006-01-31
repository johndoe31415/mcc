public class ExpressionNode extends TreeNodeImpl {
	protected Definitions.ArithOperations op;
	protected Definitions.Types types[] = new Definitions.Types[2];
	protected boolean typesvalid[] = new boolean[2];

	public ExpressionNode() {
	}
	
	public ExpressionNode(ExpressionNode copy) {
		op=copy.op;
		types[0]=copy.types[0];
		types[1]=copy.types[1];
		typesvalid[0]=copy.typesvalid[0];
		typesvalid[1]=copy.typesvalid[1];
	}

	public ExpressionNode(ExpressionNode leftexpr, Yytoken op, ExpressionNode rightexpr) {
		link(leftexpr);
		link(rightexpr);
		
		if (op.text().equals("+")) this.op = Definitions.ArithOperations.ADD;
		else if (op.text().equals("-")) this.op = Definitions.ArithOperations.SUBTRACT;
		else if (op.text().equals("*")) this.op = Definitions.ArithOperations.MULTIPLY;
		else if (op.text().equals("/")) this.op = Definitions.ArithOperations.DIVIDE;
		else if (op.text().equals("%")) this.op = Definitions.ArithOperations.MODULO;
		else throw new RuntimeException("ExpressionNode got invalid operator '"+op.text()+"'.");
	}

	public TreeNode getLeftOperand() {
		return children.getFirst();
	}

	public TreeNode getRightOperand() {
		return children.getLast();
	}

	private boolean allTypesValid() {
		boolean valid=true;
		for (int i=0; i<2; i++) valid=valid&&typesvalid[i];
		return valid;
	}

	private boolean allTypesEqual() {
		return types[0]==types[1];
	}

	public void yourChildHasType(TreeNode child, Definitions.Types t) {
		int mychild=this.getChildren().indexOf(child);
		if (mychild==-1) throw new RuntimeException("Compiler Bug.");
		
		types[mychild]=t;
		typesvalid[mychild]=true;
		if (allTypesValid()) {
			if (allTypesEqual()) {
				/* No cast necessary */
				getParent().yourChildHasType(this, types[0]);
			} else {
				/* Cast to real */
				CastIntRealNode newnode;
				newnode=new CastIntRealNode();
				newnode.parent=this;
				if (types[0]==Definitions.Types.INT) {
					/* Cast left node to real */
					newnode.getChildren().add(getChildren().get(0));
					((TreeNodeImpl)(getChildren().get(0))).parent=newnode;
					getChildren().set(0, newnode);
				} else {
					/* Cast right node to real */
					newnode.getChildren().add(getChildren().get(1));
					((TreeNodeImpl)(getChildren().get(1))).parent=newnode;
					getChildren().set(1, newnode);
				}
				getParent().yourChildHasType(this, Definitions.Types.REAL);
			}
		}
	}

	public Definitions.Types getType() {
		if (!allTypesValid()) throw new RuntimeException("Not all types known in expression!");
		if (allTypesEqual()) return types[0];
		return Definitions.Types.REAL;
	}

	public float getREAL() {
		float lval, rval;
		if (getChildren().get(0) instanceof CastIntRealNode) lval=((CastIntRealNode)(getChildren().get(0))).getValue();
		else if (getChildren().get(0) instanceof ConstantNode) lval=((ConstantNode)(getChildren().get(0))).getREAL();
		else if (getChildren().get(0) instanceof ExpressionNode) lval=((ExpressionNode)(getChildren().get(0))).getREAL();
		else throw new RuntimeException("Left real side of expression not evaluatable at compiletime: "+this);
		if (getChildren().get(1) instanceof CastIntRealNode) rval=((CastIntRealNode)(getChildren().get(1))).getValue();
		else if (getChildren().get(1) instanceof ConstantNode) rval=((ConstantNode)(getChildren().get(1))).getREAL();
		else if (getChildren().get(1) instanceof ExpressionNode) rval=((ExpressionNode)(getChildren().get(1))).getREAL();
		else throw new RuntimeException("Right real side of expression not evaluatable at compiletime: "+this);
		if (op==Definitions.ArithOperations.ADD) return lval+rval;
		else if (op==Definitions.ArithOperations.SUBTRACT) return lval-rval;
		else if (op==Definitions.ArithOperations.MULTIPLY) return lval*rval;
		else if (op==Definitions.ArithOperations.DIVIDE) return lval/rval;
		else if (op==Definitions.ArithOperations.MODULO) throw new RuntimeException("Modulo not legal with REAL type.");
		return 0;
	}

	public int getINT() {
		int lval, rval;
		if (getChildren().get(0) instanceof CastRealIntNode) lval=((CastRealIntNode)(getChildren().get(0))).getValue();
		else if (getChildren().get(0) instanceof ConstantNode) lval=((ConstantNode)(getChildren().get(0))).getINT();
		else if (getChildren().get(0) instanceof ExpressionNode) lval=((ExpressionNode)(getChildren().get(0))).getINT();
		else throw new RuntimeException("Left integer side of expression not evaluatable at compiletime: "+this);
		if (getChildren().get(1) instanceof CastRealIntNode) rval=((CastRealIntNode)(getChildren().get(1))).getValue();
		else if (getChildren().get(1) instanceof ConstantNode) rval=((ConstantNode)(getChildren().get(1))).getINT();
		else if (getChildren().get(1) instanceof ExpressionNode) rval=((ExpressionNode)(getChildren().get(1))).getINT();
		else throw new RuntimeException("Right integer side of expression not evaluatable at compiletime: "+this);
		if (op==Definitions.ArithOperations.ADD) return lval+rval;
		else if (op==Definitions.ArithOperations.SUBTRACT) return lval-rval;
		else if (op==Definitions.ArithOperations.MULTIPLY) return lval*rval;
		else if (op==Definitions.ArithOperations.DIVIDE) return lval/rval;
		else if (op==Definitions.ArithOperations.MODULO) return lval%rval;
		return 0;
	}

	public Definitions.ArithOperations getOperation() {
		return op;
	}
	
	public String toString() {
		return Definitions.enumToString(op);
	}

	public void acceptVisitor(TreeNodeVisitor visitor) {
		visitor.visit(this);
	}
}
