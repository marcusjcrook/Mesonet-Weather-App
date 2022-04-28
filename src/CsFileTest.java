import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for CsFile class
 */
public class CsFileTest
{
    /** Method to test the CSFile class */
    @Test
    public void testCSFile() throws ParseException
    {
        CsFile csFileTest = new CsFile("data/mesonet/20180102stil.mts");
        String actual = csFileTest.getFileName();
        String expected = "data/mesonet/20180102stil.mts";

        Assert.assertEquals("Wrong file name returned!", expected, actual);

        String dateString = "2017-02-19T00:00:00 UTC";
        int compare = csFileTest.compareWithTimeString(dateString);
        int expectedInt = 1;
        boolean compareOlder = csFileTest.olderThan(dateString);
        boolean compareNewer = csFileTest.newerThan(dateString);

        Assert.assertEquals("Incorrect number returned!", expectedInt, compare);
        Assert.assertFalse(compareOlder);
        Assert.assertTrue(compareNewer);

        dateString = "2018-02-19T21:47:36 UTC";
        compare = csFileTest.compareWithTimeString(dateString);
        expectedInt = 0;
        compareOlder = csFileTest.olderThan(dateString);
        compareNewer = csFileTest.newerThan(dateString);

        Assert.assertEquals("Incorrect number returned", expectedInt, compare);
        Assert.assertFalse(compareOlder);
        Assert.assertFalse(compareNewer);

        dateString = "2018-02-19T21:47:55 UTC";
        compare = csFileTest.compareWithTimeString(dateString);
        expectedInt = -1;
        compareOlder = csFileTest.olderThan(dateString);
        compareNewer = csFileTest.newerThan(dateString);

        Assert.assertEquals("Incorrect number returned!", expectedInt, compare);
        Assert.assertTrue(compareOlder);
        Assert.assertFalse(compareNewer);

        csFileTest = new CsFile("data/mesonet/40007035stil.mts");
        Assert.assertFalse(csFileTest.exists());
    }
}
