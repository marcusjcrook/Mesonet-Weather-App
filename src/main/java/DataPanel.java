import java.awt.BorderLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Displays calculated weather statistics in a sortable table.
 */
public class DataPanel extends JPanel
{
    private static final long serialVersionUID = 5777740971062336138L;

    private final ResultsTableModel tableModel;

    public DataPanel()
    {
        super(new BorderLayout());
        tableModel = new ResultsTableModel();

        JTable resultsTable = new JTable(tableModel);
        resultsTable.setAutoCreateRowSorter(true);
        resultsTable.setCellSelectionEnabled(true);
        resultsTable.setFillsViewportHeight(true);
        resultsTable.setToolTipText("Sort by clicking a column header; select cells and press Ctrl+C to copy.");
        resultsTable.getColumnModel().getColumn(2).setCellRenderer(new ValueRenderer());
        resultsTable.getColumnModel().getColumn(4).setCellRenderer(new DateTimeRenderer());

        setBorder(BorderFactory.createTitledBorder("Results"));
        add(new JScrollPane(resultsTable), BorderLayout.CENTER);
    }

    public void updateData(List<StatMeasurement> measurements)
    {
        tableModel.setMeasurements(measurements);
    }

    private static class ValueRenderer extends DefaultTableCellRenderer
    {
        private static final long serialVersionUID = 1L;

        private final DecimalFormat format = new DecimalFormat("0.0000",
                DecimalFormatSymbols.getInstance(Locale.ROOT));

        private ValueRenderer()
        {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }

        @Override
        protected void setValue(Object value)
        {
            if (value instanceof Double number)
            {
                setText(Double.isFinite(number) ? format.format(number) : "bad");
            }
            else
            {
                super.setValue(value);
            }
        }
    }

    private static class DateTimeRenderer extends DefaultTableCellRenderer
    {
        private static final long serialVersionUID = 1L;

        @Override
        protected void setValue(Object value)
        {
            setText(value instanceof Instant instant ? CsAbstractFile.formatUtcDateTime(instant) : "");
        }
    }
}
