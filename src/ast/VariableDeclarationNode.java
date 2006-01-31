import java.util.Vector;
import java.util.Iterator;

public class VariableDeclarationNode extends TreeNodeImpl {
	protected IdentifierNode id;
	/// @todo actually we should remove type here
	protected TypeNode type;
	
	public VariableDeclarationNode(TypeNode type, IdentifierNode id) {
		this.id = id;
		this.type = type;
		
		join(type);
	}

	public TypeNode getTypeNode() {
		return type;
	}

	public IdentifierNode getIdentifierNode() {
		return id;
	}
	
	public String toString() {
		return "variable\\ntype = " + getTypeNode() + "\\nid = " + id;
	}

	public void acceptVisitor(TreeNodeVisitor visitor) {
		visitor.visit(this);
	}

	public Vector<Integer> getDimensions() {
		Vector<Integer> ret = new Vector<Integer>(degree());

		for (Iterator<TreeNode> it = iterator(); it.hasNext();) {
			TreeNode node = it.next();
			if (node instanceof ConstantNode) {
				ConstantNode cnode = (ConstantNode)node;
				if (cnode.getType() == Definitions.Types.REAL) {
					throw new RuntimeException("Array dimensions must be of type int.");
				}
					
				ret.add(cnode.getINT());
			}
			else
				throw new RuntimeException("Array dimensions must be constant (but were found to be of type '" + node + "').");
		}

		//System.err.println("cnode " + ret.size());
		//System.err.println("degree " + degree());
		
		return ret;
	}
}
