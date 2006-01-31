//### This file created by BYACC 1.8(/Java extension  1.11)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 19 "src/grammar.y"
//#line 18 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//## **user defined:Object
String   yytext;//user variable to return contextual strings
Object yyval; //used to return semantic vals from action routines
Object yylval;//the 'lval' (result) I got from yylex()
Object valstk[] = new Object[YYSTACKSIZE];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
final void val_init()
{
  yyval=new Object();
  yylval=new Object();
  valptr=-1;
}
final void val_push(Object val)
{
  try {
    valptr++;
    valstk[valptr]=val;
  }
  catch (ArrayIndexOutOfBoundsException e) {
    int oldsize = valstk.length;
    int newsize = oldsize*2;
    Object[] newstack = new Object[newsize];
    System.arraycopy(valstk,0,newstack,0,oldsize);
    valstk = newstack;
    valstk[valptr]=val;
  }
}
final Object val_pop()
{
  return valstk[valptr--];
}
final void val_drop(int cnt)
{
  valptr -= cnt;
}
final Object val_peek(int relative)
{
  return valstk[valptr-relative];
}
//#### end semantic value section ####
public final static short TKIDENTIFIER=257;
public final static short TKREAL=258;
public final static short TKINT=259;
public final static short TKLEQ=260;
public final static short TKGEQ=261;
public final static short TKNEQ=262;
public final static short TKIF=263;
public final static short TKELSE=264;
public final static short TKWHILE=265;
public final static short TKRETURN=266;
public final static short TKASSIGN=267;
public final static short TKAND=268;
public final static short TKOR=269;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    3,    3,    1,    2,    2,    4,
    4,    4,    6,    6,    7,    7,    7,    7,    8,    8,
    8,   11,   11,   11,   12,   12,   12,   12,   12,   12,
   12,   10,   10,   10,   13,   13,   13,   13,   13,   13,
   15,   15,    5,   14,   14,    9,    9,   16,   16,   16,
   16,   16,   17,   17,   18,   19,   20,
};
final static short yylen[] = {                            2,
    3,    2,    1,    2,    3,    2,    2,    6,    5,    1,
    1,    4,    3,    1,    4,    3,    3,    2,    3,    3,
    1,    3,    3,    1,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    1,    3,    1,    1,    4,    3,    1,
    4,    1,    1,    3,    1,    2,    1,    1,    1,    1,
    1,    1,    7,    5,    5,    4,    3,
};
final static short yydefred[] = {                         0,
   10,   11,    0,    0,    3,    0,    0,    2,    4,   43,
    0,    0,    1,   37,   36,    0,    0,    0,    0,   34,
    0,    0,    0,    0,   12,    0,    0,    0,    0,    0,
    0,   14,    0,    0,   35,   39,    0,    0,    0,    0,
   32,   33,    0,    0,    9,    7,    0,    0,   38,    0,
   41,    0,    0,    0,   18,    0,    0,   42,   52,    0,
    0,   47,   48,   49,   50,   51,    8,   13,    0,    0,
    0,    0,    6,   17,    0,    0,   16,   46,    0,    0,
    0,    0,   24,    0,   57,    5,   15,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   56,   31,    0,    0,    0,    0,    0,    0,   22,   23,
    0,   55,    0,   53,
};
final static short yydgoto[] = {                          3,
    4,    5,   57,   33,   17,   34,   59,   81,   60,   19,
   82,   83,   20,   38,   21,   62,   63,   64,   65,   66,
};
final static short yysindex[] = {                      -177,
    0,    0, -177,  -18,    0,  -59,   16,    0,    0,    0,
   40,   71,    0,    0,    0,   40,   94,   55,   67,    0,
   -2,   -7,   86,   29,    0,   40,   40,   40,   40,   40,
  -33,    0,  -59,   -8,    0,    0,   81,   69,   67,   67,
    0,    0,   59,  -77,    0,    0,  -33, -177,    0,   40,
    0,   96,   99,   40,    0,   44,  -52,    0,    0,   43,
  -64,    0,    0,    0,    0,    0,    0,    0,   81,   46,
   46,   48,    0,    0,   57,   50,    0,    0,   40,   46,
   34,  -26,    0,  -24,    0,    0,    0,   63,   23,  -19,
   40,   40,   40,   40,   40,   40,   46,   46,  -33,  -33,
    0,    0,   81,   81,   81,   81,   81,   81,    0,    0,
 -119,    0,  -33,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   92,    0,    0,    0,    0,  -41,    0,  -36,    0,
  -31,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   76,    0,   -6,   -1,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   89,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    4,    6,    8,   10,   22,   24,    0,    0,
   54,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   75,  150,    0,  135,   68,    0,   41,  307,   98,  116,
   -4,   49,  121,    0,   61,   39,    0,    0,    0,    0,
};
final static int YYTABLESIZE=403;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         42,
   42,   42,   42,   42,   21,   42,   21,   21,   21,   40,
   40,   40,   40,   40,   99,   40,  100,   42,   42,   42,
   42,  102,   21,   21,   21,   21,   30,   40,   40,   40,
   40,   11,   47,   31,   19,   48,   19,   19,   19,   20,
    9,   20,   20,   20,   29,   44,   30,   55,   26,   42,
   25,   42,   19,   19,   19,   19,   21,   20,   20,   20,
   20,   40,   27,   35,   28,   26,   84,   27,   16,   36,
   44,   45,   74,   12,   13,   90,   26,    7,   27,   16,
    1,    2,   95,   94,   96,   80,   19,   67,   30,   44,
   26,   20,   27,   95,   94,   96,   32,   26,   78,   27,
   46,   26,   73,   27,   61,   26,   85,   27,   28,   49,
   22,   58,   50,   29,   78,   86,   45,   61,   56,   45,
   61,  101,   68,   26,   58,   27,   35,   58,   26,   44,
   27,   75,   44,   24,    6,   70,   61,    6,   71,  111,
  112,   39,   40,   58,  113,  109,  110,   25,   41,   42,
    7,   51,    8,  114,   76,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   44,    0,   77,    0,    0,
    0,    0,   44,    0,   87,    0,   54,    0,   54,   10,
    1,    2,    0,    0,    0,   52,    0,   53,   54,    0,
    0,    0,    0,    0,    0,    0,    0,   10,    0,    0,
    0,    0,   79,    0,   10,    1,    2,    0,    0,    0,
   52,    0,   53,   54,    0,    0,    0,    0,   42,   42,
   42,    0,    0,   21,   21,   21,   42,   42,   40,   40,
   40,   21,   21,    0,    0,    0,   40,   40,    0,    0,
    0,   97,   98,   97,   98,    0,    0,    0,   97,   98,
    1,    2,    0,   19,   19,   19,    0,    0,   20,   20,
   20,   19,   19,    0,    0,    0,   20,   20,    0,    0,
    0,   29,   29,   30,   30,   26,   26,   25,   25,    0,
    0,    0,   91,   92,   93,   10,   14,   15,    0,   27,
   27,   28,   28,   91,   92,   93,   10,   14,   15,   10,
    0,    0,   10,   14,   15,   52,   10,   53,   54,    0,
   54,    0,   52,    0,   53,   54,   54,   18,   54,   54,
    0,    0,   23,    0,    0,    0,    0,    0,    0,    0,
   37,    0,    0,    0,    0,    0,   43,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   69,    0,    0,    0,
   72,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   88,   89,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  103,  104,  105,
  106,  107,  108,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   41,   47,   43,   44,   45,   41,
   42,   43,   44,   45,   41,   47,   41,   59,   60,   61,
   62,   41,   59,   60,   61,   62,   91,   59,   60,   61,
   62,   91,   41,   41,   41,   44,   43,   44,   45,   41,
   59,   43,   44,   45,   41,  123,   41,  125,   41,   91,
   41,   93,   59,   60,   61,   62,   93,   59,   60,   61,
   62,   93,   41,   41,   41,   43,   71,   45,   40,   41,
  123,   31,  125,    6,   59,   80,   43,    3,   45,   40,
  258,  259,   60,   61,   62,   40,   93,   47,   91,  123,
   43,   93,   45,   60,   61,   62,   22,   43,   60,   45,
   33,   43,   59,   45,   44,   43,   59,   45,   42,   41,
   40,   44,   44,   47,   76,   59,   41,   57,   44,   44,
   60,   59,   48,   43,   57,   45,   41,   60,   43,   41,
   45,   57,   44,   40,    0,   40,   76,    3,   40,   99,
  100,   26,   27,   76,  264,   97,   98,   93,   28,   29,
   59,   93,    3,  113,   57,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  123,   -1,  125,   -1,   -1,
   -1,   -1,  123,   -1,  125,   -1,  123,   -1,  125,  257,
  258,  259,   -1,   -1,   -1,  263,   -1,  265,  266,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,   -1,   -1,
   -1,   -1,  267,   -1,  257,  258,  259,   -1,   -1,   -1,
  263,   -1,  265,  266,   -1,   -1,   -1,   -1,  260,  261,
  262,   -1,   -1,  260,  261,  262,  268,  269,  260,  261,
  262,  268,  269,   -1,   -1,   -1,  268,  269,   -1,   -1,
   -1,  268,  269,  268,  269,   -1,   -1,   -1,  268,  269,
  258,  259,   -1,  260,  261,  262,   -1,   -1,  260,  261,
  262,  268,  269,   -1,   -1,   -1,  268,  269,   -1,   -1,
   -1,  268,  269,  268,  269,  268,  269,  268,  269,   -1,
   -1,   -1,  260,  261,  262,  257,  258,  259,   -1,  268,
  269,  268,  269,  260,  261,  262,  257,  258,  259,  257,
   -1,   -1,  257,  258,  259,  263,  257,  265,  266,   -1,
  257,   -1,  263,   -1,  265,  266,  263,   11,  265,  266,
   -1,   -1,   16,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   24,   -1,   -1,   -1,   -1,   -1,   30,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   50,   -1,   -1,   -1,
   54,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   79,   80,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   91,   92,   93,
   94,   95,   96,
};
}
final static short YYFINAL=3;
final static short YYMAXTOKEN=269;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,"TKIDENTIFIER","TKREAL","TKINT","TKLEQ",
"TKGEQ","TKNEQ","TKIF","TKELSE","TKWHILE","TKRETURN","TKASSIGN","TKAND","TKOR",
};
final static String yyrule[] = {
"$accept : program",
"program : program var_decl ';'",
"program : program func_decl",
"program : func_decl",
"program : var_decl ';'",
"var_decl_list : var_decl_list var_decl ';'",
"var_decl_list : var_decl ';'",
"var_decl : type identifier",
"func_decl : type identifier '(' par_list ')' block",
"func_decl : type identifier '(' ')' block",
"type : TKREAL",
"type : TKINT",
"type : type '[' arith_expr ']'",
"par_list : par_list ',' var_decl",
"par_list : var_decl",
"block : '{' var_decl_list stmt_list '}'",
"block : '{' stmt_list '}'",
"block : '{' var_decl_list '}'",
"block : '{' '}'",
"arith_expr : arith_expr '+' term",
"arith_expr : arith_expr '-' term",
"arith_expr : term",
"cond_expr : cond_expr TKAND ao_expr",
"cond_expr : cond_expr TKOR ao_expr",
"cond_expr : ao_expr",
"ao_expr : arith_expr '=' arith_expr",
"ao_expr : arith_expr TKNEQ arith_expr",
"ao_expr : arith_expr '<' arith_expr",
"ao_expr : arith_expr '>' arith_expr",
"ao_expr : arith_expr TKLEQ arith_expr",
"ao_expr : arith_expr TKGEQ arith_expr",
"ao_expr : '(' cond_expr ')'",
"term : term '*' factor",
"term : term '/' factor",
"term : factor",
"factor : '(' arith_expr ')'",
"factor : TKINT",
"factor : TKREAL",
"factor : identifier '(' arg_list ')'",
"factor : identifier '(' ')'",
"factor : lvalue",
"lvalue : lvalue '[' arith_expr ']'",
"lvalue : identifier",
"identifier : TKIDENTIFIER",
"arg_list : arg_list ',' arith_expr",
"arg_list : arith_expr",
"stmt_list : stmt_list stmt",
"stmt_list : stmt",
"stmt : if_stmt",
"stmt : while_stmt",
"stmt : assgn_stmt",
"stmt : return_stmt",
"stmt : block",
"if_stmt : TKIF '(' cond_expr ')' block TKELSE block",
"if_stmt : TKIF '(' cond_expr ')' block",
"while_stmt : TKWHILE '(' cond_expr ')' block",
"assgn_stmt : lvalue TKASSIGN arith_expr ';'",
"return_stmt : TKRETURN arith_expr ';'",
};

