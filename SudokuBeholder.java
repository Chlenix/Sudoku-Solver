import java.util.*;
import java.io.*;

/**
 A collection used to store Sudoku solutions extending <code>LinkedList</code>
 @see LinkedList
 */
public class SudokuBeholder extends LinkedList<int[][]>
{
    private final int MAX_SOLUTIONS;    // Max number of solutions available for storage
    private int solutions;				// Sheer amount of solutions found

    // Constructor
    public SudokuBeholder() {
        MAX_SOLUTIONS = 3500;
        solutions = 0;
    }

    /**
     * Gets the solution
     * @return <code>int[][]</code> solution
     */
    public int[][] get() {
        return remove();
    }

    /**
     Inserts a solution <code>Rute[][]</code> into the collection.
     If exceeds <code>MAX_SOLUTIONS</code>, only increment.
     @param rute The "board" tile-representation.
     */
    public void insert(Rute[][] rute) {
        if (size() <= MAX_SOLUTIONS) {
            int[][] tmp = new int[rute.length][rute[0].length];
            for (int i = 0; i < rute.length; i++) {
                for (int j = 0; j < rute[0].length; j++) {
                    tmp[i][j] = rute[i][j].getValue();
                }

            }
            add(tmp);
        }
        solutions++;
    }

    /**
     Get accessor for the solution amount
     @return <code>int</code> number of solutions
     */
    public int getSolutionCount() {
        return solutions;
    }

    /**
     Stores all solutions to the file <code>outFile</code> in standard format.
     @param outFile The out file to write to.
     @param rowsInBox amount of rows in a box.
     @param colsInBox amount of columns in a box.
     @throws IOException an error has occured during writing to the file.
     */
    public void writeToFile(String outFile, int rowsInBox, int colsInBox) throws IOException {
        FileWriter fw = null;
        BufferedWriter writer = null;

        Iterator<int[][]> it = iterator();
        int[][] currentSolution;

        try {
            fw = new FileWriter(outFile);
            writer = new BufferedWriter(fw);

            // rowsInBox
            writer.write(Integer.toString(rowsInBox));
            writer.newLine();

            // colsInBox
            writer.write(Integer.toString(colsInBox));
            writer.newLine();

            while (it.hasNext()) {
                currentSolution = it.next();

                int i = 0;
                for (int[] row : currentSolution) {
                    for (int tileValue : row) {
                        char charVal = ' ';
                        try {
                            charVal = FileParser.verdiTilTegn(tileValue, '.');
                        } catch (IllegalValueException e) {
                            e.printStackTrace();
                        }
                        writer.write(charVal);
                    }
                    if (++i < row.length) {
                        writer.newLine();
                    }
                }
            }
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (fw != null) {
                fw.close();
            }
        }
    }

    /**
     Stores all solutions to the file <code>outFile</code> in an alternative format
     @param outFile The out file to write to.
     @throws IOException an error has occured during writing to the file.
     */
    public void writeToFile(String outFile) throws IOException {
        FileWriter fw = null;
        BufferedWriter writer = null;

        try {
            fw = new FileWriter(outFile);
            writer = new BufferedWriter(fw);

            Iterator<int[][]> it = iterator();
            int[][] currentSolution;

            int i = 0;
            while (it.hasNext()) {
                i++;
                currentSolution = it.next();
                writer.write(i + ": ");

                for (int[] row : currentSolution) {
                    for (int tileValue : row) {
                        char charVal = ' ';
                        try {
                            charVal = FileParser.verdiTilTegn(tileValue, '.');
                        } catch (IllegalValueException e) {
                            e.printStackTrace();
                        }
                        writer.write(charVal);
                    }
                    writer.write("//");
                }
                if (it.hasNext()) {
                    writer.newLine();
                }
            }
        } finally {
            if (writer != null) {
                writer.close();
            }

            if (fw != null) {
                fw.close();
            }
        }
    }
}