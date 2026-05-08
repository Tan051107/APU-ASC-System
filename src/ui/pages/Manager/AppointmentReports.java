package ui.pages.Manager;

import ui.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class AppointmentReports extends JDialog{
    public JLabel monthTotal = new JLabel();
    public JLabel yearTotal = new JLabel();
    public JLabel completeTotal = new JLabel();
    public JLabel assignedTotal = new JLabel();
    public JLabel cancelledTotal = new JLabel();
    public JButton closeButton;

    public AppointmentReports(JFrame parent){
        super(parent, "Appointment Reports", true);

        setTitle("Appointment Reports Details");
        setSize(450, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = UIUtils.createMenuTitle("Appointment Reports");
        panel.add(title, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(5, 1, 20, 20));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        gridPanel.add(createReportCard("Current Month Total Appointments", monthTotal));
        gridPanel.add(createReportCard("Current Year Total Appointments", yearTotal));
        gridPanel.add(createReportCard("Total Completed Appointments", completeTotal));
        gridPanel.add(createReportCard("Total Assigned Appointments", assignedTotal));
        gridPanel.add(createReportCard("Total Cancelled Appointments", cancelledTotal));

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


    private JPanel createReportCard(String titleText, JLabel valueLabel) {
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
        card.add(valueLabel);
        card.add(Box.createVerticalGlue());

        return card;
    }
}
