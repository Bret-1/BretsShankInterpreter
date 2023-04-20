package assignment1;

import java.util.ArrayList;

public class FunctionCallNode extends StatementNode{
    private String name;
    //stores the parameters of a function
    private ArrayList<ParameterNode> paramList = new ArrayList<>();
    public FunctionCallNode(){ }

    public void setParamList(ArrayList<ParameterNode> paramList) {
        this.paramList = paramList;
    }

    public ArrayList<ParameterNode> getParamList() {
        return paramList;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return "Functions: " + name + " " + paramList.toString() + '\n';
    }
}
