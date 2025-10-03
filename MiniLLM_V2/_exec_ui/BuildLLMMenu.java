package MiniLLM_V2._exec_ui;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;

import MiniLLM_V2.configuration.Configuration;

import MiniLLM_V2.create.Model;

import MiniLLM_V2.train.Trainer;

import MiniLLM_V2.predict.Query;

import MiniLLM_V2.utilities.model_persistance.ModelPersistance;

/**************************************************************************************/

public class BuildLLMMenu
{
	//
	// Main Frame Container
	//
    private JFrame mainFrame;
	//
	// Main Panel
	//
    private JPanel mainPanel;
	//
	// Panel for Buttons
	//
    private JPanel actionPanel;
	//
	// Reference to Buttons
	//
	JComboBox 		exploringCorpusCombo;

	JButton createLLM;

	JButton numberTrainingEpochs;

	JTextField epochTtextField;

	JButton trainLLM;

	JButton playLLM;

	JButton saveNetwork;

	JButton restoreNetwork;

	JButton compareStructures;

	JButton printMatricesAndBiases;

	JButton exit;
	//
	// Create Text Field
	//
	private JTextField jTextField = new JTextField(6);
	//
	// References to Classes
	//
	Model	model;

	Trainer	trainer;
	//
	// Index to the Array of Available Corpore
	//
	static int selectedCorpusIndex;

/**************************************************************************************/

    public void initBuildLLMMenu()
    {
		//
        // Create Main Frame
        //
        createMainFrame();
		//
		// Create Main Panel
		//
		createMainPanel();
        //
        // Create Action Panel
        //
        createActionPanel();
        //
        // Add Panels to respective Frames
        //
		mainPanel.add(actionPanel);

		mainFrame.add(mainPanel);
		//
		// Finish Main Frame Settings
		//
//		mainFrame.setUndecorated(true);
		//
		// Set Frame Visible
		//
		mainFrame.setVisible(true);
		//
        // Set the desired Content Area size
        //
//        int contentWidth = 220;
        int contentWidth = 430;

		int contentHeight = 564;
		//
        // Get the Insets (Title Bar and Borders)
        //
        Insets insets = mainFrame.getInsets();
		//
        // Calculate the total Frame Size
        //
        int mainFrameWidth = contentWidth + insets.left + insets.right;

        int mainFrameHeight = contentHeight + insets.top + insets.bottom;
		//
        // Set the Frame size to account for the Title Bar and Borders
        //
        mainFrame.setSize(mainFrameWidth, mainFrameHeight);
		//
        // Set Frame Location on the Screen
        //
//        mainFrame.setLocation(0, 0);
        mainFrame.setLocation(-450, 0);
		//
		// Set Action Buttons Initial State
		//
		exploringCorpusCombo.setEnabled(true);

		createLLM.setEnabled(true);

        numberTrainingEpochs.setEnabled(false);

        epochTtextField.setEnabled(false);

        trainLLM.setEnabled(false);

        playLLM.setEnabled(false);

   		saveNetwork.setEnabled(false);

   		restoreNetwork.setEnabled(false);

   		compareStructures.setEnabled(false);

   		printMatricesAndBiases.setEnabled(false);

		exit.setEnabled(true);
    }

/**************************************************************************************/

	private void createMainFrame()
	{
        mainFrame = new JFrame();

        mainFrame.setTitle("Build Network UI");

		mainFrame.setLayout(new FlowLayout(FlowLayout.CENTER));

//        mainFrame.setBounds(0,0,220,615);				// Set Container (x position, y position, width, and height)
        mainFrame.setBounds(-500, 0, 220, 615);				// Set Container (x position, y position, width, and height)

		mainFrame.getContentPane().setBackground(Color.BLUE);

		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

/**************************************************************************************/

	private void createMainPanel()
	{
		mainPanel = new JPanel();

		mainPanel.setBackground(Color.BLACK);
	}

/**************************************BUTTONS CREATION********************************/

	private void createActionPanel()												// Buttons Creation
    {

/**********************************************************/

		//
		//	BUTTON to SELECT CORPUS
		//
		final DefaultComboBoxModel<String> exploringCorpus = new DefaultComboBoxModel<>();

		exploringCorpus.addElement("SELECT CORPUS");												// If selected, means no Corpus chosen
		exploringCorpus.addElement("Hello World");													// Name of Corpus 1
		exploringCorpus.addElement("Step 1 - Minimal Base Corpus");									// Name of Corpus 2
		exploringCorpus.addElement("Step 2 - Expand With More Determiners and Variants Corpus");	// ...
		exploringCorpus.addElement("Step 3 - Add More Verbs and Actions");
		exploringCorpus.addElement("Step 4 - Add Adjectives for Finer Cluster");
		exploringCorpus.addElement("Step 5 - Add Prepositional Variety");

//		exploringCorpus.addElement("Meaningless Corpus");
//		exploringCorpus.addElement("Cat and Dog Corpus");								// Name of Corpus 3
		exploringCorpus.addElement("Alice Short Corpus");								// Name of Corpus 3
//		exploringCorpus.addElement("Extended Cat Dog Corpus");


		exploringCorpusCombo = new JComboBox<>(exploringCorpus);

		exploringCorpusCombo.setBorder(BorderFactory.createLineBorder(Color.black));

        exploringCorpusCombo.setBackground(Color.green);								// Initial Button Color

		exploringCorpusCombo.setSelectedIndex(0);

		JScrollPane labelListCorpusScrollPanel = new JScrollPane(exploringCorpusCombo);

		exploringCorpusCombo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
	            selectedCorpusIndex = exploringCorpusCombo.getSelectedIndex();			// Index to the chosen Corpus
			}
		});

