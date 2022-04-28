import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class DayDataStatisticsTest
{
    /** the date to read in */
    ArrayList<TimeData> inData;

    /** Class to initialize and set up the data */
    @Before
    public void setUp()
    {
        inData = new ArrayList<TimeData>();

        Measurement tairTest = new Measurement(10.0);
        Measurement ta9mTest = new Measurement(110.0);
        Measurement sradTest = new Measurement(12.0);

        TimeData timeDataTest1 = new TimeData("Riften", 2018, 01, 01, 0, tairTest, ta9mTest, sradTest);
        inData.add(timeDataTest1);

        tairTest = new Measurement(1.0);
        ta9mTest = new Measurement(13.8);
        sradTest = new Measurement(-3.7);

        TimeData timeDataTest2 = new TimeData("Riften", 2018, 01, 01, 75, tairTest, ta9mTest, sradTest);
        inData.add(timeDataTest2);

        tairTest = new Measurement(78.0);
        ta9mTest = new Measurement(-15.0);
        sradTest = new Measurement(210.8);

        TimeData timeDataTest3 = new TimeData("Riften", 2018, 01, 01, 15, tairTest, ta9mTest, sradTest);
        inData.add(timeDataTest3);
    }

    /** Method to test the tair getters. Also tests constructor */
    @Test
    public void tairGettersTest()
    {
        DayDataStatistics dayTest = new DayDataStatistics(inData);

        GregorianCalendar testDate = new GregorianCalendar(0, 0, 0);
        testDate.setTimeZone(TimeZone.getTimeZone("UTC"));
        testDate.set(2018, 1, 1, 0, 75, 0);

        StatMeasurement expected = new StatMeasurement(1.0, testDate, "Riften", "TAIR", StatType.MIN);
        StatMeasurement actual = dayTest.getStatMeasurement("tair", StatType.MIN);

        Assert.assertEquals("Incorrect value returned!", expected.value, actual.value, 0.0001);
        Assert.assertEquals("Incorrect date returned!", expected.getDateTimeOfMeasurment(),
                actual.getDateTimeOfMeasurment());
        Assert.assertEquals("Incorrect paramter ID returned!", expected.getParamId(), actual.getParamId());
        Assert.assertEquals("Incorrect statistic type returned!", expected.getStatType(), actual.getStatType());
        Assert.assertEquals("Incorrect station ID returned!", dayTest.getStationID(), "Riften");

        testDate.clear();
        testDate.set(2018, 1, 1, 0, 15, 0);
        expected = new StatMeasurement(78.0, testDate, "Riften", "TAIR", StatType.MAX);
        actual = dayTest.getStatMeasurement("tair", StatType.MAX);

        Assert.assertEquals("Incorrect value returned!", expected.value, actual.value, 0.0001);
        Assert.assertEquals("Incorrect date returned!", expected.getDateTimeOfMeasurment(),
                actual.getDateTimeOfMeasurment());
        Assert.assertEquals("Incorrect parameter ID returned!", expected.getParamId(), actual.getParamId());
        Assert.assertEquals("Incorrect statistic type returned!", expected.getStatType(), actual.getStatType());
        Assert.assertEquals("Incorrect station ID returned!", dayTest.getStationID(), "Riften");

        testDate.set(Calendar.MINUTE, 0);
        expected = new StatMeasurement(29.6667, testDate, "Riften", "TAIR", StatType.AVG);
        actual = dayTest.getStatMeasurement("tair", StatType.AVG);

        Assert.assertEquals("Incorrect value returned!", expected.value, actual.value, 0.0001);
        Assert.assertEquals("Incorrect date returned!", expected.getDateTimeOfMeasurment(),
                actual.getDateTimeOfMeasurment());
        Assert.assertEquals("Incorrect parameter ID returned!", expected.getParamId(), actual.getParamId());
        Assert.assertEquals("Incorrect statistic type returned!", expected.getStatType(), actual.getStatType());
        Assert.assertEquals("Incorrect station ID returned!", dayTest.getStationID(), "Riften");
    }

    /** Method to test ta9m getters */
    @Test
    public void ta9mGettersTest()
    {
        DayDataStatistics dayTest = new DayDataStatistics(inData);

        GregorianCalendar testDate = new GregorianCalendar(0, 0, 0);
        testDate.setTimeZone(TimeZone.getTimeZone("UTC"));
        testDate.set(2018, 1, 1, 0, 15, 0);

        StatMeasurement expected = new StatMeasurement(-15, testDate, "Riften", "TA9M", StatType.MIN);
        StatMeasurement actual = dayTest.getStatMeasurement("ta9m", StatType.MIN);

        Assert.assertEquals("Incorrect value returned!", expected.value, actual.value, 0.0001);
        Assert.assertEquals("Incorrect date returned!", expected.getDateTimeOfMeasurment(),
                actual.getDateTimeOfMeasurment());
        Assert.assertEquals("Incorrect parameter ID returned!", expected.getParamId(), actual.getParamId());
        Assert.assertEquals("Incorrect statistic type returned!", expected.getStatType(), actual.getStatType());
        Assert.assertEquals("Incorrect station ID returned!", dayTest.getStationID(), "Riften");

        testDate.set(Calendar.MINUTE, 0);
        expected = new StatMeasurement(110.0, testDate, "Riften", "TA9M", StatType.MAX);
        actual = dayTest.getStatMeasurement("ta9m", StatType.MAX);

        Assert.assertEquals("Incorrect value returned!", expected.value, actual.value, 0.0001);
        Assert.assertEquals("Incorrect date returned!", expected.getDateTimeOfMeasurment(),
                actual.getDateTimeOfMeasurment());
        Assert.assertEquals("Incorrect parameter ID returned!", expected.getParamId(), actual.getParamId());
        Assert.assertEquals("Incorrect statistic type returned!", expected.getStatType(), actual.getStatType());
        Assert.assertEquals("Incorrect station ID returned!", dayTest.getStationID(), "Riften");

        testDate.set(Calendar.MINUTE, 0);
        expected = new StatMeasurement(36.2667, testDate, "Riften", "TA9M", StatType.AVG);
        actual = dayTest.getStatMeasurement("ta9m", StatType.AVG);

        Assert.assertEquals("Incorrect value returned!", expected.value, actual.value, 0.0001);
        Assert.assertEquals("Incorrect date returned!", expected.getDateTimeOfMeasurment(),
                actual.getDateTimeOfMeasurment());
        Assert.assertEquals("Incorrect parameter ID returned!", expected.getParamId(), actual.getParamId());
        Assert.assertEquals("Incorrect statistic type returned!", expected.getStatType(), actual.getStatType());
        Assert.assertEquals("Incorrect station ID returned!", dayTest.getStationID(), "Riften");
    }

    /** Method to test solar radiation getters */
    @Test
    public void sradGettersTest()
    {
        DayDataStatistics dayTest = new DayDataStatistics(inData);

        GregorianCalendar testDate = new GregorianCalendar(0, 0, 0);
        testDate.setTimeZone(TimeZone.getTimeZone("UTC"));
        testDate.set(2018, 1, 1, 0, 75, 0);

        StatMeasurement expected = new StatMeasurement(-3.7, testDate, "Riften", "SRAD", StatType.MIN);
        StatMeasurement actual = dayTest.getStatMeasurement("srad", StatType.MIN);

        Assert.assertEquals("Incorrect value returned!", expected.value, actual.value, 0.0001);
        Assert.assertEquals("Incorrect date returned!", expected.getDateTimeOfMeasurment(),
                actual.getDateTimeOfMeasurment());
        Assert.assertEquals("Incorrect parameter ID returned!", expected.getParamId(), actual.getParamId());
        Assert.assertEquals("Incorrect statistic type returned!", expected.getStatType(), actual.getStatType());
        Assert.assertEquals("Incorrect station ID returned!", dayTest.getStationID(), "Riften");

        testDate.clear();
        testDate.set(2018, 1, 1, 0, 15, 0);
        expected = new StatMeasurement(210.8, testDate, "Riften", "SRAD", StatType.MAX);
        actual = dayTest.getStatMeasurement("srad", StatType.MAX);

        Assert.assertEquals("Incorrect value returned!", expected.value, actual.value, 0.0001);
        Assert.assertEquals("Incorrect date returned!", expected.getDateTimeOfMeasurment(),
                actual.getDateTimeOfMeasurment());
        Assert.assertEquals("Incorrect parameter ID returned!", expected.getParamId(), actual.getParamId());
        Assert.assertEquals("Incorrect statistic type returned!", expected.getStatType(), actual.getStatType());
        Assert.assertEquals("Incorrect station ID returned!", dayTest.getStationID(), "Riften");

        testDate.set(Calendar.MINUTE, 0);
        expected = new StatMeasurement(73.0333, testDate, "Riften", "SRAD", StatType.AVG);
        actual = dayTest.getStatMeasurement("srad", StatType.AVG);

        Assert.assertEquals("Incorrect value returned!", expected.value, actual.value, 0.0001);
        Assert.assertEquals("Incorrect date returned!", expected.getDateTimeOfMeasurment(),
                actual.getDateTimeOfMeasurment());
        Assert.assertEquals("Incorrect parameter ID returned!", expected.getParamId(), actual.getParamId());
        Assert.assertEquals("Incorrect statistic type returned!", expected.getStatType(), actual.getStatType());
        Assert.assertEquals("Incorrect station ID returned!", dayTest.getStationID(), "Riften");

        testDate.set(Calendar.MINUTE, 0);
        expected = new StatMeasurement(219.1, testDate, "Riften", "SRAD", StatType.TOT);
        actual = dayTest.getStatMeasurement("srad", StatType.TOT);

        Assert.assertEquals("Incorrect value returned!", expected.value, actual.value, 0.0001);
        Assert.assertEquals("Incorrect date returned!", expected.getDateTimeOfMeasurment(),
                actual.getDateTimeOfMeasurment());
        Assert.assertEquals("Incorrect parameter ID returned!", expected.getParamId(), actual.getParamId());
        Assert.assertEquals("Incorrect statistic type returned!", expected.getStatType(), actual.getStatType());
        Assert.assertEquals("Incorrect station ID returned!", dayTest.getStationID(), "Riften");
    }

    /**
     * Method to test toString method
     */
    @Test
    public void testToString()
    {
        DayDataStatistics dayTest = new DayDataStatistics(inData);

        GregorianCalendar testDate = new GregorianCalendar(0, 0, 0);
        testDate.setTimeZone(TimeZone.getTimeZone("UTC"));
        testDate.set(2018, 1, 1, 0, 15, 0);

        String actual = dayTest.toString();
        String expected = "2018-01-01, RIFTEN:\n Air Temperature[1.5m] = [1.0000, 29.6667, 78.0000],\n"
                + " Air Temperature[9m] = [-15.0000, 36.2667, 110.0000],\n"
                + " Solar Radiation = [-3.7000, 73.0333, 210.8000, 219.1000]";
        Assert.assertEquals("Check your string, fool!", expected, actual);
    }
}