package com.AngkorMoon;

import java.util.List;

public class Main {

    public static void main(String[] args) {
	    IUrlProcessor processor = new BellaFindingRootProcessor(JsoupHtmlParser.getInstance());
        System.out.println("Processing Bella Findings Inventory");
	    InventoryItem inventory = processor.process("https://www.bellafindings.com");
	    IInventoryItemScanner scanner = new BellaFindingInventoryItemScanner();
	    System.out.println("Scanning for out of stock items");
        List<InventoryItem> outOfStockItems = scanner.getOutOfStockInventoryItems(inventory);
        System.out.println("Bella Findings");
        System.out.println("Out of stock item :-");
	    for (InventoryItem inventoryItem : outOfStockItems) {
	        System.out.println(inventoryItem.getName());
        }
    }
}
