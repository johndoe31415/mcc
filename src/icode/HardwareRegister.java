public class HardwareRegister extends Operand {
	public HardwareRegister(String name, Definitions.Types t) {
		super(name, new Type(t));
	}

	public String toString() {
		return getName();
	}
}
