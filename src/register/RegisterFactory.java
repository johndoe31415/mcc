public class RegisterFactory {
	private static int id = 0;
	
	/**
	 * Returns a new virtual register with a unique name starting with "tmp"
	 * followed by a number sequentially assigned.
	 *
	 * @return	A new virtual register with a unique name.
	 */
	public static VirtualRegister getVirtualRegister(Type type) {
		return new VirtualRegister("__vr" + id++, type);
	}
}
