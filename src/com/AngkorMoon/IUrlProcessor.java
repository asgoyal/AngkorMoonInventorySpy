package com.AngkorMoon;

import org.jsoup.nodes.Document;

public interface IUrlProcessor {
    InventoryItem process(String url);
}
