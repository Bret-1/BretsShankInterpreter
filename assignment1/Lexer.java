package assignment1;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
public class Lexer {
    //list to store all processed tokens, probably to be used later
    private List<Token> tokenList = new LinkedList<>();
    private final HashMap<String, Token.tokenType> keywords = new HashMap<>();
    //both integers below are used to keep track of the number of tabs in a previous row
    private int numberOfIndentsPreviousLine = 0;
    private int numberOfIndentsInLine = 0;
    private Token.tokenType currentState;
    private int lineNumber;

    public Lexer() {
        populateKeywords();
        currentState = Token.tokenType.DEFAULT;
        lineNumber = 0;
    }

    //the lex method takes a singular string and breaks it into tokens whenever a space is detected
    public void lex(String nextLine) throws Exception {
        StringBuilder currentTokenString = new StringBuilder();
        lineNumber++;
        //number of spaces is used to count a possible of 4 spaces at the start of a line
        int numberOfSpaces = 0;

        //store the previous lines number of indents
        if(currentState != Token.tokenType.COMMENT) {
            numberOfIndentsInLine = 0;
        }

        if(!nextLine.contains("\t") && !nextLine.contains("    ")){
            for(;numberOfIndentsPreviousLine > 0; numberOfIndentsPreviousLine--){
                tokenList.add(new Token(Token.tokenType.DEDENT, ""));
                printToken();
            }
        }
        //checks for function after first function, this is a bandaid that will output the proper tokens for the parser to use,
        //ideally I'd re-write my lexer but we can work around it for now
        if(nextLine.contains("define") && !tokenList.isEmpty()){
            for(;numberOfIndentsPreviousLine > 0; numberOfIndentsPreviousLine--){
                tokenList.add(new Token(Token.tokenType.DEDENT, ""));
                printToken();
            }
            tokenList.add(new Token(Token.tokenType.ENDOFLINE, ""));
            printToken();
        }
        //loop until the full string is processed
        for (int i = 0; i < nextLine.length(); i++) {
            char currentChar = nextLine.charAt(i);
            switch (currentState) {
                //the DEFAULT case is the base case, indicating an empty currentTokenString
                case DEFAULT:
                    if (Character.isLetter(currentChar) || currentChar == ',' || currentChar == '(' || currentChar == ')') {
                        //switch case to WORD and add the first char
                        currentState = Token.tokenType.UNDETERMINED;
                    } else if (Character.isDigit(currentChar) || currentChar == '.' || currentChar == '0') {
                        //switch case to NUMBER and add the first char
                        //DOES NOT handle multiple '.'
                        currentState = Token.tokenType.NUMBER;
                        ///t and ' ' are counted to compare to the previous line
                    }else if(currentChar == '\t' || currentChar == ' '){
                        currentState = Token.tokenType.INDENT;
                        //decrement is needed as the next tokens will be consumed in the indent case
                        i--;
                    }else if(currentChar == '\"'){
                        currentState = Token.tokenType.STRINGLITERAL;
                    }else if(currentChar == '\''){
                        currentState = Token.tokenType.CHARACTERLITERAL;
                    }else if(currentChar == '{'){
                        currentState = Token.tokenType.COMMENT;
                    }else {
                        //all unidentifiable cases are sent to symbol
                        currentState = Token.tokenType.SYMBOL;
                    }
                    //all states consume the first char in the entered string
                    currentTokenString.append(currentChar);
                    break;
                case UNDETERMINED:
                    //WORD will either add to the string, print the current token and
                    //reset to DEFAULT or throw an error on an unrecognized symbol

                    //if the first char is a '(', add to tokenList and reset
                    if(currentTokenString.toString().equals("(")){
                        tokenList.add(new Token(keywords.get(currentTokenString.toString()), ""));
                        printToken();
                        currentTokenString = new StringBuilder();
                        currentState = Token.tokenType.DEFAULT;
                        i--;
                        break;
                    }
                    //if the currentChar is a letter or a number and not the first character, add it to the currentTokenString
                    if ((!currentTokenString.isEmpty() && Character.isDigit(currentChar)) || Character.isLetter(currentChar))
                        currentTokenString.append(currentChar);
                    else if (currentChar == ' ') {
                        //if the accumulated token is a keyword, output just the tokenType, else make a custom identifier token
                        if(keywords.containsKey(currentTokenString.toString()))
                            tokenList.add(new Token(keywords.get(currentTokenString.toString()), ""));
                        else
                            tokenList.add(new Token(Token.tokenType.IDENTIFIER, currentTokenString.toString()));
                        printToken();
                        currentState = Token.tokenType.DEFAULT;
                        currentTokenString = new StringBuilder();
                        //check for these symbols with no space in between the current token and itself
                    }else if(currentChar == ',' || currentChar == '(' || currentChar == ')' ||
                            currentChar == ';' || currentChar == '/' || currentChar == '[' || currentChar == ']'){
                        if(keywords.containsKey(currentTokenString.toString())){
                            tokenList.add(new Token(keywords.get(currentTokenString.toString()), ""));
                            printToken();
                        }
                        else if(!currentTokenString.isEmpty()){
                            tokenList.add(new Token(Token.tokenType.IDENTIFIER, currentTokenString.toString()));
                            printToken();
                        }
                        tokenList.add(new Token(keywords.get(Character.toString(currentChar)), ""));
                        printToken();
                        currentState = Token.tokenType.DEFAULT;
                        currentTokenString = new StringBuilder();
                    }else {
                        throw new SyntaxErrorException("Invalid character in a word: '" + currentChar + "'. Error on line " + lineNumber + '.');
                    }
                    break;
                case NUMBER:
                    //NUMBER will either add to the string, print the current token and reset to
                    //DEFAULT or throw an error on an unrecognized symbol
                    if (currentChar == ' ') {
                        tokenList.add(new Token(Token.tokenType.NUMBER, currentTokenString.toString()));
                        printToken();
                        currentState = Token.tokenType.DEFAULT;
                        currentTokenString = new StringBuilder();
                    }
                    // '.' is a valid char for a NUMBER
                    else if (Character.isDigit(currentChar) || currentChar == '.')
                        currentTokenString.append(currentChar);
                        //testing if there is a letter in the current NUMBER token
                   else if(currentChar == '*' || currentChar == '/' || currentChar == '+' || currentChar == '-' || currentChar == '%'){
                       tokenList.add(new Token(Token.tokenType.NUMBER, currentTokenString.toString()));
                       printToken();
                       currentTokenString = new StringBuilder();
                       currentState = Token.tokenType.DEFAULT;
                       i--;
                    } else if(currentChar == '(' || currentChar == ')' || currentChar == '[' || currentChar == ']'){
                       tokenList.add(new Token(Token.tokenType.NUMBER, currentTokenString.toString()));
                        printToken();
                        currentTokenString = new StringBuilder();
                        currentState = Token.tokenType.SYMBOL;
                        i--;
                    }else {
                        throw new SyntaxErrorException(currentTokenString + Character.toString(currentChar) + " cannot contain "
                                + "'" + currentChar + "'" + " as a number. Error on line " + lineNumber + '.');
                    }
                    break;
                case SYMBOL:
                    //parenthesis are after a space are handled in the SYMBOL state whereas
                    //parenthesis directly after an IDENTIFIER are handled in the UNDETERMINED state
                    if(currentTokenString.toString().equals("(") || currentTokenString.toString().equals(")")){
                        tokenList.add(new Token(keywords.get(currentTokenString.toString()), ""));
                    //check for the current + next characters and just the current, basically what the peek function would do if implemented
                    }else if(keywords.containsKey(currentTokenString.toString() + currentChar) && !currentTokenString.isEmpty()){
                        tokenList.add(new Token(keywords.get(currentTokenString.toString() + currentChar),
                                currentTokenString.toString() + currentChar));
                        i++;
                    }else if(keywords.containsKey(currentTokenString.toString())){
                        tokenList.add(new Token(keywords.get(currentTokenString.toString()),
                                currentTokenString.toString()));
                        if(Character.isDigit(currentChar))
                            i--;
                    }else if(keywords.containsKey(Character.toString(currentChar))){
                        tokenList.add(new Token(keywords.get(Character.toString(currentChar)), ""));
                        printToken();
                        currentState = Token.tokenType.DEFAULT;
                        break;
                    }else {
                        throw new SyntaxErrorException("'" + currentChar + "'" + " is not a valid symbol in Shank. Error on line " + lineNumber + ".");
                    }
                    printToken();
                    currentTokenString = new StringBuilder();
                    currentState = Token.tokenType.DEFAULT;
                    if(nextLine.charAt(i) == '(' || nextLine.charAt(i) == ')')
                        i--;
                    break;
                case INDENT:
                    int differenceInIndentations = 0;
                    //check for spaces first and reset the counter if less than 4 consecutive spaces are found
                    if(currentChar == ' '){
                        numberOfSpaces++;
                    }
                    if(numberOfSpaces == 4) {
                        numberOfIndentsInLine++;
                        numberOfSpaces = 0;
                    }
                    //accumulate char until there are no tabs, then compare to the previous line
                    if(currentChar == '\t')
                        numberOfIndentsInLine++;
                    if(nextLine.charAt(i + 1) != ' ' && nextLine.charAt(i + 1) != '\t') {
                        differenceInIndentations = numberOfIndentsInLine - numberOfIndentsPreviousLine;
                        if (differenceInIndentations < 0) {
                            for (int n = differenceInIndentations; n != 0; n++) {
                                tokenList.add(new Token(Token.tokenType.DEDENT, ""));
                                printToken();
                            }
                        }
                        if (differenceInIndentations > 0) {
                            for (int n = differenceInIndentations; n != 0; n--) {
                                tokenList.add(new Token(Token.tokenType.INDENT, ""));
                                printToken();
                            }
                        }
                        numberOfIndentsPreviousLine = numberOfIndentsInLine;
                        currentState = Token.tokenType.DEFAULT;
                        currentTokenString = new StringBuilder();
                        break;
                    }
                    break;
                case STRINGLITERAL:
                    //delete the first " from currentTokenString to avoid it from being added with the token
                    if(currentTokenString.toString().equals("\""))
                        currentTokenString = new StringBuilder();
                    if(currentChar == '\"'){
                        tokenList.add(new Token(Token.tokenType.STRINGLITERAL, currentTokenString.toString()));
                        printToken();
                        currentState = Token.tokenType.DEFAULT;
                        currentTokenString = new StringBuilder();
                    }else{
                        currentTokenString.append(currentChar);
                    }
                    break;
                case CHARACTERLITERAL:
                    if(currentTokenString.toString().equals("'"))
                        currentTokenString = new StringBuilder();
                    if(currentChar == '\''){
                        tokenList.add(new Token(Token.tokenType.CHARACTERLITERAL, currentTokenString.toString()));
                        printToken();
                        currentState = Token.tokenType.DEFAULT;
                        currentTokenString = new StringBuilder();
                        break;
                    }else{
                        currentTokenString.append(currentChar);
                    }
                    break;
                case COMMENT:
                    //only change the state if the end of the comment is found
                    if(currentChar == '}')
                        currentState = Token.tokenType.DEFAULT;
                    break;
            }
        }
        //if the state is still a literal, the literal was never ended so throw an exception
        if(currentState == Token.tokenType.STRINGLITERAL || currentState == Token.tokenType.CHARACTERLITERAL){
            throw new SyntaxErrorException("Unterminated STRING or CHARACTER LITERAL in '" + currentTokenString + "'. Error on line " + lineNumber + '.');
        }
        //only adding tokens to the list if the string is not empty
        if(currentState == Token.tokenType.SYMBOL && !keywords.containsKey(currentTokenString.toString())) {
            throw new SyntaxErrorException("'" + currentTokenString + "'" + " is not a valid symbol in Shank. Error on line " + lineNumber + ".");
        }
        //if the current state is COMMENT, don't add anything to the tokenList
        if(keywords.containsKey(currentTokenString.toString()) && currentState != Token.tokenType.COMMENT){
            tokenList.add(new Token(keywords.get(currentTokenString.toString()), ""));
            printToken();
        }else if(currentState == Token.tokenType.UNDETERMINED){
            tokenList.add(new Token(Token.tokenType.IDENTIFIER, currentTokenString.toString()));
            printToken();
        }else if (!currentTokenString.isEmpty() && currentState != Token.tokenType.COMMENT) {
            tokenList.add(new Token(currentState, currentTokenString.toString()));
            printToken();
        }
        //adding ENDOFLINE after the full string has been processed
        tokenList.add(new Token(Token.tokenType.ENDOFLINE, ""));
        printToken();
        //only switch the state back to DEFAULT if not in a comment
        if(currentState != Token.tokenType.COMMENT)
            currentState = Token.tokenType.DEFAULT;

    }
    private void populateKeywords(){
        keywords.put("while", Token.tokenType.WHILE);
        //keywords.put("write", Token.tokenType.WRITE);
        keywords.put("for", Token.tokenType.FOR);
        keywords.put("from", Token.tokenType.FROM);
        keywords.put("to", Token.tokenType.TO);
        keywords.put("integer", Token.tokenType.INTEGER);
        keywords.put("boolean", Token.tokenType.BOOLEAN);
        keywords.put("true", Token.tokenType.BOOLEAN);
        keywords.put("false", Token.tokenType.BOOLEAN);
        keywords.put("real", Token.tokenType.REAL);
        keywords.put("character", Token.tokenType.CHARACTER);
        keywords.put("string", Token.tokenType.STRING);
        keywords.put("array", Token.tokenType.ARRAY);
        keywords.put("of", Token.tokenType.OF);
        keywords.put("variables", Token.tokenType.VARIABLES);
        keywords.put("constants", Token.tokenType.CONSTANTS);
        keywords.put("var", Token.tokenType.VAR);
        keywords.put("define", Token.tokenType.DEFINE);
        keywords.put("if", Token.tokenType.IF);
        keywords.put("elseif", Token.tokenType.ELSEIF);
        keywords.put("else", Token.tokenType.ELSE);
        keywords.put("mod", Token.tokenType.MOD);
        keywords.put("repeat", Token.tokenType.REPEAT);
        keywords.put("until", Token.tokenType.UNTIL);
        //keywords.put("read", Token.tokenType.READ);
        //keywords.put("Left", Token.tokenType.LEFT);
        //keywords.put("Right", Token.tokenType.RIGHT);
        //keywords.put("Substring", Token.tokenType.SUBSTRING);
        //keywords.put("SquareRoot", Token.tokenType.SQUAREROOT);
        //keywords.put("GetRandom", Token.tokenType.GETRANDOM);
        //keywords.put("IntegerToReal", Token.tokenType.INTEGERTOREAL);
        //keywords.put("RealToInteger", Token.tokenType.REALTOINTEGER);
        //keywords.put("Start", Token.tokenType.START);
        //keywords.put("End", Token.tokenType.END);
        //keywords.put("args", Token.tokenType.ARGS);
        keywords.put("+", Token.tokenType.PLUS);
        keywords.put("-", Token.tokenType.MINUS);
        keywords.put("*", Token.tokenType.MULTIPLY);
        keywords.put("/", Token.tokenType.DIVIDE);
        keywords.put(">", Token.tokenType.GREATERTHAN);
        keywords.put("<", Token.tokenType.LESSTHAN);
        keywords.put("=", Token.tokenType.EQUALS);
        keywords.put(">=", Token.tokenType.GREATEROREQUAL);
        keywords.put("<=", Token.tokenType.LESSOREQUAL);
        keywords.put(":=", Token.tokenType.ASSIGNMENT);
        keywords.put("<>", Token.tokenType.NOTEQUAL);
        keywords.put(",", Token.tokenType.COMMA);
        keywords.put(":", Token.tokenType.COLON);
        keywords.put("\t", Token.tokenType.INDENT);
        keywords.put("(", Token.tokenType.LPAREN);
        keywords.put(")", Token.tokenType.RPAREN);
        keywords.put(";", Token.tokenType.SEMICOLON);
        keywords.put("{", Token.tokenType.COMMENT);
        keywords.put("then", Token.tokenType.THEN);
        keywords.put("]", Token.tokenType.RBRACKET);
        keywords.put("[", Token.tokenType.LBRACKET);
    }
    public List<Token> getTokenList(){
        return tokenList;
    }

    public void printToken(){
        //print the last token in tokenList (which should be the most recently added)
        System.out.print(tokenList.get(tokenList.size() - 1).toString());
    }
}