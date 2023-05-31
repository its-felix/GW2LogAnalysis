import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.DefaultComboBoxModel;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JFormattedTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.LayoutStyle.ComponentPlacement;
 


/*
 * TODO: 
 * BIGGEST: Find a better way to order/generate the info from JSON. right now it's chaos. 
 * 
 * 2. Find a way to insert the URL links too. One way would to to check the source file for files with the same name, but using the "url" extension. 
 * 
 * 4. warn if the application is open in another window, so it can't write to it? 
 * 
 * 
 */
 
public class MenuGUI {
	static Settings curSettings; 
	private JFrame frame;
	private static JLabel lblSourceJSONCount;
	private static JTextField textFieldCharName;
	private static JTextField textFieldSourceDirectory;
	private static JTextField textFieldOutputDirectory;
	private static JTextField textFieldOutputName;
	private static JTextField textFieldSettingsName;
	private static JFormattedTextField formattedTextFieldMinDuration;
	
	private static JComboBox<String> comboBoxLocation;
	private static JComboBox<String> comboBoxDesiredStats;
	private static JComboBox<String> comboBoxProfession;
	private static JComboBox<String> comboBoxSpecialization;
	
	private static JComboBox<String> comboBoxWepSet1MainHand;
	private static JComboBox<String> comboBoxWepSet1Offhand;
	private static JComboBox<String> comboBoxWepSet2MainHand;
	private static JComboBox<String> comboBoxWepSet2Offhand;
	private static JCheckBox chckbxWepCheckStrict;
	
	private static BuffWindow boonWindow;
	private static JCheckBox chckbxBoonsEnabled;
	private static BuffWindow professionBuffWindow;
	private static JCheckBox chckbxProfessionBuffsEnabled;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuGUI window = new MenuGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					if(findLastSettings() == false)
						curSettings = new Settings();
						boonWindow = new BuffWindow(curSettings.getBoonSettings(), Constants.BOON_TABLE_CHECKS);
						professionBuffWindow = new BuffWindow(curSettings.getBoonSettings(), 
								new ArrayList<String>(Constants.PROFESSION_BUFFS_TABLE_CHECKS.values().stream()
										.flatMap(List::stream).collect(Collectors.toList())) 
								);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MenuGUI() {
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 555, 473);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<String>(Constants.LOCATIONS.keySet().toArray(String[]::new));
		
		DefaultComboBoxModel<String> cm = new DefaultComboBoxModel<String>(Constants.DESIRED_STATS.toArray(String[]::new));
		
		JPanel panelMenu = new JPanel();
		tabbedPane.addTab("Menu", null, panelMenu, null);
		
		//Run Activate the main program. 
		JButton btnNewButton = new JButton("Run");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Analysis parse=new Analysis(); 
				File file = new File(curSettings.getOutputFile() + "\\" + curSettings.getOutputFileName() + ".csv");
				if (file.exists() && !file.isDirectory()) {
					if (!confirmationWindow("This settings file name already exists. Would you like to override it?\nSelect \"Yes\" to write over past file.")) {
						return;
					}
				}
				saveTextFields();
				
