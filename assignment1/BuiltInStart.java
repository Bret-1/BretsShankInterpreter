package assignment1;
public class BuiltInStart extends FunctionNode{
    public BuiltInStart(){
        builtIn = true;
        name = "Start";
    }
    public void execute(ArrayDataType in) throws SyntaxErrorException {;
        //checking for correct var or static data types
        if (!parameters.get(0).changeable)
            throw new SyntaxErrorException("The second parameter of IntegerToReal must be var.");
        in.FromString(in.getFirst().toString());
    }
}
