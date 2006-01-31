public class Operand {
	protected String name;
	protected Type type;
	
	public Operand(String name, Type type) {
		this.name = name;
		this.type = type;
	}


	/**
	 * Returns the name of the virtual register.
	 *
	 * @return	The virtual registers name.
	 */
	
	public String getName() {
		return name;
	}
	
	
	/**
	 * Returns the virtual register's type.
	 *
	 * @return	The virtual register's type.
	 */
	
	public Type getType() {
		return type;
	}

	public String toString() {
		return "'Op " + name + ", type = " + type + "'";
	}
}
