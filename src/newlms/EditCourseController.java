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
public class EditCourseController implements Initializable {
    
    Connection conn;
    ResultSet rs;
    PreparedStatement pst;
    
    @FXML
    private TextField title;

    @FXML
    private TextField teacherID;

    @FXML
    private TextField term;

    @FXML
    private TextField currentID;

    @FXML
    void editCourse(ActionEvent event) {
        
        conn = ConnectToDB.connect();
        String query = "UPDATE Course SET Title=?, term=?, Teacher_ID=?	WHERE Course_ID=?";

        try {
            pst = conn.prepareStatement(query);

            pst.setString(1, title.getText());
            pst.setString(2, term.getText());
            pst.setString(3, teacherID.getText());
            pst.setString(4, currentID.getText());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Edited Course Details");
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
        Stage stage = (Stage) teacherID.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    void back(ActionEvent event) {
        Stage stage = (Stage) teacherID.getScene().getWindow();
        stage.close();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    void setCurrentID(String selectedId) {
        this.currentID.setText(selectedId);
    }
}
