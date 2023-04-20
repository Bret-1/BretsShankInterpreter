package assignment1;

public class SyntaxErrorException extends  Exception{
    public SyntaxErrorException(String errorMessage){
        System.out.println(errorMessage);
        System.exit(4);
    }
}