				Analysis.main(curSettings);
			}
		});
		
		JSeparator separator = new JSeparator();
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		
		JLabel labelName = new JLabel("Name");
		labelName.setToolTipText("The character name for the character you're filtering for.");
		
		textFieldCharName = new JTextField();
		textFieldCharName.setColumns(10);
		
		textFieldSourceDirectory = new JTextField();
		textFieldSourceDirectory.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("Source Directory");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName= selectDirectory("JSON File", "json");
				if (fileName == null)
					return;
				File f = new File(fileName);
				textFieldSourceDirectory.setText(fileName);
				lblSourceJSONCount.setText("Files Found: "+FileHelper.getFileCount(f, ".json"));
			}
		});
		
		JButton btnNewButton_2 = new JButton("Output directory");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					String fileName=selectDirectory("csv SpreadSheet", "csv");
					if (fileName == null)
						return;
					textFieldOutputDirectory.setText(fileName);
			}
		});
		
		JLabel lblNewLabel = new JLabel("Output Name");
		
		textFieldOutputDirectory = new JTextField();
		textFieldOutputDirectory.setColumns(10);
		
		textFieldOutputName = new JTextField();
		textFieldOutputName.setColumns(10);
		
		JButton btnSaveSettings = new JButton("Save Settings");
		btnSaveSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = textFieldSettingsName.getText();
				if (name == null || name.length() == 0)
					name = "settings";
				name += ".xml";
				saveTextFields();
				
				File directory = new File(System.getProperty("user.dir") +"\\Settings");
				
				File file = new File(directory.getAbsolutePath() + "\\" + name);
				if (file.exists() && !file.isDirectory()) {
					if (!confirmationWindow("This settings file name already exists. Would you like to override it?\nSelect \"Yes\" to write over past file.")) {
						System.out.println("Save settings file canceled.");
						return;
					}
				}
				
				
				try {
					ObjectMapper mapper = new XmlMapper();
					mapper.writeValue(file, curSettings);
				} catch(Exception e2) {
					e2.printStackTrace(); 
				}
			}
		});
		
		JButton btnLoadSettings = new JButton("Load Settings");
		btnLoadSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File directory = new File(System.getProperty("user.dir") + "\\Settings");
				JFileChooser chooser = new JFileChooser(directory);
				chooser.setDialogTitle("Choose a settings file.");
				chooser.addChoosableFileFilter(new FileNameExtensionFilter("Data Files", "xml"));
				chooser.setAcceptAllFileFilterUsed(true);
				

				int option = chooser.showOpenDialog(null);
					if(option == JFileChooser.APPROVE_OPTION) {
					File f = chooser.getSelectedFile();
					String fileName=f.getAbsolutePath();
					loadSettings(fileName);
				}
			}
		});
		
		textFieldSettingsName = new JTextField();
		textFieldSettingsName.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Name of Settings");
		comboBoxLocation = new JComboBox<>(comboModel);
		comboBoxLocation.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange()==ItemEvent.SELECTED) {
					curSettings.setLocation(String.valueOf(comboBoxLocation.getSelectedItem()));
				}
			}
		});
		
		JLabel lblNewLabel_2 = new JLabel("Location");
		lblNewLabel_2.setToolTipText("Desired location of the battle.");
		
		JLabel lblNewLabel_3 = new JLabel("Min Duration (sec)");
		lblNewLabel_3.setToolTipText("Minimum duration of the fight log for it to be parsed into csv file, in seconds.");
		
		formattedTextFieldMinDuration = new JFormattedTextField();
		formattedTextFieldMinDuration.setValue(Integer.valueOf(0));
		formattedTextFieldMinDuration.setColumns(10);
		comboBoxDesiredStats = new JComboBox<>(cm);
		comboBoxDesiredStats.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange()==ItemEvent.SELECTED) {
					curSettings.setDesiredStats(String.valueOf(comboBoxDesiredStats.getSelectedItem()));
				}
			}
		});
		
		JLabel lblNewLabel_4 = new JLabel("Desired Stats");
		lblNewLabel_4.setToolTipText("Stats to display (additional filering can be done in the Settings tab.)");
		
		JLabel lblNewLabel_5 = new JLabel("Profession");
		lblNewLabel_5.setToolTipText("Profession/class of character");
		
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
					if (v.equals("Any"))
						professionBuffWindow = new BuffWindow(curSettings.getBoonSettings(), 
								new ArrayList<String>(Constants.PROFESSION_BUFFS_TABLE_CHECKS.values().stream()
										.flatMap(List::stream).collect(Collectors.toList())) 
								);
					else 
					professionBuffWindow.changeTableObject(Constants.PROFESSION_BUFFS_TABLE_CHECKS.get(v));
				}
			}
		});
		
		JLabel lblNewLabel_6 = new JLabel("Specialization");
		lblNewLabel_6.setToolTipText("Specialization of you character.");
		
		
		comboBoxSpecialization = new JComboBox<String>();
		comboBoxSpecialization.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange()==ItemEvent.SELECTED) {
					curSettings.setSpecialization(String.valueOf(comboBoxSpecialization.getSelectedItem()));
				}
			}
		});
		
		lblSourceJSONCount = new JLabel("Files Detected: 0");
		
		comboBoxWepSet1MainHand = new JComboBox<String>(new DefaultComboBoxModel<String>(Constants.WEAPONS.toArray(String[]::new)));
		
		JLabel lblNewLabel_7 = new JLabel("MainHand");
		
		JLabel lblNewLabel_8 = new JLabel("Offhand");
		
		comboBoxWepSet1Offhand = new JComboBox<String>(new DefaultComboBoxModel<String>(Constants.WEAPONS.toArray(String[]::new)));
		
		JLabel lblNewLabel_9 = new JLabel("MainHand");
		
		comboBoxWepSet2MainHand = new JComboBox<String>(new DefaultComboBoxModel<String>(Constants.WEAPONS.toArray(String[]::new)));
		
		comboBoxWepSet2Offhand = new JComboBox<String>(new DefaultComboBoxModel<String>(Constants.WEAPONS.toArray(String[]::new)));
		
		JLabel lblNewLabel_10 = new JLabel("Offhand");
		
		JLabel lblNewLabel_11 = new JLabel("Weapons");
		
		chckbxWepCheckStrict = new JCheckBox("Strict Wep Filter");
		chckbxWepCheckStrict.setToolTipText("Ignore any file that has \"Unknown\" in your weapon field. ArcDPS can sometimes not register a weapon for various reasons, such as never swapping weapons.");
		
		JButton btnSwapWeaponSets = new JButton("Swap Weapon Sets");
		btnSwapWeaponSets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String temp = String.valueOf(comboBoxWepSet1MainHand.getSelectedItem());
				comboBoxWepSet1MainHand.setSelectedItem(String.valueOf(comboBoxWepSet2MainHand.getSelectedItem()));
				comboBoxWepSet2MainHand.setSelectedItem(temp);
				
				temp = String.valueOf(comboBoxWepSet1Offhand.getSelectedItem());
				comboBoxWepSet1Offhand.setSelectedItem(comboBoxWepSet2Offhand.getSelectedItem());
				comboBoxWepSet2Offhand.setSelectedItem(temp);
				
			}
		});
		btnSwapWeaponSets.setToolTipText("Swap weapon set selections. The order of the sets does matter.");
		
		JButton btnDefaultSettings = new JButton("Default Settings");
		btnDefaultSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(confirmationWindow("Are you sure you'd like to reset fields to default settings?\nAny selections made will be lost, unless saved to a file.")) {
					curSettings = new Settings();
					updateTextFields();
				}
			}
		});
		btnDefaultSettings.setToolTipText("Restore fields to default settings.");
		GroupLayout gl_panelMenu = new GroupLayout(panelMenu);
		gl_panelMenu.setHorizontalGroup(
			gl_panelMenu.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelMenu.createSequentialGroup()
					.addGap(10)
					.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelMenu.createSequentialGroup()
							.addComponent(labelName, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(textFieldCharName, GroupLayout.PREFERRED_SIZE, 186, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelMenu.createSequentialGroup()
							.addComponent(lblNewLabel_5, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
							.addGap(26)
							.addComponent(comboBoxProfession, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelMenu.createSequentialGroup()
							.addComponent(lblNewLabel_6, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addGap(33)
							.addComponent(comboBoxSpecialization, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelMenu.createSequentialGroup()
							.addComponent(lblNewLabel_2, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
							.addGap(20)
							.addComponent(comboBoxLocation, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelMenu.createSequentialGroup()
							.addComponent(lblNewLabel_3, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
							.addGap(35)
							.addComponent(formattedTextFieldMinDuration, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelMenu.createSequentialGroup()
							.addComponent(lblNewLabel_4, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addGap(33)
							.addComponent(comboBoxDesiredStats, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)))
					.addGap(10)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
					.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_panelMenu.createSequentialGroup()
								.addGap(10)
								.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_panelMenu.createSequentialGroup()
										.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
											.addComponent(textFieldSettingsName, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
											.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
											.addComponent(btnDefaultSettings))
										.addGap(21)
										.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
											.addComponent(btnLoadSettings, GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
											.addComponent(btnSaveSettings, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addGap(20))
									.addGroup(gl_panelMenu.createSequentialGroup()
										.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
											.addComponent(lblNewLabel_7)
											.addComponent(comboBoxWepSet1MainHand, 0, 122, Short.MAX_VALUE)
											.addGroup(gl_panelMenu.createSequentialGroup()
												.addComponent(lblNewLabel_9)
												.addPreferredGap(ComponentPlacement.RELATED))
											.addComponent(comboBoxWepSet2MainHand, 0, 122, Short.MAX_VALUE))
										.addGap(21)
										.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
											.addComponent(lblNewLabel_10)
											.addComponent(lblNewLabel_8)
											.addGroup(gl_panelMenu.createParallelGroup(Alignment.TRAILING, false)
												.addComponent(comboBoxWepSet1Offhand, Alignment.LEADING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(comboBoxWepSet2Offhand, Alignment.LEADING, 0, 106, Short.MAX_VALUE))
											.addComponent(chckbxWepCheckStrict))
										.addContainerGap())))
							.addGroup(gl_panelMenu.createSequentialGroup()
								.addGap(18)
								.addComponent(lblNewLabel_11)
								.addContainerGap()))
						.addGroup(gl_panelMenu.createSequentialGroup()
							.addGap(80)
							.addComponent(btnSwapWeaponSets)
							.addContainerGap())))
				.addComponent(separator, GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
				.addGroup(gl_panelMenu.createSequentialGroup()
					.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(textFieldSourceDirectory, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblSourceJSONCount)
					.addContainerGap(124, Short.MAX_VALUE))
				.addGroup(gl_panelMenu.createSequentialGroup()
					.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
						.addComponent(btnNewButton_2, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panelMenu.createSequentialGroup()
							.addGap(10)
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)))
					.addGap(7)
					.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelMenu.createSequentialGroup()
							.addGap(6)
							.addComponent(textFieldOutputDirectory, GroupLayout.PREFERRED_SIZE, 216, GroupLayout.PREFERRED_SIZE))
						.addComponent(textFieldOutputName, GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE))
					.addGap(100)
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
		);
		gl_panelMenu.setVerticalGroup(
			gl_panelMenu.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelMenu.createSequentialGroup()
					.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelMenu.createSequentialGroup()
							.addGap(22)
							.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelMenu.createSequentialGroup()
									.addGap(3)
									.addComponent(labelName))
								.addComponent(textFieldCharName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(12)
							.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelMenu.createSequentialGroup()
									.addGap(4)
									.addComponent(lblNewLabel_5))
								.addComponent(comboBoxProfession, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(8)
							.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel_6)
								.addGroup(gl_panelMenu.createSequentialGroup()
									.addGap(3)
									.addComponent(comboBoxSpecialization, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
							.addGap(18)
							.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelMenu.createSequentialGroup()
									.addGap(4)
									.addComponent(lblNewLabel_2))
								.addComponent(comboBoxLocation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(4)
							.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelMenu.createSequentialGroup()
									.addGap(3)
									.addComponent(lblNewLabel_3))
								.addComponent(formattedTextFieldMinDuration, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(8)
							.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelMenu.createSequentialGroup()
									.addGap(4)
									.addComponent(lblNewLabel_4))
								.addComponent(comboBoxDesiredStats, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 294, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panelMenu.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panelMenu.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblNewLabel_11)
								.addComponent(chckbxWepCheckStrict))
							.addGap(3)
							.addGroup(gl_panelMenu.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel_7)
								.addComponent(lblNewLabel_8))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panelMenu.createParallelGroup(Alignment.BASELINE)
								.addComponent(comboBoxWepSet1MainHand, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxWepSet1Offhand, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_panelMenu.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel_9)
								.addComponent(lblNewLabel_10))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panelMenu.createParallelGroup(Alignment.BASELINE)
								.addComponent(comboBoxWepSet2MainHand, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxWepSet2Offhand, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(15)
							.addComponent(btnSwapWeaponSets)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelMenu.createSequentialGroup()
									.addGap(50)
									.addComponent(btnDefaultSettings)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
										.addComponent(lblNewLabel_1)
										.addGroup(gl_panelMenu.createSequentialGroup()
											.addGap(13)
											.addComponent(textFieldSettingsName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
								.addGroup(gl_panelMenu.createSequentialGroup()
									.addGap(47)
									.addComponent(btnLoadSettings)
									.addGap(24)
									.addComponent(btnSaveSettings)))))
					.addGap(2)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 8, GroupLayout.PREFERRED_SIZE)
					.addGap(1)
					.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
						.addComponent(btnNewButton_1)
						.addGroup(gl_panelMenu.createSequentialGroup()
							.addGap(1)
							.addGroup(gl_panelMenu.createParallelGroup(Alignment.BASELINE)
								.addComponent(textFieldSourceDirectory, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblSourceJSONCount))))
					.addGap(3)
					.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelMenu.createSequentialGroup()
							.addComponent(btnNewButton_2)
							.addGap(4)
							.addComponent(lblNewLabel))
						.addGroup(gl_panelMenu.createSequentialGroup()
							.addComponent(textFieldOutputDirectory, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(3)
							.addComponent(textFieldOutputName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelMenu.createSequentialGroup()
							.addGap(18)
							.addComponent(btnNewButton))))
		);
		panelMenu.setLayout(gl_panelMenu);
		
		JPanel panelSettings = new JPanel();
		tabbedPane.addTab("Settings", null, panelSettings, null);
		
		JButton btnOpenBoonsWindow = new JButton("Boons Settings");
		btnOpenBoonsWindow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boonWindow.setWindowVisible();
			}
		});
		
		chckbxBoonsEnabled = new JCheckBox("Boons Enabled");
		
		JButton btnOpenProfessionBuffsWindow = new JButton("Profession Buffs Settings");
		btnOpenProfessionBuffsWindow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				professionBuffWindow.setWindowVisible();
			}
		});
		
		chckbxProfessionBuffsEnabled = new JCheckBox("Profession Buffs Enabled");
		
		GroupLayout gl_panelSettings = new GroupLayout(panelSettings);
		gl_panelSettings.setHorizontalGroup(
			gl_panelSettings.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelSettings.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelSettings.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(btnOpenProfessionBuffsWindow, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnOpenBoonsWindow, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelSettings.createParallelGroup(Alignment.LEADING)
						.addComponent(chckbxBoonsEnabled, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
						.addComponent(chckbxProfessionBuffsEnabled))
					.addContainerGap(261, Short.MAX_VALUE))
		);
		gl_panelSettings.setVerticalGroup(
			gl_panelSettings.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelSettings.createSequentialGroup()
					.addGap(24)
					.addGroup(gl_panelSettings.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnOpenBoonsWindow)
						.addComponent(chckbxBoonsEnabled))
					.addGap(28)
					.addGroup(gl_panelSettings.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnOpenProfessionBuffsWindow)
						.addComponent(chckbxProfessionBuffsEnabled))
					.addContainerGap(282, Short.MAX_VALUE))
		);
		panelSettings.setLayout(gl_panelSettings);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane)
		);
		frame.getContentPane().setLayout(groupLayout);
		/* TABLE HERE */ 
		
		
	}
	
	//TODO: exception catching here... sounds like a pain though.
	//Updates fields based on current settings. 
	private static void updateTextFields() {
		textFieldCharName.setText(curSettings.getCharName());
		textFieldSourceDirectory.setText(curSettings.getInputFile());
		textFieldOutputDirectory.setText(curSettings.getOutputFile());
		textFieldOutputName.setText(curSettings.getOutputFileName());
		if(curSettings.getInputFile()!= null && curSettings.getInputFile().length() >0)
			lblSourceJSONCount.setText("Files Found: "+FileHelper.getFileCount(new File(curSettings.getInputFile()), ".json"));
		
		comboBoxLocation.setSelectedItem(curSettings.getLocation());
		comboBoxDesiredStats.setSelectedItem(curSettings.getDesiredStats());
		comboBoxProfession.setSelectedItem(curSettings.getProfession());
		comboBoxSpecialization.setSelectedItem(curSettings.getSpecialization());
		comboBoxSpecialization.setModel(new DefaultComboBoxModel<String>(Constants.profSpec.get(curSettings.getProfession()).toArray(String[]::new)));
		
		comboBoxWepSet1MainHand.setSelectedItem(curSettings.getWepSet1MainHand());
		comboBoxWepSet2Offhand.setSelectedItem(curSettings.getWepSet2Offhand());
		comboBoxWepSet2MainHand.setSelectedItem(curSettings.getWepSet2MainHand());
		comboBoxWepSet2Offhand.setSelectedItem(curSettings.getWepSet2Offhand());
		chckbxWepCheckStrict.setSelected(curSettings.getWepFilterStrict());
		
		//buffs
		chckbxBoonsEnabled.setSelected(curSettings.getBoonSettings().getDisplay());
		//boonWindow.updateFields(curSettings.getBoonSettings());
		chckbxProfessionBuffsEnabled.setSelected(curSettings.getProfessionBuffsSettings().getDisplay());
		//professionBuffWindow.updateFields(curSettings.getProfessionBuffsSettings());
		
		formattedTextFieldMinDuration.setValue(curSettings.getMinDuration());
		System.out.println("Char name is now" + curSettings.getCharName());
	}
	
	
	//saves current fields to settings. 
	private void saveTextFields() {
		curSettings.setCharName(textFieldCharName.getText());
		curSettings.setInputFile(textFieldSourceDirectory.getText());
		curSettings.setOutputFile(textFieldOutputDirectory.getText());
		curSettings.setOutputFileName(textFieldOutputName.getText());
		curSettings.setMinDuration(Integer.valueOf(formattedTextFieldMinDuration.getText()));
		curSettings.setMinDuration(Integer.parseInt(formattedTextFieldMinDuration.getText()));
		
		
		curSettings.setLocation(String.valueOf(comboBoxLocation.getSelectedItem()));
		curSettings.setDesiredStats(String.valueOf(comboBoxDesiredStats.getSelectedItem()));
		curSettings.setProfession(String.valueOf(comboBoxProfession.getSelectedItem()));
		curSettings.setSpecialization(String.valueOf(comboBoxSpecialization.getSelectedItem()));
		
		curSettings.setWepSet1MainHand(String.valueOf(comboBoxWepSet1MainHand.getSelectedItem()));
		curSettings.setWepSet1Offhand(String.valueOf(comboBoxWepSet1Offhand.getSelectedItem()));
		curSettings.setWepSet2MainHand(String.valueOf(comboBoxWepSet2MainHand.getSelectedItem()));
		curSettings.setWepSet2Offhand(String.valueOf(comboBoxWepSet2Offhand.getSelectedItem()));
		curSettings.setWepFilterStrict(chckbxWepCheckStrict.isSelected());
		
		//buffs
		SettingsBuffs sb = boonWindow.saveFields(chckbxBoonsEnabled.isSelected());
		System.out.println("this is the returned settings: " + sb.getPhaseActiveDuration() + ", " + sb.getPhaseDuration());
		curSettings.setBoonSettings(boonWindow.saveFields(chckbxBoonsEnabled.isSelected()));
		System.out.println("Just saved boon settings: " + curSettings.getBoonSettings().getPhaseActiveDuration() + ", " + curSettings.getBoonSettings().getPhaseDuration());
		curSettings.setProfessionBuffsSettings(professionBuffWindow.saveFields(chckbxProfessionBuffsEnabled.isSelected()));
		
		System.out.println("Char name is now" + curSettings.getCharName());
		
	}
	
	//TODO: Should the following functions be moved to a helper class and into settings? 
	
	public static String selectDirectory(String extDetail, String extName) {
		File directory = new File(System.getProperty("user.dir"));
		JFileChooser chooser = new JFileChooser(directory) {
	            public void approveSelection() {
	                if (getSelectedFile().isFile()) {
	                	String name = getSelectedFile().getAbsolutePath();
	                	int p = Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\'));
	                	super.setSelectedFile(new File(name.substring(0, p)));
	                }
                    super.approveSelection();
	            }
	    };
		chooser.setDialogTitle("Select a directory.");
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		if (extDetail != null && extName != null)
			chooser.setFileFilter(new FileNameExtensionFilter(extDetail, extName));
		int option = chooser.showOpenDialog(null);
		if(option == JFileChooser.APPROVE_OPTION) {
			String path = chooser.getSelectedFile().getAbsolutePath();
			return path;
		}
		return null;
	}
	

	
	/*
	 * Searches through settings file for most recently used settings file.
	 * If found, will load it. If not found, returns false and allows creation of new settings.  
	 */
	public static Boolean findLastSettings()
	{
	    File directory = new File(System.getProperty("user.dir") +"\\Settings");
	    System.out.println(directory.getPath());
	    File[] files = directory.listFiles(File::isFile);
	    long lastModifiedTime = Long.MIN_VALUE;
	    File chosenFile = null;
	    
	    if (files != null)
	    {
	        for (File file : files)
	        {
	        	System.out.println("checking file");
	            if (file.lastModified() > lastModifiedTime)
	            {
	            	String fileName = file.getName();
	            	if (FileHelper.getFileExtension(fileName).equals("xml")) {
	            		System.out.println("found file." + fileName);
		    			chosenFile = file;
		                lastModifiedTime = file.lastModified();
	            	}
	            }
	        }
	    }
	    
	    if (chosenFile == null)
	    	return false;
	    
	    loadSettings(chosenFile.getAbsolutePath());
	    return true;
	}
	
	//given the absolute path of the settings file, will load and run functions to update settings and fields. 
	public static void loadSettings(String fileName) {
		try {
			ObjectMapper mapper = new XmlMapper(); 
			InputStream inputStream = new FileInputStream(new File(fileName));
			TypeReference<Settings> typeRef = new TypeReference<Settings>() {};
			curSettings = mapper.readValue(inputStream, typeRef);
			System.out.println("values in boon settings: " + curSettings.getBoonSettings().getPhaseActiveDuration() +", " + curSettings.getBoonSettings().getPhaseDuration());
			boonWindow = new BuffWindow(curSettings.getBoonSettings(), Constants.BOON_TABLE_CHECKS);
			if (curSettings.getProfession().equals("Any"))
				professionBuffWindow = new BuffWindow(curSettings.getBoonSettings(), 
						new ArrayList<String>(Constants.PROFESSION_BUFFS_TABLE_CHECKS.values().stream()
								.flatMap(List::stream).collect(Collectors.toList())) 
						);
			else
				professionBuffWindow = new BuffWindow(curSettings.getBoonSettings(), Constants.PROFESSION_BUFFS_TABLE_CHECKS.get("Elementalist"));
			
			inputStream.close();
			updateTextFields();
			textFieldSettingsName.setText(FileHelper.getFileName(fileName));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	public static Boolean confirmationWindow(String message) {
		int dialogResult = JOptionPane.showConfirmDialog(null, message);
		return dialogResult == JOptionPane.YES_OPTION;
	}
}



