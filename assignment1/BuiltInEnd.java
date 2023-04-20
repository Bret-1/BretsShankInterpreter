package assignment1;
public class BuiltInEnd extends FunctionNode{
    public BuiltInEnd(){
        builtIn = true;
        name = "End";
    }
    public void execute(ArrayDataType in) throws SyntaxErrorException {
        //checking for correct var or static parameters
        if (!in.changeable)
            throw new SyntaxErrorException("The first parameter of End must be var.");
        in.FromString(in.getLast().toString());
    }
}
