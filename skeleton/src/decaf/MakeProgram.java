import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.Stack;
import java.util.*;

class MakeProgram implements DecafListener {
    private Stack<Ir> stack = new Stack<>();

    public Ir getAst() {
        return stack.get(0);
    }

    public void enterProgram(DecafParser.ProgramContext context) {
    }

    public void exitProgram(DecafParser.ProgramContext context) {
        System.err.println("exiting program");
        String className = context.ID().getText();
        // create methods
        int methodDeclsCount = context.method_decl().size();
        ArrayList<IrMethodDecl> methodDecls = (ArrayList<IrMethodDecl>) Utils.popListFromStackAndReverse(stack, methodDeclsCount);

        // create fields
        int fieldDeclsCount = context.field_decl().size();
        ArrayList<IrFieldDecl> fieldDecls = (ArrayList<IrFieldDecl>) Utils.popListFromStackAndReverse(stack, fieldDeclsCount); 

        IrClassDecl cd = new IrClassDecl(className, fieldDecls, methodDecls);
        stack.push(cd);
    }

    public void enterField_decl(DecafParser.Field_declContext ctx) {
    }

    public void exitField_decl(DecafParser.Field_declContext ctx) {
        System.err.println("exiting field_decl");
        ArrayList<IrFieldDeclMember> members = new ArrayList<IrFieldDeclMember>();
        List<DecafParser.Field_decl_itemContext> fieldNames = ctx.field_decl_list().field_decl_item();

        for (DecafParser.Field_decl_itemContext field : fieldNames) {
            String name = field.ID().getText();
            DecafParser.Int_literalContext intLiteral = field.int_literal();
            IrType irType = Utils.createIrType(ctx.type(), intLiteral);
            Integer size = Utils.getValueFromIntLiteral(intLiteral);
            IrFieldDeclMember member = new IrFieldDeclMember(irType, name, size);
            members.add(member);
        }
        IrFieldDecl fieldDecl = new IrFieldDecl(members);
        stack.push(fieldDecl);
    }

    public void enterField_decl_list(DecafParser.Field_decl_listContext ctx) {
    }

    public void exitField_decl_list(DecafParser.Field_decl_listContext ctx) {
        System.err.println("exiting field_decl_list");
    }

    public void enterField_decl_item(DecafParser.Field_decl_itemContext ctx) {
    }

    public void exitField_decl_item(DecafParser.Field_decl_itemContext ctx) {
        System.err.println("exiting field_decl_item");
    }

    public void enterMethod_decl(DecafParser.Method_declContext ctx) {
    }

    public void exitMethod_decl(DecafParser.Method_declContext ctx) {
        System.err.println("exiting method_decl");
        IrType methodReturnType = Utils.createIrType(ctx.type(), null); // return types can't be arrays
        String methodName = ctx.ID().getText();
        IrBlock body = (IrBlock) stack.pop();
        int paramCount = ctx.param_decl_csv() == null ? 0 : ctx.param_decl_csv().type().size();
        ArrayList<IrVarDecl> params = (ArrayList<IrVarDecl>) Utils.popListFromStackAndReverse(stack, paramCount);
        IrMethodDecl methodDecl = new IrMethodDecl(methodReturnType, methodName, params, body);
        stack.push(methodDecl);
    }

    public void enterMethod_call(DecafParser.Method_callContext ctx) {
    }

    public void exitMethod_call(DecafParser.Method_callContext ctx) {
        System.err.println("exiting method_call");
    }

    public void enterCallout_arg(DecafParser.Callout_argContext ctx) {
    }

    public void exitCallout_arg(DecafParser.Callout_argContext ctx) {
        System.err.println("exiting callout_arg");
    }

    public void enterBlock(DecafParser.BlockContext ctx) {
    }

    public void exitBlock(DecafParser.BlockContext ctx) {
        System.err.println("exiting block");
        int statementCount = ctx.statement().size();
        ArrayList<IrStatement> statements = (ArrayList<IrStatement>) Utils.popListFromStackAndReverse(stack, statementCount); 
        int varDeclCount = ctx.var_decl_csv().size();
        ArrayList<IrVarDecl> varDecls = (ArrayList<IrVarDecl>) Utils.popListFromStackAndReverse(stack, varDeclCount);  
        stack.push(new IrBlock(varDecls, statements));
    }

