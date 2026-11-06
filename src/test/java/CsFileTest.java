import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test class for CsFile class
 */
public class CsFileTest
{
    /** Method to test the CSFile class */
    @Test
    public void testCSFile() throws IOException, ParseException
    {
        File testFile = File.createTempFile("mesonet-csfile-test", ".mts");
        try
        {
            long expectedModifiedTime = CsAbstractFile.parseUtcDateTime("2018-02-19T21:47:36 UTC").toEpochMilli();
            assertTrue(testFile.setLastModified(expectedModifiedTime), "Could not set test file modification time!");

            CsFile csFileTest = new CsFile(testFile.getAbsolutePath());
            String actual = csFileTest.getFileName();
            String expected = testFile.getAbsolutePath();

            assertEquals(expected, actual, "Wrong file name returned!");

            String dateString = "2017-02-19T00:00:00 UTC";
            int compare = csFileTest.compareWithTimeString(dateString);
            int expectedInt = 1;
            boolean compareOlder = csFileTest.olderThan(dateString);
            boolean compareNewer = csFileTest.newerThan(dateString);

            assertEquals(expectedInt, compare, "Incorrect number returned!");
            assertFalse(compareOlder);
            assertTrue(compareNewer);

            dateString = "2018-02-19T21:47:36 UTC";
            compare = csFileTest.compareWithTimeString(dateString);
            expectedInt = 0;
            compareOlder = csFileTest.olderThan(dateString);
            compareNewer = csFileTest.newerThan(dateString);

            assertEquals(expectedInt, compare, "Incorrect number returned");
            assertFalse(compareOlder);
            assertFalse(compareNewer);

            dateString = "2018-02-19T21:47:55 UTC";
            compare = csFileTest.compareWithTimeString(dateString);
            expectedInt = -1;
            compareOlder = csFileTest.olderThan(dateString);
            compareNewer = csFileTest.newerThan(dateString);

            assertEquals(expectedInt, compare, "Incorrect number returned!");
            assertTrue(compareOlder);
            assertFalse(compareNewer);

            csFileTest = new CsFile("Data/mesonet/40007035stil.mts");
            assertFalse(csFileTest.exists());
        }
        finally
        {
            Files.deleteIfExists(testFile.toPath());
        }
    }
}
