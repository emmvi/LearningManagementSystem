package newlms;

import java.sql.*;
import javax.swing.JOptionPane;

public class ConnectToDB {
    Connection connection;

    public static Connection connect() {
        System.out.println("ISNIDE CONNECT METHOD");
        String username = "root";
        String pw = "1234";
        String url = "jdbc:mysql://localhost:3306/lmsdb";
        try {
            Connection connection = DriverManager.getConnection(url, username, pw);
            System.out.println("CONNECTEDQ!");
            return connection;
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);   
            return null;
        }
    }
}
