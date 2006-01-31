import java.util.*;

public class CILGeneratorASTVisitor extends TreeNodeVisitorImpl {
	public CILGeneratorASTVisitor(TreeNode root) {
		lvalueStore = false;
		GlobalFunctionTable.init(root);

		vregfactory = new RegisterFactory();
		returnIntRegister = new VirtualRegister("__return_int", new Type(Definitions.Types.INT));
		returnRealRegister = new VirtualRegister("__return_real", new Type(Definitions.Types.REAL));
	}
	
	private VirtualRegister returnIntRegister, returnRealRegister;
	private RegisterFactory vregfactory;
	
	/**
	 * Points to the current intermediate code container which is usually the
	 * last element in container.
	 */
	private ICodeContainer definition;
	
	/**
	 * Holds a reference to all functions' intermediate code containers already
	 * generated.
	 */
	private FunctionContainer container;

	/**
	 * Indicates that the lvalue shouldn't be loaded into a temporary register
	 * since we want to store a value there. Instead the array index should be
	 * computed and stored into a temporary register.
	 */
	private boolean lvalueStore;

	/**
	 * Is used as a virtual return value from visit methods since the visitor
	 * pattern cannot properly handle that. It indicates where the read-only
	 * temporary result was stored.
	 */
	private Operand lastreg;
	
	/**
	 * Indicates if the intermediate code should branch if the condition or
	 * comparison evaluates to true (non-inverted) or to false (inverted). If
	 * the condition evaluation is inverted than jumplabel marks the reject
	 * label of the condition branch and if the evaluation is non-inverted it
	 * marks the accept label of the condition branch.
	 *
	 * The inverted condition evaluation behavior is suitable for the while(){},
	 * for(){}, do{}until() and if(){} flow control statements but not for
	 * do{}while() for example.
	 */
	private boolean inverteval;
	
	/**
	 * Serves as a parameter for subsequent visit() calls. Condition nodes can
	 * propagate in this way the jump label to subconditions. If the jump label
	 * should be branched to on condition acceptance or rejectance is indicated
	 * by inverteval.
	 */
	private CLabel jumplabel;

	/**
	 * Points to the tree node of the current block or null if none is open.
	 */
	private BlockNode curblock;

	/**
	 * Generates intermediate codes for each function in an ICodeContainer and
	 * groups those in a function container which is returned afterwards.
	 *
	 * @return	A FunctionContainer object containing intermediate code
	 * 			definitions for each function in form of an ICodeContainer.
	 */
	public FunctionContainer generateICode(TreeNode root) {
		container = new FunctionContainer();
		
		root.acceptVisitor(this);
		
		// destroy local references
		FunctionContainer ret = container;
		container = null;
		definition = null;

		return ret;
	}

	private void continueTraversal(TreeNode node) {
		for (Iterator<TreeNode> i = node.iterator(); i.hasNext();)
			i.next().acceptVisitor(this);
	}
	
	static int debuglvl = 0;
	static final boolean debug = false;
	private void predebug(TreeNode node) {
		if (debug) {
//			for (int i=0; i<3*debuglvl; i++) System.err.print(" ");
//			System.err.println(" => Entering " + node);
			if (definition!=null) definition.add(new CNop(" Entering " + node));
			debuglvl++;
		}
	}
	private void postdebug(TreeNode node) {
		if (debug) {
			debuglvl--;
//			for (int i=0; i<3*debuglvl; i++) System.err.print(" ");
//			System.err.println(" <= Leaving  " + node);
			if (definition!=null) definition.add(new CNop(""));
		}
	}
	
	/**
	 * Generate a temporary register in the current intermediate code
	 * definition.
	 *
	 * @return	The temporary register.
	 */
	private VirtualRegister generateVReg(Type type) {
		lastreg = vregfactory.getVirtualRegister(type);
		return (VirtualRegister)lastreg;
	}
	
	public void visit(FunctionDeclarationNode node) {
		predebug(node);
		definition = new ICodeContainer(node.getIdentifierNode().getIdentifier());
		container.add(definition);
		
		int offset = -20;
		for (Iterator<Variable> i = node.getBlock().iterator(); i.hasNext();) {
			Variable v = i.next();
			v.setOffset(offset);
			//System.err.println("funcparam: " + v + " at offset " + v.getOffset() + " at depth " + v.getDepth());
			offset -= v.getType().getSize();
		}
		
		continueTraversal(node);

		definition = null;
		postdebug(node);
	}
	
