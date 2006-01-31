import java.util.*;


public interface TreeNode extends Iterable<TreeNode> {

	/**
	 * Calls the visitor's visit() method which enables the visitor to use
	 * Javas automatical dispatching to differentiate between different node
	 * types.
	 *
	 * @param	visitor	The visitor which gets called.
	 */
	
	public void acceptVisitor(TreeNodeVisitor visitor);
	
	
	/**
	 * Adds the specified node to the children of this node.
	 * 
	 * @param	node	The node to add to the children.
	 * @return	Returns false if such a link already existed.
	 */

	public boolean link(TreeNode node);
	
	public boolean unlink(TreeNode node);

	/**
	 * Validates that a node may have a link on this one and sets the parent
	 * node.
	 *
	 * @param	node	The node which would like to point to this node.
	 * @return	Returns true if the link got accepted.
	 */

	public boolean acceptLink(TreeNode node);

	public boolean acceptUnlink(TreeNode node);

	/**
	 * Returns a reference to a node pointing to this one. If this node has
	 * no incomming edges null is returned.
	 *
	 * @return	Returns the parent node or null if none present.
	 */

	public TreeNode getParent();

	public LinkedList<TreeNode> getChildren();

	/**
	 * Returns the nodes degree.
	 *
	 * @return 	The nodes degree.
	 */

	public int degree();

	
	/**
	 * Add the node's children to this node.
	 *
	 * @param	node	The tree to add.
	 */
	
	public void join(TreeNode node);

	public void yourChildHasType(TreeNode child, Definitions.Types t);

	public void replaceBy(TreeNode node);

};
