package newlms;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import javax.swing.JOptionPane;
import static newlms.NewlmsLogin.window;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class CoursesController implements Initializable {

    
    Connection conn;
    ResultSet rs;
    PreparedStatement pst;

    int user_ID;
    String userType;
    ArrayList<String> coursesNames = new ArrayList<>();
    
    @FXML
    private Text userName;
    @FXML
    private TableView<RowStudentTable> pageTable;
    @FXML
    private TableColumn<RowStudentTable, String> ID;
    @FXML
    private TableColumn<RowStudentTable, String> title;
    @FXML
    private TableColumn<RowStudentTable, String> teacher;
    @FXML
    private TableColumn<RowStudentTable, String> term;
    @FXML
    private TableColumn<RowStudentTable, String> numOfStudents;
    @FXML
    private ImageView userIcon;
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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void goToHome(ActionEvent event) throws IOException {
        openDashboard(this.user_ID, this.userType);
    }

    @FXML
    private void goToCourses(ActionEvent event) {
        
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
        System.out.println("courseClicked EVENT INVOKED FROM DASHBOARD!");
        String courseName = ((Button) event.getSource()).getText();
        String courseTitle = courseName.split("-", 2)[0];
        String courseID = courseName.split("-", 2)[1];
        System.out.println("TITLE: " + courseTitle);        
        
        openCourseContent(courseName, null);
    }
    
    
    
    
    public void openCourseContent(String courseName, String contentType) throws IOException {
        FXMLLoader courseLoader = new FXMLLoader(getClass().getResource("CourseContent.fxml"));
        Parent courseRoot = courseLoader.load();
        CourseContentController  courseController = courseLoader.getController();
        
        
        courseController.setCredentials(this.user_ID, this.userType);
        courseController.setUserName(this.userName.getText());
        courseController.setCoursesNames(this.coursesNames);
        courseController.setCurrentCourseDetails(courseName);
        courseController.setContentType(contentType);

        
        Scene courseScene = new Scene(courseRoot);
        window.setScene(courseScene);        
        window.setMaximized(true);
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

 
    void setCredentials(int id, String type) throws FileNotFoundException {
        this.user_ID = id;
        this.userType = type;
        
        if(userType.equals("Teacher")) {
            FileInputStream input = new FileInputStream("src\\newlms\\images\\teacher-icon2.png");
            Image image = new Image(input);
            this.userIcon.setImage(image);
        }
    }

    void setUserName(String name) {
        userName.setText(name);
    }

    void setCoursesNames(ArrayList<String> names) {
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
        
        setContentTable();
    }
    
    public void setContentTable() {
        ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        teacher.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        term.setCellValueFactory(new PropertyValueFactory<>("term"));
        numOfStudents.setCellValueFactory(new PropertyValueFactory<>("numOfStudents"));
        pageTable.setItems(getContents());
    }
    
    public ObservableList<RowStudentTable> getContents() {
        
        ObservableList<RowStudentTable> announcements = FXCollections.observableArrayList();
        conn = ConnectToDB.connect();
        
        try {
            for(int i=0; i<this.coursesNames.size(); i++) {
                pst = conn.prepareCall("{call get_course_details(?)}");
                int id = Integer.parseInt(coursesNames.get(i).split("-", 2)[1]);
                pst.setInt(1, id);
                pst.execute();
                rs = pst.getResultSet();
                while(rs.next()) {
                    announcements.add(new RowStudentTable(rs.getString("Course_ID"), rs.getString("Title"), rs.getString("Name"), rs.getString("Term"), rs.getString("NumOfStudents")));
                }
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
    
}
