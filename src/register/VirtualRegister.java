public class VirtualRegister extends Operand {
	
	public VirtualRegister(String name, Type type) {
		super(name, type);
	}
	
	
	/**
	 * Used for outputing the virtual register's name.
	 *
	 * @return	A constant string representation (name) of this object.
	 */
	
	public final String toString() {
		return getName();
	}
}
