import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test class for TimeData
 */
public class TimeDataTest
{
    @Test
    public void constructorStoresMeasurementsAtTheSpecifiedUtcMinute()
    {
        TimeData time = new TimeData("test", 2018, 3, 3, 15, new Measurement(5), new Measurement(7),
                new Measurement(9));

        assertEquals("test", time.getStationID());
        assertEquals(2018, time.getYear());
        assertEquals(3, time.getMonth());
        assertEquals(3, time.getDay());
        assertEquals(15, time.getMinute());
        assertEquals(Instant.parse("2018-03-03T00:15:00Z"), time.getMeasurementDateTime());
        assertEquals(5.0, time.getMeasurement("TAIR").getValue(), 0.001);
        assertEquals(7.0, time.getMeasurement("TA9M").getValue(), 0.001);
        assertEquals(9.0, time.getMeasurement("SRAD").getValue(), 0.001);
    }

    @Test
    public void constructorAcceptsAnInstant()
    {
        Instant dateTime = Instant.parse("2016-11-03T00:20:00Z");
        TimeData time = new TimeData("test2", dateTime, new Measurement(11), new Measurement(13),
                new Measurement(15));

        assertEquals(dateTime, time.getMeasurementDateTime());
        assertEquals(2016, time.getYear());
        assertEquals(11, time.getMonth());
        assertEquals(3, time.getDay());
        assertEquals(20, time.getMinute());
    }

    @Test
    public void dateAndTimeFormattingIsExplicitlyUtc()
    {
        TimeData time = new TimeData("test", 2018, 2, 12, 15, new Measurement(5), new Measurement(7),
                new Measurement(9));

        assertEquals("2018-02-12T00:15:00 UTC",
                CsAbstractFile.formatUtcDateTime(time.getMeasurementDateTime()));
    }

    @Test
    public void constructorRejectsMinuteOutsideTheDay()
    {
        assertThrows(IllegalArgumentException.class,
                () -> new TimeData("test", 2018, 2, 12, 1440, new Measurement(5), new Measurement(7),
                        new Measurement(9)));
    }
}
