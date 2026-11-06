import java.io.File;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Abstract File class
 */
public abstract class CsAbstractFile implements TimeComparable
{
    /** A File object */
    protected File file;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("uuuu-MM-dd'T'HH:mm:ss 'UTC'", Locale.ROOT)
            .withResolverStyle(ResolverStyle.STRICT);

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
        file = new File(inFileName);
    }

    static Instant parseUtcDateTime(String dateTime) throws ParseException
    {
        try
        {
            return LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER).toInstant(ZoneOffset.UTC);
        }
        catch (DateTimeParseException ex)
        {
            ParseException parseException = new ParseException(ex.getMessage(), ex.getErrorIndex());
            parseException.initCause(ex);
            throw parseException;
        }
    }

    static String formatUtcDateTime(Instant dateTime)
    {
        return DATE_TIME_FORMATTER.withZone(ZoneOffset.UTC).format(dateTime);
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
