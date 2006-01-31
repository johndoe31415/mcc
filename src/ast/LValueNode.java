import java.lang.*;
import java.util.*;

public class LValueNode extends ExpressionNode {
	IdentifierNode id;
	Variable v;
	Vector<Boolean> childTypesOkay;
	
	public LValueNode(IdentifierNode id) {
		this.id = id;
		v = null;
		childTypesOkay = null;
	}

	private void initChildTypesOkay() {
		if (childTypesOkay!=null) return;
		childTypesOkay = new Vector<Boolean>();
		for (int i=0; i<degree(); i++) childTypesOkay.add(new Boolean(false));
	}

	public Definitions.Types getType() {
		return v.getType().getType();
	}

	public Variable getVariable() {
		return v;
	}

	public void setVariable(Variable v) {
		this.v=v;
	}

	public String getIdentifier() {
		return id.getIdentifier();
	}

	public String toString() {
		return "lvalue\\nid = " + id;
	}

	public int getINT() {
		throw new RuntimeException("LValue cannot be evaluated at compiletime.");
	}
	
	public float getREAL() {
		throw new RuntimeException("LValue cannot be evaluated at compiletime.");
	}

	public void acceptVisitor(TreeNodeVisitor visitor) {
		visitor.visit(this);
	}

	private boolean allChildTypesOkay() {
		if (childTypesOkay == null) return false;
		for (int i = 0; i < childTypesOkay.size(); i++) {
			if (childTypesOkay.get(i) == false) return false;
		}
		return true;
	}
	
	public void yourChildHasType(TreeNode child, Definitions.Types t) {
		initChildTypesOkay();
		int mychild = this.getChildren().indexOf(child);
		if (mychild == -1) throw new RuntimeException("Compiler Bug.");		
		if (t != Definitions.Types.INT) throw new RuntimeException("Trying to assign an array with index of type 'real' is forbidden.");
		
		childTypesOkay.set(mychild, new Boolean(true));		
		if (allChildTypesOkay()) getParent().yourChildHasType(this, v.getType().getType());
	}
}