/**********************************************************/

		//
		//  BUTTON to CREATE the LLM
		//
        createLLM = new JButton(" CREATE ");

        createLLM.setPreferredSize(new Dimension(25, 40));	// (W, H)

		createLLM.setBorder(BorderFactory.createLineBorder(Color.black));

        createLLM.setBackground(Color.green);					// Initial Button Color

        createLLM.addActionListener(e ->
        {
			//
			// Changes Button Color to RED, meaning it was clicked
			//
			createLLM.setBackground(Color.red);
			//
			// Instantiate the Model and Embedding and update the Configure Class
			// whith their Refereces to be used by the Driver and Play Classes
			//
    		model 					= new Model();

    		Configuration.model 	= model;
			//
    		// Create the Model
    		//
			model.createLLM();
			//
			// Toggle Buttons Status
			//
			createLLM. setEnabled(false);

        	numberTrainingEpochs.setEnabled(true);

        	epochTtextField.setEnabled(true );

			trainLLM.setEnabled(true);

			restoreNetwork.setEnabled(true);

restoreNetwork.setEnabled(true);
printMatricesAndBiases.setEnabled(true);
saveNetwork.setEnabled(true);
compareStructures.setEnabled(true);

		});

/**********************************************************/

		//
		//  BUTTON to LABEL AND TEXT FIELD TO ENTER THE OF NUMBER OF EPOCHS TO TRAIN THE LLM
		//
        numberTrainingEpochs = new JButton(" ENTER #EPOCHS <-- ");

		epochTtextField = new JTextField(15); 							// Input Field

		numberTrainingEpochs.setBorder(BorderFactory.createLineBorder(Color.black));

        numberTrainingEpochs.setBackground(Color.yellow);				// Initial Button Color

        epochTtextField.setBackground(Color.yellow);					// Initial Button Color

        epochTtextField.addActionListener(e ->
        {
			String numberEpochs = epochTtextField.getText();

			Configuration.note = Integer.parseInt(numberEpochs);		// String to int

System.out.println("\n\n"  + "numberEpochs = "  + numberEpochs  + "\n\n");


        });
		//
        // Trigger on "Enter" Key in the Text Field
        //
        epochTtextField.addActionListener(e -> numberTrainingEpochs.doClick());

/**********************************************************/

		//
		//  BUTTON to TRAIN the LLM
		//
        trainLLM = new JButton(" TRAIN ");

		trainLLM.setBorder(BorderFactory.createLineBorder(Color.black));

        trainLLM.setBackground(Color.yellow);					// Initial Button Color

        trainLLM.addActionListener(e ->
        {
			trainer = new Trainer();

			new Thread(() ->
			{
				trainer.train();

			}).start();
			//
			// Toggle Buttons Status
			//
			trainLLM.setEnabled(false);

			numberTrainingEpochs.setEnabled(false);

			epochTtextField.setEnabled(false);

			restoreNetwork.setEnabled(true);

			playLLM.setEnabled(true);

			saveNetwork.setEnabled(true);

			compareStructures.setEnabled(true);

			printMatricesAndBiases.setEnabled(true);

        });

/**********************************************************/

		//
		//  BUTTON to PLAY (PREDICT) the LLM
		//
        playLLM = new JButton(" PREDICT ");

		playLLM.setBorder(BorderFactory.createLineBorder(Color.black));

        playLLM.setBackground(Color.green);						// Initial Button Color

        playLLM.addActionListener(e ->
        {
			Query query = new Query();

			query.readQuery();

			//
			// Toggle Buttons Status
			//
//			playLLM.setEnabled(false);
        });


