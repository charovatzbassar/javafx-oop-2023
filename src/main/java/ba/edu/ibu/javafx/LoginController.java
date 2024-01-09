package ba.edu.ibu.javafx;

import ba.edu.ibu.javafx.components.ErrorModal;
import ba.edu.ibu.javafx.models.User;
import ba.edu.ibu.javafx.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    public Button btnLogin;
    @FXML
    public TextField emailField;
    @FXML
    public Label emailFieldLabel;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label passwordFieldLabel;
    private Parent root;
    private Stage stage;
    private Scene scene;

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d).{8,}$";

    private static final DBConnection dbConnection = DBConnection.getInstance();

    @FXML
    void login(ActionEvent event) throws IOException {
        User user = new User(emailField.getText(), passwordField.getText());
        if (credentialsValid(user)) {
            switchToStudentsScreen(event);
        } else {
            ErrorModal.showError("Invalid credentials", "Please try again");
        }
    }

    boolean credentialsValid(User user) {
        if (!user.getEmail().matches(EMAIL_REGEX) || !user.getPassword().matches(PASSWORD_REGEX)) {
            return false;
        }

        try {
            PreparedStatement statement = dbConnection.getConnection().prepareStatement("select email, password from user where email = '" + user.getEmail() + "' and password = '" + user.getPassword() + "';");
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    void switchToStudentsScreen(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("students.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
