import java.util.Iterator;

public class PrintAST extends TreeNodeVisitorImpl {
	private int nodeno;
	private int depth = 0;
	boolean pretty;
	
	
	public PrintAST(boolean pretty) {
		this.pretty = pretty;
	}
	
	
	public void print(TreeNode root) {
		nodeno = 0;
		depth = 0;

		System.out.println("Digraph G {");
		root.acceptVisitor(this);
		System.out.println("}");
	}


	private void indent(int depth) {
		for (int i = 1; i <= depth; i++)
			System.out.print("\t");
	}

	
	private String assembleNodeFormat() {
		return "";
	}
	
	
	private String assembleNodeFormat(String color, String style, String fillcolor, String shape) {
		if (!pretty)
			return "";

		String format = new String(" ");
		
		if (color.length() > 0)
			format += ", color=" + color;
		if (fillcolor.length() > 0)
			format += ", fillcolor=" + fillcolor;
		if (style.length() > 0)
			format += ", style=\"" + style + "\"";
		if (shape.length() > 0)
			format += ", shape=" + shape;

		return format;
	}
	
	
	private void postvisit(TreeNode node, String format) {
		int curnode = nodeno++;
		depth++;
		
		indent(depth);
		System.out.println("n" + curnode + " [label=\"" + node + "\"" + format + "]");
		
		for (Iterator<TreeNode> i = node.iterator(); i.hasNext();) {
			// print: curnode -> nodeno\n
			indent(depth);
			System.out.println("n" + curnode + " -> n" + nodeno);
			i.next().acceptVisitor(this);
		}

		depth--;
	}
	
	
	public void visit(TreeNode node) {
		postvisit(node, assembleNodeFormat());
	}
	
	
	public void visit(AssignNode node) {
		postvisit(node, assembleNodeFormat("", "filled", "yellow", ""));
	}
	
	
	public void visit(CastIntRealNode node) {
		postvisit(node, assembleNodeFormat("", "filled", "lightblue1", ""));
	}
	
	
	public void visit(CastRealIntNode node) {
		postvisit(node, assembleNodeFormat("", "filled", "lightblue3", ""));
	}

	
	public void visit(ConstantNode node) {
		postvisit(node, assembleNodeFormat("", "rounded,filled", "brown1", "diamond"));
	}

	
	public void visit(LValueNode node) {
		postvisit(node, assembleNodeFormat("", "rounded,filled", "darkseagreen3", "diamond"));
	}

	
	public void visit(ExpressionNode node) {
		postvisit(node, assembleNodeFormat("", "filled", "darkorange1", ""));
	}

	
	public void visit(BlockNode node) {
		postvisit(node, assembleNodeFormat("darkorange1", "", "", ""));
	}

	
	public void visit(WhileNode node) {
		postvisit(node, assembleNodeFormat("", "filled", "turquoise1", "rectangle"));
	}

	
	public void visit(IdentifierNode node) {
		postvisit(node, assembleNodeFormat("", "filled", "olivedrab2", ""));
	}

	
	public void visit(VariableDeclarationNode node) {
		postvisit(node, assembleNodeFormat("", "filled", "salmon3", ""));
	}

	
	public void visit(FunctionDeclarationNode node) {
		postvisit(node, assembleNodeFormat("", "filled", "salmon1", ""));
	}

	
	public void visit(CompareNode node) {
		postvisit(node, assembleNodeFormat("", "filled", "aquamarine3", ""));
	}

	
	public void visit(ConditionNode node) {
		postvisit(node, assembleNodeFormat("", "filled", "aquamarine1", ""));
	}

	
	public void visit(IfNode node) {
		postvisit(node, assembleNodeFormat("", "filled", "coral1", ""));
	}

	
	public void visit(ArgumentsNode node) {
		postvisit(node, assembleNodeFormat("", "filled", "goldenrod1", ""));
	}

	
	public void visit(ParametersNode node) {
		postvisit(node, assembleNodeFormat("", "filled", "goldenrod1", ""));
	}

	
	public void visit(ProgramNode node) {
		postvisit(node, assembleNodeFormat("red", "", "", ""));
	}

	
	public void visit(ReturnNode node) {
		postvisit(node, assembleNodeFormat("red", "", "", ""));
	}
}
