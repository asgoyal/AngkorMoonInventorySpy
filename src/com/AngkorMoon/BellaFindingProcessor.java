package com.AngkorMoon;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class BellaFindingProcessor implements IUrlProcessor {
    private IHtmlParser htmlParser;
    private ILogger processingLogger;
    private ILogger intermediateResultLogger;
    private ILogger outOfStockLogger;
    private boolean loggingEnabled;
    private static int childCount = 0;
    private static int childsProcessed = 20;
    private static boolean hardStop = false;

    public BellaFindingProcessor() {
        this.htmlParser = JsoupHtmlParser.getInstance();
        this.loggingEnabled = true;
        this.processingLogger = new UrlProcessorLogger("InventoryProcessingSteps.log");
        this.intermediateResultLogger = new UrlProcessorLogger("InventoryProcessingIntermediateResults.log");
        this.outOfStockLogger = new UrlProcessorLogger("InventoryOutOfStock.log");
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
            this.processCurrentItem(currentItem);
            if (!hardStop) {
                queue.addAll(currentItem.getSubItems());
            } else {
                this.processingLogger.info("App reached max processing limit, hard stopping");
                break;
            }
        }

        return inventoryRoot;
    }

    private void processCurrentItem(InventoryItem currentItem) {
        Document document = this.htmlParser.parse(currentItem.getUrl());
        // if there are sub categories, they are inside a table with id = TABLE_CATEGORIES
        Elements categories = document.select("table#TABLE_CATEGORIES");
        if (!categories.isEmpty()) {
            String message = "Processing category: " + currentItem.getName() + ", link: " + currentItem.getUrl();
            this.logProcessingMessage(message);
            this.processTableCategories(currentItem, categories);
        } else {
            // as per their pattern, sub categories will also have a drop down "SELECT ONE"
            categories = document.select("form#myForm");
            if (!categories.isEmpty()) {
                String message = "Processing category: " + currentItem.getName() + ", link: " + currentItem.getUrl();
                this.logProcessingMessage(message);
                this.processFormOptions(currentItem, categories);
            } else {
                // this is a child item, get its relevant details
                String message = "Processing item: " + currentItem.getName() + ", link: " + currentItem.getUrl();
                this.logProcessingMessage(message);
                this.processChildItem(currentItem, document);
                this.logIntermediateResultMessage(currentItem);
                if (currentItem.isOutOfStock()) {
                    this.logOutOfStockItem(currentItem);
                }

                childCount++;
                if (childsProcessed != -1 && childCount > childsProcessed) {
                    hardStop = true;
                }
            }
        }
    }

    private void processTableCategories(InventoryItem parentItem, Elements tableCategories) {
        for (Element tableCategory : tableCategories) {
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
    }

    private void processFormOptions(InventoryItem parentItem, Elements forms) {
        for (Element form : forms) {
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

    private void processChildItem(InventoryItem childItem, Document document) {
        Elements itemCodeElements = document.select("p:matches(Item: \\d+)");
        if (!itemCodeElements.isEmpty()) {
            String itemCodeString = itemCodeElements.first().text();
            String code = itemCodeString.replace("Item: ", "");
            childItem.setCode(code);
        }

        Elements outOfStockFonts = document.select("font.outofstock");
        childItem.setOutOfStock(!outOfStockFonts.isEmpty());
    }

    private void logProcessingMessage(String message) {
        System.out.println(message);
        this.processingLogger.info(message);
    }

    private void logIntermediateResultMessage(InventoryItem currentItem) {
        String message = "Item: " + currentItem.getName() + ", code: " + currentItem.getCode() + ", out of stock: " + currentItem.isOutOfStock() + ", link: " + currentItem.getUrl();
        System.out.println(message);
        this.intermediateResultLogger.info(message);
    }

    private void logOutOfStockItem(InventoryItem currentItem) {
        String message = "Out of stock item: " + currentItem.getName() + ", code: " + currentItem.getCode() + ", link: " + currentItem.getUrl();
        System.out.println(message);
        this.outOfStockLogger.info(message);
    }
}