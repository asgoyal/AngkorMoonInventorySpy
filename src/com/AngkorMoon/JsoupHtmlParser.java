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
    public Document parse(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert document != null;
        return document;
    }
}
