import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Class that takes a Mesonet Time File and parses the data contained
 * within. Provides methods to parse the file, date, and headers, and
 * provides a method to check the copyright.
 */
public class MesonetTimeFile extends CsFile
{
    /** an ArrayList of TimeData objects */
    private ArrayList<TimeData> data = new ArrayList<TimeData>();

    /** an ArrayList for holding the paramIds */
    private ArrayList<String> paramIds = new ArrayList<String>();

    /**
     * a HeaderDateTime object to hold information about the time and date of the
     * file.
     */
    private HeaderDateTime headerDateTime;

    /** constant for year of file */
    private static final int YEAR = 1;

    /** constant for month of file */
    private static final int MONTH = 2;

    /** constant for day of file */
    private static final int DAY = 3;

    /** constant for hour of file */
    private static final int HOUR = 4;

    /** constant for minute of file */
    private static final int MINUTE = 5;

    /** constant for second of file */
    private static final int SECOND = 6;

    /** constant for tair parameter id */
    private static final String TAIR = "TAIR";

    /** constant for ta9m parameter id */
    private static final String TA9M = "TA9M";

    /** constant for srad parameter id */
    private static final String SRAD = "SRAD";

    /** constant for time parameter id */
    private static final String TIME = "TIME";

    /** constant for station id parameter id */
    private static final String STID = "STID";

    /**
     * variable representing the location of the tair measurement in the Array(List)
     */
    private int tairPosition = -1;

    /**
     * variable representing the location of the ta9m measurement in the Array(List)
     */
    private int ta9mPosition = -1;

    /**
     * variable representing the location of the srad measurement in the Array(List)
     */
    private int sradPosition = -1;

    /** variable representing the location of the minute in the Array(List) */
    private int minutePosition = -1;

    /** variable representing the location of the station id in the Array(List) */
    private int stidPosition = -1;

    /** GregorianCalendar representing the date and time */
    private GregorianCalendar dateTime;

    /**
     * Class to hold the date and time of the header.
     */
    class HeaderDateTime
    {
        /**
         * int representing the year of the file
         */
        public int year;

        /**
         * int representing the month of the file
         */
        public int month;

        /**
         * int representing the day of the file
         */
        public int day;

        /**
         * int representing the minute of the file
         */
        public int minute;

        /**
         * Constructor for the HeaderDateTime class
         * 
         * @param inYear
         *            the year of the file
         * @param inMonth
         *            the month of the file
         * @param inDay
         *            the day of the file
         * @param inMinute
         *            the minute of the file
         */
        HeaderDateTime(int inYear, int inMonth, int inDay, int inMinute)
        {
            // assign arguments to fields
            year = inYear;
            month = inMonth;
            day = inDay;
            minute = inMinute;
        }
    }

