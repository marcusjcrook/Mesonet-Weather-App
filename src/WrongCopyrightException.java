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
}
