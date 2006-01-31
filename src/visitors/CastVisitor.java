import java.util.Iterator;

public class CastVisitor extends TreeNodeVisitorImpl {
	
	public CastVisitor() {
	}
	
	public void insertCastNodes(TreeNode root) {
		root.acceptVisitor(this);
	}
	
	private void continueTraversal(TreeNode node) {
		for (Iterator<TreeNode> i = node.iterator(); i.hasNext();)
			i.next().acceptVisitor(this);
	}
	
	public void visit(TreeNode node) {
		continueTraversal(node);
	}
	
	public void visit(ConstantNode node) {
		if (node.degree() == 0) {
			node.getParent().yourChildHasType(node, node.getType());
		}
		continueTraversal(node);
	}
	
	public void visit(LValueNode node) {
		if (node.degree() == 0) {
			node.getParent().yourChildHasType(node, node.getType());
		}		
	
		continueTraversal(node);
	}
	
	public void visit(CallNode node) {
		node.getParent().yourChildHasType(node, node.getType());
		continueTraversal(node);
	}
}
