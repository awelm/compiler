import java.util.ArrayList;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

abstract class Ir {
    ArrayList<Ir> children = new ArrayList<Ir>();

    public void prettyPrint(int indent) {
        System.out.print(this.toString().indent(indent));
        for(Ir child : children)
            child.prettyPrint(indent+1);
    }

    public void prettyPrint() {
        prettyPrint(0);
    }

    public void addChildrenArrayList(ArrayList<? extends Ir> newChildren) {
        if(newChildren != null)
            for(Ir node : newChildren)
                children.add(node);
    }
};

// class
class IrClassDecl extends Ir {
    private String className;
    private ArrayList<IrFieldDecl> fields;
    private ArrayList<IrMethodDecl> methods;
    public String toString() {
        return String.format("IrClassDecl(className=%s, fields=%d, methods=%d)", className, fields.size(), methods.size());
    }
    public IrClassDecl(String cn, ArrayList<IrFieldDecl> fl, ArrayList<IrMethodDecl> ml) {
        className=cn;
        fields=fl;
        methods=ml;
        addChildrenArrayList(fl);
        addChildrenArrayList(ml);
    }
};

// class methods and fields
abstract class IrMemberDecl extends Ir {};

class IrFieldDecl extends IrMemberDecl {
    private ArrayList<IrFieldDeclMember> members = new ArrayList<>();
    public String toString() {
        return String.format("IrFieldDecl(members=%d)", members.size());
    }
    public IrFieldDecl(ArrayList<IrFieldDeclMember> m) {
        members=m;
        addChildrenArrayList(m);
    }
};

class IrFieldDeclMember extends Ir {
    private IrType type;
    private String name;
    private int size;
    public String toString() {
        return String.format("IrFieldDeclMember(type=%b, name=%s, size=%d)", type!=null, name, size);
    }
    public IrFieldDeclMember(IrType t, String n, int s) {
        type=t;
        name=n;
        size=s;
        children.add(t);
    } 
}

class IrMethodDecl extends IrMemberDecl {
    private IrType returnType; // possible null
    private String methodName;
    private ArrayList<IrVarDecl> parameters = new ArrayList();
    private IrBlock body;
    public String toString() {
        return String.format("IrMethodDecl(returnType=%b, methodName=%s, parameters=%d, body=%b)", returnType!=null, methodName, parameters.size(), body!=null);
    }
    public IrMethodDecl(IrType rt, String name, ArrayList<IrVarDecl> params, IrBlock b) {
        returnType=rt;
        if(returnType != null)
            children.add(returnType);

        methodName=name;

        if(params != null)
            parameters=params;
            for(Ir param : parameters)
                children.add(param);

        body=b;
        children.add(b);
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

class IrBlock extends IrStatement {
    private ArrayList<IrVarDecl> vars = new ArrayList();
    private ArrayList<IrStatement> statements = new ArrayList();
    public String toString() {
        return String.format("IrBlock(vars=%d, statements=%d)", vars.size(), statements.size());
    }
    public void addVars(ArrayList<IrVarDecl> newVars) {
        for(IrVarDecl newVar : newVars) {
            vars.add(newVar);
            children.add(newVar);
        }
    }
    public void addStatement(IrStatement s) {
        statements.add(s);
        children.add(s);
    }
};

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
    private String type;
    private boolean isArray=false;
    public String toString() {
        return String.format("IrType(type=%s, isArray=%b)", type, isArray);
    }
    public IrType(DecafParser.TypeContext tc, TerminalNode indexToken) {
        if(tc.INT().getText() != "0")
            type="int";
        else if(tc.BOOLEAN().getText() != "0")
            type="boolean";

        if(indexToken != null)
            isArray=true;
    }
};
