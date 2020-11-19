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
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javax.swing.JOptionPane;
import static newlms.NewlmsLogin.window;


public class AdminDashboardController implements Initializable {
    Connection conn;
    ResultSet rs;
    PreparedStatement pst;
    
    String user_ID;
    
    @FXML
    private Text userName;

    
    @FXML
    void adminPageClicked(ActionEvent event) throws IOException {
        Button page = (Button) event.getSource();   
        String type = page.getText();
        System.out.println("Type: " + type);
        setPageType(type);
    }
    
    
    @FXML
    void logoutEvent(ActionEvent event) throws IOException {
        System.out.println("LOGOUT EVENT INVOKED FROM DASHBOARD!");
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent loginRoot = loginLoader.load();
            
        Scene loginScene = new Scene(loginRoot);
        window.setScene(loginScene);        
        window.setMaximized(true);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    void setUserDetails(String id) {
        this.user_ID = id; 

        conn = ConnectToDB.connect();
        String queryName = "Select Name from Admin where Admin_ID=?";        
        try {
            pst = conn.prepareStatement(queryName);
            
            pst.setString(1, user_ID);
            rs = pst.executeQuery();
            userName.setText(rs.getString("Name"));             
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
    }

    void setPageType(String type) throws IOException {
        if(type == null || type.equals("Home")) {
            
        }
        else {
            FXMLLoader adminPageLoader = new FXMLLoader(getClass().getResource("AdminPages.fxml"));
            Parent adminPagesRoot = adminPageLoader.load();
            AdminPagesController adminPageController = adminPageLoader.getController();
            adminPageController.setUserDetails(this.user_ID);
            adminPageController.setPageType(type);

            Scene adminPagesScene = new Scene(adminPagesRoot);
            window.setScene(adminPagesScene);
            window.setMaximized(true);

        }
    }

}

