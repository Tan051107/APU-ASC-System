package mapper;

import exceptions.FileCorruptedException;
import mapper.interfaces.Mapper;
import models.PaymentRecord;

import java.time.LocalDateTime;

public class PaymentRecordMapper implements Mapper<PaymentRecord> {
    @Override
    public PaymentRecord toObject(String row) throws FileCorruptedException {
        String[] paymentData = row.split("\\|");
        if(paymentData.length != 8){
            throw new FileCorruptedException("Appointment file contains extra data or has missing data");
        }
        String paymentDateTimeString = paymentData[3];
        String paymentMethodString = paymentData[4];

        PaymentRecord paymentRecord = new PaymentRecord();

        if(paymentDateTimeString.isEmpty() || paymentMethodString.isEmpty()){
            paymentRecord.setPaymentDateTime(null);
            paymentRecord.setPaymentMethod(null);
        }
        else{
            paymentRecord.setPaymentDateTime(LocalDateTime.parse(paymentDateTimeString));
            paymentRecord.setPaymentMethod(paymentMethodString);
        }

        paymentRecord.setId(paymentData[0]);
        paymentRecord.setAppointmentId(paymentData[1]);
        paymentRecord.setAmount(Double.parseDouble(paymentData[2]));
        paymentRecord.setHasPaid(Boolean.parseBoolean(paymentData[5]));
        paymentRecord.setCreatedAt(LocalDateTime.parse(paymentData[6]));
        paymentRecord.setUpdatedAt(LocalDateTime.parse(paymentData[7]));

        return paymentRecord;
    }

    @Override
    public String toString(PaymentRecord paymentRecord) {
        String paymentDateTimeString = paymentRecord.getPaymentDateTime() == null ? "" : paymentRecord.getPaymentDateTime().toString();
        String paymentMethodString = paymentRecord.getPaymentMethod() == null ? "" : paymentRecord.getPaymentMethod();
        return String.join("|" ,
                paymentRecord.getId(),
                paymentRecord.getAppointmentId(),
                String.valueOf(paymentRecord.getAmount()),
                paymentDateTimeString,
                paymentMethodString,
                String.valueOf(paymentRecord.isHasPaid()),
                paymentRecord.getCreatedAt().toString(),
                paymentRecord.getUpdatedAt().toString()
                );
    }
}
