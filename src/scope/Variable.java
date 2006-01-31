import java.util.*;

public class Variable extends Operand {
	private int offset;
	private int depth;
	private Vector<Integer> dimensions;

	public Variable(String name, Type type, int offset, Vector<Integer>dimensions) {
		super(name, type);
		this.offset = offset;
		this.dimensions = dimensions;
	}

	public boolean equals(Object v) {
		if (!(v instanceof Variable)) throw new RuntimeException("Uncomparable objects!");
		return ((Variable)v).name.equals(name);
	}

	public int hashCode() {
		return name.hashCode();
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getDepth() {
		return depth;
	}

	public Type getType() {
		return type;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Vector<Integer> getDimensions()  {
		return dimensions;
	}

	public int getArraySize() {
		if (getDimensions() == null)
			return 1;
		
		int size = 1;
		for (Integer dim : dimensions) {
			size *= dim;
		}

		return size;
	}

	public final String toString() {
		return name;
	}
};
