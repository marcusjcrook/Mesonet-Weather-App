import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for StatMeasurement
 */
public class StatMeasurementTest
{
    /** a constant for setting first constructor */
    private static final String NADA = "nada";

    /** a variable for holding the date */
    private GregorianCalendar date;

    /** a variable for holding the parameter ID */
    private String paramId;

    /** a variable for holding the stat type */
    private StatType statType;

    /** a variable for holding the station ID */
    private String stationId;

    /**
     * the setup before running the tests. Initializes the variables
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception
    {
        // initialize the variables
        date = new GregorianCalendar();
        date.set(Calendar.YEAR, 2018);
        date.set(Calendar.MONTH, 2);
        date.set(Calendar.DAY_OF_MONTH, 19);
        date.set(Calendar.MINUTE, 15);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        paramId = "TEST";
        statType = StatType.MAX;
        stationId = "STATION";
    }

    /**
     * method for testing first StatMeasurement constructor
     */
    @Test
    public void testStatMeasurementConstructor1()
    {
        StatMeasurement stat1 = new StatMeasurement();

        Assert.assertEquals("Incorrect value returned!", Double.NaN, stat1.getValue(), 0.001);
        Assert.assertFalse(stat1.isValid());
        Assert.assertEquals("Incorrect parameter ID returned!", NADA, stat1.getParamId());
    }

    /**
     * method for testing second StatMeasurement constructor
     */
    @Test
    public void testStatMeasurementConstructor2()
    {
        StatMeasurement stat = new StatMeasurement(2.5, date, stationId, paramId, statType);

        Assert.assertEquals("Incorrect value returned!", 2.5, stat.getValue(), 0.001);
        Assert.assertTrue(stat.isValid());
        Assert.assertEquals("Incorrect parameter ID returned!", "TEST", stat.getParamId());
        Assert.assertEquals("Incorrect statistic type returned!", StatType.MAX, stat.getStatType());
    }

    /**
     * method for testing the setters
     */
    @Test
    public void testSetters()
    {
        StatMeasurement stat = new StatMeasurement();

        stat.setStatType(StatType.MIN);
        stat.setParamId("PASS");

        Assert.assertEquals("Incorrect parameter ID returned!", "PASS", stat.getParamId());
        Assert.assertEquals("Incorrect statistic type returned!", StatType.MIN, stat.getStatType());

        stat.setStatType(StatType.AVG);
        Assert.assertEquals("Incorrect statistic type returned!", StatType.AVG, stat.getStatType());

        stat.setStatType(StatType.TOT);
        Assert.assertEquals("Incorrect statistic type returned!", StatType.TOT, stat.getStatType());
    }

    /**
     * method for testing isLessThan method
     */
    @Test
    public void testIsLessThan()
    {
        StatMeasurement stat1 = new StatMeasurement(2.4, date, stationId, paramId, statType);
        StatMeasurement stat2 = new StatMeasurement(2.5, date, stationId, paramId, statType);
        StatMeasurement stat3 = new StatMeasurement(-901, date, stationId, paramId, statType);

        Assert.assertTrue("Incorrect boolean returned!", stat1.isLessThan(stat2));
        Assert.assertFalse("Incorrect boolean returned!", stat2.isLessThan(stat1));
        Assert.assertTrue("Incorrect boolean returned!", stat1.isLessThan(stat3));
        Assert.assertFalse("Incorrect boolean returned!", stat3.isLessThan(stat3));
    }

    /**
     * method for testing isGreaterThan method
     */
    @Test
    public void testIsGreaterThan()
    {
        StatMeasurement stat1 = new StatMeasurement(2.4, date, stationId, paramId, statType);
        StatMeasurement stat2 = new StatMeasurement(2.5, date, stationId, paramId, statType);
        StatMeasurement stat3 = new StatMeasurement(-901, date, stationId, paramId, statType);

        Assert.assertFalse("Incorrect boolean returned!", stat1.isGreaterThan(stat2));
        Assert.assertTrue("Incorrect boolean returned!", stat2.isGreaterThan(stat1));
        Assert.assertTrue("Incorrect boolean returned!", stat1.isGreaterThan(stat3));
        Assert.assertFalse("Incorrect boolean returned!", stat3.isGreaterThan(stat3));
    }

    /** Method to test the compareWithTimeString method */
    @Test
    public void testCompareWithTimeString() throws ParseException
    {
        String inDate = "2018-02-19T00:15:00 UTC";
        StatMeasurement stat = new StatMeasurement(31, date, stationId, paramId, statType);
        int actual = stat.compareWithTimeString(inDate);
        int expected = 0;

        Assert.assertEquals("Check your date, fool!", expected, actual);
        Assert.assertFalse("Incorrect boolean returned!", stat.newerThan(inDate));
        Assert.assertFalse("Incorrect boolean returned!", stat.olderThan(inDate));

        inDate = "2018-03-19T00:15:00 UTC";
        actual = stat.compareWithTimeString(inDate);
        expected = -1;

        Assert.assertEquals("Check your date, fool!", expected, actual);
        Assert.assertTrue("Incorrect boolean returned!", stat.olderThan(inDate));

        inDate = "2018-02-19T00:10:00 UTC";
        actual = stat.compareWithTimeString(inDate);
        expected = 1;

        Assert.assertEquals("Check your date, fool!", expected, actual);
        Assert.assertTrue("Incorrect boolean returned!", stat.newerThan(inDate));
    }

    /** Method to test toString() */
    @Test
    public void testToString()
    {
        StatMeasurement stat = new StatMeasurement(31, date, stationId, paramId, statType);
        String expected = "TEST MAX 31.0000 STATION 2018-02-19T00:15:00 UTC";
        String actual = stat.toString();

        Assert.assertEquals("Check your string, fool!", expected, actual);
    }

    /** Method to test getDateTimeOfMeasurement() method */
    @Test
    public void testGetDateTimeOfMeasurement()
    {
        StatMeasurement stat = new StatMeasurement(31, date, stationId, paramId, statType);
        DateFormat format = new SimpleDateFormat(CsAbstractFile.dateTimeFormat);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        String actual = format.format(stat.getDateTimeOfMeasurment().getTime());
        String expected = "2018-02-19T00:15:00 UTC";
        Assert.assertEquals("Check your date, fool!", expected, actual);
    }

    /** Method to test compareTo() method */
    @Test
    public void compareToTest()
    {
        StatMeasurement stat = new StatMeasurement(31, date, stationId, paramId, statType);
        StatMeasurement stat2 = new StatMeasurement(65, date, stationId, paramId, statType);
        StatMeasurement stat3 = new StatMeasurement(23, date, stationId, paramId, statType);

        Assert.assertEquals("Incorrect int returned", 1, stat.compareTo(stat3));
        Assert.assertEquals("Incorrect int returned", -1, stat.compareTo(stat2));
        Assert.assertEquals("Incorrect int returned", 0, stat.compareTo(stat));
    }
}
