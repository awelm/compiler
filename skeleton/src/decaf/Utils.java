import org.antlr.v4.runtime.tree.*;
import java.util.*;

import javax.lang.model.util.ElementScanner14;

public class Utils {
    public static IrType createIrType(DecafParser.TypeContext ctx, DecafParser.Int_literalContext intLiteral) {
        if(ctx != null) {
            return new IrType(ctx, intLiteral);
        } else
            return null;
    }

    public static Integer getValueFromIntLiteral(DecafParser.Int_literalContext intCtx) {
        if(intCtx != null) {
            if(intCtx.DECIMAL_LITERAL() != null) {
                // integer is decimal
                return Integer.parseInt(intCtx.DECIMAL_LITERAL().getText());
            } else {
                // otherwise its hex
                String hexWithoutPrefix = intCtx.HEX_LITERAL().getText().substring(2);
                return Integer.parseInt(hexWithoutPrefix, 16);
            }
        } else
            return null;
    }

    public static Boolean getValueFromBoolLiteral(DecafParser.Bool_literalContext boolCtx) {
        if(boolCtx != null) {
            return boolCtx.TRUE() != null; 
        } else
            return null;
    }

    public static ArrayList<? extends Ir> popListFromStackAndReverse(Stack<Ir> stack, int nodeCount) {
        ArrayList<Ir> nodes = new ArrayList(); 
        for(int x=0; x<nodeCount; x++)
            nodes.add(stack.pop());
        Collections.reverse(nodes);
        return nodes;
    }
}
