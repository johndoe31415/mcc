public class ImmediateOperandFactory {
	private static int id = 0;
	
	
	public static ImmediateOperand getImmediateOperand(Constant constant) {
		return new ImmediateOperand("tmp" + id++, constant);
	}
	
	public static ImmediateOperand getImmediateIntOperand(int o) {
		return new ImmediateOperand("tmp" + id++, new Constant(Definitions.Types.INT, new Integer(o).toString()));
	}
}
