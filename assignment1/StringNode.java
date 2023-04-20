package assignment1;

import java.util.Objects;

public class StringNode extends Node{
    private String value;
    private String name;
    //multiple constructors for sending in different cases, such as when the value is declared in the same line as the variable
    //which should only happen for constants
    public StringNode(String in, boolean state, String name){
        value = in;
        changeable = state;
        this.name = name;
    }
    public StringNode(boolean state, String name){
        changeable = state;
        this.name = name;
        value = " ";
    }
    public StringNode(String in){
        value = in;
    }
    public StringNode(){ }


    public String getValue() {
        return value;
    }

    public void setValue(String value) throws SyntaxErrorException {
        this.value = value;
    }
    public String getName(){
        return name;
    }
    @Override
    public String toString() {
        //ternary statement outputs the mutability of this variable
        if(name != null)
            return (changeable ? "Mutable" : "NonMutable") + "String, " + name + ": " + value + ")";
        else if(Objects.equals(value, " "))
            return null;
        else
            return value;
    }
}
