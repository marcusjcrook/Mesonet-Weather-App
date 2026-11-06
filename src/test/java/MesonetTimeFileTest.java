import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A test class for MesonetTimeFile class.
 */
public class MesonetTimeFileTest
{
    /** a MesonetTimeFile to test */
    MesonetTimeFile testMesonetFile;

    /** a test file */
    File testFile;

    /**
     * the setup before running the tests. Initializes the variables
     * 
     * @throws Exception
     */
    @BeforeEach
    public void setUp() throws Exception
    {
        testFile = new File("Data/mesonet/20180102stil.mts");
        testFile.exists();
        testMesonetFile = new MesonetTimeFile("Data/mesonet/20180102stil.mts");
    }

    /**
     * method to test the MesonetTimeFile constructor. Also tests helper methods,
     * getters, and CsFile
     * 
     * @throws WrongCopyrightException
     * @throws IOException
     */
    @Test
    public void testMesonetTimeFileConstructor() throws IOException, WrongCopyrightException
    {
        testMesonetFile.parseFile();

        String actual = testMesonetFile.getStarDateTimeStringFromFile();
        String expected = "2018-01-02T00:00:00 UTC";
        System.out.println(actual);

        assertEquals(expected, actual, "Wrong date returned!");

        actual = testMesonetFile.getDateTimeString();
        expected = "2018-03-30T16:58:15 UTC";

        assertNotNull(actual, "No date returned!");
    }

    /** Method to test the CSAbstractFile toString method */
    @Test
    public void testCsAbstractFileToString() throws IOException, WrongCopyrightException
    {
        String expected = "FileName: Data/mesonet/20180102stil.mts";
        String actual = testMesonetFile.toString();
        assertEquals(expected, actual, "Wrong number returned!");
    }

    /**
     * Test method for IllegalArgumentException
     * 
     * @throws IOException
     * @throws WrongCopyrightException
     */
    @Test()
    public void missingFileIsReportedWithItsPath()
    {
        IOException exception = assertThrows(IOException.class,
                () -> new MesonetTimeFile("missing.mts").parseFile());
        assertTrue(exception.getMessage().contains("Data file does not exist or is not a regular file"));
        assertTrue(exception.getMessage().contains("missing.mts"));
    }

    @Test
    public void malformedDateHeaderHasFileAndLineContext() throws Exception
    {
        assertMalformedFile("101 test\n21 2018 02 30 00 00 00\nSTID TIME TAIR TA9M SRAD\n",
                ":2: invalid date header");
    }

    @Test
    public void missingHeadersAreReportedWithLineContext() throws Exception
    {
        assertMalformedFile("101 test\n", ":2: missing date header");
    }

    @Test
    public void headerOnlyFileReportsNoDataRowsAtEndOfFile() throws Exception
    {
        assertMalformedFile("101 test\n21 2018 01 02 00 00 00\nSTID TIME TAIR TA9M SRAD\n",
                ":4: no data rows after the headers");
    }

    @Test
    public void invalidCopyrightHasFileAndLineContext() throws Exception
    {
        Path path = Files.createTempFile("mesonet-copyright", ".mts");
        try
        {
            Files.writeString(path, "not a Mesonet file\n");
            try
            {
                new MesonetTimeFile(path.toString()).parseFile();
                fail("Expected invalid copyright to be rejected.");
            }
            catch (WrongCopyrightException ex)
            {
                assertTrue(ex.getMessage().contains(path.toString() + ":1:"), ex.getMessage());
            }
        }
        finally
        {
            Files.deleteIfExists(path);
        }
    }

    @Test
    public void missingRequiredParameterIsRejected() throws Exception
    {
        assertMalformedFile("101 test\n21 2018 01 02 00 00 00\nSTID TIME TAIR TA9M\n",
                ":3: parameter header must contain");
    }

    @Test
    public void malformedDataRowsHaveFileAndLineContext() throws Exception
    {
        assertMalformedFile("101 test\n21 2018 01 02 00 00 00\nSTID TIME TAIR TA9M SRAD\nTEST 0 1\n",
                ":4: data row has fewer columns");
        assertMalformedFile("101 test\n21 2018 01 02 00 00 00\nSTID TIME TAIR TA9M SRAD\nTEST 0 invalid 1 2\n",
                ":4: invalid data row: TAIR must be a number");
    }

    @Test
    public void parseFileClosesTemporaryFile() throws Exception
    {
        Path path = Files.createTempFile("mesonet-parse-close", ".mts");
        try
        {
            Files.writeString(path, "101 test\n21 2018 01 02 00 00 00\nSTID TIME TAIR TA9M SRAD\nTEST 0 1 2 3\n");
            MesonetTimeFile parser = new MesonetTimeFile(path.toString());
            assertEquals(1, parser.parseFile().size());
        }
        finally
        {
            Files.deleteIfExists(path);
        }
        assertFalse(Files.exists(path));
    }

    private void assertMalformedFile(String contents, String expectedMessage) throws Exception
    {
        Path path = Files.createTempFile("mesonet-malformed", ".mts");
        try
        {
            Files.writeString(path, contents);
            MesonetTimeFile parser = new MesonetTimeFile(path.toString());
            try
            {
                parser.parseFile();
                fail("Expected malformed input to be rejected.");
            }
            catch (IOException ex)
            {
                assertTrue(ex.getMessage().contains(path.toString()), ex.getMessage());
                assertTrue(ex.getMessage().contains(expectedMessage), ex.getMessage());
            }
        }
        finally
        {
            Files.deleteIfExists(path);
        }
    }
}
