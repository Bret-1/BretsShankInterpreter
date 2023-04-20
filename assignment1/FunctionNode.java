package assignment1;

import java.util.ArrayList;

public class FunctionNode extends Node{
    public ArrayList<VariableNode> getParameters() {
        return parameters;
    }

    protected ArrayList<VariableNode> parameters;
    protected ArrayList<VariableNode> constOrVariables = new ArrayList<>();
    //statement list will be added to later when we can process statements
    protected ArrayList<StatementNode> statementList = new ArrayList<StatementNode>();
    protected String name;
    protected boolean vardic = false;
    protected boolean builtIn = false;
    public FunctionNode(){ }
    //multiple functions for added one or a list of parameters
    public FunctionNode(String name){
        this.name = name;
    }
    public void addParameters(ArrayList<VariableNode> in){
        parameters = in;
    }
    public void addParameter(VariableNode in){
        parameters.add(in);
    }
    public void addStatements(ArrayList<StatementNode> in){ statementList.addAll(in);}
    public void addBodyConstOrVariable(ArrayList<VariableNode> in){
        constOrVariables = in;
    }
    public ArrayList<VariableNode> getConstOrVariables(){
        return constOrVariables;
    }
    public ArrayList<StatementNode> getStatementList(){ return statementList; }
    public String getName(){
        return name;
    }
    public boolean isVardic(){
        return vardic;
    }
    public boolean isBuiltIn(){return builtIn;}

    @Override
    public String toString() {
        return "Parameters: " + parameters.toString() + "\nConstants & Variables: " +
                constOrVariables.toString() + "\nStatements: " + statementList.toString();
    }
}
