package assignment1;

public class MathOpNode extends Node{
    private String symbol;
    public MathOpNode(String in){
        symbol = in;
    }
    public MathOpNode(){ };
    public void setSymbol(String in){
        symbol = in;
    }
    public String getSymbol(){
        return symbol;
    }

    @Override
    public String toString() {
        return "MathOpNode(" + symbol + ",";
    }
}
