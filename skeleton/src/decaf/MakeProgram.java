import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.Stack;
import java.util.*;
class MakeProgram implements DecafListener {
    private Stack<Ir> stack = new Stack<>();

    public Ir getAst() {
        return stack.get(0);
    }

    public void enterProgram(DecafParser.ProgramContext context) {}

    public void exitProgram(DecafParser.ProgramContext context) {
        System.err.println("exiting program");
        String className = context.ID().getText();
        int fieldDeclsCount = context.field_decl().size();
        ArrayList<IrFieldDecl> fieldDecls = new ArrayList<IrFieldDecl>(); 
        for(int x=0; x<fieldDeclsCount; x++) {
            IrFieldDecl fd = (IrFieldDecl) stack.pop();
            fieldDecls.add(fd);
        }
        Collections.reverse(fieldDecls);
        ArrayList<IrMethodDecl> methodDecls = new ArrayList<IrMethodDecl>(); 
        IrClassDecl cd = new IrClassDecl(className, fieldDecls, methodDecls);
        stack.push(cd);
    }

    public void enterField_decl(DecafParser.Field_declContext ctx) {}

	public void exitField_decl(DecafParser.Field_declContext ctx) {
        System.err.println("exiting field_decl");
        ArrayList<IrFieldDeclMember> members = new ArrayList<IrFieldDeclMember>(); 
        DecafParser.TypeContext fieldType = ctx.type();
        List<DecafParser.Field_decl_itemContext> fieldNames = ctx.field_decl_list().field_decl_item();

        for(DecafParser.Field_decl_itemContext field : fieldNames) {
            String name = field.ID().getText(); 
            DecafParser.Int_literalContext intLiteral = field.int_literal();
            TerminalNode indexToken = null;
            if(intLiteral != null)
                indexToken = intLiteral.DECIMAL_LITERAL() != null ? intLiteral.DECIMAL_LITERAL() : intLiteral.HEX_LITERAL(); 
            IrType irType = new IrType(fieldType, indexToken);
            int size = 1; 
            if(indexToken != null && intLiteral.DECIMAL_LITERAL() != null)
                size=Integer.parseInt(indexToken.getText());
            else if(indexToken != null && intLiteral.HEX_LITERAL() != null) {
                String hexWithoutPrefix = indexToken.getText().substring(2);
                size=Integer.parseInt(hexWithoutPrefix, 16);
            }
            IrFieldDeclMember member = new IrFieldDeclMember(irType, name, size);
            members.add(member);
        }
        IrFieldDecl fieldDecl = new IrFieldDecl(members);
        stack.push(fieldDecl);
    }

	public void enterField_decl_list(DecafParser.Field_decl_listContext ctx) {}

	public void exitField_decl_list(DecafParser.Field_decl_listContext ctx) {
        System.err.println("exiting field_decl_list");
    }

	public void enterField_decl_item(DecafParser.Field_decl_itemContext ctx) {}

	public void exitField_decl_item(DecafParser.Field_decl_itemContext ctx) {
        System.err.println("exiting field_decl_item");
    } 

	public void enterMethod_decl(DecafParser.Method_declContext ctx) {}

	public void exitMethod_decl(DecafParser.Method_declContext ctx) {
        System.err.println("exiting method_decl"); 
    }

	public void enterMethod_call(DecafParser.Method_callContext ctx) {}

	public void exitMethod_call(DecafParser.Method_callContext ctx) {
        System.err.println("exiting method_call");
    }

	public void enterCallout_arg(DecafParser.Callout_argContext ctx) {} 

	public void exitCallout_arg(DecafParser.Callout_argContext ctx) {
        System.err.println("exiting callout_arg");
    }

	public void enterBlock(DecafParser.BlockContext ctx) {}

	public void exitBlock(DecafParser.BlockContext ctx) {
        System.err.println("exiting block");
    }

	public void enterStatement(DecafParser.StatementContext ctx) {}

	public void exitStatement(DecafParser.StatementContext ctx) {
        System.err.println("exiting statement");
    }

	public void enterExpr(DecafParser.ExprContext ctx) {}

	public void exitExpr(DecafParser.ExprContext ctx) {
        System.err.println("exiting expr");
    }

	public void enterSubexpr(DecafParser.SubexprContext ctx) {}

	public void exitSubexpr(DecafParser.SubexprContext ctx) {
        System.err.println("exiting subexpr");
    }

	public void enterLocation(DecafParser.LocationContext ctx) {}

	public void exitLocation(DecafParser.LocationContext ctx) {
        System.err.println("exiting location");
    }

	public void enterParam_decl_csv(DecafParser.Param_decl_csvContext ctx) {}

	public void exitParam_decl_csv(DecafParser.Param_decl_csvContext ctx) {
        System.err.println("exiting param_decl_csv");
    }

	public void enterVar_decl_csv(DecafParser.Var_decl_csvContext ctx) {}

	public void exitVar_decl_csv(DecafParser.Var_decl_csvContext ctx) {
        System.err.println("exiting var_decl_csv");
    }

	public void enterType(DecafParser.TypeContext ctx) {}

	public void exitType(DecafParser.TypeContext ctx) {
        System.err.println("exiting type");
    }

	public void enterBin_op(DecafParser.Bin_opContext ctx) {}

	public void exitBin_op(DecafParser.Bin_opContext ctx) {
        System.err.println("exiting bin_op");
    } 

	public void enterArith_op(DecafParser.Arith_opContext ctx) {}

	public void exitArith_op(DecafParser.Arith_opContext ctx) {
        System.err.println("exiting arith_op");
    } 

	public void enterAssign_op(DecafParser.Assign_opContext ctx) {}

	public void exitAssign_op(DecafParser.Assign_opContext ctx) {
        System.err.println("exiting assign_op");
    }

	public void enterRel_op(DecafParser.Rel_opContext ctx) {}

	public void exitRel_op(DecafParser.Rel_opContext ctx) {
        System.err.println("exiting rel_op");
    }

	public void enterEq_op(DecafParser.Eq_opContext ctx) {}

	public void exitEq_op(DecafParser.Eq_opContext ctx) {
        System.err.println("exiting eq_op");
    }

	public void enterLiteral(DecafParser.LiteralContext ctx) {}

	public void exitLiteral(DecafParser.LiteralContext ctx) {
        System.err.println("exiting literal");
    }

	public void enterInt_literal(DecafParser.Int_literalContext ctx) {}

	public void exitInt_literal(DecafParser.Int_literalContext ctx) {
        System.err.println("exiting int_literal");
    }

	public void enterBool_literal(DecafParser.Bool_literalContext ctx) {}

	public void exitBool_literal(DecafParser.Bool_literalContext ctx) {
        System.err.println("exiting bool_literal");
    }

    public void visitTerminal(TerminalNode terminal) {}

    // don't need these here, so just make them empty implementations
    public void enterEveryRule(ParserRuleContext context) {}
    public void exitEveryRule(ParserRuleContext context) {}
    public void visitErrorNode(ErrorNode node) { }         
}