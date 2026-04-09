package ui.pages.CounterStaffPanels.forms;

import exceptions.FileCorruptedException;
import exceptions.GetEntityListException;
import models.PaymentRecord;
import models.User;
import ui.utils.UIUtils;
import javax.swing.*;
import java.awt.*;

public class MakePaymentForm extends JDialog {
    public JLabel appointmentIdLbl;
    public JLabel customerNameLbl;
    public JLabel carPlateLbl;
    public JLabel serviceTypeLbl;
    public JLabel amountLbl;
    public JComboBox<String> paymentMethodCombo;
    public JButton makePaymentBtn;
    private final PaymentRecord paymentRecord;
    private final User loginStaff;

    public MakePaymentForm(Frame owner, PaymentRecord record, User loginStaff) {
        super(owner, "Collect Payment", true);
        this.paymentRecord = record;
        this.loginStaff = loginStaff;

        setSize(450, 650);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Payment Collection");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // --- Details Section ---
        addDetailRow(mainPanel, "Appointment ID:", appointmentIdLbl = new JLabel(""));
        addDetailRow(mainPanel, "Customer Name:", customerNameLbl = new JLabel(""));
        addDetailRow(mainPanel, "Car Plate:", carPlateLbl = new JLabel(""));
        addDetailRow(mainPanel, "Service Type:", serviceTypeLbl = new JLabel(""));
        
        mainPanel.add(new JSeparator());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- Amount Section ---
        JLabel amountTitle = UIUtils.createLabel("Total Amount Due");
        amountTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        mainPanel.add(amountTitle);
        
        amountLbl = new JLabel("");
        amountLbl.setFont(new Font("Segoe UI", Font.BOLD, 28));
        amountLbl.setForeground(new Color(37, 99, 235));
        amountLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(amountLbl);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // --- Payment Method ---
        mainPanel.add(UIUtils.createLabel("Payment Method *"));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        String[] methods = {"Cash", "Credit Card", "Debit Card", "Online Transfer", "E-Wallet"};
        paymentMethodCombo = UIUtils.createJComboBox(methods);
        mainPanel.add(paymentMethodCombo);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // --- Make Payment Button ---
        makePaymentBtn = UIUtils.createPrimaryButton("MAKE PAYMENT");
        makePaymentBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        mainPanel.add(makePaymentBtn);

        add(mainPanel);
    }

    private void addDetailRow(JPanel container, String labelText, JLabel valueLabel) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(107, 114, 128));
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        valueLabel.setForeground(new Color(31, 41, 55));

        row.add(label, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.EAST);
        
        container.add(row);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
    }


    public User getLoginStaff() {
        return loginStaff;
    }
}
