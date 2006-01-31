import java.util.Vector;
import java.util.Iterator;

public class FunctionDeclarationNode extends TreeNodeImpl {
	protected TypeNode type;
	protected IdentifierNode id;
	protected Block block;
	
	public FunctionDeclarationNode(TypeNode type, IdentifierNode id, ParametersNode pars, BlockNode block) {
		this.type = type;
		this.id = id;

		if (pars != null) {
			link(pars);
		}
		link(block);
	}

	public TypeNode getTypeNode() {
		return type;
	}

	public IdentifierNode getIdentifierNode() {
		return id;
	}

	public ParametersNode getParameterNode() {
		if (degree() < 2)
			return null;
		else
			return (ParametersNode)children.getFirst();
	}

	public BlockNode getBlockNode() {
		return (BlockNode)children.getLast();
	}
	
	public String toString() {
		return "function\\ntype = " + type + "\\nid = " + id;
	}

	public void acceptVisitor(TreeNodeVisitor visitor) {
		visitor.visit(this);
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
		//getBlockNode().setBlock(block);
	}
}
