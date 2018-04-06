/**
 * Thrown if an unsupported character <code>tegn</code> has been input.
 */
public class IllegalCharacterException extends Exception {
    public IllegalCharacterException(char tegn) {
        super(String.format("%s is an illegal charecter.", tegn));
    }
}
