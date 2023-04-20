package assignment1;

public class StringDataType extends InterpreterDataType{
    private String data;
    public StringDataType(StringDataType IDTIn, boolean changeable){
        FromString(IDTIn.toString());
        this.changeable = changeable;
    }
    public StringDataType(String in, boolean change){
        data = in;
        changeable = change;
    }
    public StringDataType(String in){
        data = in;
    }

    public StringDataType() {

    }
    @Override
    public String toString() {
        return data;
    }

    @Override
    public void FromString(String in) {
        data = in;
    }
}
