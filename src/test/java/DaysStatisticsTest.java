import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DaysStatisticsTest
{
    /** Array to hold the files */
    String[] files = new String[3];

    private Instant testDateTime;

    /** a DayStatistics object for test classes */
    DaysStatistics statTest;

    /**
     * Method to initialize data
     * 
     * @throws IOException
     * @throws WrongCopyrightException
     */
    @BeforeEach
    public void setUp() throws IOException, WrongCopyrightException
    {
        testDateTime = utcMinuteOfDay(1300);

        files[0] = "Data/mesonet/20180102okcn.mts";
        files[1] = "Data/mesonet/20180102stil.mts";
        files[2] = "Data/mesonet/20180102okce.mts";

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
        StatMeasurement expected = new StatMeasurement(-5.3, testDateTime, "STIL", "tair", StatType.MAX);

        String actualDate = CsAbstractFile.formatUtcDateTime(actual.getDateTimeOfMeasurement());
        String expectedDate = CsAbstractFile.formatUtcDateTime(expected.getDateTimeOfMeasurement());

        assertEquals(expected.getValue(), actual.value, 0.0001, "Incorrect value!");
        assertEquals("TAIR", actual.getParamId(), "Incorrect parameter ID!");
        assertEquals(StatType.MAX, actual.getStatType(), "Incorrect stat type ID!");
        assertEquals(expectedDate, actualDate, "Incorrect date!");

        actual = statTest.getMaximumDay("ta9m");
        expected = new StatMeasurement(-5.6, testDateTime, "STIL", "ta9m", StatType.MAX);

        actualDate = CsAbstractFile.formatUtcDateTime(actual.getDateTimeOfMeasurement());
        expectedDate = CsAbstractFile.formatUtcDateTime(expected.getDateTimeOfMeasurement());

        assertEquals(expected.getValue(), actual.value, 0.0001, "Incorrect value!");
        assertEquals("TA9M", actual.getParamId(), "Incorrect parameter ID!");
        assertEquals(StatType.MAX, actual.getStatType(), "Incorrect stat type ID!");
        assertEquals(expectedDate, actualDate, "Incorrect date!");

        testDateTime = utcMinuteOfDay(1130);
        actual = statTest.getMaximumDay("srad");
        expected = new StatMeasurement(447.0, testDateTime, "STIL", "srad", StatType.MAX);

        actualDate = CsAbstractFile.formatUtcDateTime(actual.getDateTimeOfMeasurement());
        expectedDate = CsAbstractFile.formatUtcDateTime(expected.getDateTimeOfMeasurement());

        assertEquals(expected.getValue(), actual.value, 0.0001, "Wrong value returned!");
        assertEquals("SRAD", actual.getParamId(), "Incorrect parameter ID!");
        assertEquals(StatType.MAX, actual.getStatType(), "Incorrect stat type ID!");
        assertEquals(expectedDate, actualDate, "Incorrect date!");
    }

    /**
     * Method to test the getMinimum method
     * 
     * @throws WrongParameterIdException
     */
    @Test
    public void dayStatisticsMinTest() throws WrongParameterIdException
    {
        testDateTime = utcMinuteOfDay(485);

        StatMeasurement actual = statTest.getMinimumDay("tair");
        StatMeasurement expected = new StatMeasurement(-13.3, testDateTime, "OKCE", "tair", StatType.MIN);

        String actualDate = CsAbstractFile.formatUtcDateTime(actual.getDateTimeOfMeasurement());
        String expectedDate = CsAbstractFile.formatUtcDateTime(expected.getDateTimeOfMeasurement());

        assertEquals(expected.getValue(), actual.value, 0.0001, "Incorrect value!");
        assertEquals("TAIR", actual.getParamId(), "Incorrect parameter ID!");
        assertEquals(StatType.MIN, actual.getStatType(), "Incorrect stat type ID!");
        assertEquals(expectedDate, actualDate, "Incorrect date!");

        testDateTime = utcMinuteOfDay(500);
        actual = statTest.getMinimumDay("ta9m");
        expected = new StatMeasurement(-12.4, testDateTime, "OKCE", "ta9m", StatType.MIN);

        actualDate = CsAbstractFile.formatUtcDateTime(actual.getDateTimeOfMeasurement());
        expectedDate = CsAbstractFile.formatUtcDateTime(expected.getDateTimeOfMeasurement());

        assertEquals(expected.getValue(), actual.value, 0.0001, "Incorrect value!");
        assertEquals("TA9M", actual.getParamId(), "Incorrect parameter ID!");
        assertEquals(StatType.MIN, actual.getStatType(), "Incorrect stat type ID!");
        assertEquals(expectedDate, actualDate, "Incorrect date!");

        testDateTime = utcMinuteOfDay(0);
        actual = statTest.getMinimumDay("srad");
        expected = new StatMeasurement(0.0, testDateTime, "STIL", "srad", StatType.MIN);

        actualDate = CsAbstractFile.formatUtcDateTime(actual.getDateTimeOfMeasurement());
        expectedDate = CsAbstractFile.formatUtcDateTime(expected.getDateTimeOfMeasurement());

        assertEquals(expected.getValue(), actual.value, 0.0001, "Wrong value returned!");
        assertEquals("SRAD", actual.getParamId(), "Incorrect parameter ID!");
        assertEquals(StatType.MIN, actual.getStatType(), "Incorrect stat type ID!");
        assertEquals(expectedDate, actualDate, "Incorrect date!");

    }

    /** Verifies daily averages are retained for each loaded file. */
    @Test
    public void dailyAverageAndTotalTest() throws WrongParameterIdException
    {
        for (String parameter : new String[] { "tair", "ta9m", "srad" })
        {
            assertEquals(3, statTest.getAverageDays(parameter).size());
            for (StatMeasurement measurement : statTest.getAverageDays(parameter))
            {
                assertEquals(StatType.AVG, measurement.getStatType());
            }
        }
    }

    @Test
    public void extremeDayStatisticsIgnoreDaysWithNoValidMeasurements()
            throws IOException, WrongCopyrightException, WrongParameterIdException
    {
        Path invalidFile = Files.createTempFile("mesonet-invalid-day", ".mts");
        Path validFile = Files.createTempFile("mesonet-valid-day", ".mts");
        try
        {
            String header = "101 Mesonet test data\n21 2018 01 02 00 00 00\nSTID TIME TAIR TA9M SRAD\n";
            Files.writeString(invalidFile, header + "TEST 0 -999 10 20\nTEST 5 -995 20 40\n");
            Files.writeString(validFile, header + "TEST 0 12 10 20\nTEST 5 18 20 40\n");

            DaysStatistics days = new DaysStatistics(new String[] { invalidFile.toString(), validFile.toString() });
            days.findStatistics();
            assertEquals(18.0, days.getMaximumDay("TAIR").getValue(), 0.0001);
            assertEquals(12.0, days.getMinimumDay("TAIR").getValue(), 0.0001);

            DaysStatistics allInvalidDays = new DaysStatistics(new String[] { invalidFile.toString() });
            allInvalidDays.findStatistics();
            assertFalse(allInvalidDays.getMaximumDay("TAIR").isValid());
            assertFalse(allInvalidDays.getMinimumDay("TAIR").isValid());
        }
        finally
        {
            Files.deleteIfExists(invalidFile);
            Files.deleteIfExists(validFile);
        }
    }

    /** Method to test the getMaxAndMin method */
    @Test
    public void dayStatisticsMaxAndMinTest() throws WrongParameterIdException
    {
        String expected = "-5.3\n-13.3\n";
        String actual = statTest.combineMinMaxStatistics("tair");

        assertEquals(expected, actual, "Incorrect min/max string!");
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

        assertEquals(expected, actual, "Incorrect String returned!");
    }

    /**
     * Test class for exception thrown in getMaximumDay()
     * 
     * @throws IOException
     * @throws WrongCopyrightException
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
        assertTrue(throwException);
    }

    /**
     * Test class for exception thrown in getMinimumDay()
     * 
     * @throws IOException
     * @throws WrongCopyrightException
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
        assertTrue(throwException);
    }

    private Instant utcMinuteOfDay(int minuteOfDay)
    {
        return LocalDate.of(2018, 1, 2).atStartOfDay().plusMinutes(minuteOfDay).toInstant(ZoneOffset.UTC);
    }
}