//#line 292 "src/grammar.y"

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
//#line 382 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 24 "src/grammar.y"
{
		ast.link((TreeNode)val_peek(1));
	}
break;
case 2:
//#line 27 "src/grammar.y"
{
		ast.link((TreeNode)val_peek(0));
	}
break;
case 3:
//#line 30 "src/grammar.y"
{
		ast = new ProgramNode();
		ast.link((TreeNode)val_peek(0));
	}
break;
case 4:
//#line 34 "src/grammar.y"
{
		ast = new ProgramNode();
		ast.link((TreeNode)val_peek(1));
	}
break;
case 5:
//#line 41 "src/grammar.y"
{
		TreeNode node = new TreeNodeImpl();
		node.join((TreeNode)val_peek(2));
		node.link((TreeNode)val_peek(1));
		yyval = node;
	}
break;
case 6:
//#line 47 "src/grammar.y"
{
		TreeNode node = new TreeNodeImpl();
		node.link((TreeNode)val_peek(1));
		yyval = node;
	}
break;
case 7:
//#line 55 "src/grammar.y"
{
		yyval = new VariableDeclarationNode((TypeNode)val_peek(1), (IdentifierNode)val_peek(0));
	}
break;
case 8:
//#line 61 "src/grammar.y"
{
		yyval = new FunctionDeclarationNode((TypeNode)val_peek(5), (IdentifierNode)val_peek(4), (ParametersNode)val_peek(2), (BlockNode)val_peek(0));
	}
