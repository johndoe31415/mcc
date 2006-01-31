import java.util.LinkedList;
import java.util.Iterator;


public class TreeNodeImpl implements TreeNode {
	TreeNode parent;
	protected LinkedList<TreeNode> children;
	
	public TreeNodeImpl() {
		children = new LinkedList<TreeNode>();
		parent = null;
	}
	
	public Iterator<TreeNode> iterator() {
		return children.iterator();
	}

	public void acceptVisitor(TreeNodeVisitor visitor) {
		visitor.visit(this);
	}

	public int degree() {
		return children.size();
	}

	public boolean link(TreeNode node) {
		if (children.contains(node) || !node.acceptLink(this))
			return false;
		
		children.add(node);
		return true;
	}

	public boolean unlink(TreeNode node) {
		if (!acceptUnlink(node))
			return false;
		
		return children.remove(node);
	}

	public boolean acceptLink(TreeNode node) {
		/*if (getParent() != null)
			return false;
		*/
		parent = node;

		return true;
	}

	public boolean acceptUnlink(TreeNode node) {
		/*if (parent != node)
			return false;
		*/
		parent = null;
		
		return true;
	}

	public TreeNode getParent() {
		return parent;
	}
	
	public LinkedList<TreeNode> getChildren() {
		return children;
	}

	public void join(TreeNode node) {
		for (Iterator<TreeNode> i = node.iterator(); i.hasNext();) {
			TreeNode ref = i.next();
			i.remove();
			link(ref);
		}
	}

	public void yourChildHasType(TreeNode child, Definitions.Types t) {
	}

	public void replaceBy(TreeNode newnode) {
		TreeNodeImpl onode, nnode, oparent;
		onode=(TreeNodeImpl)this;
		nnode=(TreeNodeImpl)newnode;
		oparent=(TreeNodeImpl)onode.parent;
		nnode.parent=oparent;
		int idx = oparent.getChildren().indexOf(onode);
		if (idx!=-1) {
			oparent.getChildren().set(idx, newnode);
		} else {
			throw new RuntimeException("Node replacement failed, child wasn't found: check tree consistency.");
		}
	}
}
