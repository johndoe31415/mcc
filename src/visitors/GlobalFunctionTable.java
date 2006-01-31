class GlobalFunctionTable {
	static FunctionTable ft;
	
	GlobalFunctionTable() {
		ft = null;
	}
	
	public static void init(TreeNode node) {
		if (ft != null) return;
		ft = new FunctionTable(node);
	}
	
	public static FunctionTable get() {
		return ft;
	}
}
