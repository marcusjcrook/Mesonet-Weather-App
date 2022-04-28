import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Class to create frame for displaying Mesonet data
 */
public class MesonetFrame extends JFrame
{
    /** to please eclipse */
    private static final long serialVersionUID = 1L;

    /** Menu bar */
    private FileMenuBar fileMenuBar;

    /** a StatisticsPanel Object */
    private StatisticsPanel statistics;

    /** a ParameterPanelObject */
    private ParameterPanel parameters;

    /** a DataPanel Object */
    private DataPanel dataPanel;

    /** a MesonetMainPanel object */
    private MesonetMainPanel banner;

    /** a JPanel object holding the buttons */
    private JPanel buttonPanel;

    /** a JButton object for calculating */
    private JButton calcButton;

    /** a JButton object for exiting */
    private JButton exitButton;

    /** Constructor for MesonetFrame */
    public MesonetFrame()
    {
        // call JFrame constructor
        super("Mesonet Calculator");

        // set the close operation
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // set the layout manager
        setLayout(new BorderLayout());

        // Menu Bar
        fileMenuBar = new FileMenuBar();
        setJMenuBar(fileMenuBar);

        // Create the panels
        banner = new MesonetMainPanel();
        statistics = new StatisticsPanel();
        parameters = new ParameterPanel();
        dataPanel = new DataPanel();

        // add the panels
        buildButtonPanel();
        add(banner, BorderLayout.NORTH);
        add(statistics, BorderLayout.WEST);
        add(dataPanel, BorderLayout.EAST);
        add(parameters, BorderLayout.CENTER);

        // make sure everything fits
        pack();
        // set the frame visible
        setVisible(true);
    }

    /** A Method to build the button panel */
    private void buildButtonPanel()
    {
        // Create a panel for buttons.
        buttonPanel = new JPanel();

        // Create the buttons
        calcButton = new JButton("Calculate");
        exitButton = new JButton("Exit");

        // register the buttons with the action listeners
        exitButton.addActionListener(new ExitButtonListener());
        calcButton.addActionListener(new CalcButtonListener());

        // set the layout manager
        setLayout(new BorderLayout());

        // add the buttons to the panel and set the background color
        buttonPanel.add(calcButton);
        buttonPanel.add(exitButton);
        // color originally suggested by Dr. Z.: 102, 178, 210
        buttonPanel.setBackground(new Color(0, 78, 56));

        // add the panel to the frame
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /** Listener class for exit button */
    private class ExitButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);
        }
    }

    /** Listener class for calculate button */
    private class CalcButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String result = new String("");
            String statString = statistics.getStatisticsType();
            ArrayList<String> paramType = parameters.getParamIds();
            ArrayList<String> files = fileMenuBar.getFileList();
            String[] fileArray = new String[1];
            fileArray = files.toArray(fileArray);
            int exceptionIndex = 0;

            try
            {
                DaysStatistics days = new DaysStatistics(fileArray);
                days.findStatistics();

                // loop through the parameter ArrayList and print information
                for (int parameterIndex = 0; parameterIndex < paramType.size(); parameterIndex++)
                {
                    if (statString.equals("MAXIMUM"))
                    {
                        result += days.getMaximumDay(paramType.get(parameterIndex)).toString() + "\n";
                    }
                    else if (statString.equals("MINIMUM"))
                    {
                        result += days.getMinimumDay(paramType.get(parameterIndex)).toString() + "\n";
                    }
                    exceptionIndex++;
                }
                System.out.println(fileMenuBar.getFileList());
            }
            catch (Exception e1)
            {
                // for debugging
                JOptionPane.showMessageDialog(null, "Should display what is calculated for " + statString + " "
                        + paramType.get(exceptionIndex).toString() + " result: " + result);
                e1.printStackTrace();
            }
            MesonetFrame.this.dataPanel.updateData(result);
        }
    }

    ///////////////////////////////////////////////////////////////////
    /**
     * Menu bar that provides file loading and program exit capabilities.
     */
    public class FileMenuBar extends JMenuBar
    {
        /**
         * to please eclipse
         */
        private static final long serialVersionUID = 1L;

        /** Menu on the menu bar */
        private JMenu menu;

        /** Open option for the menu */
        private JMenuItem menuOpen;

        /** Close option for the menu */
        private JMenuItem menuExit;

        /** Reference to a file chooser pop-up */
        private JFileChooser fileChooser;

        /** an ArrayList for the files */
        private ArrayList<String> listOfFiles;

        /**
         * Constructor: fully assemble the menu bar and attach the necessary action
         * listeners.
         */
        public FileMenuBar()
        {
            listOfFiles = new ArrayList<>();
            // Create the menu and add it to the menu bar
            menu = new JMenu("File");
            add(menu);

            // Create the menu items and add them to the menu
            menuOpen = new JMenuItem("Open Data File");
            menuOpen.setName("Menu Open");
            menuExit = new JMenuItem("Exit");
            menu.add(menuOpen);
            menu.add(menuExit);

            // Action listener for exit
            menuExit.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    System.exit(0);
                }
            });

            // Create the file chooser
            fileChooser = new JFileChooser(new File("./data/mesonet"));
            fileChooser.setMultiSelectionEnabled(true);

            // Action listener for file open
            menuOpen.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    // Ask for files
                    int returnVal = fileChooser.showOpenDialog(menuOpen);
                    // Did we get one?
                    if (returnVal == JFileChooser.APPROVE_OPTION)
                    {
                        // Yes
                        File[] files = fileChooser.getSelectedFiles();
                        // System.out.println(files.length);
                        try
                        {
                            for (File file : files)
                            {
                                String fileName = file.toString();
                                System.out.println(fileName);
                                listOfFiles.add(fileName);
                            }
                        }
                        catch (Exception e2)
                        {
                            // Catch all other exceptions
                            JOptionPane.showMessageDialog(fileChooser, "File load error");
                            MesonetFrame.this.setCursor(null);
                        }
                    }
                    else
                    {
                        System.out.println("No files.");
                    }
                }
            });
        }

        /**
         * Method to return a list containing the files
         * 
         * @return ArrayList<String> with a list of the files
         */
        public ArrayList<String> getFileList()
        {
            String[] temp = new String[1];
            ArrayList<String> result = new ArrayList<String>();

            // to only keep first file name in ArrayList
            for (int index = 0; index < listOfFiles.size(); ++index)
            {
                temp[0] = listOfFiles.get(index);
            }
            result.add(temp[0]);
            return result;
        }
    }
}