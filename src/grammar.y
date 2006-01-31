//%token TKNUMBER
%token TKIDENTIFIER
%token TKREAL
%token TKINT
%token TKLEQ
%token TKGEQ
%token TKNEQ
%token TKIF
%token TKELSE
%token TKWHILE
%token TKRETURN
%token TKASSIGN
%token TKAND
%token TKOR

%start program
	
%{
%}

%%

program: 
	program var_decl ';' {
		ast.link((TreeNode)$2);
	}
	| program func_decl {
		ast.link((TreeNode)$2);
	}
	| func_decl	{
		ast = new ProgramNode();
		ast.link((TreeNode)$1);
	}
	| var_decl ';' {
		ast = new ProgramNode();
		ast.link((TreeNode)$1);
	}
	;

var_decl_list:
	var_decl_list var_decl ';' {
		TreeNode node = new TreeNodeImpl();
		node.join((TreeNode)$1);
		node.link((TreeNode)$2);
		$$ = node;
	}
	| var_decl ';' {
		TreeNode node = new TreeNodeImpl();
		node.link((TreeNode)$1);
		$$ = node;
	}
	;

var_decl:
	type identifier {
		$$ = new VariableDeclarationNode((TypeNode)$1, (IdentifierNode)$2);
	}
	;

func_decl:
	type identifier '(' par_list ')' block {
		$$ = new FunctionDeclarationNode((TypeNode)$1, (IdentifierNode)$2, (ParametersNode)$4, (BlockNode)$6);
	}
	| type identifier '(' ')' block {
		$$ = new FunctionDeclarationNode((TypeNode)$1, (IdentifierNode)$2, null, (BlockNode)$5);
	}
	;

type:
	TKREAL {
		$$ = new TypeNode((Yytoken)$1);
	}
	| TKINT {
		$$ = new TypeNode((Yytoken)$1);
	}
	| type '[' arith_expr ']' {
		TreeNode node = (TreeNode)$1;
		node.link((TreeNode)$3);
		$$ = node;
	}
	;

par_list:
	par_list ',' var_decl {
		ParametersNode pars = new ParametersNode();
		pars.join((TreeNode)$1);
		pars.link((TreeNode)$3);
		$$ = pars;
	}
	| var_decl {
		ParametersNode pars = new ParametersNode();
		pars.link((TreeNode)$1);
		$$ = pars;
	}
	;

block:
	'{' var_decl_list stmt_list '}' {
		BlockNode block = new BlockNode();
		block.join((TreeNode)$2);
		block.join((TreeNode)$3);
		$$ = block;
	}
	| '{' stmt_list '}' {
		BlockNode block = new BlockNode();
		block.join((TreeNode)$2);
		$$ = block;
	}
	| '{' var_decl_list '}' {
		BlockNode block = new BlockNode();
		block.join((TreeNode)$2);
		$$ = block;
	}
	| '{' '}' {
		$$ = new BlockNode();
	}
	;

arith_expr:
	arith_expr '+' term {
		$$ = new ExpressionNode((ExpressionNode)$1, (Yytoken)$2, (ExpressionNode)$3);
	}
	| arith_expr '-' term {
		$$ = new ExpressionNode((ExpressionNode)$1, (Yytoken)$2, (ExpressionNode)$3);
	}
	| term {
		$$ = $1;
	}
	;

cond_expr:
	cond_expr TKAND ao_expr {
		$$ = new ConditionNode((ConditionNode)$1, (Yytoken)$2, (ConditionNode)$3);
	}
	| cond_expr TKOR ao_expr {
		$$ = new ConditionNode((ConditionNode)$1, (Yytoken)$2, (ConditionNode)$3);
	}
	| ao_expr {
		$$ = $1;
	}
	;

ao_expr:
	arith_expr '=' arith_expr {
		$$ = new CompareNode((ExpressionNode)$1, (Yytoken)$2, (ExpressionNode)$3);
	}
	| arith_expr TKNEQ arith_expr {
		$$ = new CompareNode((ExpressionNode)$1, (Yytoken)$2, (ExpressionNode)$3);
	}
	| arith_expr '<'   arith_expr {
		$$ = new CompareNode((ExpressionNode)$1, (Yytoken)$2, (ExpressionNode)$3);
	}
	| arith_expr '>'   arith_expr {
		$$ = new CompareNode((ExpressionNode)$1, (Yytoken)$2, (ExpressionNode)$3);
	}
	| arith_expr TKLEQ arith_expr {
		$$ = new CompareNode((ExpressionNode)$1, (Yytoken)$2, (ExpressionNode)$3);
	}
	| arith_expr TKGEQ arith_expr {
		$$ = new CompareNode((ExpressionNode)$1, (Yytoken)$2, (ExpressionNode)$3);
	}
	| '(' cond_expr ')' {
		$$ = $2;
	}

	;

