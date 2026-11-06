import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.CodeSource;

public final class DataDirectory
{
    private DataDirectory()
    {
    }

    public static Path findInitialDirectory(Class<?> applicationClass)
    {
        CodeSource codeSource = applicationClass.getProtectionDomain().getCodeSource();
        Path applicationLocation = null;
        if (codeSource != null)
        {
            try
            {
                applicationLocation = Path.of(codeSource.getLocation().toURI());
            }
            catch (URISyntaxException ex)
            {
                throw new IllegalStateException("Could not resolve the application's location.", ex);
            }
        }

        return findInitialDirectory(applicationLocation, Path.of(System.getProperty("user.dir")),
                Path.of(System.getProperty("user.home")));
    }

    static Path findInitialDirectory(Path applicationLocation, Path workingDirectory, Path userHome)
    {
        Path dataDirectory = findDataDirectory(applicationLocation);
        if (dataDirectory != null)
        {
            return dataDirectory;
        }

        dataDirectory = findDataDirectory(workingDirectory);
        return dataDirectory != null ? dataDirectory : userHome;
    }

    private static Path findDataDirectory(Path location)
    {
        if (location == null)
        {
            return null;
        }

        Path directory = Files.isDirectory(location) ? location : location.getParent();
        while (directory != null)
        {
            Path candidate = directory.resolve("Data").resolve("mesonet");
            if (Files.isDirectory(candidate))
            {
                return candidate;
            }
            directory = directory.getParent();
        }
        return null;
    }
}