/**********************************************************/

		//
		//	BUTTON to SAVE the Trained NETWORK
		//
        saveNetwork = new JButton(" SAVE NETWORK ");

		saveNetwork.setBorder(BorderFactory.createLineBorder(Color.black));

        saveNetwork.setBackground(Color.magenta);				// Initial Button Color

        saveNetwork.addActionListener(e ->
        {
				saveNetwork.setBackground(Color.red);

				System.out.println("\n\n" + "                                  SAVING WEIGHTS AND BIASES\n");

				try
				{
					ModelPersistance.saveModel(Configuration.persistancePath);
				}
				catch(Exception e0)
				{
					System.out.println("\n\n"  + "@@@@@ IN: BuildLLMMenu: Restore Network BUTTON: e0: "  + e0);
				}

				System.out.println("                                   WEIGHTS AND BIASES SAVED");
        });

/**********************************************************/

		//
		//	BUTTON to RESTORE the Trained NETWORK
		//
        restoreNetwork = new JButton(" RESTORE NETWORK ");

		restoreNetwork.setBorder(BorderFactory.createLineBorder(Color.black));

        restoreNetwork.setBackground(Color.magenta);	// Initial Button Color

        restoreNetwork.addActionListener(e ->
        {
			restoreNetwork.setBackground(Color.red);

			System.out.println("\n\n" + "                                  RESTORING WEIGHTS AND BIASES\n");

			try
			{
				ModelPersistance.restoreModel(Configuration.persistancePath);

				System.out.println("                                  WEIGHTS AND BIASES RESTORED");
			}
			catch(Exception e1)
			{
				System.out.println("\n\n"  + "@@@@@ IN: BuildLLMMenu: Restore Network BUTTON: e1: "  + e1);
			}

			playLLM.setEnabled(true);

			compareStructures.setEnabled(true);

			printMatricesAndBiases.setEnabled(true);

        });

/**********************************************************/

		//
		//	BUTTON to COMPARE STRUCTURES
		//
        compareStructures = new JButton(" COMPARE STRUCTURES ");

		compareStructures.setBorder(BorderFactory.createLineBorder(Color.black));

        compareStructures.setBackground(Color.magenta);		// Initial Button Color

        compareStructures.addActionListener(e ->
        {
				compareStructures.setBackground(Color.red);

				System.out.println("\n\n" + "                                      COMPARING STRUCTURES\n");

				try
				{
					ModelPersistance.compareModelToFile(Configuration.persistancePath);
				}
				catch(Exception e2)
				{
					System.out.println("\n\n"  + "Exception Comparing Structures: e2:"  + e2);
				}

				System.out.println("\n"  + "                                      STRUCTURES COMPARED");
        });

/**********************************************************/

		//
		//	BUTTON to PRINT STRUCTURES
		//
        printMatricesAndBiases = new JButton(" PRINT STRUCTURES ");

		printMatricesAndBiases.setBorder(BorderFactory.createLineBorder(Color.black));

        printMatricesAndBiases.setBackground(Color.magenta);				// Initial Button Color

        printMatricesAndBiases.addActionListener(e ->
        {
			printMatricesAndBiases.setBackground(Color.red);

			System.out.println("\n\n" + "                                     PRINTING STRUCTURES\n");

			try
			{
				ModelPersistance.printSavedModel(Configuration.persistancePath);

				System.out.println("                                   STRUCTURES PRINTED");
			}
			catch(Exception e4)
			{
				System.out.println("\n\n"  + "@@@@@ IN: BuildLLMMenu: Restore Network BUTTON: e4: "  + e4);
			}
        });

/**********************************************************/

		//
		//	BUTTON to EXIT the Application
		//
        exit = new JButton(" EXIT ");

		exit.setBorder(BorderFactory.createLineBorder(Color.black));

        exit.setBackground(Color.red);	// Initial Button Color

        exit.addActionListener(e ->
        {
			System.exit(0);
        });


/**********************************************************/

		//
		// Update Action Panel With Defined BUTTONS
		//
        actionPanel = new JPanel(new GridLayout(11, 1));

		actionPanel.add(exploringCorpusCombo);

        actionPanel.add(createLLM);

        actionPanel.add(numberTrainingEpochs);

        actionPanel.add(epochTtextField);

        actionPanel.add(trainLLM);

        actionPanel.add(playLLM);

   		actionPanel.add(saveNetwork);

   		actionPanel.add(restoreNetwork);

   		actionPanel.add(compareStructures);

   		actionPanel.add(printMatricesAndBiases);

		actionPanel.add(exit);
    }

/**************************************************************************************/

	public static int getSelectedCorpusIndex()
	{
		return selectedCorpusIndex;
	}

/**************************************************************************************/

}