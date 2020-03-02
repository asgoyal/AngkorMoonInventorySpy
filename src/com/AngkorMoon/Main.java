package com.AngkorMoon;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        String jsonFile = "bellafindingsinventory.json";
        System.out.println("Checking if inventory file " + jsonFile + " exists...");
        IFileHelper fileHelper = FileHelper.getInstance();
        InventoryItem inventory = null;

        if (fileHelper.doesFileExists(jsonFile)) {
            System.out.println("Inventory found on local file, reading data from it");
            inventory = fileHelper.readJsonFileToObj(jsonFile);
        } else {
            System.out.println("Inventory not found on local file, processing data from bella findings website...");
            IUrlProcessor processor = new BellaFindingProcessor();
            System.out.println("Processing Bella Findings Inventory....");
            inventory = processor.process("https://www.bellafindings.com");

            System.out.println("Storing inventory to file " + jsonFile);
            fileHelper.writeObjToJsonFile(inventory, jsonFile);
        }

	    IInventoryItemScanner scanner = new BellaFindingInventoryItemScanner();
	    System.out.println("Scanning for out of stock items");
	    assert inventory != null;
        List<InventoryItem> outOfStockItems = scanner.getOutOfStockInventoryItems(inventory);
        System.out.println("Bella Findings");
        System.out.println("Out of stock item :-");
	    for (InventoryItem inventoryItem : outOfStockItems) {
	        System.out.println("Out of stock item: " + inventoryItem.getName() + ", code: " + inventoryItem.getCode() + ", link: " + inventoryItem.getUrl());
        }
    }
}