    /**
     * Constructor for MesonetTimeFile. Invokes super constructor.
     * 
     * @param inFileName
     *            the name of the file
     * @throws IOException
     * @throws WrongCopyrightException
     */
    MesonetTimeFile(String inFileName) throws IOException, WrongCopyrightException
    {
        // call super constructor
        super(inFileName);
        dateTime = new GregorianCalendar();

        if (!file.exists())
        {
            String msg = String.format("[%s] %s", getDateTimeString(), "File " + inFileName + " does not exist!!!");
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Method that parses a file, adding the results to a TimeData ArrayList. Calls
     * the private helper methods below.
     * 
     * @return ArrayList<TimeData> an ArrayList resulting from parsing the file
     * @throws IOException
     * @throws WrongCopyrightException
     * @throws NumberFormatException
     */
    public ArrayList<TimeData> parseFile() throws IOException, WrongCopyrightException
    {
        BufferedReader br = null;
        String line = "";

        if (file.exists())
        {
            // Construct and use BufferedReader object to get relevant lines
            br = new BufferedReader(new FileReader(fileName));

            // read the first line to check copyright
            line = br.readLine();
            copyrightIsCorrect(line);

            // read the next line to parse date header
            line = br.readLine();
            parseDateTimeHeader(line);

            // read the next line to parse parameter header
            line = br.readLine();
            parseParamHeader(line);

            // only read in data while line is not null
            while (line != null)
            {
                // read in the line of data and parse it
                line = br.readLine();
                parseData(line);
            }
        }
        else
        {
            String msg = String.format("[%s] %s", getDateTimeString(), "File " + fileName + " does not exist!!!");
            throw new IllegalArgumentException(msg);
        }

        br.close();
        return data;
    }

    /**
     * Private helper method for parseFile method that adds values to an ArrayList
     * 
     * @param line
     *            the line to be parsed
     */
    private void parseData(String line)
    {
        if (line != null)
        {
            String[] input = line.trim().split("\\s+");

            // add the values to create a TimeData object and add it to the data ArrayList
            TimeData values = new TimeData(input[stidPosition], headerDateTime.year, headerDateTime.month,
                    headerDateTime.day, Integer.parseInt(input[minutePosition]),
                    new Measurement(Double.parseDouble(input[tairPosition])),
                    new Measurement(Double.parseDouble(input[ta9mPosition])),
                    new Measurement(Double.parseDouble(input[sradPosition])));
            data.add(values);
        }
    }

    /**
     * Private helper method for parseFile method that parses the header
     * 
     * @param inParamStr
     *            the string to parse
     */
    private void parseParamHeader(String inParamStr)
    {
        // create String array and split data by white spaces
        String[] splitArray = inParamStr.trim().split("\\s+");

        // loop through the Array and add the results to the paramIds ArrayList
        for (int index = 0; index < splitArray.length; ++index)
        {
            paramIds.add(splitArray[index]);
        }

        // set the positions, in the data, for the ArrayList
        minutePosition = paramIds.indexOf(TIME);
        tairPosition = paramIds.indexOf(TAIR);
        ta9mPosition = paramIds.indexOf(TA9M);
        sradPosition = paramIds.indexOf(SRAD);
        stidPosition = paramIds.indexOf(STID);
    }

    /**
     * Method to check the status of the copyright
     * 
     * @param inCopyrightStr
     *            the copyright in string form
     */
    protected void copyrightIsCorrect(String inCopyrightStr) throws WrongCopyrightException
    {
        // first line of copyright string must be 101 (found on discussion board)
        inCopyrightStr = inCopyrightStr.trim();
        String[] splitArray = inCopyrightStr.split("\\s+");

        // if the first line is not "101", throw WrongCopyrightException
        if (!splitArray[0].equals("101"))
        {
            throw new WrongCopyrightException();
        }
    }

    /**
     * Method to parse the date time header
     * 
     * @param inHeader
     *            a string representation of the date
     */
    void parseDateTimeHeader(String inHeader)
    {
        // split the string according to white space and add to an array
        String[] splitArray = inHeader.trim().split("\\s+");

        // set the values
        int year = Integer.valueOf(splitArray[YEAR]);
        int month = Integer.valueOf(splitArray[MONTH]);
        int day = Integer.valueOf(splitArray[DAY]);
        int hour = Integer.valueOf(splitArray[HOUR]);
        int minute = Integer.valueOf(splitArray[MINUTE]);
        int second = Integer.valueOf(splitArray[SECOND]);

        // call HeaderDateConstructor
        headerDateTime = new HeaderDateTime(year, month, day, minute);
        dateTime.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateTime.set(year, (month - 1), day, hour, minute, second);
    }

    /**
     * method to return the date and time in string form
     * 
     * @return String a string representing the date and time
     */
    String getStarDateTimeStringFromFile()
    {
        dateTime.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(dateTime.getTime());
    }

    /**
     * method to get the date and time to add to an error message
     * 
     * @return String a string the date and time
     */
    String getDateTimeString()
    {
        GregorianCalendar now = new GregorianCalendar();
        now.setTimeZone(TimeZone.getTimeZone("UTC"));

        return dateFormat.format(now.getTime());
    }
}