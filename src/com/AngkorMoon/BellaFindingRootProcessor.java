package com.AngkorMoon;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class BellaFindingRootProcessor implements IUrlProcessor {
    private IHtmlParser htmlParser;

    public BellaFindingRootProcessor(IHtmlParser htmlParser) {
        this.htmlParser = htmlParser;
    }

    @Override
    public InventoryItem process(String url) {
        Document document = this.htmlParser.parse(url);

        Elements inventories = document.select("ul#example");

        InventoryItem inventoryRoot = new InventoryItem("Root", url);
        for (Element inventory : inventories) {
            // get all parent categories
            Elements categories = inventory.getElementsByAttributeValue("class", "menu"); //inventory.select("a[href]");
            for (Element category : categories) {
                String name = category.text();
                String categoryUrl = category.attr("href");
                InventoryItem categoryItem = new InventoryItem(name, categoryUrl);
                inventoryRoot.addSubItem(categoryItem);
            }
        }

        List<InventoryItem> queue = new ArrayList<>(inventoryRoot.getSubItems());
        while (!queue.isEmpty()) {
            InventoryItem currentItem = queue.remove(0);
            System.out.println("Processing item: " + currentItem.getName() + ", link: " + currentItem.getUrl());
            this.processCurrentItem(currentItem);
            queue.addAll(currentItem.getSubItems());
        }

        return inventoryRoot;
    }

    private void processCurrentItem(InventoryItem currentItem) {
        Document document = this.htmlParser.parse(currentItem.getUrl());
        // if there are sub categories, they are inside a table with id = TABLE_CATEGORIES
        Elements categories = document.select("table#TABLE_CATEGORIES");
        if (!categories.isEmpty()) {
            this.processTableCategories(currentItem, categories.first());
        } else {
            // as per their pattern, sub categories will also have a drop down "SELECT ONE"
            categories = document.select("form#myForm");
            if (!categories.isEmpty()) {
                this.processFormOptions(currentItem, categories.first());
            } else {
                // this is a child item, check if its out of stock
                Elements outOfStockFonts = document.select("font.outofstock");
                currentItem.setOutOfStock(!outOfStockFonts.isEmpty());
            }
        }
    }

    private void processTableCategories(InventoryItem parentItem, Element tableCategory) {
        Elements hrefs = tableCategory.select("a[href]");
        for (Element href : hrefs) {
            // hrefs with h2 are the children as per the pattern of bella findings
            Elements h2Elements = href.getElementsByTag("h2");
            if (!h2Elements.isEmpty()) {
                String name = h2Elements.first().text();
                String url = href.attr("href");
                InventoryItem childItem = new InventoryItem(name, url);
                parentItem.addSubItem(childItem);
            }
        }
    }

    private void processFormOptions(InventoryItem parentItem, Element form) {
        Elements options = form.getElementsByTag("option");
        for (Element option : options) {
            String url = option.attr("value");
            // ignore select one, value = #
            if (url.equals("#")) {
                continue;
            }

            String name = option.text();
            InventoryItem childItem = new InventoryItem(name, url);
            parentItem.addSubItem(childItem);
        }
    }
}
