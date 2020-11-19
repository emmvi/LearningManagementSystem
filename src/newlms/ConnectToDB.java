package newlms;

import java.sql.*;
import javax.swing.JOptionPane;

public class ConnectToDB {
    Connection connection;
    
    public static Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:test_database.sqlite");
            return connection;
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);   
            return null;
        }
    }
}
