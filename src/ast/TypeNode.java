public class TypeNode extends TreeNodeImpl {
	Definitions.Types type;
	
	public TypeNode(Yytoken type) {
		if (type.text().equals("int")) this.type = Definitions.Types.INT;
		else if (type.text().equals("real")) this.type = Definitions.Types.REAL;
		else throw new RuntimeException("Invalid type '" + type.text() + "'.");
	}

	public Definitions.Types getType() {
		return type;
	}

	public String toString() {
		return Definitions.enumToString(type);
	}

	public void acceptVisitor(TreeNodeVisitor visitor) {
		visitor.visit(this);
	}
}
