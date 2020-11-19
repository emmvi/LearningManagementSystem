/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlms;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static newlms.NewlmsLogin.window;

/**
 * FXML Controller class
 *
 * @author MARYAM
 */
public class CourseContentController implements Initializable {

    Connection conn;
    ResultSet rs;
    PreparedStatement pst;

    String user_ID;
    String userType;
    ArrayList<String> coursesNames = new ArrayList<>();
    String course_ID;
    String courseName;
    byte[] uploadedFileBytes;
    
    
    @FXML
    private TableColumn<RowContent, String> subjectCol;
    @FXML
    private TableColumn<RowContent, String> authorCol;
    @FXML
    private TableColumn<RowContent, String> dateCol;
    @FXML
    private TableView<RowContent> contentTable;
    
    
    @FXML
    private Text userName;
    @FXML
    private Button course1;
    @FXML
    private Button course2;
    @FXML
    private Button course3;
    @FXML
    private Button course4;
    @FXML
    private Button course5;
    @FXML
    private Text currentCourse;
    @FXML
    private Text currentID;
    @FXML
    private Text currentTerm;
    @FXML
    private Text contentType;
    @FXML
    private ImageView userIcon;
    @FXML
    private Button addButton;
    @FXML
    private Button downloadButton;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       
    }    
    @FXML
    void goToHome(ActionEvent event) throws IOException {
        openDashboard(this.user_ID, this.userType);
    }
    @FXML
    void goToCourses(ActionEvent event) throws IOException {
        FXMLLoader coursesLoader = new FXMLLoader(getClass().getResource("Courses.fxml"));
        Parent coursesRoot = coursesLoader.load();
        CoursesController coursesController = coursesLoader.getController();
        coursesController.setCredentials(this.user_ID, this.userType);
        coursesController.setUserName(this.userName.getText());
        coursesController.setCoursesNames(this.coursesNames);

        Scene dashboardScene = new Scene(coursesRoot);
        window.setScene(dashboardScene);        
        window.setMaximized(true);
    }
    @FXML
    void goToCalendar(ActionEvent event) {

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

    @FXML
    private void courseClicked(ActionEvent event) throws IOException {
    
        System.out.println("courseClicked EVENT INVOKED FROM ContentController!");
        String courseName = ((Button) event.getSource()).getText();
        String courseID = courseName.split("-", 2)[1];
        
        setCurrentCourseDetails(courseName);
        setContentType(null);
    }
    
    
    
    @FXML
    void goToContent(ActionEvent event) throws IOException { 
        Button content = (Button) event.getSource();   
        String type = content.getText();
        System.out.println("Type: " + type);
        
        System.out.println(this.courseName);
        System.out.println(this.courseName.split("-", 2));
        setCurrentCourseDetails(this.courseName);
        setContentType(type);
    }
    
    @FXML
    void add(ActionEvent event) {
        System.out.print("Add clicked");
        addContent();
    }
    
    @FXML
    void download(ActionEvent event) {
        System.out.print("Download clicked");
        downloadFile();
    }
    
    
    public void setCredentials(String id, String type) throws FileNotFoundException {
        this.user_ID = id;
        this.userType = type;
        
        if(userType.equals("Teacher")) {
            FileInputStream input = new FileInputStream("src\\newlms\\images\\teacher-icon2.png");
            Image image = new Image(input);
            this.userIcon.setImage(image);
        }
        
        
        
        
    }
    
    public void setUserName(String name) {
        userName.setText(name);
    }

//    Add the 5 most recent coursesNames for a user. 
//    If less than 5 coursesNames exist, delete the extra buttons.
    public void setCoursesNames(ArrayList<String> names) {
        
        this.coursesNames = names;
        int n = names.size();
        System.out.println(n);
        
        if (n >= 5) {
            course1.setText(names.get(n-1));
            course2.setText(names.get(n-2));
            course3.setText(names.get(n-3));
            course4.setText(names.get(n-4));
            course5.setText(names.get(n-5));
        }
        
        else if(n == 4) {
            course1.setText(names.get(n-1));
            course2.setText(names.get(n-2));
            course3.setText(names.get(n-3));
            course4.setText(names.get(n-4));
            course5.setVisible(false);
        }
        else if(n == 3) {
            course1.setText(names.get(n-1));
            course2.setText(names.get(n-2));
            course3.setText(names.get(n-3));
            course4.setVisible(false);
            course5.setVisible(false);
        }
        else if(n == 2) {
            course1.setText(names.get(n-1));
            course2.setText(names.get(n-2));
            course3.setVisible(false);
            course4.setVisible(false);
            course5.setVisible(false);
        }
        else if(n == 1) {
            course1.setText(names.get(n-1));
            course2.setVisible(false);
            course3.setVisible(false);
            course4.setVisible(false);
            course5.setVisible(false);
        }
        else {
            course1.setVisible(false);
            course2.setVisible(false);
            course3.setVisible(false);
            course4.setVisible(false);
            course5.setVisible(false);
        }
    }
    

    public void setCurrentCourseDetails(String name) {
        this.courseName = name;
        this.course_ID = name.split("-", 2)[1];
        
        currentCourse.setText(name);
        
        System.out.println("ID: " + this.course_ID);
        currentID.setText("ID: " + this.course_ID);
        getCurrentTerm(this.course_ID);
    }
    

    public void setContentType(String type) {
        System.out.println("Check type null: " + type);
        if(type == null ) type = "Announcements";
        System.out.println("contentType B4: " + contentType.getText());
        contentType.setText(type);
        System.out.println("type: " + type);
        System.out.println("contentType After: " + contentType.getText());
        setContent(type);
        
        if(this.userType.equals("Teacher") || type.equals("Dropbox")) {
            addButton.setVisible(true);
        }
        else {
            addButton.setVisible(false);
        }
    }
    
    public void setContent(String type) {
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        contentTable.setItems(getContents(type));
    }
    
//    public ObservableList<RowContent> getContentsTeacher(String type) {
//        
//        return null;
//    }
    
//    public ObservableList<RowContent> getContentsStudent(String type) {
//        ObservableList<RowContent> announcements = FXCollections.observableArrayList();
//        conn = ConnectToDB.connect();
//        
//        String contentQuery = null;
//        if(type.equals("Dropbox")) {
//            contentQuery = "?";
//        }
//        else {
//            contentQuery = "Select Subject, Date_Modified, Name as Author from Content join Teacher using (Teacher_ID) where Course_ID=10";
//        }
//        try {
//            
//            pst = conn.prepareStatement(contentQuery);
//            pst.setString(1, course_ID);
//            if(type.equals("Dropbox") && this.userType.equals("Student")) {
//                pst.setString(2, this.user_ID);
//            }
//            else {
//                pst.setString(2, type); 
//            }
//            rs = pst.executeQuery();
//            while(rs.next()) {
//                announcements.add(new RowContent(rs.getString("Subject"), rs.getString("Author"), rs.getString("Date_Modified")));
//            }
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, e);
//        }
//        finally {
//          try{
//              rs.close();
//              pst.close();
//              conn.close();
//          }  
//          catch(Exception e) {      
//          }
//        }
//        return announcements;
//        
//    }
 
    public ObservableList<RowContent> getContents(String type) {
        
        ObservableList<RowContent> announcements = FXCollections.observableArrayList();
        conn = ConnectToDB.connect();
        String contentQuery = "Select Subject, Date_Modified, Teacher_ID as id from Content where Course_ID=? and Content_Type=?";
        if(type.equals("Dropbox")) {
//            contentQuery = "Select Subject, Date_Modified, Name as Author from Content join Student using (Student_ID) where Course_ID=? and Student_ID=?";
        }
        try {
            
            pst = conn.prepareStatement(contentQuery);
            pst.setString(1, course_ID);
            if(type.equals("Dropbox") && this.userType.equals("Student")) {
                pst.setString(2, this.user_ID);
            }
            else {
                pst.setString(2, type); 
            }
            rs = pst.executeQuery();
            while(rs.next()) {
                announcements.add(new RowContent(rs.getString("Subject"), this.userName.getText(), rs.getString("Date_Modified")));
            }
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
        return announcements;
    }
    
    
    
   
    public void openDashboard(String val_id, String val_type) throws IOException {
        FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        Parent dashboardRoot = dashboardLoader.load();
        DashboardController dashboardController = dashboardLoader.getController();
        dashboardController.setUserDetails(val_id, val_type);

        Scene dashboardScene = new Scene(dashboardRoot);
        window.setScene(dashboardScene);        
        window.setMaximized(true);
    }
    
    
    
    
    
    public void addContent() {

        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File file =  chooser.getSelectedFile();
        String filename = file.getAbsolutePath();


        try {
            File newFile = new File(filename);
            FileInputStream fis = new FileInputStream(newFile);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int readNum; (readNum=fis.read(buf))!=-1;) {
                bos.write(buf, 0, readNum);
            }
            this.uploadedFileBytes = bos.toByteArray();

        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }


        conn = ConnectToDB.connect();
            String queryAddContent = "Insert into content (Content_ID, Content_Type, Course_ID, subject, File_Uploaded, Author) values (?,?,?,?,?,?)";
//        String queryAddContent = "UPDATE Content SET File_Uploaded=? WHERE Content_ID=5";

        try {
            pst = conn.prepareStatement(queryAddContent);
            System.out.println("Bytes Value: " + this.uploadedFileBytes);
                pst.setString(1, "113");
                pst.setString(2, "Dropbox");
                pst.setString(3, "10");
                pst.setString(4, "Trying with updated styling");
                pst.setBytes(5, this.uploadedFileBytes);
                pst.setString(5, this.user_ID);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "WOHOOOOOO! FILE UPLOADED");



        }
        catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e);
        }
        finally {
            try{
    //              rs.close();
                pst.close();
                conn.close();
            }  
            catch(Exception e) {
                
            }
        }   
    }
        
    
    
    
    void downloadFile() {
        byte[] fileBytes = null;
        conn = ConnectToDB.connect();
        String getFile = "Select File_Uploaded from Content where Content_ID=?";
        
        try {
            pst = conn.prepareStatement(getFile);
            pst.setString(1, "113");
            rs = pst.executeQuery();
            fileBytes = rs.getBytes("File_Uploaded");    
            
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
        
        JFileChooser chooser = new JFileChooser();
        chooser.showSaveDialog(null);
        
        File file =  chooser.getSelectedFile();
        String filename = file.getAbsolutePath();
        
        try {
            FileOutputStream fos = new FileOutputStream(file);
             
            // Writes bytes from the specified byte array to this file output stream 
            fos.write(fileBytes);
            JOptionPane.showMessageDialog(null, "WOHOOOOOO! FILE SAVED");
            
        }
        catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    
    public void getCurrentTerm(String id) {
        conn = ConnectToDB.connect();
        String contentQuery = "Select Term from Course where Course_ID=?";
        
        try {
            
            pst = conn.prepareStatement(contentQuery);
            pst.setString(1, course_ID);
            rs = pst.executeQuery();
            this.currentTerm.setText(rs.getString("Term"));
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
}
