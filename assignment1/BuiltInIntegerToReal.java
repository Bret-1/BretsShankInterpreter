package assignment1;

import java.util.ArrayList;

public class BuiltInIntegerToReal extends FunctionNode{
    public BuiltInIntegerToReal(){
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
        if(!(in.get(0) instanceof IntegerDataType))
            throw new SyntaxErrorException("The first parameter in the function IntegerToReal must be of type integer.");
        if(!(in.get(1) instanceof RealDataType))
            throw new SyntaxErrorException("The second parameter in the function IntegerToReal must be of type real.");
        in.get(1).FromString(in.get(0).toString());
    }
}
