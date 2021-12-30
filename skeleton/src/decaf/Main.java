package decaf;

import java.io.*;
import antlr.Token;
import java6035.tools.CLI.*;

class Main {
    public static void main(String[] args) {
        try {
        	CLI.parse (args, new String[0]);
        	
        	InputStream inputStream = args.length == 0 ?
                    System.in : new java.io.FileInputStream(CLI.infile);

        	if (CLI.target == CLI.SCAN)
        	{
        		DecafScanner lexer = new DecafScanner(new DataInputStream(inputStream));
        		Token token;
        		boolean done = false;
        		while (!done)
        		{
        			try
        			{
		        		for (token=lexer.nextToken(); token.getType()!=DecafParserTokenTypes.EOF; token=lexer.nextToken())
		        		{
		        			String type = "";
		        			String text = token.getText();
		
		        			switch (token.getType())
		        			{
		        			case DecafScannerTokenTypes.ID:
		        				type = " ID";
		        				break;


							case DecafScannerTokenTypes.CHAR:
		        				type = " CHAR";
		        				break;
							case DecafScannerTokenTypes.STRING:
		        				type = " STRING";
		        				break;


							case DecafScannerTokenTypes.SEMI:
		        				type = " SEMI";
		        				break;
							case DecafScannerTokenTypes.COMMA:
		        				type = " COMMA";
		        				break;


							case DecafScannerTokenTypes.ADD:
		        				type = " ADD";
		        				break;
							case DecafScannerTokenTypes.MULT:
		        				type = " MULT";
		        				break;
							case DecafScannerTokenTypes.DIV:
		        				type = " DIV";
		        				break;
							case DecafScannerTokenTypes.MOD:
		        				type = " MOD";
		        				break;
							case DecafScannerTokenTypes.SUB:
		        				type = " SUB";
		        				break;
							case DecafScannerTokenTypes.NEGATE:
		        				type = " NEGATE";
		        				break;
							case DecafScannerTokenTypes.EQ:
		        				type = " EQ";
		        				break;
							case DecafScannerTokenTypes.LESS:
		        				type = " LESS";
		        				break;
							case DecafScannerTokenTypes.GREATER:
		        				type = " GREATER";
		        				break;
							case DecafScannerTokenTypes.COND_OP:
		        				type = " COND_OP";
		        				break;


							case DecafScannerTokenTypes.DECIMAL_LITERAL:
		        				type = " DECIMAL_LITERAL";
		        				break;
							case DecafScannerTokenTypes.HEX_LITERAL:
		        				type = " HEX_LITERAL";
		        				break;


							case DecafScannerTokenTypes.LCURLY:
		        				type = " LCURLY";
		        				break;
							case DecafScannerTokenTypes.RCURLY:
		        				type = " RCURLY";
		        				break;
							case DecafScannerTokenTypes.LBRAC:
		        				type = " LBRAC";
		        				break;
							case DecafScannerTokenTypes.RBRAC:
		        				type = " RBRAC";
		        				break;
							case DecafScannerTokenTypes.LPAREN:
		        				type = " LPAREN";
		        				break;
							case DecafScannerTokenTypes.RPAREN:
		        				type = " RPAREN";
		        				break;
		        			}
		        			System.out.println (token.getLine() + type + " " + text);
		        		}
		        		done = true;
        			} catch(Exception e) {
        	        	// print the error:
        	            System.out.println(CLI.infile+" "+e);
        	            lexer.consume ();
        	        }
        		}
        	}
        	else if (CLI.target == CLI.PARSE)
        	{
        		DecafScanner lexer = new DecafScanner(new DataInputStream(inputStream));
        		DecafParser parser = new DecafParser (lexer);
                parser.program();
        	}
			else if (CLI.target == CLI.INTER) {
				DecafScanner lexer = new DecafScanner(new DataInputStream(inputStream));
        		DecafParser parser = new DecafParser (lexer);
                Ir ast = parser.program();
				ast.prettyPrint();
			}
        	
        } catch(Exception e) {
        	// print the error:
            System.out.println(CLI.infile+" "+e);
        }
    }
}

