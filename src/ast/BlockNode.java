public class BlockNode extends TreeNodeImpl {
	protected Block block;
	
	public BlockNode() {
		block = null;
	}

	public String toString() {
		return "block";
	}

	public void setBlock(Block block) {
		this.block = block;
	}
	
	public Block getBlock() {
		return block;
	}

	public Variable searchVariable(String name) {
		return block.searchVariable(name);
	}

	public void acceptVisitor(TreeNodeVisitor visitor) {
		visitor.visit(this);
	}
}
