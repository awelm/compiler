package decaf;
import java.util.ArrayList;

abstract class Ir {
    List<Ir> children = new List<Ir>();

    public void prettyPrint(int indent) {
        System.out.print(this.toString().indent(indent));
        for(Ir child : children)
            child.prettyPrint(indent+1);
    }

    public void prettyPrint() {
        prettyPrint(0);
    }

    public void addChildrenList(List<Ir> newChildren) {
        for(Ir node : newChildren)
            children.add(node);
    }
};

// class
class IrClassDecl extends Ir {
    private String className;
    private List<IrFieldDecl> fields = new List<IrFieldDecl>();
    private List<IrMethodDecl> methods = new List<IrMethodDecl>();
    public String toString() {
        return String.format("IrClassDecl(className=%s, fields=%d, methods=%d)", className, fields.size(), methods.size());
    }
    public void addName(String n) {
        className=n;
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
    public String toString() {
        return String.format("IrFieldDecl(members=%d)", members.size());
    }
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
    public String toString() {
        return String.format("IrFieldDeclMember(type=%b, name=%s)", type!=null, name);
    }
    public IrFieldDeclMember(IrType t, String n) {
        type=t;
        name=n;
        children.add(t);
    } 
}

class IrMethodDecl extends IrMemberDecl {
    private IrType returnType;
    private String methodName;
    private List<IrVarDecl> parameters;
    private IrBlock body;
    public String toString() {
        return String.format("IrMethodDecl(returnType=%b, methodName=%s, parameters=%d, body=%b)", returnType!=null, methodName, parameters.size(), body!=null);
    }
    public IrMethodDecl(IrType rt, String name, List<IrVarDecl> params, IrBlock b) {
        returnType=rt;
        methodName=name;
        parameters=params;
        body=b;
        children.add(returnType); //TODO: add body once ready
        if(params != null)
            for(Ir param : parameters)
                children.add(param);
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

// vars and type
class IrVarDecl extends Ir {
    private IrType type;
    private String name;
    public String toString() {
        return String.format("IrVarDecl(type=%b, name=%s)", type!=null, name);
    }
    public IrVarDecl(IrType t, String n) {
        type=t;
        name=n;
        children.add(t);
    }
};

class IrType extends Ir {
    private int type;
    public String toString() {
        return String.format("IrType(type=%d)", type);
    }
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