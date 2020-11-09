package com.mycompany.html_wysiwyg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.InputEvent;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Modality;

import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 
 * @author Javier Torres Sevilla, Cristina Domenech Moreno.
 */
public class PrimaryController implements Initializable {

    private Scene scene;
    private Stage stage;
    private Parent root;
    private Document HTMLdoc;
    private FXMLLoader fxmlLoader;
    private SecondaryController secController;
    private FileChooser fileChooser = new FileChooser();
    private File file;
    private File startDir = new File(System.getProperty("user.home"), "/Desktop"); // Set the html document browser to initialise in the users desktop.
    
    @FXML
    private TabPane TabPane;
    @FXML
    private Tab editorTab;
    @FXML
    private Tab codeTab;
    @FXML
    private TextArea codeArea; // code area is the area that shows the HTML code raw.
    @FXML
    private HTMLEditor editorArea; // editor area is the area of the HTML Editor.
    @FXML
    private Button showMobile;

    @FXML
    private MenuItem open;
    @FXML
    private MenuItem save;
    @FXML
    private MenuItem saveAs;
    @FXML
    private MenuItem aboutUs;
    @FXML
    private Menu help;

    /**
     * Everything inside this method is initialised when the project is started.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML", "*.html"));
        fxmlLoader = new FXMLLoader();

        HTMLdoc = Jsoup.parseBodyFragment("<html><head></head><body></body></html>");
        HTMLdoc.outputSettings().indentAmount(4);
        codeArea.setText(HTMLdoc.toString());

        // Custom event handler that changes the code in the "code area" as well as the mobile view whenever there is ANY change in the HTML editor.
        editorArea.addEventHandler(InputEvent.ANY, (InputEvent event) -> {
            HTMLdoc = Jsoup.parseBodyFragment(editorArea.getHtmlText());
            HTMLdoc.outputSettings().indentAmount(4);
            codeArea.setText(HTMLdoc.toString());
            secController.setHTML(editorArea.getHtmlText());
        });

        // Custom event handler that changes the code in the "code area" as well as the mobile view whenever there is ANY change in the "code area".
        codeArea.addEventHandler(InputEvent.ANY, (InputEvent event) -> {
            editorArea.setHtmlText(codeArea.getText());
            secController.setHTML(editorArea.getHtmlText());
            HTMLdoc = Jsoup.parseBodyFragment(codeArea.getText());
            HTMLdoc.outputSettings().indentAmount(4);
        });
        
        try { // We initialise the secondary controller with the program to avoid having the button "mobile view" open multiple windows when pressed.
            fxmlLoader.setLocation(getClass().getResource("secondary.fxml"));
            root = fxmlLoader.load();

            secController = fxmlLoader.getController();

            Image image = new Image("images/smartphone_icon.png");
            scene = new Scene(root, 360, 740);
            stage = new Stage();
            stage.setTitle("Mobile View");
            stage.setScene(scene);
            stage.getIcons().add(image);
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method opens the window to the mobile view unless it's already open.
     * @param event 
     */
    @FXML
    private void setMobile(ActionEvent event) {
        stage.show();
    }

    /**
     * Method for opening HTML Files from the users computer.
     * @param event 
     */
    @FXML
    private void openFile(ActionEvent event) {
        fileChooser.setInitialDirectory(startDir);
        fileChooser.setTitle("Abrir archivo...");
        file = fileChooser.showOpenDialog(stage);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            editorArea.setHtmlText(reader.lines().collect(Collectors.joining()));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method to save and ovewrite an existing file.
     * @param event 
     */
    @FXML
    private void saveFile(ActionEvent event) {
        if (file != null) {
            Document fileToSave = Jsoup.parseBodyFragment(editorArea.getHtmlText());
            fileToSave.outputSettings().indentAmount(4);
            saveTextToFile(fileToSave.toString(), file);
        } else { // Usa el mismo event que el de action event para ejecutar metodo saveAsFile si el archivo no existe. (Hace Guardar como si no hay archivo existente)
            saveAsFile(event); 
        }

    }
    /**
     * Method to save the currently worked on file as a new file.
     * @param event 
     */
    @FXML
    private void saveAsFile(ActionEvent event) {
        fileChooser.setTitle("Guardar archivo...");
        fileChooser.setInitialDirectory(startDir);
        file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            Document fileToSave = Jsoup.parseBodyFragment(editorArea.getHtmlText());
            fileToSave.outputSettings().indentAmount(4);
            saveTextToFile(fileToSave.toString(), file);
        }
    }

    /**
     * Method that writes the content of a String into a File.
     * @param content String being sent to be written into the file.
     * @param file  File in which you want the String to be written.
     */
    private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Method that shows the information about the creators of the program.
     * @param event 
     */
    @FXML
    private void aboutUsScreen(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("acerda_de.fxml"));
            Parent root2 = fxmlLoader2.load();

            scene = new Scene(root2, 360, 250);
            Stage stageAbout = new Stage();
            stageAbout.setTitle("Acerca de...");
            stageAbout.setScene(scene);
            stageAbout.initModality(Modality.APPLICATION_MODAL);
            stageAbout.setResizable(false);
            stageAbout.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
