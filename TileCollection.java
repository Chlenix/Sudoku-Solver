/**
 * An abstract collection of tiles {@link Rute} stored in array.
 */

public abstract class TileCollection {
    private Rute[] ruter;

    public TileCollection(int size) {
        ruter = new Rute[size];
    }

    public Rute[] getRuter() {
        return ruter;
    }

    /**
     * Checks if a value already exists in the collection. Used when performing
     * multiple checks in order to find solutions to the board.
     * @param value number to check against.
     * @return <code>true</code> if the number exists in the collection. <code>false</code> otherwise.
     *
     * Inheritance examples
     * @see Row
     * @see Column
     * @see Box
     */
    public boolean contains(int value) {
        if (value == 0) {
            return false;
        }
        for (Rute rute : ruter) {
            if (rute.getValue() == value) {
                return true;
            }
        }
        return false;
    }

    public void insert(Rute rute, int index) {
        this.ruter[index] = rute;
    }
}
