package utils.exporters.CsvExporters;

import exceptions.GetEntityListException;
import models.PaymentRecord;
import utils.DialogUtil;
import utils.exporters.interfaces.CsvExporter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentRecordCsvExporter implements CsvExporter<PaymentRecord> {
    Logger logger = Logger.getLogger(PaymentRecordCsvExporter.class.getName());
    @Override
    public void exportData(List<PaymentRecord> paymentRecords, String filePath) throws IOException {
        try(FileWriter fileWriter = new FileWriter(filePath)){
            fileWriter.append("Record Id, Appointment Id, Amount , Payment Method, Payment Status, Payment Date, Payment Time,Payment Collected By\n");
            for(PaymentRecord paymentRecord : paymentRecords){
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalDateTime paymentDateTime = paymentRecord.getPaymentDateTime();
                String paymentDate =  paymentDateTime == null ? "-" :" "+paymentDateTime.toLocalDate().format(dateFormatter);
                String paymentTime = paymentDateTime == null ? "-" :paymentDateTime.toLocalTime().format(timeFormatter);
                String paymentStatus = paymentRecord.isHasPaid() ? "Paid" : "Unpaid";
                String paymentMethod = paymentRecord.getPaymentMethod() == null ? "-" : paymentRecord.getPaymentMethod();
                String paymentCollectedBy = paymentRecord.getPaymentCollectedBy() == null ? "-" : paymentRecord.getStaffCollectPayment().getName();
                String row = String.join(",",
                        escapeCSV(paymentRecord.getId()),
                        escapeCSV(paymentRecord.getAppointmentId()),
                        escapeCSV(String.valueOf(paymentRecord.getAmount())),
                        escapeCSV(paymentMethod),
                        escapeCSV(paymentStatus),
                        escapeCSV(paymentDate),
                        escapeCSV(paymentTime),
                        escapeCSV(paymentCollectedBy)
                        );
                fileWriter.append(row).append("\n");
            }
        } catch (GetEntityListException e) {
            logger.log(Level.SEVERE,e.getMessage());
            DialogUtil.showErrorMessage("Encounter Error" , "Failed to export payment records");
        }
    }
}
