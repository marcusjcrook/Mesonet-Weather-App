import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;

/**
 * Class for assigning day data statistics. Creates ArrayLists for all the
 * maximum, minimum, average, and totals, storing them in the lists. Provides
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
        sradList.get("SRAD").put(StatType.TOT, new ArrayList<StatMeasurement>());
    }

    /**
     * Method for finding the statistics
     * 
     * @throws IOException
     * @throws WrongCopyrightException
     * @throws ParseException
     */
    public void findStatistics() throws IOException, WrongCopyrightException, ParseException
    {
        for (String fileName : files)
        {
            MesonetTimeFile mtsFile = new MesonetTimeFile(fileName);
            mtsFile.parseFile();
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
     * @throws ParseException
     */
    private void assignStats(DayDataStatistics dataStats) throws ParseException
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
        sradList.get("SRAD").get(StatType.AVG).add(dataStats.getStatMeasurement("Srad", StatType.TOT));
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
        if (inParamId.equalsIgnoreCase("tair"))
        {
            return Collections.min(tairList.get("TAIR").get(StatType.MIN));
        }
        else if (inParamId.equalsIgnoreCase("ta9m"))
        {
            return Collections.min(ta9mList.get("TA9M").get(StatType.MIN));
        }
        else if (inParamId.equalsIgnoreCase("srad"))
        {
            return Collections.min(sradList.get("SRAD").get(StatType.MIN));
        }
        else
        {
            String msg = String.format("%s", inParamId);
            throw new WrongParameterIdException(msg);
        }
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
        if (inParamId.equalsIgnoreCase("tair"))
        {
            return Collections.max(tairList.get("TAIR").get(StatType.MAX));
        }
        else if (inParamId.equalsIgnoreCase("ta9m"))
        {
            return Collections.max(ta9mList.get("TA9M").get(StatType.MAX));
        }
        else if (inParamId.equalsIgnoreCase("srad"))
        {
            return Collections.max(sradList.get("SRAD").get(StatType.MAX));
        }
        else
        {
            String msg = String.format("%s", inParamId);
            throw new WrongParameterIdException(msg);
        }
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