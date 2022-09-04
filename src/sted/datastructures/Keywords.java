package sted.datastructures;

import java.io.StringReader;
import sted.javacc.IASTExpression;
import sted.javacc.ParseException;
import sted.javacc.Parser;

public class Keywords {
    private String keywords;

    public Keywords(String keywords) {
        this.keywords = keywords;
    }

    public boolean match(String string) {
        if (keywords == null | keywords.isEmpty())
            return true;
        try {
            Parser parser = new Parser(new StringReader(keywords));
            IASTExpression ast = parser.Start();
            //System.out.println("Syntax Ok!");
            return ast.evaluate(string);
        } catch (ParseException x) {
            //System.out.println("Syntax Error.");
            //x.printStackTrace();
            return false;
        }
    }

    @Override
    public String toString() {
        return keywords;
    }
}
