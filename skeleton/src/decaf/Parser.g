header {package decaf;}

options
{
  mangleLiteralPrefix = "TK_";
  language="Java";
}

class DecafParser extends Parser;
options
{
  importVocab=DecafScanner;
  k=3;
  buildAST=true;
}

// start
program returns [IrClassDecl c] {c = new IrClassDecl(); IrFieldDecl fd=null; IrMethodDecl md=null;} : 
    TK_class id:ID
      {c.addName(id.getText());} LCURLY
    (fd=field_decl {c.addField(fd);})*
    (md=method_decl {c.addMethod(md);})*
    RCURLY EOF;

// field
field_decl returns [IrFieldDecl f] {f = new IrFieldDecl();} : type field_decl_list SEMI;
field_decl_list: field_decl_item (COMMA field_decl_item)*;
field_decl_item: ID | (ID LBRAC int_literal RBRAC);

// methods
method_decl returns [IrMethodDecl m] {m = new IrMethodDecl();} : (type | TK_void) ID LPAREN (param_decl_csv)? RPAREN block;
method_call:
  ID LPAREN (expr)? (COMMA expr)* RPAREN |
  TK_callout LPAREN STRING (COMMA callout_arg)* RPAREN;
callout_arg: expr | STRING;

// block
block: LCURLY (var_decl_csv SEMI)* (statement)*  RCURLY;
statement:
  location assign_op expr SEMI |
  TK_if LPAREN expr RPAREN block (TK_else block)? |
  TK_for ID EQ expr COMMA expr block |
  method_call SEMI |
  TK_return (expr)? SEMI |
  TK_break SEMI |
  TK_continue SEMI |
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
type: TK_int | TK_boolean;

// operations
bin_op: arith_op | rel_op | eq_op | COND_OP;
arith_op: ADD | MULT | DIV | MOD | SUB;
assign_op: EQ | ADD EQ | SUB EQ;
rel_op: LESS | GREATER | LESS EQ | GREATER EQ;
eq_op: EQ EQ | NEGATE EQ;

// literals
literal: int_literal | CHAR | bool_literal;
int_literal: DECIMAL_LITERAL | HEX_LITERAL;
bool_literal: TK_true | TK_false;
