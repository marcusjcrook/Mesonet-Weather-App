/**
 * Class for WrongTimeZoneException. Extends Exception class
 */
public class WrongTimeZoneException extends Exception
{
    /**
     * a constant holding the serial version UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * constructor for WrongTimeZoneException class
     */
    public WrongTimeZoneException()
    {
        // call the super class constructor
        super("Invalid time zone detected, should be UTC");
        // default implementation ignored
    }
}
