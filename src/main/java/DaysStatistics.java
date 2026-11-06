import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;

/**
 * Class for assigning day data statistics. Creates ArrayLists for all the
 * maximum, minimum, and average, storing them in the lists. Provides
 * methods to find the maximum and minimum of a particular data (e.g. Tair
 * Minimum).
 */
public class DaysStatistics extends StatisticsAbstract
{
    /** an ArrayList that stores the files */
    private ArrayList<String> files;

    /** HashMap for the tairs */
    HashMap<String, EnumMap<StatType, ArrayList<StatMeasurement>>> tairList;

    /** HashMap for the ta9ms */
    HashMap<String, EnumMap<StatType, ArrayList<StatMeasurement>>> ta9mList;

    /** HashMap for the srads */
    HashMap<String, EnumMap<StatType, ArrayList<StatMeasurement>>> sradList;

    /**
     * Constructor for the DayStatistics class
     * 
     * @param files
     *            a string array of files
     */
    public DaysStatistics(String[] files)
    {
        // assign the 'files' variable to the argument passed in
        this.files = new ArrayList<String>(Arrays.asList(files));

        // initialize the tair fields
        tairList = new HashMap<String, EnumMap<StatType, ArrayList<StatMeasurement>>>();
        tairList.put("TAIR", new EnumMap<StatType, ArrayList<StatMeasurement>>(StatType.class));
        tairList.get("TAIR").put(StatType.MAX, new ArrayList<StatMeasurement>());
        tairList.get("TAIR").put(StatType.MIN, new ArrayList<StatMeasurement>());
        tairList.get("TAIR").put(StatType.AVG, new ArrayList<StatMeasurement>());

        // initialize the ta9m fields
        ta9mList = new HashMap<String, EnumMap<StatType, ArrayList<StatMeasurement>>>();
        ta9mList.put("TA9M", new EnumMap<StatType, ArrayList<StatMeasurement>>(StatType.class));
        ta9mList.get("TA9M").put(StatType.MAX, new ArrayList<StatMeasurement>());
        ta9mList.get("TA9M").put(StatType.MIN, new ArrayList<StatMeasurement>());
        ta9mList.get("TA9M").put(StatType.AVG, new ArrayList<StatMeasurement>());

        // initialize the srad fields
        sradList = new HashMap<String, EnumMap<StatType, ArrayList<StatMeasurement>>>();
        sradList.put("SRAD", new EnumMap<StatType, ArrayList<StatMeasurement>>(StatType.class));
        sradList.get("SRAD").put(StatType.MAX, new ArrayList<StatMeasurement>());
        sradList.get("SRAD").put(StatType.MIN, new ArrayList<StatMeasurement>());
        sradList.get("SRAD").put(StatType.AVG, new ArrayList<StatMeasurement>());
    }

    /**
     * Method for finding the statistics
     * 
     * @throws IOException
     * @throws WrongCopyrightException
     */
    public void findStatistics() throws IOException, WrongCopyrightException
    {
        for (String fileName : files)
        {
            MesonetTimeFile mtsFile = new MesonetTimeFile(fileName);
            ArrayList<TimeData> data = mtsFile.parseFile();
            DayDataStatistics dataStats = new DayDataStatistics(data);

            assignStats(dataStats);
        }
    }

    /**
     * Private helper method for assigning the stats to the relevant ArrayLists
     * 
     * @param dataStats
     *            a DayDataStatics Object
     */
    private void assignStats(DayDataStatistics dataStats)
    {
        // assign the stats to the appropriate ArrayList
        tairList.get("TAIR").get(StatType.MAX).add(dataStats.getStatMeasurement("Tair", StatType.MAX));
        tairList.get("TAIR").get(StatType.MIN).add(dataStats.getStatMeasurement("Tair", StatType.MIN));
        tairList.get("TAIR").get(StatType.AVG).add(dataStats.getStatMeasurement("Tair", StatType.AVG));

        ta9mList.get("TA9M").get(StatType.MAX).add(dataStats.getStatMeasurement("Ta9m", StatType.MAX));
        ta9mList.get("TA9M").get(StatType.MIN).add(dataStats.getStatMeasurement("Ta9m", StatType.MIN));
        ta9mList.get("TA9M").get(StatType.AVG).add(dataStats.getStatMeasurement("Ta9m", StatType.AVG));

        sradList.get("SRAD").get(StatType.MAX).add(dataStats.getStatMeasurement("Srad", StatType.MAX));
        sradList.get("SRAD").get(StatType.MIN).add(dataStats.getStatMeasurement("Srad", StatType.MIN));
        sradList.get("SRAD").get(StatType.AVG).add(dataStats.getStatMeasurement("Srad", StatType.AVG));
    }

