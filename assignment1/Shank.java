package assignment1;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static java.lang.System.exit;

public class Shank {
    public static void main(String[] args) throws Exception {
        if(args.length > 1){
            System.out.println("Too many command line arguments. Only enter 1 argument. ");
            exit(1);
        }
        if(args.length < 1){
            System.out.println("Too few command line argument. Enter 1 argument. ");
            exit(2);
        }
        Path myPath = Paths.get(args[0]);
        List<String> lines = Files.readAllLines(myPath, StandardCharsets.UTF_8);
        Lexer shankLexer = new Lexer();
        int i = 0;
        while (i < lines.size()) {
            try {
                shankLexer.lex(lines.get(i));
                i++;
            }catch (Exception ex){
                exit(4);
            }
        }
        shankLexer.lex("");
        //give the list then parse and print the tree
        List<Token> tempList = shankLexer.getTokenList();
        Parser p = new Parser(tempList);
        ProgramNode program = p.parse();
        SemanticAnalysis sam = new SemanticAnalysis(program);
        Interpreter interpreter = new Interpreter(program);
        interpreter.interpret();



    }
}