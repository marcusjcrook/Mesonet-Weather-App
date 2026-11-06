import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for Measurement class
 */
public class MeasurementTest
{
    /**
     * a valid value.
     */
    private double goodValue;

    /**
     * an invalid value.
     */
    private double badValue;

    /**
     * a valid Measurement Object.
     */
    private Measurement goodMeasurement;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    public void setUp() throws Exception
    {
        goodValue = 10; // A valid value.
        badValue = -1000; // An invalid value.

    }

    /**
     * Method to test constructor with no parameter.
     */
    @Test
    public void noParameterConstructorTest()
    {
        Measurement noParameterMeasure = new Measurement();

        assertEquals(Double.NaN, noParameterMeasure.getValue(), 0.001, "Incorrect value returned!");
        assertFalse(noParameterMeasure.isValid(), "Incorrect boolean returned!");
    }

    /**
     * Method to test constructor with one parameter.
     */
    @Test
    public void parameterConstructorTest()
    {
        Measurement parameterMeasure = new Measurement(goodValue);

        assertEquals(10, parameterMeasure.getValue(), 0.001, "Incorrect value returned!");
        assertTrue(parameterMeasure.isValid(), "Incorrect boolean returned!");
    }

    /**
     * Test good value set to true.
     */
    @Test
    public void testValid()
    {
        goodMeasurement = new Measurement(goodValue);
        boolean actual = goodMeasurement.isValid();

        assertTrue(actual);
    }

    /**
     * Test bad value set to false.
     */
    @Test
    public void testInvalid()
    {
        Measurement badMeasurement = new Measurement(badValue);
        boolean actual = badMeasurement.isValid();

        assertFalse(actual, "Incorrect boolean returned!");
    }

    /**
     * Test get value method properly returns value.
     */
    @Test
    public void testGetValue()
    {
        goodMeasurement = new Measurement(goodValue);
        double actual = goodMeasurement.getValue();

        assertEquals(10, actual, 0.01, "Incorrect value returned!");
    }

    /**
     * Test toString method returns proper correct String.
     */
    @Test
    public void testToString()
    {
        goodMeasurement = new Measurement(goodValue);
        String actual = goodMeasurement.toString();
        String expected = "10.0000";

        assertEquals(expected, actual);

        Measurement badMeasurement = new Measurement(badValue);

        actual = badMeasurement.toString();
        expected = "bad";

        assertEquals(expected, actual, "Incorrect string returned!");
    }
}
