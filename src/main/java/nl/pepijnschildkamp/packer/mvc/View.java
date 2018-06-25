package nl.pepijnschildkamp.packer.mvc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

public class View {

    private JFrame frame;
    private JComboBox waveComboBox;
    private JComboBox timeOutComboBox;
    private JButton addRowButton;
    private JButton removeRowButton;
    private JButton packButton;
    private JTable boxesTable;

    public View() {
        frame = createFrame();
        Container container = frame.getContentPane();

        JPanel panel = new JPanel(new GridLayout(4, 1, 0, 10));

        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        /* Add header */
        panel.add(getHeaderPanel());

        /* add container selection at left column */
        panel.add(getPackerPanel());

        /* right column */
        panel.add(getBoxesPanel());

        panel.add(getOptionsPanel());
        container.add(panel);
    }

    private JFrame createFrame() {
        JFrame frame = new JFrame("Mainfreight - Packing optimalisator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);

        return frame;
    }

    private JPanel getPackerPanel() {
        JPanel panel  = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));

        JPanel containerPanel = new JPanel(new BorderLayout());
        waveComboBox = new JComboBox();
        JLabel containerLabel = new JLabel("Kies container");
        containerLabel.setLabelFor(waveComboBox);
        containerPanel.add(containerLabel, BorderLayout.NORTH);
        containerPanel.add(waveComboBox);

        JPanel timeoutPanel = new JPanel(new BorderLayout());
        timeOutComboBox = new JComboBox();
        JLabel timeoutLabel = new JLabel("Kies maximale calculatie tijd in seconden");
        containerLabel.setLabelFor(timeOutComboBox);
        timeoutPanel.add(timeoutLabel, BorderLayout.NORTH);
        timeoutPanel.add(timeOutComboBox);

        panel.add(timeoutPanel);
        panel.add(containerPanel);

        return panel;
    }

    private JPanel getHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.5;
        constraints.weighty = 1.5;
        constraints.gridy = 0;
        constraints.ipadx = 0;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.insets = new Insets(0,0,0,0);

        ClassLoader classLoader = View.class.getClassLoader();
        URL mainFreightLogoResource = classLoader.getResource("mainfreight-logo.png");

        if(mainFreightLogoResource != null) {
            ImageIcon mainFreightLogo = new ImageIcon(mainFreightLogoResource);
            JLabel label = new JLabel(mainFreightLogo);
            panel.add(label, constraints);
        }

        return panel;
    }

    private JPanel getBoxesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        boxesTable = new JTable(null, null, null);
        boxesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        boxesTable.setPreferredSize(new Dimension(800, 400));
        boxesTable.setPreferredScrollableViewportSize(boxesTable.getPreferredSize());
        boxesTable.setFillsViewportHeight(false);

        JScrollPane listScroller = new JScrollPane(boxesTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        listScroller.setPreferredSize(new Dimension(800, 400));
        panel.add(listScroller);

        return panel;
    }

    public JPanel getOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.5;
        constraints.weighty = 1.5;
        constraints.gridy = 0;
        constraints.ipadx = 0;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.PAGE_END;
        constraints.insets = new Insets(10,0,0,0);

        packButton = new JButton("Calculeer ideale posities dozen in container");
        packButton.setVerticalTextPosition(SwingConstants.CENTER);
        packButton.setActionCommand("pack");
        packButton.setToolTipText("Calculeer ideale posities dozen in container");

        addRowButton = new JButton("Voeg nieuwe doos toe");
        addRowButton.setVerticalTextPosition(SwingConstants.CENTER);
        addRowButton.setActionCommand("addBox");
        addRowButton.setToolTipText("Voeg nieuwe doos toe");

        removeRowButton = new JButton("Verwijder geselecteerde doos");
        removeRowButton.setVerticalTextPosition(SwingConstants.CENTER);
        removeRowButton.setActionCommand("removeBox");
        removeRowButton.setToolTipText("Verwijder geselecteerde doos");

        panel.add(removeRowButton, constraints);
        panel.add(addRowButton, constraints);
        panel.add(packButton, constraints);

        return panel;
    }

    public JFrame getFrame() {
        return this.frame;
    }

    public JComboBox getWaveComboBox() {
        return this.waveComboBox;
    }

    public JComboBox getTimeOutComboBox() {
        return this.timeOutComboBox;
    }

    public JButton getAddRowButton() {
        return this.addRowButton;
    }

    public JButton getRemoveRowButton() {
        return this.removeRowButton;
    }

    public JButton getPackButton() {
        return this.packButton;
    }

    public JTable getBoxesTable() {
        return this.boxesTable;
    }
}
