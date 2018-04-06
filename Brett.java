/**
 * A Sudoku board <code>Brett</code>.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Brett {
    private Box[][] boxes;
    private Row[] rows;
    private Column[] cols;
    private Rute[][] ruter;

    private int rowsInBox;
    private int colsInBox;
    private int gridSize;

    private SudokuBeholder solutions;

    public int getSize() {
        return this.gridSize;
    }

    public Brett() {
        solutions = new SudokuBeholder();
    }

    /**
     Reads the board values from a file
     @param filename The file to read and further create the board from.
     @throws InconsistentFormatException Throws a <code>InconsistentFormatException</code> if the row length does not
     correspond to the <code>gridSize</code>, a product of <code>rowsInBox</code> and <code>colsInBox</code>
     */
    public void readFile(String filename) throws InconsistentFormatException {
        File fileToRead = new File(filename);

        try (Scanner reader = new Scanner(new FileReader(fileToRead))) {

            rowsInBox = FileParser.tegnTilVerdi(reader.nextLine().charAt(0));
            colsInBox = FileParser.tegnTilVerdi(reader.nextLine().charAt(0));

            gridSize = rowsInBox * colsInBox;
            int maxTiles = (int) Math.pow(gridSize, 2);

            // create an empty board
            opprettDatastruktur();

            int tileCounter = 0;
            int lineCounter = 0;

            Rute prev = null;

            while (reader.hasNextLine()) {
                String rowLine = reader.nextLine();
                int rowLength = rowLine.length();

                // radene i brettet (fil-linjene) har samme lengde
                if (rowLength != gridSize) {
                    throw new InconsistentFormatException();
                }

                for (int i = 0; i < rowLength; i++) {
                    int ruteVerdi = FileParser.tegnTilVerdi(rowLine.charAt(i));

                    if (ruteVerdi < 0 || ruteVerdi > gridSize) {
                        throw new OutOfBoundIntervalException(ruteVerdi, gridSize);
                    }

                    // Calculate the x and y coordinates of a box
                    int y = tileCounter / gridSize;
                    int x = tileCounter % gridSize;

                    int inner_y = y % rowsInBox;
                    int inner_x = x % colsInBox;

                    int box_y = y / rowsInBox;
                    int box_x = x / colsInBox;

                    // Translate the coordinates to a relative index
                    int index = inner_y * colsInBox + inner_x;

                    Row row = this.ruter[lineCounter][i].getRow();
                    Column col = this.ruter[lineCounter][i].getCol();
                    Box box = this.boxes[box_x][box_y];

                    Rute next = this.ruter[lineCounter][i].getNext();

                    this.ruter[lineCounter][i] = new Rute(ruteVerdi, box, row, col, this, next);
                    if (ruteVerdi != 0) {
                        this.ruter[lineCounter][i].isPrefilled = true;
                    }
                    row.insert(this.ruter[lineCounter][i], i);
                    col.insert(this.ruter[lineCounter][i], lineCounter);
                    box.insert(this.ruter[lineCounter][i], index);
                    tileCounter++;

                    if (prev != null) {
                        prev.setNext(this.ruter[lineCounter][i]);
                    }
                    prev = this.ruter[lineCounter][i];
                }
                lineCounter++;
            }

            if (tileCounter != maxTiles) {
                throw new TileMismatchException();
            }

        } catch (FileNotFoundException | IllegalCharacterException |
                NullPointerException | OutOfBoundIntervalException |
                TileMismatchException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     Creates the base data structure for the sudoku board
    */
    private void opprettDatastruktur() {
        // set initial size
        this.rows = new Row[gridSize];
        this.cols = new Column[gridSize];
        this.boxes = new Box[rowsInBox][colsInBox];
        this.ruter = new Rute[gridSize][gridSize];

        // Instantiate empty columns and rows
        for (int i = 0; i < gridSize; i++) {
            this.cols[i] = new Column(gridSize);
            this.rows[i] = new Row(gridSize);
        }

        // Instantiate empty boxes
        for (int i = 0; i < rowsInBox; i++) {
            for (int j = 0; j < colsInBox; j++) {
                boxes[i][j] = new Box(gridSize);
            }
        }

        // Fill the empty object pointers with temporare references
        int charCounter = 0;
        for (int yCoordinate = 0; yCoordinate < gridSize; yCoordinate++) {
            for (int xCoordinate = 0; xCoordinate < gridSize; xCoordinate++) {

                int y = charCounter / gridSize;
                int x = charCounter % gridSize;

                int boxWidth = x / colsInBox;
                int boxHeight = y / rowsInBox;

                Box b = boxes[boxWidth][boxHeight];
                Column c = cols[yCoordinate];
                Row r = rows[xCoordinate];
                Rute tmpNext;

                if (xCoordinate < gridSize && yCoordinate < gridSize)
                    tmpNext = null;
                else if (xCoordinate < gridSize)
                    tmpNext = ruter[yCoordinate+1][0];
                else
                    tmpNext = ruter[yCoordinate][xCoordinate+1];

                ruter[xCoordinate][yCoordinate] = new Rute(0, b, r, c, this, tmpNext);

                charCounter++;
            }
        }
    }

    /**
     * Prints out the board in a tabular format.
     */
    public void prettyPrintTable() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < ruter.length; i++) {
            for (int j = 0; j < ruter[i].length; j++) {
                int output = ruter[i][j].getValue();
                if (output == 0) {
                    sb.append(" ");
                } else {
                    sb.append(output);
                }
                if ((j + 1) % colsInBox == 0 && j < ruter[i].length - 1) {
                    sb.append("|");
                }
            }
            sb.append("\r\n");
            if ((i + 1) % rowsInBox == 0 && i < ruter.length - 1) {
                for (int k = 0; k < rowsInBox; k++) {
                    sb.append("---+");
                }
                sb.deleteCharAt(sb.length() - 1).append("\r\n");
            }
        }
        System.out.println();
        System.out.println(sb);
    }

    /**
     * Prints out the solutions according to 10b but not 8b
     */
    public void uglyPrintFor10b() {
        // Som jeg sa, jeg var usikker, men lagde denne for sikkerhetskyld.
        // For aa se metoden i gang, erstatt sb.prettyPrintSolutions() i Oblig10 med denne
        StringBuilder sb;

        for (int[][] solution : solutions) {
            sb = new StringBuilder();
            sb.append(rowsInBox).append("\r\n");
            sb.append(colsInBox).append("\r\n");
            for (int i = 0; i < solution.length; i++) {
                for (int j = 0; j < solution[i].length; j++) {
                    int output = solution[i][j];
                    if (output == 0) {
                        sb.append(".");
                    } else {
                        sb.append(output);
                    }
                }
                if (i != solution.length - 1) {
                    sb.append("\r\n");
                }
            }
            System.out.println(sb);
            System.out.println();
        }
    }

    /**
     * Prints out the solution(s) found in a tabular format,
     * or alt. form if the solution count is greater than 1
     */
    public void prettyPrintSolutions() {

        // if more than one solution
        if (solutions.getSolutionCount() > 1) {
            prettyPrintLinearSolutions();
            return;
        }

        StringBuilder sb;

        for (int[][] solution : solutions) {
            sb = new StringBuilder();
            for (int i = 0; i < solution.length; i++) {
                for (int j = 0; j < solution[i].length; j++) {
                    int output = solution[i][j];
                    if (output == 0) {
                        sb.append(" ");
                    } else {
                        sb.append(output);
                    }
                    if ((j + 1) % colsInBox == 0 && j < solution[i].length - 1) {
                        sb.append("|");
                    }
                }
                sb.append("\r\n");
                if ((i + 1) % rowsInBox == 0 && i < solution.length - 1) {
                    for (int k = 0; k < rowsInBox; k++) {
                        sb.append("---+");
                    }
                    sb.deleteCharAt(sb.length() - 1).append("\r\n");
                }
            }
            System.out.println();
            System.out.println(sb);
        }
    }

    /**
     * Prints the solutions in the alternative form
     */
    private void prettyPrintLinearSolutions() {
        StringBuilder sb;
        int s = 0;

        for (int[][] solution : solutions) {
            sb = new StringBuilder();
            sb.append(++s).append(": ");
            for (int i = 0; i < solution.length; i++) {
                for (int j = 0; j < solution[i].length; j++) {
                    char charVal = ' ';
                    try {
                        charVal = FileParser.verdiTilTegn(solution[i][j], ' ');
                    } catch (IllegalValueException e) {
                        e.printStackTrace();
                    }
                    sb.append(charVal);
                }
                sb.append("//");
            }
            System.out.println(sb);
        }
    }

    /**
     * Solves the board by calling <code>fyllUtDenneRuteOgResten()</code> on the first square.
     * @see Rute::fyllUtDenneRuteOgResten()
     */
    public void solveBrett() {
        this.ruter[0][0].fyllUtDenneRuteOgResten();
    }

    /**
     Stores the board to the <code>SudokuBeholder</code>
     @see SudokuBeholder
     */
    public void saveSolution() {
        solutions.insert(this.ruter);
    }

    public void writeToFile(String filename) throws IOException {
        int solutionCount = this.solutions.getSolutionCount();
        if (solutionCount > 1) {
            solutions.writeToFile(filename);
        } else if (solutionCount == 1) {
            solutions.writeToFile(filename, rowsInBox, colsInBox);
        } else {
            System.out.println("No solutions were found.");
        }
    }
}