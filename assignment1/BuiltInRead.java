package assignment1;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BuiltInRead extends FunctionNode{
    public BuiltInRead(){
        builtIn = true;
        vardic = true;
        name = "Read";
    }
    public void execute(ArrayList<InterpreterDataType> in) throws SyntaxErrorException {
        Scanner scnr = new Scanner(System.in);
        int paramCount = 0;
        for (InterpreterDataType interpreterDataType : in) {
            //check for var parameter
            if(!in.get(paramCount).changeable)
                throw new SyntaxErrorException("Read function must be passed all var parameters.");
            //case for IntegerDataType, handles invalid input
            if (in.get(0) instanceof IntegerDataType) {
                try {
                    int tempInt = scnr.nextInt();
                    interpreterDataType.FromString(String.valueOf(tempInt));
                } catch (InputMismatchException ignored) {
                    throw new SyntaxErrorException("Variable of type integer cannot accept " + scnr.next() + " as an assignment.");
                }
                //case for RealDataType, handles invalid input
            } else if (interpreterDataType instanceof RealDataType) {
                try {
                    double tempDouble = scnr.nextDouble();
                    interpreterDataType.FromString(String.valueOf(tempDouble));
                } catch (InputMismatchException ignored) {
                    throw new SyntaxErrorException("Variable of type double cannot accept " + scnr.next() + " as an assignment.");
                }
                //case for CharacterDataType, handles invalid input
            } else if (interpreterDataType instanceof CharacterDataType) {
                String tempString = scnr.next();
                if (tempString.length() > 1)
                    throw new SyntaxErrorException("Variable of type character cannot accept more then one letter as an assignment");
                interpreterDataType.FromString(scnr.next());
                //case for BooleanDataType, handles invalid input
            } else if (interpreterDataType instanceof BooleanDataType) {
                try {
                    boolean b = scnr.nextBoolean();
                    interpreterDataType.FromString(String.valueOf(b));
                } catch (InputMismatchException ignored) {
                    throw new SyntaxErrorException("Variable of type boolean cannot accept " + scnr.next() + " as an assignment.");
                }
                //send everything else to string
            } else {
                interpreterDataType.FromString(scnr.next());
            }
            //incrementing paramCount for the next parameter
            paramCount++;
        }
    }
}
