import java.util.Iterator;

public class CheckAST extends TreeNodeVisitorImpl {
	int Errors;
	int ProgramNodes;
	
	
	public CheckAST() {
	}
	
	
	public void checkAST(TreeNode root) {
		Errors=0;
		ProgramNodes=0;
		root.acceptVisitor(this);
		System.out.println("Checking of tree consistency done, " + Errors + " errors found.");
	}
	
	
	private void continueTraversal(TreeNode node) {
		for (Iterator<TreeNode> i = node.iterator(); i.hasNext();)
			i.next().acceptVisitor(this);
	}
	
	
	public void visit(TreeNode node) {
		if (node.getParent() == null) {
			System.out.println("Error: Node '" + node + "' has NULL parent.");
			Errors++;
		}
		else {
			boolean valid = false;
			for (Iterator<TreeNode> it = node.getParent().iterator(); it.hasNext();)
				if (it.next() == node) {
					valid = true;
					break;
				}

			if (!valid) {
				System.out.println("Error: Node '" + node + "' has wrong parent.");
				Errors++;
			}
		}
		
		continueTraversal(node);
	}
	

	public void visit(ProgramNode node) {
		ProgramNodes++;
		
		if (ProgramNodes > 1) {
			System.out.println("Error: Node '" + node + "' claims to be ProgramNode (" + ProgramNodes + " PN total).");
			Errors++;
		}
		
		continueTraversal(node);
	}
}
