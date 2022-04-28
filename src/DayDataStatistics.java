import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.GregorianCalendar;
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
        ArrayList<Double> timeDataVals = new ArrayList<Double>();

        // loop through data and add elements to ArrayList
        for (TimeData time : data)
        {
            timeDataVals.add(time.getMeasurement(paramName).getValue());
        }

        // find the average, sum, min, and max
        Double average = timeDataVals.stream().mapToDouble(val -> val).average().getAsDouble();
        Double sum = timeDataVals.stream().mapToDouble(val -> val).sum();
        Double max = timeDataVals.stream().mapToDouble(val -> val).max().getAsDouble();
        Double min = timeDataVals.stream().mapToDouble(val -> val).min().getAsDouble();

        // find the TimeData object in order to get the time
        TimeData maxTime = data.get(timeDataVals.indexOf(max));
        TimeData minTime = data.get(timeDataVals.indexOf(min));

        // create calendars for the StatMeasurement objects
        GregorianCalendar maxCal = new GregorianCalendar(maxTime.getYear(), maxTime.getMonth(), maxTime.getDay(),
                maxTime.getMeasurementDateTime().get(Calendar.HOUR_OF_DAY), maxTime.getMinute());
        GregorianCalendar minCal = new GregorianCalendar(minTime.getYear(), minTime.getMonth(), minTime.getDay(),
                minTime.getMeasurementDateTime().get(Calendar.HOUR_OF_DAY), minTime.getMinute());
        GregorianCalendar avgCal = new GregorianCalendar(maxTime.getYear(), maxTime.getMonth(), maxTime.getDay(), 0, 0,
                0);

        // create the StatMeasurement objects
        StatMeasurement maxStat = new StatMeasurement(max, maxCal, stationId, paramName, StatType.MAX);
        StatMeasurement minStat = new StatMeasurement(min, minCal, stationId, paramName, StatType.MIN);
        StatMeasurement avgStat = new StatMeasurement(average, avgCal, stationId, paramName, StatType.AVG);
        StatMeasurement sumStat = new StatMeasurement(sum, avgCal, stationId, paramName, StatType.TOT);

        // add the StatMeasurement objects to the HashMap
        paramStats.get(paramName).put(StatType.MAX, maxStat);
        paramStats.get(paramName).put(StatType.MIN, minStat);
        paramStats.get(paramName).put(StatType.AVG, avgStat);
        paramStats.get(paramName).put(StatType.TOT, sumStat);
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
        double tairMinValue = paramStats.get("TAIR").get(StatType.MIN).getValue();
        double tairMaxValue = paramStats.get("TAIR").get(StatType.MAX).getValue();
        double tairAvgValue = paramStats.get("TAIR").get(StatType.AVG).getValue();

        double ta9mMinValue = paramStats.get("TA9M").get(StatType.MIN).getValue();
        double ta9mMaxValue = paramStats.get("TA9M").get(StatType.MAX).getValue();
        double ta9mAvgValue = paramStats.get("TA9M").get(StatType.AVG).getValue();

        double sradMinValue = paramStats.get("SRAD").get(StatType.MIN).getValue();
        double sradMaxValue = paramStats.get("SRAD").get(StatType.MAX).getValue();
        double sradAvgValue = paramStats.get("SRAD").get(StatType.AVG).getValue();
        double sradTotValue = paramStats.get("SRAD").get(StatType.TOT).getValue();

        return String.format("%d-%02d-%02d, %s:\n Air Temperature[1.5m] = [%.4f, %.4f, %.4f],\n"
                + " Air Temperature[9m] = [%.4f, %.4f, %.4f],\n" + " Solar Radiation = [%.4f, %.4f, %.4f, %.4f]",
                data.get(0).getYear(), data.get(0).getMonth(), data.get(0).getDay(), stationId.toUpperCase(),
                tairMinValue, tairAvgValue, tairMaxValue, ta9mMinValue, ta9mAvgValue, ta9mMaxValue, sradMinValue,
                sradAvgValue, sradMaxValue, sradTotValue);
    }
}