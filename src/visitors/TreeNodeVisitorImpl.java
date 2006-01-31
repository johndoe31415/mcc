public abstract class TreeNodeVisitorImpl implements TreeNodeVisitor {
	
	public abstract void visit(TreeNode node);

	public void visit(ArgumentsNode node) {
		visit((TreeNode)node);
	}
	
	public void visit(AssignNode node) {
		visit((TreeNode)node);
	}
	
	public void visit(BlockNode node) {
		visit((TreeNode)node);
	}
	
	public void visit(CallNode node) {
		visit((ExpressionNode)node);
	}
	
	public void visit(CastIntRealNode node) {
		visit((TreeNode)node);
	}
	
	public void visit(CastRealIntNode node) {
		visit((TreeNode)node);
	}
	
	public void visit(CompareNode node) {
		visit((ConditionNode)node);
	}
	
	public void visit(ConditionNode node) {
		visit((TreeNode)node);
	}
	
	public void visit(ConstantNode node) {
		visit((ExpressionNode)node);
	}

	public void visit(ExpressionNode node) {
		visit((TreeNode)node);
	}

	public void visit(FunctionDeclarationNode node) {
		visit((TreeNode)node);
	}
	
	public void visit(IdentifierNode node) {
		visit((TreeNode)node);
	}
	
	public void visit(IfNode node) {
		visit((TreeNode)node);
	}
	
	public void visit(LValueNode node) {
		visit((ExpressionNode)node);
	}
	
	public void visit(ParametersNode node) {
		visit((TreeNode)node);
	}
	
	public void visit(ProgramNode node) {
		visit((TreeNode)node);
	}
	
	public void visit(ReturnNode node) {
		visit((TreeNode)node);
	}
	
	public void visit(TypeNode node) {
		visit((TreeNode)node);
	}
	
	public void visit(VariableDeclarationNode node) {
		visit((TreeNode)node);
	}
	
	public void visit(WhileNode node) {
		visit((TreeNode)node);
	}
}