	public void visit(LValueNode node) {
		boolean lvalueStoreLocal = lvalueStore;
		lvalueStore = false;

		predebug(node);
		if (node.degree() == 0) {
			// simple variable
			if (lvalueStoreLocal) {
				// return index for writing
				lastreg = generateVReg(new Type(node.getType()));
				definition.add(new CAssgn(ImmediateOperandFactory.getImmediateOperand(new Constant(node.getType(), new String("0"))), lastreg));
			}
			else {
				// return content for reading
				lastreg = node.getVariable();
			}
		}
		else {
			// array
			VirtualRegister index = generateVReg(new Type(node.getType()));
			definition.add(new CAssgn(ImmediateOperandFactory.getImmediateOperand(new Constant(node.getType(), new String("0"))), index));

			Vector<Integer> dimensions = node.getVariable().getDimensions();
			
			//System.err.println("dimsize = " + dimensions.size() + "; dyndimsize = " + node.degree());
			if (dimensions.size() != node.degree()) {
				throw new RuntimeException("LValue's array dimensions differ from declaration (declared " + dimensions.size() + ", but found " + node.degree() + ").");
			}

			int offsetAccu = node.getVariable().getArraySize();
			
			int i = 0;
			VirtualRegister accu = generateVReg(new Type(node.getType()));
			for (Iterator<TreeNode> it = node.iterator(); it.hasNext(); ++i) {
				offsetAccu /= dimensions.get(i);

				TreeNode n = it.next();
				if (n instanceof ConstantNode) {
					ConstantNode constant = (ConstantNode)n;
					if (constant.getType() != Definitions.Types.INT)
						throw new RuntimeException("Array index must be an integer.");

					definition.add(new CAssgn(ImmediateOperandFactory.getImmediateIntOperand(constant.getINT() * offsetAccu), accu));
					definition.add(new CAdd(accu, index, index));
				}
				else {
					n.acceptVisitor(this);
					Operand op = lastreg;
					if (offsetAccu != 1) {
						definition.add(new CAssgn(ImmediateOperandFactory.getImmediateIntOperand(offsetAccu), accu));
						definition.add(new CMul(op, accu, accu));
						definition.add(new CAdd(accu, index, index));
					}
					else {
						definition.add(new CAdd(op, index, index));
					}
				}
			}
			
			if (lvalueStoreLocal) {
				// return index for writing
				lastreg = index;
			}
			else {
				// return content for reading
				lastreg = generateVReg(new Type(node.getType()));
				definition.add(new CLoad(node.getVariable(), index, lastreg));
			}
		}
		postdebug(node);
	}

	public void visit(CallNode node) {
		predebug(node);

		FunctionDeclarationNode f = null;
		try {
			f = GlobalFunctionTable.get().lookup(node.getIdentifierNode().getIdentifier());
		} catch (Exception E) {
		}
		if (f==null) throw new RuntimeException("Function call to function '" + node.getIdentifierNode().getIdentifier() + "' unknown.");
		Type t = new Type(f.getTypeNode().getType());
		VirtualRegister tmp = generateVReg(t);

		int argumentSize = 0;
		TreeNode argNode = node.getArgumentsNode();
		if (argNode != null) {
			// push arguments
			LinkedList<TreeNode> args = new LinkedList<TreeNode>();
			
			for (Iterator<TreeNode> it = node.getArgumentsNode().iterator(); it.hasNext();) {
				args.addFirst(it.next());
			}
			
			for (TreeNode it : args) {
				it.acceptVisitor(this);
				definition.add(new CPush(lastreg));
				argumentSize += lastreg.getType().getSize();
			}
		} 
		
		String id = node.getIdentifierNode().getIdentifier();
		definition.add(new CCall(id, argumentSize, tmp));

		/*if (tmp.getType().getType() == Definitions.Types.INT) {
			definition.add(new CLoad(returnIntRegister, ImmediateOperandFactory.getImmediateOperand(0), tmp));
		}
		else if (tmp.getType().getType() == Definitions.Types.REAL) {
			definition.add(new CLoad(returnRealRegister, ImmediateOperandFactory.getImmediateOperand(0), tmp));
		}*/
		
		lastreg = tmp;
		postdebug(node);
	}
	
