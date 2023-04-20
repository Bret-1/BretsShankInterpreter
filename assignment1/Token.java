package assignment1;

import java.util.Objects;

public class Token {

    public enum tokenType{DEFAULT, UNDETERMINED, IDENTIFIER,SYMBOL, NUMBER, ENDOFLINE,
        WHILE, WRITE, FOR, FROM, TO, INTEGER, BOOLEAN, REAL, CHARACTER,
        STRING, ARRAY, OF, VARIABLES, CONSTANTS, VAR, DEFINE,
        IF, ELSEIF, ELSE, MOD, REPEAT, UNTIL, READ, LEFT,
        RIGHT, SUBSTRING, SQUAREROOT, GETRANDOM, INTEGERTOREAL,
        REALTOINTEGER, START, END, INDENT, DEDENT, ARGS,
        PLUS, MINUS, MULTIPLY, DIVIDE, GREATERTHAN, LESSTHAN,
        EQUALS, GREATEROREQUAL, LESSOREQUAL, ASSIGNMENT, NOTEQUAL,
        COMMA, COLON, SEMICOLON, LPAREN, RPAREN, STRINGLITERAL, CHARACTERLITERAL,
        COMMENT, RBRACKET, LBRACKET, THEN};
    private tokenType token;
    private String value;

    public Token(){ }
    public Token(Token t){
        token = t.getToken();
        value = t.getValue();
    }

    public Token(tokenType tokenTypeIn, String valueIn){
        this.value = valueIn;
        this.token = tokenTypeIn;
    }

    public tokenType getToken(){
        return token;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString(){
        if(token == tokenType.ENDOFLINE)
            return token.name() + "\n";
        else if(!Objects.equals(value, ""))
            return token.name() + "(" + value + ") ";
        else
            return token.name() + ' ';
    }

}
