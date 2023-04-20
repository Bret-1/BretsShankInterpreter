package assignment1;

import java.util.ArrayList;

public class VariableNode extends Node{
    private int range = 1;
    private double lowerBound;
    private double upperBound;
    private ArrayList<Node> array = new ArrayList<>();
    public VariableNode(Node in, int maxRange){
        range = maxRange;
        array.add(in);
    }
    public VariableNode(Node in, double lower, double upper){
        array.add(in);
        lowerBound = lower;
        upperBound = upper;
    }
    public VariableNode(Node in){
        array.add(in);
    }
    public String getName(){
        return array.get(0).getName();
    }
    public Node getNode(){
        return array.get(0);
    }

    @Override
    public String toString() {
        StringBuilder tempString = new StringBuilder();
        for(Node n : array){
            tempString.append(n.toString());
        }
        if(range != 1){
            return array.toString() + " with array range of " + range;
        }
        else if(!Double.isNaN(lowerBound) && !Double.isNaN(upperBound) && upperBound != 0){
            return array + " with value range from " + lowerBound + " to " + upperBound;
        }
        else
            return array.toString();
    }
}
