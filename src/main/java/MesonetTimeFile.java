import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Locale;

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

    private LocalDate fileDate;

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

    private Instant dateTime;

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
        if (!Files.isRegularFile(file.toPath()))
        {
            throw new IOException("Data file does not exist or is not a regular file: " + fileName);
        }
        if (!Files.isReadable(file.toPath()))
        {
            throw new IOException("Data file is not readable. Check its permissions: " + fileName);
        }

        data.clear();
        paramIds.clear();
        dateTime = null;
        fileDate = null;
        int lineNumber = 1;
        try (BufferedReader br = openReader())
        {
            String line = br.readLine();
            if (line == null)
            {
                throw malformedFile(lineNumber, "missing copyright header");
            }
            try
            {
                copyrightIsCorrect(line);
            }
            catch (WrongCopyrightException ex)
            {
                throw new WrongCopyrightException(String.format("%s:%d: %s", fileName, lineNumber, ex.getMessage()));
            }

            lineNumber++;
            line = br.readLine();
            if (line == null)
            {
                throw malformedFile(lineNumber, "missing date header");
            }
            try
            {
                parseDateTimeHeader(line);
            }
            catch (IllegalArgumentException | DateTimeException ex)
            {
                throw malformedFile(lineNumber, "invalid date header", ex);
            }

            lineNumber++;
            line = br.readLine();
            if (line == null)
            {
                throw malformedFile(lineNumber, "missing parameter header");
            }
            parseParamHeader(line, lineNumber);

            while ((line = br.readLine()) != null)
            {
                lineNumber++;
                if (!line.isBlank())
                {
                    parseData(line, lineNumber);
                }
            }
        }

        if (data.isEmpty())
        {
            throw malformedFile(lineNumber + 1, "no data rows after the headers");
        }

        return data;
    }

    private BufferedReader openReader() throws IOException
    {
        try
        {
            return Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
        }
        catch (IOException ex)
        {
            throw new IOException("Could not open Mesonet data file '" + fileName + "': " + ex.getMessage(), ex);
        }
    }

    /**
     * Private helper method for parseFile method that adds values to an ArrayList
     * 
     * @param line
     *            the line to be parsed
     */
    private void parseData(String line, int lineNumber) throws IOException
    {
        String[] input = line.trim().split("\\s+");
        int requiredColumn = Math.max(Math.max(stidPosition, minutePosition),
                Math.max(Math.max(tairPosition, ta9mPosition), sradPosition));
        if (input.length <= requiredColumn)
        {
            throw malformedFile(lineNumber, "data row has fewer columns than its header");
        }

        try
        {
            String stationId = input[stidPosition];
            int minute = Integer.parseInt(input[minutePosition]);
            double tair = parseFiniteMeasurement(input[tairPosition], TAIR);
            double ta9m = parseFiniteMeasurement(input[ta9mPosition], TA9M);
            double srad = parseFiniteMeasurement(input[sradPosition], SRAD);
            if (minute < 0 || minute >= 24 * 60)
            {
                throw new IllegalArgumentException("TIME must be between 0 and 1439 minutes");
            }

            data.add(new TimeData(stationId, fileDate.getYear(), fileDate.getMonthValue(), fileDate.getDayOfMonth(),
                    minute, new Measurement(tair), new Measurement(ta9m), new Measurement(srad)));
        }
        catch (IllegalArgumentException ex)
        {
            throw malformedFile(lineNumber, "invalid data row: " + ex.getMessage(), ex);
        }
    }

    private double parseFiniteMeasurement(String value, String parameter)
    {
        double measurement;
        try
        {
            measurement = Double.parseDouble(value);
        }
        catch (NumberFormatException ex)
        {
            throw new IllegalArgumentException(parameter + " must be a number", ex);
        }
        if (!Double.isFinite(measurement))
        {
            throw new IllegalArgumentException(parameter + " must be finite");
        }
        return measurement;
    }

    /**
     * Private helper method for parseFile method that parses the header
     * 
     * @param inParamStr
     *            the string to parse
     */
    private void parseParamHeader(String inParamStr, int lineNumber) throws IOException
    {
        for (String parameter : inParamStr.trim().split("\\s+"))
        {
            paramIds.add(parameter.toUpperCase(Locale.ROOT));
        }

        minutePosition = paramIds.indexOf(TIME);
        tairPosition = paramIds.indexOf(TAIR);
        ta9mPosition = paramIds.indexOf(TA9M);
        sradPosition = paramIds.indexOf(SRAD);
        stidPosition = paramIds.indexOf(STID);
        if (minutePosition < 0 || tairPosition < 0 || ta9mPosition < 0 || sradPosition < 0 || stidPosition < 0)
        {
            throw malformedFile(lineNumber, "parameter header must contain STID, TIME, TAIR, TA9M, and SRAD");
        }
        if (paramIds.lastIndexOf(TIME) != minutePosition || paramIds.lastIndexOf(TAIR) != tairPosition
                || paramIds.lastIndexOf(TA9M) != ta9mPosition || paramIds.lastIndexOf(SRAD) != sradPosition
                || paramIds.lastIndexOf(STID) != stidPosition)
        {
            throw malformedFile(lineNumber, "parameter header contains duplicate required columns");
        }
    }

    /**
     * Method to check the status of the copyright
     * 
     * @param inCopyrightStr
     *            the copyright in string form
     */
    protected void copyrightIsCorrect(String inCopyrightStr) throws WrongCopyrightException
    {
        if (inCopyrightStr == null)
        {
            throw new WrongCopyrightException();
        }
        inCopyrightStr = inCopyrightStr.trim();
        String[] splitArray = inCopyrightStr.split("\\s+");

        if (splitArray.length == 0 || !splitArray[0].equals("101"))
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
        String[] splitArray = inHeader.trim().split("\\s+");
        if (splitArray.length < 7)
        {
            throw new IllegalArgumentException("date header must contain year, month, day, hour, minute, and second");
        }

        int year = Integer.parseInt(splitArray[YEAR]);
        int month = Integer.parseInt(splitArray[MONTH]);
        int day = Integer.parseInt(splitArray[DAY]);
        int hour = Integer.parseInt(splitArray[HOUR]);
        int minute = Integer.parseInt(splitArray[MINUTE]);
        int second = Integer.parseInt(splitArray[SECOND]);

        LocalDateTime headerTimestamp = LocalDateTime.of(year, month, day, hour, minute, second);
        dateTime = headerTimestamp.toInstant(ZoneOffset.UTC);
        fileDate = headerTimestamp.toLocalDate();
    }

    /**
     * method to return the date and time in string form
     * 
     * @return String a string representing the date and time
     */
    String getStarDateTimeStringFromFile()
    {
        if (dateTime == null)
        {
            throw new IllegalStateException("File date and time has not been parsed.");
        }
        return formatUtcDateTime(dateTime);
    }

    /**
     * method to get the date and time to add to an error message
     * 
     * @return String a string the date and time
     */
    String getDateTimeString()
    {
        return formatUtcDateTime(Instant.now());
    }

    private IOException malformedFile(int lineNumber, String problem)
    {
        return malformedFile(lineNumber, problem, null);
    }

    private IOException malformedFile(int lineNumber, String problem, Exception cause)
    {
        return new IOException(String.format("%s:%d: %s", fileName, lineNumber, problem), cause);
    }
}