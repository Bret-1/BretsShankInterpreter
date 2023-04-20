package assignment1;

public class ParameterNode extends Node{
    VariableReferenceNode varParam = null;
    Node staticParam = null;
    public ParameterNode(Node param){
        staticParam = param;
    }
    public ParameterNode(VariableReferenceNode param){
        varParam = param;
    }
    public boolean getVariance(){
        return varParam != null;
    }
    public VariableReferenceNode getVarParam(){
        return varParam;
    }
    public Node getStaticParam(){
        return staticParam;
    }
    public boolean isStatic(){
        return staticParam != null;
    }
    @Override
    public String toString() {
        //print the correct node
        if(staticParam != null)
            return "(Static)" + staticParam.toString();
        else
            return "(Var)" + varParam.toString();
    }
}
