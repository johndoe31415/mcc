public class Definitions {
	static enum BoolOperations { OR, AND };
	static enum CmpOperations { EQ, NEQ, LT, GT, LE, GE };
	static enum ArithOperations { ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO };
	static enum Types { INT, REAL };

	static String enumToString(BoolOperations b) {
		if (b==BoolOperations.OR) return new String("||");
		return new String("&&");
	}
	
	static String enumToString(CmpOperations o) {
		if (o==CmpOperations.EQ) return new String("=");
		else if (o==CmpOperations.NEQ) return new String("!=");
		else if (o==CmpOperations.LT) return new String("<");
		else if (o==CmpOperations.GT) return new String(">");
		else if (o==CmpOperations.LE) return new String("<=");
		return new String(">=");
	}
	
	static String enumToString(ArithOperations o) {
		if (o==ArithOperations.ADD) return new String("+");
		else if (o==ArithOperations.SUBTRACT) return new String("-");
		else if (o==ArithOperations.MULTIPLY) return new String("*");
		else if (o==ArithOperations.DIVIDE) return new String("/");
		return new String("%");
	}
	
	static String enumToString(Types t) {
		if (t==Types.INT) return new String("int");
		return new String("real");
	}

	static Types yyToTypes(Yytoken y) {
		if (y.type()==Parser.TKINT) return Types.INT;
		if (y.type()==Parser.TKREAL) return Types.REAL;
		throw new RuntimeException("Got invalid type '"+y.text()+"'.");
	}
}
