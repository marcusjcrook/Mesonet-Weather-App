import java.util.ArrayList;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DayDataStatisticsTest
{
    /** the date to read in */
    ArrayList<TimeData> inData;

    /** Class to initialize and set up the data */
    @BeforeEach
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

        Instant testDate = utc(2018, 1, 1, 1, 15);

        StatMeasurement expected = new StatMeasurement(1.0, testDate, "Riften", "TAIR", StatType.MIN);
        StatMeasurement actual = dayTest.getStatMeasurement("tair", StatType.MIN);

        assertEquals(expected.value, actual.value, 0.0001, "Incorrect value returned!");
        assertEquals(expected.getDateTimeOfMeasurement(), actual.getDateTimeOfMeasurement(), "Incorrect date returned!");
        assertEquals(expected.getParamId(), actual.getParamId(), "Incorrect paramter ID returned!");
        assertEquals(expected.getStatType(), actual.getStatType(), "Incorrect statistic type returned!");
        assertEquals(dayTest.getStationID(), "Riften", "Incorrect station ID returned!");

        testDate = utc(2018, 1, 1, 0, 15);
        expected = new StatMeasurement(78.0, testDate, "Riften", "TAIR", StatType.MAX);
        actual = dayTest.getStatMeasurement("tair", StatType.MAX);

        assertEquals(expected.value, actual.value, 0.0001, "Incorrect value returned!");
        assertEquals(expected.getDateTimeOfMeasurement(), actual.getDateTimeOfMeasurement(), "Incorrect date returned!");
        assertEquals(expected.getParamId(), actual.getParamId(), "Incorrect parameter ID returned!");
        assertEquals(expected.getStatType(), actual.getStatType(), "Incorrect statistic type returned!");
        assertEquals(dayTest.getStationID(), "Riften", "Incorrect station ID returned!");

        testDate = utc(2018, 1, 1, 0, 0);
        expected = new StatMeasurement(29.6667, testDate, "Riften", "TAIR", StatType.AVG);
        actual = dayTest.getStatMeasurement("tair", StatType.AVG);

        assertEquals(expected.value, actual.value, 0.0001, "Incorrect value returned!");
        assertEquals(expected.getDateTimeOfMeasurement(), actual.getDateTimeOfMeasurement(), "Incorrect date returned!");
        assertEquals(expected.getParamId(), actual.getParamId(), "Incorrect parameter ID returned!");
        assertEquals(expected.getStatType(), actual.getStatType(), "Incorrect statistic type returned!");
        assertEquals(dayTest.getStationID(), "Riften", "Incorrect station ID returned!");
    }

    /** Method to test ta9m getters */
    @Test
    public void ta9mGettersTest()
    {
        DayDataStatistics dayTest = new DayDataStatistics(inData);

        Instant testDate = utc(2018, 1, 1, 0, 15);

        StatMeasurement expected = new StatMeasurement(-15, testDate, "Riften", "TA9M", StatType.MIN);
        StatMeasurement actual = dayTest.getStatMeasurement("ta9m", StatType.MIN);

        assertEquals(expected.value, actual.value, 0.0001, "Incorrect value returned!");
        assertEquals(expected.getDateTimeOfMeasurement(), actual.getDateTimeOfMeasurement(), "Incorrect date returned!");
        assertEquals(expected.getParamId(), actual.getParamId(), "Incorrect parameter ID returned!");
        assertEquals(expected.getStatType(), actual.getStatType(), "Incorrect statistic type returned!");
        assertEquals(dayTest.getStationID(), "Riften", "Incorrect station ID returned!");

        testDate = utc(2018, 1, 1, 0, 0);
        expected = new StatMeasurement(110.0, testDate, "Riften", "TA9M", StatType.MAX);
        actual = dayTest.getStatMeasurement("ta9m", StatType.MAX);

        assertEquals(expected.value, actual.value, 0.0001, "Incorrect value returned!");
        assertEquals(expected.getDateTimeOfMeasurement(), actual.getDateTimeOfMeasurement(), "Incorrect date returned!");
        assertEquals(expected.getParamId(), actual.getParamId(), "Incorrect parameter ID returned!");
        assertEquals(expected.getStatType(), actual.getStatType(), "Incorrect statistic type returned!");
        assertEquals(dayTest.getStationID(), "Riften", "Incorrect station ID returned!");

        testDate = utc(2018, 1, 1, 0, 0);
        expected = new StatMeasurement(36.2667, testDate, "Riften", "TA9M", StatType.AVG);
        actual = dayTest.getStatMeasurement("ta9m", StatType.AVG);

        assertEquals(expected.value, actual.value, 0.0001, "Incorrect value returned!");
        assertEquals(expected.getDateTimeOfMeasurement(), actual.getDateTimeOfMeasurement(), "Incorrect date returned!");
        assertEquals(expected.getParamId(), actual.getParamId(), "Incorrect parameter ID returned!");
        assertEquals(expected.getStatType(), actual.getStatType(), "Incorrect statistic type returned!");
        assertEquals(dayTest.getStationID(), "Riften", "Incorrect station ID returned!");
    }

    /** Method to test solar radiation getters */
    @Test
    public void sradGettersTest()
    {
        DayDataStatistics dayTest = new DayDataStatistics(inData);

        Instant testDate = utc(2018, 1, 1, 1, 15);

        StatMeasurement expected = new StatMeasurement(-3.7, testDate, "Riften", "SRAD", StatType.MIN);
        StatMeasurement actual = dayTest.getStatMeasurement("srad", StatType.MIN);

        assertEquals(expected.value, actual.value, 0.0001, "Incorrect value returned!");
        assertEquals(expected.getDateTimeOfMeasurement(), actual.getDateTimeOfMeasurement(), "Incorrect date returned!");
        assertEquals(expected.getParamId(), actual.getParamId(), "Incorrect parameter ID returned!");
        assertEquals(expected.getStatType(), actual.getStatType(), "Incorrect statistic type returned!");
        assertEquals(dayTest.getStationID(), "Riften", "Incorrect station ID returned!");

        testDate = utc(2018, 1, 1, 0, 15);
        expected = new StatMeasurement(210.8, testDate, "Riften", "SRAD", StatType.MAX);
        actual = dayTest.getStatMeasurement("srad", StatType.MAX);

        assertEquals(expected.value, actual.value, 0.0001, "Incorrect value returned!");
        assertEquals(expected.getDateTimeOfMeasurement(), actual.getDateTimeOfMeasurement(), "Incorrect date returned!");
        assertEquals(expected.getParamId(), actual.getParamId(), "Incorrect parameter ID returned!");
        assertEquals(expected.getStatType(), actual.getStatType(), "Incorrect statistic type returned!");
        assertEquals(dayTest.getStationID(), "Riften", "Incorrect station ID returned!");

        testDate = utc(2018, 1, 1, 0, 0);
        expected = new StatMeasurement(73.0333, testDate, "Riften", "SRAD", StatType.AVG);
        actual = dayTest.getStatMeasurement("srad", StatType.AVG);

        assertEquals(expected.value, actual.value, 0.0001, "Incorrect value returned!");
        assertEquals(expected.getDateTimeOfMeasurement(), actual.getDateTimeOfMeasurement(), "Incorrect date returned!");
        assertEquals(expected.getParamId(), actual.getParamId(), "Incorrect parameter ID returned!");
        assertEquals(expected.getStatType(), actual.getStatType(), "Incorrect statistic type returned!");
        assertEquals(dayTest.getStationID(), "Riften", "Incorrect station ID returned!");

    }

    @Test
    public void statisticsIgnoreInvalidMeasurements()
    {
        ArrayList<TimeData> observations = new ArrayList<TimeData>();
        observations.add(new TimeData("Riften", 2018, 1, 1, 0, new Measurement(10.0), new Measurement(5.0),
                new Measurement(20.0)));
        observations.add(new TimeData("Riften", 2018, 1, 1, 5, new Measurement(-999.0), new Measurement(15.0),
                new Measurement(-999.0)));
        observations.add(new TimeData("Riften", 2018, 1, 1, 10, new Measurement(30.0), new Measurement(25.0),
                new Measurement(40.0)));

        DayDataStatistics statistics = new DayDataStatistics(observations);

        assertEquals(10.0, statistics.getStatMeasurement("TAIR", StatType.MIN).getValue(), 0.0001);
        assertEquals(30.0, statistics.getStatMeasurement("TAIR", StatType.MAX).getValue(), 0.0001);
        assertEquals(20.0, statistics.getStatMeasurement("TAIR", StatType.AVG).getValue(), 0.0001);
        assertEquals(30.0, statistics.getStatMeasurement("SRAD", StatType.AVG).getValue(), 0.0001);
    }

    @Test
    public void statisticsForAllInvalidMeasurementsAreMarkedBad()
    {
        ArrayList<TimeData> observations = new ArrayList<TimeData>();
        observations.add(new TimeData("Riften", 2018, 1, 1, 0, new Measurement(-999.0), new Measurement(5.0),
                new Measurement(20.0)));
        observations.add(new TimeData("Riften", 2018, 1, 1, 5, new Measurement(-995.0), new Measurement(15.0),
                new Measurement(40.0)));

        DayDataStatistics statistics = new DayDataStatistics(observations);
        for (StatType type : StatType.values())
        {
            StatMeasurement result = statistics.getStatMeasurement("TAIR", type);
            assertFalse(result.isValid(), type + " should be invalid");
            assertTrue(Double.isNaN(result.getValue()), type + " should have a NaN value");
            assertTrue(result.toString().contains(" bad "), type + " should display as bad");
        }
        assertTrue(statistics.toString().contains("Air Temperature[1.5m] = [bad, bad, bad]"));
    }

    @Test
    public void emptyObservationListIsRejected()
    {
        assertThrows(IllegalArgumentException.class, () -> new DayDataStatistics(new ArrayList<TimeData>()));
    }

    /**
     * Method to test toString method
     */
    @Test
    public void testToString()
    {
        DayDataStatistics dayTest = new DayDataStatistics(inData);

        String actual = dayTest.toString();
        String expected = "2018-01-01, RIFTEN:\n Air Temperature[1.5m] = [1.0000, 29.6667, 78.0000],\n"
                + " Air Temperature[9m] = [-15.0000, 36.2667, 110.0000],\n"
                + " Solar Radiation = [-3.7000, 73.0333, 210.8000]";
        assertEquals(expected, actual, "Check your string, fool!");
    }

    private Instant utc(int year, int month, int day, int hour, int minute)
    {
        return LocalDateTime.of(year, month, day, hour, minute).toInstant(ZoneOffset.UTC);
    }
}