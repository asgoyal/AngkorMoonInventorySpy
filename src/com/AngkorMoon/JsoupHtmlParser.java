package com.AngkorMoon;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class JsoupHtmlParser implements IHtmlParser {
    private static IHtmlParser instance = null;

    public static IHtmlParser getInstance() {
        if (instance == null) {
            instance = new JsoupHtmlParser();
        }

        return instance;
    }

    @Override
    public Document parse(String url) throws IOException {
        Document document = Jsoup.connect(url).timeout(5000).get();

        assert document != null;
        return document;
    }
}
