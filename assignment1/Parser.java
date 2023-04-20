package assignment1;

import java.util.*;

public class Parser {
    private List<Token> tokenList;
    private  StringBuilder expressionString = new StringBuilder();
    private int lineNumber = 1;
    private static final Set<String> booleanOperators = Set.of(">", "<", ">=", "<=", "=", "<>");
    public Parser(List<Token> tokenListIn){
        tokenList = new LinkedList<Token>(tokenListIn);
    }


    public ProgramNode parse() throws SyntaxErrorException {
        ProgramNode programNode = new ProgramNode();
        while(!tokenList.isEmpty()){
            FunctionNode tempFunct = function();
            programNode.addFunction(tempFunct);
            System.out.println(programNode.singleElementToString(tempFunct.getName()));
            //expressionString stores the expression() results, this allows the expression results to be output after the functions
            System.out.println(expressionString);
            expressionString = new StringBuilder();
        }
        return programNode;
    }
    private ArrayList<VariableNode> generateVariableNode( LinkedList<String> variableNames, boolean changeable) throws SyntaxErrorException {
        ArrayList<VariableNode> returnList = new ArrayList<>();
        //repeat for all the names found, turning them into the appropriate node type and adding them to a list to return
        Token varType = tokenList.get(0);
        tokenList.remove(0);
        //set ranges for values to NaN, so we can test if their ever assigned
        double upperRange = Double.NaN;
        double lowerRange = Double.NaN;
        Token tempToken = null;
        //make sure were not stealing from an array
        if(varType.getToken() != Token.tokenType.ARRAY)
            tempToken = matchAndRemove(new Token(Token.tokenType.FROM, ""));
        if(tempToken != null && varType.getToken() != Token.tokenType.ARRAY){
            //check for the first number
            tempToken = matchAndRemove(new Token(Token.tokenType.NUMBER, tokenList.get(0).getValue()));
            if(tempToken == null)
                throw new SyntaxErrorException("Missing base of range in variable declaration. Error on line " + lineNumber);
            //saving first number
            lowerRange = Double.parseDouble(tempToken.getValue());
            //checking for to
            tempToken = matchAndRemove(new Token(Token.tokenType.TO, ""));
            if(tempToken == null)
                throw new SyntaxErrorException("Missing 'to' in variable declaration. Error on line " + lineNumber);
            //checking and saving second number
            tempToken = matchAndRemove(new Token(Token.tokenType.NUMBER, tokenList.get(0).getValue()));
            if(tempToken == null)
                throw new SyntaxErrorException("Missing end of range in variable declaration. Error on line " + lineNumber);
            upperRange = Double.parseDouble(tempToken.getValue());
        }
        for (String variableName : variableNames) {
            //using getToken is easier then matchAndRemoving for every type
            switch (varType.getToken()) {
                case INTEGER -> returnList.add(new VariableNode(new IntegerNode(changeable, variableName), lowerRange, upperRange));
                case STRING, STRINGLITERAL -> returnList.add(new VariableNode(new StringNode(changeable, variableName), lowerRange, upperRange));
                case CHARACTER, CHARACTERLITERAL -> returnList.add(new VariableNode(new CharacterNode(changeable, variableName)));
                case REAL -> returnList.add(new VariableNode(new RealNode(changeable, variableName), lowerRange, upperRange));
                case BOOLEAN -> returnList.add(new VariableNode(new BooleanNode(changeable, variableName)));
                case ARRAY -> {
                    //special case for array because we need to include the range and consume the extra tokens
                    //removing the ARRAY token
                    tempToken = matchAndRemove(new Token(Token.tokenType.FROM, ""));
                    if (tempToken == null)
                        throw new SyntaxErrorException("Missing 'from' token in array declaration. Error on line " + lineNumber);
                    tempToken = matchAndRemove(new Token(Token.tokenType.NUMBER, "0"));
                    if (tempToken == null)
                        throw new SyntaxErrorException("Start of array cannot differ from 0. Error on line " + lineNumber);
                    tempToken = matchAndRemove(new Token(Token.tokenType.TO, ""));
                    if (tempToken == null)
                        throw new SyntaxErrorException("Missing 'to' token in array declaration. Error on line " + lineNumber);
                    tempToken = matchAndRemove(new Token(Token.tokenType.NUMBER, tokenList.get(0).getValue()));
                    if (tempToken == null)
                        throw new SyntaxErrorException("Missing upper range in array declaration. Error on line " + lineNumber);
                    //storing upper bound, we don't need to store the lower as it should always be 0
                    int arrayRange = Integer.parseInt(tempToken.getValue());
                    tempToken = matchAndRemove(new Token(Token.tokenType.OF, ""));
                    if (tempToken == null)
                        throw new SyntaxErrorException("Missing 'of' token in array declaration. Error on line " + lineNumber);
                    //add to list with the range included
                    varType = matchAndRemove(new Token(tokenList.get(0).getToken(), tokenList.get(0).getValue()));
                    switch (varType.getToken()) {
                        case INTEGER -> returnList.add(new VariableNode(new IntegerNode(changeable, variableName), arrayRange));
                        case STRINGLITERAL -> returnList.add(new VariableNode(new StringNode(changeable, variableName), arrayRange));
                        case CHARACTERLITERAL -> returnList.add(new VariableNode(new CharacterNode(changeable, variableName), arrayRange));
                        case REAL -> returnList.add(new VariableNode(new RealNode(changeable, variableName), arrayRange));
                        //booleans cannot be arrays as stated in the language definition
                        case BOOLEAN -> throw new SyntaxErrorException("Type Boolean cannot be an array. Error on line " + lineNumber);
                    }
                }
                default -> throw new SyntaxErrorException("Missing type of parameter variable. Error on line " + lineNumber);
            }
        }
        return returnList;
    }
    private ArrayList<VariableNode> parameterDeclarations() throws SyntaxErrorException {
        ArrayList<VariableNode> variableList = new ArrayList<>();
        boolean changeable;
        //loop for parameters until the right parenthesis is found
        while(tokenList.get(0).getToken() != Token.tokenType.RPAREN) {
            //check for var and initialize changeable accordingly
            Token tempToken = matchAndRemove(new Token(Token.tokenType.VAR, ""));
            changeable = tempToken != null;
            //list in loop to store the names found before the colon
            LinkedList<String> variableNames = new LinkedList<>();
            do {
                //accumulate identifiers while there is a comma as the next token
                tempToken = matchAndRemove(new Token(Token.tokenType.IDENTIFIER, tokenList.get(0).getValue()));
                if (tempToken == null)
                    throw new SyntaxErrorException("Missing identifier in function declaration. Error on line " + lineNumber);
                variableNames.add(tempToken.getValue());
                tempToken = matchAndRemove(new Token(Token.tokenType.COMMA, ""));
            } while (tempToken != null);
            //check for colon
            tempToken = matchAndRemove(new Token(Token.tokenType.COLON, ":"));
            if(tempToken == null)
                throw new SyntaxErrorException("Missing ':' in variable declaration. Error on line " + lineNumber);
            //send the names to be turned into variableNodes of the appropriate type and mutable state
            variableList.addAll(generateVariableNode(variableNames, changeable));
            //check for more parameters
            tempToken = matchAndRemove(new Token(Token.tokenType.SEMICOLON, ""));
            if(tempToken == null && peek(0).getToken() != Token.tokenType.RPAREN)
                throw new SyntaxErrorException("Missing ';' in variable declaration of function parameters. Error on line " + lineNumber);
        }
        return variableList;
    }

