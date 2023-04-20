package assignment1;

import static java.lang.System.exit;

public class BooleanNode extends Node{
    private boolean value;
    private String name;
    //multiple constructors for sending in different cases, such as when the value is declared in the same line as the variable
    //which should only happen for constants
    public BooleanNode(String in, boolean state, String name){
        value = Boolean.parseBoolean(in);
        changeable = state;
        this.name = in;
    }
    public BooleanNode(boolean state, String name){
        changeable = state;
        this.name = name;
    }
    public BooleanNode(String in){
        value = Boolean.parseBoolean(in);
    }

    public BooleanNode(){ }

    public boolean getValue() {
        return value;
    }
    public String getName(){
        return name;
    }

    public void setValue(boolean value) throws SyntaxErrorException {
        this.value = value;
    }

    @Override
    public String toString() {
        //ternary statement outputs the mutability of this variable
        if(name != null)
            return (changeable ? "Mutable" : "NonMutable") + "Boolean, " + name + ": " + value + ")";
        else
            return " " + value;
    }
}