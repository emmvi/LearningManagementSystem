package newlms;


import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javax.swing.JOptionPane;
import javafx.scene.image.ImageView;
import static newlms.NewlmsLogin.window;

public class DashboardController implements Initializable {

    Connection conn;
    ResultSet rs;
    PreparedStatement pst;

    int user_ID;
    String userType;
    
    ArrayList<String> courses = new ArrayList<>();
    ArrayList<String> coursesIDs = new ArrayList<>();
    ArrayList<String> coursesNames = new ArrayList<>();
    
    
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
    private ImageView userIcon;
   
    
    
    
    @FXML
    void goToHome(ActionEvent event) throws FileNotFoundException {
        
    }
    @FXML
    void goToCourses(ActionEvent event) throws FileNotFoundException, IOException {
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
        //window.centerOnScreen();  
    }
    
    @FXML
    void courseClicked(ActionEvent event) throws IOException {
        System.out.println("courseClicked EVENT INVOKED FROM DASHBOARD!");
        String courseName = ((Button) event.getSource()).getText();
        String courseTitle = courseName.split("-", 2)[0];
        String courseID = courseName.split("-", 2)[1];
        System.out.println("TITLE: " + courseTitle);        
        
        openCourseContent(courseName, null);
    }
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
    
    public void setUserDetails(int user_ID, String type) throws FileNotFoundException {
        this.user_ID = user_ID; 
        this.userType = type;
        
        if(userType.equals("Teacher")) {
            FileInputStream input = new FileInputStream("src\\newlms\\images\\teacher-icon2.png");
            Image image = new Image(input);
            this.userIcon.setImage(image);
        }
        
        conn = ConnectToDB.connect();        
        try {
            pst = conn.prepareCall("{call get_name(?, ?)}");
            pst.setInt(1, user_ID);
            pst.setString(2, type);
            pst.execute();
            rs = pst.getResultSet();
            while(rs.next())
                userName.setText(rs.getString("Name"));
            
            pst = conn.prepareCall("{call get_courses_for_user(?, ?)}");
            pst.setInt(1, user_ID);
            pst.setString(2, type);
            pst.execute();
            rs = pst.getResultSet();
            
            while(rs.next()) {
                String course_ID = rs.getString("Course_ID");
                String title = rs.getString("Title");
                this.coursesIDs.add(course_ID);
                this.courses.add(title);
                this.coursesNames.add(title + "-" + course_ID);
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
        
        setCourses();
        
    }
    
    //    Add the 5 most recent courses for a user. 
//    If less than 5 courses exist, delete the extra buttons.
    public void setCourses() {
        int n = coursesNames.size();
        System.out.println(n);
        
        if (n >= 5) {
            course1.setText(coursesNames.get(0));
            course2.setText(coursesNames.get(1));
            course3.setText(coursesNames.get(2));
            course4.setText(coursesNames.get(3));
            course5.setText(coursesNames.get(4));
        }
        
        else if(n == 4) {
            course1.setText(coursesNames.get(0));
            course2.setText(coursesNames.get(1));
            course3.setText(coursesNames.get(2));
            course4.setText(coursesNames.get(3));
            course5.setVisible(false);
        }
        else if(n == 3) {
            course1.setText(coursesNames.get(0));
            course2.setText(coursesNames.get(1));
            course3.setText(coursesNames.get(2));
            course4.setVisible(false);
            course5.setVisible(false);
        }
        else if(n == 2) {
            course1.setText(coursesNames.get(0));
            course2.setText(coursesNames.get(1));
            course3.setVisible(false);
            course4.setVisible(false);
            course5.setVisible(false);
        }
        else if(n == 1) {
            course1.setText(coursesNames.get(0));
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
}

