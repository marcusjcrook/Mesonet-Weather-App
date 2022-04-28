import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class ParameterPanel extends JPanel
{
    /**
     * to keep eclipse happy
     */
    private static final long serialVersionUID = 4236142307230121244L;

    /** constant for Tair */
    public final String TAIR = "TAIR";

    /** constant for Ta9m */
    public final String TA9M = "TA9M";

    /** constant for Srad */
    public final String SRAD = "SRAD";

    /// ** constant for WSPD */
    // public final String WSPD = "WSPD";

    // Check boxes for the available parameters
    /** a JCheckBox object for air temperature */
    private JCheckBox airTemp;

    /** a JCheckBox object for ta9m */
    private JCheckBox ta9m;

    /** a JCheckBox object for srad */
    private JCheckBox srad;

    // /** a JCheckBox object for wspd */
    // private JCheckBox wspd;

    /** Constructor for ParameterPanel */
    public ParameterPanel()
    {
        System.out.println("Building Parameter panel");

        // Create a GridLayout Manager
        setLayout(new GridLayout(0, 1));

        // color originally suggested by Dr. Z: 153, 204, 210
        setBackground(new Color(152, 251, 152));

        airTemp = new JCheckBox(TAIR);
        ta9m = new JCheckBox(TA9M);
        srad = new JCheckBox(SRAD);
        // wspd = new JCheckBox(WSPD);

        airTemp.setSelected(true);

        setBorder(BorderFactory.createTitledBorder("Parameter"));

        add(airTemp);
        add(ta9m);
        add(srad);
        // add(wspd);
    }

    /** Method to get the parameter IDs */
    public ArrayList<String> getParamIds()
    {
        // create ArrayList<String> to hold selected parameters
        ArrayList<String> result = new ArrayList<String>();
        if (airTemp.isSelected())
        {
            result.add(TAIR);
        }
        if (ta9m.isSelected())
        {
            result.add(TA9M);
        }
        if (srad.isSelected())
        {
            result.add(SRAD);
        }
        // if (wspd.isSelected())
        // {
        // result.add(WSPD);
        // }
        return result;
    }
}
