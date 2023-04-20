package assignment1;

import java.util.ArrayList;
import java.util.Random;

public class BuiltInGetRandom extends FunctionNode{
    public BuiltInGetRandom(){
        builtIn = true;
        name = "GetRandom";
    }
    public void execute(ArrayList<InterpreterDataType> in) throws SyntaxErrorException {
        //checking for correct var or static parameter
        if(!in.get(0).changeable)
            throw new SyntaxErrorException("The first parameter of GetRandom must be var.");
        //checking for correct data type
        if(!(in.get(0) instanceof IntegerDataType))
            throw new SyntaxErrorException("The first parameter in the function GetRandom must be of type integer.");
        Random rand = new Random();
        in.get(0).FromString(String.valueOf(rand.nextInt()));
    }
}
