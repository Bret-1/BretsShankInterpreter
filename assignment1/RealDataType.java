package assignment1;
public class RealDataType extends InterpreterDataType{
    private double data;
    public RealDataType(double in, boolean change){
        data = in;
        changeable = change;
    }
    public RealDataType( double in){
        data = in;
        changeable = true;
    }
    public RealDataType(){ }

    public RealDataType(RealDataType IDTIn, boolean changeable) {
        FromString(IDTIn.toString());
        this.changeable = changeable;
    }

    @Override
    public String toString() {
        return String.valueOf(data);
    }

    @Override
    public void FromString(String in) {
        data = Double.parseDouble(in);
    }
}
