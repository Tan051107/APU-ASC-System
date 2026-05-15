package ui.pages.Manager;

import ui.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class RevenueReports extends JDialog{
    public JLabel monthTotal = new JLabel();
    public JLabel yearTotal = new JLabel();
    public JLabel selectedMonthTotal = new JLabel();
    public JComboBox<String> monthComboBox = new JComboBox<>();
    public JButton closeButton;

    public RevenueReports(JFrame parent){
        super(parent, "Revenue Reports", true);

        setTitle("Revenue Reports Details");
        setSize(450, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = UIUtils.createMenuTitle("Revenue Reports");
        panel.add(title, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(3, 1, 20, 20));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        gridPanel.add(createReportCard("Selected Month Total Revenue", selectedMonthTotal, true));
        gridPanel.add(createReportCard("Current Month Total Revenue", monthTotal, false));
        gridPanel.add(createReportCard("Current Year Total Revenue", yearTotal, false));

        panel.add(gridPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); 

        closeButton = UIUtils.createCRUDButton("Close");

        bottomPanel.add(Box.createHorizontalGlue()); 
        bottomPanel.add(closeButton);
        bottomPanel.add(Box.createHorizontalGlue());
        
        panel.add(bottomPanel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane);
    }


    private JPanel createReportCard(String titleText, JLabel valueLabel, Boolean dropDown) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1), 
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = UIUtils.createLabel(titleText);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(Color.DARK_GRAY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(Color.blue);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        card.add(Box.createVerticalGlue());
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        if (dropDown == true){
            String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            monthComboBox = UIUtils.createJComboBox(months);
            monthComboBox.setMaximumSize(new Dimension(150, 30));
            monthComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(monthComboBox);
            card.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        card.add(valueLabel);
        card.add(Box.createVerticalGlue());

        return card;
    }
}
