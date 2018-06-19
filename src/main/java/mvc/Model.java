package mvc;

import packing.Dimension;

import javax.swing.*;
import javax.swing.event.SwingPropertyChangeSupport;
import javax.swing.table.DefaultTableModel;

import java.beans.PropertyChangeListener;
import java.util.List;

public class Model {

    protected SwingPropertyChangeSupport propChangeFirer;

    private static DefaultComboBoxModel<Dimension> AVAILABLE_PACKING_CONTAINERS = new DefaultComboBoxModel<>();

    private static DefaultComboBoxModel<Integer> TIME_OUTS = new DefaultComboBoxModel<>();

    private DefaultTableModel boxes = new DefaultTableModel() {
        Class[] types = new Class[] { String.class, Integer.class, Integer.class, Integer.class };
        String[] columnNames = new String[] { "Naam", "Hoogte", "Breedte", "Diepte" };

        @Override
        public Class getColumnClass(int columnIndex) {
            return types[columnIndex];
        }

        @Override
        public String getColumnName(int index) {
            return columnNames[index];
        }

        @Override
        public int getColumnCount() {
            return types.length;
        }
    };

    static {
        AVAILABLE_PACKING_CONTAINERS.addElement(new Dimension("Bestelbus", 2000, 4500, 1900));
        AVAILABLE_PACKING_CONTAINERS.addElement(new Dimension("Vrachtwagen 1tue", 2450, 6000, 2590));
        AVAILABLE_PACKING_CONTAINERS.addElement(new Dimension("Vrachtwagen 2tue", 2450, 12000, 2590));

        TIME_OUTS.addElement(30);
        TIME_OUTS.addElement(60);
        TIME_OUTS.addElement(120);
        TIME_OUTS.addElement(180);
        TIME_OUTS.addElement(360);
        TIME_OUTS.addElement(600);
    }

    public Model() {
        propChangeFirer = new SwingPropertyChangeSupport(this);
    }

    public void addListener(PropertyChangeListener prop) {
        propChangeFirer.addPropertyChangeListener(prop);
    }

    public static ComboBoxModel getAvailablePackingContainers() {
        return AVAILABLE_PACKING_CONTAINERS;
    }

    public static ComboBoxModel getTimeOuts() {
        return TIME_OUTS;
    }

    public DefaultTableModel getBoxes() {
        return boxes;
    }
}