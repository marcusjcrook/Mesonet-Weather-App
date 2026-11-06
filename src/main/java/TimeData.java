import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This class represents a summary of one time's data from a single
 * Mesonet station.
 */
public class TimeData
{
    private final Instant measurementDateTime;
    private final String stationID;
    private final Map<String, Measurement> measurements = new HashMap<>();

    /**
     * Creates a measurement at the given UTC date and minute of day.
     */
    public TimeData(String stationID, int year, int month, int day, int minute, Measurement tair, Measurement ta9m,
            Measurement solarRadiation)
    {
        this(stationID, atUtcMinute(year, month, day, minute), tair, ta9m, solarRadiation);
    }

    /**
     * Creates a measurement at the given UTC instant.
     */
    public TimeData(String stationID, Instant dateTime, Measurement tair, Measurement ta9m,
            Measurement solarRadiation)
    {
        this.stationID = stationID;
        measurementDateTime = dateTime;
        measurements.put("TAIR", tair);
        measurements.put("TA9M", ta9m);
        measurements.put("SRAD", solarRadiation);
    }

    private static Instant atUtcMinute(int year, int month, int day, int minute)
    {
        if (minute < 0 || minute >= 24 * 60)
        {
            throw new IllegalArgumentException("minute must be between 0 and 1439");
        }
        return LocalDate.of(year, month, day).atStartOfDay().plusMinutes(minute).toInstant(ZoneOffset.UTC);
    }

    public Instant getMeasurementDateTime()
    {
        return measurementDateTime;
    }

    public Measurement getMeasurement(String param)
    {
        return measurements.get(param.toUpperCase(Locale.ROOT));
    }

    public String getStationID()
    {
        return stationID;
    }

    public int getMinute()
    {
        return measurementDateTime.atOffset(ZoneOffset.UTC).getMinute();
    }

    public int getMonth()
    {
        return measurementDateTime.atOffset(ZoneOffset.UTC).getMonthValue();
    }

    public int getDay()
    {
        return measurementDateTime.atOffset(ZoneOffset.UTC).getDayOfMonth();
    }

    public int getYear()
    {
        return measurementDateTime.atOffset(ZoneOffset.UTC).getYear();
    }
}
