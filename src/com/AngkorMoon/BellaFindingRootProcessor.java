package com.AngkorMoon;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BellaFindingRootProcessor implements IUrlProcessor {
    private IHtmlParser htmlParser;

    public BellaFindingRootProcessor(IHtmlParser htmlParser) {
        this.htmlParser = htmlParser;
    }

    @Override
    public InventoryItem process(String url) {
        Document document = null;
        try {
            document = this.htmlParser.parse(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert document != null;

        Elements inventories = document.select("ul#example");

        InventoryItem inventoryRoot = new InventoryItem("Root");
        for (Element inventory : inventories) {
            // get all parent categories
            Elements categories = inventory.getElementsByAttributeValue("class", "menu"); //inventory.select("a[href]");
            for (Element category : categories) {
                String title = category.attr("title");
                InventoryItem categoryItem = new InventoryItem(title);
                inventoryRoot.addSubItem(categoryItem);
            }
        }
        for (Element inventoryItem : inventoryItems) {
            // Bella finding html content has parent categories and sub categories listed on the same web page.
            // We will skip the parent categories
            // parent categories have class attribute with value "menu"
            Set<String> classNames = inventoryItem.classNames();
            String title = inventoryItem.attr("title");
            // parent category
            if (classNames.contains("menu")) {
                InventoryItem category = new InventoryItem(title);
                categoryLookup.put(title, category);
                inventoryRoot.addSubItem(category);
            } else {
                // its a child sub category or item

            }
        }

        return null;
    }
}
