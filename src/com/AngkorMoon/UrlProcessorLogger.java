package com.AngkorMoon;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class UrlProcessorLogger implements ILogger {
    private String logFileName;
    private BlockingQueue<String> logQueue;

    public UrlProcessorLogger(String logFileName, int queueSize) {
        this.logFileName = logFileName;
        this.logQueue = new ArrayBlockingQueue<>(queueSize);
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

    @Override
    public void collect(String message) {
        try {
            this.logQueue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeAll() {
        List<String> copy = new ArrayList<>(this.logQueue.size());
        this.logQueue.drainTo(copy);
        try (FileWriter fileWriter = new FileWriter(this.logFileName, true);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {
            for (String log : copy) {
                System.out.println(log);
                writer.write(log);
                writer.newLine();   //Add new line
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
