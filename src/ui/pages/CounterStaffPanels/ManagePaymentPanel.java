package ui.pages.CounterStaffPanels;

import models.PaymentRecord;
import models.User;
import ui.utils.RoundedPanel;
import ui.utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class ManagePaymentPanel extends JPanel {
    private final JPanel rowsContainer;
    public JButton exportBtn;
    public JTextField searchField;
    public JComboBox<String> statusFilterCombo;
    private List<PaymentRecord> paymentRecords;
    private final User loginStaff;

    public ManagePaymentPanel(User loginStaff) {
        this.loginStaff = loginStaff;
        setLayout(new BorderLayout());
        setBackground(new Color(249, 250, 251)); // Very light gray background
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // --- TOP HEADER ---
        JPanel headerContainer = new JPanel();
        headerContainer.setLayout(new BoxLayout(headerContainer, BoxLayout.Y_AXIS));
        headerContainer.setOpaque(false);
        headerContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Row 1: Title and Buttons
        JPanel titleAndButtonsPanel = new JPanel(new BorderLayout());
        titleAndButtonsPanel.setOpaque(false);

        JLabel title = new JLabel("Payments");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(31, 41, 55));
        titleAndButtonsPanel.add(title, BorderLayout.WEST);

        // Right Header: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setOpaque(false);

        exportBtn = UIUtils.createSecondaryButton("Export Payments");
        exportBtn.setPreferredSize(new Dimension(180, 40));
        buttonPanel.add(exportBtn);

        titleAndButtonsPanel.add(buttonPanel, BorderLayout.EAST);
        headerContainer.add(titleAndButtonsPanel);

        // Row 2: Spacing and Search Field + Filter
        headerContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        searchPanel.setOpaque(false);

        searchField = UIUtils.createTextField();
        searchField.setPreferredSize(new Dimension(300, 45));
        searchField.setMaximumSize(new Dimension(300, 45));
        searchField.setText("Search by Record ID or Appointment ID");
        searchField.setForeground(Color.GRAY);
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Search by Record ID or Appointment ID")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search by Record ID or Appointment ID");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        searchPanel.add(searchField);

        // --- Status Filter ---
        JPanel statusFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        statusFilterPanel.setOpaque(false);
        JLabel statusLabel = UIUtils.createLabel("Payment Status:");
        statusFilterPanel.add(statusLabel);
        String[] statusOptions = {"All", "Paid", "Unpaid"};
        statusFilterCombo = UIUtils.createJComboBox(statusOptions);
        statusFilterCombo.setPreferredSize(new Dimension(150, 45));
        statusFilterPanel.add(statusFilterCombo);
        searchPanel.add(statusFilterPanel);

        headerContainer.add(searchPanel);

        add(headerContainer, BorderLayout.NORTH);

        // --- TABLE CONTAINER ---
        RoundedPanel tableCard = new RoundedPanel(15);
        tableCard.setBackground(Color.WHITE);
        tableCard.setLayout(new BorderLayout());

        // Table Header
        JPanel tableHeader = createTableHeader();
        tableCard.add(tableHeader, BorderLayout.NORTH);

        // Rows Container
        rowsContainer = new JPanel();
        rowsContainer.setOpaque(false);
        rowsContainer.setLayout(new BoxLayout(rowsContainer, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(rowsContainer);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        tableCard.add(scrollPane, BorderLayout.CENTER);
        add(tableCard, BorderLayout.CENTER);
    }

    private JPanel createTableHeader() {
        JPanel header = new JPanel(new GridLayout(1, 6, 10, 0));
        header.setBackground(new Color(37, 99, 235));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        String[] cols = {"Record ID", "Appointment ID", "Amount", "Payment Method", "Payment Status", "Actions"};
        for (String col : cols) {
            JLabel label = new JLabel(col);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setForeground(Color.WHITE);
            header.add(label);
        }
        return header;
    }

    public void addPaymentRecordRow(PaymentRecord paymentRecord, Consumer<PaymentRecord> onCollect, Consumer<PaymentRecord> onViewReceipt) {
        boolean hasMadePayment = paymentRecord.isHasPaid();

        JPanel row = new JPanel(new GridLayout(1, 6, 10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(243, 244, 246)),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        // Record ID
        JLabel idLbl = new JLabel(paymentRecord.getId());
        idLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        idLbl.setForeground(new Color(107, 114, 128));
        row.add(idLbl);

        // Appointment ID
        JLabel appointmentIdLbl = new JLabel(paymentRecord.getAppointmentId());
        appointmentIdLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        appointmentIdLbl.setForeground(new Color(31, 41, 55));
        row.add(appointmentIdLbl);

        // Amount
        row.add(createLabel(String.format("RM %.2f", paymentRecord.getAmount())));

        // Payment Method
        String method = paymentRecord.getPaymentMethod();
        row.add(createLabel(method == null || method.isEmpty() ? "-" : method));

        // Payment Status Badge
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        statusPanel.setOpaque(false);
        statusPanel.add(createStatusBadge(hasMadePayment ? "Paid" : "Unpaid"));
        row.add(statusPanel);

        // Actions
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        actions.setOpaque(false);

        if (hasMadePayment) {
            JButton receiptBtn = UIUtils.createActionIconButton("Receipt", new Color(37, 99, 235));
            receiptBtn.addActionListener(e -> onViewReceipt.accept(paymentRecord));
            actions.add(receiptBtn);
        }
        else{
            JButton collectBtn = UIUtils.createActionIconButton("Collect Payment",  new Color(16, 185, 129));
            collectBtn.addActionListener(e -> onCollect.accept(paymentRecord));
            actions.add(collectBtn);
        }
        
        row.add(actions);

        rowsContainer.add(row);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(55, 65, 81));
        return label;
    }

    private JPanel createStatusBadge(String status) {
        Color bg, fg;
        if (status.equals("Paid")) {
            bg = new Color(220, 252, 231);
            fg = new Color(22, 101, 52);
        } else {
            bg = new Color(254, 242, 242);
            fg = new Color(185, 28, 28);
        }

        RoundedPanel badge = new RoundedPanel(12);
        badge.setBackground(bg);
        badge.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 4));
        JLabel label = new JLabel(status);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(fg);
        badge.add(label);
        return badge;
    }

    public List<PaymentRecord> getPaymentRecords() {
        return paymentRecords;
    }

    public void setPaymentRecords(List<PaymentRecord> paymentRecords) {
        this.paymentRecords = paymentRecords;
        clearPayments();
    }

    public void clearPayments() {
        rowsContainer.removeAll();
        rowsContainer.revalidate();
        rowsContainer.repaint();
    }

    public User getLoginStaff() {
        return loginStaff;
    }
}
