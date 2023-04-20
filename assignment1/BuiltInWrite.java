package assignment1;

import java.util.ArrayList;

public class BuiltInWrite extends FunctionNode{
    public BuiltInWrite(){
        builtIn = true;
        vardic = true;
        name = "Write";
    }
    public void execute(ArrayList<InterpreterDataType> in) throws SyntaxErrorException {
        int paramCount = 0;
        for(InterpreterDataType i : in){
            //check for all staic parameters
            if(in.get(paramCount).changeable)
                throw new SyntaxErrorException("Write function must be passed all non var parameters.");
            System.out.print(i.toString() + " ");
            paramCount++;
        }
        System.out.print("\n");
    }
}
