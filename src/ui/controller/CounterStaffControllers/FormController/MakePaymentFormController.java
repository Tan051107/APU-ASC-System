package ui.controller.CounterStaffControllers.FormController;

import exceptions.BusinessRuleException;
import exceptions.FileCorruptedException;
import exceptions.GetEntityListException;
import exceptions.NotFoundException;
import models.PaymentRecord;
import services.PaymentRecordService;
import ui.pages.CounterStaffPanels.forms.MakePaymentForm;
import utils.DialogUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MakePaymentFormController {

    private final MakePaymentForm makePaymentForm;
    private final PaymentRecord paymentRecordToMakePayment;
    private final PaymentRecordService paymentRecordService = new PaymentRecordService();
    Logger logger = Logger.getLogger(MakePaymentFormController.class.getName());

    public MakePaymentFormController(MakePaymentForm makePaymentForm, PaymentRecord paymentRecordToAMakePayment) {
        this.makePaymentForm = makePaymentForm;
        this.paymentRecordToMakePayment = paymentRecordToAMakePayment;
        initListener();
        initForm();
    }

    private void initListener(){
        makePaymentForm.makePaymentBtn.addActionListener(e->makePayment());
    }

    private void initForm(){
        try {
            String customerName = paymentRecordToMakePayment.getAppointment().getCustomer().getName();
            String carPlate = paymentRecordToMakePayment.getAppointment().getCar().getCarPlate();
            String serviceType = paymentRecordToMakePayment.getAppointment().getService().getName();
            makePaymentForm.customerNameLbl.setText(customerName);
            makePaymentForm.carPlateLbl.setText(carPlate);
            makePaymentForm.serviceTypeLbl.setText(serviceType);
            makePaymentForm.appointmentIdLbl.setText(paymentRecordToMakePayment.getAppointmentId());
            makePaymentForm.amountLbl.setText(String.format("RM %.2f", paymentRecordToMakePayment.getAmount()));
        } catch (FileCorruptedException | GetEntityListException e) {
            logger.log(Level.SEVERE,e.getMessage());
            DialogUtil.showErrorMessage("Encountered Error" , e.getMessage());
        }
    }


    private void makePayment(){
        String selectedPaymentMethod = Objects.requireNonNull(makePaymentForm.paymentMethodCombo.getSelectedItem()).toString();
        paymentRecordToMakePayment.setPaymentDateTime(LocalDateTime.now());
        paymentRecordToMakePayment.setPaymentMethod(selectedPaymentMethod);
        paymentRecordToMakePayment.setPaymentCollectedBy(makePaymentForm.getLoginStaff().getId());
        try {
            paymentRecordService.makePayment(paymentRecordToMakePayment);
            DialogUtil.showInfoMessage("Payment Successful" , String.format("Payment for  appointment %s has been successfully made", paymentRecordToMakePayment.getAppointmentId()));
            makePaymentForm.dispose();
        } catch (GetEntityListException | FileCorruptedException | IOException e) {
            logger.log(Level.SEVERE,e.getMessage());
        } catch (BusinessRuleException | NotFoundException e) {
            DialogUtil.showWarningMessage("Failed to make payment" ,e.getMessage());
        }
    }
}