    public void enterStatement(DecafParser.StatementContext ctx) {
    }

    public void exitStatement(DecafParser.StatementContext ctx) {
        System.err.println("exiting statement");
        IrStatement statement = null;
        if(ctx.IF() != null) {
            // if statement
            IrBlock elseBlock = null;
            if(ctx.ELSE() != null)
                elseBlock = (IrBlock) stack.pop();
            IrBlock ifBlock = (IrBlock) stack.pop();
            IrExpression condition = (IrExpression) stack.pop();
            stack.push(new IrIfStmt(condition, ifBlock, elseBlock));
        }
        else if(ctx.FOR() != null) {
            // for statement
            IrBlock body = (IrBlock) stack.pop();
            IrExpression loopVarEndExpression = (IrExpression) stack.pop();
            IrAssignStmt loopVarAssignStatement = (IrAssignStmt) stack.pop();
            stack.push(new IrForStmt(loopVarAssignStatement, loopVarEndExpression, body));
        }
        else if(ctx.RETURN() != null) {
            // return statement
            stack.push(new IrReturnStmt((IrExpression) stack.pop()));
        }
        else if(ctx.BREAK() != null) {
            // break statement
            stack.push(new IrBreakStmt());
        }
        else if(ctx.CONTINUE() != null) {
            // continue statement
            stack.push(new IrContinueStmt());
        }
    }

    public void enterExpr(DecafParser.ExprContext ctx) {
    }

    public void exitExpr(DecafParser.ExprContext ctx) {
        System.err.println("exiting expr");
        int subExprCount = ctx.subexpr().size(); 
        int binOpCount = ctx.bin_op().size();
        //TODO: iterate through list of binOps and reduce one at a time based on order and highest precedence
        // multiplication, division, and mod have highest precedence
        // then addition and subtraction
        // then less-than and greater-than
        // then equality and inequality
        // then &&
        // then ||
    }

    public void enterSubexpr(DecafParser.SubexprContext ctx) {
    }

    public void exitSubexpr(DecafParser.SubexprContext ctx) {
        System.err.println("exiting subexpr");

        // location
        if(ctx.location() != null) {
            DecafParser.LocationContext locationCtx = ctx.location();
            String id = locationCtx.ID().getText();
            if(locationCtx.expr() == null) {
                // not indexed location
                stack.push(new IrLocation(id, null));
            } else {
                IrExpression locationIndexExpr = (IrExpression) stack.pop();
                stack.push(new IrLocation(id, locationIndexExpr));
            }
        }
        // method call
        else if(ctx.method_call() != null) {
            DecafParser.Method_callContext callContext = ctx.method_call(); 
            if(callContext.CALLOUT() != null) {
                // callout expression
                String functionName = callContext.STRING().getText();
                ArrayList<IrExpression> args = new ArrayList();
                // Note for callout expressions we can't always rely on the stack because string arguments
                // are also possible, which aren't captured by IrExpressions or other Ir nodes
                List<DecafParser.Callout_argContext> argCtxs = callContext.callout_arg(); 
                Collections.reverse(argCtxs);
                for(DecafParser.Callout_argContext argCtx : argCtxs) {
                    if(argCtx.STRING() != null)
                        args.add(new IrStringLiteral(argCtx.STRING().getText()));
                    else
                        args.add((IrExpression) stack.pop());
                }
                stack.push(new IrMethodCallExpr(functionName, args));
            } else {
                String functionName = callContext.ID().getText();
                int expressionCount = callContext.expr().size();
                ArrayList<IrExpression> args = (ArrayList<IrExpression>) Utils.popListFromStackAndReverse(stack, expressionCount);
                stack.push(new IrMethodCallExpr(functionName, args));
            }
        }
        // literal
        else if(ctx.literal() != null) {
            DecafParser.LiteralContext literalCtx = ctx.literal(); 
            if(literalCtx.int_literal() != null) {
                Integer value = Utils.getValueFromIntLiteral(literalCtx.int_literal());
                stack.push(new IrIntLiteral(value));
            }
            else if(literalCtx.bool_literal() != null) {
                Boolean value = Utils.getValueFromBoolLiteral(literalCtx.bool_literal());
                stack.push(new IrBoolLiteral(value));
            }
            else {
                // must be a char, so we convert it to the integer ASCII value
                char c = literalCtx.CHAR().getText().charAt(0);
                stack.push(new IrIntLiteral((int) c));
            }
        }
        // multiply by -1
        else if(ctx.SUB() != null)
            stack.push(new IrUnaryOpExpr("-", (IrExpression) stack.pop()));
        // invert
        else if(ctx.NEGATE() != null)
            stack.push(new IrUnaryOpExpr("!", (IrExpression) stack.pop())); 
    }

