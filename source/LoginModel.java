package sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Maciek on 29.11.2016.
 */
public class LoginModel {

    Connection connection;

    public LoginModel(){
        connection = SqlConnection.Connector();
        if(connection == null){
            System.exit(0);
        }
    }

    public boolean isDbConnected(){
        try{
            return !connection.isClosed();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean isLogin(String user, String pass) throws SQLException{
        PreparedStatement pst = null;
        ResultSet rs = null;
        String query = "SELECT * FROM Users WHERE username=? AND password=?";
                try{
                    pst = connection.prepareStatement(query);
                    pst.setString(1, user);
                    pst.setString(2, pass);
                    rs = pst.executeQuery();
                    if (rs.next()){
                        return true;
                    } else{
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                } finally {
                    if (pst != null) {
                        pst.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                }

    }
}
