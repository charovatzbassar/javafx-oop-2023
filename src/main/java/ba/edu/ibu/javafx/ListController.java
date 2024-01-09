package ba.edu.ibu.javafx;

import ba.edu.ibu.javafx.models.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class ListController implements Initializable {
    @FXML
    public TextField txtId;
    @FXML
    public TextField txtName;
    @FXML
    public TextField txtSurname;
    @FXML
    public TextField txtYear;
    @FXML
    public TextField txtCycle;
    @FXML
    public TableView tblStudents;
    @FXML
    public TableColumn<Student, Integer> colId;
    @FXML
    public TableColumn<Student, String> colName;
    @FXML
    public TableColumn<Student, String> colSurname;
    @FXML
    public TableColumn<Student, String> colCycle;
    @FXML
    public TableColumn<Student, String> colYear;
    @FXML
    public Button btnSave;
    ObservableList<Student> students = FXCollections.observableArrayList(
            Arrays.asList(
                    new Student(1, "Becir", "Isakovic", "1", "1"),
                    new Student(2, "Bekir", "Ishakovic", "2", "2")
            )
    );
    int selectedIndex;

    static void showError(String title, String message) {
        Locale.setDefault(Locale.ENGLISH);
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle(title);
        error.setHeaderText(message);
        error.show();
    }

    @FXML
    void getStudent(MouseEvent event) {
        selectedIndex = tblStudents.getSelectionModel().getSelectedIndex();
        if (selectedIndex <= -1) return;

        txtId.setText(colId.getCellData(selectedIndex).toString());
        txtName.setText(colName.getCellData(selectedIndex));
        txtSurname.setText(colSurname.getCellData(selectedIndex));
        txtYear.setText(colYear.getCellData(selectedIndex));
        txtCycle.setText(colCycle.getCellData(selectedIndex));
    }

    @FXML
    void saveStudent() {
        Student tempStudent = null;
        if (tblStudents.getSelectionModel() != null) {
            selectedIndex = tblStudents.getSelectionModel().getSelectedIndex();
            if (colId.getCellData(selectedIndex) != null) {
                for (int i = 0; i < students.size(); i++) {
                    if (students.get(i).getId() == colId.getCellData(selectedIndex)) {
                        tempStudent = new Student(Integer.valueOf(txtId.getText()), txtName.getText(), txtSurname.getText(), txtYear.getText(), txtCycle.getText());
                        students.set(i, tempStudent);
                    }
                }
                tblStudents.refresh();
            } else {
                try {
                    if (txtId.getText().equals("") || txtName.getText().equals("") || txtSurname.getText().equals("") || txtYear.getText().equals("") || txtCycle.getText().equals("")) {
                        showError("Invalid attribute", "You have to populate all fields");
                    } else {
                        students.add(new Student(Integer.valueOf(txtId.getText()), txtName.getText(), txtSurname.getText(), txtYear.getText(), txtCycle.getText()));
                    }
                } catch (NumberFormatException e) {
                    showError("Invalid attribute", "You have to populate all fields");
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
    }

    public void initializeTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colCycle.setCellValueFactory(new PropertyValueFactory<>("cycle"));
        reinitializeTable();
    }

    public void reinitializeTable() {
        tblStudents.setItems(students);
    }

}
