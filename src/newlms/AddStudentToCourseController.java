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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author MARYAM
 */
public class AddStudentToCourseController implements Initializable {

    Connection conn;
    ResultSet rs;
    PreparedStatement pst;

    @FXML
    private TextField studentID;
    
    private String courseID;

    @FXML
    void addStudentToCourse(ActionEvent event) {
        conn = ConnectToDB.connect();
        String query = "Insert into Enrollment(Student_ID, Course_ID) values(?,?)";

        try {
            pst = conn.prepareStatement(query);

            pst.setString(1, studentID.getText());
            pst.setString(2, courseID);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Student Added to Course!");
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
        
        Stage stage = (Stage) studentID.getScene().getWindow();
        stage.close();
    }

    @FXML
    void back(ActionEvent event) {
        Stage stage = (Stage) studentID.getScene().getWindow();
        stage.close();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    void setCurrentID(String selectedId) {
       this.courseID = selectedId;
    }

}