    public void enterLocation(DecafParser.LocationContext ctx) {
    }

    public void exitLocation(DecafParser.LocationContext ctx) {
        System.err.println("exiting location");
    }

    public void enterParam_decl_csv(DecafParser.Param_decl_csvContext ctx) {
    }

    public void exitParam_decl_csv(DecafParser.Param_decl_csvContext ctx) {
        System.err.println("exiting param_decl_csv");
        int paramCount = ctx.type().size();
        for (int paramIndex = 0; paramIndex < paramCount; paramIndex++) {
            IrType paramType = Utils.createIrType(ctx.type(paramIndex), null); // params can't be arrays
            String paramName = ctx.ID(paramIndex).getText();
            stack.push(new IrVarDecl(paramType, paramName));
        }
    }

    public void enterVar_decl_csv(DecafParser.Var_decl_csvContext ctx) {
    }

    public void exitVar_decl_csv(DecafParser.Var_decl_csvContext ctx) {
        System.err.println("exiting var_decl_csv");
        int varDeclCount = ctx.ID().size();
        IrType varType = Utils.createIrType(ctx.type(), null); // vars can't be arrays
        for (int varDeclIndex = 0; varDeclIndex < varDeclCount; varDeclIndex++) {
            String varName = ctx.ID(varDeclIndex).getText();
            stack.push(new IrVarDecl(varType, varName));
        } 
    }

    public void enterType(DecafParser.TypeContext ctx) {
    }

    public void exitType(DecafParser.TypeContext ctx) {
        System.err.println("exiting type");
    }

    public void enterBin_op(DecafParser.Bin_opContext ctx) {
    }

    public void exitBin_op(DecafParser.Bin_opContext ctx) {
        System.err.println("exiting bin_op");
    }

    public void enterArith_op(DecafParser.Arith_opContext ctx) {
    }

    public void exitArith_op(DecafParser.Arith_opContext ctx) {
        System.err.println("exiting arith_op");
    }

    public void enterAssign_op(DecafParser.Assign_opContext ctx) {
    }

    public void exitAssign_op(DecafParser.Assign_opContext ctx) {
        System.err.println("exiting assign_op");
    }

    public void enterRel_op(DecafParser.Rel_opContext ctx) {
    }

    public void exitRel_op(DecafParser.Rel_opContext ctx) {
        System.err.println("exiting rel_op");
    }

    public void enterEq_op(DecafParser.Eq_opContext ctx) {
    }

    public void exitEq_op(DecafParser.Eq_opContext ctx) {
        System.err.println("exiting eq_op");
    }

    public void enterLiteral(DecafParser.LiteralContext ctx) {
    }

    public void exitLiteral(DecafParser.LiteralContext ctx) {
        System.err.println("exiting literal");
    }

    public void enterInt_literal(DecafParser.Int_literalContext ctx) {
    }

    public void exitInt_literal(DecafParser.Int_literalContext ctx) {
        System.err.println("exiting int_literal");
    }

    public void enterBool_literal(DecafParser.Bool_literalContext ctx) {
    }

    public void exitBool_literal(DecafParser.Bool_literalContext ctx) {
        System.err.println("exiting bool_literal");
    }

    public void visitTerminal(TerminalNode terminal) {
    }

    // don't need these here, so just make them empty implementations
    public void enterEveryRule(ParserRuleContext context) {
    }

    public void exitEveryRule(ParserRuleContext context) {
    }

    public void visitErrorNode(ErrorNode node) {
    }
}