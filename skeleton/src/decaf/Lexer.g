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
  "continue";
}

ID:
  ('a'..'z' | 'A'..'Z' | '_')
  ('a'..'z' | 'A'..'Z' | '_' | '0'..'9')*
;

WS_ : (' ' | '\t' | '\n' {newline();}) {_ttype = Token.SKIP; };

SL_COMMENT : "//" (~'\n')* '\n' {_ttype = Token.SKIP; newline (); };

CHAR : '\''
  (ESC|~('\'' | '\"' | '\\')) // dont allow unescaped ' " \ characters
  '\''
;
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
LCURLY: "{";
RCURLY: "}";
LBRAC: "[";
RBRAC: "]";
LPAREN: "(";
RPAREN: ")";

protected
ESC :  '\\' ('n' | 't' | | '"' | '\'' | '\\');
