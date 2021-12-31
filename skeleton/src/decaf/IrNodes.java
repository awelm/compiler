package decaf;
import java.util.ArrayList;

abstract class Ir {
    List<Ir> children = new List<Ir>();

    public void prettyPrint(int indent) {
        System.out.print(this.getClass().getSimpleName().indent(indent));
        for(Ir child : children)
            child.prettyPrint(indent+1);
    }

    public void prettyPrint() {
        prettyPrint(0);
    }
};

// class
class IrClassDecl extends Ir {
    private String name;
    private List<IrFieldDecl> fields = new List<IrFieldDecl>();
    private List<IrMethodDecl> methods = new List<IrMethodDecl>();
    public void addName(String n) {
        name=n;
    }
    public void addField(IrFieldDecl field) {
        fields.add(field);
        children.add(field);
    }
    public void addMethod(IrMethodDecl method) {
        methods.add(method);
        children.add(method);
    }
};

// class methods and fields
abstract class IrMemberDecl extends Ir {};

class IrFieldDecl extends IrMemberDecl {
    private List<IrFieldDeclMember> members = new List<IrFieldDeclMember>();
    public IrFieldDecl(IrType type, List<String> names) {
        for(String name : names) {
            IrFieldDeclMember member = new IrFieldDeclMember(type, name);
            members.add(member);
            children.add(member);
        }
    }
};

class IrFieldDeclMember extends Ir {
    private IrType type;
    private String name;
    public IrFieldDeclMember(IrType t, String n) {
        type=t;
        name=n;
        children.add(t);
    } 
}

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
class IrVarDecl extends Ir {
    private IrType type;
    private String name;
    public IrVarDecl(IrType t, String n) {
        type=t;
        name=n;
        children.add(t);
    }
};

class IrType extends Ir {
    private int type;
    public IrType(int t) {
        type=t;
    }
};


// Helpers
class List<T> extends ArrayList<T> {
    public List() {
        super();
    }
}