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
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

    int user_ID;
    String userType;
    ArrayList<String> coursesNames = new ArrayList<>();
    String course_ID;
    String courseName;
    byte[] uploadedFileBytes;
    private Integer selectedId;
    
    @FXML
    private TableColumn<RowContent, String> idCol;
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
    @FXML
    private TextField subject;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.contentTable.setOnMouseClicked(selectEvent);
       
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
    void logoutEvent(ActionEvent event) throws IOException {
        System.out.println("LOGOUT EVENT INVOKED FROM DASHBOARD!");
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent loginRoot = loginLoader.load();
            
        Scene loginScene = new Scene(loginRoot);
        window.setScene(loginScene);
        //window.centerOnScreen();  
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
        System.out.println("Add clicked");
        if(subject.getText() != null) {
            addContent(subject.getText());
            refresh();
        }
        else {
            JOptionPane.showMessageDialog(null, "Please Provide Subject");   
        }
        
    }
    
    @FXML
    void download(ActionEvent event) {
        System.out.println("Download clicked");
        if(this.selectedId != null) {
            downloadFile();            
        }
        else {
            JOptionPane.showMessageDialog(null, "Please click the content you'd like to download!");
        }

    }
    
    
    public void setCredentials(int id, String type) throws FileNotFoundException {
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
            course1.setText(names.get(0));
            course2.setText(names.get(1));
            course3.setText(names.get(2));
            course4.setText(names.get(3));
            course5.setText(names.get(4));
        }
        
        else if(n == 4) {
            course1.setText(names.get(0));
            course2.setText(names.get(1));
            course3.setText(names.get(2));
            course4.setText(names.get(3));
            course5.setVisible(false);
        }
        else if(n == 3) {
            course1.setText(names.get(0));
            course2.setText(names.get(1));
            course3.setText(names.get(2));
            course4.setVisible(false);
            course5.setVisible(false);
        }
        else if(n == 2) {
            course1.setText(names.get(0));
            course2.setText(names.get(1));
            course3.setVisible(false);
            course4.setVisible(false);
            course5.setVisible(false);
        }
        else if(n == 1) {
            course1.setText(names.get(0));
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
        this.selectedId = null;
    }
    
    public void setContent(String type) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        contentTable.setItems(getContents(type));
    }
 
    public ObservableList<RowContent> getContents(String type) {
        
        ObservableList<RowContent> rows = FXCollections.observableArrayList();
        conn = ConnectToDB.connect();
        
        try {
            pst = conn.prepareCall("{call get_course_content(?, ?, ?, ?)}");
            pst.setInt(1, Integer.parseInt(this.course_ID));
            pst.setString(2, type);
            pst.setInt(3, this.user_ID);
            pst.setString(4, this.userType);
            pst.execute();
            rs = pst.getResultSet();

            while(rs.next()) {
                rows.add(new RowContent(rs.getString("Content_ID"), rs.getString("Content_Subject"), rs.getString("Author"), rs.getString("Date_Uploaded")));
            }

//            }
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
        return rows;
    }
    
    
    
   
    public void openDashboard(int val_id, String val_type) throws IOException {
        FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        Parent dashboardRoot = dashboardLoader.load();
        DashboardController dashboardController = dashboardLoader.getController();
        dashboardController.setUserDetails(val_id, val_type);

        Scene dashboardScene = new Scene(dashboardRoot);
        window.setScene(dashboardScene);        
        window.setMaximized(true);
    }
    
    
    
    
    
    public void addContent(String subject) {

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
                    
        try {
            if(this.userType.equals("Teacher")) {
                pst = conn.prepareCall("{call add_content_teacher(?,?,?,?,?,?)}");
            }
            else {
               pst = conn.prepareCall("{call add_content_student(?,?,?,?,?,?)}");
            }
            pst.setString(1, this.contentType.getText());
            pst.setInt(2, Integer.parseInt(this.course_ID));
            pst.setString(3, subject);
            pst.setBytes(4, this.uploadedFileBytes);
            pst.setInt(5, this.user_ID);
            pst.setString(6, this.userName.getText());
            pst.execute();
            
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
            pst = conn.prepareCall("{call download_content(?)}");
            pst.setInt(1,this.selectedId);
            pst.execute();
            rs = pst.getResultSet();
            while(rs.next())
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
             System.out.println("BYTES " + fileBytes);
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
        
        try {
            pst = conn.prepareCall("{call get_course_term(?)}");
            pst.setInt(1, Integer.parseInt(this.course_ID));
            pst.execute();
            rs = pst.getResultSet();
            while(rs.next()) {
                this.currentTerm.setText(rs.getString("Term"));
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
        
    }
    
//    Add mouseEvent if a person clicks a row
    EventHandler<MouseEvent> selectEvent = new EventHandler<MouseEvent>() { 
        @Override
        public void handle(MouseEvent event) {
            if (event.getClickCount() > 0) {
                setSelectedId();
            }   
        } 
    };
    
    public void setSelectedId() {
        if (this.contentTable.getSelectionModel().getSelectedItem() != null) {
            RowContent selectedRow = this.contentTable.getSelectionModel().getSelectedItem();
            this.selectedId = Integer.parseInt(selectedRow.getId());
            
            System.out.println("ID SELECTED:" + selectedId);
        }
    }
    
    public void refresh() {
        setContent(contentType.getText());
    }
}
