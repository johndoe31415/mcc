import java.util.*;

/**
 * @todo need memory hierarchy
 */

public class ICVMachine extends CILVisitorImpl {
	protected FunctionContainer icodes;
	protected Stack stack;
	protected HashMap<String, VirtualRegister> vregisters;
	protected HashMap<String, Integer>	memoryInt;
	protected HashMap<String, Double> memoryReal;
	protected boolean quit;
		
	public ICVMachine(FunctionContainer icodes) {
		this.icodes = icodes;
	}
	
	/**
	 * Searches the function container for a definition of a function called
	 * name.
	 *
	 * @param	name	The name of the function.
	 * @return	The function's icode container.
	 */
	protected ICodeContainer findFunction(String name) {
		for (ICodeContainer i : icodes) {
			if (i.getName().equals(name)) {
				return i;
			}
		}
		
		return null;
	}
	
	/**
	 * Retrieves a reference to the desired virtual register.
	 *
	 * @param	String	The name of the virtual register.
	 * @return	A reference to the virtual register.
	 */
	protected VirtualRegister getVRegister(String name) {
		return null;
	}
	
	/**
	 * Emulates the given intermediate code starting at the program entry point
	 * main().
	 *
	 * @return	Returns false if an error occured otherwise true.
	 */
	public boolean emulate() {
		// reset vmachine
		quit = false;
		vregisters.clear();
		memoryInt.clear();
		memoryReal.clear();
		stack.clear();

		ICodeContainer programEntry = findFunction("main");
		if (programEntry == null) {
			throw new RuntimeException("No program entry point called main found.");
		}
		
		try {
			programEntry.acceptVisitor(this);
		}
		catch(RuntimeException err) {
			return false;
		}

		return true;
	}
	
	public void visit(ICodeContainer icode) {
	}
	
	public void visit(ICode icode) {
		throw new RuntimeException("Invalid icode found.");
	}
	
	public void visit(FunctionContainer funcs) {
		throw new RuntimeException("Compiler error.");
	}
	
	public void visit(CAdd icode) {
		visit((CArithmeticCode)icode);
	}
	
	public void visit(CArithmeticCode icode) {
		visit((ICode)icode);
	}
	
	public void visit(CAssgn icode) {
		visit((CVariableCode)icode);
	}

	public void visit(CBEQ icode) {
		visit((CBranchCode)icode);
	}

	public void visit(CBGE icode) {
		visit((CBranchCode)icode);
	}
	
	public void visit(CBGT icode) {
		visit((CBranchCode)icode);
	}
	
	public void visit(CBLE icode) {
		visit((CBranchCode)icode);
	}
	
	public void visit(CBLT icode) {
		visit((CBranchCode)icode);
	}
	
	public void visit(CBNE icode) {
		visit((CBranchCode)icode);
	}
	
	public void visit(CBRA icode) {
		visit((CBranchCode)icode);
	}
	
	public void visit(CBranchCode icode) {
		visit((ICode)icode);
	}
	
	public void visit(CCall icode) {
		visit((CFunctionCode)icode);
	}
	
	public void visit(CDiv icode) {
		visit((CArithmeticCode)icode);
	}

	public void visit(CFunctionCode icode) {
		visit((ICode)icode);
	}
	
	public void visit(CI2R icode) {
		visit((CTypeConversionCode)icode);
	}

	public void visit(CLabel icode) {
		visit((ICode)icode);
	}
	
	public void visit(CLoad icode) {
		visit((CVariableCode)icode);
	}
	
	public void visit(CMul icode) {
		visit((CArithmeticCode)icode);
	}
	
	public void visit(CPush icode) {
		visit((CFunctionCode)icode);
	}
	
	public void visit(CR2I icode) {
		visit((CTypeConversionCode)icode);
	}
	
	public void visit(CRet icode) {
		visit((CFunctionCode)icode);
	}
	
	public void visit(CStore icode) {
		visit((CVariableCode)icode);
	}
	
	public void visit(CSub icode) {
		visit((CArithmeticCode)icode);
	}
	
	public void visit(CTypeConversionCode icode) {
		visit((ICode)icode);
	}

	public void visit(CVariableCode icode) {
		visit((ICode)icode);
	}

	public void visit(CVar icode) {
		visit((CVariableCode)icode);
	}
}
