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
    public IrFieldDeclMember(IrType t, String n, Integer s) {
        type=t;
        name=n;
        size= s == null ? 1 : s.intValue();
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

class IrLocation extends IrExpression {
    private String id;
    private IrExpression indexExpr; // possibly null
    public String toString() {
        return String.format("IrLocation(id=%s, indexExpr=%b)", id, indexExpr != null);
    }
    public IrLocation(String i, IrExpression ie) {
        id=i;
        indexExpr=ie;
        if(indexExpr != null)
            children.add(indexExpr);
    }
};

abstract class IrLiteral extends IrExpression {};

class IrIntLiteral extends IrLiteral {
    int value;
    public String toString() {
        return String.format("IrIntLiteral(%d)", value);
    }
    public IrIntLiteral(int v) {
        value=v;
    }
};

class IrBoolLiteral extends IrLiteral {
    boolean value;
    public String toString() {
        return String.format("IrBoolLiteral(%b)", value);
    }
    public IrBoolLiteral(boolean v) {
        value=v;
    } 
};

// IrStringLiteral should only be used for callout args
class IrStringLiteral extends IrLiteral {
    String value;
    public String toString() {
        return String.format("IrStringLiteral(%s)", value);
    }
    public IrStringLiteral(String v) {
        value=v;
    } 
}; 

abstract class IrCallExpr extends IrExpression {};

class IrMethodCallExpr extends IrCallExpr {
    private String functionName;
    private ArrayList<IrExpression> args;
    public String toString() {
        return String.format("IrMethodCallExpr(funcName=%s, args=%d)", functionName, args.size());
    }
    public IrMethodCallExpr(String fn, ArrayList<IrExpression> as) {
        functionName=fn;
        args=as;
        addChildrenArrayList(args);
    }
};

class IrCalloutExpr extends IrCallExpr {
    private String functionName;
    private ArrayList<IrExpression> args;
    public String toString() {
        return String.format("IrCalloutExpr(funcName=%s, args=%d)", functionName, args.size());
    }
    public IrCalloutExpr(String fn, ArrayList<IrExpression> as) {
        functionName=fn;
        args=as;
        addChildrenArrayList(args);
    }
};

class IrUnaryOpExpr extends IrExpression {
    private String op;
    private IrExpression expr;
    public String toString() {
        return String.format("IrUnaryOpExpr(op=%s, expr=%b)", op, expr != null);
    }
    public IrUnaryOpExpr(String o, IrExpression e)  {
        op=o;
        expr=e;
        children.add(expr);
    }
}; 

class IrBinOpExpr extends IrExpression {
    private String op;
    private IrExpression leftExpr;
    private IrExpression rightExpr;
    public String toString() {
        return String.format("IrBinOpExpr(op=%s, leftExpr=%b, rightExpr=%b)", op, leftExpr != null, rightExpr != null);
    }
    public IrBinOpExpr(String o, IrExpression le, IrExpression re)  {
        op=o;
        leftExpr=le;
        rightExpr=re;
        children.add(leftExpr);
        children.add(rightExpr);
    }
}; 


// statements
abstract class IrStatement extends Ir {
   IrLocation location;
   IrExpression expression; 
   public String toString() {
       return String.format("IrAssignStmt(location=%b, expr=%b)", location != null, expression != null);
    }
};

class IrAssignStmt extends IrStatement {
   public IrAssignStmt(IrLocation l, IrExpression e) {
       location=l;
       expression=e;
       children.add(location);
       children.add(expression);
   }
}; 

class IrPlusAssignStmt extends IrStatement {
   public IrPlusAssignStmt(IrLocation l, IrExpression e) {
       location=l;
       expression=e;
       children.add(location);
       children.add(expression);
    } 
};

class IrMinusAssignStmt extends IrStatement {
   public IrMinusAssignStmt(IrLocation l, IrExpression e) {
       location=l;
       expression=e;
       children.add(location);
       children.add(expression);
    } 
};

class IrBreakStmt extends IrStatement {
    public String toString() {
        return String.format("IrBreakStmt()");
     }
};
class IrContinueStmt extends IrStatement {
    public String toString() {
        return String.format("IrContinueStmt()");
     }
};

class IrIfStmt extends IrStatement {
    private IrExpression condition;
    private IrBlock ifBlock;
    private IrBlock elseBlock; // possibly null
    public String toString() {
        return String.format("IrIfStmt(cond=%b, ifBlock=%b, elseBlock=%b)", condition != null, ifBlock != null, elseBlock != null);
    }
    public IrIfStmt(IrExpression c, IrBlock ib, IrBlock eb) {
        condition=c;
        ifBlock=ib;
        elseBlock=eb;
        children.add(condition);
        children.add(ifBlock);
        if(elseBlock != null) 
            children.add(elseBlock);
     } 
};

class IrForStmt extends IrStatement {
    // Assignment that gives the loop var its initial value. The loop var gets incremented after each loop body
    private IrAssignStmt loopVarAssignment; 

    // Loop terminates when loop var reaches this expression value. This expression is only evaluated once when
    // the loop first starts 
    private IrExpression loopVarEndVal; 

    private IrBlock body;

    public String toString() {
        return String.format("IrForStmt(assign=%b, endExpr=%b, body=%b)", loopVarAssignment != null, loopVarEndVal != null, body != null);
     }

    public IrForStmt(IrAssignStmt lva, IrExpression lvev, IrBlock b) {
        loopVarAssignment=lva;
        loopVarEndVal=lvev;
        body=b;  
        children.add(loopVarAssignment); 
        children.add(loopVarEndVal); 
        children.add(body);
    }
};

class IrReturnStmt extends IrStatement {
    private IrExpression expression;
    public String toString() {
        return String.format("IrReturnStmt(expr=%b)", expression != null);
     }
    public IrReturnStmt(IrExpression e) {
        expression=e;
        children.add(expression);
    }
};

class IrInvokeStmt extends IrStatement {
    private IrMethodCallExpr methodCallExpression; // possibly null
    private IrCalloutExpr calloutExpression; // possibly null
    public String toString() {
        return String.format("IrInvokeStmt(methodCall=%b, callout=%b)", methodCallExpression != null, calloutExpression != null);
     }
    public IrInvokeStmt(IrMethodCallExpr mce, IrCalloutExpr ce) {
        methodCallExpression=mce;
        calloutExpression=ce; 
        if(methodCallExpression != null)
            children.add(methodCallExpression);
        if(calloutExpression != null)
            children.add(calloutExpression);
    }
};

class IrBlock extends IrStatement {
    private ArrayList<IrVarDecl> vars = new ArrayList();
    private ArrayList<IrStatement> statements = new ArrayList();
    public String toString() {
        return String.format("IrBlock(vars=%d, statements=%d)", vars.size(), statements.size());
    }
    public IrBlock(ArrayList<IrVarDecl> vs, ArrayList<IrStatement> ss) {
        vars = vs;
        statements = ss;
        addChildrenArrayList(vs);
        addChildrenArrayList(ss);
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
    public IrType(DecafParser.TypeContext tc, DecafParser.Int_literalContext intLiteral) {
        if(tc.INT() != null)
            type="int";
        else if(tc.BOOLEAN() != null)
            type="boolean";

        if(intLiteral != null)
            isArray=true;
    }
};