	public void visit(ExpressionNode node) {
		// expression on the right side of the assignment;
		predebug(node);

		node.getLeftOperand().acceptVisitor(this);
		Operand a = lastreg;
		
		node.getRightOperand().acceptVisitor(this);
		Operand b = lastreg;
		
		lastreg = generateVReg(new Type(node.getType()));
		switch (node.getOperation()) {
			case ADD:
				definition.add(new CAdd(a, b, lastreg));
				break;
			case SUBTRACT:
				definition.add(new CSub(a, b, lastreg));
				break;
			case MULTIPLY:
				definition.add(new CMul(a, b, lastreg));
				break;
			case DIVIDE:
				definition.add(new CDiv(a, b, lastreg));
				break;
			case MODULO:
				//definition.add(new CMod(a, b, lastreg));
				throw new RuntimeException("Modulo not implemented.");
			default:
				throw new RuntimeException("Unknown operation not implemented.");
		}
		postdebug(node);
	}

	public void visit(AssignNode node) {
		predebug(node);

		if (node.getLValueNode().degree() == 0) {
			// simple variable
			node.getLValueNode().acceptVisitor(this);
			Operand dst = lastreg;
			
			node.getExpressionOrCastNode().acceptVisitor(this);
			Operand src = lastreg;
			
			definition.add(new CStore(src, dst, ImmediateOperandFactory.getImmediateIntOperand(0)));
		}
		else {
			// array
			lvalueStore = true;
			node.getLValueNode().acceptVisitor(this);
			Operand index = lastreg;

			node.getExpressionOrCastNode().acceptVisitor(this);
			Operand src = lastreg;
			
			definition.add(new CStore(src, node.getLValueNode().getVariable(), index));
		}
		
		postdebug(node);
	}
	
	public void visit(ArgumentsNode node) {
		throw new RuntimeException("We should never visit an arguments node here.");
	}

	public void visit(BlockNode node) {
		predebug(node);
		BlockNode tmp = curblock;
		curblock = node;
		
		// reserve memory for local variables
		for (Iterator<Variable> i = curblock.getBlock().iterator(); i.hasNext();) {
			definition.add(new CVar(i.next()));
		}
		
		continueTraversal(node);

		curblock = tmp;
		
		postdebug(node);
	}
	
	public void visit(CastIntRealNode node) {
		predebug(node);
		VirtualRegister dst = generateVReg(new Type(Definitions.Types.REAL));	/* explicitly cast to REAL */
		node.getChildren().get(0).acceptVisitor(this);
		definition.add(new CI2R(lastreg, dst));
		lastreg = dst;
		postdebug(node);
	}
	
	public void visit(CastRealIntNode node) {
		predebug(node);
		VirtualRegister dst = generateVReg(new Type(Definitions.Types.INT));	/* explicitly cast to INT */
		node.getChildren().get(0).acceptVisitor(this);
		definition.add(new CR2I(lastreg, dst));
		lastreg = dst;
		postdebug(node);
	}

	public void visit(CompareNode node) { /* < = > != >= <= */
		predebug(node);
		
		node.getLeftExpressionNode().acceptVisitor(this);
		Operand lex = lastreg;
		node.getRightExpressionNode().acceptVisitor(this);
		Operand rex = lastreg;

		switch (inverteval ? node.getInvertedCmpOperation() : node.getCmpOperation()) {
			case LT:
				definition.add(new CBLT(lex, rex, jumplabel));
				break;
			case GT:
				definition.add(new CBGT(lex, rex, jumplabel));
				break;
			case LE:
				definition.add(new CBLE(lex, rex, jumplabel));
				break;
			case GE:
				definition.add(new CBGE(lex, rex, jumplabel));
				break;
			case EQ:
				definition.add(new CBEQ(lex, rex, jumplabel));
				break;
			case NEQ:
				definition.add(new CBNE(lex, rex, jumplabel));
				break;
		}
		
		postdebug(node);
	}
	
