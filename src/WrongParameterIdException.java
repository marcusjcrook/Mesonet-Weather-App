/**
 * Class for WrongParameterId exception. Extends Exception class
 */
public class WrongParameterIdException extends Exception
{
    /** a constant holding the serial version UID */
    private static final long serialVersionUID = 7394973112258653626L;

    /**
     * WrongParameterIdException constructor
     * 
     * @param msg
     *            a Strong message to print
     */
    public WrongParameterIdException(String msg)
    {
        // invoke the super class
        super(msg + " Invalid parameterID detected");
    }
}
