package assignment1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Interpreter {
    private ProgramNode program;

    public Interpreter(ProgramNode p) {
        program = p;
    }
    public void interpret() throws SyntaxErrorException {
        //HashMap<String, InterpreterDataType> tempMap = null;
        interpretFunction(program.getFunctions().get("start"), null);
    }

    private HashMap<String, InterpreterDataType> generateIDTs(FunctionNode funcIn){
        //loop through the variables that were declared at the start of the function
        ArrayList<VariableNode> constOrVar = funcIn.getConstOrVariables();
        HashMap<String, InterpreterDataType> idtMap = new HashMap<>();
        for(VariableNode v : funcIn.getParameters()){
            //if its a constant, it will go to the else branch
            if (v.getNode().isChangeable()) {
                if (v.getNode() instanceof IntegerNode)
                    idtMap.put(v.getNode().getName(), new IntegerDataType(((IntegerNode) v.getNode()).getStoredNumber(), true));
                else if (v.getNode() instanceof RealNode)
                    idtMap.put(v.getNode().getName(), new RealDataType(((RealNode) v.getNode()).getStoredNumber(), true));
                else if (v.getNode() instanceof StringNode)
                    idtMap.put(v.getNode().getName(), new StringDataType(((StringNode) v.getNode()).getValue(), true));
                else if (v.getNode() instanceof BooleanNode)
                    idtMap.put(v.getNode().getName(), new BooleanDataType(((BooleanNode) v.getNode()).getValue(), true));
                else
                    idtMap.put(v.getNode().getName(), new CharacterDataType(((CharacterNode) v.getNode()).getValue(), true));
            } else {
                //added a new constructor to the InterpreterDataType and its children as well as a boolean variable to tell if the
                //value is changeable or not, the rubric has this function of verifying mutability in the expression function
                //but i didn't understand how that would work so I put it here, and in interpretAssign()
                if (v.getNode() instanceof IntegerNode)
                    idtMap.put(v.getNode().getName(), new IntegerDataType(((IntegerNode) v.getNode()).getStoredNumber(), false));
                else if (v.getNode() instanceof RealNode)
                    idtMap.put(v.getNode().getName(), new RealDataType(((RealNode) v.getNode()).getStoredNumber(), false));
                else if (v.getNode() instanceof StringNode)
                    idtMap.put(v.getNode().getName(), new StringDataType(((StringNode) v.getNode()).getValue(), false));
                else if (v.getNode() instanceof BooleanNode)
                    idtMap.put(v.getNode().getName(), new BooleanDataType(((BooleanNode) v.getNode()).getValue(), false));
                else
                    idtMap.put(v.getNode().getName(), new CharacterDataType(((CharacterNode) v.getNode()).getValue(), false));
            }
        }
        for (VariableNode v : constOrVar) {
            //if its a constant, it will go to the else branch
            if (v.getNode().isChangeable()) {
                if (v.getNode() instanceof IntegerNode)
                    idtMap.put(v.getNode().getName(), new IntegerDataType(((IntegerNode) v.getNode()).getStoredNumber(), true));
                else if (v.getNode() instanceof RealNode)
                    idtMap.put(v.getNode().getName(), new RealDataType(((RealNode) v.getNode()).getStoredNumber(), true));
                else if (v.getNode() instanceof StringNode)
                    idtMap.put(v.getNode().getName(), new StringDataType(((StringNode) v.getNode()).getValue(), true));
                else if (v.getNode() instanceof BooleanNode)
                    idtMap.put(v.getNode().getName(), new BooleanDataType(((BooleanNode) v.getNode()).getValue(), true));
                else
                    idtMap.put(v.getNode().getName(), new CharacterDataType(((CharacterNode) v.getNode()).getValue(), true));
            } else {
                //added a new constructor to the InterpreterDataType and its children as well as a boolean variable to tell if the
                //value is changeable or not, the rubric has this function of verifying mutability in the expression function
                //but i didn't understand how that would work so I put it here, and in interpretAssign()
                if (v.getNode() instanceof IntegerNode)
                    idtMap.put(v.getNode().getName(), new IntegerDataType(((IntegerNode) v.getNode()).getStoredNumber(), false));
                else if (v.getNode() instanceof RealNode)
                    idtMap.put(v.getNode().getName(), new RealDataType(((RealNode) v.getNode()).getStoredNumber(), false));
                else if (v.getNode() instanceof StringNode)
                    idtMap.put(v.getNode().getName(), new StringDataType(((StringNode) v.getNode()).getValue(), false));
                else if (v.getNode() instanceof BooleanNode)
                    idtMap.put(v.getNode().getName(), new BooleanDataType(((BooleanNode) v.getNode()).getValue(), false));
                else
                    idtMap.put(v.getNode().getName(), new CharacterDataType(((CharacterNode) v.getNode()).getValue(), false));
            }
        }
        return idtMap;
    }

    private void interpretBlock(ArrayList<StatementNode> statements, HashMap<String, InterpreterDataType> nameToIDT) throws SyntaxErrorException {
        //loop through the statements and decide what the statement is
        for (StatementNode s : statements) {
            if (s instanceof AssignmentNode) {
                interpretAssignment((AssignmentNode) s, nameToIDT);
            } else if (s instanceof IfNode) {
                interpretIf((IfNode) s, nameToIDT);
            } else if (s instanceof WhileNode) {
                interpretWhile((WhileNode) s, nameToIDT);
            } else if (s instanceof RepeatNode) {
                interpretRepeat((RepeatNode) s, nameToIDT);
            } else if (s instanceof ForNode) {
                interpretFor((ForNode) s, nameToIDT);
            } else if (s instanceof FunctionCallNode){
                interpretFunctionCall((FunctionCallNode) s,  nameToIDT);
            }
        }
    }
    public void interpretAssignment(AssignmentNode assignIn, HashMap<String, InterpreterDataType> nameToIDT) throws SyntaxErrorException {
        String varToAssign = assignIn.getVarRefNode().getName();
        //make sure the variable was initialized in Shank
        if(nameToIDT.get(varToAssign) == null)
            throw new SyntaxErrorException("Variable " + varToAssign + " not previously declared.");
        //ensure that we can change the variable
        if(!nameToIDT.get(varToAssign).isChangeable())
            throw new SyntaxErrorException("The constant variable " + varToAssign + " was declared constant and cannot have its value changed.");
        //we are basically copying the old IDT value into this new one, but while copying the mutability of the IDT and the updated value
        //ints, reals and string call expression as they can be set to an operation
        //character and bool can only be assigned a direct value
        if(expression(assignIn.getTarget(), nameToIDT) instanceof IntegerDataType){
            nameToIDT.put(varToAssign, new IntegerDataType((IntegerDataType) expression(assignIn.getTarget(), nameToIDT), nameToIDT.get(varToAssign).isChangeable()));
        } else if(expression(assignIn.getTarget(), nameToIDT) instanceof RealDataType){
            nameToIDT.put(varToAssign, new RealDataType((RealDataType)expression(assignIn.getTarget(), nameToIDT), nameToIDT.get(varToAssign).isChangeable()));
        } else if (expression(assignIn.getTarget(), nameToIDT) instanceof StringDataType){
            nameToIDT.put(varToAssign, new StringDataType((StringDataType) expression(assignIn.getTarget(), nameToIDT), nameToIDT.get(varToAssign).isChangeable()));
        } else if (expression(assignIn.getTarget(), nameToIDT) instanceof CharacterDataType){
            nameToIDT.put(varToAssign, new CharacterDataType(((CharacterNode)assignIn.getTarget()).getValue(), nameToIDT.get(varToAssign).isChangeable()));
        } else
            nameToIDT.put(varToAssign, new BooleanDataType(((BooleanNode)assignIn.getTarget()).getValue(), nameToIDT.get(varToAssign).isChangeable()));
    }
    private void interpretFunction(FunctionNode funcIn, HashMap<String, InterpreterDataType> nameToIDT) throws SyntaxErrorException {
        //using nameToIDT, we can tell if we need to interpret the variables or not depending on the situation
        if(nameToIDT == null) {
            nameToIDT = new HashMap<>();
            nameToIDT.putAll(generateIDTs(funcIn));
        }
        interpretBlock(funcIn.getStatementList(), nameToIDT);
    }
    public void interpretFunctionCall(FunctionCallNode funcRef, HashMap<String, InterpreterDataType> nameToIDT) throws SyntaxErrorException {
        FunctionNode referencedFunction = program.getFunctions().get(funcRef.getName());
        //make sure the fuction has been previously defined, all BuiltIns were added to the ProgramNodes hashmap in its constructor
        if(referencedFunction == null)
            throw new SyntaxErrorException("The called function " + funcRef.getName() + " was not previously declared");
        if(referencedFunction.isBuiltIn()){
            if(referencedFunction instanceof BuiltInStart){
                //validate the amount of parameters
                if(funcRef.getParamList().size() != 1)
                    throw new SyntaxErrorException("Function Start can only accept 1 parameter.");
                //my BuiltInStart and BuiltInEnd take ArrayDataTypes, so get the original from nameToIDT and send it to the execute function
                ((BuiltInStart)referencedFunction).execute((ArrayDataType) nameToIDT.get(funcRef.getParamList().get(0).getVarParam().getName()));
            } else if (referencedFunction instanceof BuiltInEnd){
                //validate the amount of parameters
                if(funcRef.getParamList().size() != 1)
                    throw new SyntaxErrorException("Function End can only accept 1 parameter.");
                //my BuiltInStart and BuiltInEnd take ArrayDataTypes, so get the original from nameToIDT and send it to the execute function
                ((BuiltInEnd)referencedFunction).execute((ArrayDataType) nameToIDT.get(funcRef.getParamList().get(0).getVarParam().getName()));
            } else if (referencedFunction instanceof BuiltInSquareRoot){
                //validate the number of parameters
                if(funcRef.getParamList().size() != 2)
                    throw new SyntaxErrorException("Function SquareRoot can only accept 2 parameters.");
                ArrayList<InterpreterDataType> squareRootParams = new ArrayList<>();
                //add the parameters to the new ArrayList, with their evaluated expressions and the appropriate mutability
                squareRootParams.add(expression(funcRef.getParamList().get(0).getStaticParam(), nameToIDT));
                squareRootParams.get(0).setChangeable(false);
                squareRootParams.add(expression(funcRef.getParamList().get(1).getVarParam(), nameToIDT));
                squareRootParams.get(1).setChangeable(true);
                //call execute which changes the values in the arrayList
                ((BuiltInSquareRoot)referencedFunction).execute(squareRootParams);
                //update the var values from the arrayList
                nameToIDT.put(funcRef.getParamList().get(1).getVarParam().getName(), squareRootParams.get(1));
            } else if (referencedFunction instanceof BuiltInSubstring){
                if(funcRef.getParamList().size() != 2)
                    throw new SyntaxErrorException("Function SquareRoot can only accept 2 parameters.");
                ArrayList<InterpreterDataType> substringParams = new ArrayList<>();
                //add the parameters to the new ArrayList, with their evaluated expressions and the appropriate mutability
                substringParams.add(expression(funcRef.getParamList().get(0).getStaticParam(), nameToIDT));
                substringParams.get(0).setChangeable(false);
                substringParams.add(expression(funcRef.getParamList().get(1).getStaticParam(), nameToIDT));
                substringParams.get(1).setChangeable(false);
                substringParams.add(expression(funcRef.getParamList().get(2).getStaticParam(), nameToIDT));
                substringParams.get(2).setChangeable(false);
                substringParams.add(expression(funcRef.getParamList().get(3).getVarParam(), nameToIDT));
                substringParams.get(3).setChangeable(true);
                //call execute which changes the values in the arrayList
                ((BuiltInSubstring)referencedFunction).execute(substringParams);
                nameToIDT.put(funcRef.getParamList().get(3).getVarParam().getName(), substringParams.get(3));
            } else if (referencedFunction instanceof BuiltInLeft){
                if(funcRef.getParamList().size() != 3)
                    throw new SyntaxErrorException("Function BuiltInLeft can only accept 3 parameters.");
                ArrayList<InterpreterDataType> leftParams = new ArrayList<>();
                //add the parameters to the new ArrayList, with their evaluated expressions and the appropriate mutability
                leftParams.add(expression(funcRef.getParamList().get(0).getStaticParam(), nameToIDT));
                leftParams.get(0).setChangeable(false);
                leftParams.add(expression(funcRef.getParamList().get(1).getStaticParam(), nameToIDT));
                leftParams.get(1).setChangeable(false);
                leftParams.add(expression(funcRef.getParamList().get(2).getVarParam(), nameToIDT));
                leftParams.get(2).setChangeable(true);
                //call execute which changes the values in the arrayList
                ((BuiltInLeft)referencedFunction).execute(leftParams);
                nameToIDT.put(funcRef.getParamList().get(2).getVarParam().getName(), leftParams.get(2));
            } else if (referencedFunction instanceof BuiltInRight){
                if(funcRef.getParamList().size() != 3)
                    throw new SyntaxErrorException("Function Right can only accept 2 parameters.");
                ArrayList<InterpreterDataType> rightParams = new ArrayList<>();
                //add the parameters to the new ArrayList, with their evaluated expressions and the appropriate mutability
                rightParams.add(expression(funcRef.getParamList().get(0).getStaticParam(), nameToIDT));
                rightParams.get(0).setChangeable(false);
                rightParams.add(expression(funcRef.getParamList().get(1).getStaticParam(), nameToIDT));
                rightParams.get(1).setChangeable(false);
                rightParams.add(expression(funcRef.getParamList().get(2).getVarParam(), nameToIDT));
                rightParams.get(2).setChangeable(true);
                //call execute which changes the values in the arrayList
                ((BuiltInRight)referencedFunction).execute(rightParams);
                nameToIDT.put(funcRef.getParamList().get(2).getVarParam().getName(), rightParams.get(2));
            } else if (referencedFunction instanceof BuiltInRealToInteger){
                if(funcRef.getParamList().size() != 2)
                    throw new SyntaxErrorException("Function RealToInteger can only accept 2 parameters.");
                ArrayList<InterpreterDataType> realToIntParams = new ArrayList<>();
                //add the parameters to the new ArrayList, with their evaluated expressions and the appropriate mutability
                realToIntParams.add(expression(funcRef.getParamList().get(0).getStaticParam(), nameToIDT));
                realToIntParams.get(0).setChangeable(false);
                realToIntParams.add(expression(funcRef.getParamList().get(1).getVarParam(), nameToIDT));
                realToIntParams.get(1).setChangeable(true);
                //call execute which changes the values in the arrayList
                ((BuiltInRealToInteger)referencedFunction).execute(realToIntParams);
                nameToIDT.put(funcRef.getParamList().get(1).getVarParam().getName(), realToIntParams.get(1));
            } else if (referencedFunction instanceof BuiltInIntegerToReal){
                if(funcRef.getParamList().size() != 2)
                    throw new SyntaxErrorException("Function IntegerToReal can only accept 2 parameters.");
                ArrayList<InterpreterDataType> intToRealParams = new ArrayList<>();
                //add the parameters to the new ArrayList, with their evaluated expressions and the appropriate mutability
                intToRealParams.add(expression(funcRef.getParamList().get(0).getStaticParam(), nameToIDT));
                intToRealParams.get(0).setChangeable(false);
                intToRealParams.add(expression(funcRef.getParamList().get(1).getVarParam(), nameToIDT));
                intToRealParams.get(1).setChangeable(true);
                //call execute which changes the values in the arrayList
                ((BuiltInIntegerToReal)referencedFunction).execute(intToRealParams);
                nameToIDT.put(funcRef.getParamList().get(1).getVarParam().getName(), intToRealParams.get(1));
            } else if (referencedFunction instanceof BuiltInGetRandom){
                if(funcRef.getParamList().size() != 1)
                    throw new SyntaxErrorException("Function GetRandom can only accept 1 parameter.");
                ArrayList<InterpreterDataType> getRandParams = new ArrayList<>();
                //add the parameters to the new ArrayList, with their evaluated expressions and the appropriate mutability
                getRandParams.add(expression(funcRef.getParamList().get(0).getVarParam(), nameToIDT));
                getRandParams.get(0).setChangeable(true);
                //call execute which changes the values in the arrayList
                ((BuiltInGetRandom)referencedFunction).execute(getRandParams);
                nameToIDT.put(funcRef.getParamList().get(0).getVarParam().getName(), getRandParams.get(0));
            } else if(referencedFunction instanceof BuiltInWrite){
                //for write, we only need to execute, not read anything back
                ArrayList<InterpreterDataType> writeParams = generateIDTFromParams(funcRef, nameToIDT);
                ((BuiltInWrite)referencedFunction).execute(writeParams);
            } else if (referencedFunction instanceof BuiltInRead){
                //we read the values in and send them to nameToIDT
                ArrayList<InterpreterDataType> readParams = generateIDTFromParams(funcRef, nameToIDT);
                ((BuiltInRead)referencedFunction).execute(readParams);
                for(int i = 0; i < readParams.size(); i++){
                    nameToIDT.put(funcRef.getParamList().get(i).getName(), readParams.get(i));
                }
            }
        } else {
            //validating the type of the parameters, but we need to check either the VariableReferenceNodes static or var stored parameter
            if(funcRef.getParamList().size() != program.getFunctions().get(funcRef.getName()).getParameters().size())
                throw new SyntaxErrorException("Passed parameters to function " + funcRef.getName() + " does not match the number parameters in the functions declaration.");
            for(int i = 0; i < funcRef.getParamList().size(); i++){
                if(program.getFunctions().get(funcRef.getName()).getParameters().get(i).getNode() instanceof IntegerNode) {
                    if(funcRef.getParamList().get(i).getVarParam() != null) {
                        if (!(nameToIDT.get(funcRef.getParamList().get(i).getVarParam().getName()) instanceof IntegerDataType))
                            throw new SyntaxErrorException("Parameter " + i + " of function " + funcRef.getName() + " must be of type integer.");
                    } else {
                        if (!(nameToIDT.get(funcRef.getParamList().get(i).getStaticParam().getName()) instanceof IntegerDataType)) {
                            throw new SyntaxErrorException("Parameter " + i + " of function " + funcRef.getName() + " must be of type integer.");
                        }
                    }
                }else if(program.getFunctions().get(funcRef.getName()).getParameters().get(i).getNode() instanceof RealNode) {
                    if(funcRef.getParamList().get(i).getVarParam() != null) {
                        if (!(nameToIDT.get(funcRef.getParamList().get(i).getVarParam().getName()) instanceof RealDataType))
                            throw new SyntaxErrorException("Parameter " + i + " of function " + funcRef.getName() + " must be of type real.");
                    } else {
                        if (!(nameToIDT.get(funcRef.getParamList().get(i).getStaticParam().getName()) instanceof RealDataType))
                            throw new SyntaxErrorException("Parameter " + i + " of function " + funcRef.getName() + " must be of type real.");
                    }
                }else if(program.getFunctions().get(funcRef.getName()).getParameters().get(i).getNode() instanceof StringNode) {
                    if(funcRef.getParamList().get(i).getVarParam() != null) {
                        if (!(nameToIDT.get(funcRef.getParamList().get(i).getVarParam().getName()) instanceof StringDataType))
                            throw new SyntaxErrorException("Parameter " + i + " of function " + funcRef.getName() + " must be of type string.");
                    } else {
                        if (!(nameToIDT.get(funcRef.getParamList().get(i).getStaticParam().getName()) instanceof StringDataType))
                            throw new SyntaxErrorException("Parameter " + i + " of function " + funcRef.getName() + " must be of type string.");
                    }
                }else if(program.getFunctions().get(funcRef.getName()).getParameters().get(i).getNode() instanceof CharacterNode) {
                    if(funcRef.getParamList().get(i).getVarParam() != null) {
                        if (!(nameToIDT.get(funcRef.getParamList().get(i).getVarParam().getName()) instanceof CharacterDataType))
                            throw new SyntaxErrorException("Parameter " + i + " of function " + funcRef.getName() + " must be of type character.");
                    } else {
                        if (!(nameToIDT.get(funcRef.getParamList().get(i).getStaticParam().getName()) instanceof CharacterDataType))
                            throw new SyntaxErrorException("Parameter " + i + " of function " + funcRef.getName() + " must be of type character.");
                    }
                }else if(program.getFunctions().get(funcRef.getName()).getParameters().get(i).getNode() instanceof BooleanNode) {
                    if(funcRef.getParamList().get(i).getVarParam() != null) {
                        if (!(nameToIDT.get(funcRef.getParamList().get(i).getVarParam().getName()) instanceof BooleanDataType))
                            throw new SyntaxErrorException("Parameter " + i + " of function " + funcRef.getName() + " must be of type integer.");
                    } else {
                        if (!(nameToIDT.get(funcRef.getParamList().get(i).getStaticParam().getName()) instanceof BooleanDataType))
                            throw new SyntaxErrorException("Parameter " + i + " of function " + funcRef.getName() + " must be of type integer.");
                    }
                }
            }
            //add the values of the variables from this function to a new hash map holding the same values, but under the alias of the other functions
            //but under the alias of the other functions names
            HashMap<String, InterpreterDataType> valuesToSend = new HashMap<>();
            for(int i = 0; i < funcRef.getParamList().size(); i++) {
                if (funcRef.getParamList().get(i).getVarParam() != null) {
                    valuesToSend.put(referencedFunction.getParameters().get(i).getName(), nameToIDT.get(funcRef.getParamList().get(i).getVarParam().getName()));
                    valuesToSend.get(referencedFunction.getParameters().get(i).getName()).setChangeable(true);
                } else {
                    valuesToSend.put(referencedFunction.getParameters().get(i).getName(), nameToIDT.get(funcRef.getParamList().get(i).getStaticParam().getName()));
                    valuesToSend.get(referencedFunction.getParameters().get(i).getName()).setChangeable(false);
                }
            }
            //ensure that the mutability of the functions being sent to interpretFunction are appropriate to the function declaration
            //basically checking for the correct var and non var parameters
            for(int i = 0; i < funcRef.getParamList().size(); i++){
                if(funcRef.getParamList().get(i).getVarParam() != null){
                    if(!referencedFunction.getParameters().get(i).getNode().isChangeable() && valuesToSend.get(referencedFunction.getParameters().get(i).getName()).isChangeable())
                        throw new SyntaxErrorException("Parameter " + (i + 1) + " of the function " + referencedFunction.getName() + " must be static.");
                } else {
                    if(referencedFunction.getParameters().get(i).getNode().isChangeable() && !valuesToSend.get(referencedFunction.getParameters().get(i).getName()).isChangeable())
                        throw new SyntaxErrorException("Parameter " + (i + 1) + " of the function " + referencedFunction.getName() + " must be var.");
                }
            }
            interpretFunction(program.getFunctions().get(funcRef.getName()), valuesToSend);
            //add the changed values back to the current nameToIDT map
            for(int i = 0; i < funcRef.getParamList().size(); i++){
                if(valuesToSend.get(referencedFunction.getParameters().get(i).getName()).isChangeable()) {
                    if(funcRef.getParamList().get(i).getVarParam() != null)
                        nameToIDT.put(funcRef.getParamList().get(i).getVarParam().getName(), valuesToSend.get(referencedFunction.getParameters().get(i).getName()));
                    else
                        nameToIDT.put(funcRef.getParamList().get(i).getStaticParam().getName(), valuesToSend.get(referencedFunction.getParameters().get(i).getName()));
                }
            }
        }
    }

    public void interpretIf(IfNode ifIn, HashMap<String, InterpreterDataType> nameToIDT) throws SyntaxErrorException {
        //the if statements act the same as the shank ones, so we execute them if their booleanCompare is true
        if(booleanCompare(ifIn.getParam(), nameToIDT)){
            interpretBlock(ifIn.getStatementList(), nameToIDT);
        } else if (ifIn.hasNext()){
            interpretIf(ifIn.getNextIf(), nameToIDT);
        } else if (!ifIn.getElseStatements().isEmpty()){
            //if we have else statements and no more elseif, do the else block
            interpretBlock(ifIn.getElseStatements(), nameToIDT);
        }
    }
    public void interpretWhile(WhileNode whileIn, HashMap<String, InterpreterDataType> nameToIDT) throws SyntaxErrorException {
        //loop while the Shank condition is true
        while(booleanCompare(whileIn.getParam(), nameToIDT)){
            interpretBlock(whileIn.getStatementList(), nameToIDT);
        }
    }
    public void interpretRepeat(RepeatNode repeatIn, HashMap<String, InterpreterDataType> nameToIDT) throws SyntaxErrorException {
        //loop while the Shank condition is false
        while(!(booleanCompare(repeatIn.getParam(), nameToIDT))){
            interpretBlock(repeatIn.getStatementList(), nameToIDT);
        }
    }
    public void interpretFor(ForNode forIn, HashMap<String, InterpreterDataType> nameToIDT) throws SyntaxErrorException {
        //for loops in shank only accept integer ranges
        int base =  Integer.parseInt(expression(forIn.getBase(), nameToIDT).toString());
        int range = Integer.parseInt(expression(forIn.getRange(), nameToIDT).toString());
        if(base < range){
            for(int i = base; i < range; i++) {
                interpretBlock(forIn.getStatementList(), nameToIDT);
            }
        } else {
            for(int i = base; i > range; i--){
                interpretBlock(forIn.getStatementList(), nameToIDT);
            }
        }
    }
    private boolean booleanCompare(BooleanCompareNode bcnIn, HashMap<String, InterpreterDataType> nameToIDT) throws SyntaxErrorException {
        //get the left and right, then compare them based on the symbol and type
        InterpreterDataType left = expression(bcnIn.left, nameToIDT);
        InterpreterDataType right = expression(bcnIn.right, nameToIDT);
        if(left instanceof IntegerDataType){
            switch(bcnIn.getSymbol()) {
                case "<":
                    return Integer.parseInt(((IntegerDataType) left).toString()) < Integer.parseInt(((IntegerDataType) right).toString());
                case ">":
                    return Integer.parseInt(((IntegerDataType) left).toString()) > Integer.parseInt(((IntegerDataType) right).toString());
                case ">=":
                    return Integer.parseInt(((IntegerDataType) left).toString()) >= Integer.parseInt(((IntegerDataType) right).toString());
                case "<=":
                    return Integer.parseInt(((IntegerDataType) left).toString()) <= Integer.parseInt(((IntegerDataType) right).toString());
                case "=":
                    return Integer.parseInt(((IntegerDataType) left).toString()) == Integer.parseInt(((IntegerDataType) right).toString());
                case "<>":
                    return Integer.parseInt(((IntegerDataType) left).toString()) != Integer.parseInt(((IntegerDataType) right).toString());
            }
        } else {
            switch(bcnIn.getSymbol()){
                case "<":
                    return Double.parseDouble(((RealDataType) left).toString()) < Double.parseDouble(((RealDataType) right).toString());
                case ">":
                    return Double.parseDouble(((RealDataType) left).toString()) > Double.parseDouble(((RealDataType) right).toString());
                case ">=":
                    return Double.parseDouble(((RealDataType) left).toString()) >= Double.parseDouble(((RealDataType) right).toString());
                case "<=":
                    return Double.parseDouble(((RealDataType) left).toString()) <= Double.parseDouble(((RealDataType) right).toString());
                case "=":
                    return Double.parseDouble(((RealDataType) left).toString()) == Double.parseDouble(((RealDataType) right).toString());
                case "<>":
                    return Double.parseDouble(((RealDataType) left).toString()) != Double.parseDouble(((RealDataType) right).toString());
            }
        }
        return false;
    }

    private InterpreterDataType expression(Node nodeIn, HashMap<String, InterpreterDataType> nameToIDT) throws SyntaxErrorException {
        //if were at a mathOpNode, call expression on the left and right with the correct operation and type
        //this will also convert numerical types to whatever type the left operand is, which should be changed
        if (nodeIn instanceof MathOpNode) {
            //only get the left and right if nodeIn is a mathOpNode
            InterpreterDataType left = expression(nodeIn.left, nameToIDT);
            InterpreterDataType right = expression(nodeIn.right, nameToIDT);
            //handles all appropriate types and operations
            if (left instanceof IntegerDataType) {
                switch (((MathOpNode) nodeIn).getSymbol()) {
                    case "+":
                        return new IntegerDataType((int) (Double.parseDouble(left.toString()) + Double.parseDouble(right.toString())));
                    case "-":
                        return new IntegerDataType((int) (Double.parseDouble(left.toString()) - Double.parseDouble(right.toString())));
                    case "*":
                        return new IntegerDataType((int) (Double.parseDouble(left.toString()) * Double.parseDouble(right.toString())));
                    case "/":
                        return new IntegerDataType((int) (Double.parseDouble(left.toString()) / Double.parseDouble(right.toString())));
                    case "%":
                        return new IntegerDataType((int) (Double.parseDouble(left.toString()) % Double.parseDouble(right.toString())));
                }
            } else if (left instanceof RealDataType) {
                switch (((MathOpNode) nodeIn).getSymbol()) {
                    case "+":
                        return new RealDataType(Double.parseDouble(expression(nodeIn.left, nameToIDT).toString()) + Double.parseDouble(expression(nodeIn.right, nameToIDT).toString()));
                    case "-":
                        return new RealDataType(Double.parseDouble(expression(nodeIn.left, nameToIDT).toString()) - Double.parseDouble(expression(nodeIn.right, nameToIDT).toString()));
                    case "*":
                        return new RealDataType(Double.parseDouble(expression(nodeIn.left, nameToIDT).toString()) * Double.parseDouble(expression(nodeIn.right, nameToIDT).toString()));
                    case "/":
                        return new RealDataType(Double.parseDouble(expression(nodeIn.left, nameToIDT).toString()) / Double.parseDouble(expression(nodeIn.right, nameToIDT).toString()));
                    case "%":
                        return new RealDataType(Double.parseDouble(expression(nodeIn.left, nameToIDT).toString()) % Double.parseDouble(expression(nodeIn.right, nameToIDT).toString()));
                }
            } else if (left instanceof StringDataType) {
                //make sure were adding the strings as no other operations are available
                if (!Objects.equals(((MathOpNode) nodeIn).getSymbol(), "+"))
                    throw new SyntaxErrorException("Operator " + ((MathOpNode) nodeIn).getSymbol() + " cannot be applied to a variable of type string.");
                //add the strings and return
                StringBuilder tempSB = new StringBuilder();
                tempSB.append(nodeIn.left.toString()).append(nodeIn.left.toString());
                return new StringDataType(tempSB.toString());
            }
        }
        //base cases, will only get here if nodeIn is a leaf node if the AST
        if (nodeIn instanceof IntegerNode) {
            return new IntegerDataType((((IntegerNode) nodeIn).getStoredNumber()));
        } else if (nodeIn instanceof RealNode) {
            return new RealDataType(((RealNode) nodeIn).getStoredNumber());
        } else if (nodeIn instanceof VariableReferenceNode) {
            return nameToIDT.get(nodeIn.getName());
        } else
            return new StringDataType(((StringNode) nodeIn).getValue());
    }
    private ArrayList<InterpreterDataType> generateIDTFromParams(FunctionCallNode funcRef, HashMap<String, InterpreterDataType> nameToIDT) throws SyntaxErrorException {
        ArrayList<InterpreterDataType> returnList = new ArrayList<>();
        //used for vardic functions, loops through all the PerameterNodes, evaluates them, sets the mutability and returns the list
        for(ParameterNode p : funcRef.getParamList()){
            if(p.isStatic()) {
                returnList.add(expression(p.getStaticParam(), nameToIDT));
                returnList.get(returnList.size() - 1).setChangeable(false);
            } else {
                returnList.add(expression(p.getVarParam(), nameToIDT));
                returnList.get(returnList.size() - 1).setChangeable(true);
            }
        }
        return returnList;
    }
    public void printNameToIDTMap(HashMap<String, InterpreterDataType> nameToIDT){
        System.out.println(nameToIDT.toString());
    }
}
