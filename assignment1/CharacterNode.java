package assignment1;

public class CharacterNode extends Node{
    private char value;
    private String name;
    //multiple constructors for sending in different cases, such as when the value is declared in the same line as the variable
    //which should only happen for constants
    public CharacterNode(String in, boolean state, String name) throws SyntaxErrorException {
        if(in.length() > 1)
            throw new SyntaxErrorException("Char cannot be assigned a more than 1 character.");
        value = in.charAt(0);
        changeable = state;
        this.name = name;
    }
    public CharacterNode(char in, boolean state, String name){
        value = in;
        changeable = state;
        this.name = name;
    }
    public CharacterNode(boolean state, String name){
        changeable = state;
        this.name = name;
        value = ' ';
    }
    public CharacterNode(String in) throws SyntaxErrorException {
        if(in.length() > 1)
            throw new SyntaxErrorException("Char cannot be assigned a more than 1 character.");
        value = in.charAt(0);
    }
    public CharacterNode(){ }

    public char getValue() {
        return value;
    }

    public void setValue(char value) throws SyntaxErrorException {
        this.value = value;
    }
    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        //ternary statement outputs the mutability of this variable
        if(name != null)
            return (changeable ? "Mutable" : "NonMutable") + "Character, " + name + ": " + String.valueOf(value) + ")";
        else if(value == ' ')
            return null;
        else
            return String.valueOf(value);
    }
}
