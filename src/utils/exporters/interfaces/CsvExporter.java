package utils.exporters.interfaces;

import exceptions.FileCorruptedException;

import java.io.IOException;
import java.util.List;

public interface CsvExporter <T> {

    void exportData(List<T> data, String filePath) throws FileCorruptedException, IOException;

    default String escapeCSV(String value){
        if(value.contains(",") || value.contains("\"")){
            value = value.replace("\"", "\"\"");
            value = "\"" + value + "\"";
        }
        return value;
    }
}
