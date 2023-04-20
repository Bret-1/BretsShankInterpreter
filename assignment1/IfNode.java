package assignment1;

import java.util.ArrayList;

public class IfNode extends StatementNode{
    private BooleanCompareNode param;
    private ArrayList<StatementNode> statementList = new ArrayList<>();
    private IfNode nextIf = null;
    private ArrayList<StatementNode> elseStatements = new ArrayList<>();
    public IfNode(BooleanCompareNode in){
        param = in;
    }
    public IfNode(){ }

    public void setStatementList(ArrayList<StatementNode> statementList) {
        this.statementList = statementList;
    }
    public void setNextIf(IfNode in){
        nextIf = in;
    }

    public ArrayList<StatementNode> getElseStatements() {
        return elseStatements;
    }

    public ArrayList<StatementNode> getStatementList() {
        return statementList;
    }

    public BooleanCompareNode getParam() {
        return param;
    }

    public IfNode getNextIf() {
        return nextIf;
    }

    public boolean hasNext(){
        return !(nextIf == null);
    }

    public void setElseStatements(ArrayList<StatementNode> elseStatements) {
        this.elseStatements = elseStatements;
    }

    @Override
    public String toString() {
        if(nextIf != null)
            return "\nif statements: " + param.toString() + " then " + statementList.toString() + " else" + nextIf.toString();
        else if(!elseStatements.isEmpty()){
            return "\nif statement: " + param.toString() + " then " + statementList.toString() + "\nelse" + elseStatements.toString() + '\n';
        } else {
            return "\nif statement: " + param.toString() + " then " + statementList.toString() + '\n';
        }
    }
}
