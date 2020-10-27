package JavaFiles;


import java.sql.*;

/**
 * Establish connection to database and return it. Used in userQueries and ElectorQueries. Need to fix to connect to an sql server.
 */
public class DBConnection {


    public static Connection getConnection () throws SQLException {

        //return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT", "root", "root");

        //test remote connection
        // change depending on where running from
        return DriverManager.getConnection("jdbc:mysql://192.168.0.24:3306/mydb?serverTimezone=GMT", "testRemote2", "root");

        //return DriverManager.getConnection("jdbc:mysql://192.168.1.224:3306/mydb?serverTimezone=GMT", "testRemote2", "root");



    }
}
