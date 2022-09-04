/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sted.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.Serializable;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import sted.datastructures.IconDB;
import sted.datastructures.TVShowEntry;
import sted.datastructures.TVShowEntry.State;

/**
 *
 * @author tvcsantos
 */
public class TableCellRendererImpl extends JLabel implements TableCellRenderer,
        Serializable {

    private static final Font SMALL_FONT = new Font("Dialog", 0, 10);
    private static final Font LARGE_FONT = new Font("Dialog", 0, 15);
    private final Color defaultEvenRowColor = Color.WHITE;
    private final Color defaultOddRowColor = new Color(236, 243, 254);
    private Color evenRowColor = Color.WHITE;
    private Color oddRowColor = new Color(236, 243, 254);
    private Color selectedRowColor = new Color(61, 128, 223);
    private Color gridColor = new Color(205, 205, 205);

    private JPanel currentPanel = new JPanel();
    private JLabel nameLabel = new JLabel();
    private JLabel searchforLabel = new JLabel();
    private JLabel progressLabel = new JLabel();
    private JLabel iconLabel = new JLabel();
    private JLabel HDIconLabel = new JLabel();
    private JProgressBar progressBar = new JProgressBar(0, 100);

    public TableCellRendererImpl() {
	super();
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        currentPanel.setLayout(null);

        // we can assume value is a tedserie
        TVShowEntry serie = (TVShowEntry) value;
        State state = serie.getState();

        currentPanel.setBackground(colorForRow(row, isSelected));

        // name
        nameLabel.setText(serie.getTitle() + "");
        nameLabel.setFont(LARGE_FONT);
        nameLabel.setForeground(this.getFontColor(Color.BLACK, isSelected));
        nameLabel.setBounds(28, 3, nameLabel.getPreferredSize().width,
                nameLabel.getPreferredSize().height);
        currentPanel.add(nameLabel);
        // search for
        searchforLabel.setText(serie.getSearch() + "");
        searchforLabel.setFont(SMALL_FONT);
        searchforLabel.setBounds(28, 23, 
                searchforLabel.getPreferredSize().width,
                searchforLabel.getPreferredSize().height);
        searchforLabel.setForeground(this.getFontColor(Color.DARK_GRAY, isSelected));
        currentPanel.add(searchforLabel);
        // progress
        progressLabel.setText(state.getMessage() + "");
        progressLabel.setForeground(this.getFontColor(Color.GRAY, isSelected));
        progressLabel.setFont(SMALL_FONT);
        progressLabel.setBounds(63, 38, progressLabel.getPreferredSize().width,
                progressLabel.getPreferredSize().height);
        currentPanel.add(progressLabel);
        // icon
        iconLabel.setIcon(getIcon(state));
        iconLabel.setBounds(6, 4, iconLabel.getPreferredSize().width,
                iconLabel.getPreferredSize().height);
        currentPanel.add(iconLabel);
        // HD icon
        if (serie.isHD()) {
            HDIconLabel.setIcon(IconDB.getHDIcon());
            HDIconLabel.setBounds(6, 21, HDIconLabel.getPreferredSize().width,
                    HDIconLabel.getPreferredSize().height);
            currentPanel.add(HDIconLabel);
        } else {
            HDIconLabel.setIcon(null);
        }
        // progress bar
        if (state instanceof TVShowEntry.Searching) {
          progressBar.setValue((int)(((TVShowEntry.Searching)state).getProgress()*100));
        } else progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setString("");
        progressBar.setBounds(28, 41, 30, 7);
        currentPanel.add(progressBar);

        return currentPanel;
    }

    /**
     * Returns the appropriate background color for the given row.
     */
    protected Color colorForRow(int row, boolean isSelected) {
        if (isSelected) {
            return selectedRowColor;
        }
        if ((row % 2) == 0) {
            return evenRowColor;
        } else {
            return oddRowColor;
        }
    }

    /**
     * @param color Current font color
     * @param isSelected If font is selected
     * @return Font color for text
     */
    private Color getFontColor(Color color, boolean isSelected) {
        if (isSelected) {
            return Color.WHITE;
        } else {
            return color;
        }
    }

    private Icon getIcon(State s) {
        String file = null;
        if (s instanceof TVShowEntry.Disabled) {
            file = "stop.png";
        } else if (s instanceof TVShowEntry.Enabled) {
            file = "tv.png";
        } else if (s instanceof TVShowEntry.Play) {
            file = "play.png";
        } else if (s instanceof TVShowEntry.Downloading) {
            file = "play.png";
        } else if (s instanceof TVShowEntry.GettingStatus) {
            file = "play.png";
        } else if (s instanceof TVShowEntry.Searching) {
            file = "play.png";
        } else if (s instanceof TVShowEntry.Iddle) {
            file = "tv.png";
        }
        if (file != null)
            return new ImageIcon(
                    TableCellRendererImpl.class.getResource("resources/" + file));
        return null;
    }

}