    private ArrayList<VariableNode> constOrVariableDeclarations() throws SyntaxErrorException {
        ArrayList<VariableNode> constOrVarList = new ArrayList<>();
        //loop while the first token is CONSTANTS or VARIABLES
        while(peek(0).getToken() == Token.tokenType.CONSTANTS || peek(0).getToken() == Token.tokenType.VARIABLES) {
            Token tempToken = matchAndRemove(new Token(Token.tokenType.CONSTANTS, ""));
            //case for constants
            if (tempToken != null) {
                //get identifier
                tempToken = matchAndRemove(new Token(Token.tokenType.IDENTIFIER, tokenList.get(0).getValue()));
                if(tempToken == null)
                    throw new SyntaxErrorException("Missing identifier in constant declaration. Error on line " + lineNumber);
                //store the name of identifier
                String name = tempToken.getValue();
                //check for assignment token
                tempToken = matchAndRemove(new Token(Token.tokenType.ASSIGNMENT, ":="));
                if(tempToken == null)
                    throw new SyntaxErrorException("Missing assignment operator in constant declaration. Error on line " + lineNumber);
                //handle negative numbers using a -1 or 1 int
                tempToken = matchAndRemove(new Token(Token.tokenType.MINUS, "-"));
                int relationToZero = 1;
                if(tempToken != null)
                    relationToZero = -1;
                tempToken = peek(0);
                //making sure the negative is being applied to a numeric type
                if(relationToZero == -1 && tempToken.getToken() != Token.tokenType.NUMBER)
                    throw new SyntaxErrorException("Non-numeric type cannot be negative. Error on line " + lineNumber);
                //creating constant and adding it to the list
                switch(tempToken.getToken()){
                    case BOOLEAN -> constOrVarList.add(new VariableNode(new BooleanNode(tempToken.getValue(),false, name)));
                    case STRINGLITERAL -> constOrVarList.add(new VariableNode(new StringNode(tempToken.getValue(),false, name)));
                    case CHARACTERLITERAL -> constOrVarList.add(new VariableNode(new CharacterNode(tempToken.getValue(),false, name)));
                    case NUMBER -> {
                        if(tempToken.getValue().contains("."))
                            constOrVarList.add(new VariableNode(new RealNode(String.valueOf(relationToZero * Double.parseDouble(tempToken.getValue())),false, name)));
                        else
                            constOrVarList.add(new VariableNode(new IntegerNode(String.valueOf(relationToZero * Integer.parseInt(tempToken.getValue())),false, name)));
                    }
                    default -> throw new SyntaxErrorException("Invalid type in constant declaration. Error on line " + lineNumber);
                }
                tokenList.remove(0);
                expectEndOfLine();
                //skip the VARIABLE case and return to the start of the loop
                continue;
            }
            //consume the VARIABLE token
            tempToken = matchAndRemove(new Token(Token.tokenType.VARIABLES, ""));
            LinkedList<String> names = new LinkedList<>();
            //loop through and gather identifiers until no more commas are detected, add them to the names list
            if (tempToken != null) {
                do{
                    tempToken = matchAndRemove(new Token(Token.tokenType.IDENTIFIER, tokenList.get(0).getValue()));
                    if (tempToken == null)
                        throw new SyntaxErrorException("Missing identifier in function declaration. Error on line " + lineNumber);
                    names.add(tempToken.getValue());
                    tempToken = matchAndRemove(new Token(Token.tokenType.COMMA, ""));
                }while(tempToken != null);
            }
            //look for colon
            tempToken = matchAndRemove(new Token(Token.tokenType.COLON, ":"));
            if(tempToken == null)
                throw new SyntaxErrorException("Missing ':' in variable declaration. Error on line " + lineNumber);
            //generate variable nodes for each name in names
            constOrVarList.addAll(generateVariableNode(names, true));
            expectEndOfLine();
        }
        return constOrVarList;
    }
    private FunctionNode function() throws SyntaxErrorException{
        //ensure there are tokens to process
        if(tokenList.isEmpty())
            return null;
        //check for define
        Token tempToken = matchAndRemove(new Token(Token.tokenType.DEFINE, ""));
        if(tempToken == null)
            throw new SyntaxErrorException("Missing define before function declaration. Error on line " + lineNumber);
        //check for function name, and store it
        tempToken = matchAndRemove(new Token(Token.tokenType.IDENTIFIER, tokenList.get(0).getValue()));
        if(tempToken == null)
            throw new SyntaxErrorException("Missing identifier in function declaration. Error on line " + lineNumber);
        String identifierName = tempToken.getValue();
        //check for left parenthesis
        tempToken = matchAndRemove(new Token(Token.tokenType.LPAREN, ""));
        if(tempToken == null)
            throw new SyntaxErrorException("Missing parenthesis before parameters. Error on line " + lineNumber);
        //create new function node and set its parameters using parameterDeclarations()
        FunctionNode tempFunctNode = new FunctionNode(identifierName);
        tempFunctNode.addParameters(parameterDeclarations());
        //check for right parenthesis
        tempToken = matchAndRemove(new Token(Token.tokenType.RPAREN, ""));
        if(tempToken == null){
            throw new SyntaxErrorException("Missing parenthesis after parameters. Error on line " + lineNumber);
        }
        expectEndOfLine();
        //add constants and variables next
        tempFunctNode.addBodyConstOrVariable(constOrVariableDeclarations());
        //expect an indent
        tempToken = matchAndRemove(new Token(Token.tokenType.INDENT, ""));
        if(tempToken == null)
            throw new SyntaxErrorException("Missing indent after function declaration. Error on line " + lineNumber);
        //loop through expressions and later statements until a DEDENT is found, should be updated later to look for multiple dedents at once
        while(matchAndRemove(new Token(Token.tokenType.DEDENT, "")) == null){
            //check for empty list or a new define before dedenting
            if(Objects.equals(peek(1), new Token(Token.tokenType.DEFINE, "")))
                throw new SyntaxErrorException("Missing dedent after function body. Error on line " + lineNumber);
            //add the statements to the current function
            tempFunctNode.addStatements(generateStatements());
            if(tokenList.isEmpty())
                break;
        }
        if(!tokenList.isEmpty())
            expectEndOfLine();
        return tempFunctNode;
    }
    private Node assignment() throws SyntaxErrorException {
        AssignmentNode assign;
        //check for identifier
        Token tempToken = matchAndRemove(new Token(Token.tokenType.IDENTIFIER, tokenList.get(0).getValue()));
        if(tempToken == null)
            throw new SyntaxErrorException("An assignment needs an identifier on the left side. Error on line " + lineNumber);
        //check for an array index
        if(peek(0).getToken() == Token.tokenType.LBRACKET) {
            //remove the bracket, no need to do a match and remove as we know its there, but expression needs it when factor is called so
            //we leave it in the list for convenience
            tokenList.remove(0);
            //expression as a parameter will set the optional range of the variableReferenceNode to
            //an array range which is built from a expression tree in which factor will create a new variableReferenceNode if more brackets are
            //found, which will all be linked through this variable reference nodes range
            assign = new AssignmentNode(new VariableReferenceNode(tempToken.getValue(), expression()));
            //remove the right bracket
            tokenList.remove(0);
        } else {
            //add non array variable
            assign = new AssignmentNode(new VariableReferenceNode(tempToken.getValue()));
        }
        //check for assignment
        tempToken = matchAndRemove(new Token(Token.tokenType.ASSIGNMENT, ":="));
        if(tempToken == null)
            throw new SyntaxErrorException("Missing assignment operator. Error on line " + lineNumber);
        //bool compare the right side to build a tree
        assign.setTarget(boolCompare());
        return assign;
    }
    private Node statement() throws SyntaxErrorException {
        return assignment();
    }
    private ArrayList<StatementNode> generateStatements() throws SyntaxErrorException {
        //finds and adds all statements to a collection before returning
        ArrayList<StatementNode> returnList = new ArrayList<>();
        //loop while more statements are found, statements include assignments, loops, ifs and function calls
        while(peek(0).getToken() == Token.tokenType.IDENTIFIER || peek(0).getToken() == Token.tokenType.IF
                || peek(0).getToken() == Token.tokenType.WHILE || peek(0).getToken() == Token.tokenType.REPEAT
                || peek(0).getToken() == Token.tokenType.FOR){
            //check for function
            if((peek(0).getToken() == Token.tokenType.IDENTIFIER) &&
                    (peek(1).getToken() != Token.tokenType.ASSIGNMENT)){
                returnList.add(parseFunctionCall());
            }else if(peek(0).getToken() == Token.tokenType.IDENTIFIER) {
                returnList.add((StatementNode) statement());
                expectEndOfLine();
            } else if(peek(0).getToken() == Token.tokenType.IF){
                returnList.add(parseIf());
            } else if(peek(0).getToken() == Token.tokenType.WHILE){
                returnList.add(parseWhile());
            } else if(peek(0).getToken() == Token.tokenType.REPEAT){
            returnList.add(parseRepeat());
            } else if(peek(0).getToken() == Token.tokenType.FOR){
                returnList.add(parseFor());
            }
            //ensure we aren't going out of bounds
            if(tokenList.isEmpty())
                break;
        }
        return returnList;
    }