break;
case 9:
//#line 64 "src/grammar.y"
{
		yyval = new FunctionDeclarationNode((TypeNode)val_peek(4), (IdentifierNode)val_peek(3), null, (BlockNode)val_peek(0));
	}
break;
case 10:
//#line 70 "src/grammar.y"
{
		yyval = new TypeNode((Yytoken)val_peek(0));
	}
break;
case 11:
//#line 73 "src/grammar.y"
{
		yyval = new TypeNode((Yytoken)val_peek(0));
	}
break;
case 12:
//#line 76 "src/grammar.y"
{
		TreeNode node = (TreeNode)val_peek(3);
		node.link((TreeNode)val_peek(1));
		yyval = node;
	}
break;
case 13:
//#line 84 "src/grammar.y"
{
		ParametersNode pars = new ParametersNode();
		pars.join((TreeNode)val_peek(2));
		pars.link((TreeNode)val_peek(0));
		yyval = pars;
	}
break;
case 14:
//#line 90 "src/grammar.y"
{
		ParametersNode pars = new ParametersNode();
		pars.link((TreeNode)val_peek(0));
		yyval = pars;
	}
break;
case 15:
//#line 98 "src/grammar.y"
{
		BlockNode block = new BlockNode();
		block.join((TreeNode)val_peek(2));
		block.join((TreeNode)val_peek(1));
		yyval = block;
	}
