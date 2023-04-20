package assignment1;

import java.util.ArrayList;

public class BuiltInLeft extends FunctionNode{
    public BuiltInLeft(){
        builtIn = true;
        name = "Left";
    }
    public void execute(ArrayList<InterpreterDataType> in) throws SyntaxErrorException {
        //checking for proper var and static parameters
        if(in.get(0).changeable)
            throw new SyntaxErrorException("The first parameter in the function Left must be static.");
        if(in.get(1).changeable)
            throw new SyntaxErrorException("The second parameter in the function Left must be static.");
        if(!in.get(2).changeable)
            throw new SyntaxErrorException("The third parameter in the function Left must be var.");
        //checking for correct Data Types
        if(!(in.get(0) instanceof StringDataType))
            throw new SyntaxErrorException("The first parameter in the function Left must be of type String.");
        if(!(in.get(1) instanceof IntegerDataType))
            throw new SyntaxErrorException("The second parameter in the function Left must be of type integer.");
        if(!(in.get(2) instanceof StringDataType))
            throw new SyntaxErrorException("The third parameter in the function Left must be of type String.");
        in.get(2).FromString(in.get(0).toString().substring(0, Integer.parseInt(in.get(1).toString())));
    }
}
