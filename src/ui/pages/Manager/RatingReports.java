package ui.pages.Manager;

import ui.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

public class RatingReports extends JDialog{
    public JLabel bestRatingName = new JLabel();
    public JLabel bestRating = new JLabel();
    public JLabel worstRatingName = new JLabel();
    public JLabel worstRating = new JLabel();
    public JComboBox<String> roleComboBox = new JComboBox<>();
    public JButton closeButton;

    public RatingReports(JFrame parent){
        super(parent, "Rating Reports", true);

        setTitle("Rating Reports Details");
        setSize(450, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel title = UIUtils.createMenuTitle("Rating Reports");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(title);

        String[] roles = {"Counter Staff", "Technician", "Customer"};
        roleComboBox = UIUtils.createJComboBox(roles);
        roleComboBox.setMaximumSize(new Dimension(150, 40));
        roleComboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(roleComboBox);

        panel.add(topPanel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        gridPanel.add(createReportCard("Selected Role Best Rating", bestRatingName, bestRating, true));
        gridPanel.add(createReportCard("Selected Role Worst Rating", worstRatingName, worstRating, false));

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


    private JPanel createReportCard(String titleText, JLabel valueLabel1, JLabel valueLabel2, Boolean goodRating) {
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

        valueLabel1.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel1.setForeground(Color.blue);
        valueLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);

        valueLabel2.setFont(new Font("Segoe UI", Font.BOLD, 36));
        if (goodRating == true){
            valueLabel2.setForeground(Color.green);
        } else {
            valueLabel2.setForeground(Color.red);
        }
        
        valueLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(valueLabel1);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(valueLabel2);
        card.add(Box.createVerticalGlue());


        return card;
    }
}
