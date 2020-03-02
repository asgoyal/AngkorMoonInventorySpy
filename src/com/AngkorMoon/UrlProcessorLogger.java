package com.AngkorMoon;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class UrlProcessorLogger implements ILogger {
    private String logFileName;

    public UrlProcessorLogger(String logFileName) {
        this.logFileName = logFileName;
    }

    @Override
    public void info(String message) {
        try (FileWriter fileWriter = new FileWriter(this.logFileName, true)) {
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(message);
            writer.newLine();   //Add new line
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
