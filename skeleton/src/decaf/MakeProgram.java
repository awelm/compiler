import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.Stack;
import java.util.ArrayList;

class MakeProgram implements DecafListener {
    private Stack<Ir> stack = new Stack<>();

    public Ir getAst() {
        return stack.get(0);
    }

    public void enterProgram(DecafParser.ProgramContext context) {
        System.err.println("entering program");
    }
    public void exitProgram(DecafParser.ProgramContext context) {
        System.err.println("exiting program");
        ArrayList<IrFieldDecl> fieldDecls = new ArrayList<IrFieldDecl>(); 
        ArrayList<IrMethodDecl> methodDecls = new ArrayList<IrMethodDecl>(); 
        IrClassDecl cd = new IrClassDecl("Program", fieldDecls, methodDecls);
        stack.push(cd);
    }

    public void enterField_decl(DecafParser.Field_declContext ctx) {}

	public void exitField_decl(DecafParser.Field_declContext ctx) {}

	public void enterField_decl_list(DecafParser.Field_decl_listContext ctx) {}

	public void exitField_decl_list(DecafParser.Field_decl_listContext ctx) {}

	public void enterField_decl_item(DecafParser.Field_decl_itemContext ctx) {}

	public void exitField_decl_item(DecafParser.Field_decl_itemContext ctx) {} 

	public void enterMethod_decl(DecafParser.Method_declContext ctx) {}

	public void exitMethod_decl(DecafParser.Method_declContext ctx) {}

	public void enterMethod_call(DecafParser.Method_callContext ctx) {}

	public void exitMethod_call(DecafParser.Method_callContext ctx) {}

	public void enterCallout_arg(DecafParser.Callout_argContext ctx) {} 

	public void exitCallout_arg(DecafParser.Callout_argContext ctx) {}

	public void enterBlock(DecafParser.BlockContext ctx) {}

	public void exitBlock(DecafParser.BlockContext ctx) {}

	public void enterStatement(DecafParser.StatementContext ctx) {}

	public void exitStatement(DecafParser.StatementContext ctx) {}

	public void enterExpr(DecafParser.ExprContext ctx) {}

	public void exitExpr(DecafParser.ExprContext ctx) {}

	public void enterSubexpr(DecafParser.SubexprContext ctx) {}

	public void exitSubexpr(DecafParser.SubexprContext ctx) {}

	public void enterLocation(DecafParser.LocationContext ctx) {}

	public void exitLocation(DecafParser.LocationContext ctx) {}

	public void enterParam_decl_csv(DecafParser.Param_decl_csvContext ctx) {}

	public void exitParam_decl_csv(DecafParser.Param_decl_csvContext ctx) {}

	public void enterVar_decl_csv(DecafParser.Var_decl_csvContext ctx) {}

	public void exitVar_decl_csv(DecafParser.Var_decl_csvContext ctx) {}

	public void enterType(DecafParser.TypeContext ctx) {}

	public void exitType(DecafParser.TypeContext ctx) {}

	public void enterBin_op(DecafParser.Bin_opContext ctx) {}

	public void exitBin_op(DecafParser.Bin_opContext ctx) {} 

	public void enterArith_op(DecafParser.Arith_opContext ctx) {}

	public void exitArith_op(DecafParser.Arith_opContext ctx) {} 

	public void enterAssign_op(DecafParser.Assign_opContext ctx) {}

	public void exitAssign_op(DecafParser.Assign_opContext ctx) {}

	public void enterRel_op(DecafParser.Rel_opContext ctx) {}

	public void exitRel_op(DecafParser.Rel_opContext ctx) {}

	public void enterEq_op(DecafParser.Eq_opContext ctx) {}

	public void exitEq_op(DecafParser.Eq_opContext ctx) {}

	public void enterLiteral(DecafParser.LiteralContext ctx) {}

	public void exitLiteral(DecafParser.LiteralContext ctx) {}

	public void enterInt_literal(DecafParser.Int_literalContext ctx) {}

	public void exitInt_literal(DecafParser.Int_literalContext ctx) {}

	public void enterBool_literal(DecafParser.Bool_literalContext ctx) {}

	public void exitBool_literal(DecafParser.Bool_literalContext ctx) {}

    public void visitTerminal(TerminalNode terminal) {
        System.err.println("terminal " + terminal.getText());            
    }

    // don't need these here, so just make them empty implementations
    public void enterEveryRule(ParserRuleContext context) { }
    public void exitEveryRule(ParserRuleContext context) { }
    public void visitErrorNode(ErrorNode node) { }         
}