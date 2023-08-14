package com.bloodyblade4.gw2loganalysis.gui_components;

import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.bloodyblade4.gw2loganalysis.components.Constants;
import com.bloodyblade4.gw2loganalysis.settings.Settings;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class DatePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	UtilDateModel dateModelFrom;
	UtilDateModel dateModelTo;
	JDatePanelImpl datePanelFrom;
	private static JDatePickerImpl datePickerFrom;
	JDatePanelImpl datePanelTo;
	private static JDatePickerImpl datePickerTo;
	private static JCheckBox chckbxDatesByLog;
	
	public DatePanel() {
		this.setVisible(true);
		
		Properties dates = new Properties();
		DateComponentFormatter dateFormat = new DateComponentFormatter();
		//try (InputStream input = new FileInputStream(System.getProperty("user.dir") +"\\src\\resources\\constDates.properties")) {
		try (InputStream input = Constants.class.getResourceAsStream("/constDates.properties")) {
			 dates.load(input);
			 
				
				dateModelFrom = new UtilDateModel();
				datePanelFrom = new JDatePanelImpl(dateModelFrom, dates);
				
				dateModelTo = new UtilDateModel();
				datePanelTo = new JDatePanelImpl(dateModelTo, dates);
		}
		catch (Exception e) {
			System.out.println("Something went wrong generating JDatePicker... " + e);
		}
		
		JLabel lblNewLabel_13 = new JLabel("End Date:");
		lblNewLabel_13.setToolTipText("Logs will be parse if they were recorded on or before this date.");
		
		datePickerTo = new JDatePickerImpl(datePanelTo, dateFormat);
		datePickerTo.setBounds(90,250,160,30);

		datePickerFrom = new JDatePickerImpl(datePanelFrom, dateFormat);
		datePickerFrom.setBounds(90,220,160,30);
		
		JLabel lblNewLabel = new JLabel("Start Date:");
		lblNewLabel.setToolTipText("Parse log on or after this date.");
		
		JLabel lblNewLabel_1 = new JLabel("End Date:");
		lblNewLabel_1.setToolTipText("Parse logs on or before this date.");
		
		chckbxDatesByLog = new JCheckBox("Dates by log (Not recommended)");
		chckbxDatesByLog.setToolTipText("<html>(Not recommended) <br>By default dates are checked via the \"last modified\" timestamp on your PC. <br>By checking this option, date will be checked inside of each log.<br>This can significaly slow performance, depending on use-case, but can be useful if you've modified those files after generation.</html>");
		
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel)
								.addComponent(lblNewLabel_1))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(datePickerTo, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
								.addComponent(datePickerFrom, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(29)
							.addComponent(chckbxDatesByLog)))
					.addContainerGap(25, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(chckbxDatesByLog)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel)
						.addComponent(datePickerFrom, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(datePickerTo, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1))
					.addContainerGap())
		);
		this.setLayout(groupLayout);
	}
	
	public void updateFields(Settings s) {
		chckbxDatesByLog.setSelected(s.getDatesByLog());
		Date tempDate = s.getFromDate();
		if(tempDate != null) {
			datePickerFrom.getModel().setDate(tempDate.getYear()+1900, tempDate.getMonth(), tempDate.getDate());
			datePickerFrom.getModel().setSelected(true);
		}
		tempDate = s.getToDate(); 
		if (tempDate!= null) {
			datePickerTo.getModel().setDate(tempDate.getYear()+1900, tempDate.getMonth(), tempDate.getDate());
			datePickerTo.getModel().setSelected(true);
		}
	}
	
	public void saveFields(Settings s) {
		s.setDatesByLog(chckbxDatesByLog.isSelected());
		if(datePickerFrom.getModel().getValue() == null)
			s.setFromDate(null);
		else
			s.setFromDate(datePickerFrom.getModel().getYear(), datePickerFrom.getModel().getMonth(), datePickerFrom.getModel().getDay());
		
		if (datePickerTo.getModel().getValue() == null)
			s.setToDate(null);
		else
			s.setToDate(datePickerTo.getModel().getYear(), datePickerTo.getModel().getMonth(), datePickerTo.getModel().getDay());
	}
}
