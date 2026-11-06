
import java.nio.file.Path;

/**
 * Driver class for project 2
 */
public class Driver
{

    public static void main(String[] args)
    {
        Path dataDirectory = DataDirectory.findInitialDirectory(Driver.class);
        String[] files = { "20180102stil.mts", "20180102okcn.mts", "20180102okce.mts" };
        for (int i = 0; i < files.length; i++)
        {
            files[i] = dataDirectory.resolve(files[i]).toString();
        }

        try
        {
            DaysStatistics stats = new DaysStatistics(files);
            stats.findStatistics();
            System.out.println(stats.toString());
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

    }

}
