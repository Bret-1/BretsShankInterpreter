package assignment1;

import java.util.ArrayList;

public class AssignmentNode extends StatementNode{
    private VariableReferenceNode varRefNode;
    private Node target;
    public AssignmentNode(VariableReferenceNode varRefNodeIn, Node targetIn){
        varRefNode = varRefNodeIn;
        target = targetIn;
    }
    public AssignmentNode(VariableReferenceNode varRefNodeIn){
        varRefNode = varRefNodeIn;
    }
    public Node getTarget() {
        return target;
    }
    public void setTarget(Node in){
        target = in;
    }
    public VariableReferenceNode getVarRefNode() {
        return varRefNode;
    }

    @Override
    public String toString() {
        //creating a temporary parser to call TreeToString
        Parser p = new Parser(new ArrayList<>());
        if(target != null)
            return varRefNode.toString() + " assigned " + p.TreeToString(target);
        else
            return varRefNode.toString();
    }
}
