import java.io.PrintStream;

public abstract class CodeGenerator extends CILVisitorImpl {
	protected PrintStream ostream;
	protected boolean debugFlag;
	
	public CodeGenerator() {
		debugFlag = false;
	}
	
	/**
	 * Generates ia32 code for the intermediate code in the provided function
	 * container and writes it the provided output stream.
	 * @param	ostream	The output stream to write the ia32 code to.
	 * @param	funcs	The functions to generate code for.
	 */
	public void generateCode(PrintStream ostream, FunctionContainer funcs) {
		this.ostream = ostream;
		funcs.acceptVisitor(this);
	}

	public void enableDebug() {
		debugFlag = true;
	}
	
	public void debug(ICode icode) {
		if (debugFlag) {
			ostream.println("# " + icode + ":");
		}
	}
	
	public void debug_leave(ICode icode) {
		if (debugFlag) {
			ostream.println();
		}
	}
	
	public void visit(ICode icode) {
		ostream.println("\t#" + icode);
	}

}
