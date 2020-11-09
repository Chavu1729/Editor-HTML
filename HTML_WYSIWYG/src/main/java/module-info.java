module com.mycompany.html_wysiwyg {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.jsoup;
    requires java.base;

    opens com.mycompany.html_wysiwyg to javafx.fxml, javafx.web;
    exports com.mycompany.html_wysiwyg;
}
