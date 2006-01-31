import java.util.*;

public class Symboltable {
	Vector<Block> blocks;
	Block currentBlock;
	Block rootBlock;
	int currentDepth;
	
	public Symboltable() {
		blocks = new Vector<Block>();
		rootBlock = new Block(currentDepth, null);
		currentBlock = rootBlock;
		blocks.add(currentBlock);
		currentDepth = 0;
	}
	
	public Block enter() {
		currentDepth++;
		currentBlock = new Block(currentDepth, currentBlock);
		blocks.add(currentBlock);
		return currentBlock;
	}

	public void leave() {
		currentDepth--;
		currentBlock = currentBlock.getParent();
	}

	public void add(Variable v) {
		v.setDepth(currentDepth);
		currentBlock.addVariable(v);
	}

	public Variable search(String name) {
		return currentBlock.searchVariable(name);
	}

	public void display() {
		for (Iterator<Block> i = blocks.iterator(); i.hasNext();) {
			Block b = i.next();
			b.display();
		}
	}
};
