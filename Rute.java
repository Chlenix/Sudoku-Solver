/**
 * Structure for a tile <code>Rute</code> in Sudoku.
 */

import java.util.ArrayList;

public class Rute {
    private int value;

    private Box box;
    private Row row;
    private Column col;
    private Rute next;
    private Brett brett;

    public boolean isPrefilled;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    // Constructor
    public Rute(int value, Box box, Row row, Column col, Brett brett, Rute next) {
        this.isPrefilled = false;
        this.value = value;
        this.box = box;
        this.row = row;
        this.col = col;
        this.brett = brett;
        this.next = next;
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }

    public Row getRow() {
        return row;
    }

    public Column getCol() {
        return col;
    }

    public Rute getNext() {
        return next;
    }

    public void setNext(Rute s) {
        next = s;
    }

    public Box getBox() {
        return box;
    }

    /**
     * Attempts to find all the possible solutions for a given tile pointing to <code>this</code> object.
     * @param size highest number allowed
     * @return An array of <code>int</code> containing the possibile solutions.
     */
    public int[] finnAlleMuligeTall(int size) {
        int[] solutions = null;
        ArrayList<Integer> tempSolutions = new ArrayList<>();
        if (!this.isPrefilled) {
            for (int i = 1; i <= size; i++) {
                if (!this.row.contains(i) && !this.col.contains(i) && !this.box.contains(i)) {
                    tempSolutions.add(i);
                }
            }
            solutions = new int[tempSolutions.size()];
            for (int i = 0; i < solutions.length; i++) {
                solutions[i] = tempSolutions.get(i);
            }
        }
        return solutions;
    }

    /**
     * Fills out the non-prefilled "rute" <code>Tile</code> with all the possible numbers.
     * For each possible number calls itself in the next <code>Tile</code>
     * Saves the solution(s) in the <code>SudokuBeholder</code> upon reaching a <code>null</code>-pointer
     * Repeats until all possible numbers are exhausted in all tiles.
     * @see SudokuBeholder
     */
    public void fyllUtDenneRuteOgResten() {
        // Check if the tile is prefilled.
        if (this.isPrefilled) {
            // We've reached the end.
            if (next == null) {
                brett.saveSolution(); // save solution
                return;
            }
            next.fyllUtDenneRuteOgResten();
            return;
        }

        // get all possible numbers for this Tile
        int[] allPossible = finnAlleMuligeTall(this.brett.getSize());

        // for each number:
        for (int number : allPossible) {
            // set the value to the number
            setValue(number);
            // and if the next Tile exists
            if (next != null) {
                // call the method again in the next Tile
                next.fyllUtDenneRuteOgResten();
            } else {
                // otherwise (next is a null-pointer), we've reached the end
                brett.saveSolution(); // save the solution
            }
            // reset the value before returning to the previous Tile
            setValue(0);
        }
    }
}
