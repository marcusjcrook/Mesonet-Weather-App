import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * This class represents a summary of one time's data from a single
 * Mesonet station.
 */
public class TimeData
{
    /** a GregorianCalendar holding the measurement date and time in UTC format */
    private GregorianCalendar measurementDateTimeUTC;

    /** a String representing the station ID */
    private String stationID = "nada";

    /** HashMap to hold the Measurement objects */
    HashMap<String, Measurement> measurements = new HashMap<String, Measurement>();

    /**
     * constructor for the time data class. Calls private helper methods.
     * 
     * @param stationID
     *            the station ID
     * @param year
     *            the year of measurement
     * @param month
     *            the month of measurement
     * @param day
     *            the day of measurement
     * @param minute
     *            the minute of measurement
     * @param tair
     *            the tair measurement
     * @param ta9m
     *            the ta9m measurement
     * @param solarRadiation
     *            the solar radiation measurement
     */
    public TimeData(String stationID, int year, int month, int day, int minute, Measurement tair, Measurement ta9m,
            Measurement solarRadiation)
    {
        // initialize the data
        measurementDateTimeUTC = new GregorianCalendar();
        measurementDateTimeUTC.setTimeZone(TimeZone.getTimeZone("UTC"));

        this.stationID = stationID;

        // call the setDateTime method
        setDateTimeComponents(year, month, day, minute);

        // call the setMeasurements method
        setMeasurements(tair, ta9m, solarRadiation);
    }

    /**
     * a constructor for the TimeData class that takes a GregorianCalendar and
     * extracts the relevant data
     * 
     * @param inStationID
     *            the station ID
     * @param dateTime
     *            the date and time
     * @param tair
     *            the tair measurement object
     * @param ta9m
     *            the ta9m measurement object
     * @param solarRadiation
     *            the solar radiation measurement
     */
    public TimeData(String inStationID, GregorianCalendar dateTime, Measurement tair, Measurement ta9m,
            Measurement solarRadiation) throws WrongTimeZoneException
    {
        // Initialize the data
        measurementDateTimeUTC = new GregorianCalendar(0, 0, 0);
        measurementDateTimeUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        stationID = inStationID;

        // call the setMeasurements method
        setMeasurements(tair, ta9m, solarRadiation);

        int year = dateTime.get(Calendar.YEAR);
        int month = dateTime.get(Calendar.MONTH);
        int day = dateTime.get(Calendar.DAY_OF_MONTH);
        int minute = dateTime.get(Calendar.MINUTE);

        // if time zone is correct, call the SetDateTime method, else throw exception
        if (!dateTime.getTimeZone().equals(TimeZone.getTimeZone("UTC")))
        {
            throw new WrongTimeZoneException();
        }
        else
        {
            setDateTimeComponents(year, month, day, minute);
        }
    }

    /**
     * method for getting the date and time of measurement
     * 
     * @return GregorianCalendar representing the date and time
     */
    public GregorianCalendar getMeasurementDateTime()
    {
        return measurementDateTimeUTC;
    }

    /**
     * private helper method for setting the date and time components
     * 
     * @param year
     *            the year of measurement
     * @param month
     *            the month of measurement
     * @param day
     *            the day of measurement
     * @param minute
     *            the minute of measurement
     */
    private void setDateTimeComponents(int year, int month, int day, int minute)
    {
        measurementDateTimeUTC.set(year, (month - 1), day, 0, minute, 0);
    }

    /**
     * private helper method for setting the measurements
     * 
     * @param inTair
     *            the tair measurement
     * @param inTa9m
     *            the ta9m measurement
     * @param inSolarRadiation
     *            the solar radiation measurement
     */
    private void setMeasurements(Measurement inTair, Measurement inTa9m, Measurement inSolarRadiation)
    {
        measurements.put("TAIR", inTair);
        measurements.put("TA9M", inTa9m);
        measurements.put("SRAD", inSolarRadiation);
    }

    public Measurement getMeasurement(String param)
    {
        param = param.toUpperCase();
        return measurements.get(param);
    }

    /**
     * method for getting the station ID
     * 
     * @return String a string representing the station ID
     */
    public String getStationID()
    {
        return stationID;
    }
    
    /**
     * method for getting the minute of measurement
     * 
     * @return int an int representing the minute
     */
    public int getMinute()
    {
        return measurementDateTimeUTC.get(Calendar.MINUTE);
    }

    /**
     * method for getting the month of measurement
     * 
     * @return int an int representing the month
     */
    public int getMonth()
    {
        return measurementDateTimeUTC.get(Calendar.MONTH) + 1;
    }

    /**
     * method for getting the day of measurement
     * 
     * @return int an int representing the day of month
     */
    public int getDay()
    {
        return measurementDateTimeUTC.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * method for getting the year of measurement
     * 
     * @return int an int representing the year
     */
    public int getYear()
    {
        return measurementDateTimeUTC.get(Calendar.YEAR);
    }
}