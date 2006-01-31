import java.util.Iterator;

public class ConstCast extends TreeNodeVisitorImpl {
	
	public ConstCast() {
	}
	
	
	public void castConstants(TreeNode root) {
		root.acceptVisitor(this);
	}
	
	
	public void continueTraversal(TreeNode node) {
		for (Iterator<TreeNode> i = node.iterator(); i.hasNext();)
			i.next().acceptVisitor(this);
	}
	
	
	public void visit(TreeNode node) {
		continueTraversal(node);
	}
	
	
	public void visit(CastIntRealNode node) {
		if ((node.getChildren().get(0)) instanceof ConstantNode) {				
			ConstantNode oldconst = (ConstantNode)(node.getChildren().get(0));
			ConstantNode newconst = new ConstantNode((float)oldconst.getINT());
			node.replaceBy(newconst);
		}
		
		continueTraversal(node);
	}
	

	public void visit(CastRealIntNode node) {
		if ((node.getChildren().get(0)) instanceof ConstantNode) {
			ConstantNode oldconst = (ConstantNode)(node.getChildren().get(0));
			ConstantNode newconst = new ConstantNode((int)oldconst.getREAL());
			node.replaceBy(newconst);
		}

		continueTraversal(node);
	}
}
