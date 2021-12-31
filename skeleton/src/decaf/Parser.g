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

/*
 * start
 */

// program: TK_class ID field_decl* method_decl* RCURLY EOF; 
program returns [IrClassDecl c] {c = new IrClassDecl(); IrFieldDecl fd; IrMethodDecl md;} : 
    TK_class id:ID
      {c.addName(id.getText());} LCURLY
    (fd=field_decl {c.addField(fd);})*
    (md=method_decl {c.addMethod(md);})*
    RCURLY EOF;

/*
 * field
 */

// field_decl: type field_decl_list SEMI; 
field_decl returns [IrFieldDecl f] {f = null; IrType t; List<String> fdl;} : 
  t=type
  fdl=field_decl_list
  SEMI
    {f = new IrFieldDecl(t, fdl);};

// field_decl_list: field_decl_item (COMMA field_decl_item)*; 
field_decl_list returns [List<String> l] {l= new List<String>(); String s;} :
  s=field_decl_item {l.add(s);}
  (
  COMMA s=field_decl_item
    {l.add(s);} 
  )*;

// field_decl_item: ID | (ID LBRAC int_literal RBRAC);
field_decl_item returns [String s] {s=null; String n=null;} :
  id:ID
    {s=id.getText();} 
  | 
  (idd:ID LBRAC n:int_literal RBRAC
    {s=idd.getText()+n;});

/*
 * methods
 */

// method_decl: (type | TK_void) ID LPAREN (param_decl_csv)? RPAREN block; 
method_decl returns [IrMethodDecl m] {m = null; IrType t=null; List<IrVarDecl> params=null; IrBlock b;} :
  (t=type | TK_void) name:ID LPAREN (params=param_decl_csv)? RPAREN b=block {m=new IrMethodDecl(t, name.getText(), params, b);};
method_call:
  ID LPAREN (expr)? (COMMA expr)* RPAREN |
  TK_callout LPAREN STRING (COMMA callout_arg)* RPAREN;
callout_arg: expr | STRING;

/*
 * blocks
 */

block returns [IrBlock b] {b=null;} : LCURLY (var_decl_csv SEMI)* (statement)*  RCURLY;
statement:
  location assign_op expr SEMI |
  TK_if LPAREN expr RPAREN block (TK_else block)? |
  TK_for ID EQ expr COMMA expr block |
  method_call SEMI |
  TK_return (expr)? SEMI |
  TK_break SEMI |
  TK_continue SEMI |
  block;

/*
 * expressions
 */

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
param_decl_csv returns [List<IrVarDecl> p] {p=new List<IrVarDecl>(); IrType t;} : 
  t=type n:ID
    {p.add(new IrVarDecl(t, n.getText()));}
  (COMMA t=type nn:ID
    {p.add(new IrVarDecl(t, nn.getText()));}
  )*;
var_decl_csv: type ID (COMMA ID)*;

// types 
// type: TK_int | TK_boolean; 
type returns [IrType t] {t=null;} :
  TK_int {t=new IrType(TK_int);} | TK_boolean {t=new IrType(TK_boolean);};

/*
 * operations
 */
bin_op: arith_op | rel_op | eq_op | COND_OP;
arith_op: ADD | MULT | DIV | MOD | SUB;
assign_op: EQ | ADD EQ | SUB EQ;
rel_op: LESS | GREATER | LESS EQ | GREATER EQ;
eq_op: EQ EQ | NEGATE EQ;

/*
 * literals
 */
literal: int_literal | CHAR | bool_literal;
int_literal: DECIMAL_LITERAL | HEX_LITERAL;
bool_literal: TK_true | TK_false;
