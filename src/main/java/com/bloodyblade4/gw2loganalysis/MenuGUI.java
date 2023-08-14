package com.bloodyblade4.gw2loganalysis;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import com.bloodyblade4.gw2loganalysis.components.Constants;
import com.bloodyblade4.gw2loganalysis.components.DialogHelper;
import com.bloodyblade4.gw2loganalysis.components.FileHelper;
import com.bloodyblade4.gw2loganalysis.gui_components.AdvancedFilteringTab;
import com.bloodyblade4.gw2loganalysis.gui_components.ParsingOptionsPanel;
import com.bloodyblade4.gw2loganalysis.gui_components.SettingPanel;
import com.bloodyblade4.gw2loganalysis.settings.Settings;
import com.bloodyblade4.gw2loganalysis.settings.SettingsBuff;
import com.bloodyblade4.gw2loganalysis.settings.XMLHelper;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/*
 * TODO: 
 * 
 * -Properties files are quite a mess. It's great for a huge list to match an id, but perhaps not use cases such as profession buffs --where the output changes a lot. 
 * 		-Figure out a better way to handle profession buffs, or finish input each and every profession effect into the list.
 * 		-Effects that require percentages could be handled better? Perhaps use a table for values that don't have percentages, since most do?  

 * - description to stats inside the "StatsWindow" gui element would be very nice, but would require modification of the DataModel/DefaultTableDataModel. So, perhaps we'll come back to that. 
 * -try catch for the resources options. 
 * - show how many files there are to run based on your filters.. I thought I already did that, but... 
 * - add "Any" to the specializations field, this will require adding the same into the JSONParsing general section. Some users may play the same profession on different characters.
 * - Check the influence of ArcDPS addons, such as healing stats. This could cause some bugs. 
 * 
 * Bugs:
 * 
 * 
 * 
 * 
 */
 
public class MenuGUI {
	static final Logger logger = Logger.getLogger(MenuGUI.class); 
	static Settings curSettings;
	private JFrame frame;

	private static JTextField textFieldCharName;
	private static JTextField textFieldSettingsName;
	private static JFormattedTextField formattedTextFieldMinDuration;
	
	private static JComboBox<String> comboBoxLocation;
	private static JComboBox<String> comboBoxEncounter;
	private static JComboBox<String> comboBoxProfession;
	private static JComboBox<String> comboBoxSpecialization;
	
	private static AdvancedFilteringTab panelFiltering;
	private static ParsingOptionsPanel parsingOptionsPanel;
	private static SettingPanel panelSettings;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				initializeLogger();
				try {
					String path = XMLHelper.findLastSettings();
					curSettings = XMLHelper.loadSettings(path);
					logger.info("Previous main.java.com.bloodybade4.gw2loganalysis.settings found and loaded from: \"" + path + "\"");
				} catch (Exception e) {
					curSettings = new Settings();
					logger.info("No previous setting were found. New main.java.com.bloodybade4.gw2loganalysis.settings initialized.");
				}
				
				MenuGUI window = new MenuGUI();
				updateTextFields();
				window.frame.setVisible(true);
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MenuGUI() {
		initialize();
	}
	
