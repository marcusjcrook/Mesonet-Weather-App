import java.text.ParseException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for StatMeasurement
 */
public class StatMeasurementTest
{
    /** a constant for setting first constructor */
    private static final String NADA = "nada";

    /** a variable for holding the date */
    private Instant date;

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
    @BeforeEach
    public void setUp() throws Exception
    {
        date = Instant.parse("2018-02-19T00:15:00Z");
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

        assertEquals(Double.NaN, stat1.getValue(), 0.001, "Incorrect value returned!");
        assertFalse(stat1.isValid());
        assertEquals(NADA, stat1.getParamId(), "Incorrect parameter ID returned!");
    }

    /**
     * method for testing second StatMeasurement constructor
     */
    @Test
    public void testStatMeasurementConstructor2()
    {
        StatMeasurement stat = new StatMeasurement(2.5, date, stationId, paramId, statType);

        assertEquals(2.5, stat.getValue(), 0.001, "Incorrect value returned!");
        assertTrue(stat.isValid());
        assertEquals("TEST", stat.getParamId(), "Incorrect parameter ID returned!");
        assertEquals(StatType.MAX, stat.getStatType(), "Incorrect statistic type returned!");
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

        assertEquals("PASS", stat.getParamId(), "Incorrect parameter ID returned!");
        assertEquals(StatType.MIN, stat.getStatType(), "Incorrect statistic type returned!");

        stat.setStatType(StatType.AVG);
        assertEquals(StatType.AVG, stat.getStatType(), "Incorrect statistic type returned!");

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

        assertTrue(stat1.isLessThan(stat2), "Incorrect boolean returned!");
        assertFalse(stat2.isLessThan(stat1), "Incorrect boolean returned!");
        assertTrue(stat1.isLessThan(stat3), "Incorrect boolean returned!");
        assertFalse(stat3.isLessThan(stat3), "Incorrect boolean returned!");
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

        assertFalse(stat1.isGreaterThan(stat2), "Incorrect boolean returned!");
        assertTrue(stat2.isGreaterThan(stat1), "Incorrect boolean returned!");
        assertTrue(stat1.isGreaterThan(stat3), "Incorrect boolean returned!");
        assertFalse(stat3.isGreaterThan(stat3), "Incorrect boolean returned!");
    }

    /** Method to test the compareWithTimeString method */
    @Test
    public void testCompareWithTimeString() throws ParseException
    {
        String inDate = "2018-02-19T00:15:00 UTC";
        StatMeasurement stat = new StatMeasurement(31, date, stationId, paramId, statType);
        int actual = stat.compareWithTimeString(inDate);
        int expected = 0;

        assertEquals(expected, actual, "Check your date, fool!");
        assertFalse(stat.newerThan(inDate), "Incorrect boolean returned!");
        assertFalse(stat.olderThan(inDate), "Incorrect boolean returned!");

        inDate = "2018-03-19T00:15:00 UTC";
        actual = stat.compareWithTimeString(inDate);
        expected = -1;

        assertEquals(expected, actual, "Check your date, fool!");
        assertTrue(stat.olderThan(inDate), "Incorrect boolean returned!");

        inDate = "2018-02-19T00:10:00 UTC";
        actual = stat.compareWithTimeString(inDate);
        expected = 1;

        assertEquals(expected, actual, "Check your date, fool!");
        assertTrue(stat.newerThan(inDate), "Incorrect boolean returned!");
    }

    @Test
    public void utcDateTimeParserRejectsInvalidCalendarDates()
    {
        try
        {
            CsAbstractFile.parseUtcDateTime("2018-02-30T00:15:00 UTC");
            fail("Expected invalid date to be rejected.");
        }
        catch (ParseException expected)
        {
            assertNotNull(expected.getCause());
        }
    }

    /** Method to test toString() */
    @Test
    public void testToString()
    {
        StatMeasurement stat = new StatMeasurement(31, date, stationId, paramId, statType);
        String expected = "TEST MAX 31.0000 STATION 2018-02-19T00:15:00 UTC";
        String actual = stat.toString();

        assertEquals(expected, actual, "Check your string, fool!");
    }

    /** Method to test getDateTimeOfMeasurement() method */
    @Test
    public void testGetDateTimeOfMeasurement()
    {
        StatMeasurement stat = new StatMeasurement(31, date, stationId, paramId, statType);
        String actual = CsAbstractFile.formatUtcDateTime(stat.getDateTimeOfMeasurement());
        String expected = "2018-02-19T00:15:00 UTC";
        assertEquals(expected, actual, "Check your date, fool!");
    }

    /** Method to test compareTo() method */
    @Test
    public void compareToTest()
    {
        StatMeasurement stat = new StatMeasurement(31, date, stationId, paramId, statType);
        StatMeasurement stat2 = new StatMeasurement(65, date, stationId, paramId, statType);
        StatMeasurement stat3 = new StatMeasurement(23, date, stationId, paramId, statType);

        assertEquals(1, stat.compareTo(stat3), "Incorrect int returned");
        assertEquals(-1, stat.compareTo(stat2), "Incorrect int returned");
        assertEquals(0, stat.compareTo(stat), "Incorrect int returned");
    }
}
