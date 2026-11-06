import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class ResultsTableModel extends AbstractTableModel
{
    private static final long serialVersionUID = 1L;

    private static final String[] COLUMN_NAMES = { "Parameter", "Statistic", "Value", "Station", "Date / Time (UTC)" };

    private List<StatMeasurement> measurements = List.of();

    public void setMeasurements(List<StatMeasurement> measurements)
    {
        this.measurements = new ArrayList<>(measurements);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount()
    {
        return measurements.size();
    }

    @Override
    public int getColumnCount()
    {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column)
    {
        return COLUMN_NAMES[column];
    }

    @Override
    public Class<?> getColumnClass(int column)
    {
        return switch (column)
        {
            case 2 -> Double.class;
            case 4 -> Instant.class;
            default -> String.class;
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        StatMeasurement measurement = measurements.get(rowIndex);
        return switch (columnIndex)
        {
            case 0 -> measurement.getParamId().toUpperCase();
            case 1 -> statisticName(measurement.getStatType());
            case 2 -> measurement.isValid() ? measurement.getValue() : Double.NaN;
            case 3 -> measurement.getStationId().toUpperCase();
            case 4 -> measurement.getDateTimeOfMeasurement();
            default -> throw new IndexOutOfBoundsException("Unknown column: " + columnIndex);
        };
    }

    private static String statisticName(StatType statType)
    {
        return switch (statType)
        {
            case MIN -> "Minimum";
            case MAX -> "Maximum";
            case AVG -> "Average";
        };
    }
}
