package com.mycompany.html_wysiwyg;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author Javier Torres Sevilla, Cristina Domenech Moreno
 */
public class SecondaryController{

    @FXML
    private WebView webView;

    /**
     * This method obtains the HTML text from the editor area of the previous controller and sends it to the web view for displaying.
     * @param htmlText String that will be sent to web view to show.
     */
    public void setHTML(String htmlText) {
        final WebEngine mobileEngine = webView.getEngine();
        
        // This sets the HTML content to be non-editable for the mobile view.
        if (htmlText.contains("contenteditable=\"true\"")) {
            htmlText = htmlText.replace("contenteditable=\"true\"", "contenteditable=\"false\"");
        }
        mobileEngine.loadContent(htmlText);
    }
}
