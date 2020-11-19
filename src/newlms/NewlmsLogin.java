/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlms;

import com.sun.org.apache.bcel.internal.generic.L2D;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;


public class NewlmsLogin extends Application {
    
    public static Stage window;
    
    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        Parent loginPage = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene login = new Scene(loginPage);
        window.setScene(login);       
        window.show();
    }
    
    public static void changeScene(Scene newScene) {
//        window.setScene(newScene);
    }
    
    
    
    
   

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

  
