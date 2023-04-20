package assignment1;

import java.util.ArrayList;

public class VariableReferenceNode extends Node{
    private String name;
    public Node arrayExpression;
    public VariableReferenceNode(String name, Node arrayIndex){
        this.name = name;
        //expression is called for the range of any array
        arrayExpression = arrayIndex;
    }
    public VariableReferenceNode(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public Node getIndex(){
        return arrayExpression;
    }
    @Override
    public String toString() {
        if(arrayExpression == null)
            return name;
        Parser temp = new Parser(new ArrayList<>());
        return name + "[ index of " + temp.TreeToString(arrayExpression) + "]";
    }
}
