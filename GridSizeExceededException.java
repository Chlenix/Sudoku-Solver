/**
 * @deprecated due to the courtesy of UiO by providing the conversion
 * functions for input values. Replaced by {@link IllegalCharacterException}
 *
 * <code>IllegalCharacterException</code> exception will be fired if the input
 * value is not within the correct bounds.
 *
 * {@see FileParser#tegnTilVerdi(char tegn)}
 */
@Deprecated public class GridSizeExceededException extends Exception {
    public GridSizeExceededException(int value) {
        super(String.format("The grid size of [%d] is not supported.", value));
    }
}
