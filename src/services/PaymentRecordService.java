package services;

import enums.AppointmentStatus;
import exceptions.*;
import mapper.PaymentRecordMapper;
import models.Appointment;
import models.PaymentRecord;
import repositories.CrudRepository;
import utils.RandomIdGenerator;

import java.io.IOException;
import java.util.List;

public class PaymentRecordService {
    private final String PAYMENT_RECORD_FILE = "txt_files/PaymentRecord.txt";
    private final PaymentRecordMapper paymentRecordMapper = new PaymentRecordMapper();
    private final CrudRepository<PaymentRecord> paymentRecordCrudRepository = new CrudRepository<>(PAYMENT_RECORD_FILE,paymentRecordMapper);
    private final AppointmentService appointmentService = new AppointmentService();

    public List<PaymentRecord> getPaymentRecords() throws FileCorruptedException {
        return paymentRecordCrudRepository.getAll();
    }

    public PaymentRecord getPaymentRecordById(String paymentId) throws FileCorruptedException {
        return paymentRecordCrudRepository.getOne(paymentId);
    }

    public PaymentRecord getPaymentRecordByAppointment(String appointmentId) throws FileCorruptedException {
        return paymentRecordCrudRepository.getAll(paymentRecord -> paymentRecord.getAppointmentId().equalsIgnoreCase(appointmentId)).getFirst();
    }

    public void addPaymentRecord(PaymentRecord paymentRecord) throws IOException {
        String paymentRecordId = generatePaymentId();
        paymentRecord.setId(paymentRecordId);
        paymentRecord.setPaymentDateTime(null);
        paymentRecord.setPaymentMethod(null);
        paymentRecord.setHasPaid(false);
        paymentRecord.setPaymentCollectedBy(null);
        paymentRecordCrudRepository.create(paymentRecord);
    }

    public void deletePaymentRecord(PaymentRecord paymentRecordToDelete) throws GetEntityListException, BusinessRuleException, DeleteException {
        Appointment appointment = paymentRecordToDelete.getAppointment();
        if(!appointment.getStatusService().equals(AppointmentStatus.CANCELLED)){
            throw new BusinessRuleException("Not allowed to delete payment record for none cancelled appointments");
        }
        paymentRecordCrudRepository.delete(paymentRecordToDelete.getId());
    }

    public void makePayment(PaymentRecord paymentRecordToMakePayment) throws GetEntityListException, BusinessRuleException, FileCorruptedException, NotFoundException {
//        Appointment appointment = appointmentService.getAppointmentById(paymentRecordToMakePayment.getAppointmentId());
//        if(!appointment.getStatusService().equals(AppointmentStatus.COMPLETED)){
//            throw new BusinessRuleException("Payment can only be made for completed appointments");
//        }
        paymentRecordToMakePayment.setHasPaid(true);
        paymentRecordCrudRepository.update(paymentRecordToMakePayment);
    }

    private String generatePaymentId(){
        while (true){
            String generatedId = RandomIdGenerator.generateId("INV" , 5);
            try {
                PaymentRecord paymentRecord = getPaymentRecordById(generatedId);
                if(paymentRecord == null){
                    return generatedId;
                }
            } catch (FileCorruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
