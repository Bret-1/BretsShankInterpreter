package assignment1;

public class RealNode extends Node{
    private double storedNumber;
    private String name;
    //multiple constructors for sending in different cases, such as when the value is declared in the same line as the variable
    //which should only happen for constants
    public RealNode(String in, boolean state, String name){
        storedNumber = Double.parseDouble(in);
        changeable = state;
        this.name = name;
    }
    public RealNode(boolean state, String name){
        changeable = state;
        this.name = name;
    }
    public RealNode(String in){
        storedNumber = Double.parseDouble(in);
    }
    public  RealNode(){ }
    public double getStoredNumber(){
        return storedNumber;
    }
    public void setStoredNumber(double in) throws SyntaxErrorException {
        storedNumber = in;
    }
    public String getName(){
        return name;
    }
    @Override
    public String toString() {
        //ternary statement outputs the mutability of this variable
        if(name != null)
            return (changeable ? "Mutable" : "NonMutable") + "Real, " + name + ": " + String.valueOf(storedNumber) + ")";
        else
            return String.valueOf(storedNumber);
    }
}
