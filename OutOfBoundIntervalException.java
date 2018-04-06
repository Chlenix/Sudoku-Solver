/**
 * Thrown if the <code>value</code> is outside the bounds defined by <code>gridSize</code>
 */
public class OutOfBoundIntervalException extends Exception {
    public OutOfBoundIntervalException(int value, int gridSize) {
        super(String.format("The value [%d] is out of scope. Allowed scope is between 1 and %d", value, gridSize));
    }
}
