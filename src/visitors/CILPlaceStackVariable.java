import java.util.*;

public class CILPlaceStackVariable extends CILVisitorImpl {
	protected int offset;

	public void placeVariables(FunctionContainer funcs) {
		funcs.acceptVisitor(this);
	}
	
	public void visit(ICode icode) {
	}
	
	public void visit(FunctionContainer funcs) {
		for (Iterator<ICodeContainer> i = funcs.iterator(); i.hasNext();) {
			i.next().acceptVisitor(this);
		}
	}
	
	public void visit(ICodeContainer icodes) {
		offset = 0;
		
		for (Iterator<ICode> i = icodes.iterator(); i.hasNext();) {
			i.next().acceptVisitor(this);
		}
	}
	
	public void visit(CVar icode) {
		if (!(icode.getOperand() instanceof Variable)) {
			return;
		}

		Variable v = (Variable)icode.getOperand();
		v.setOffset(offset);

		offset += icode.getOperand().getType().getSize() * v.getArraySize();
	}
}
