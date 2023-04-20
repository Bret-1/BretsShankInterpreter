package assignment1;

import java.util.HashMap;

public class IntegerDataType extends InterpreterDataType{
    private int data;
    public IntegerDataType(int in, boolean change){
        data = in;
        changeable = change;
    }
    public IntegerDataType(int in){
        data = in;
    }
    public IntegerDataType(){ }

    public IntegerDataType(IntegerDataType IDTIn, boolean changeable) {
        FromString(IDTIn.toString());
        this.changeable = changeable;
    }

    @Override
    public String toString() { return String.valueOf(data);
    }

    @Override
    public void FromString(String in) {
        data = Integer.parseInt(in);
    }
}
