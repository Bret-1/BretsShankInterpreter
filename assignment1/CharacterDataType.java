package assignment1;

public class CharacterDataType extends InterpreterDataType{
    private char data;
    public CharacterDataType(CharacterDataType IDTIn, boolean changeable){
        FromString(IDTIn.toString());
        this.changeable = changeable;
    }
    public CharacterDataType(char in, boolean change){
        data = in;
        changeable = change;
    }
    public CharacterDataType( char in){
        //the size of in should've been verified in the parser when the variableReferenceNode holding the characterNode was createrd
        data = in;
    }

    public CharacterDataType() {

    }
    @Override
    public String toString() {
         return String.valueOf(data);
    }

    @Override
    public void FromString(String in) {
        data = in.charAt(0);
    }
}
