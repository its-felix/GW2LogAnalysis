package com.bloodyblade4.gw2loganalysis.gui_components;

import com.bloodyblade4.gw2loganalysis.settings.SettingsCategories;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsWindow {
    private static final String[] tableChecksColumns = {"Categories", "Active"};
    JFrame frame = new JFrame();
    private Map<String, DataModel> dataModelMap = new HashMap<String, DataModel>();
    private JButton btnNewButton;
    private JTabbedPane tabbedPane;
    private JButton btnNewButton_4;

    public StatsWindow(List<SettingsCategories> cats) {
        frame.setBounds(100, 100, 555, 447);
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        btnNewButton = new JButton("(un)Select All");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (DataModel dm : dataModelMap.values())
                    dm.toggleAll();
            }
        });

        btnNewButton_4 = new JButton("(un)Select Current Tab");
        btnNewButton_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = tabbedPane.getSelectedIndex();
                dataModelMap.get(cats.get(index).getDisplayName()).toggleAll();
            }
        });


        GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(btnNewButton)
                                                .addGap(18)
                                                .addComponent(btnNewButton_4, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(22)
                                                .addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(32)
                                .addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                                .addGap(34)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(btnNewButton)
                                        .addComponent(btnNewButton_4))
                                .addContainerGap())
        );

        //TODO: Modify the following so that the table shrinks horizontally as well.
        for (SettingsCategories cat : cats) {
            JPanel pan = new JPanel();

            tabbedPane.addTab(cat.getDisplayName(), null, pan, null);

            DataModel dm = new DataModel(cat.retrieveCompleteListNames(), tableChecksColumns);
            JTable table = new JTable(dm);
            JScrollPane catScrollPane = new JScrollPane();

            GroupLayout gl = new GroupLayout(pan);
            gl.setHorizontalGroup(
                    gl.createParallelGroup(Alignment.LEADING)
                            .addComponent(catScrollPane, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            gl.setVerticalGroup(
                    gl.createParallelGroup(Alignment.LEADING)
                            .addComponent(catScrollPane, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            gl.setAutoCreateContainerGaps(true);
            gl.setAutoCreateGaps(true);
            pan.setLayout(gl);

            catScrollPane.setLayout(new ScrollPaneLayout());
            catScrollPane.setViewportView(table);
            catScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            catScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            catScrollPane.setSize(new Dimension(25, 25));
            pan.setSize(new Dimension(25, 25));


            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.setSize(new Dimension(25, 25));


            dm.toggleGivenValues(cat.retrieveActiveListNames());
            dataModelMap.put(cat.getDisplayName(), dm);
        }
        frame.getContentPane().setLayout(groupLayout);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                for (SettingsCategories SC : cats) {
                    SC.updateActiveListNames(dataModelMap.get(SC.getDisplayName()).getSelectedValues());
                }
            }
        });
    }

    public void setWindowVisible() {
        frame.setVisible(true);
    }
}
