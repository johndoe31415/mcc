import java.util.Iterator;

public class ConstantExprSimplifyAST extends TreeNodeVisitorImpl {
	public ConstantExprSimplifyAST() {
	}
	

	public void simplify(TreeNode root) {
		root.acceptVisitor(this);
	}
	
	
	private void continueTraversal(TreeNode node) {
		for (Iterator<TreeNode> i = node.iterator(); i.hasNext();)
			i.next().acceptVisitor(this);
	}
	

	public void visit(ExpressionNode node) {
		// try to evaluate
		ExpressionNode newnode=null;
		try {
			if (node.getType() == Definitions.Types.INT) {
				newnode = new ConstantNode(node.getINT());
			} else {
				newnode = new ConstantNode(node.getREAL());
			}
		} catch (Exception E) {
//			System.err.println("Result determination failed: "+E);
		}
		
		if (newnode!=null) {
//			System.err.println("Will replace '"+node+"' by '"+newnode+"'");
			node.replaceBy(newnode);
		} else {
			continueTraversal(node);
		}
	}
	
	public void visit(ConstantNode node) {
		continueTraversal(node);
	}
	
	public void visit(LValueNode node) {
		continueTraversal(node);
	}
	
	public void visit(CallNode node) {
		continueTraversal(node);
	}
	
	public void visit(TreeNode node) {
		continueTraversal(node);
	}
}
