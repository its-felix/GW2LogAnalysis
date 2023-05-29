

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;


public class BuffWindow {
	JFrame frame = new JFrame();
	
	private static JCheckBox chckbxBuffPhaseDuration;
	private static JCheckBox chckbxBuffPhaseActiveDuration;
	private static JCheckBox chckbxBuffUptime;
	private static JCheckBox chckbxBuffGenerationSelf;
	private static JCheckBox chckbxBuffGenerationGroup;
	
	private JTable tableBoonChecks;
	private DataModel dataModelBoonChecks;
	private static final String[] tableChecksColumns = {"Buff", "find"};
	private static final int CHECK_COL = 1;
	private JButton btnBoonSelectAll;
	
	public void setWindowVisible() {
		frame.setVisible(true);
	}
	
	BuffWindow(SettingsBuffs s, List<String> tableChecksList) {
		frame.setBounds(100, 100, 555, 447);
		
		JScrollPane scrollPane = new JScrollPane();
		
		chckbxBuffPhaseDuration = new JCheckBox("Phase Duration");
		
		chckbxBuffPhaseActiveDuration = new JCheckBox("Phase Active Duration");
		
		chckbxBuffUptime = new JCheckBox("Uptime");
		
		chckbxBuffGenerationSelf = new JCheckBox("Generation Self");
		
		chckbxBuffGenerationGroup = new JCheckBox("Generation Group");
		
		btnBoonSelectAll = new JButton("(un)Select All");
		btnBoonSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dataModelBoonChecks.toggleAll();
			}
		});
		
		
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(31)
							.addComponent(chckbxBuffPhaseDuration)
							.addGap(57)
							.addComponent(chckbxBuffPhaseActiveDuration))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnBoonSelectAll)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(chckbxBuffUptime)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(chckbxBuffGenerationSelf)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxBuffGenerationGroup))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(67)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 331, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(141, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxBuffPhaseDuration)
						.addComponent(chckbxBuffPhaseActiveDuration))
					.addGap(21)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(btnBoonSelectAll)
					.addGap(29)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxBuffUptime)
						.addComponent(chckbxBuffGenerationSelf)
						.addComponent(chckbxBuffGenerationGroup))
					.addGap(21))
		);
		
		//Initialize the boon table check marks. 
		dataModelBoonChecks = new DataModel(checkTableObject(tableChecksList), tableChecksColumns);
		tableBoonChecks = new JTable(dataModelBoonChecks);
		tableBoonChecks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		//Initialize all values according to settings. 
		chckbxBuffPhaseDuration.setSelected(s.getPhaseDuration());
		chckbxBuffPhaseActiveDuration.setSelected(s.getPhaseActiveDuration()); 
		chckbxBuffUptime.setSelected(s.getUptime()); 
		chckbxBuffGenerationSelf.setSelected(s.getGenerationSelf());
		chckbxBuffGenerationGroup.setSelected(s.getGenerationGroup()); 
		//"What is this display value?",
		dataModelBoonChecks.toggleGivenValues(s.getBuffs());
		
		scrollPane.setViewportView(tableBoonChecks);
		
		frame.getContentPane().setLayout(groupLayout);
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	s.setPhaseDuration(chckbxBuffPhaseDuration.isSelected()); 
		    	s.setPhaseActiveDuration(chckbxBuffPhaseActiveDuration.isSelected());
		    	s.setUptime(chckbxBuffUptime.isSelected());
		    	s.setGenerationSelf(chckbxBuffGenerationSelf.isSelected());
		    	s.setGenerationGroup(chckbxBuffGenerationGroup.isSelected());
		    	//"What is this display value?",
		    	s.setBuffs(dataModelBoonChecks.getSelectedValues());
		    }
		});
		
		
	}
	
	public void changeTableObject(List<String> list) {
		dataModelBoonChecks.setDataVector(checkTableObject(list), tableChecksColumns);
	}
	
	private static Object[][] checkTableObject(List<String> list) {
		Object[][] obj = new Object[list.size()][list.size()];
		for (int i = 0; i < list.size(); i++) {
			obj[i] = new Object[] {list.get(i), Boolean.FALSE};
		}
		return obj;
	}
	
	private class DataModel extends DefaultTableModel {
		//original, one check mark active code is from https://stackoverflow.com/questions/7920068/using-setvalueat-to-recreate-mutually-exclusive-check-boxes/7920159#7920159
		private Boolean toggleState = false;
		
	    public DataModel(Object[][] data, Object[] columnNames) {
	        super(data, columnNames);
	    }
	    
	    public void toggleAll() {
	    	for (int r = 0; r < getRowCount(); r++) {
	    		super.setValueAt(!toggleState, r, CHECK_COL);
	    	}
	    	toggleState = !toggleState;
	    }
	    
	    public List<String> getSelectedValues() {
	    	List<String> list = new ArrayList<String>();
	    	for (int r = 0; r < getRowCount(); r++) {
	    		if ((Boolean)getValueAt(r, CHECK_COL))
	    			list.add((String.valueOf(getValueAt(r,0))));
	    	}
	    	return list;
	    }
	    
	    public void toggleGivenValues(List<String> list) {
	    	if (list == null)
	    		return;
	    	for (int r = 0; r < getRowCount(); r++) {
	    		if (list.contains(String.valueOf(getValueAt(r,0)))) 
	    			super.setValueAt(true, r, CHECK_COL);
	    		else
	    			super.setValueAt(false, r, CHECK_COL);
	    	}
	    }
	    
	    //Is getColumnClass necessary? 
	    @Override
	    public Class<?> getColumnClass(int col) {
	        if (col == CHECK_COL) {
	            return getValueAt(0, CHECK_COL).getClass();
	        }
	        return super.getColumnClass(col);
	    }

	    @Override
	    public boolean isCellEditable(int row, int col) {
	        return col == CHECK_COL;
	    }
	}
}






