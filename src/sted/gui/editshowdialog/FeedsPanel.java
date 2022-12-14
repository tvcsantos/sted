/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FeedsPanel.java
 *
 * Created on 8/Nov/2009, 11:55:28
 */

package sted.gui.editshowdialog;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import pt.unl.fct.di.tsantos.util.net.RSSFeed;
import sted.datastructures.TVShowEntry;

/**
 *
 * @author tvcsantos
 */
public class FeedsPanel extends javax.swing.JPanel {

    private EditShowDialog parent;
    private TVShowEntry entry;
    //private List<RSSFeed> table;

    /** Creates new form FeedsPanel */
    public FeedsPanel() {
        initComponents();
        initData();
    }
    
    /*public FeedsPanel(EditShowDialog parent, TVShowEntry entry) {
        initComponents();
        this.parent = parent;
        this.entry = entry;
        //this.table = new ArrayList<RSSFeed>();
        initData();
    }*/

    public void setParent(EditShowDialog parent) {
        this.parent = parent;
    }

    public void setTVShowEntry(TVShowEntry entry) {
        this.entry = entry;
        initData();
    }

    private void initData() {
        if (entry != null) {
            DefaultTableModel model = (DefaultTableModel) feedsTable.getModel();
            model.getDataVector().clear();
            int id = 1;
            for (RSSFeed f : entry.getRSSFeeds()) {
                //table.add(f);
                model.addRow(new Object[] { id++,
                    f.getURL().toString(), f.getType() });
            }
        }
    }

    private List<RSSFeed> reconstruct() {
        //table.clear();
        List<RSSFeed> table = new LinkedList<RSSFeed>();
        DefaultTableModel model = (DefaultTableModel) feedsTable.getModel();
        int rows = model.getRowCount();
        int i = 0;
        while (i < rows) {
            int id = Integer.parseInt(model.getValueAt(i, 0).toString());
            String uri = (String) model.getValueAt(i, 1);
            RSSFeed.Type type = (RSSFeed.Type) model.getValueAt(i, 2);
            try {
                RSSFeed f = new RSSFeed(new URL(uri), type);
                table.add(f);
                i++;
            } catch (MalformedURLException ex) {
                if (uri.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Empty URI",
                            "Invalid URI", JOptionPane.ERROR_MESSAGE);
                }
                else JOptionPane.showMessageDialog(this, uri, "Invalid URI",
                        JOptionPane.ERROR_MESSAGE);
                return table;
            }
        }
        return table;
    }

    public List<RSSFeed> getRSSFeeds() {
        return reconstruct();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        feedsTable = new javax.swing.JTable();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        moveUpButton = new javax.swing.JButton();
        moveDownButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        openButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();

        setName("Form"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        feedsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "RSS Feed", "Type"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        feedsTable.setName("feedsTable"); // NOI18N
        jScrollPane1.setViewportView(feedsTable);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(sted.StedApp.class).getContext().getResourceMap(FeedsPanel.class);
        feedsTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("feedsTable.columnModel.title0")); // NOI18N
        feedsTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("feedsTable.columnModel.title1")); // NOI18N
        feedsTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("feedsTable.columnModel.title2")); // NOI18N

        addButton.setIcon(resourceMap.getIcon("addButton.icon")); // NOI18N
        addButton.setText(resourceMap.getString("addButton.text")); // NOI18N
        addButton.setName("addButton"); // NOI18N
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        removeButton.setIcon(resourceMap.getIcon("removeButton.icon")); // NOI18N
        removeButton.setText(resourceMap.getString("removeButton.text")); // NOI18N
        removeButton.setName("removeButton"); // NOI18N
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        moveUpButton.setIcon(resourceMap.getIcon("moveUpButton.icon")); // NOI18N
        moveUpButton.setText(resourceMap.getString("moveUpButton.text")); // NOI18N
        moveUpButton.setName("moveUpButton"); // NOI18N
        moveUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveUpButtonActionPerformed(evt);
            }
        });

        moveDownButton.setIcon(resourceMap.getIcon("moveDownButton.icon")); // NOI18N
        moveDownButton.setText(resourceMap.getString("moveDownButton.text")); // NOI18N
        moveDownButton.setName("moveDownButton"); // NOI18N
        moveDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveDownButtonActionPerformed(evt);
            }
        });

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setName("jSeparator1"); // NOI18N

        openButton.setIcon(resourceMap.getIcon("openButton.icon")); // NOI18N
        openButton.setText(resourceMap.getString("openButton.text")); // NOI18N
        openButton.setName("openButton"); // NOI18N
        openButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openButtonActionPerformed(evt);
            }
        });

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator2.setName("jSeparator2"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addButton)
                .addGap(8, 8, 8)
                .addComponent(removeButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(openButton)
                .addGap(8, 8, 8)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(moveUpButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(moveDownButton))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(removeButton)
                        .addComponent(addButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(openButton)
                            .addComponent(moveUpButton)
                            .addComponent(moveDownButton)))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        DefaultTableModel model = (DefaultTableModel) feedsTable.getModel();
        model.addRow(new Object[] { model.getRowCount() + 1,
                                        "", RSSFeed.Type.DEFINED });
    }//GEN-LAST:event_addButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        int row = feedsTable.getSelectedRow();
        if (row != -1) {
            int crow = row;
            while(crow < feedsTable.getRowCount() - 1) {
                move(false);
                crow++;
            }
            DefaultTableModel model = (DefaultTableModel) feedsTable.getModel();
            model.removeRow(crow);
        }
    }//GEN-LAST:event_removeButtonActionPerformed

    private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openButtonActionPerformed
        reconstruct();
        int row = feedsTable.getSelectedRow();
        if (row != -1) {
            RSSFeed feed = reconstruct().get(row);
            try {
                feed.browse();
            } catch (IOException ex) {
                Logger.getLogger(FeedsPanel.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_openButtonActionPerformed

    private void moveUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveUpButtonActionPerformed
        move(true);
    }//GEN-LAST:event_moveUpButtonActionPerformed

    private void moveDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveDownButtonActionPerformed
        move(false);
    }//GEN-LAST:event_moveDownButtonActionPerformed

    private void move(boolean up) {
        int row = feedsTable.getSelectedRow();
        if (up ? row > 0 : row < feedsTable.getRowCount() - 1) {
            DefaultTableModel model = (DefaultTableModel) feedsTable.getModel();
            model.moveRow(row, row, up ? --row : ++row);
            Integer i = (Integer) model.getValueAt(row, 0);
            model.setValueAt(up ? --i : ++i, row, 0);
            model.setValueAt(up ? ++i : --i, up ? ++row : --row, 0);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JTable feedsTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JButton moveDownButton;
    private javax.swing.JButton moveUpButton;
    private javax.swing.JButton openButton;
    private javax.swing.JButton removeButton;
    // End of variables declaration//GEN-END:variables

}