break;
case 16:
//#line 104 "src/grammar.y"
{
		BlockNode block = new BlockNode();
		block.join((TreeNode)val_peek(1));
		yyval = block;
	}
break;
case 17:
//#line 109 "src/grammar.y"
{
		BlockNode block = new BlockNode();
		block.join((TreeNode)val_peek(1));
		yyval = block;
	}
break;
case 18:
//#line 114 "src/grammar.y"
{
		yyval = new BlockNode();
	}
break;
case 19:
//#line 120 "src/grammar.y"
{
		yyval = new ExpressionNode((ExpressionNode)val_peek(2), (Yytoken)val_peek(1), (ExpressionNode)val_peek(0));
	}
break;
case 20:
//#line 123 "src/grammar.y"
{
		yyval = new ExpressionNode((ExpressionNode)val_peek(2), (Yytoken)val_peek(1), (ExpressionNode)val_peek(0));
	}
break;
case 21:
//#line 126 "src/grammar.y"
{
		yyval = val_peek(0);
	}
break;
case 22:
//#line 132 "src/grammar.y"
{
		yyval = new ConditionNode((ConditionNode)val_peek(2), (Yytoken)val_peek(1), (ConditionNode)val_peek(0));
	}
break;
case 23:
//#line 135 "src/grammar.y"
{
		yyval = new ConditionNode((ConditionNode)val_peek(2), (Yytoken)val_peek(1), (ConditionNode)val_peek(0));
	}
break;
case 24:
//#line 138 "src/grammar.y"
{
		yyval = val_peek(0);
	}
break;
case 25:
//#line 144 "src/grammar.y"
{
		yyval = new CompareNode((ExpressionNode)val_peek(2), (Yytoken)val_peek(1), (ExpressionNode)val_peek(0));
	}
break;
case 26:
//#line 147 "src/grammar.y"
{
		yyval = new CompareNode((ExpressionNode)val_peek(2), (Yytoken)val_peek(1), (ExpressionNode)val_peek(0));
	}
break;
case 27:
//#line 150 "src/grammar.y"
{
		yyval = new CompareNode((ExpressionNode)val_peek(2), (Yytoken)val_peek(1), (ExpressionNode)val_peek(0));
	}
break;
case 28:
//#line 153 "src/grammar.y"
{
		yyval = new CompareNode((ExpressionNode)val_peek(2), (Yytoken)val_peek(1), (ExpressionNode)val_peek(0));
	}
break;
case 29:
//#line 156 "src/grammar.y"
{
		yyval = new CompareNode((ExpressionNode)val_peek(2), (Yytoken)val_peek(1), (ExpressionNode)val_peek(0));
	}
