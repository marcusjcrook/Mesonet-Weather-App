/**
 * This class represents individual, real-valued samples. This class
 * explicitly addresses the fact that some samples are bad.
 */
public class Measurement
{

    /** The observed value. */
    // use NaN for missing values
    protected double value = Double.NaN;

    /** Indicates whether the observation is a valid one */
    protected boolean valid = false;

    /**
     * 
     */
    protected Measurement()
    {
        valid = false;
        value = Double.NaN;
    }

    /**
     * Sample constructor.
     * <P>
     * Values that are larger than -900 are assumed to be valid
     * 
     * @param value
     *            The value to be stored
     */
    public Measurement(double value)
    {

        if (value > -900.0)
        {
            this.value = value;
            valid = true;
        }
        else
        {
            this.value = value;
            valid = false;
        }
    }

    /**
     * Returns the observed value
     * 
     * @return The observed value
     */
    public double getValue()
    {
        return value;
    }

    /**
     * Indicates whether the observation is valid
     * 
     * @return True if valid; false otherwise
     */
    public boolean isValid()
    {
        return valid;
    }

    /**
     * Describes the observation.
     * 
     * @return String that describes the observation: either the value or "bad";
     */
    public String toString()
    {
        if (valid)
        {
            return String.format("%.04f", value);
        }
        else
        {
            return "bad";
        }
    }
}