package sample;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Maciek on 29.11.2016.
 */
public class RegisterModel {
    Connection connection = SqlConnection.Connector();


    public boolean register(String name, String pass) throws SQLException {
        PreparedStatement pst = null;

        String query = "INSERT INTO Users (username, password) VALUES (?,?)";
        try{
            pst = connection.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, pass);
           // pst.setString(3, email);
            pst.executeUpdate();
            return true;

        } catch (SQLException e) {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Nazwa użytkwnika zajęta");
            alert2.setHeaderText(null);
            alert2.setContentText("Użytkownik " + name + " już istnieje. Wybierz inną nazwe użytkownika.");
            alert2.showAndWait();

            System.err.println(e);

            return false;
        }  catch (Exception e) {
            System.err.print( e.getClass().getName() + ": " + e.getMessage());
            return false;
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }
}
