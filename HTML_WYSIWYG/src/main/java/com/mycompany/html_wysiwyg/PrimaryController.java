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
    private File startDir = new File(System.getProperty("user.home"), "/Desktop");

    @FXML
    private TabPane TabPane;
    @FXML
    private Tab editorTab;
    @FXML
    private Tab codeTab;
    @FXML
    private TextArea codeArea;
    @FXML
    private HTMLEditor editorArea;
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
     * Todo lo que esta dentro de este metodo se inicializa con la ventana.
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

        // Esto se ocupa de cambiar el codigo tanto en el area donde se muestra el codigo HTML basico como en el mobile view.
        editorArea.addEventHandler(InputEvent.ANY, (InputEvent event) -> {
            HTMLdoc = Jsoup.parseBodyFragment(editorArea.getHtmlText());
            HTMLdoc.outputSettings().indentAmount(4);
            codeArea.setText(HTMLdoc.toString());
            secController.setHTML(editorArea.getHtmlText());
        });

        // Esto se ocupa de cambiar el codigo tanto en el area donde se muestra el editor HTML como en el mobile view.
        codeArea.addEventHandler(InputEvent.ANY, (InputEvent event) -> {
            editorArea.setHtmlText(codeArea.getText());
            secController.setHTML(editorArea.getHtmlText());
            HTMLdoc = Jsoup.parseBodyFragment(codeArea.getText());
            HTMLdoc.outputSettings().indentAmount(4);
        });
        
        try { // Inicializamos la ventana de mobile view cuando se haga el programa para que no se abran muchas ventanas al pulsar el boton del mobile view.
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
     * Metodo que abre la ventana de la vista movil.
     * @param event 
     */
    @FXML
    private void setMobile(ActionEvent event) {
        stage.show();
    }

    /**
     * Metodo para abrir archivos.
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
     * Metodo para guardar archivos y sobreescribirlos.
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
     * Metodo para guardar archivos como nuevo archivo.
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
     * Metodo que guarda el contenido de un string a un File.
     * @param content String que le envias para guardar en el archivo.
     * @param file  Archivo en el cual quieres guardar el contenido.
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
     * Metodo que meustra ventana de informacion sobre los colaboradores.
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
