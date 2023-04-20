package assignment1;

import java.util.*;

public class SemanticAnalysis {
    public SemanticAnalysis(ProgramNode p) throws SyntaxErrorException {
        //checks all assignements in the program
        checkAssignments(p);
    }
    private void checkAssignments(ProgramNode p) throws SyntaxErrorException {
        for(FunctionNode funcIn : p.getFunctions().values()){
            //test if the function is a builtIn, which is a special cas
            if(funcIn.builtIn)
                continue;
            //get all names from the parameters, constants and parameters
            //this hashmap stores a name, and a string describing the type of the Node
            HashMap<String, String> variableNames = new HashMap<>();
            for(int i = 0; i < funcIn.getParameters().size(); i++){
                if(funcIn.getParameters().get(i).getNode() instanceof IntegerNode)
                    variableNames.put(funcIn.getParameters().get(i).getName(), "int");
                else if(funcIn.getParameters().get(i).getNode() instanceof RealNode)
                    variableNames.put(funcIn.getParameters().get(i).getName(), "real");
                else if(funcIn.getParameters().get(i).getNode() instanceof StringNode)
                    variableNames.put(funcIn.getParameters().get(i).getName(), "string");
                else if(funcIn.getParameters().get(i).getNode() instanceof CharacterNode)
                    variableNames.put(funcIn.getParameters().get(i).getName(), "char");
                else if(funcIn.getParameters().get(i).getNode() instanceof BooleanNode)
                    variableNames.put(funcIn.getParameters().get(i).getName(), "bool");
            }
            for(int i = 0; i < funcIn.getConstOrVariables().size(); i++){
                if(funcIn.getConstOrVariables().get(i).getNode() instanceof IntegerNode)
                    variableNames.put(funcIn.getConstOrVariables().get(i).getName(), "int");
                else if(funcIn.getConstOrVariables().get(i).getNode() instanceof RealNode)
                    variableNames.put(funcIn.getConstOrVariables().get(i).getName(), "real");
                else if(funcIn.getConstOrVariables().get(i).getNode() instanceof StringNode)
                    variableNames.put(funcIn.getConstOrVariables().get(i).getName(), "string");
                else if(funcIn.getConstOrVariables().get(i).getNode() instanceof CharacterNode)
                    variableNames.put(funcIn.getConstOrVariables().get(i).getName(), "char");
                else if(funcIn.getConstOrVariables().get(i).getNode() instanceof BooleanNode)
                    variableNames.put(funcIn.getConstOrVariables().get(i).getName(), "bool");
            }
            //iterate over all statements and send found assignments to checkSingleAssignment
            ArrayList<StatementNode> potentialAssignments = funcIn.getStatementList();
            for(StatementNode s : potentialAssignments){
                if(s instanceof AssignmentNode){
                    checkSingleAssignment((AssignmentNode) s, variableNames);
                }
            }
        }
    }
    private void checkSingleAssignment(AssignmentNode assignment, HashMap<String, String> nameToType) throws SyntaxErrorException{
        //extract the type
       String type = nameToType.get(assignment.getVarRefNode().getName());
       //call inspectExpression which will throw an Exception if a bad assignment is found. we catch it here and throw a more detailed error
       try {
           inspectExpression(type, assignment.getTarget(),nameToType);
       } catch (Exception ex){
           throw new SyntaxErrorException(treeToString(assignment.getTarget()) + "cannot be assigned to the variable " + assignment.getVarRefNode().getName() + " of type " + type);
       }
    }
    private void inspectExpression(String type, Node expression, HashMap<String, String> nameToType) throws Exception {
        //go left and right, then return as we only want to validate leaf nodes
        if(expression.hasLeft()) {
            inspectExpression(type, expression.getLeft(), nameToType);
            inspectExpression(type, expression.getRight(), nameToType);
            return;
        }
        switch(type){
            case "int":
                //checking both for integerNodes, and variableReferenceNodes that reference an integerNode
                if(!(expression instanceof IntegerNode || expression instanceof RealNode)) {
                    if (expression instanceof VariableReferenceNode)
                        if(!Objects.equals(nameToType.get(expression.getName()), "int") && !Objects.equals(nameToType.get(expression.getName()), "real"))
                            throw new Exception();
                        else return;
                        //throwing an exception back to checkSingleAssignment
                    throw new Exception();
                }
                break;
            case "real":
                if(!(expression instanceof RealNode || expression instanceof IntegerNode)) {
                    if (expression instanceof VariableReferenceNode)
                        if(!Objects.equals(nameToType.get(expression.getName()), "real") && !Objects.equals(nameToType.get(expression.getName()), "int"))
                            throw new Exception();
                        else return;
                    throw new Exception();
                }
                break;
            case "string":
                if(!(expression instanceof StringNode)) {
                    if (expression instanceof VariableReferenceNode)
                        if(!Objects.equals(nameToType.get(expression.getName()), "string"))
                            throw new Exception();
                        else return;
                    throw new Exception();
                }
                break;
            case "char":
                if(!(expression instanceof CharacterNode)) {
                    if (expression instanceof VariableReferenceNode)
                        if(!Objects.equals(nameToType.get(expression.getName()), "char"))
                            throw new Exception();
                        else return;
                    throw new Exception();
                }
                break;
            case "bool":
                if(!(expression instanceof BooleanNode)) {
                    if (expression instanceof VariableReferenceNode)
                        if(!Objects.equals(nameToType.get(expression.getName()), "bool"))
                            throw new Exception();
                        else return;
                    throw new Exception();
                }
                break;
        }
    }
    //formatted string containing an expression, used when throwing errors
    private String treeToString(Node n){
        StringBuilder s = new StringBuilder();
        //left
        if(n.hasLeft())
            s.append(treeToString(n.getLeft())).append(" ");
        //action
        if(n instanceof MathOpNode)
            s.append(((MathOpNode) n).getSymbol()).append(" ");
        else
            s.append(n.toString());
        //right
        if(n.hasRight())
            s.append(treeToString(n.getRight())).append(" ");
        return s.toString();
    }
}