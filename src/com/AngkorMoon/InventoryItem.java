package com.AngkorMoon;

import java.util.ArrayList;
import java.util.List;

public class InventoryItem {
    private String name;
    private List<InventoryItem> subItems;
    private boolean isOutOfStock;

    public InventoryItem(String name) {
        this.name = name;
        this.subItems = new ArrayList<>();
        this.isOutOfStock = false;
    }

    public String getName() {
        return this.name;
    }

    public List<InventoryItem> getSubItems() {
        return subItems;
    }

    public void addSubItem(InventoryItem item) {
        this.subItems.add(item);
    }

    public boolean isOutOfStock() {
        return isOutOfStock;
    }

    public void setOutOfStock(boolean outOfStock) {
        isOutOfStock = outOfStock;
    }

    public boolean isChildItem() {
        return this.subItems.isEmpty();
    }
}
