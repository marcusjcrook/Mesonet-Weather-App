import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
    @Before
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

        Assert.assertEquals("Incorrect value returned!", Double.NaN, noParameterMeasure.getValue(), 0.001);
        Assert.assertFalse("Incorrect boolean returned!", noParameterMeasure.isValid());
    }

    /**
     * Method to test constructor with one parameter.
     */
    @Test
    public void parameterConstructorTest()
    {
        Measurement parameterMeasure = new Measurement(goodValue);

        Assert.assertEquals("Incorrect value returned!", 10, parameterMeasure.getValue(), 0.001);
        Assert.assertTrue("Incorrect boolean returned!", parameterMeasure.isValid());
    }

    /**
     * Test good value set to true.
     */
    @Test
    public void testValid()
    {
        goodMeasurement = new Measurement(goodValue);
        boolean actual = goodMeasurement.isValid();

        Assert.assertTrue(actual);
    }

    /**
     * Test bad value set to false.
     */
    @Test
    public void testInvalid()
    {
        Measurement badMeasurement = new Measurement(badValue);
        boolean actual = badMeasurement.isValid();

        Assert.assertFalse("Incorrect boolean returned!", actual);
    }

    /**
     * Test get value method properly returns value.
     */
    @Test
    public void testGetValue()
    {
        goodMeasurement = new Measurement(goodValue);
        double actual = goodMeasurement.getValue();

        Assert.assertEquals("Incorrect value returned!", 10, actual, 0.01);
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

        Assert.assertEquals(expected, actual);

        Measurement badMeasurement = new Measurement(badValue);

        actual = badMeasurement.toString();
        expected = "bad";

        Assert.assertEquals("Incorrect string returned!", expected, actual);
    }
}
