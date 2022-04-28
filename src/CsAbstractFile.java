import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Abstract File class
 */
public abstract class CsAbstractFile implements TimeComparable
{
    /** A File object */
    protected File file;

    /** a DateFormat object */
    protected DateFormat dateFormat;

    /** time format used for strings */
    protected static String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss z";

    /** a String representing the file name */
    protected String fileName;

    /**
     * Constructor for CsAbstractFile that assigns argument to field
     * 
     * @param inFileName
     *            the name of the file to store in the field
     */
    protected CsAbstractFile(String inFileName)
    {
        // assign inFileName to a the fileName field
        fileName = inFileName;
        dateFormat = new SimpleDateFormat(dateTimeFormat);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        file = new File(inFileName);
    }

    /**
     * Method to check whether file exists
     * 
     * @return boolean a boolean indicated whether the file exists
     */
    public boolean exists()
    {
        // check whether the file exists
        return file.exists();
    }

    /**
     * Method to check when the file was last modified
     * 
     * @return long the date the file was last modified in long form
     */
    public long getDateModified()
    {
        // check when the file was last modified
        return file.lastModified();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    /**
     * toString override method for CsAbstractFile
     * 
     * @return String a string representing the file name
     */
    @Override
    public String toString()
    {
        // return descriptive string
        return String.format("FileName: %s", fileName);
    }

    /**
     * Abstract method comparing two dates
     * 
     * @param inDateTimeStr
     *            the string to compare
     * @return int an int value indicated relative position according to time
     * @throws ParseException
     */
    public abstract int compareWithTimeString(String inDateTimeStr) throws ParseException;
}
