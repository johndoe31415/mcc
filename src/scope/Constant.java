public class Constant {
	protected Definitions.Types type;
	protected String value;

	public Constant(Definitions.Types type, String value) {
		this.type = type;
		this.value = value;
	}

	public Definitions.Types getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}

	public String toString() {
		//return Definitions.enumToString(type) + " constant = " + value;
		return value;
	}
};
