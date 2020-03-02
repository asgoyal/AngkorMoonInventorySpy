package com.AngkorMoon;

import com.google.gson.Gson;

import java.io.*;

public class FileHelper implements IFileHelper {
    private static IFileHelper instance;

    public static IFileHelper getInstance() {
        if (instance == null) {
            instance = new FileHelper();
        }

        return instance;
    }

    @Override
    public void writeObjToJsonFile(Object obj, String fileName) {
        Gson gson = new Gson();

        try {
            gson.toJson(obj, new FileWriter(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InventoryItem readJsonFileToObj(String fileName) {
        Gson gson = new Gson();

        InventoryItem inventoryItem = null;

        try {
            inventoryItem = gson.fromJson(new FileReader(fileName), InventoryItem.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert inventoryItem != null;

        return inventoryItem;
    }

    @Override
    public boolean doesFileExists(String fileName) {
        return new File(fileName).exists();
    }
}
