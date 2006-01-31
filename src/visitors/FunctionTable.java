import java.util.*;

public class FunctionTable extends TreeNodeVisitorImpl {
	private HashMap<String, FunctionDeclarationNode> table;
	
	public FunctionTable(TreeNode root) {
		table = new HashMap<String, FunctionDeclarationNode>();
		root.acceptVisitor(this);
	}

	private void continueTraversal(TreeNode node) {
		for (Iterator<TreeNode> i = node.iterator(); i.hasNext();)
			i.next().acceptVisitor(this);
	}
	
	public void visit(TreeNode node) {
		continueTraversal(node);
	}
	
	public void visit(FunctionDeclarationNode node) {
		table.put(node.getIdentifierNode().getIdentifier(), node);
		continueTraversal(node);
	}

	public FunctionDeclarationNode lookup(String id) {
		return table.get(id);
	}
}
