header {package decaf;}

options 
{
  mangleLiteralPrefix = "TK_";
  language="Java";
}

class DecafScanner extends Lexer;
options 
{
  k=2;
}

tokens 
{
  "class";
  "int";
  "boolean";
  "void";
  "true";
  "false";
  "return";
  "if";
  "else";
  "for";
  "callout";
  "break";
}

ID options { paraphrase = "an identifier"; } : ('a'..'z' | 'A'..'Z')+;

WS_ : (' ' | '\t' | '\n' {newline();}) {_ttype = Token.SKIP; };

SL_COMMENT : "//" (~'\n')* '\n' {_ttype = Token.SKIP; newline (); };

CHAR : '\'' (ESC|~'\'') '\'';
STRING : '"' (ESC|~'"')* '"';

SEMI : ';';
COMMA : ',';

// ops
ADD: '+';
MULT: '*';
DIV: '/';
MOD: '%';
SUB: '-';
NEGATE: '!';
EQ: '=';
LESS: '<';
GREATER: '>';
COND_OP: "&&" | "||";

//digits
DECIMAL_LITERAL : ('0'..'9')+;
HEX_LITERAL : "0x" ('0'..'9'|'a'..'f'|'A'..'F')+;

//all braces
LCURLY options { paraphrase = "{"; } : "{";
RCURLY options { paraphrase = "}"; } : "}";
LBRAC options { paraphrase = "["; } : "[";
RBRAC options { paraphrase = "]"; } : "]";
LPAREN options { paraphrase = "("; } : "(";
RPAREN options { paraphrase = ")"; } : ")";

protected
ESC :  '\\' ('n'|'"');
