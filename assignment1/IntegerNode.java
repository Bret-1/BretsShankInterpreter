package assignment1;

public class IntegerNode extends Node{
    private int storedNumber;
    private String name;
    //multiple constructors for sending in different cases, such as when the value is declared in the same line as the variable
    //which should only happen for constants
    public IntegerNode(String in, boolean state, String name){
        storedNumber = Integer.parseInt(in);
        changeable = state;
        this.name = name;
    }
    public IntegerNode(boolean state, String name){
        changeable = state;
        this.name = name;
    }
    public IntegerNode(String in){
        storedNumber = Integer.parseInt(in);
    }
    public IntegerNode(){ }
    public int getStoredNumber(){
        return storedNumber;
    }
    public void setStoredNumber(int in) throws SyntaxErrorException {
        storedNumber = in;
    }
    public String getName(){
        return name;
    }
    @Override
    public String toString() {
        //ternary statement outputs the mutability of this variable
        if(name != null)
            return (changeable ? "Mutable" : "NonMutable") + " Integer, " +  name + ": " + String.valueOf(storedNumber) + ")";
        else
            return String.valueOf(storedNumber);
    }
}
