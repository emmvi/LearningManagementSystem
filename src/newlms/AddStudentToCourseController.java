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


public class AddStudentToCourseController implements Initializable {

    Connection conn;
    ResultSet rs;
    PreparedStatement pst;

    @FXML
    private TextField studentID;
    
    private int courseID;

    @FXML
    void addStudentToCourse(ActionEvent event) {
        conn = ConnectToDB.connect();
        
        try {
            pst = conn.prepareCall("{call add_std_to_course(?,?)}");
            pst.setInt(1, Integer.parseInt(studentID.getText()));
            pst.setInt(2, courseID);

            pst.execute();

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

    void setCurrentID(int selectedId) {
       this.courseID = selectedId;
    }

}
