package ui.pages.CounterStaffPanels.forms;

import models.*;
import ui.utils.UIUtils;
import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class ServiceReceipt extends JDialog {

    public ServiceReceipt(Frame owner, PaymentRecord record, Customer customer, CustomerCar car, Services services , User loginStaff) {
        super(owner, "Official Receipt", true);
        
        setSize(500, 800);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        mainPanel.setBackground(Color.WHITE);

        // --- Header / Logo ---
        JLabel receiptTitle = new JLabel("OFFICIAL RECEIPT");
        receiptTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        receiptTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(receiptTitle);
        
        JLabel companyName = new JLabel("APU Automotive Service Centre");
        companyName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        companyName.setForeground(Color.GRAY);
        companyName.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(companyName);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(new JSeparator());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Basic Info Row ---
        JPanel basicInfo = new JPanel(new GridLayout(1, 2));
        basicInfo.setOpaque(false);
        basicInfo.add(createDetailItem("Receipt ID:", record.getId()));
        basicInfo.add(createDetailItem("Appointment ID:", record.getAppointmentId()));
        mainPanel.add(basicInfo);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Customer Section ---
        mainPanel.add(createSectionHeader("CUSTOMER DETAILS"));
        mainPanel.add(createDetailRow("Name:", customer.getName()));
        mainPanel.add(createDetailRow("Email:", customer.getEmail()));
        mainPanel.add(createDetailRow("Contact:", customer.getContactNumber()));
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Vehicle Section ---
        mainPanel.add(createSectionHeader("VEHICLE DETAILS"));
        mainPanel.add(createDetailRow("Plate Number:", car.getCarPlate()));
        mainPanel.add(createDetailRow("Brand / Model:", car.getCarBrand() + " " + car.getCarModel()));
        mainPanel.add(createDetailRow("Current Mileage:", String.format("%.0f km", car.getMileage())));
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Service Section ---
        mainPanel.add(createSectionHeader("SERVICE RENDERED"));
        mainPanel.add(createDetailRow("Service Type:", services.getServiceName()));
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(new JSeparator());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Payment Section ---
        mainPanel.add(createSectionHeader("PAYMENT SUMMARY"));
        mainPanel.add(createDetailRow("Payment Method:", record.getPaymentMethod()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        mainPanel.add(createDetailRow("Payment Date:", record.getUpdatedAt().format(formatter)));
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setOpaque(false);
        JLabel totalLabel = new JLabel("TOTAL PAID:");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel totalAmount = new JLabel(String.format("RM %.2f", record.getAmount()));
        totalAmount.setFont(new Font("Segoe UI", Font.BOLD, 22));
        totalAmount.setForeground(new Color(22, 101, 52)); // Dark green
        
        totalPanel.add(totalLabel, BorderLayout.WEST);
        totalPanel.add(totalAmount, BorderLayout.EAST);
        mainPanel.add(totalPanel);

        mainPanel.add(Box.createVerticalGlue());
        
        // --- Footer ---
        JLabel footerMsg = new JLabel("Thank you for choosing APU-ASC!");
        footerMsg.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        footerMsg.setForeground(Color.GRAY);
        footerMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(footerMsg);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        JButton closeBtn = UIUtils.createPrimaryButton("CLOSE");
        closeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeBtn.addActionListener(e -> dispose());
        mainPanel.add(closeBtn);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        add(scrollPane);
    }

    private JPanel createSectionHeader(String title) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        JLabel l = new JLabel(title);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(new Color(107, 114, 128)); // Gray-500
        p.add(l);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        return p;
    }

    private JPanel createDetailRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(75, 85, 99)); // Gray-600
        
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 13));
        val.setForeground(new Color(31, 41, 55)); // Gray-900
        
        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.EAST);
        return row;
    }

    private JPanel createDetailItem(String label, String value) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(Color.GRAY);
        
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        p.add(lbl);
        p.add(val);
        return p;
    }
}
