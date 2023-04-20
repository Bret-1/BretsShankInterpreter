package assignment1;

import java.util.ArrayList;

public class ArrayDataType extends InterpreterDataType{
    private InterpreterDataType[] data;
    static int SIZE;
    public ArrayDataType(int size){
        data = new InterpreterDataType[SIZE];
    }
    public InterpreterDataType getFirst() throws SyntaxErrorException {
        return data[0];
    }
    public InterpreterDataType getLast() throws SyntaxErrorException {
        return data[data.length - 1];
    }
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < SIZE - 1; i++){
            s.append(data[i].toString());
            s.append(", ");
        }
        s.append(data[SIZE - 1]);
        return s.toString();
    }

    @Override
    public void FromString(String in) {
        String[] arrIn = in.split(", ");
        if(data instanceof IntegerDataType[]) {
            for (int i = 0; i < arrIn.length; i++) {
                data[i] = new IntegerDataType();
                data[i].FromString(arrIn[i]);
            }
        }
        else if(data instanceof RealDataType[]) {
            for (int i = 0; i < arrIn.length; i++) {
                data[i] = new RealDataType();
                data[i].FromString(arrIn[i]);
            }
        }
        else if(data instanceof BooleanDataType[]) {
            for (int i = 0; i < arrIn.length; i++) {
                data[i] = new BooleanDataType();
                data[i].FromString(arrIn[i]);
            }
        }
        else if(data instanceof CharacterDataType[]) {
            for (int i = 0; i < arrIn.length; i++) {
                data[i] = new CharacterDataType();
                data[i].FromString(arrIn[i]);
            }
        }else {
            for (int i = 0; i < arrIn.length; i++) {
                data[i] = new StringDataType();
                data[i].FromString(arrIn[i]);
            }
        }

    }
}
