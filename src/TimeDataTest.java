import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test Class for TimeData
 */
public class TimeDataTest
{
    /**
     * Method to test the first TimeData constructor (also tests getters)
     */
    @Test
    public void firstConstructorTest()
    {
        String station = "test";
        Measurement tair = new Measurement(5);
        Measurement ta9m = new Measurement(7);
        Measurement solar = new Measurement(9);
        int year = 2018;
        int month = 3;
        int day = 3;
        int minute = 15;

        TimeData time = new TimeData(station, year, month, day, minute, tair, ta9m, solar);
        
        Assert.assertEquals("Incorrect station ID returned!", "test", time.getStationID());
        Assert.assertEquals("Incorrect year returned!", 2018, time.getYear());
        Assert.assertEquals("Incorrect month returned!", 3, time.getMonth());
        Assert.assertEquals("Incorrect day returned!", 3, time.getDay());
        Assert.assertEquals("Incorrect minute returned!", 15, time.getMinute());
        Assert.assertEquals("Incorrect tair returned!", 5.0, time.getMeasurement("TAIR").getValue(), 0.001);
        Assert.assertEquals("Incorrect ta9m returned!", 7.0, time.getMeasurement("TA9M").getValue(), 0.001);
        Assert.assertEquals("Incorrect srad returned!", 9.0, time.getMeasurement("SRAD").getValue(), 0.001);
    }

    /**
     * Method to test the second TimeData Constructor
     * 
     * @throws WrongParameterIdException
     */
    @Test
    public void secondConstructorTest() throws WrongTimeZoneException
    {
        boolean throwException = false;
        String station = "test2";
        Measurement tair = new Measurement(11);
        Measurement ta9m = new Measurement(13);
        Measurement solar = new Measurement(15);
        GregorianCalendar calendar = new GregorianCalendar(0, 0, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(2016, 10, 3, 0, 20, 0);

        TimeData time2 = new TimeData(station, calendar, tair, ta9m, solar);

        DateFormat format = new SimpleDateFormat(CsAbstractFile.dateTimeFormat);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String actual = format.format(time2.getMeasurementDateTime().getTime());
        String expected = "2016-10-03T00:20:00 UTC";

        Assert.assertEquals("Check your date, fool!", expected, actual);

        // test WrongCopyRightException
        // for exception testing:
        // https://github.com/junit-team/junit4/wiki/exception-testing
        try
        {
            calendar.setTimeZone(TimeZone.getTimeZone("GST"));
            time2 = new TimeData(station, calendar, tair, ta9m, solar);
        }
        catch (WrongTimeZoneException e)
        {
            throwException = true;
        }
        Assert.assertTrue(throwException);
    }

    /** Method to test getMeasurementDateTime() method */
    @Test
    public void testGetMeasurementDateTime()
    {
        String station = "test";
        Measurement tair = new Measurement(5);
        Measurement ta9m = new Measurement(7);
        Measurement solar = new Measurement(9);
        int year = 2018;
        int month = 2;
        int day = 12;
        int minute = 15;
        TimeData time = new TimeData(station, year, month, day, minute, tair, ta9m, solar);
        DateFormat format = new SimpleDateFormat(CsAbstractFile.dateTimeFormat);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String actual = format.format(time.getMeasurementDateTime().getTime());
        String expected = "2018-02-12T00:15:00 UTC";
        System.out.println(actual);

        Assert.assertEquals("Check your date, fool!", expected, actual);
    }
}