    private Node boolCompare() throws SyntaxErrorException {
        Node expressionMathOpNode =  expression();
        //return expression if no comparison is found
        //booleanOperators is a static final string set storing all six possible comparisons
        if(!booleanOperators.contains(peek(0).getValue()))
            return expressionMathOpNode;
        //no need to check if the next symbol is valid as the function would have returned if it wasnt there
        BooleanCompareNode compareSymbol = new BooleanCompareNode(tokenList.get(0).getValue());
        compareSymbol.setLeft(expressionMathOpNode);
        //remove comparison symbol
        tokenList.remove(0);
        //unsure what type expression will return so storing in parent node
        Node n = expression();
        compareSymbol.setRight(n);
        return compareSymbol;
    }
    private Node expression() throws SyntaxErrorException {
        Node termNode = term();
        MathOpNode expMathOp = new MathOpNode();
        // check for -
        Token tempToken = matchAndRemove(new Token(Token.tokenType.MINUS, tokenList.get(0).getValue()));
        if(tempToken != null) {
            //if -, set the term to the left
            if (tempToken.getValue().equals("-")) {
                expMathOp.setSymbol(tempToken.getValue());
                expMathOp.setLeft(termNode);
            }
        }
        //check for +
        tempToken = matchAndRemove(new Token(Token.tokenType.PLUS, tokenList.get(0).getValue()));
        if(tempToken != null) {
            //if +, set term to the left
            if (tempToken.getValue().equals("+")) {
                expMathOp.setSymbol(tempToken.getValue());
                expMathOp.setLeft(termNode);
            }
        }
        //check for end of expression
        tempToken = matchAndRemove(new Token(Token.tokenType.RPAREN, tokenList.get(0).getValue()));
        if(expMathOp.getSymbol() == null)
            return termNode;
        //check for more possible expressions, they become the right node
        if(peek(1).getValue().equals("+") || peek(1).getValue().equals("-")){
            expMathOp.setRight(expression());
            return expMathOp;
        }
        if(booleanOperators.contains(peek(0).getValue())){
            return expMathOp;
        }
        //get the right term and return
        termNode = term();
        expMathOp.setRight(termNode);
        return expMathOp;
    }
    private Node term() throws SyntaxErrorException {
        Node factNode = factor();
        //get rid of ) if found
        Token tempToken = matchAndRemove(new Token(Token.tokenType.RPAREN, tokenList.get(0).getValue()));
        MathOpNode tempMathOp = new MathOpNode();
        //check for *
        tempToken = matchAndRemove(new Token(Token.tokenType.MULTIPLY, tokenList.get(0).getValue()));
        if (tempToken != null) {
            //if *, set token and set the factor to the left
            if (tempToken.getValue().equals("*")) {
                tempMathOp.setSymbol(tempToken.getValue());
                tempMathOp.setLeft(factNode);
            }
        }
        //check for /
        tempToken = matchAndRemove(new Token(Token.tokenType.DIVIDE, tokenList.get(0).getValue()));
        if(tempToken != null) {
            //if /, set token and set the factor to the left
            if (tempToken.getValue().equals("/")) {
                tempMathOp.setSymbol(tempToken.getValue());
                tempMathOp.setLeft(factNode);
            }
        }
        //if no * or /, return to expression
        if(tempMathOp.getSymbol() == null)
            return factNode;
        //check for more terms before returning to expression, maintains order of operations
        if(peek(1).getValue().equals("*") || peek(1).getValue().equals("/")){
            tempMathOp.setRight(term());
            return tempMathOp;
        }
        factNode = factor();
        tempMathOp.setRight(factNode);
        //return the node containing * or /
        return tempMathOp;
    }
    private Node factor() throws SyntaxErrorException {
        //both need to be initialized before using or java throws an error
        RealNode tempReal = new RealNode();
        IntegerNode tempInt = new IntegerNode();
        //check for a negative number, then multiply the next number token by -1
        Token tempToken = matchAndRemove(new Token(Token.tokenType.MINUS, "-"));
        if(tempToken != null){
            tempToken = matchAndRemove(new Token(Token.tokenType.NUMBER, tokenList.get(0).getValue()));
            //check for real vs integer
            if(tempToken.getValue().contains(".")) {
                tempReal = new RealNode(String.valueOf(Double.parseDouble(tempToken.getValue()) * - 1));
                tempReal.setStoredNumber(-1 * Double.parseDouble(tempToken.getValue()));
            }
            else {
                tempInt = new IntegerNode(String.valueOf(Integer.parseInt(tempToken.getValue()) * - 1));
                tempInt.setStoredNumber(-1 * Integer.parseInt(tempToken.getValue()));
            }
            if(tokenList.get(0).getValue().contains(". Error on line " + lineNumber))
                return tempReal;
            else return tempInt;
        }
        //positive case
        tempToken = matchAndRemove(new Token(Token.tokenType.NUMBER, tokenList.get(0).getValue()));
        if(tempToken != null){
            //check for real vs integer
            if(tempToken.getValue().contains("."))
                tempReal.setStoredNumber(Double.parseDouble(tempToken.getValue()));
            else tempInt.setStoredNumber(Integer.parseInt(tempToken.getValue()));
            if(tempToken.getValue().contains("."))
                return tempReal;
            else return tempInt;
        }
        MathOpNode tempMathOpNode = new MathOpNode();
        //if there is a (, call a new expression
        tempToken = matchAndRemove(new Token(Token.tokenType.LPAREN, tokenList.get(0).getValue()));
        if(tempToken != null){
            if(tempToken.getToken() == Token.tokenType.LPAREN)
                tempMathOpNode = (MathOpNode) expression();
        }
        //check for variable reference
        tempToken = matchAndRemove(new Token(Token.tokenType.IDENTIFIER, tokenList.get(0).getValue()));
        if(tempToken != null){
            //check for array type
            if(peek(0).getToken() == Token.tokenType.LBRACKET) {
                tokenList.remove(0);
                //call expression for index, if it is another identifer and array, this will repeat and link to the original
                //variableReferenceNode in which expression is first called as the range parameter
                VariableReferenceNode tempVarRefNode = new VariableReferenceNode(tempToken.getValue(), expression());
                //check for right bracket
                tempToken = matchAndRemove(new Token(Token.tokenType.RBRACKET, tokenList.get(0).getValue()));
                if(tempToken == null)
                    throw new SyntaxErrorException("Missing closing bracket after array reference. Error on line " + lineNumber);
                return tempVarRefNode;
            } else
                return new VariableReferenceNode(tempToken.getValue());
        }
        tempToken = matchAndRemove(new Token(Token.tokenType.CHARACTERLITERAL, tokenList.get(0).getValue()));
        if(tempToken != null){
            CharacterNode storedChar = new CharacterNode(tempToken.getValue());
            return storedChar;
        }
        tempToken = matchAndRemove(new Token(Token.tokenType.STRINGLITERAL, tokenList.get(0).getValue()));
        if(tempToken != null){
            StringNode storedString = new StringNode(tempToken.getValue());
            return storedString;
        }
        return tempMathOpNode;
    }
    private IfNode parseIf() throws SyntaxErrorException {
        //remove the if, we dont need to store it as the only way we'd get here is if we found an if
        matchAndRemove(new Token(Token.tokenType.IF, ""));
        //get the parameter, and ensure it is a boolean compare
        Node parameter = boolCompare();
        if(!(parameter instanceof BooleanCompareNode))
            throw new SyntaxErrorException("Parameter of if statement must be of type boolean. Error on line " + lineNumber);
        IfNode tempIfNode = new IfNode((BooleanCompareNode) parameter);
        //check for then
        Token tempToken = matchAndRemove(new Token(Token.tokenType.THEN, ""));
        if(tempToken == null)
            throw new SyntaxErrorException("Missing 'then' in if declaration. Error seen on line " + lineNumber);
        expectEndOfLine();
        //check for indent in the body
        tempToken = matchAndRemove(new Token(Token.tokenType.INDENT, ""));
        if(tempToken == null)
            throw new SyntaxErrorException("Missing indent in if statement body. Error on line " + lineNumber);
        //add statements, if there is anything nested, generateStatements will catch it
        tempIfNode.setStatementList(generateStatements());
        //check for dedent
        tempToken = matchAndRemove(new Token(Token.tokenType.DEDENT, ""));
        if(tempToken == null)
            throw new SyntaxErrorException("Missing dedent after if body statements. Error on line " + lineNumber);
        //if there is an elseif, link it to this(almost like a linked list :)
        tempToken = matchAndRemove(new Token(Token.tokenType.ELSEIF, ""));
        if(tempToken != null) {
            tempIfNode.setNextIf(parseIf());
        }
        //if there is an else, set the elseStatements of the last node to the list of generatedStatements
        tempToken = matchAndRemove(new Token(Token.tokenType.ELSE, ""));
        if(tempToken != null){
            expectEndOfLine();
            //check for indent
            tempToken = matchAndRemove(new Token(Token.tokenType.INDENT, ""));
            if(tempToken == null)
                throw new SyntaxErrorException("Missing indent in else statement body. Error on line " + lineNumber);
            //getting statements
            tempIfNode.setElseStatements(generateStatements());
            //check for dedent
            tempToken = matchAndRemove(new Token(Token.tokenType.DEDENT, ""));
            if(tempToken == null)
                throw new SyntaxErrorException("Missing dedent after if body statements. Error on line " + lineNumber);
        }
        return tempIfNode;
    }
    private WhileNode parseWhile() throws SyntaxErrorException {
        //remove the while token
        tokenList.remove(0);
        //get the parameter and make sure its a BooleanCompareNode
        Node parameter = boolCompare();
        if(!(parameter instanceof BooleanCompareNode))
            throw new SyntaxErrorException("Parameter of while statement must be of type boolean. Error on line " + lineNumber);
        WhileNode tempWhileNode = new WhileNode((BooleanCompareNode) parameter);
        expectEndOfLine();
        //checking for indent
        Token tempToken = matchAndRemove(new Token(Token.tokenType.INDENT, ""));
        if(tempToken == null)
            throw new SyntaxErrorException("Missing indent before while body statements. Error on line " + lineNumber);
        //getting statements
        tempWhileNode.setStatementList(generateStatements());
        //checking for dedent
        tempToken = matchAndRemove(new Token(Token.tokenType.DEDENT, ""));
        if(tempToken == null)
            throw new SyntaxErrorException("Missing dedent after while body statements. Error on line " + lineNumber);
        return tempWhileNode;
    }
    private RepeatNode parseRepeat() throws SyntaxErrorException {
        //remove the repeat and until functions
        tokenList.remove(0);
        tokenList.remove(0);
        //get parameter and ensure that is a boolean compare
        Node parameter = boolCompare();
        if(!(parameter instanceof BooleanCompareNode))
            throw new SyntaxErrorException("Parameter of while statement must be of type boolean. Error on line " + lineNumber);
        RepeatNode tempRepeatNode = new RepeatNode((BooleanCompareNode) parameter);
        expectEndOfLine();
        //check for indent
        Token tempToken = matchAndRemove(new Token(Token.tokenType.INDENT, ""));
        if(tempToken == null)
            throw new SyntaxErrorException("Missing indent before repeat until body statements. Error on line " + lineNumber);
        //get statements
        tempRepeatNode.setStatementList(generateStatements());
        //check for dedents
        tempToken = matchAndRemove(new Token(Token.tokenType.DEDENT, ""));
        if(tempToken == null)
            throw new SyntaxErrorException("Missing dedent after repeat until body statements. Error on line " + lineNumber);
        return tempRepeatNode;
    }
    private ForNode parseFor() throws SyntaxErrorException{
        //remove the for token
        tokenList.remove(0);
        ForNode tempForNode = new ForNode();
        //check for identifier
        Token tempToken = matchAndRemove(new Token(Token.tokenType.IDENTIFIER, tokenList.get(0).getValue()));
        if(tempToken == null)
            throw new SyntaxErrorException("Missing identifier in for statement. Error on line " + lineNumber);
        //check for from token
        tempToken = matchAndRemove(new Token(Token.tokenType.FROM, ""));
        if(tempToken == null)
            throw new SyntaxErrorException("Missing from in for statement. Error on line " + lineNumber);
        //check for lower bound, and store it in tempForNode
        tempToken = matchAndRemove(new Token(Token.tokenType.NUMBER, tokenList.get(0).getValue()));
        if(tempToken == null)
            throw new SyntaxErrorException("Missing base of range number in for statement. Error on line " + lineNumber);
        tempForNode.setBase(new IntegerNode(tempToken.getValue()));
        //check for to token
        tempToken = matchAndRemove(new Token(Token.tokenType.TO, ""));
        if(tempToken == null)
            throw new SyntaxErrorException("Missing to in for statement. Error on line " + lineNumber);
        //check for upper bound and add it to tempForNode
        tempToken = matchAndRemove(new Token(Token.tokenType.NUMBER, tokenList.get(0).getValue()));
        if(tempToken == null)
            throw new SyntaxErrorException("Missing range number in for statement. Error on line " + lineNumber);
        tempForNode.setRange(new IntegerNode(tempToken.getValue()));
        expectEndOfLine();
        //check for indent
        tempToken = matchAndRemove(new Token(Token.tokenType.INDENT, ""));
        if(tempToken == null)
            throw new SyntaxErrorException("Missing indent in for loop body. Error on line " + lineNumber);
        //get statements and add them to tempForNode
        tempForNode.setStatementList(generateStatements());
        //check for dedent
        tempToken = matchAndRemove(new Token(Token.tokenType.DEDENT, ""));
        if(tempToken == null)
            throw new SyntaxErrorException("Missing dedent after for loop body. Error on line " + lineNumber);
        return tempForNode;
    }
    private ArrayList<ParameterNode> generateFunctionParameters() throws SyntaxErrorException {
        Token tempToken = null;
        ArrayList<ParameterNode> returnList = new ArrayList<>();
        do {
            //get more parameters while there is a comma after the parameter
            tempToken = matchAndRemove(new Token(Token.tokenType.VAR, ""));
            if(tempToken != null){
                tempToken = matchAndRemove(new Token(Token.tokenType.IDENTIFIER, tokenList.get(0).getValue()));
                returnList.add(new ParameterNode(new VariableReferenceNode(tempToken.getValue())));
            } else if(tokenList.get(0).getToken() == Token.tokenType.IDENTIFIER){
                returnList.add(new ParameterNode((Node)boolCompare()));
            } else {
                returnList.add(new ParameterNode(new IntegerNode(tokenList.get(0).getValue())));
                tokenList.remove(0);
            }
            tempToken = matchAndRemove(new Token(Token.tokenType.COMMA, ""));
        } while (tempToken != null);
        return returnList;
    }
    private FunctionCallNode parseFunctionCall() throws SyntaxErrorException {
        FunctionCallNode funcReference = new FunctionCallNode();
        funcReference.setName(tokenList.get(0).getValue());
        tokenList.remove(0);
        //generate the function parameter list and add it to the functionCall
        funcReference.setParamList(generateFunctionParameters());
        expectEndOfLine();
        return funcReference;
    }

