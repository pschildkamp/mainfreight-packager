package nl.pepijnschildkamp.packer.mvc;

import nl.pepijnschildkamp.packer.packing.Dimension;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class Model {

    private static final DefaultComboBoxModel<Dimension> AVAILABLE_PACKING_WAVES = new DefaultComboBoxModel<>();

    private static final DefaultComboBoxModel<Integer> TIME_OUTS = new DefaultComboBoxModel<>();

    private final DefaultTableModel boxes = new DefaultTableModel() {
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
        AVAILABLE_PACKING_WAVES.addElement(new Dimension("Bestelbus", 2000, 4500, 1900));
        AVAILABLE_PACKING_WAVES.addElement(new Dimension("Vrachtwagen 1tue", 2450, 6000, 2590));
        AVAILABLE_PACKING_WAVES.addElement(new Dimension("Vrachtwagen 2tue", 2450, 12000, 2590));

        TIME_OUTS.addElement(30);
        TIME_OUTS.addElement(60);
        TIME_OUTS.addElement(120);
        TIME_OUTS.addElement(180);
        TIME_OUTS.addElement(360);
        TIME_OUTS.addElement(600);
    }

    public static ComboBoxModel getAvailablePackingWaves() {
        return AVAILABLE_PACKING_WAVES;
    }

    public static ComboBoxModel getTimeOuts() {
        return TIME_OUTS;
    }

    public DefaultTableModel getBoxes() {
        return this.boxes;
    }
}