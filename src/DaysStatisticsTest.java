import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DaysStatisticsTest
{
    /** Array to hold the files */
    String[] files = new String[3];

    /** calendar to hold the time */
    GregorianCalendar testCalendar = new GregorianCalendar(0, 0, 0);

    /** a DateFormat object to format dates */
    DateFormat date;

    /** a DayStatistics object for test classes */
    DaysStatistics statTest;

    /**
     * Method to initialize data
     * 
     * @throws IOException
     * @throws WrongCopyrightException
     * @throws ParseException
     */
    @Before
    public void setUp() throws IOException, WrongCopyrightException, ParseException
    {
        date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z");
        date.setTimeZone(TimeZone.getTimeZone("UTC"));

        testCalendar.set(2018, 1, 2, 0, 1300, 0);
        testCalendar.setTimeZone(TimeZone.getTimeZone("UTC"));

        files[0] = "data/mesonet/20180102okcn.mts";
        files[1] = "data/mesonet/20180102stil.mts";
        files[2] = "data/mesonet/20180102okce.mts";

        statTest = new DaysStatistics(files);
        statTest.findStatistics();
    }

    /**
     * Method to test the getMaximum method
     * 
     * @throws WrongParameterIdException
     */
    @Test()
    public void dayStatisticsMaxTest() throws WrongParameterIdException
    {
        StatMeasurement actual = statTest.getMaximumDay("tair");
        StatMeasurement expected = new StatMeasurement(-5.3, testCalendar, "STIL", "tair", StatType.MAX);

        String actualDate = date.format(actual.getDateTimeOfMeasurment().getTime());
        String expectedDate = date.format(expected.getDateTimeOfMeasurment().getTime());

        Assert.assertEquals("Incorrect value!", expected.getValue(), actual.value, 0.0001);
        Assert.assertEquals("Incorrect parameter ID!", "TAIR", actual.getParamId());
        Assert.assertEquals("Incorrect stat type ID!", StatType.MAX, actual.getStatType());
        Assert.assertEquals("Incorrect date!", expectedDate, actualDate);

        actual = statTest.getMaximumDay("ta9m");
        expected = new StatMeasurement(-5.6, testCalendar, "STIL", "ta9m", StatType.MAX);

        actualDate = date.format(actual.getDateTimeOfMeasurment().getTime());
        expectedDate = date.format(expected.getDateTimeOfMeasurment().getTime());

        Assert.assertEquals("Incorrect value!", expected.getValue(), actual.value, 0.0001);
        Assert.assertEquals("Incorrect parameter ID!", "TA9M", actual.getParamId());
        Assert.assertEquals("Incorrect stat type ID!", StatType.MAX, actual.getStatType());
        Assert.assertEquals("Incorrect date!", expectedDate, actualDate);

        testCalendar.clear();
        testCalendar.set(2018, 1, 2, 0, 1130, 0);
        actual = statTest.getMaximumDay("srad");
        expected = new StatMeasurement(447.0, testCalendar, "STIL", "srad", StatType.MAX);

        actualDate = date.format(actual.getDateTimeOfMeasurment().getTime());
        expectedDate = date.format(expected.getDateTimeOfMeasurment().getTime());

        Assert.assertEquals("Wrong value returned!", expected.getValue(), actual.value, 0.0001);
        Assert.assertEquals("Incorrect parameter ID!", "SRAD", actual.getParamId());
        Assert.assertEquals("Incorrect stat type ID!", StatType.MAX, actual.getStatType());
        Assert.assertEquals("Incorrect date!", expectedDate, actualDate);
    }

    /**
     * Method to test the getMinimum method
     * 
     * @throws WrongParameterIdException
     */
    @Test
    public void dayStatisticsMinTest() throws WrongParameterIdException
    {
        testCalendar.set(2018, 1, 2, 0, 485, 0);

        StatMeasurement actual = statTest.getMinimumDay("tair");
        StatMeasurement expected = new StatMeasurement(-13.3, testCalendar, "OKCE", "tair", StatType.MIN);

        String actualDate = date.format(actual.getDateTimeOfMeasurment().getTime());
        String expectedDate = date.format(expected.getDateTimeOfMeasurment().getTime());

        Assert.assertEquals("Incorrect value!", expected.getValue(), actual.value, 0.0001);
        Assert.assertEquals("Incorrect parameter ID!", "TAIR", actual.getParamId());
        Assert.assertEquals("Incorrect stat type ID!", StatType.MIN, actual.getStatType());
        Assert.assertEquals("Incorrect date!", expectedDate, actualDate);

        testCalendar.clear();
        testCalendar.set(2018, 1, 2, 0, 500, 0);
        actual = statTest.getMinimumDay("ta9m");
        expected = new StatMeasurement(-12.4, testCalendar, "OKCE", "ta9m", StatType.MIN);

        actualDate = date.format(actual.getDateTimeOfMeasurment().getTime());
        expectedDate = date.format(expected.getDateTimeOfMeasurment().getTime());

        Assert.assertEquals("Incorrect value!", expected.getValue(), actual.value, 0.0001);
        Assert.assertEquals("Incorrect parameter ID!", "TA9M", actual.getParamId());
        Assert.assertEquals("Incorrect stat type ID!", StatType.MIN, actual.getStatType());
        Assert.assertEquals("Incorrect date!", expectedDate, actualDate);

        testCalendar.clear();
        testCalendar.set(2018, 1, 2, 0, 0, 0);
        actual = statTest.getMinimumDay("srad");
        expected = new StatMeasurement(0.0, testCalendar, "STIL", "srad", StatType.MIN);

        actualDate = date.format(actual.getDateTimeOfMeasurment().getTime());
        expectedDate = date.format(expected.getDateTimeOfMeasurment().getTime());

        Assert.assertEquals("Wrong value returned!", expected.getValue(), actual.value, 0.0001);
        Assert.assertEquals("Incorrect parameter ID!", "SRAD", actual.getParamId());
        Assert.assertEquals("Incorrect stat type ID!", StatType.MIN, actual.getStatType());
        Assert.assertEquals("Incorrect date!", expectedDate, actualDate);

    }

    /** Method to test the getMaxAndMin method */
    @Test
    public void dayStatisticsMaxAndMinTest() throws WrongParameterIdException
    {
        String expected = "-5.3\n-13.3\n";
        String actual = statTest.combineMinMaxStatistics("tair");

        Assert.assertEquals("Incorrect min/max string!", expected, actual);
    }

    /** Method to test the toString method */
    @Test
    public void testToString()
    {
        String actual = statTest.toString();
        String expected = "TAIR MAX -5.3000 STIL 2018-01-02T21:40:00 UTC\n"
                + "TAIR MIN -13.3000 OKCE 2018-01-02T08:05:00 UTC\n" + "TA9M MAX -5.6000 STIL 2018-01-02T21:40:00 UTC\n"
                + "TA9M MIN -12.4000 OKCE 2018-01-02T08:20:00 UTC\n"
                + "SRAD MAX 447.0000 STIL 2018-01-02T18:50:00 UTC\n" + "SRAD MIN 0.0000 OKCN 2018-01-02T00:00:00 UTC";

        Assert.assertEquals("Incorrect String returned!", expected, actual);
    }

    /**
     * Test class for exception thrown in getMaximumDay()
     * 
     * @throws IOException
     * @throws WrongCopyrightException
     * @throws ParseException
     * @throws WrongParameterIdException
     */
    @Test
    public void testGetMaximumException()
    {
        boolean throwException = false;

        try
        {
            statTest.getMaximumDay("Stuff");
        }
        catch (WrongParameterIdException e)
        {
            throwException = true;
        }
        Assert.assertTrue(throwException);
    }

    /**
     * Test class for exception thrown in getMinimumDay()
     * 
     * @throws IOException
     * @throws WrongCopyrightException
     * @throws ParseException
     * @throws WrongParameterIdException
     */
    @Test
    public void testGetMinimumException()
    {
        boolean throwException = false;

        try
        {
            statTest.getMinimumDay("Stuff");
        }
        catch (WrongParameterIdException e)
        {
            throwException = true;
        }
        Assert.assertTrue(throwException);
    }
}
