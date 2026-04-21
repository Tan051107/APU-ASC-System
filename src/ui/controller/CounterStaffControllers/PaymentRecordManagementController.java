package ui.controller.CounterStaffControllers;

import enums.AppointmentStatus;
import exceptions.FileCorruptedException;
import exceptions.GetEntityListException;
import models.*;
import services.*;
import ui.controller.CounterStaffControllers.FormController.MakePaymentFormController;
import ui.pages.CounterStaffPanels.ManagePaymentPanel;
import ui.pages.CounterStaffPanels.forms.MakePaymentForm;
import ui.pages.CounterStaffPanels.forms.ServiceReceipt;
import utils.CSVExporter;
import utils.DialogUtil;
import utils.exporters.CsvExporters.PaymentRecordCsvExporter;
import utils.exporters.interfaces.CsvExporter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentRecordManagementController {

    private final ManagePaymentPanel managePaymentPanel;
    private final PaymentRecordService paymentRecordService = new PaymentRecordService();
    Logger logger = Logger.getLogger(PaymentRecordManagementController.class.getName());

    public PaymentRecordManagementController(ManagePaymentPanel managePaymentPanel){
        this.managePaymentPanel = managePaymentPanel;
        initPanel();
    }

    public void resetAllPaymentRecords(){
        try {
            List<PaymentRecord> paymentRecords = paymentRecordService.getPaymentRecords();
            managePaymentPanel.setPaymentRecords(paymentRecords);
            loadPaymentRecords();
        } catch (FileCorruptedException e) {
            DialogUtil.showErrorMessage("Failed to get payment records" , "Encountered errors when getting payment records");
            logger.log(Level.SEVERE,e.getMessage());
        }
    }

    private void loadPaymentRecords(){
        List<PaymentRecord> paymentRecords = managePaymentPanel.getPaymentRecords();
        for(PaymentRecord paymentRecord : paymentRecords){
            try {
                boolean showActionButton = paymentRecord.getAppointment().getStatusService().equals(AppointmentStatus.COMPLETED);
                managePaymentPanel.addPaymentRecordRow(paymentRecord,this::onCollect,this::onViewReceipt , showActionButton);
            } catch (GetEntityListException e) {
                DialogUtil.showErrorMessage("Failed to load appointments" , "Encountered errors when getting payment records");
                logger.log(Level.SEVERE,e.getMessage());
            }
        }
    }

    private void onCollect(PaymentRecord paymentRecord){
        Window parent = SwingUtilities.getWindowAncestor(managePaymentPanel);
        MakePaymentForm makePaymentForm = new MakePaymentForm((Frame) parent, paymentRecord,managePaymentPanel.getLoginStaff());
        new MakePaymentFormController(makePaymentForm , paymentRecord);
        makePaymentForm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                resetAllPaymentRecords();
            }
        });
        makePaymentForm.setVisible(true);

    }

    private void onViewReceipt(PaymentRecord paymentRecord){
        Window parent = SwingUtilities.getWindowAncestor(managePaymentPanel);
        try {
            Customer customer = paymentRecord.getAppointment().getCustomer();
            CustomerCar customerCar = paymentRecord.getAppointment().getCar();
            Services service = paymentRecord.getAppointment().getService();
            ServiceReceipt serviceReceipt = new ServiceReceipt((Frame) parent,paymentRecord,customer,customerCar,service);
            serviceReceipt.setVisible(true);
        } catch (FileCorruptedException | GetEntityListException e) {
            logger.log(Level.SEVERE , e.getMessage());
            DialogUtil.showErrorMessage("Encountered Error" , "Failed to show receipt");
        }
    }

    private void searchPaymentRecord(){
        String keyword = managePaymentPanel.searchField.getText();
        String paymentMethodFilterSelection = Objects.requireNonNull(managePaymentPanel.paymentMethodFilterCombo.getSelectedItem()).toString();
        String paymentStatusFilterSelection = Objects.requireNonNull(managePaymentPanel.statusFilterCombo.getSelectedItem()).toString();
        if(paymentStatusFilterSelection.equalsIgnoreCase("all") && paymentMethodFilterSelection.equalsIgnoreCase("all") && keyword.isEmpty()){
            resetAllPaymentRecords();
        }
        else{
            String paymentMethodSelected = paymentMethodFilterSelection.equalsIgnoreCase("all") ? "" : paymentMethodFilterSelection;
            String paymentStatusSelected = paymentStatusFilterSelection.equalsIgnoreCase("all") ? "" :paymentStatusFilterSelection;
            try {
                List<PaymentRecord> paymentRecordsFound = paymentRecordService.searchPaymentRecord(keyword,paymentStatusSelected,paymentMethodSelected);
                System.out.println(paymentRecordsFound.toString());
                managePaymentPanel.setPaymentRecords(paymentRecordsFound);
                loadPaymentRecords();
            } catch (FileCorruptedException e) {
                logger.log(Level.SEVERE , e.getMessage());
                DialogUtil.showErrorMessage("Encountered Error" , "Failed to search payment records");
            }
        }
    }

    private void exportPaymentRecord(){
        List<PaymentRecord> paymentRecords = managePaymentPanel.getPaymentRecords();
        PaymentRecordCsvExporter paymentRecordCsvExporter = new PaymentRecordCsvExporter();
        CSVExporter<PaymentRecord> csvExporter = new CSVExporter<>();
        csvExporter.exportData(paymentRecords,"Payment Records" , paymentRecordCsvExporter);
    }

    private void initPanel(){
        resetAllPaymentRecords();
        initListener();
    }

    private void initListener(){
        managePaymentPanel.paymentMethodFilterCombo.addActionListener(e->searchPaymentRecord());
        managePaymentPanel.searchField.addActionListener(e->searchPaymentRecord());
        managePaymentPanel.statusFilterCombo.addActionListener(e->searchPaymentRecord());
        managePaymentPanel.exportBtn.addActionListener(e->exportPaymentRecord());
    }
}
