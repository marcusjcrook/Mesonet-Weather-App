import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A class for holding statistic measurement objects. Provides methods for
 * creating and comparing temperatures and/or dates.
 */
public class StatMeasurement extends Measurement implements TimeComparable, Comparable<StatMeasurement>
{
    /** a variable for holding the date and time of measurement */
    private GregorianCalendar dateTimeOfMeasurment;

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
    public StatMeasurement(double inValue, GregorianCalendar obsDateTime, String inStationId, String inParamId,
            StatType inStatType)
    {
        super(inValue);

        dateTimeOfMeasurment = new GregorianCalendar(0, 0, 0);
        int year = obsDateTime.get(Calendar.YEAR);
        int month = obsDateTime.get(Calendar.MONTH) - 1;
        int day = obsDateTime.get(Calendar.DAY_OF_MONTH);
        int hour = obsDateTime.get(Calendar.HOUR_OF_DAY);
        int minute = obsDateTime.get(Calendar.MINUTE);

        dateTimeOfMeasurment.set(year, month, day, hour, minute, 0);
        dateTimeOfMeasurment.setTimeZone(TimeZone.getTimeZone("UTC"));
        stationId = inStationId;
        paramId = inParamId;
        statType = inStatType;
    }

    /**
     * method for getting the date and time of measurement
     * 
     * @return GregorianCalendar a calendar object holding the date and time of
     *         measurement
     */
    public GregorianCalendar getDateTimeOfMeasurment()
    {
        return dateTimeOfMeasurment;
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
        return compareWithTimeString(inDateTime) == 1 ? true : false;
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
        return compareWithTimeString(inDateTime) == -1 ? true : false;
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
        DateFormat format = new SimpleDateFormat(CsAbstractFile.dateTimeFormat);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date dateString = format.parse(inDateTime);
        int value = dateTimeOfMeasurment.getTime().compareTo(dateString);

        if (value > 0)
        {
            return 1;
        }
        else if (value < 0)
        {
            return -1;
        }
        else
        {
            return 0;
        }
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
        DateFormat format = new SimpleDateFormat(CsAbstractFile.dateTimeFormat);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = format.format(dateTimeOfMeasurment.getTime());
        return String.format("%s %s %.4f %s %s", paramId.toUpperCase(), this.statType, this.value,
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