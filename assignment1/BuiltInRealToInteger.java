package assignment1;

import java.util.ArrayList;

public class BuiltInRealToInteger extends FunctionNode{
    public BuiltInRealToInteger(){
        builtIn = true;
        name = "IntegerToReal";
    }
    public void execute(ArrayList<InterpreterDataType> in) throws SyntaxErrorException {
        //checking for correct var or static parameters
        if (in.get(0).changeable)
            throw new SyntaxErrorException("The first parameter of IntegerToReal must be static.");
        if (!in.get(1).changeable)
            throw new SyntaxErrorException("The second parameter of IntegerToReal must be var.");
        //checking for correct data types
        if(!(in.get(0) instanceof RealDataType))
            throw new SyntaxErrorException("The first parameter in the function RealToInteger must be of type real.");
        if(!(in.get(1) instanceof IntegerDataType))
            throw new SyntaxErrorException("The second parameter in the function RealToInteger must be of type integer.");
        in.get(1).FromString(String.valueOf((int)Double.parseDouble(in.get(0).toString())));
    }
}
