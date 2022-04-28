import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Class that extends CsAbstractFile. Provides methods to compare files
 * according to date
 */
public class CsFile extends CsAbstractFile
{
    /**
     * Constructor for CsFile class
     * 
     * @param inFileName
     *            the name of the file
     */
    public CsFile(String inFileName)
    {
        super(inFileName);
    }

    /**
     * Method to get the file name
     * 
     * @return String the file name in string form
     */
    public String getFileName()
    {
        return fileName;
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

    /*
     * (non-Javadoc)
     * 
     * @see CsAbstractFile#compareTo(java.lang.String)
     */
    /**
     * Override CompareWithTimeString method for comparing two dates
     * 
     * @param inDateTime
     *            a string representation of the date and time to compare
     * 
     * @return int an int representing the temporal order of the dates, returning 1
     *         if newer, -1 if older, or zero if equal
     * @throws ParseException
     */
    @Override
    public int compareWithTimeString(String inDateTime) throws ParseException
    {
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date stringToDate = dateFormat.parse(inDateTime);
        Calendar cal = new GregorianCalendar();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(this.getDateModified());

        int value = cal.getTime().compareTo(stringToDate);

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
}