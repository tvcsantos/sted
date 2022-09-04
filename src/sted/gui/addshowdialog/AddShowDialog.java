/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AddShowDialog.java
 *
 * Created on 14/Nov/2009, 22:32:10
 */

package sted.gui.addshowdialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import sted.StedApp;
import sted.gui.StedView;
import sted.datastructures.tvshow.Episode;
import sted.datastructures.tvshow.TVShow;
import sted.datastructures.TVShowEntry;
import sted.datastructures.parseinfo.TVShowInfo;

/**
 *
 * @author tvcsantos
 */
public class AddShowDialog extends javax.swing.JDialog {

    private TVShow currentShow = null;
    private TVShowInfo currentShowInfo = null;
    private StedView parent = null;

    /** Creates new form AddShowDialog */
    private AddShowDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        jTextPane1.setText("Pick a show from the list...");
        nameLabel.setText("");
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD));
        addButton.setEnabled(false);
        showSubscribe(false);
                
        fillTable(StedApp.getApplication().getShowsInfo());
        addListeners();
    }

    public AddShowDialog(StedView view, boolean modal) {
        this(view.getFrame(), modal);
        parent = view;
    }

    private void fillTable(List<TVShowInfo> list) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        showTable.setModel(model);
        model.addColumn("TV Shows");
        for (TVShowInfo info : list) {
            model.addRow(new Object[] { info });
        }
        if (model.getRowCount() == 1)
            showTable.setRowSelectionInterval(0, 0);       
    }

    private void showSubscribe(boolean visible) {
        subscribePanel.setVisible(visible);
    }

    private void tableUpdated() {
        showSubscribe(false);
        addButton.setEnabled(false);
        currentShow = null;
        currentShowInfo = null;
        nameLabel.setText("");
        jTextPane1.setText("<html><h1>Loading info...</h1></html>");
        jTextPane1.repaint();
        final int index = showTable.getSelectedRow();
        if (index != -1) {
            final TVShowInfo info = (TVShowInfo) showTable.getModel().getValueAt(index, 0);
            nameLabel.setText(info.getName());
            new Thread() {
                int selected = index;
                @Override
                public void run() {
                    String html = info.getHTML();
                    TVShow show = info.grabInfo();
                    if (show == null) {
                        System.err.println(info.getName() + " FATAL ERROR!!");
                        return;
                    }
                    Episode next = show.getNextAirEpisode();
                    Episode prev = show.getPreviousAiredEpisode();
                    if (selected != showTable.getSelectedRow()) {
                        return;
                    }
                    jTextPane1.setText(html);
                    DateFormat sdf = DateFormat.getDateInstance(
                            DateFormat.FULL, Locale.US);
                    if (next != null) {
                        jLabel2.setText(next.getTitle());
                        String nextD = sdf.format(next.getAirDate());
                        jLabel4.setText("Season: " + next.getSeason().getNumber() +
                            ", Episode: " + next.getNumber() +
                            ". Will air on " + nextD + ".");
                    } else {
                        jLabel2.setText("");
                        jLabel4.setText("Season: " + prev.getSeason().getNumber() +
                            ", Episode: " + (prev.getNumber() + 1) +
                            " or Season " + (prev.getSeason().getNumber() + 1) +
                            ". Unknown air date.");

                    }
                    jLabel3.setText(prev.getTitle());
                    String prevD = sdf.format(prev.getAirDate());
                    jLabel5.setText("Season: " + prev.getSeason().getNumber() +
                            ", Episode: " + prev.getNumber() +
                            ". Aired on " + prevD + ".");
                    currentShow = show;
                    currentShowInfo = info;
                    addButton.setEnabled(true);
                    showSubscribe(true);
                }
            }.start();
        } else {
            jTextPane1.setText("Pick a show from the list...");
        }
    }

    private void addListeners() {
        showTable.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                tableUpdated();
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        showTable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        searchTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        subscribePanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        hdCheckBox = new javax.swing.JCheckBox();
        customRadioButton = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nextRadioButton = new javax.swing.JRadioButton();
        prevRadioButton = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        customButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sted.StedApp.class).getContext().getResourceMap(AddShowDialog.class);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setName("Form"); // NOI18N
        setResizable(false);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        showTable.setModel(new DefaultTableModel());
        showTable.setColumnSelectionAllowed(true);
        showTable.setName("showTable"); // NOI18N
        showTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        showTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(showTable);
        showTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jTextPane1.setContentType(resourceMap.getString("jTextPane1.contentType")); // NOI18N
        jTextPane1.setEditable(false);
        jTextPane1.setText(resourceMap.getString("jTextPane1.text")); // NOI18N
        jTextPane1.setName("jTextPane1"); // NOI18N
        jScrollPane2.setViewportView(jTextPane1);

        searchTextField.setText(resourceMap.getString("searchTextField.text")); // NOI18N
        searchTextField.setName("searchTextField"); // NOI18N
        searchTextField.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                searchTextFieldCaretUpdate(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        nameLabel.setText(resourceMap.getString("nameLabel.text")); // NOI18N
        nameLabel.setName("nameLabel"); // NOI18N

        subscribePanel.setName("subscribePanel"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        hdCheckBox.setText(resourceMap.getString("hdCheckBox.text")); // NOI18N
        hdCheckBox.setName("hdCheckBox"); // NOI18N

        buttonGroup1.add(customRadioButton);
        customRadioButton.setText(resourceMap.getString("customRadioButton.text")); // NOI18N
        customRadioButton.setName("customRadioButton"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        buttonGroup1.add(nextRadioButton);
        nextRadioButton.setSelected(true);
        nextRadioButton.setText(resourceMap.getString("nextRadioButton.text")); // NOI18N
        nextRadioButton.setName("nextRadioButton"); // NOI18N

        buttonGroup1.add(prevRadioButton);
        prevRadioButton.setText(resourceMap.getString("prevRadioButton.text")); // NOI18N
        prevRadioButton.setName("prevRadioButton"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        javax.swing.GroupLayout subscribePanelLayout = new javax.swing.GroupLayout(subscribePanel);
        subscribePanel.setLayout(subscribePanelLayout);
        subscribePanelLayout.setHorizontalGroup(
            subscribePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subscribePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(subscribePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                    .addComponent(nextRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(prevRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hdCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE))
                .addContainerGap())
        );
        subscribePanelLayout.setVerticalGroup(
            subscribePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subscribePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prevRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hdCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        customButton.setText(resourceMap.getString("customButton.text")); // NOI18N
        customButton.setName("customButton"); // NOI18N

        addButton.setText(resourceMap.getString("addButton.text")); // NOI18N
        addButton.setName("addButton"); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(customButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 601, Short.MAX_VALUE)
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 201, Short.MAX_VALUE)
                                .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(subscribePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nameLabel))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subscribePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customButton)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cancelButton)
                        .addComponent(addButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_searchTextFieldCaretUpdate
        String text = searchTextField.getText();
        if (text.isEmpty()) text = ".*";
        else text = ".*" + text + ".*";
        fillTable(StedApp.getApplication().getShowsInfo(text));
    }//GEN-LAST:event_searchTextFieldCaretUpdate

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        if (currentShowInfo != null && currentShow != null) {
            TVShowEntry entry =
                    new TVShowEntry(currentShowInfo.getName(), 
                    currentShowInfo, currentShow,
                    StedApp.getApplication().getSettingsDirectory());
            Episode next = currentShow.getNextAirEpisode();
            Episode prev = currentShow.getPreviousAiredEpisode();
            if (nextRadioButton.isSelected()) {
                if (next != null) {
                    entry.setSeasonAndEpisode(next.getSeason().getNumber(),
                            next.getNumber());
                } else if (prev != null) {
                    entry.setSeasonAndEpisode(prev.getSeason().getNumber(),
                            prev.getNumber() + 1);
                }
            } else if (prevRadioButton.isSelected()) {
                if (prev != null) {
                    entry.setSeasonAndEpisode(prev.getSeason().getNumber(),
                            prev.getNumber());
                }
            }
            entry.setMinSize(currentShowInfo.getMinSize());
            entry.setMaxSize(currentShowInfo.getMaxSize());
            entry.setMinSeeders(currentShowInfo.getMinSeeders());
            entry.setHD(hdCheckBox.isSelected());
            entry.startScheduling();
            parent.addTVShowEntry(entry);
            dispose();
        }
    }//GEN-LAST:event_addButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton customButton;
    private javax.swing.JRadioButton customRadioButton;
    private javax.swing.JCheckBox hdCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JRadioButton nextRadioButton;
    private javax.swing.JRadioButton prevRadioButton;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JTable showTable;
    private javax.swing.JPanel subscribePanel;
    // End of variables declaration//GEN-END:variables

}
