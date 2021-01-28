package com.esliceu.utils;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.text.TextContentRenderer;

public class Util {
    public String renderMarkdown2HTML(String s) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(s);
        HtmlRenderer renderer = HtmlRenderer.builder().escapeHtml(true).sanitizeUrls(true).build();
        return renderer.render(document);
    }

    public String renderMarkdown2TEXT(String s) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(s);
        TextContentRenderer renderer = TextContentRenderer.builder().build();
        return renderer.render(document);
    }
}
