/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlms;


import java.awt.Image;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import static newlms.NewlmsLogin.window;

/**
 *
 * @author Dell
 */
public class RowAdminTable {
    private String col1, col2, col3, col4;
    private Button add;
    
    EventHandler<ActionEvent> addEvent = new EventHandler<ActionEvent>() { 
        public void handle(ActionEvent e) { 
            System.out.println("ADD CLICKED");     
        } 
    };
    
    EventHandler<ActionEvent> editEvent = new EventHandler<ActionEvent>() { 
        public void handle(ActionEvent e) { 
            System.out.println("EDIT CLICKED");        
        } 
    };
    
    EventHandler<ActionEvent> deleteEvent = new EventHandler<ActionEvent>() { 
        public void handle(ActionEvent e) { 
            System.out.println("DELETE CLICKED");
        } 
    };
    
   

    public RowAdminTable(String col1, String col2, String col3, String col4) {
        this.col1 = col1;
        this.col2 = col2;
        this.col3 = col3;
        this.col4 = col4;
        add = new Button("Add Student");
        add.setOnAction(addEvent);
        
    }

    public String getCol1() {
        return col1;
    }

    public String getCol2() {
        return col2;
    }

    public String getCol3() {
        return col3;
    }

    public String getCol4() {
        return col4;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
    }

    public void setCol2(String col2) {
        this.col2 = col2;
    }

    public void setCol3(String col3) {
        this.col3 = col3;
    }

    public void setCol4(String col4) {
        this.col4 = col4;
    }

    public Button getAdd() {
        return add;
    }

    

    public void setAdd(Button add) {
        this.add = add;
    }

   
   

          

    
}
