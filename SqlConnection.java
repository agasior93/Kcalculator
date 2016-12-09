package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SqlConnection {

    public static Connection Connector(){
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Products.sqlite");
            return conn;
                } catch (Exception e) {
                System.out.println(e);
                return null;
                }
            }
}
