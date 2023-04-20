package assignment1;

import java.util.ArrayList;

public class BuiltInSubstring extends FunctionNode{
    public BuiltInSubstring(){
        builtIn = true;
        name = "Substring";
    }
    public void execute(ArrayList<InterpreterDataType> in) throws SyntaxErrorException {
        //checking for correct var and static parameters
        if(parameters.get(0).changeable)
            throw new SyntaxErrorException("The first parameter in the function Substring must be static.");
        if(parameters.get(1).changeable)
            throw new SyntaxErrorException("The second parameter in the function Substring must be static.");
        if(parameters.get(2).changeable)
            throw new SyntaxErrorException("The third parameter in the function Substring must be static.");
        if(!parameters.get(3).changeable)
            throw new SyntaxErrorException("The fourth parameter in the function Substring must be var.");
        //checking for proper data types
        if(!(in.get(0) instanceof StringDataType))
            throw new SyntaxErrorException("The first parameter in the function Substring must be of type String.");
        if(!(in.get(1) instanceof IntegerDataType))
            throw new SyntaxErrorException("The second parameter in the function Substring must be of type integer.");
        if(!(in.get(2) instanceof IntegerDataType))
            throw new SyntaxErrorException("The third parameter in the function Substring must be of type integer.");
        if(!(in.get(3) instanceof StringDataType))
            throw new SyntaxErrorException("The fourth parameter in the function Substring must be of type String.");
        in.get(3).FromString(in.get(0).toString().substring(Integer.parseInt(in.get(1).toString()), Integer.parseInt(in.get(2).toString())));
    }
}
