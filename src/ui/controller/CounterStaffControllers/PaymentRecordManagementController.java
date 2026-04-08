package ui.controller.CounterStaffControllers;

import exceptions.FileCorruptedException;
import exceptions.GetEntityListException;
import models.*;
import services.*;
import ui.controller.CounterStaffControllers.FormController.MakePaymentFormController;
import ui.pages.CounterStaffPanels.ManagePaymentPanel;
import ui.pages.CounterStaffPanels.forms.MakePaymentForm;
import ui.pages.CounterStaffPanels.forms.ServiceReceipt;
import utils.DialogUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
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
            loadAppointments();
        } catch (FileCorruptedException e) {
            DialogUtil.showErrorMessage("Failed to get payment records" , "Encountered errors when getting payment records");
            logger.log(Level.SEVERE,e.getMessage());
        }
    }

    private void loadAppointments(){
        List<PaymentRecord> paymentRecords = managePaymentPanel.getPaymentRecords();
        for(PaymentRecord paymentRecord : paymentRecords){
            managePaymentPanel.addPaymentRecordRow(paymentRecord,this::onCollect,this::onViewReceipt);
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
            ServiceReceipt serviceReceipt = new ServiceReceipt((Frame) parent,paymentRecord,customer,customerCar,service ,managePaymentPanel.getLoginStaff());
            serviceReceipt.setVisible(true);
        } catch (FileCorruptedException | GetEntityListException e) {
            logger.log(Level.SEVERE , e.getMessage());
            DialogUtil.showErrorMessage("Encountered Error" , "Failed to show receipt");
        }
    }

    private void initPanel(){
        resetAllPaymentRecords();
    }
}
