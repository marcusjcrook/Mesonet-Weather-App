import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class StatisticsPanel extends JPanel
{
    /**
     * to please Eclipse
     */
    private static final long serialVersionUID = -5778130703074619169L;

    /** a string constant for max */
    public final String MAX_BUTTON = "MAXIMUM";

    /** a string constant for min */
    public final String MIN_BUTTON = "MINIMUM";

    /** a string constant for average */
    public final String AVG_BUTTON = "AVERAGE";

    /** a JRadioButton for the maximum */
    private JRadioButton maxButton;

    /** a JRadioButton for the minimum */
    private JRadioButton minButton;

    /** a JRadioButton for the average */
    private JRadioButton avgButton;

    /** a button group for the buttons */
    private ButtonGroup bg;

    /** Constructor for StatisticsPanel */
    public StatisticsPanel()
    {
        // set layout manager
        setLayout(new GridLayout(3, 3));

        // color originally suggested by Dr. Z: 153, 204, 210
        setBackground(new Color(152, 251, 152));

        // create the buttons and button group
        minButton = new JRadioButton(MIN_BUTTON);
        maxButton = new JRadioButton(MAX_BUTTON);
        avgButton = new JRadioButton(AVG_BUTTON);
        minButton.setSelected(true);
        bg = new ButtonGroup();

        // add a border around the panel
        setBorder(BorderFactory.createTitledBorder("Statistics"));

        // add the buttons to the panel and button group
        add(minButton);
        add(maxButton);
        add(avgButton);
        bg.add(minButton);
        bg.add(maxButton);
        bg.add(avgButton);
    }

    /** Method to get stattype */
    public String getStatisticsType()
    {
        String result = "";

        // determine which button is selected and assign string
        if (minButton.isSelected())
        {
            result = MIN_BUTTON;
        }
        else if (maxButton.isSelected())
        {
            result = MAX_BUTTON;
        }
        else if (avgButton.isSelected())
        {
            result = AVG_BUTTON;
        }
        return result;
    }
}