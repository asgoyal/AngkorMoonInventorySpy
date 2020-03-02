package com.AngkorMoon;

import java.util.ArrayList;
import java.util.List;

public class InventoryItem {
    private String name;
    private List<InventoryItem> subItems;
    private boolean isOutOfStock;
    private String url;

    public InventoryItem(String name, String url) {
        this.name = name;
        this.url = url;
        this.subItems = new ArrayList<>();
        this.isOutOfStock = false;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
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
