import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Class that creates text area to display results
 */
public class DataPanel extends JPanel
{
    /** to please Eclipse */
    private static final long serialVersionUID = 5777740971062336138L;

    /** a JTextArea object for describing the results */
    private JTextArea resultDescription;

    /** column field width constant */
    final int COLUMN_FIELD_WIDTH = 50;

    /** row field width constant */
    final int ROW_FIELD_WIDTH = 15;

    /** Constructor for DataPanel */
    public DataPanel()
    {
        // initialize resultDescription
        resultDescription = new JTextArea(ROW_FIELD_WIDTH, COLUMN_FIELD_WIDTH);

        // set layout manager
        setLayout(new GridLayout(0, 1));

        // set layout for text area
        GridBagConstraints layoutConst = new GridBagConstraints();
        layoutConst.gridx = 0;
        layoutConst.gridy = 3;
        layoutConst.insets = new Insets(10, 10, 10, 10);
        add(resultDescription, layoutConst);

        setBackground(new Color(0, 168, 107));
        setBorder(BorderFactory.createTitledBorder("Output"));

        resultDescription.setWrapStyleWord(true);
        resultDescription.setEditable(true);
    }

    /**
     * A method to update the data
     * 
     * @param result
     *            a String object
     */
    public void updateData(String result)
    {
        resultDescription.setText(result);
    }
}
