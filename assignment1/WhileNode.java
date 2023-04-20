package assignment1;

import java.util.ArrayList;

public class WhileNode extends StatementNode{
    private BooleanCompareNode param;

    public BooleanCompareNode getParam() {
        return param;
    }

    public ArrayList<StatementNode> getStatementList() {
        return statementList;
    }

    private ArrayList<StatementNode> statementList = new ArrayList<>();
    public WhileNode(BooleanCompareNode in){
        param = in;
    }
    public WhileNode() { }
    public void setStatementList(ArrayList<StatementNode> statementList) {
        this.statementList = statementList;
    }
    @Override
    public String toString() {
        return "While: " + param.toString() + statementList.toString() + " end while";
    }
}
