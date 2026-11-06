import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class DataDirectoryTest
{
    @TempDir
    Path temporaryDirectory;

    @Test
    public void findsDataFolderRelativeToApplicationLocation() throws Exception
    {
        Path applicationLocation = Files.createDirectories(temporaryDirectory.resolve("app/target/classes"));
        Path workingDirectory = Files.createDirectories(temporaryDirectory.resolve("other"));
        Path userHome = Files.createDirectories(temporaryDirectory.resolve("home"));
        Path expected = Files.createDirectories(temporaryDirectory.resolve("app/Data/mesonet"));

        assertEquals(expected, DataDirectory.findInitialDirectory(applicationLocation, workingDirectory, userHome));
    }

    @Test
    public void fallsBackToWorkingDirectoryWhenApplicationHasNoDataFolder() throws Exception
    {
        Path applicationLocation = Files.createDirectories(temporaryDirectory.resolve("app/classes"));
        Path workingDirectory = Files.createDirectories(temporaryDirectory.resolve("working"));
        Path userHome = Files.createDirectories(temporaryDirectory.resolve("home"));
        Path expected = Files.createDirectories(workingDirectory.resolve("Data/mesonet"));

        assertEquals(expected, DataDirectory.findInitialDirectory(applicationLocation, workingDirectory, userHome));
    }

    @Test
    public void fallsBackToUserHomeWhenNoDataFolderIsFound() throws Exception
    {
        Path applicationLocation = Files.createDirectories(temporaryDirectory.resolve("app/classes"));
        Path workingDirectory = Files.createDirectories(temporaryDirectory.resolve("working"));
        Path userHome = Files.createDirectories(temporaryDirectory.resolve("home"));

        assertEquals(userHome, DataDirectory.findInitialDirectory(applicationLocation, workingDirectory, userHome));
    }
}
