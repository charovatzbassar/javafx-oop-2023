package ba.edu.ibu.javafx.components;

import javafx.scene.control.Alert;

import java.util.Locale;

public class ErrorModal {
    public static void showError(String title, String message) {
        Locale.setDefault(Locale.ENGLISH);
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle(title);
        error.setHeaderText(message);
        error.show();
    }
}