    /**
     * Returns the daily average measurements for a parameter across the loaded files.
     *
     * @param inParamId the parameter ID
     * @return the daily average measurements
     * @throws WrongParameterIdException if the parameter ID is unsupported
     */
    public ArrayList<StatMeasurement> getAverageDays(String inParamId) throws WrongParameterIdException
    {
        return getDailyStatistics(inParamId, StatType.AVG);
    }

    private ArrayList<StatMeasurement> getDailyStatistics(String inParamId, StatType statType)
            throws WrongParameterIdException
    {
        if (inParamId.equalsIgnoreCase("tair"))
        {
            return new ArrayList<StatMeasurement>(tairList.get("TAIR").get(statType));
        }
        else if (inParamId.equalsIgnoreCase("ta9m"))
        {
            return new ArrayList<StatMeasurement>(ta9mList.get("TA9M").get(statType));
        }
        else if (inParamId.equalsIgnoreCase("srad"))
        {
            return new ArrayList<StatMeasurement>(sradList.get("SRAD").get(statType));
        }

        throw new WrongParameterIdException(inParamId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see StatisticsAbstract#getMinimumDay(java.lang.String)
     */
    /**
     * Method for getting the minimum statMeasurement according to day
     * 
     * @param inParamId
     *            the parameter ID for which to find the minimum (e.g. Tair)
     * @return StatMeasurement a StatMeasurement object representing the minimum air
     *         temperature day
     */
    @Override
    public StatMeasurement getMinimumDay(String inParamId) throws WrongParameterIdException
    {
        return getExtremeDay(inParamId, StatType.MIN);
    }

    /*
     * (non-Javadoc)
     * 
     * @see StatisticsAbstract#getMaximumDay(java.lang.String)
     */
    /**
     * Method for getting the maximum statMeasurement according to day
     * 
     * @param inParamId
     *            the parameter ID for which to find the maximum (e.g. Tair)
     * @return StatMeasurement a StatMeasurement object representing the maximum air
     *         temperature day
     */
    @Override
    public StatMeasurement getMaximumDay(String inParamId) throws WrongParameterIdException
    {
        return getExtremeDay(inParamId, StatType.MAX);
    }

    private StatMeasurement getExtremeDay(String inParamId, StatType statType) throws WrongParameterIdException
    {
        ArrayList<StatMeasurement> measurements = getDailyStatistics(inParamId, statType);
        if (measurements.isEmpty())
        {
            throw new IllegalStateException("No daily statistics are available for " + inParamId + ".");
        }

        StatMeasurement extreme = null;
        for (StatMeasurement measurement : measurements)
        {
            if (measurement.isValid()
                    && (extreme == null || (statType == StatType.MIN
                            ? measurement.compareTo(extreme) < 0
                            : measurement.compareTo(extreme) > 0)))
            {
                extreme = measurement;
            }
        }

        if (extreme != null)
        {
            return extreme;
        }
        return measurements.get(0);
    }

    /**
     * Method for combining the max and min statistics and printing out a string
     * 
     * @param paramId
     *            the relevant parameter ID
     * @return String string showing the maximum and minimum temperatures
     * @throws WrongParameterIdException
     */
    public String combineMinMaxStatistics(String paramId) throws WrongParameterIdException
    {
        StatMeasurement maximumDay = getMaximumDay(paramId);
        StatMeasurement miniumumDay = getMinimumDay(paramId);
        return maximumDay.value + "\n" + miniumumDay.value + "\n";
    }

    /**
     * toString method for providing descriptive string of DayData objects
     * 
     * @return String a descriptive string
     */
    public String toString()
    {
        String maxTair = "";
        String minTair = "";
        String maxTa9m = "";
        String minTa9m = "";
        String maxSrad = "";
        String minSrad = "";
        try
        {
            maxTair = getMaximumDay("TAIR").toString();
            minTair = getMinimumDay("TAIR").toString();
            maxTa9m = getMaximumDay("TA9M").toString();
            minTa9m = getMinimumDay("TA9M").toString();
            maxSrad = getMaximumDay("SRAD").toString();
            minSrad = getMinimumDay("SRAD").toString();
        }
        catch (WrongParameterIdException e)
        {
            System.out.print(e.getMessage());
        }

        return String.format("%s\n%s\n%s\n%s\n%s\n%s", maxTair, minTair, maxTa9m, minTa9m, maxSrad, minSrad);
    }
}