	private static void initializeLogger() {
		File dir = new File(Constants.GW2_APP_DATA_FILE);
	     if (!dir.exists()) {dir.mkdir();}
	     
	    //Location of the log4j.properties file.
		InputStream location = MenuGUI.class.getResourceAsStream("/log4j.properties");
		
		//open the properties file and assign to the logger. 
		Properties props = new Properties();
		try {
			props.load(new InputStreamReader(location));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PropertyConfigurator.configure(props);
		logger.info("Logger assigned.");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 636, 609);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel rightPanel = new JPanel();
		JPanel leftPanel = new JPanel();
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		

		JPanel panelMenu = new JPanel();
		tabbedPane.addTab("Menu", null, panelMenu, null);
		panelSettings = new SettingPanel();
		
		JSeparator separator = new JSeparator();
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		
		JButton btnNewButton = new JButton("Run");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveTextFields();
				if (!readyToRun(curSettings))
					return;
				
				Analysis analysis = new Analysis();
				analysis.analize(curSettings);
			}
		});
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setOrientation(SwingConstants.VERTICAL);
		
		GroupLayout gl_panelMenu = new GroupLayout(panelMenu);
		gl_panelMenu.setHorizontalGroup(
			gl_panelMenu.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelMenu.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelMenu.createParallelGroup(Alignment.TRAILING)
							.addGroup(gl_panelMenu.createSequentialGroup()
								.addComponent(leftPanel, GroupLayout.PREFERRED_SIZE, 278, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
									.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
									.addGroup(gl_panelMenu.createSequentialGroup()
										.addComponent(separator_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGap(10)
										.addComponent(rightPanel, GroupLayout.PREFERRED_SIZE, 292, GroupLayout.PREFERRED_SIZE)))
								.addGap(283))
							.addGroup(gl_panelMenu.createSequentialGroup()
								.addComponent(separator, GroupLayout.DEFAULT_SIZE, 850, Short.MAX_VALUE)
								.addGap(21))
							.addGroup(Alignment.LEADING, gl_panelMenu.createSequentialGroup()
								.addComponent(panelSettings, GroupLayout.PREFERRED_SIZE, 589, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()))
						.addGroup(Alignment.TRAILING, gl_panelMenu.createSequentialGroup()
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
							.addGap(306))))
		);
		gl_panelMenu.setVerticalGroup(
			gl_panelMenu.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelMenu.createSequentialGroup()
					.addContainerGap()
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
						.addComponent(separator_3, GroupLayout.PREFERRED_SIZE, 265, GroupLayout.PREFERRED_SIZE)
						.addComponent(leftPanel, 0, 0, Short.MAX_VALUE)
						.addComponent(rightPanel, GroupLayout.PREFERRED_SIZE, 267, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 6, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelSettings, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton)
					.addGap(12))
		);
		
		/*
		 * Left Panel, Filters
		 */
		
		JLabel lblNewLabel_3 = new JLabel("Min Duration (sec)");
		lblNewLabel_3.setToolTipText("Minimum duration of the fight log for it to be parsed into csv file, in seconds.");
		
		formattedTextFieldMinDuration = new JFormattedTextField();
		formattedTextFieldMinDuration.setValue(Integer.valueOf(0));
		formattedTextFieldMinDuration.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Location");
		lblNewLabel_2.setToolTipText("Desired location of the battle.");
		
		
		comboBoxEncounter = new JComboBox<String>();
		comboBoxEncounter.setModel(new DefaultComboBoxModel<String>(
				Constants.ENCOUNTER_IDS.get(curSettings.getLocation()).values().toArray(String[]::new)
				));
		
		
		DefaultComboBoxModel<String> comboModelLocation = new DefaultComboBoxModel<String>(Constants.ENCOUNTER_IDS.keySet().toArray(String[]::new));
		comboBoxLocation = new JComboBox<>(comboModelLocation);
		comboBoxLocation.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange()==ItemEvent.SELECTED) {
					String v = String.valueOf(comboBoxLocation.getSelectedItem());
					if (v.equals(curSettings.getLocation()))
						return;
					curSettings.setLocation(v);
					comboBoxEncounter.setModel(new DefaultComboBoxModel<String>(
							Constants.ENCOUNTER_IDS.get(v).values().toArray(String[]::new)
							));
					curSettings.setEncounterID(0); //Zero is the default.
					
				}
			}
		});
		
		JLabel lblNewLabel_6 = new JLabel("Specialization");
		lblNewLabel_6.setToolTipText("Specialization of your character. (Will reset Profession Buffs, inside of main.java.com.bloodybade4.gw2loganalysis.settings.)");
		comboBoxSpecialization = new JComboBox<String>();
		
		comboBoxProfession = new JComboBox<String>(new DefaultComboBoxModel<String>(Constants.profSpec.keySet().toArray(String[]::new)));
		comboBoxProfession.setSelectedItem("Any");
		comboBoxProfession.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange()==ItemEvent.SELECTED) {
					String v = String.valueOf(comboBoxProfession.getSelectedItem());
					if (v.equals(curSettings.getProfession()))
						return;
					curSettings.setProfession(v);
					comboBoxSpecialization.setModel(new DefaultComboBoxModel<String>(Constants.profSpec.get(v).toArray(String[]::new)));
					curSettings.setProfessionBuffsSettings( new SettingsBuff
							(true, true, true, null, null, null, "Profession Buffs", "personalBuffs",
									Constants.generateBuffListWithFilter(System.getProperty("user.dir") +"\\src\\resources\\constProfessionBuffIDs.properties", v))
							);
					
				}
			}
		});
		
		JLabel lblNewLabel_5 = new JLabel("Profession");
		lblNewLabel_5.setToolTipText("Profession/class of character. (Will reset Profession Buffs, inside of main.java.com.bloodybade4.gw2loganalysis.settings.)");
		JLabel labelName = new JLabel("Name*");
		labelName.setToolTipText("Required* Character name or Account name. ");
		
		textFieldCharName = new JTextField();
		textFieldCharName.setColumns(10);

		
		JLabel lblNewLabel_7 = new JLabel("Filtering Options");
		
		JLabel lblNewLabel_11 = new JLabel("Encounter");
		lblNewLabel_11.setToolTipText("Specific encounter from selected Location. *Location must be selected first. If the encounter you want is not found here, change \"Location\" main.java.com.bloodybade4.gw2loganalysis.settings to the correct location or to \"Any.\"");
		

		GroupLayout gl_leftPanel = new GroupLayout(leftPanel);
		gl_leftPanel.setHorizontalGroup(
			gl_leftPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_leftPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_leftPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_leftPanel.createSequentialGroup()
							.addComponent(labelName, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(textFieldCharName, GroupLayout.PREFERRED_SIZE, 186, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_leftPanel.createSequentialGroup()
							.addComponent(lblNewLabel_5, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
							.addGap(26)
							.addComponent(comboBoxProfession, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_leftPanel.createSequentialGroup()
							.addComponent(lblNewLabel_6, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addGap(33)
							.addComponent(comboBoxSpecialization, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(26, Short.MAX_VALUE))
				.addGroup(gl_leftPanel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(lblNewLabel_7)
					.addGap(230))
				.addGroup(gl_leftPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_3, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
					.addGap(35)
					.addComponent(formattedTextFieldMinDuration, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(66, Short.MAX_VALUE))
				.addGroup(gl_leftPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_leftPanel.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_leftPanel.createSequentialGroup()
							.addComponent(lblNewLabel_11)
							.addGap(18)
							.addComponent(comboBoxEncounter, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(gl_leftPanel.createSequentialGroup()
							.addComponent(lblNewLabel_2, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
							.addGap(20)
							.addComponent(comboBoxLocation, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(66, Short.MAX_VALUE))
		);
		gl_leftPanel.setVerticalGroup(
			gl_leftPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_leftPanel.createSequentialGroup()
					.addComponent(lblNewLabel_7)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_leftPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_leftPanel.createSequentialGroup()
							.addGap(3)
							.addComponent(labelName))
						.addComponent(textFieldCharName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(12)
					.addGroup(gl_leftPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_leftPanel.createSequentialGroup()
							.addGap(4)
							.addComponent(lblNewLabel_5))
						.addComponent(comboBoxProfession, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(8)
					.addGroup(gl_leftPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_6)
						.addGroup(gl_leftPanel.createSequentialGroup()
							.addGap(3)
							.addComponent(comboBoxSpecialization, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_leftPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_leftPanel.createSequentialGroup()
							.addGap(4)
							.addComponent(lblNewLabel_2))
						.addComponent(comboBoxLocation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_leftPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_11)
						.addComponent(comboBoxEncounter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(23)
					.addGroup(gl_leftPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_leftPanel.createSequentialGroup()
							.addGap(3)
							.addComponent(lblNewLabel_3))
						.addComponent(formattedTextFieldMinDuration, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(101, Short.MAX_VALUE))
		);
		leftPanel.setLayout(gl_leftPanel);
		
		
		/*
		 * Right panel
		 */
		
		parsingOptionsPanel = new ParsingOptionsPanel(curSettings);
		
		JButton btnLoadSettings = new JButton("Load Settings");
		btnLoadSettings.setToolTipText("Locate and load a main.java.com.bloodybade4.gw2loganalysis.settings file from your computer.");
		btnLoadSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					File f = FileHelper.selectFile(Constants.GW2_APP_DATA_FILE + "\\Settings", "Choose a main.java.com.bloodybade4.gw2loganalysis.settings file.","Data Files", "xml");
					if (f == null)
						return;
					curSettings = XMLHelper.loadSettings(f.getAbsolutePath());
					updateTextFields();
				}
				catch (Exception ex) {
					DialogHelper.errorMessage("Error Window","Error trying to load the main.java.com.bloodybade4.gw2loganalysis.settings file.\n" + ex + "\n  Current main.java.com.bloodybade4.gw2loganalysis.settings will be kept.");
				}
			}
		});
		JButton btnDefaultSettings = new JButton("Default Settings");
		btnDefaultSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(DialogHelper.confirmationWindow("Are you sure you'd like to reset fields to default main.java.com.bloodybade4.gw2loganalysis.settings?\nAny selections made will be lost, unless saved to a file.")) {
					curSettings = new Settings();
					updateTextFields();
				}
			}
		});
		btnDefaultSettings.setToolTipText("Restore fields to default main.java.com.bloodybade4.gw2loganalysis.settings.");
		
		JLabel lblNewLabel_1 = new JLabel("Name of Settings");
		textFieldSettingsName = new JTextField();
		textFieldSettingsName.setColumns(10);
		
		JButton btnSaveSettings = new JButton("Save Settings");
		btnSaveSettings.setToolTipText("Save your main.java.com.bloodybade4.gw2loganalysis.settings to you computer with the name chosen in the \"Name of Settings\" field.");
		btnSaveSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = textFieldSettingsName.getText();
				if (name == null || name.isBlank()) {
					DialogHelper.errorMessage("Error Window", "Please enter a name for your main.java.com.bloodybade4.gw2loganalysis.settings file before saving.");
					return;
				}
				name += ".xml";
				saveTextFields();
				
				File dir = new File(Constants.GW2_APP_DATA_FILE + "\\Settings");
			     if (!dir.exists()) {dir.mkdir();}
				
				File file = new File(dir.getAbsolutePath() + "\\" + name);
				if (file.exists() && !file.isDirectory()) {
					if (!DialogHelper.confirmationWindow("This main.java.com.bloodybade4.gw2loganalysis.settings file name already exists. Would you like to override it?\nSelect \"Yes\" to write over past file.")) {
						System.out.println("Save main.java.com.bloodybade4.gw2loganalysis.settings file canceled.");
						return;
					}
				}
				
				try {
					ObjectMapper mapper = new XmlMapper();
					mapper.writeValue(file, curSettings);
				} catch(Exception e2) {
					e2.printStackTrace(); 
					DialogHelper.errorMessage("Error Window", "An error occured while saving your main.java.com.bloodybade4.gw2loganalysis.settings. This could be due to administrator privilages or your OS. \n Exception: " + e2);
				}
			}
		});
		
		JSeparator separator_2 = new JSeparator();
		
		JLabel lblNewLabel_8 = new JLabel("Settings:");
		GroupLayout gl_rightPanel = new GroupLayout(rightPanel);
		gl_rightPanel.setHorizontalGroup(
			gl_rightPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_rightPanel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(separator_2, GroupLayout.PREFERRED_SIZE, 276, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(gl_rightPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_rightPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_rightPanel.createSequentialGroup()
							.addGap(10)
							.addComponent(btnLoadSettings, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnDefaultSettings))
						.addComponent(lblNewLabel_8))
					.addContainerGap(44, Short.MAX_VALUE))
				.addGroup(gl_rightPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_rightPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(textFieldSettingsName, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE))
					.addGap(21)
					.addComponent(btnSaveSettings, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(33, Short.MAX_VALUE))
				.addGroup(Alignment.LEADING, gl_rightPanel.createSequentialGroup()
					.addComponent(parsingOptionsPanel, GroupLayout.PREFERRED_SIZE, 257, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(35, Short.MAX_VALUE))
		);
		gl_rightPanel.setVerticalGroup(
			gl_rightPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_rightPanel.createSequentialGroup()
					.addComponent(parsingOptionsPanel, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(separator_2, GroupLayout.PREFERRED_SIZE, 8, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_8)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_rightPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnDefaultSettings)
						.addComponent(btnLoadSettings))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_rightPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_rightPanel.createSequentialGroup()
							.addGap(13)
							.addComponent(textFieldSettingsName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblNewLabel_1)
						.addGroup(gl_rightPanel.createSequentialGroup()
							.addGap(10)
							.addComponent(btnSaveSettings)))
					.addGap(38))
		);
		rightPanel.setLayout(gl_rightPanel);
		
		panelMenu.setLayout(gl_panelMenu);
		
		
		panelFiltering = new AdvancedFilteringTab(curSettings);
		tabbedPane.addTab("Adv. Filtering", null, panelFiltering, null);

		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 618, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
		);
		frame.getContentPane().setLayout(groupLayout);
	}
	
	//TODO: exception catching here... sounds like a pain though.
	//Updates fields based on current main.java.com.bloodybade4.gw2loganalysis.settings.
	private static void updateTextFields() {
		textFieldSettingsName.setText(curSettings.getSettingsName());
		textFieldCharName.setText(curSettings.getCharName());
		formattedTextFieldMinDuration.setValue(curSettings.getMinDuration());
		
		comboBoxLocation.setSelectedItem(curSettings.getLocation());
		comboBoxEncounter.setSelectedItem(Constants.ENCOUNTER_IDS.get(curSettings.getLocation()).get(curSettings.getEncounterID()));
		comboBoxProfession.setSelectedItem(curSettings.getProfession());
		comboBoxSpecialization.setModel(new DefaultComboBoxModel<String>(Constants.profSpec.get(curSettings.getProfession()).toArray(String[]::new)));
		comboBoxSpecialization.setSelectedItem(curSettings.getSpecialization());
		
		panelSettings.updateFields(curSettings);
		panelFiltering.updateFields(curSettings);
		parsingOptionsPanel.updateFields(curSettings);
	}
	
	//saves current fields to main.java.com.bloodybade4.gw2loganalysis.settings.
	private void saveTextFields() {
		curSettings.setSettingsName(textFieldSettingsName.getText());
		curSettings.setCharName(textFieldCharName.getText());
		curSettings.setMinDuration(Integer.parseInt(formattedTextFieldMinDuration.getText().replaceAll(",","")));
		
		curSettings.setLocation(String.valueOf(comboBoxLocation.getSelectedItem()));
		curSettings.setEncounterID(Constants.retrieveEncounterIDInt(curSettings.getLocation(), String.valueOf(comboBoxEncounter.getSelectedItem())));
		curSettings.setProfession(String.valueOf(comboBoxProfession.getSelectedItem()));
		curSettings.setSpecialization(String.valueOf(comboBoxSpecialization.getSelectedItem()));
		
		panelFiltering.saveFields(curSettings);
		parsingOptionsPanel.saveFields(curSettings);
		panelSettings.saveFields(curSettings);
		
	}
	
	//Check to make sure that the required fields have been filled. 
	private Boolean readyToRun(Settings s) {
		File file = new File(curSettings.getOutputFile() + "\\" + curSettings.getOutputFileName() + ".csv");
		if (file.exists() && !file.isDirectory()) {//if file already exists.
			//if user would not like to override the file, return false. 
			if (!DialogHelper.confirmationWindow("This output file name already exists. Would you like to override it?\nSelect \"Yes\" to write over past file.")) 
				return false;
		}
		if (s.getCharName().isBlank()) 
			return DialogHelper.runProgramErrorMessage("The \"Name\" field is required to run the program. Please enter your character or account name, be careful with spelling.");
		if (s.getOutputFile().isBlank())
			return DialogHelper.runProgramErrorMessage("The \"Output Directory\" field is required to run the program. Please select a location for outputing results.");
		if(s.getOutputFileName().isBlank())
			return DialogHelper.runProgramErrorMessage("The \"Output Name\" field is required to run the program. Please input a name for your output file.");
		if (s.getInputFile().isBlank())
			return DialogHelper.runProgramErrorMessage("The \"Source Directory\" field is required to run the program. Please select the location of the logs you wish to analize.");
		
		//Check EI use: 
		if(s.getUseEI()) {
			if (s.getEIAbsPath() == null || s.getEIAbsPath().isBlank() || !(new File(s.getEIAbsPath()).exists()) )
				return DialogHelper.runProgramErrorMessage("The \"Elite Insights Directory\" is not filled correctly. If you wish to parse from .zvect files, you must select your Elite Insight's .exe file.");
		}
		
		return true;
	}
}