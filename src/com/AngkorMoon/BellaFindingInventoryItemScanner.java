package com.AngkorMoon;

import java.util.ArrayList;
import java.util.List;

public class BellaFindingInventoryItemScanner implements IInventoryItemScanner {
    @Override
    public List<InventoryItem> getOutOfStockInventoryItems(InventoryItem inventory) {
        List<InventoryItem> queue = new ArrayList<>(inventory.getSubItems());
        List<InventoryItem> outOfStockItems = new ArrayList<>();
        while (!queue.isEmpty()) {
            InventoryItem currentItem = queue.remove(0);
            if (currentItem.isOutOfStock()) {
                outOfStockItems.add(currentItem);
            }

            queue.addAll(currentItem.getSubItems());
        }

        return outOfStockItems;
    }
}