    private Token matchAndRemove(Token in){
        //remove and return the next token if they match, or return null
        if(Objects.equals(tokenList.get(0).getValue(), in.getValue()) && tokenList.get(0).getToken() == in.getToken()){
            Token temp = tokenList.get(0);
            tokenList.remove(0);
            return temp;
        }
        return null;
    }
    private void expectEndOfLine() throws SyntaxErrorException {
        //if the last token after the full expression has been evaluated isnt EOL, throw an error
        Token temp = matchAndRemove(new Token(Token.tokenType.ENDOFLINE, ""));
        if(temp == null)
            throw new SyntaxErrorException("No end of line token. Error on line: " + lineNumber);
        lineNumber++;
        while(!tokenList.isEmpty() && tokenList.get(0).getToken() == Token.tokenType.ENDOFLINE){
            tokenList.remove(0);
            lineNumber++;
        }
    }
    private Token peek(int index){
        //returns the requested index token
        if(!tokenList.isEmpty()){
            if(tokenList.size() > index)
                return tokenList.get(index);
        }
        return null;
    }
    private StringBuilder tempString = new StringBuilder();

    public String TreeToString(Node n){
        tempString.append(n.toString());
        if(n.hasLeft()) {
            TreeToString(n.getLeft());
            tempString.append(",");
        }
        if(n.hasRight()) {
            TreeToString(n.getRight());
            tempString.append(")");
        }
        return tempString.toString();
    }
    public void printTree(Node n){
        //prints the tree from top down, formatted for convenience
        System.out.print(n.toString());
        if(n.hasLeft()) {
            printTree(n.getLeft());
            System.out.print(",");
        }
        if(n.hasRight())
            printTree(n.getRight());
        System.out.print(")");
    }
}
