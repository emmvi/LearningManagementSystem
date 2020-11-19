package newlms;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import static newlms.NewlmsLogin.window;


public class AdminPagesController implements Initializable {
    
//    event handlers for add/edit/remove button in each row. Add button only applies to courses table
    EventHandler<ActionEvent> addStudentToCourseEvent = new EventHandler<ActionEvent>() { 
        public void handle(ActionEvent e) { 
            try {
                addStdentToCourse();
            } 
            catch (IOException ex) {  
            }
        } 
    };

    
    Connection conn;
    ResultSet rs;
    PreparedStatement pst;
    
    String user_ID;
    String[] coursesColumns = { "ID" , "Title", "Teacher", "NumOfStudents"};
    String[] teachersColumns = { "ID" , "Name", "Designation", "Salary"};
    String[] studentsColumns = { "ID" , "Name", "Program", "Batch"};
    private String selectedId;
    private Button selectedAddButton;
    
    @FXML
    private TableView<RowAdminTable> pageTable;

    @FXML
    private TableColumn<RowAdminTable, String> col1;
    @FXML
    private TableColumn<RowAdminTable, String> col2;
    @FXML
    private TableColumn<RowAdminTable, String> col3;
    @FXML
    private TableColumn<RowAdminTable, String> col4;
    @FXML
    private TableColumn<RowAdminTable, String> col5;
    
    
    @FXML
    private Button addButton;
    
    @FXML
    private Button editButton;

    
    @FXML
    private Text pageType;
    
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
                
