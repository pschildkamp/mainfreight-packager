package mvc;

import java.awt.*;

import javax.swing.*;

import static java.awt.Component.LEFT_ALIGNMENT;
import static javax.swing.BoxLayout.PAGE_AXIS;

public class View {

    private JFrame frame;
    private JComboBox containerComboBox;
    private JComboBox timeOutComboBox;
    private JButton addRowButton;
    private JButton packButton;
    private JTable boxesTable;

    public View() {
        frame = new JFrame("Mainfreight - Packing optimalisator");
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        /* Add header */
        mainPanel.add(getHeaderPanel());

        /* 2 columns */
        JPanel grid = new JPanel();
        GridLayout gridLayout = new GridLayout(1, 2, 0, 0);
        grid.setLayout(gridLayout);


        /* add container selection at left column */
        grid.add(getPackerPanel());

        /* right column */
        grid.add(getBoxesPanel());

        mainPanel.add(grid);

        frame.add(mainPanel);
    }

    private JPanel getPackerPanel() {
        JPanel packingOptionsPanel = new JPanel();
        packingOptionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        packingOptionsPanel.setPreferredSize(new Dimension(370, 500));

        containerComboBox = new JComboBox();
        JLabel containerLabel = new JLabel("Kies container");
        containerLabel.setLabelFor(containerComboBox);
        containerComboBox.setPreferredSize(new Dimension(370, 25));

        packingOptionsPanel.add(containerLabel);
        packingOptionsPanel.add(containerComboBox);

        timeOutComboBox = new JComboBox();
        JLabel timeOutLabel = new JLabel("Kies maximale calculatie tijd in seconden");
        timeOutLabel.setLabelFor(timeOutComboBox);
        timeOutComboBox.setPreferredSize(new Dimension(370, 25));

        packingOptionsPanel.add(timeOutLabel);
        packingOptionsPanel.add(timeOutComboBox);

        packButton = new JButton("Calculeer ideale posities dozen in container");
        packButton.setVerticalTextPosition(SwingConstants.CENTER);
        packButton.setActionCommand("pack");
        packButton.setToolTipText("Calculeer ideale posities dozen in container");
        packButton.setPreferredSize(new Dimension(370, 25));

        packingOptionsPanel.add(packButton);

        return packingOptionsPanel;
    }

    private JPanel getHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setSize(new Dimension(780, 9));

        ImageIcon mainFreightLogo = new ImageIcon("modules/ontwerpen_en_programmeren/exam/resources/mainfreight-logo.png");
        JLabel label = new JLabel(mainFreightLogo);
        label.setPreferredSize(new Dimension(780, 30));
        panel.add(label);
        panel.setBorder(BorderFactory.createMatteBorder(0,0,1,0, new Color(0,0,0)));

        return panel;
    }

    private JPanel getBoxesPanel() {
        boxesTable = new JTable(null, null, null);

        addRowButton = new JButton("Voeg nieuwe doos toe");
        addRowButton.setVerticalTextPosition(SwingConstants.CENTER);
        addRowButton.setActionCommand("addBox");
        addRowButton.setToolTipText("Voeg nieuwe doos toe");

        JLabel boxesLabel = new JLabel("Te berekenen pakketten");
        boxesLabel.setLabelFor(boxesTable);

        JScrollPane listScroller = new JScrollPane(boxesTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        listScroller.setPreferredSize(new Dimension(380, 500));
        boxesTable.setPreferredScrollableViewportSize(listScroller.getPreferredSize());
        listScroller.setAlignmentX(LEFT_ALIGNMENT);

        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, PAGE_AXIS));
        listPane.add(boxesLabel);
        listPane.add(Box.createRigidArea(new Dimension(0,5)));
        listPane.add(listScroller);
        listPane.add(Box.createRigidArea(new Dimension(0, 5)));

        listPane.add(addRowButton);
        listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        return listPane;
    }

    public JComboBox getContainerComboBox() {
        return containerComboBox;
    }

    public JComboBox getTimeOutComboBox() {
        return timeOutComboBox;
    }

    public JTable getBoxesTable() {
        return boxesTable;
    }

    public JButton getAddBoxBtn() {
        return addRowButton;
    }

    public JButton getPackBtn() {
        return packButton;
    }

    public JFrame getFrame() {
        return frame;
    }
}
