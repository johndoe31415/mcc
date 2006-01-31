public class Type {
	protected Definitions.Types type;
	
	public Type(TypeNode typenode) {
		type = typenode.type;
	}
	
	public Type(Definitions.Types type) {
		this.type = type;
	}

	public void setType(Definitions.Types newtype) {
		type = newtype;
	}

	public Definitions.Types getType() {
		return type;
	}

	public final String toString() {
		return Definitions.enumToString(type);
	}

	public int getSize() {
		if (getType() == Definitions.Types.INT) {
			return 4;
		}
		else if (getType() == Definitions.Types.REAL) {
			return 8;
		}
		else {
			throw new RuntimeException("Invalid Type");
		}
	}
};
