import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ResultsTableModelTest
{
    @Test
    public void displaysMeasurementFieldsInSortableColumns()
    {
        Instant dateTime = Instant.parse("2018-02-19T00:15:00Z");
        StatMeasurement measurement = new StatMeasurement(2.5, dateTime, "station", "tair", StatType.MAX);
        ResultsTableModel model = new ResultsTableModel();

        model.setMeasurements(List.of(measurement));

        assertEquals(1, model.getRowCount());
        assertEquals(5, model.getColumnCount());
        assertEquals("TAIR", model.getValueAt(0, 0));
        assertEquals("Maximum", model.getValueAt(0, 1));
        assertEquals(2.5, model.getValueAt(0, 2));
        assertEquals("STATION", model.getValueAt(0, 3));
        assertEquals(dateTime, model.getValueAt(0, 4));
        assertEquals(Double.class, model.getColumnClass(2));
        assertEquals(Instant.class, model.getColumnClass(4));
    }

    @Test
    public void invalidMeasurementRemainsRepresentedAsAnInvalidNumericValue()
    {
        StatMeasurement measurement = new StatMeasurement(-999.0, Instant.EPOCH, "station", "srad", StatType.AVG);
        ResultsTableModel model = new ResultsTableModel();

        model.setMeasurements(List.of(measurement));

        assertFalse(measurement.isValid());
        assertTrue(Double.isNaN((Double) model.getValueAt(0, 2)));
    }

    @Test
    public void replacesDisplayedMeasurements()
    {
        ResultsTableModel model = new ResultsTableModel();
        model.setMeasurements(List.of(new StatMeasurement(1, Instant.EPOCH, "station", "tair", StatType.MIN),
                new StatMeasurement(2, Instant.EPOCH, "station", "ta9m", StatType.MAX)));

        model.setMeasurements(List.of());

        assertEquals(0, model.getRowCount());
    }
}
