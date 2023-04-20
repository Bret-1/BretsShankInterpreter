package assignment1;

public class BooleanDataType extends InterpreterDataType{
    private boolean data;
    public BooleanDataType(BooleanDataType IDTIn, boolean changeable){
        FromString(IDTIn.toString());
        this.changeable = changeable;
    }
    public BooleanDataType(boolean in, boolean change){
        data = in;
        changeable = change;
    }
    public BooleanDataType(boolean in){
        data = in;
    }

    public BooleanDataType() {

    }
    @Override
    public String toString() {
        return String.valueOf(data);
    }

    @Override
    public void FromString(String in) {
        data = Boolean.parseBoolean(in);
    }
}
