import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

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
            String statString = statistics.getStatisticsType();
            ArrayList<String> paramType = parameters.getParamIds();
            ArrayList<String> files = fileMenuBar.getFileList();
            String[] fileArray = files.toArray(new String[0]);

            if (files.isEmpty())
            {
                JOptionPane.showMessageDialog(MesonetFrame.this, "Select at least one data file.");
                return;
            }
            if (paramType.isEmpty())
            {
                JOptionPane.showMessageDialog(MesonetFrame.this, "Select at least one parameter.");
                return;
            }

            calcButton.setEnabled(false);
            calcButton.setText("Calculating...");

            new SwingWorker<List<StatMeasurement>, Void>()
            {
                @Override
                protected List<StatMeasurement> doInBackground() throws Exception
                {
                    ArrayList<StatMeasurement> result = new ArrayList<>();
                    DaysStatistics days = new DaysStatistics(fileArray);
                    days.findStatistics();

                    for (String parameter : paramType)
                    {
                        if (statString.equals("MAXIMUM"))
                        {
                            result.add(days.getMaximumDay(parameter));
                        }
                        else if (statString.equals("MINIMUM"))
                        {
                            result.add(days.getMinimumDay(parameter));
                        }
                        else if (statString.equals("AVERAGE"))
                        {
                            result.addAll(days.getAverageDays(parameter));
                        }
                    }
                    return result;
                }

                @Override
                protected void done()
                {
                    try
                    {
                        List<StatMeasurement> result = get();
                        dataPanel.updateData(result);
                        ArrayList<String> parametersWithoutReadings = new ArrayList<>();
                        for (String parameter : paramType)
                        {
                            if (result.stream().filter(measurement -> measurement.getParamId().equalsIgnoreCase(parameter))
                                    .noneMatch(StatMeasurement::isValid))
                            {
                                parametersWithoutReadings.add(parameter);
                            }
                        }
                        if (!parametersWithoutReadings.isEmpty())
                        {
                            JOptionPane.showMessageDialog(MesonetFrame.this,
                                    "No usable readings were found for " + String.join(", ", parametersWithoutReadings)
                                            + ". Results for these parameters are marked \"bad\".",
                                    "No usable readings", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    catch (InterruptedException ex)
                    {
                        Thread.currentThread().interrupt();
                        JOptionPane.showMessageDialog(MesonetFrame.this,
                                "The " + statString.toLowerCase() + " calculation was interrupted. Please try again.",
                                "Calculation interrupted", JOptionPane.ERROR_MESSAGE);
                    }
                    catch (java.util.concurrent.ExecutionException ex)
                    {
                        Throwable cause = ex.getCause();
                        String message = cause.getMessage() == null ? cause.toString() : cause.getMessage();
                        JOptionPane.showMessageDialog(MesonetFrame.this,
                                "Could not calculate " + statString.toLowerCase() + ".\n\n" + message
                                        + "\n\nCheck that the selected files exist, are readable Mesonet .mts files, "
                                        + "and contain the required headers and data rows.",
                                "Data file error", JOptionPane.ERROR_MESSAGE);
                    }
                    finally
                    {
                        calcButton.setText("Calculate");
                        calcButton.setEnabled(true);
                    }
                }
            }.execute();
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
            fileChooser = new JFileChooser(DataDirectory.findInitialDirectory(MesonetFrame.class).toFile());
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setFileFilter(new FileNameExtensionFilter("Mesonet data files (*.mts)", "mts"));

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
                        listOfFiles.clear();
                        for (File file : files)
                        {
                            listOfFiles.add(file.getPath());
                        }
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
            return new ArrayList<String>(listOfFiles);
        }
    }
}