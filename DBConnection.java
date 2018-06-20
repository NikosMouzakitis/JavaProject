package ent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {

    public Connection con;

    public DBConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            String msg = "The com.mysql.jdbc.Driver is missing\n"
                    + "install and rerun the application";
            System.out.println(msg);
            System.exit(1);
        }

        // connect to db
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/schooldb?useSSL=false", "root", "");

            System.out.println("Test connection OK\n");
        } catch (SQLException e) {
            String msg = "Error: " + e.getMessage();
            System.out.println(msg);
            System.exit(1);
        }
    }

    public ResultSet Query(String sql) {
        ResultSet retval = null;

        try {
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            retval = stmt.executeQuery(sql);
        } catch (SQLException e) {
            String msg = "Error: " + e.getMessage();
            System.out.println(msg);
        }

        return retval;
    }

    public int Update(String sql) {
        int retval = 0;
        try {
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            retval = stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return retval;
    }
}
