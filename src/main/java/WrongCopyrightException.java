/**
 * Class representing wrong copyright exception, extending the exception class
 */

public class WrongCopyrightException extends Exception
{
    /**
     * a long constant representing the serial version UID
     */
    private static final long serialVersionUID = -3352808845495117276L;

    /**
     * Constructor for WrongCopyrightException class
     */
    public WrongCopyrightException()
    {
        super("Invalid copyright detected");
    }

    /**
     * Constructor for a copyright exception with additional context.
     *
     * @param message the detailed error message
     */
    public WrongCopyrightException(String message)
    {
        super(message);
    }
}
