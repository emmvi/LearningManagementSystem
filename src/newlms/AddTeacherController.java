/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlms;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author MARYAM
 */
public class AddTeacherController implements Initializable {

    Connection conn;
    ResultSet rs;
    PreparedStatement pst;

    @FXML
    private TextField name;

    @FXML
    private TextField designation;

    @FXML
    private TextField salary;

    @FXML
    private PasswordField pw;

    @FXML
    void addTeacher(ActionEvent event) {
        conn = ConnectToDB.connect();
        String query = "Insert into Teacher(Name, Password, Designation, Salary) values(?,?,?,?)";

        try {
            pst = conn.prepareStatement(query);

            pst.setString(1, name.getText());
            pst.setString(2, pw.getText());
            pst.setString(3, designation.getText());
            pst.setString(4, salary.getText());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "New Teacher Added!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e);
        } finally {
            try {
                rs.close();
                pst.close();
                conn.close();
            } catch (Exception e) {

            }
        }

        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    @FXML
    void back(ActionEvent event) {
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
