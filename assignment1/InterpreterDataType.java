package assignment1;

public abstract class InterpreterDataType {

    protected boolean changeable;
    public boolean isChangeable(){ return changeable; }
    public abstract String toString();
    public abstract void FromString(String in);

    public void setChangeable(boolean changeable) {
        this.changeable = changeable;
    }
}
