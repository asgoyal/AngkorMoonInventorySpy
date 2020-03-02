package com.AngkorMoon;

public class Main {

    public static void main(String[] args) {
	    IUrlProcessor processor = new BellaFindingRootProcessor(JsoupHtmlParser.getInstance());
	    processor.process("https://www.bellafindings.com");
    }
}
