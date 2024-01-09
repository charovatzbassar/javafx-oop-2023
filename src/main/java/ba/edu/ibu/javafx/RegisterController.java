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

public class RegisterController implements Initializable {
    private Parent root;
    private Stage stage;
    private Scene scene;

    @FXML
    public Button btnRegister;
    @FXML
    public TextField emailField;
    @FXML
    public Label emailFieldLabel;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label passwordFieldLabel;
    @FXML
    public Button goToLoginButton;
    private static final DBConnection dbConnection = DBConnection.getInstance();
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d).{8,}$";

    @FXML
    void register (ActionEvent event) throws IOException {
        User user = new User(emailField.getText(), passwordField.getText());
        if (credentialsValid(user)) {
            switchToStudentsScreen(event);
        } else {
            ErrorModal.showError("Invalid credentials", "Invalid credentials, or user already exists.");
        }
    }

    void switchToStudentsScreen(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("students.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    boolean credentialsValid(User user) {
        if (!user.getEmail().matches(EMAIL_REGEX) || !user.getPassword().matches(PASSWORD_REGEX)) {
            return false;
        }

        try {
            PreparedStatement statementUser = dbConnection.getConnection().prepareStatement("select email, password from user where email = ? and password = ?;");
            statementUser.setString(1, user.getEmail());
            statementUser.setString(2, user.getPassword());

            ResultSet rs = statementUser.executeQuery();

            if (rs.next()) {
                return false;
            }

            PreparedStatement statementNewUser = dbConnection.getConnection().prepareStatement("INSERT INTO user (email, password) VALUES (? ,?);");
            statementNewUser.setString(1, user.getEmail());
            statementNewUser.setString(2, user.getPassword());

            int rowsAffected = statementNewUser.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            return false;
        }
    }


    @FXML
    void goToLoginPage(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
