package com.AngkorMoon;

import java.util.List;

public interface IInventoryItemScanner {
    List<InventoryItem> getOutOfStockInventoryItems(InventoryItem inventory);
}
