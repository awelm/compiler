import java.io.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.gui.Trees;
import java6035.tools.CLI.*;

class Main {
    public static void main(String[] args) {
        try {
        	CLI.parse (args, new String[0]);
        	
        	InputStream inputStream = args.length == 0 ?
                    System.in : new java.io.FileInputStream(CLI.infile);

        	if (CLI.target == CLI.SCAN)
        	{
        		DecafLexer lexer = new DecafLexer(CharStreams.fromStream(inputStream));
        		Token token;
        		boolean done = false;
        		while (!done)
        		{
        			try
        			{
		        		for (token=lexer.nextToken(); token.getType()!=DecafParser.EOF; token=lexer.nextToken())
		        		{
		        			String type = "";
		        			String text = token.getText();
		
		        			switch (token.getType())
		        			{
		        			case DecafParser.ID:
		        				type = " ID";
		        				break;


							case DecafParser.CHAR:
		        				type = " CHAR";
		        				break;
							case DecafParser.STRING:
		        				type = " STRING";
		        				break;


							case DecafParser.SEMI:
		        				type = " SEMI";
		        				break;
							case DecafParser.COMMA:
		        				type = " COMMA";
		        				break;


							case DecafParser.ADD:
		        				type = " ADD";
		        				break;
							case DecafParser.MULT:
		        				type = " MULT";
		        				break;
							case DecafParser.DIV:
		        				type = " DIV";
		        				break;
							case DecafParser.MOD:
		        				type = " MOD";
		        				break;
							case DecafParser.SUB:
		        				type = " SUB";
		        				break;
							case DecafParser.NEGATE:
		        				type = " NEGATE";
		        				break;
							case DecafParser.EQ:
		        				type = " EQ";
		        				break;
							case DecafParser.LESS:
		        				type = " LESS";
		        				break;
							case DecafParser.GREATER:
		        				type = " GREATER";
		        				break;
							case DecafParser.COND_OP:
		        				type = " COND_OP";
		        				break;


							case DecafParser.DECIMAL_LITERAL:
		        				type = " DECIMAL_LITERAL";
		        				break;
							case DecafParser.HEX_LITERAL:
		        				type = " HEX_LITERAL";
		        				break;


							case DecafParser.LCURLY:
		        				type = " LCURLY";
		        				break;
							case DecafParser.RCURLY:
		        				type = " RCURLY";
		        				break;
							case DecafParser.LBRAC:
		        				type = " LBRAC";
		        				break;
							case DecafParser.RBRAC:
		        				type = " RBRAC";
		        				break;
							case DecafParser.LPAREN:
		        				type = " LPAREN";
		        				break;
							case DecafParser.RPAREN:
		        				type = " RPAREN";
		        				break;
		        			}
		        			System.out.println (token.getLine() + type + " " + text);
		        		}
		        		done = true;
        			} catch(Exception e) {
        	        	// print the error:
        	            System.out.println(CLI.infile+" "+e);
        	            lexer.skip();
        	        }
        		}
        	}
        	else if (CLI.target == CLI.PARSE || CLI.target == CLI.DEFAULT)
        	{
        		DecafLexer lexer = new DecafLexer(CharStreams.fromStream(inputStream));
				CommonTokenStream tokens = new CommonTokenStream(lexer);
        		DecafParser parser = new DecafParser (tokens);
                ParseTree tree = parser.program();
				// Uncomment to display parse tree
				//Trees.inspect(tree, parser);
        	}
			else if(CLI.target == CLI.INTER) {
				DecafLexer lexer = new DecafLexer(CharStreams.fromStream(inputStream));
				CommonTokenStream tokens = new CommonTokenStream(lexer);
        		DecafParser parser = new DecafParser (tokens);
                ParseTree tree = parser.program();
				ParseTreeWalker walker = new ParseTreeWalker();
				MakeProgram listener = new MakeProgram();
				walker.walk(listener, tree);
				Ir ast = listener.getAst();
				System.out.println("AST: ");
				ast.prettyPrint();
			}
        	
        } catch(Exception e) {
        	// print the error:
            System.out.println(CLI.infile+" "+e);
        }
    }
}

