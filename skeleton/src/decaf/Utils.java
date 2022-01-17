import org.antlr.v4.runtime.tree.*;
import java.util.*;

public class Utils {
    public static IrType createIrType(DecafParser.TypeContext ctx, DecafParser.Int_literalContext intLiteral) {
        if(ctx != null) {
            return new IrType(ctx, intLiteral);
        } else
            return null;
    }

    public static Integer getValueFromIntLiteral(DecafParser.Int_literalContext intLiteral) {
        if(intLiteral != null) {
            if(intLiteral.DECIMAL_LITERAL() != null) {
                // integer is decimal
                return Integer.parseInt(intLiteral.DECIMAL_LITERAL().getText());
            } else {
                // otherwise its hex
                String hexWithoutPrefix = intLiteral.HEX_LITERAL().getText().substring(2);
                return Integer.parseInt(hexWithoutPrefix, 16);
            }
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
