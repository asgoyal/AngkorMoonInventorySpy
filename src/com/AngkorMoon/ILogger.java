package com.AngkorMoon;

public interface ILogger {
    void info(String message);
    void collect(String message);
    void writeAll();
}
