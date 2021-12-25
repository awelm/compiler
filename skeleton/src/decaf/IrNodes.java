package decaf;
import java.util.ArrayList;

abstract class Ir {
    public void visit() {}
};

// class
class IrClassDecl extends Ir {
    private String name;
    private ArrayList<IrFieldDecl> fields = new ArrayList<IrFieldDecl>();
    private ArrayList<IrMethodDecl> methods = new ArrayList<IrMethodDecl>();
    public void addName(String n) {
        name=n;
    }
    public void addField(IrFieldDecl field) {
        fields.add(field);
    }
    public void addMethod(IrMethodDecl method) {
        methods.add(method);
    }
    public void visit() {
        String output = String.format("ClassName=%s, fields=%s, methods=%s", name, fields, methods);
        System.out.println(output);
    }
};

// class methods and fields
abstract class IrMemberDecl extends Ir {};
class IrFieldDecl extends IrMemberDecl {};
class IrMethodDecl extends IrMemberDecl {};

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

// vars and type
class IrVarDecl extends Ir {};
class IrType extends Ir {};