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
public class EditStudentController implements Initializable {
    
    Connection conn;
    ResultSet rs;
    PreparedStatement pst;
    
    @FXML
    private TextField name;

    @FXML
    private TextField program;

    @FXML
    private TextField batch;

    @FXML
    private PasswordField pw;

    @FXML
    private TextField studentID;
    

    @FXML
    void back(ActionEvent event) {
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    @FXML
    void editStudent(ActionEvent event) {
        conn = ConnectToDB.connect();
        String query = "UPDATE Student SET Name=?, Password=?, Program=?, Batch=? WHERE Student_ID=?";

        try {
            pst = conn.prepareStatement(query);

            pst.setString(1, name.getText());
            pst.setString(2, pw.getText());
            pst.setString(3, program.getText());
            pst.setString(4, batch.getText());
            pst.setString(5, studentID.getText());
            
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "Edited Student Details");
        } 
        catch (Exception e) {
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    void setCurrentID(String selectedId) {
        this.studentID.setText(selectedId);
    }

}
