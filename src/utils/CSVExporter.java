package utils;

import exceptions.FileCorruptedException;
import utils.exporters.interfaces.CsvExporter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CSVExporter<T>{

    private final Logger logger = Logger.getLogger(CSVExporter.class.getName());

    public void exportData(List <T> data, String fileName, CsvExporter<T> exporter) {
        if(data.isEmpty()){
            DialogUtil.showWarningMessage("No data" , "Data is empty");
            return;
        }
        String userHome = System.getProperty("user.home");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String currentDateTime = now.format(formatter);
        String filePath = String.format("%s_%s.csv" , fileName,currentDateTime);
        String downloads = Paths.get(userHome,"Downloads",filePath).toString();
        try {
            exporter.exportData(data,downloads);
            File file = new File(downloads);
            if(Desktop.isDesktopSupported()){
                Desktop.getDesktop().open(file);
            }
        } catch (FileCorruptedException | IOException e) {
            logger.log(Level.SEVERE , e.getMessage());
        }
        logger.log(Level.INFO , "Successfully exported data");
        DialogUtil.showInfoMessage("Export Successful","Successfully exported data");
    }
}
