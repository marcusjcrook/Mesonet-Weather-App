import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
    @Before
    public void setUp() throws Exception
    {
        testFile = new File("data/mesonet/20180102stil.mts");
        testFile.exists();
        testMesonetFile = new MesonetTimeFile("data/mesonet/20180102stil.mts");
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

        Assert.assertEquals("Wrong date returned!", expected, actual);

        actual = testMesonetFile.getDateTimeString();
        expected = "2018-03-30T16:58:15 UTC";

        Assert.assertNotNull("No date returned!", actual);
    }

    /** Method to test the CSAbstractFile toString method */
    @Test
    public void testCsAbstractFileToString() throws IOException, WrongCopyrightException
    {
        String expected = "FileName: data/mesonet/20180102stil.mts";
        String actual = testMesonetFile.toString();
        Assert.assertEquals("Wrong number returned!", expected, actual);
    }

    /**
     * Test method for IllegalArgumentException
     * 
     * @throws IOException
     * @throws WrongCopyrightException
     */
    @Test()
    public void testIllegalArgumentException() throws IOException, WrongCopyrightException
    {
        boolean throwException = false;

        try
        {
            testMesonetFile = new MesonetTimeFile("Wrong");
            testMesonetFile.parseFile();
        }
        catch (IllegalArgumentException e)
        {
            throwException = true;
        }
        Assert.assertTrue(throwException);
    }
}
