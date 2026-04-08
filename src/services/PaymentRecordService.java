package services;

import exceptions.FileCorruptedException;
import mapper.PaymentRecordMapper;
import models.PaymentRecord;
import repositories.CrudRepository;
import utils.RandomIdGenerator;

import java.io.IOException;
import java.util.List;

public class PaymentRecordService {
    private final String PAYMENT_RECORD_FILE = "txt_files/PaymentRecord.txt";
    private final PaymentRecordMapper paymentRecordMapper = new PaymentRecordMapper();
    private final CrudRepository<PaymentRecord> paymentRecordCrudRepository = new CrudRepository<>(PAYMENT_RECORD_FILE,paymentRecordMapper);

    public List<PaymentRecord> getPaymentRecords() throws FileCorruptedException {
        return paymentRecordCrudRepository.getAll();
    }

    public PaymentRecord getPaymentRecord(String paymentId) throws FileCorruptedException {
        return paymentRecordCrudRepository.getOne(paymentId);
    }

    public void addPaymentRecord(PaymentRecord paymentRecord) throws IOException {
        String paymentRecordId = generatePaymentId();
        paymentRecord.setId(paymentRecordId);
        paymentRecord.setPaymentDateTime(null);
        paymentRecord.setPaymentMethod(null);
        paymentRecord.setHasPaid(false);
        paymentRecordCrudRepository.create(paymentRecord);
    }

    private String generatePaymentId(){
        while (true){
            String generatedId = RandomIdGenerator.generateId("INV" , 5);
            try {
                PaymentRecord paymentRecord = getPaymentRecord(generatedId);
                if(paymentRecord == null){
                    return generatedId;
                }
            } catch (FileCorruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
