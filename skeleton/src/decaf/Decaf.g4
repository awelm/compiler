grammar Decaf;

@header {
    import java.util.*; 
}

/**
 * Parser Rules
 */

// start
program: CLASS ID LCURLY (field_decl)* (method_decl)* RCURLY EOF;

// field
field_decl: type field_decl_list SEMI;
field_decl_list: field_decl_item (COMMA field_decl_item)*;
field_decl_item: ID | (ID LBRAC int_literal RBRAC);

// methods
method_decl: (type | VOID) ID LPAREN (param_decl_csv)? RPAREN block;
method_call:
  ID LPAREN (expr)? (COMMA expr)* RPAREN |
  CALLOUT LPAREN STRING (COMMA callout_arg)* RPAREN;
callout_arg: expr | STRING;

// block
block: LCURLY (var_decl_csv SEMI)* (statement)*  RCURLY;
statement:
  location assign_op expr SEMI |
  IF LPAREN expr RPAREN block (ELSE block)? |
  FOR ID EQ expr COMMA expr block |
  method_call SEMI |
  RETURN (expr)? SEMI |
  BREAK SEMI |
  CONTINUE SEMI |
  block;

// expressions
expr: subexpr (bin_op subexpr)*;
subexpr:
  location |
  method_call |
  literal |
  SUB expr |
  NEGATE expr |
  LPAREN expr RPAREN;

// data access
location: ID (LBRAC expr RBRAC)?;

// function param and variable declarations
param_decl_csv: type ID (COMMA type ID)*;
var_decl_csv: type ID (COMMA ID)*;

// types 
type: INT | BOOLEAN;

// operations
bin_op: arith_op | rel_op | eq_op | COND_OP;
arith_op: ADD | MULT | DIV | MOD | SUB;
assign_op: EQ | ADD EQ | SUB EQ;
rel_op: LESS | GREATER | LESS EQ | GREATER EQ;
eq_op: EQ EQ | NEGATE EQ;

// literals
literal: int_literal | CHAR | bool_literal;
int_literal: DECIMAL_LITERAL | HEX_LITERAL;
bool_literal: TRUE | FALSE;



/**
 * Lexer Rules
 */

 // keywords
 CLASS: 'class';
 INT: 'int';
 BOOLEAN: 'boolean';
 VOID: 'void';
 TRUE: 'true';
 FALSE: 'false';
 RETURN: 'return';
 IF: 'if';
 ELSE: 'else';
 FOR: 'for';
 CALLOUT: 'callout';
 BREAK: 'break';
 CONTINUE: 'continue';


ID:
  ('a'..'z' | 'A'..'Z' | '_')
  ('a'..'z' | 'A'..'Z' | '_' | '0'..'9')*
;

WS_ : [ \t\n\r]+ -> skip;

SL_COMMENT : '//' ~[\r\n]* -> skip; 

CHAR : '\''
  (ESC|~('\'' | '"' | '\\')) // dont allow unescaped ' " \ characters
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
COND_OP: '&&' | '||';

//digits
DECIMAL_LITERAL : ('0'..'9')+;
HEX_LITERAL : '0x' ('0'..'9'|'a'..'f'|'A'..'F')+;

//all braces
LCURLY: '{';
RCURLY: '}';
LBRAC: '[';
RBRAC: ']';
LPAREN: '(';
RPAREN: ')';

ESC :  '\\' ('n' | 't' | | '"' | '\'' | '\\');