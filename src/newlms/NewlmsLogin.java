package newlms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

  
