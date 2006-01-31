public class ConstantNode extends ExpressionNode {
	protected Constant c;
	
	public ConstantNode(float f) {
		c = new Constant(Definitions.Types.REAL, Float.toString(f));
	}

	public ConstantNode(int i) {
		c = new Constant(Definitions.Types.INT, Integer.toString(i));
	}
	
	public ConstantNode(Yytoken value) {
		c = new Constant(Definitions.yyToTypes(value), value.text());
	}

	public Constant getConstant() {
		return c;
	}

	public Definitions.Types getType() {
		return c.getType();
	}

	public float getREAL() {
		return Float.valueOf(c.getValue());
	}

	public int getINT() {
		return Integer.valueOf(c.getValue());
	}

	public String toString() {
		return "constant\\nType = "+Definitions.enumToString(c.getType())+"\\nValue = " + c.getValue();
	}

	public void acceptVisitor(TreeNodeVisitor visitor) {
		visitor.visit(this);
	}
}
