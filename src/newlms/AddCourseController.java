package newlms;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.sql.Connection;

/**
 * FXML Controller class
 *
 * @author MARYAM
 */
public class AddCourseController implements Initializable {

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
    private void addCourse(ActionEvent event) {
        System.out.println("ADD COYasSAWSDASDAS");
        conn = ConnectToDB.connect();
        String query = "Insert into Course(Title, Teacher_ID, Term) values(?,?,?)";

        try {
            pst = conn.prepareStatement(query);

            pst.setString(1, title.getText());
            pst.setString(2, teacherID.getText());
            pst.setString(3, term.getText());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, "New Course Added!");
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

        Stage stage = (Stage) title.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void back(ActionEvent event) {

        Stage stage = (Stage) title.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
