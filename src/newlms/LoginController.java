package newlms;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import static newlms.NewlmsLogin.window;


public class LoginController implements Initializable {

    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    @FXML
    private TextField userID;
    @FXML
    private PasswordField pw;

    @FXML
    private ComboBox comb;
    ObservableList<String> list = FXCollections.observableArrayList("Student", "Teacher", "Admin");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comb.setValue("Student");
        comb.setItems(list);
    }

    @FXML
    private void loginEvent(ActionEvent event) throws IOException {
        int val_id = Integer.parseInt(userID.getText());
        String val_pw = pw.getText();
        String val_type = (String) comb.getValue();
        System.out.println("ID: " + val_id);
        System.out.println("Password: " + val_pw);
        System.out.println("Type: " + val_type);

        if (validateID(val_id, val_type)) {
            if(validatePW(val_id, val_pw, val_type)) {
                if(val_type.equals("Admin")) {
                    System.out.println("READY TO OPEN ADMIN DASHBOARD");
                    openAdminDashboard(val_id);
            }
                else {
                    System.out.println("READY TO OPEN NON ADMIN DASHBOARD");
                    openNonAdminDashboard(val_id, val_type);
                }
            }
        }
    }

    public boolean validateID(int id, String type) {
        conn = ConnectToDB.connect();
        System.out.println("conn connected");
        try {            
            pst = conn.prepareCall("{call id_exists(?, ?)}");
            pst.setInt(1, id);
            pst.setString(2, type);
            pst.execute();
            rs = pst.getResultSet();
            
            if (rs.next()) {
                System.out.println("ID Exists");
            } 
            else {
                JOptionPane.showMessageDialog(null, "Wrong ID!");
                return false;
            }
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } 
        finally {
            try {
                rs.close();
                pst.close();
                conn.close();
            } 
            catch (Exception e) {

            }
        }
        return true;
    }
    
    public void openNonAdminDashboard(int id, String type) throws IOException {
        FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        Parent dashboardRoot = dashboardLoader.load();
        DashboardController dashboardController = dashboardLoader.getController();
        
        dashboardController.setUserDetails(id, type);
        
        Scene dashboardScene = new Scene(dashboardRoot);
        window.setScene(dashboardScene);
        window.setMaximized(true);

    }
    
    public void openAdminDashboard(int id) throws IOException {
        FXMLLoader adminDashboarLoader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
        Parent adminRoot = adminDashboarLoader.load();
        AdminDashboardController adminDashboardController = adminDashboarLoader.getController();
        
        
        adminDashboardController.setUserDetails(id);
        adminDashboardController.setPageType(null);

        Scene adminDashboardScene = new Scene(adminRoot);
        window.setScene(adminDashboardScene);
        window.setMaximized(true);

    }

    private boolean validatePW(int id, String pw, String type) {
        conn = ConnectToDB.connect();
        System.out.println("conn connected");
        try {            
            pst = conn.prepareCall("{call check_pw(?, ?, ?)}");
            pst.setInt(1, id);
            pst.setString(2, pw);
            pst.setString(3, type);
            pst.execute();
            rs = pst.getResultSet();
            
            if (rs.next()) {
                System.out.println("LOGIN SUCCESSFULL");
            } 
            else {
                JOptionPane.showMessageDialog(null, "WRONG PASSWORD!");
                return false;
            }
        } 
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        } 
        finally {
            try {
                rs.close();
                pst.close();
                conn.close();
            } 
            catch (Exception e) {

            }
        }
        return true;
    }

}
