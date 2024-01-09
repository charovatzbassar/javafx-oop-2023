package ba.edu.ibu.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

/*

* 2. Add validation for the saveStudent method so it does not allow
*    duplicate ID-s
* 3. List of the users and save and update action should interact with
*    the database instead of the in memory ds
* 4. Search with autocomplete should be available for the list
*    of users in the table
* */

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        stage.setTitle("Users");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}