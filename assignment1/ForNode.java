package assignment1;

import java.util.ArrayList;

public class ForNode extends StatementNode{
    private Node base;
    private Node range;
    private ArrayList<StatementNode> statementList = new ArrayList<>();

    public void setStatementList(ArrayList<StatementNode> statementList) {
        this.statementList = statementList;
    }

    public ForNode(Node baseIn, Node rangeIn){
        base = baseIn;
        range = rangeIn;
    }
    public ForNode(){ }
    public void setBase(Node in){
        base = in;
    }
    public void setRange(Node in){
        range = in;
    }

    public Node getBase() {
        return base;
    }

    public Node getRange() {
        return range;
    }

    public ArrayList<StatementNode> getStatementList() {
        return statementList;
    }

    @Override
    public String toString() {
        return "For: range from " + base.toString() + " to " + range.toString()  + statementList.toString() + " end for";
    }
}
