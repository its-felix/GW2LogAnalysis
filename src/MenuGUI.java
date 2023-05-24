import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ComboBoxModel;
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
 
 


/*
 * TODO: 
 * 
 * 
 * 
 * 
 */
 
public class MenuGUI {

	private JFrame frame;
	private static JTextField textFieldCharName;
	private static JTextField textFieldSourceDirectory;
	private static JTextField textFieldOutputDirectory;
	private static JTextField textFieldOutputName;
	
	static Settings curSettings; 
	private static JTextField textFieldSettingsName;
	private static JFormattedTextField formattedTextFieldMinDuration;
	
	private static JComboBox<String> comboBoxLocation;

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
					if(loadLastSettings() == false)
						curSettings = new Settings();
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
		frame.setBounds(100, 100, 555, 447);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 539, 408);
		frame.getContentPane().add(tabbedPane);
		
		JPanel panelMenu = new JPanel();
		tabbedPane.addTab("Menu", null, panelMenu, null);
		panelMenu.setLayout(null);
		
		//Run Activate the main program. 
		JButton btnNewButton = new JButton("Run");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Analysis parse=new Analysis(); 
				parse.main(curSettings);
			}
		});
		btnNewButton.setBounds(422, 349, 89, 23);
		panelMenu.add(btnNewButton);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 296, 534, 8);
		panelMenu.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(262, 0, 10, 294);
		panelMenu.add(separator_1);
		
		JLabel labelName = new JLabel("Name");
		labelName.setToolTipText("The character name for the character you're filtering for.");
		labelName.setBounds(10, 25, 46, 14);
		panelMenu.add(labelName);
		
		textFieldCharName = new JTextField();
		textFieldCharName.setBounds(66, 22, 186, 20);
		panelMenu.add(textFieldCharName);
		textFieldCharName.setColumns(10);
		
		textFieldSourceDirectory = new JTextField();
		textFieldSourceDirectory.setBounds(99, 306, 233, 20);
		panelMenu.add(textFieldSourceDirectory);
		textFieldSourceDirectory.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("Source Directory");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File f = selectDirectory();
				if (f == null)
					return;
				String fileName=f.getAbsolutePath();
				textFieldSourceDirectory.setText(fileName);
			}
		});
		btnNewButton_1.setBounds(0, 305, 89, 23);
		panelMenu.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Output directory");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

					File f = selectDirectory();
					if (f == null)
						return;
					String fileName=f.getAbsolutePath();
					textFieldOutputDirectory.setText(fileName);
			}
		});
		btnNewButton_2.setBounds(0, 331, 89, 23);
		panelMenu.add(btnNewButton_2);
		
		JLabel lblNewLabel = new JLabel("Output Name");
		lblNewLabel.setBounds(10, 358, 75, 14);
		panelMenu.add(lblNewLabel);
		
		textFieldOutputDirectory = new JTextField();
		textFieldOutputDirectory.setBounds(102, 331, 216, 20);
		panelMenu.add(textFieldOutputDirectory);
		textFieldOutputDirectory.setColumns(10);
		
		textFieldOutputName = new JTextField();
		textFieldOutputName.setBounds(96, 354, 226, 20);
		panelMenu.add(textFieldOutputName);
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
				String outputFile=directory.getAbsolutePath() + "\\" + name;
				System.out.println("outputFile is: " + outputFile);
				
				try {
					ObjectMapper mapper = new XmlMapper();
					mapper.writeValue(new File(outputFile), curSettings);
				} catch(Exception e2) {
					e2.printStackTrace(); 
				}
			}
		});
		btnSaveSettings.setBounds(422, 262, 102, 23);
		panelMenu.add(btnSaveSettings);
		
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
		btnLoadSettings.setBounds(422, 215, 102, 23);
		panelMenu.add(btnLoadSettings);
		
		textFieldSettingsName = new JTextField();
		textFieldSettingsName.setBounds(282, 265, 119, 20);
		panelMenu.add(textFieldSettingsName);
		textFieldSettingsName.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Name of Settings");
		lblNewLabel_1.setBounds(282, 252, 102, 14);
		panelMenu.add(lblNewLabel_1);
		
		DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<String>(Constants.LOCATIONS.keySet().toArray(String[]::new));
		comboBoxLocation = new JComboBox<>(comboModel);
		comboBoxLocation.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange()==ItemEvent.SELECTED) {
					curSettings.setLocation(String.valueOf(comboBoxLocation.getSelectedItem()));
				}
			}
		});
		comboBoxLocation.setBounds(76, 53, 176, 22);
		panelMenu.add(comboBoxLocation);
		
		JLabel lblNewLabel_2 = new JLabel("Location");
		lblNewLabel_2.setBounds(10, 57, 46, 14);
		panelMenu.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Min Duration (sec)");
		lblNewLabel_3.setBounds(10, 82, 89, 14);
		panelMenu.add(lblNewLabel_3);
		
		formattedTextFieldMinDuration = new JFormattedTextField();
		formattedTextFieldMinDuration.setBounds(134, 79, 118, 20);
		formattedTextFieldMinDuration.setValue(new Integer(0));
		formattedTextFieldMinDuration.setColumns(10);
		panelMenu.add(formattedTextFieldMinDuration);
		
		JPanel panelSettings = new JPanel();
		tabbedPane.addTab("Settings", null, panelSettings, null);
		
		
	}
	
	private static void updateTextFields() {
		textFieldCharName.setText(curSettings.getCharName());
		textFieldSourceDirectory.setText(curSettings.getInputFile());
		textFieldOutputDirectory.setText(curSettings.getOutputFile());
		textFieldOutputName.setText(curSettings.getOutputFileName());
		comboBoxLocation.setSelectedItem(curSettings.getLocation());
		formattedTextFieldMinDuration.setValue(curSettings.getMinDuration());
		System.out.println("Char name is now" + curSettings.getCharName());
	}
	
	private void saveTextFields() {
		curSettings.setCharName(textFieldCharName.getText());
		curSettings.setInputFile(textFieldSourceDirectory.getText());
		curSettings.setOutputFile(textFieldOutputDirectory.getText());
		curSettings.setOutputFileName(textFieldOutputName.getText());
		curSettings.setMinDuration(0);
		curSettings.setMinDuration(Integer.parseInt(formattedTextFieldMinDuration.getText()));
		
		
		
		
		
		curSettings.setLocation(String.valueOf(comboBoxLocation.getSelectedItem()));
		
		
		System.out.println("Char name is now" + curSettings.getCharName());
		
	}
	
	public static File selectDirectory() {
		File directory = new File(System.getProperty("user.dir"));
		JFileChooser chooser = new JFileChooser(directory);
		chooser.setDialogTitle("Select a directory.");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int option = chooser.showOpenDialog(null);
		if(option == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		}
		return null;
	}
	
	public static Boolean loadLastSettings()
	{
	    //File settingsDirFile = new File(".\\Settings");
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
	            	if (getFileExtension(fileName).equals("xml")) {
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
	
	public static void loadSettings(String fileName) {
		try {
			ObjectMapper mapper = new XmlMapper(); 
			InputStream inputStream = new FileInputStream(new File(fileName));
			TypeReference<Settings> typeRef = new TypeReference<Settings>() {};
			curSettings = mapper.readValue(inputStream, typeRef);
			inputStream.close();
			updateTextFields();
			textFieldSettingsName.setText(getFileName(fileName));
			//System.out.println("outputfile is: " + curSettings.getOutputFile() + "\\" + curSettings.getOutputFileName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public static String getFileExtension(String fileName) {
		int i = fileName.lastIndexOf('.');
		int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
		if (i > p) {
		    return fileName.substring(i+1);
		}
		return "";
	}
	public static String getFileName(String fileLoc) {
		Path p = Paths.get(fileLoc);
		String fileName = p.getFileName().toString();
		int i = fileName.lastIndexOf('.'); //remove file extension.
		
		return fileName.substring(0, i);
	}
}