break;
case 30:
//#line 159 "src/grammar.y"
{
		yyval = new CompareNode((ExpressionNode)val_peek(2), (Yytoken)val_peek(1), (ExpressionNode)val_peek(0));
	}
break;
case 31:
//#line 162 "src/grammar.y"
{
		yyval = val_peek(1);
	}
break;
case 32:
//#line 169 "src/grammar.y"
{
		yyval = new ExpressionNode((ExpressionNode)val_peek(2), (Yytoken)val_peek(1), (ExpressionNode)val_peek(0));
	}
break;
case 33:
//#line 172 "src/grammar.y"
{
		yyval = new ExpressionNode((ExpressionNode)val_peek(2), (Yytoken)val_peek(1), (ExpressionNode)val_peek(0));
	}
break;
case 34:
//#line 175 "src/grammar.y"
{
		yyval = val_peek(0);
	}
break;
case 35:
//#line 181 "src/grammar.y"
{
		yyval = val_peek(1);
	}
break;
case 36:
//#line 184 "src/grammar.y"
{
		yyval = new ConstantNode((Yytoken)val_peek(0));
	}
break;
case 37:
//#line 187 "src/grammar.y"
{
		yyval = new ConstantNode((Yytoken)val_peek(0));
	}
break;
case 38:
//#line 190 "src/grammar.y"
{
		yyval = new CallNode((IdentifierNode)val_peek(3), (ArgumentsNode)val_peek(1));
	}
break;
case 39:
//#line 193 "src/grammar.y"
{
		yyval = new CallNode((IdentifierNode)val_peek(2), null);
	}
break;
case 40:
//#line 196 "src/grammar.y"
{
		yyval = val_peek(0);
	}
break;
case 41:
//#line 202 "src/grammar.y"
{
		TreeNode node = (TreeNode)val_peek(3);
		node.link((TreeNode)val_peek(1));
		yyval = node;
	}
break;
case 42:
//#line 207 "src/grammar.y"
{
		yyval = new LValueNode((IdentifierNode)val_peek(0));
	}
break;
case 43:
//#line 213 "src/grammar.y"
{
		yyval = new IdentifierNode((Yytoken)val_peek(0));
	}
break;
case 44:
//#line 219 "src/grammar.y"
{
		TreeNode node = new ArgumentsNode();
		node.join((TreeNode)val_peek(2));
		node.link((TreeNode)val_peek(0));
		yyval = node;
	}
break;
case 45:
//#line 225 "src/grammar.y"
{
		TreeNode node = new ArgumentsNode();
		node.link((TreeNode)val_peek(0));
		yyval = node;
	}
break;
case 46:
//#line 233 "src/grammar.y"
{
		TreeNode node = new TreeNodeImpl();
		node.join((TreeNode)val_peek(1));
		node.link((TreeNode)val_peek(0));
		yyval = node;
	}
break;
case 47:
//#line 239 "src/grammar.y"
{
		TreeNode node = new TreeNodeImpl();
		node.link((TreeNode)val_peek(0));
		yyval = node;
	}
break;
case 48:
//#line 247 "src/grammar.y"
{
		yyval = val_peek(0);
	}
break;
case 49:
//#line 250 "src/grammar.y"
{
		yyval = val_peek(0);
	}
break;
case 50:
//#line 253 "src/grammar.y"
{
		yyval = val_peek(0);
	}
break;
case 51:
//#line 256 "src/grammar.y"
{
		yyval = val_peek(0);
	}
break;
case 52:
//#line 259 "src/grammar.y"
{
		yyval = val_peek(0);
	}
break;
case 53:
//#line 265 "src/grammar.y"
{
		yyval = new IfNode((ConditionNode)val_peek(4), (BlockNode)val_peek(2), (BlockNode)val_peek(0));
	}
break;
case 54:
//#line 268 "src/grammar.y"
{
		yyval = new IfNode((ConditionNode)val_peek(2), (BlockNode)val_peek(0), null);
	}
break;
case 55:
//#line 274 "src/grammar.y"
{
		yyval = new WhileNode((ConditionNode)val_peek(2), (BlockNode)val_peek(0));
	}
break;
case 56:
//#line 280 "src/grammar.y"
{
		yyval = new AssignNode((LValueNode)val_peek(3), (ExpressionNode)val_peek(1));
	}
break;
case 57:
//#line 286 "src/grammar.y"
{
		yyval = new ReturnNode((ExpressionNode)val_peek(1));
	}
break;
//#line 894 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
