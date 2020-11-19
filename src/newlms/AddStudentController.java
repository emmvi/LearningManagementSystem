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


public class AddStudentController implements Initializable {
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
    void back(ActionEvent event) {

    }

    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void submit(ActionEvent event) {
//        
        conn = ConnectToDB.connect();
        String query = "Insert into Student(Name, Password, Program, Batch) values(?,?,?,?)";       

        try {
            pst = conn.prepareStatement(query);  
            
            pst.setString(1, name.getText());
            pst.setString(2, pw.getText());
            pst.setString(3, program.getText());
            pst.setString(4, batch.getText());
            
            pst.executeUpdate();       
            
            JOptionPane.showMessageDialog(null, "New Student Added!");
        }
        catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e);
        }
        
        finally {
            try{ 
                rs.close();
                pst.close();
                conn.close();
            }  
            catch(Exception e) {
              
            }
        }
        
        
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }
    
    
}
