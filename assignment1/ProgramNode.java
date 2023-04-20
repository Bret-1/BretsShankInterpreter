package assignment1;
import java.util.HashMap;
public class ProgramNode extends Node{
    private HashMap<String, FunctionNode> functions = new HashMap<>();
    public ProgramNode(){
        functions.put("Read", new BuiltInRead());
        functions.put("Write", new BuiltInWrite());
        functions.put("End", new BuiltInEnd());
        functions.put("GetRandom", new BuiltInGetRandom());
        functions.put("IntegerToReal", new BuiltInIntegerToReal());
        functions.put("Left", new BuiltInLeft());
        functions.put("RealToInteger", new BuiltInRealToInteger());
        functions.put("Right", new BuiltInRight());
        functions.put("SquareRoot", new BuiltInSquareRoot());
        functions.put("Start", new BuiltInStart());
        functions.put("Substring", new BuiltInSubstring());
    }
    public void addFunction(FunctionNode in){
        functions.put(in.getName(), in);
    }
    public HashMap<String, FunctionNode> getFunctions(){
        return functions;
    }
    //the following function converts only the requested function node to a string
    public String singleElementToString(String name){
        //couldn't figure out how to convert a single function to a nicely formatted output without using a stringBuilder,
        //so this block is just for formatting
        StringBuilder returnString = new StringBuilder();
        returnString.append("Function name: ").append(name).append("\n");
        returnString.append(functions.get(name).toString());
        return returnString.toString();
    }
    @Override
    public String toString() {
        return "Start of Program: \n" + formatFunctionsToPrint();
    }
    public String formatFunctionsToPrint(){
        //accumulate the name and function nodes implicitly casted toString
        StringBuilder returnString = new StringBuilder();
        for (String name : functions.keySet()) {
            returnString.append("Function Name: ");
            returnString.append(name).append("\n ");
            returnString.append(functions.get(name));
        }
        return returnString.toString();
    }
}
