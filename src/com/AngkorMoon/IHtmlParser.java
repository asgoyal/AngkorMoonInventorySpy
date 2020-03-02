package com.AngkorMoon;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface IHtmlParser {
    Document parse(String url);
}
