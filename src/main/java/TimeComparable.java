import java.text.ParseException;

/**
 * An interface specifying the newerThan and olderThan methods
 */
public interface TimeComparable
{
    /**
     * the newerThan method (see implementing classes)
     * 
     * @param inDateTimeStr
     *            the date string to check against
     * @return boolean whether this is newer than inDateTimeStr
     * @throws ParseException
     */
    boolean newerThan(String inDateTimeStr) throws ParseException;

    /**
     * the olderThan method (see implementing classes)
     * 
     * @param inDateTimeStr
     *            the date string to check against
     * @return boolean whether this is newer than inDateTimeStr
     * @throws ParseException
     */
    boolean olderThan(String inDateTimeStr) throws ParseException;
}