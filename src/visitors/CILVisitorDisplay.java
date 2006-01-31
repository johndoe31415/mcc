import java.util.*;
import java.io.*;

public class CILVisitorDisplay extends CILVisitorImpl {
	private PrintStream ostream;
	
	public CILVisitorDisplay(PrintStream ostream) {
		this.ostream = ostream;
	}

	public void visit(FunctionContainer funcs) {
		for (Iterator<ICodeContainer> i=funcs.iterator(); i.hasNext(); ) {
			ICodeContainer currentICodeContainer = i.next();
			ostream.println("#" + currentICodeContainer.getName() + " {");
			visit(currentICodeContainer);
			ostream.println("#}");
		}
	}
	
	public void visit(ICodeContainer icodes) {
		for (Iterator<ICode> i=icodes.iterator(); i.hasNext(); ) {
			ICode currentICode = i.next();
			ostream.print("#\t");
			visit(currentICode);
		}
	}
	
	public void visit(ICode icode) {
		ostream.println(icode);
	}
}
