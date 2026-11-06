# Mesonet Weather App

A Java Swing application for reading Oklahoma Mesonet `.mts` files and calculating daily weather statistics. Select one or more files, choose a statistic and parameters, and view the results in the app.

## Requirements

- Java Development Kit (JDK) 26
- Apache Maven 3.10 or later

Check that Java and Maven are available in a terminal:

```text
java -version
mvn -version
```

## Run the application

From the project directory, compile the application and start its Swing interface:

```text
mvn compile
java -cp target/classes MesonetDriver
```

## Use the application

1. Choose **File > Open Data File**.
2. Select one or more Mesonet `.mts` files. Use the file chooser's multi-select feature to select several files.
3. Choose **MINIMUM**, **MAXIMUM**, or **AVERAGE**.
4. Select one or more parameters: `TAIR`, `TA9M`, and/or `SRAD`.
5. Click **Calculate**. Results appear in a table with the parameter, statistic, value, station, and date/time (UTC). Click a column heading to sort, or select cells and press **Ctrl+C** to copy them. While it calculates, the button changes to **Calculating...**.

Minimum and maximum report the selected extreme daily measurement across the chosen files. Average reports the daily average for each chosen file.

## Data files

The sample files are in `Data/mesonet`. When the file chooser opens, the app looks for that directory relative to the application's location, then relative to the current working directory. If neither location contains it, the chooser starts in your home directory. You can still navigate to and select files stored elsewhere.

The file chooser filters for `.mts` files. Inputs must be readable Mesonet files with the required headers and data rows. If a file is missing, unreadable, or malformed, the error dialog identifies the file and, when available, the line and problem. Parameters with no usable readings are still shown in the table as **bad**, with an explanatory notice.

## Run tests

Run the test suite with:

```text
mvn test
```
