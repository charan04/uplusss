//$Id$
package uplus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class JDBCMySQLConnection {

	 private static JDBCMySQLConnection instance = new JDBCMySQLConnection();
	    public static final String URL = "jdbc:mysql://localhost:3306/jdbcdb";
	    public static final String USER = "root";
	    public static final String PASSWORD = "";
	    public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver"; 
	   public static Connection connection = null;
	    static 
	     {
	    	 
	     }
	    //private constructor
	    private JDBCMySQLConnection() {
	        try {
	            //Step 2: Load MySQL Java driver
	            Class.forName(DRIVER_CLASS);
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	    }
	     
	    protected Connection createConnection() {
	 
	        
	        try {
	            //Step 3: Establish Java MySQL connection
	            connection = DriverManager.getConnection(URL, USER, PASSWORD);
	        } catch (SQLException e) {
	            System.out.println("ERROR: Unable to Connect to Database.");
	        }
	        System.out.println("connection created ");
	        return connection;
	    }   
	     
	    public static Connection getConnection() {
	        return instance.createConnection();
	    }
}
