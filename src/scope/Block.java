import java.util.*;
import java.io.PrintStream;

public class Block implements Iterable<Variable> {
	int depth;
	Block parent;
	Vector<Variable> variables;
	static int number;
	int blockNumber;

	public Block(int depth, Block parent) {
		this.depth = depth;
		this.parent = parent;
		variables = new Vector<Variable>();
		blockNumber = number++;
	}
	
	/**
	 * Adds a variable to the scope of the current block.
	 *
	 * @param	v	The variable
	 */
	public void addVariable(Variable v) {
		if (variables.indexOf(v) != -1) {
			throw new RuntimeException("Variable '" + v.getName() + "' already declared in current scope");
		}
		
		variables.add(v);
	}

	/**
	 * Searches for a variable identifier in the current block.
	 *
	 * @param	name	The identifier as a string.
	 * @return	Returns a reference to a variable or null if none is available.
	 */
	public Variable searchVariableLocal(String name) {
		for (Iterator<Variable> i = variables.iterator(); i.hasNext();) {
			Variable v = i.next();
			if (v.getName().equals(name)) {
				return v;
			}
		}

		return null;
	}
	
	/**
	 * Searches recursively for a variable identifier in the block hierarchy.
	 *
	 * @param	name	The identifier as a string.
	 * @return	Returns a reference to a variable or null if none is available.
	 */
	public Variable searchVariable(String name) {
		for (Iterator<Variable> i = variables.iterator(); i.hasNext();) {
			Variable v = i.next();
			if (v.getName().equals(name)) {
				return v;
			}
		}

		if (getParent() == null)
			return null;
		
		return getParent().searchVariable(name);
	}
	
	/**
	 * Returns an iterator which enumerates all variables in the block's scope.
	 *
	 * @return	An iterator enumerating all local variables.
	 */
	public Iterator<Variable> iterator() {
		return variables.iterator();
	}
	
	private PrintStream indent() {
		return indent(getDepth());
	}
	
	private PrintStream indent(int depth) {
		for (int i = -1; i < depth; i++) {
			System.out.print("\t");
		}

		return System.out;
	}
	
	public void display() {
		indent().println("{ B" + blockNumber + ", parent = " +
			(getParent() != null ? "B" + getParent().blockNumber : "null"));
		
		for (Iterator<Variable> i = variables.iterator(); i.hasNext();) {
			indent(depth + 1).println(i.next());
		}

		indent().println("}");
	}
	
	/**
	 * Returns a reference to the parent block.
	 *
	 * @return	Returns the parent block or null if it is a root block.
	 */
	public Block getParent() {
		return parent;
	}
	
	/**
	 * Returns the block depth of the current block.
	 *
	 * @return	Returns the block depth.
	 */
	public int getDepth() {
		return depth;
	}
}
