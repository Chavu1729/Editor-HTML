package com.mycompany.html_wysiwyg;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class SecondaryController{

    @FXML
    private WebView webView;

    public void setHTML(String htmlText) {
        final WebEngine mobileEngine = webView.getEngine();
        
        if (htmlText.contains("contenteditable=\"true\"")) {
            htmlText = htmlText.replace("contenteditable=\"true\"", "contenteditable=\"false\"");
        }
        mobileEngine.loadContent(htmlText);
    }
}
