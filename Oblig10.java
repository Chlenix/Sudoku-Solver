import java.io.IOException;

public class Oblig10 {

    // Var litt usikker paa om oppgaven ba om ut-filen eller in-filen under 10b "Utskrift".
    // Lagde derfor baade filutskrift (standard og alternativ form) og skjermutskrift (begge former ogsaa)
    // Standard form hvis antall loesninger == 1. Alt form hvis ant. loesninger > 1

    public static void main(String args[]) {
        Brett sb = new Brett();

        String inFile = null;
        String outFile = null;

        if (args.length >= 1) {
            inFile = args[0];
        }

        if (inFile == null) {
            System.out.println("Not enough arguments. Requires in-file and (optionally) out-file. E.g. Oblig10 in.txt OR Oblig10 in.txt out.txt");
            System.exit(0);
        }

        try {
            sb.readFile(inFile);            // read the file
            sb.prettyPrintTable();          // print the table
            sb.solveBrett();                // solveBrett the board
            sb.prettyPrintSolutions();      // print the solution(s)
            if (args.length > 1) {
                outFile = args[1];
                sb.writeToFile(outFile);    // write the solutions to the file specified in the arguments.
            }
        } catch (InconsistentFormatException | IOException e) {
            e.printStackTrace();
        }
    }
}
