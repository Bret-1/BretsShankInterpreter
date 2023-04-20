package assignment1;

import java.util.HashMap;

public class BooleanCompareNode extends Node{
    public enum boolSymbolType{ GREATERTHAN, LESSTHAN,GREATEROREQUAL, LESSOREQUAL, EQUAL, NOTEQUAL}
    private final HashMap<String, boolSymbolType> boolSymbolMap = new HashMap<>();
    private boolSymbolType boolSymbol;
    private String value;
    public BooleanCompareNode(String symbol){
        populateMap();
        value = symbol;
        boolSymbol = boolSymbolMap.get(symbol);
    }
    public String getSymbol(){
        return switch (boolSymbol) {
            case GREATERTHAN -> ">";
            case LESSTHAN -> "<";
            case LESSOREQUAL -> "<=";
            case EQUAL -> "=";
            case NOTEQUAL -> "<>";
            case GREATEROREQUAL -> ">=";
        };
    }
    private void populateMap(){
        //populating the map of comparison symbols
        boolSymbolMap.put(">", boolSymbolType.GREATERTHAN);
        boolSymbolMap.put("<", boolSymbolType.LESSTHAN);
        boolSymbolMap.put(">=", boolSymbolType.GREATEROREQUAL);
        boolSymbolMap.put("<=", boolSymbolType.LESSOREQUAL);
        boolSymbolMap.put("=", boolSymbolType.EQUAL);
        boolSymbolMap.put("<>", boolSymbolType.NOTEQUAL);
    }
    @Override
    public String toString() {
        return "BooleanCompareNode(" + value + ", " + left.toString() + ", " + right.toString() + ") ";
    }
}
