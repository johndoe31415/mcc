import java.util.*;

public class CreateSymboltable extends TreeNodeVisitorImpl {
	private Symboltable s;
	private boolean dryRun;
	
	public CreateSymboltable(boolean dryRun) {
		s = new Symboltable();
		this.dryRun = dryRun;
	}
	
	public Symboltable generate(TreeNode root) {
		root.acceptVisitor(this);
		return s;
	}
	
	public void visit(TreeNode node) {
		boolean BeginsBlock;

		BeginsBlock=(node instanceof BlockNode) || (node instanceof FunctionDeclarationNode);
		if (node instanceof VariableDeclarationNode) {
			VariableDeclarationNode vardeclnode = (VariableDeclarationNode)node;
			
			Vector<Integer> dimensions;
			if (dryRun) {
				dimensions = new Vector<Integer>();
			} else {
				dimensions = vardeclnode.getDimensions();
			}
			
			s.add(new Variable(vardeclnode.getIdentifierNode().toString(), new Type(vardeclnode.getTypeNode()), -1, dimensions));
		}
		if (node instanceof FunctionDeclarationNode) {
			s.add(new Variable(((FunctionDeclarationNode)node).getIdentifierNode().toString(), new Type(((FunctionDeclarationNode)node).getTypeNode()), -1, new Vector<Integer>()));
		}
		
		if (BeginsBlock) {
			Block newBlock = s.enter();
			if (node instanceof BlockNode) ((BlockNode)node).setBlock(newBlock);
			if (node instanceof FunctionDeclarationNode) ((FunctionDeclarationNode)node).setBlock(newBlock);
		}
		for (Iterator<TreeNode> i = node.iterator(); i.hasNext();) {
			i.next().acceptVisitor(this);
		}

		Variable v;
		String id;
		if (node instanceof LValueNode) {
			LValueNode n;
			n=(LValueNode)node;
			id=n.getIdentifier();
			v=s.search(id);
			if (v==null) throw new RuntimeException("Variable LValue references by '" + id + "' undeclared.");
			n.setVariable(v);
		}
			
		if (BeginsBlock) s.leave();
	}
}
