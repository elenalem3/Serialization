package ru.netology;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {

    protected List<String[]> exportActions = new ArrayList<>();

    //метод сохранения действия добавления покупки покупателем
    public void log(int productNum, int amount) {
        exportActions.add(new String[]{String.valueOf(productNum), String.valueOf(amount)});

    }

    //метод для сохранения всего журнала действия в файл в формате csv
    public void exportAsCSV(File txtFile) {
        try (CSVWriter csvWriter = new CSVWriter (new FileWriter(txtFile, true));
             FileInputStream stream = new FileInputStream(txtFile)) {
            if (stream.read() == -1) {
                csvWriter.writeNext(new String[] {"productNum", "amount"});
            }
            csvWriter.writeAll(exportActions);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
