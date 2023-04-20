package assignment1;

import java.util.ArrayList;
import java.lang.Math;

public class BuiltInSquareRoot extends FunctionNode{
    public BuiltInSquareRoot(){
        builtIn = true;
        name = "SquareRoot";
    }
    public void execute(ArrayList<InterpreterDataType> in) throws SyntaxErrorException {
        //checking for proper var and static parameters
        if(in.get(0).changeable)
            throw new SyntaxErrorException("The first parameter in the function SquareRoot must be static.");
        if(!in.get(1).changeable)
            throw new SyntaxErrorException("The second parameter in the function SquareRoot must be var.");
        //checking for correct data types
        if(!(in.get(0) instanceof RealDataType))
            throw new SyntaxErrorException("The first parameter in the function SquareRoot must be of type real.");
        if(!(in.get(1) instanceof RealDataType))
            throw new SyntaxErrorException("The second parameter in the function SquareRoot must be of type real.");
        in.get(1).FromString(String.valueOf(Math.sqrt(Double.parseDouble(in.get(0).toString()))));
    }
}
