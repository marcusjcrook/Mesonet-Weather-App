import java.text.ParseException;
import java.time.Instant;

/**
 * A class for holding statistic measurement objects. Provides methods for
 * creating and comparing temperatures and/or dates.
 */
public class StatMeasurement extends Measurement implements TimeComparable, Comparable<StatMeasurement>
{
    /** a variable for holding the date and time of measurement */
    private Instant dateTimeOfMeasurement = Instant.EPOCH;

    /** a variable for holding the parameter Id */
    private String paramId;

    /** a variable for holding the stat type */
    private StatType statType;

    /** a variable for holding the station ID */
    private String stationId;

    /** a constant for use by the constructors */
    private static final String NADA = "nada";

    /**
     * constructor for the StatMeasurement class
     */
    public StatMeasurement()
    {
        super();
        paramId = NADA;
        stationId = NADA;
    }

    /**
     * constructor for StatMeasurement that takes in five arguments
     * 
     * @param inValue
     *            value of the measurement
     * 
     * @param obsDateTime
     *            the date and time of the observation
     * @param inStationId
     *            the station to check
     * @param inParamId
     *            the parameter to check
     * @param inStatType
     *            the statistic type
     */
    public StatMeasurement(double inValue, Instant obsDateTime, String inStationId, String inParamId,
            StatType inStatType)
    {
        super(inValue);
        dateTimeOfMeasurement = obsDateTime;
        stationId = inStationId;
        paramId = inParamId;
        statType = inStatType;
    }

    /**
     * method for getting the date and time of measurement
     * 
     * @return the UTC instant of the measurement
     */
    public Instant getDateTimeOfMeasurement()
    {
        return dateTimeOfMeasurement;
    }

    /**
     * method for setting the parameter
     * 
     * @param inParamId
     *            the parameter to set paramId
     */
    public void setParamId(String inParamId)
    {
        paramId = inParamId;
    }

    /**
     * method for getting the parameter ID
     * 
     * @return String a string representing the parameter ID
     */
    public String getParamId()
    {
        return paramId;
    }

    public String getStationId()
    {
        return stationId;
    }

    /**
     * method for setting the stat type
     * 
     * @param type
     *            the stat type
     */
    public void setStatType(StatType type)
    {
        statType = type;
    }

    /**
     * method for getting the stat type
     * 
     * @return StatType the stat type
     */
    public StatType getStatType()
    {
        return statType;
    }

    /**
     * Compare this Measurement with another Measurement
     * 
     * @param compareWith
     *            Measurement to compare with
     * @return true if both Measurements are valid AND this is strictly smaller than
     *         s OR if this is valid and s is not valid
     */
    public boolean isLessThan(StatMeasurement compareWith)
    {
        boolean valid;

        if ((this.isValid() && compareWith.isValid()) && this.getValue() < compareWith.getValue())
        {
            valid = true;
        }
        else if (this.isValid() && !compareWith.isValid())
        {
            valid = true;
        }
        else
        {
            valid = false;
        }

        return valid;
    }

    /**
     * Compare this Measurement with another Measurement
     * 
     * @param compareWith
     *            Measurement to compare with
     * @return true if both Measurements are valid AND this is strictly larger than
     *         s OR if this is valid and s is not valid
     */
    public boolean isGreaterThan(StatMeasurement compareWith)
    {
        boolean valid;

        if ((this.isValid() && compareWith.isValid()) && (this.getValue() > compareWith.getValue()))
        {
            valid = true;
        }
        else if (this.isValid() && !compareWith.isValid())
        {
            valid = true;
        }
        else
        {
            valid = false;
        }

        return valid;
    }

    /*
     * (non-Javadoc)
     * 
     * @see TimeComparable#newerThan(java.lang.String)
     */
    /**
     * Method for checking if this date is newer than inDateTime
     * 
     * @param inDateTime
     *            a string representation of the date and time to compare
     * 
     * @return boolean a boolean representing whether this is newer than inDateTime
     */
    public boolean newerThan(String inDateTime) throws ParseException
    {
        return compareWithTimeString(inDateTime) > 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see TimeComparable#olderThan(java.lang.String)
     */
    /**
     * Method for checking if this date is older than inDateTime
     * 
     * @param inDateTime
     *            a string representation of the date and time to compare
     * 
     * @return boolean a boolean representing whether this is older than inDateTime
     */
    public boolean olderThan(String inDateTime) throws ParseException
    {
        return compareWithTimeString(inDateTime) < 0;
    }

    /**
     * Override CompareWithTimeString method for comparing two dates
     * 
     * @param inDateTime
     *            a string representation of the date and time to compare
     * 
     * @return int an int representing the temporal order of the dates, returning 1
     *         if newer, -1 if older, and zero if equal
     * @throws ParseException
     */
    public int compareWithTimeString(String inDateTime) throws ParseException
    {
        return dateTimeOfMeasurement.compareTo(CsAbstractFile.parseUtcDateTime(inDateTime));
    }

    /*
     * (non-Javadoc)
     * 
     * @see Measurement#toString()
     */
    /**
     * toString override for StatMeasurement class
     * 
     * @return String a descriptive string
     */
    @Override
    public String toString()
    {
        String date = CsAbstractFile.formatUtcDateTime(dateTimeOfMeasurement);
        return String.format("%s %s %s %s %s", paramId.toUpperCase(), this.statType, super.toString(),
                stationId.toUpperCase(), date);
    }

    @Override
    public int compareTo(StatMeasurement stat)
    {
        if (this.value > stat.value)
        {
            return 1;
        }
        else if (this.value < stat.value)
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }
}