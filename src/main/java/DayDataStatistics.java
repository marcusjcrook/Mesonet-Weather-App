import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

/**
 * Class for finding and storing the maximum, minimum, and average temperatures
 * and solar radiation.
 */
public class DayDataStatistics
{
    /**
     * HashMap to hold parameter name and an EnumMap that maps StatType objects to a
     * StatMeasurement object
     */
    private HashMap<String, EnumMap<StatType, StatMeasurement>> paramStats;

    /** The set of data. */
    private ArrayList<TimeData> data;

    /** Station ID */
    private String stationId = "nada";

    /**
     * Constructor for the DayDataStatistics class. Calls private helper methods to
     * calculate the statistics.
     * 
     * @param inData
     *            an ArrayList of time data objects
     */
    public DayDataStatistics(ArrayList<TimeData> inData)
    {
        if (inData.isEmpty())
        {
            throw new IllegalArgumentException("Cannot calculate statistics without observations.");
        }

        data = inData;
        stationId = inData.get(0).getStationID();
        paramStats = new HashMap<String, EnumMap<StatType, StatMeasurement>>();
        paramStats.put("TAIR", new EnumMap<StatType, StatMeasurement>(StatType.class));
        paramStats.put("TA9M", new EnumMap<StatType, StatMeasurement>(StatType.class));
        paramStats.put("SRAD", new EnumMap<StatType, StatMeasurement>(StatType.class));

        calculateStatistics("TAIR");
        calculateStatistics("TA9M");
        calculateStatistics("SRAD");
    }

    /**
     * Method to calculate statistics
     * 
     * @param paramName
     *            the name of parameter to calculate
     */
    private void calculateStatistics(String paramName)
    {
        ArrayList<TimeData> validData = new ArrayList<TimeData>();
        ArrayList<Double> timeDataVals = new ArrayList<Double>();

        // Exclude missing or otherwise invalid observations from each statistic.
        for (TimeData time : data)
        {
            Measurement measurement = time.getMeasurement(paramName);
            if (measurement.isValid())
            {
                validData.add(time);
                timeDataVals.add(measurement.getValue());
            }
        }

        StatMeasurement maxStat;
        StatMeasurement minStat;
        StatMeasurement avgStat;
        if (validData.isEmpty())
        {
            Instant day = startOfDay(data.get(0));
            maxStat = new StatMeasurement(Double.NaN, day, stationId, paramName, StatType.MAX);
            minStat = new StatMeasurement(Double.NaN, day, stationId, paramName, StatType.MIN);
            avgStat = new StatMeasurement(Double.NaN, day, stationId, paramName, StatType.AVG);
        }
        else
        {
            double average = timeDataVals.stream().mapToDouble(Double::doubleValue).average().orElseThrow();
            double max = timeDataVals.stream().mapToDouble(Double::doubleValue).max().orElseThrow();
            double min = timeDataVals.stream().mapToDouble(Double::doubleValue).min().orElseThrow();

            TimeData maxTime = validData.get(timeDataVals.indexOf(max));
            TimeData minTime = validData.get(timeDataVals.indexOf(min));
            Instant maxTimeInstant = maxTime.getMeasurementDateTime();
            Instant minTimeInstant = minTime.getMeasurementDateTime();
            Instant avgTimeInstant = startOfDay(validData.get(0));

            maxStat = new StatMeasurement(max, maxTimeInstant, stationId, paramName, StatType.MAX);
            minStat = new StatMeasurement(min, minTimeInstant, stationId, paramName, StatType.MIN);
            avgStat = new StatMeasurement(average, avgTimeInstant, stationId, paramName, StatType.AVG);
        }

        // add the StatMeasurement objects to the HashMap
        paramStats.get(paramName).put(StatType.MAX, maxStat);
        paramStats.get(paramName).put(StatType.MIN, minStat);
        paramStats.get(paramName).put(StatType.AVG, avgStat);
    }

    private Instant startOfDay(TimeData time)
    {
        return time.getMeasurementDateTime().atOffset(ZoneOffset.UTC).toLocalDate().atStartOfDay()
                .toInstant(ZoneOffset.UTC);
    }

    /**
     * Method to get a particular StatMeasurement Object
     * 
     * @param parameter
     *            string representing name of parameter
     * @param stat
     *            a StatType object representing the StatType to be measured
     * @return StatMeasurement a StatMeasurement Object
     */
    public StatMeasurement getStatMeasurement(String parameter, StatType stat)
    {
        // Capitalize the parameter String for consistency
        parameter = parameter.toUpperCase();

        // return the appropriate StatMeasurement object
        return paramStats.get(parameter).get(stat);
    }

    /**
     * a Method to get the station ID
     * 
     * @return String a string representing the station ID
     */
    public String getStationID()
    {
        return stationId;
    }

    /**
     * toString method that describes DayStatistics
     * 
     * @return A string describing the statistics for the day
     */
    public String toString()
    {
        return String.format("%d-%02d-%02d, %s:\n Air Temperature[1.5m] = [%s, %s, %s],\n"
                + " Air Temperature[9m] = [%s, %s, %s],\n" + " Solar Radiation = [%s, %s, %s]",
                data.get(0).getYear(), data.get(0).getMonth(), data.get(0).getDay(), stationId.toUpperCase(),
                formatValue("TAIR", StatType.MIN), formatValue("TAIR", StatType.AVG),
                formatValue("TAIR", StatType.MAX), formatValue("TA9M", StatType.MIN),
                formatValue("TA9M", StatType.AVG), formatValue("TA9M", StatType.MAX),
                formatValue("SRAD", StatType.MIN), formatValue("SRAD", StatType.AVG),
                formatValue("SRAD", StatType.MAX));
    }

    private String formatValue(String parameter, StatType statType)
    {
        StatMeasurement measurement = paramStats.get(parameter).get(statType);
        return measurement.isValid() ? String.format("%.4f", measurement.getValue()) : "bad";
    }
}