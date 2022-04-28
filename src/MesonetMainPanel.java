import javax.swing.JLabel;
import javax.swing.JPanel;

public class MesonetMainPanel extends JPanel
{
    /** to keep eclipse happy */
    private static final long serialVersionUID = 6224309422787783370L;

    /** constructor for MesonetMain panel */
    public MesonetMainPanel()
    {
        JLabel greetingLabel = new JLabel("Mesonet Calculator");

        // Add greeting to this panel
        add(greetingLabel);
    }

}
