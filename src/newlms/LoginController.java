/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    ResultSet rs;
    PreparedStatement pst;

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
        String val_id = userID.getText();
        String val_pw = pw.getText();
        String val_type = (String) comb.getValue();
        System.out.println("ID: " + val_id);
        System.out.println("Password: " + val_pw);
        System.out.println("Type: " + val_type);

        if (validateLogin(val_id, val_pw, val_type)) {
            if(val_type.equals("Admin")) {
                openAdminDashboard(val_id);
            }
            else {
                openNonAdminDashboard(val_id, val_type);
            }
        }
    }

    public boolean validateLogin(String id, String pw, String type) {
        conn = ConnectToDB.connect();

        String sql = "Select * from " + type + " where " + type + "_ID" + "=? and Password=?";

        try {
//            Try to check if id doesn't exist or pw is wrong.

            pst = conn.prepareStatement(sql);
            pst.setString(1, id);
            pst.setString(2, pw);
            rs = pst.executeQuery();
            
            if (rs.next()) {
                rs.close();
                pst.close();
                System.out.println("Login Successful");
            } 
            else {
                JOptionPane.showMessageDialog(null, "Incorrect Login Details!");
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
            } 
            catch (Exception e) {

            }
        }
        return true;
    }
    
    public void openNonAdminDashboard(String id, String type) throws IOException {
        FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        Parent dashboardRoot = dashboardLoader.load();
        DashboardController dashboardController = dashboardLoader.getController();
        dashboardController.setUserDetails(id, type);

        Scene dashboardScene = new Scene(dashboardRoot);
        window.setScene(dashboardScene);
        window.setMaximized(true);

    }
    
    public void openAdminDashboard(String id) throws IOException {
        FXMLLoader adminDashboarLoader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
        Parent adminRoot = adminDashboarLoader.load();
        AdminDashboardController adminDashboardController = adminDashboarLoader.getController();
        adminDashboardController.setUserDetails(id);
        adminDashboardController.setPageType(null);

        Scene adminDashboardScene = new Scene(adminRoot);
        window.setScene(adminDashboardScene);
        window.setMaximized(true);

    }

}
