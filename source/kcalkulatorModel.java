package sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Maciek on 02.12.2016.
 */
public class kcalkulatorModel {

    Connection conn = new SqlConnection().Connector();

    public enum Sex {Mężczyzna, Kobieta}


    public float TDEE(int age, float height, float weight, Sex sex)
    {
        if(sex == Sex.Kobieta){
            return (float) ((9.99 * weight) + (6.25 * height) - (4.92 * age) - 161);
        }
        else {
            return (float) ((9.99 * weight) + (6.25 * height) - (4.92 * age) + 5);
        }
    }

    public float Activity(int sil, int aero)
    {
        return ((sil*500 + aero*350))/7;
    }

    public void updateTDEE(int userID, float tdee){
        try {
            PreparedStatement pst = conn.prepareStatement("UPDATE Users SET tdee=? WHERE id=?");
            pst.setString(1, String.valueOf(tdee));
            pst.setString(2, String.valueOf(userID));
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
