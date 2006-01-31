import java.util.*;

public class CILReserveTempVars extends CILVisitorImpl {
	protected Set<String> reservedVars;
	protected LinkedList<CVar> newVars;
	
	public CILReserveTempVars() {
	}

	public void reserveTempVars(FunctionContainer funcs) {
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
		reservedVars = new HashSet<String>();
		newVars = new LinkedList<CVar>();
		
		for (Iterator<ICode> i = icodes.iterator(); i.hasNext();) {
			i.next().acceptVisitor(this);
		}

		//if (newVars.size() != 0)
		//	System.err.println("adding " + newVars.size() + " temporary variable declarations.");
		for (Iterator<CVar> i = newVars.iterator(); i.hasNext();) {
			CVar var = i.next();
			//System.err.println("\t" + var);
			icodes.addFirst(var);
		}

		newVars = null;
	}
	
	/**
	 * Reserve memory for operand if it is a variable and was not already
	 * reserved earlier using CVar. If necessary an appropriate CVar instruction
	 * is added at the beginning of the current icode container.
	 */
	protected void reserveOperand(Operand op) {
		if (!(op instanceof Variable)) {
			return;
		}

		Variable v = (Variable)op;
		
		// skip global vars and function parameters
		if (v.getDepth() != -1)
			return;
		
		if (!reservedVars.contains(v.getName())) {
			newVars.add(new CVar(v));
			reservedVars.add(v.getName());
		}
	}
	
	public void visit(CArithmeticCode icode) {
		reserveOperand(icode.getFirstOp());
		reserveOperand(icode.getSecondOp());
		reserveOperand(icode.getTarget());
	}
	
	public void visit(CAssgn icode) {
		reserveOperand(icode.getFirstOp());
		reserveOperand(icode.getSecondOp());
	}

	public void visit(CBEQ icode) {
		reserveOperand(icode.getFirstOp());
		reserveOperand(icode.getSecondOp());
	}

	public void visit(CBGE icode) {
		reserveOperand(icode.getFirstOp());
		reserveOperand(icode.getSecondOp());
	}
	
	public void visit(CBGT icode) {
		reserveOperand(icode.getFirstOp());
		reserveOperand(icode.getSecondOp());
	}
	
	public void visit(CBLE icode) {
		reserveOperand(icode.getFirstOp());
		reserveOperand(icode.getSecondOp());
	}
	
	public void visit(CBLT icode) {
		reserveOperand(icode.getFirstOp());
		reserveOperand(icode.getSecondOp());
	}
	
	public void visit(CBNE icode) {
		reserveOperand(icode.getFirstOp());
		reserveOperand(icode.getSecondOp());
	}
	
	public void visit(CBRA icode) {
	}
	
	public void visit(CCall icode) {
		// handle return vreg ?
	}
	
	public void visit(CI2R icode) {
		reserveOperand(icode.getSourceOp());
		reserveOperand(icode.getDestinationOp());
	}

	public void visit(CLabel icode) {
	}
	
	public void visit(CLoad icode) {
		reserveOperand(icode.getBase());
		reserveOperand(icode.getIndex());
		reserveOperand(icode.getTarget());
	}
	
	public void visit(CPush icode) {
		reserveOperand(icode.getOperand());
	}
	
	public void visit(CR2I icode) {
		reserveOperand(icode.getSourceOp());
		reserveOperand(icode.getDestinationOp());
	}
	
	public void visit(CRet icode) {
	}
	
	public void visit(CStore icode) {
		reserveOperand(icode.getSource());
		reserveOperand(icode.getIndex());
		reserveOperand(icode.getTarget());
	}

	public void visit(CVar icode) {
		if (!(icode.getOperand() instanceof Variable)) {
			return;
		}

		Variable v = (Variable)icode.getOperand();
		reservedVars.add(v.getName());
	}
}
