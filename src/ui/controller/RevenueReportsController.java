package ui.controller;

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
        getTotalMonthlyRevenue();
        getTotalYearlyRevenue();
    }

    private void initListeners() {
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
            DialogUtil.showErrorMessage("Error", "Error Getting Total Revenue: " + e.getMessage());
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
            DialogUtil.showErrorMessage("Error", "Error Getting Total Revenue: " + e.getMessage());
            panel.yearTotal.setText("0");
        }
    }
}