        this.pageTable.setOnMouseClicked(selectEvent);
//        if(this.pageType.getText().equals("Courses")) {
        
        
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
            openAdminHome();
        }
        else {
            this.pageType.setText(type);

            if(type.equals("Courses")) {
                setCoursesTable();
            }
            else if (type.equals("Teachers")) {
                setTeachersTable();
            }
            else if (type.equals("Students")) {
                setStudentsTable();
            }

            this.selectedId = null;
        }
    }
    
    public void openAdminHome() throws IOException {
        FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
        Parent dashboardRoot = dashboardLoader.load();
        AdminDashboardController dashboardController = dashboardLoader.getController();
        dashboardController.setUserDetails(this.user_ID);
        dashboardController.setPageType(null);

        Scene dashboardScene = new Scene(dashboardRoot);
        window.setScene(dashboardScene);
        window.setMaximized(true);
    }
    
    public void setCoursesTable() {
//        Only for Courses
        col1.setText(this.coursesColumns[0]);
        col2.setText(this.coursesColumns[1]);
        col3.setText(this.coursesColumns[2]);
        col4.setText(this.coursesColumns[3]);
    
        setRows();
        col5.setVisible(true);
        
        pageTable.setItems(getCoursesTable());
    }
    
    public void setRows() {
        col1.setCellValueFactory(new PropertyValueFactory<>("col1")); 
        col2.setCellValueFactory(new PropertyValueFactory<>("col2"));
        col3.setCellValueFactory(new PropertyValueFactory<>("col3"));
        col4.setCellValueFactory(new PropertyValueFactory<>("col4"));
        
        col5.setCellValueFactory(new PropertyValueFactory<>("add"));
    }
    
    public ObservableList<RowAdminTable> getCoursesTable() {
        ArrayList<String> ids = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> numbers = new ArrayList<>();
        
        ObservableList<RowAdminTable> table = FXCollections.observableArrayList();
        conn = ConnectToDB.connect();
        
        String coursesQuery = "Select Course_ID, Title, Name from Course join Teacher using (Teacher_ID)";
        String numOfStudentsQuery = "Select count(*) as NumOfStudents from  Enrollment where Course_ID=?";
        
        try {
            pst = conn.prepareStatement(coursesQuery);
            rs = pst.executeQuery();
            
//            String id = 
      
            while(rs.next()) {
                ids.add(rs.getString("Course_ID"));
                titles.add(rs.getString("Title"));
                names.add(rs.getString("Name"));
            }
            
            
            for(int i=0; i<ids.size(); i++) {
                pst = conn.prepareStatement(numOfStudentsQuery);
                pst.setString(1, ids.get(i));
                rs = pst.executeQuery();
                numbers.add(rs.getString("NumOfStudents"));
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
        
        for(int i=0; i<ids.size(); i++) {
            table.add(new RowAdminTable(ids.get(i), titles.get(i), names.get(i), numbers.get(i)));
        }
        
        return table;
    }
    
    public void setTeachersTable() {
        col1.setText(this.teachersColumns[0]);
        col2.setText(this.teachersColumns[1]);
        col3.setText(this.teachersColumns[2]);
        col4.setText(this.teachersColumns[3]);
        
        setRows();
        col5.setVisible(false);
 
        
 
        pageTable.setItems(getTeachersTable());
    }
    
    public ObservableList<RowAdminTable> getTeachersTable() {
        
        ObservableList<RowAdminTable> table = FXCollections.observableArrayList();
        conn = ConnectToDB.connect();
        String teachersQuery = "Select Teacher_ID, Name, Designation, Salary from Teacher";
        
        try {
            pst = conn.prepareStatement(teachersQuery);
            rs = pst.executeQuery();
      
            while(rs.next()) {
                table.add(new RowAdminTable(rs.getString("Teacher_ID"), rs.getString("Name"), rs.getString("Designation"), rs.getString("Salary")));
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
        
        return table;
    }
    
    public void setStudentsTable() {
        col1.setText(this.studentsColumns[0]);
        col2.setText(this.studentsColumns[1]);
        col3.setText(this.studentsColumns[2]);
        col4.setText(this.studentsColumns[3]);
    
        setRows();
        col5.setVisible(false);

        pageTable.setItems(getStudentsTable());
    }

    private ObservableList<RowAdminTable> getStudentsTable() {
        ObservableList<RowAdminTable> table = FXCollections.observableArrayList();
        conn = ConnectToDB.connect();
        String studentsQuery = "select Student_ID, Name, Batch, Program from Student";
        
        try {
            pst = conn.prepareStatement(studentsQuery);
            rs = pst.executeQuery();
      
            while(rs.next()) {
                table.add(new RowAdminTable(rs.getString("Student_ID"), rs.getString("Name"), rs.getString("Program"), rs.getString("Batch")));
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
        
        return table;
    }
    
    
    @FXML
    void addData(ActionEvent event) throws IOException {
        if(this.pageType.getText().equals("Students")) { 
            openPopupForm("AddStudent.fxml", 0);
        }
        else if(this.pageType.getText().equals("Courses")) {
            openPopupForm("AddCourse.fxml", 0);
        }
        else if(this.pageType.getText().equals("Teachers")) {
            openPopupForm("AddTeacher.fxml", 0);
        }
    }
    
    public void addStdentToCourse() throws IOException {
        openPopupForm("AddStudentToCourse.fxml", 1);
    }
    
    @FXML
    public void editData() throws IOException {
        if(this.selectedId != null) {
            if(this.pageType.getText().equals("Students")) { 
            openPopupForm("EditStudent.fxml", 2);
            }
            else if(this.pageType.getText().equals("Courses")) {
                openPopupForm("EditCourse.fxml", 3);
            }
            else if(this.pageType.getText().equals("Teachers")) {
                openPopupForm("EditTeacher.fxml", 4);
            }
        }
        
    }
    
   

    public void openPopupForm(String filename, int controllerType) throws IOException{
        Stage popupWindow = new Stage();
        FXMLLoader popupLoader = new FXMLLoader(getClass().getResource(filename));
        Parent popupRoot = popupLoader.load();
        if(controllerType == 1) {
            AddStudentToCourseController controller = popupLoader.getController();
            System.out.println("ID PICKED IN POPUP FORM " + this.selectedId);
            controller.setCurrentID(this.selectedId);
        }
        else if(controllerType == 2) {
            EditStudentController controller = popupLoader.getController();
            System.out.println("ID PICKED IN POPUP FORM " + this.selectedId);
            controller.setCurrentID(this.selectedId);
        }
        else if(controllerType == 3) {
            EditCourseController controller = popupLoader.getController();
            System.out.println("ID PICKED IN POPUP FORM " + this.selectedId);
            controller.setCurrentID(this.selectedId);
        }
        else if(controllerType == 4) {
            EditTeacherController controller = popupLoader.getController();
            System.out.println("ID PICKED IN POPUP FORM " + this.selectedId);
            controller.setCurrentID(this.selectedId);
        }
        
        Scene popupScene = new Scene(popupRoot);
        popupWindow.setScene(popupScene);       
        popupWindow.show();

    }
    
    
    @FXML
    public void deleteData() {
        if(this.selectedId != null) {
            String page = this.pageType.getText();
            String id = this.selectedId;
            String table = null;
            if(page.equals("Courses")) table = "Course";
            else if(page.equals("Students")) table = "Student";
            else if(page.equals("Teachers")) table = "Teacher";        

            String deleteQuery = "Delete from " + table + " where " + table + "_ID=?";
            conn = ConnectToDB.connect();
            System.out.println("Deleting id: " + id);

            try {
                pst = conn.prepareStatement(deleteQuery);  

                pst.setString(1, id);
                pst.executeUpdate();       
                JOptionPane.showMessageDialog(null, table + " Deleted!");
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
    
    
    
    
    EventHandler<MouseEvent> selectEvent = new EventHandler<MouseEvent>() { 
        @Override
        public void handle(MouseEvent event) {
            if (event.getClickCount() > 0) {
                setSelectedId();
            }   
        } 
    };
    
    public void setSelectedId() {
        if (this.pageTable.getSelectionModel().getSelectedItem() != null) {
            RowAdminTable selectedRow = this.pageTable.getSelectionModel().getSelectedItem();
            this.selectedId = selectedRow.getCol1();
            if(this.pageType.getText().equals("Courses")) {
                this.selectedAddButton = selectedRow.getAdd(); 
                this.selectedAddButton.setOnAction(addStudentToCourseEvent);
            }
            
            System.out.println("ID SELECTED:" + selectedId);
        }
    }
    
        
}