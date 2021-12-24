package decaf;

abstract class Ir {};

// class
class IrClassDecl extends Ir {
    private String name;
    void setName(String n) {
        name=n;
    }
};

// expressions
abstract class IrExpression extends Ir {};

abstract class IrLiteral extends IrExpression {};
class IrIntLiteral extends IrLiteral {};
class IrBoolLiteral extends IrLiteral {};

abstract class IrCallExpr extends IrExpression {};
class IrMethodCallExpr extends IrCallExpr {};
class IrCalloutExpr extends IrCallExpr {};

class IrBinopExpr extends IrExpression {}; 

// statements
abstract class IrStatement extends Ir {};
class IrAssignStmt extends IrStatement {}; 
class IrPlusAssignStmt extends IrStatement {};
class IrBreakStmt extends IrStatement {};
class IrContinueStmt extends IrStatement {};
class IrIfStmt extends IrStatement {};
class IrForStmt extends IrStatement {};
class IrReturnStmt extends IrStatement {};
class IrInvokeStmt extends IrStatement {};
class IrBlock extends IrStatement {};

// member
abstract class IrMemberDecl extends Ir {};
class IrMethodDecl extends IrMemberDecl {};
class IrFieldDecl extends IrMemberDecl {};

// vars and type
class IrVarDecl extends Ir {};
class IrType extends Ir {};