	public void visit(ConditionNode node) { /* &&, || */
		predebug(node);
		CLabel lastlabel = jumplabel;

		if (inverteval) {
			/**
			 * If condition is evaluated to false than jump to the reject label
			 * given by jumplabel otherwise do not jump.
			 */

			if (node.getBoolOperation() == Definitions.BoolOperations.OR) {
				CLabel acceptlabel = LabelFactory.getNextLabel();
				CLabel rejectlabel = LabelFactory.getNextLabel();
				
				jumplabel = rejectlabel;
				node.getLeftCondition().acceptVisitor(this);
				// left condition was not rejected so continue with parent condition evaluation
				definition.add(new CBRA(acceptlabel));
				definition.add(rejectlabel);
				
				// if right condition gets rejected, reject whole condition branch
				jumplabel = lastlabel;
				node.getRightCondition().acceptVisitor(this);
				// right condition was not rejected so continue with parent condition evaluation
				definition.add(acceptlabel);
			}
			else if (node.getBoolOperation() == Definitions.BoolOperations.AND) {
				jumplabel = lastlabel;
				node.getLeftCondition().acceptVisitor(this);
				// left condition was not rejected so continue with condition evaluation
				jumplabel = lastlabel;
				node.getRightCondition().acceptVisitor(this);
				// right condition was not rejected so accept whole condition branch
			}
			else {
				throw new RuntimeException("Invalid compare statement.");
			}
		}
		else {
			/**
			 * If condition is evaluated to true than jump to the accept label
			 * given by jumplabel otherwise do not jump.
			 */
			
			if (node.getBoolOperation() == Definitions.BoolOperations.OR) {
				jumplabel = lastlabel;
				node.getLeftCondition().acceptVisitor(this);
				// left condition was not accepted so continue with condition evaluation
				jumplabel = lastlabel;
				node.getRightCondition().acceptVisitor(this);
				// right condition was not accepted so reject whole condition branch
			}
			else if (node.getBoolOperation() == Definitions.BoolOperations.AND) {
				CLabel acceptlabel = LabelFactory.getNextLabel();
				CLabel rejectlabel = LabelFactory.getNextLabel();
				
				jumplabel = acceptlabel;
				node.getLeftCondition().acceptVisitor(this);
				// left condition was not accepted so continue with parent condition evaluation
				definition.add(new CBRA(rejectlabel));
				definition.add(acceptlabel);
				
				// if right condition gets accept, accept whole condition branch
				jumplabel = lastlabel;
				node.getRightCondition().acceptVisitor(this);
				// right condition was not accepted so continue with parent condition evaluation
				definition.add(rejectlabel);
			}
			else {
				throw new RuntimeException("Invalid compare statement.");
			}
		}

		jumplabel = lastlabel;
		postdebug(node);
	}

	public void visit(IfNode node) {
		predebug(node);

		CLabel rejectlabel = LabelFactory.getNextLabel();
		CLabel escapelabel = rejectlabel;
		jumplabel = rejectlabel;

		// visit condition
		inverteval = true;
		node.getCondition().acceptVisitor(this);
		
		// visit accept branch
		node.getAcceptBranch().acceptVisitor(this);
		
		// visit reject branch
		if (node.getRejectBranch() != null) {
			escapelabel = LabelFactory.getNextLabel();
			definition.add(new CBRA(escapelabel));
			definition.add(rejectlabel);
			node.getRejectBranch().acceptVisitor(this);
		}
		
		definition.add(escapelabel);
		
		postdebug(node);
	}
	
	public void visit(ConstantNode node) {
		predebug(node);
		if (definition != null) {
			/* We're in a function here */
			lastreg = generateVReg(new Type(node.getType()));
			definition.add(new CAssgn(ImmediateOperandFactory.getImmediateOperand(node.getConstant()), lastreg));
		}
		postdebug(node);
	}
	
	public void visit(IdentifierNode node) {
		predebug(node);
		visit((TreeNode)node);
		postdebug(node);
	}
	
	public void visit(ParametersNode node) {
		predebug(node);
		continueTraversal(node);
		postdebug(node);
	}
	
	public void visit(ProgramNode node) {
		predebug(node);
		visit((TreeNode)node);
		postdebug(node);
	}
	
	public void visit(ReturnNode node) {
		predebug(node);
		node.getChildren().get(0).acceptVisitor(this);
		
		if (lastreg.getType().getType() == Definitions.Types.INT) {
			definition.add(new CLoad(lastreg, ImmediateOperandFactory.getImmediateIntOperand(0), returnIntRegister));
		}
		else if (lastreg.getType().getType() == Definitions.Types.REAL) {
			definition.add(new CLoad(lastreg, ImmediateOperandFactory.getImmediateIntOperand(0), returnRealRegister));
		}
		
		definition.add(new CRet());
		
		postdebug(node);
	}
	
	public void visit(TypeNode node) {
		predebug(node);
		visit((TreeNode)node);
		postdebug(node);
	}
	
	public void visit(VariableDeclarationNode node) {
		predebug(node);
		visit((TreeNode)node);
		postdebug(node);
	}
	
	public void visit(WhileNode node) {
		predebug(node);
	
		CLabel rejectlabel = LabelFactory.getNextLabel();
		CLabel looplabel = LabelFactory.getNextLabel();
		
		definition.add(looplabel);
		/* Check condition and block */
		jumplabel = rejectlabel;

		inverteval = true;
		continueTraversal(node);

		definition.add(new CBRA(looplabel));
		definition.add(rejectlabel);
		
		postdebug(node);
	}
	
	public void visit(TreeNode node) {
		continueTraversal(node);
	}
}
