package com.AngkorMoon;

public interface IFileHelper {
    void writeObjToJsonFile(Object obj, String fileName);
    InventoryItem readJsonFileToObj(String fileName);
    boolean doesFileExists(String fileName);
}