term:
	term '*' factor {
		$$ = new ExpressionNode((ExpressionNode)$1, (Yytoken)$2, (ExpressionNode)$3);
	}
	| term '/' factor {
		$$ = new ExpressionNode((ExpressionNode)$1, (Yytoken)$2, (ExpressionNode)$3);
	}
	| factor {
		$$ = $1;
	}
	;

factor:
	'(' arith_expr ')' {
		$$ = $2;
	}
	| TKINT {
		$$ = new ConstantNode((Yytoken)$1);
	}
	| TKREAL {
		$$ = new ConstantNode((Yytoken)$1);
	}
	| identifier '(' arg_list ')' {
		$$ = new CallNode((IdentifierNode)$1, (ArgumentsNode)$3);
	}
	| identifier '(' ')' {
		$$ = new CallNode((IdentifierNode)$1, null);
	}
	| lvalue {
		$$ = $1;
	}
	;

lvalue:
	lvalue '[' arith_expr ']' {
		TreeNode node = (TreeNode)$1;
		node.link((TreeNode)$3);
		$$ = node;
	}
	| identifier {
		$$ = new LValueNode((IdentifierNode)$1);
	}
	;

identifier:
	TKIDENTIFIER {
		$$ = new IdentifierNode((Yytoken)$1);
	}
	;

arg_list:
	arg_list ',' arith_expr {
		TreeNode node = new ArgumentsNode();
		node.join((TreeNode)$1);
		node.link((TreeNode)$3);
		$$ = node;
	}
	| arith_expr {
		TreeNode node = new ArgumentsNode();
		node.link((TreeNode)$1);
		$$ = node;
	}
	;

stmt_list:
	stmt_list stmt {
		TreeNode node = new TreeNodeImpl();
		node.join((TreeNode)$1);
		node.link((TreeNode)$2);
		$$ = node;
	}
	| stmt {
		TreeNode node = new TreeNodeImpl();
		node.link((TreeNode)$1);
		$$ = node;
	}
	;

stmt:
	if_stmt {
		$$ = $1;
	}
	| while_stmt {
		$$ = $1;
	}
	| assgn_stmt {
		$$ = $1;
	}
	| return_stmt {
		$$ = $1;
	}
	| block {
		$$ = $1;
	}
	;

if_stmt:
	TKIF '(' cond_expr ')' block TKELSE block {
		$$ = new IfNode((ConditionNode)$3, (BlockNode)$5, (BlockNode)$7);
	}
	| TKIF '(' cond_expr ')' block {
		$$ = new IfNode((ConditionNode)$3, (BlockNode)$5, null);
	}
	;

while_stmt:
	TKWHILE '(' cond_expr ')' block {
		$$ = new WhileNode((ConditionNode)$3, (BlockNode)$5);
	}
	;

assgn_stmt:
	lvalue TKASSIGN arith_expr ';' {
		$$ = new AssignNode((LValueNode)$1, (ExpressionNode)$3);
	}
	;

return_stmt:
	TKRETURN arith_expr ';' {
		$$ = new ReturnNode((ExpressionNode)$2);
	}
	;

%%

private String lxtext	= "";
private Yylex lx	= null;
private int line 	= 1;
private String file;
private TreeNode ast;


public Parser(final Yylex lexer) {
	lx = lexer;
}

public final void start(String filename) {
	file = filename;
	yyparse();
	return;
}

private final void yyerror(final String s) {
	System.err.println(file + ":" + line + " :error: parsing error caused by token " + lxtext);
}

public final int yylex() {
	try{
		yylval=lx.yylex();
		lxtext=((Yytoken) yylval).text();
		line = ((Yytoken) yylval).line();
		return ((Yytoken) yylval).type();
	} catch(Exception e) {
		return -1;
	}
}

public final TreeNode getAST() {
	return ast;
}
