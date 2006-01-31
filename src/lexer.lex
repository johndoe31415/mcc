%%

%line

%state COMMENT

WHITE_SPACE=[\n\ \t\b\012]
DIGIT=[0-9]
ALPHA=[a-zA-Z_]
ALNUM=({DIGIT}|{ALPHA})
LINECOMMENT=//.*

%%

<YYINITIAL> "/*" { yybegin(COMMENT); }
<COMMENT> "*/" { yybegin(YYINITIAL); }

{WHITE_SPACE}                             {/* ingore */}
{LINECOMMENT}                             {/* ingore */}
<COMMENT> .									{}

<YYINITIAL> "!="                {return new Yytoken(Parser.TKNEQ,yytext(), (yyline+1));}
<YYINITIAL> "<="                {return new Yytoken(Parser.TKLEQ,yytext(), (yyline+1));}
<YYINITIAL> ">="                {return new Yytoken(Parser.TKGEQ,yytext(), (yyline+1));}
<YYINITIAL> "&&"                {return new Yytoken(Parser.TKAND,yytext(), (yyline+1));}
<YYINITIAL> "||"                {return new Yytoken(Parser.TKOR,yytext(), (yyline+1));}
<YYINITIAL> ":="                {return new Yytoken(Parser.TKASSIGN,yytext(), (yyline+1));}

<YYINITIAL> ["()[],;+-*/<>{}="] {return new Yytoken(yytext().charAt(0),yytext(), (yyline+1));}

<YYINITIAL> {DIGIT}*          		 {return new Yytoken(Parser.TKINT,yytext(), (yyline+1)); }
<YYINITIAL> {DIGIT}+"."({DIGIT})*      {return new Yytoken(Parser.TKREAL,yytext(), (yyline+1)); }
<YYINITIAL> {ALPHA}{ALNUM}* {
    if(yytext().equals("int"))    return new Yytoken(Parser.TKINT, yytext(), (yyline+1));
    if(yytext().equals("real"))   return new Yytoken(Parser.TKREAL, yytext(), (yyline+1));
    if(yytext().equals("if"))     return new Yytoken(Parser.TKIF, yytext(), (yyline+1));
    if(yytext().equals("else"))   return new Yytoken(Parser.TKELSE, yytext(), (yyline+1));
    if(yytext().equals("while"))  return new Yytoken(Parser.TKWHILE, yytext(), (yyline+1));
    if(yytext().equals("return")) return new Yytoken(Parser.TKRETURN, yytext(), (yyline+1));
                                  return new Yytoken(Parser.TKIDENTIFIER, yytext(), (yyline+1));
}


