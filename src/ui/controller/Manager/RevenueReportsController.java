package ui.controller.Manager;

import java.time.LocalDate;
import java.util.List;

import models.PaymentRecord;
import services.PaymentRecordService;
import ui.pages.Manager.RevenueReports;
import utils.DialogUtil;

public class RevenueReportsController {
    private final RevenueReports panel;

    private final PaymentRecordService paymentRecordService = new PaymentRecordService();

    public RevenueReportsController(RevenueReports panel){
        this.panel = panel;
        initListeners();
        int currentMonthIndex = LocalDate.now().getMonthValue() - 1;
        panel.monthComboBox.setSelectedIndex(currentMonthIndex);
        getSelectedMonthTotal();
        getTotalMonthlyRevenue();
        getTotalYearlyRevenue();
    }

    private void initListeners() {
        panel.monthComboBox.addActionListener(e -> getSelectedMonthTotal());
        panel.closeButton.addActionListener(e -> panel.dispose());
    }

    public void getTotalMonthlyRevenue(){
        try {
            List<PaymentRecord> allPaymentRecords = paymentRecordService.getPaymentRecords();

            double total = 0;
            LocalDate today = LocalDate.now();
            int currentMonth = today.getMonthValue();
            int currentYear = today.getYear();

            for (PaymentRecord paymentRecord : allPaymentRecords){
                if (paymentRecord.isHasPaid() && 
                    paymentRecord.getPaymentDateTime() != null && 
                    paymentRecord.getPaymentDateTime().getMonthValue() == currentMonth && 
                    paymentRecord.getPaymentDateTime().getYear() == currentYear) {
                    total = total + paymentRecord.getAmount();
                }
            }
            
            String totalString = "RM" + String.format("%.2f", total); 
            panel.monthTotal.setText(totalString);
        } catch (Exception e) {
            DialogUtil.showErrorMessage("Error", "Error Getting Total Month Revenue: " + e.getMessage());
            panel.yearTotal.setText("0");
        }
    }

    public void getTotalYearlyRevenue(){
        try {
            List<PaymentRecord> allPaymentRecords = paymentRecordService.getPaymentRecords();

            double total = 0;
            LocalDate today = LocalDate.now();
            int currentYear = today.getYear();

            for (PaymentRecord paymentRecord : allPaymentRecords){
                if (paymentRecord.isHasPaid() && 
                    paymentRecord.getPaymentDateTime() != null &&  
                    paymentRecord.getPaymentDateTime().getYear() == currentYear) {
                    total = total + paymentRecord.getAmount();
                }
            }
            
            String totalString = "RM" + String.format("%.2f", total); 
            panel.yearTotal.setText(totalString);
        } catch (Exception e) {
            DialogUtil.showErrorMessage("Error", "Error Getting Total Year Revenue: " + e.getMessage());
            panel.yearTotal.setText("0");
        }
    }

    public void getSelectedMonthTotal(){
        int selectedMonth = panel.monthComboBox.getSelectedIndex() + 1;
        try {
            List<PaymentRecord> allPaymentRecords = paymentRecordService.getPaymentRecords();

            double total = 0;
            LocalDate today = LocalDate.now();
            int currentYear = today.getYear();

            for (PaymentRecord paymentRecord : allPaymentRecords){
                if (paymentRecord.isHasPaid() && 
                    paymentRecord.getPaymentDateTime() != null && 
                    paymentRecord.getPaymentDateTime().getMonthValue() == selectedMonth && 
                    paymentRecord.getPaymentDateTime().getYear() == currentYear) {
                    total = total + paymentRecord.getAmount();
                }
            }
            
            String totalString = "RM" + String.format("%.2f", total); 
            panel.selectedMonthTotal.setText(totalString);
        } catch (Exception e) {
            DialogUtil.showErrorMessage("Error", "Error Getting Total Revenue: " + e.getMessage());
            panel.selectedMonthTotal.setText("0");
        }
    }
}
