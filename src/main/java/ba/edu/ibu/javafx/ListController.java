package ba.edu.ibu.javafx;

import ba.edu.ibu.javafx.components.ErrorModal;
import ba.edu.ibu.javafx.models.Student;
import ba.edu.ibu.javafx.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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

    @FXML
    public TextField searchTerm;

    @FXML
    public Button searchButton;

    private static final DBConnection dbConnection = new DBConnection();
    ObservableList<Student> students = FXCollections.observableArrayList(Arrays.asList());

    void fetchStudents() {
        try {
            PreparedStatement statement = dbConnection.getConnection().prepareStatement("select * from student;");
            ResultSet rs = statement.executeQuery();

            while(rs.next()) {
                Student student = new Student(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("yearOfStudy"), rs.getString("cycle"));
                students.add(student);
            }


        } catch (SQLException e) {

        }
    }

    int selectedIndex;

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
        if (tblStudents.getSelectionModel() != null) {
            selectedIndex = tblStudents.getSelectionModel().getSelectedIndex();
            if (colId.getCellData(selectedIndex) != null) {
                for (int i = 0; i < students.size(); i++) {
                    if (students.get(i).getId() == colId.getCellData(selectedIndex)) {
                        Student tempStudent = new Student(Integer.valueOf(txtId.getText()), txtName.getText(), txtSurname.getText(), txtYear.getText(), txtCycle.getText());
                        students.set(i, tempStudent);
                        modifyStudent(tempStudent);
                    }
                }
                tblStudents.refresh();
            } else {
                try {
                    if (txtId.getText().equals("") || txtName.getText().equals("") || txtSurname.getText().equals("") || txtYear.getText().equals("") || txtCycle.getText().equals("")) {
                        ErrorModal.showError("Invalid attribute", "You have to populate all fields");
                    }
                    else if (!isValidId(txtId.getText())) {
                        ErrorModal.showError("Invalid ID", "Student ID is invalid");
                    }
                    else {
                        Student newStudent = new Student(Integer.valueOf(txtId.getText()), txtName.getText(), txtSurname.getText(), txtYear.getText(), txtCycle.getText());
                        students.add(newStudent);
                        saveStudentToDB(newStudent);
                    }
                } catch (NumberFormatException e) {
                    ErrorModal.showError("Invalid attribute", "You have to populate all fields");
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fetchStudents();
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

    public boolean saveStudentToDB(Student student) {
        try {
            PreparedStatement statementNewUser = dbConnection.getConnection().prepareStatement("INSERT INTO student (id, firstName, lastName, yearOfStudy, cycle) VALUES (? ,?, ?, ?, ?);");
            statementNewUser.setInt(1, student.getId());
            statementNewUser.setString(2, student.getName());
            statementNewUser.setString(3, student.getSurname());
            statementNewUser.setString(4, student.getYear());
            statementNewUser.setString(5, student.getCycle());

            int rowsAffected = statementNewUser.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean modifyStudent(Student student) {
        try {
            PreparedStatement statementNewUser = dbConnection.getConnection().prepareStatement("UPDATE student SET id = ?, firstName = ?, lastName = ?, yearOfStudy = ?, cycle = ? WHERE id = ? ;");
            statementNewUser.setInt(1, student.getId());
            statementNewUser.setString(2, student.getName());
            statementNewUser.setString(3, student.getSurname());
            statementNewUser.setString(4, student.getYear());
            statementNewUser.setString(5, student.getCycle());
            statementNewUser.setInt(6, student.getId());

            int rowsAffected = statementNewUser.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @FXML
    public void searchStudents() {
        try {
            PreparedStatement statement = dbConnection.getConnection().prepareStatement("select * from student where concat(firstName, ' ', lastName) like ?;");
            statement.setString(1, "%" + searchTerm.getText() + "%");
            ResultSet rs = statement.executeQuery();

            ObservableList<Student> foundStudents = FXCollections.observableArrayList(Arrays.asList());

            while (rs.next()) {
                foundStudents.add(new Student(rs.getInt("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("yearOfStudy"), rs.getString("cycle")));
            }

            tblStudents.setItems(foundStudents);

        } catch (SQLException e) {
        }
    }

    public boolean isValidId(String id) {
        try {
            PreparedStatement statement = dbConnection.getConnection().prepareStatement("select id from student where id = ?");
            statement.setInt(1, Integer.parseInt(id));
            ResultSet rs = statement.executeQuery();

            return !rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

}
