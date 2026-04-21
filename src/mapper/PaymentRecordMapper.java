package mapper;

import exceptions.FileCorruptedException;
import mapper.interfaces.Mapper;
import models.PaymentRecord;

import java.time.LocalDateTime;

public class PaymentRecordMapper implements Mapper<PaymentRecord> {
    @Override
    public PaymentRecord toObject(String row) throws FileCorruptedException {
        String[] paymentData = row.split("\\|");
        if(paymentData.length != 9){
            throw new FileCorruptedException("Payment file contains extra data or has missing data:" + paymentData.length);
        }
        String paymentDateTimeString = paymentData[3];
        String paymentMethodString = paymentData[4];
        String paymentCollectedByString = paymentData[6];

        PaymentRecord paymentRecord = new PaymentRecord();

        if(paymentDateTimeString.isEmpty() || paymentMethodString.isEmpty() || paymentCollectedByString.isEmpty()){
            paymentRecord.setPaymentDateTime(null);
            paymentRecord.setPaymentMethod(null);
            paymentRecord.setPaymentCollectedBy(null);
        }
        else{
            paymentRecord.setPaymentDateTime(LocalDateTime.parse(paymentDateTimeString));
            paymentRecord.setPaymentMethod(paymentMethodString);
            paymentRecord.setPaymentCollectedBy(paymentCollectedByString);
        }

        paymentRecord.setId(paymentData[0]);
        paymentRecord.setAppointmentId(paymentData[1]);
        paymentRecord.setAmount(Double.parseDouble(paymentData[2]));
        paymentRecord.setHasPaid(Boolean.parseBoolean(paymentData[5]));
        paymentRecord.setCreatedAt(LocalDateTime.parse(paymentData[7]));
        paymentRecord.setUpdatedAt(LocalDateTime.parse(paymentData[8]));

        return paymentRecord;
    }

    @Override
    public String toString(PaymentRecord paymentRecord) {
        String paymentDateTimeString = paymentRecord.getPaymentDateTime() == null ? "" : paymentRecord.getPaymentDateTime().toString();
        String paymentMethodString = paymentRecord.getPaymentMethod() == null ? "" : paymentRecord.getPaymentMethod();
        String paymentCollectedByString = paymentRecord.getPaymentCollectedBy() == null ? "" : paymentRecord.getPaymentCollectedBy();
        return String.join("|" ,
                paymentRecord.getId(),
                paymentRecord.getAppointmentId(),
                String.valueOf(paymentRecord.getAmount()),
                paymentDateTimeString,
                paymentMethodString,
                String.valueOf(paymentRecord.isHasPaid()),
                paymentCollectedByString,
                paymentRecord.getCreatedAt().toString(),
                paymentRecord.getUpdatedAt().toString()
                );
    }
}
