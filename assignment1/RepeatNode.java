package assignment1;

import java.util.ArrayList;

public class RepeatNode extends StatementNode{
    public BooleanCompareNode getParam() {
        return param;
    }

    private BooleanCompareNode param;

    public ArrayList<StatementNode> getStatementList() {
        return statementList;
    }

    private ArrayList<StatementNode> statementList = new ArrayList<>();
    public void setStatementList(ArrayList<StatementNode> statementList) {
        this.statementList = statementList;
    }
    public RepeatNode(BooleanCompareNode in){
        param = in;
    }
    public RepeatNode(){ }
    @Override
    public String toString() {
        return "Repeat: " + param.toString() + statementList.toString() + " end repeat";
    }
}
