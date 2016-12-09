package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


/**
 * Created by Maciek on 02.12.2016.
 */
public class kcalkulatorViewController extends  mainViewController implements Initializable {

    kcalkulatorModel model = new kcalkulatorModel();

    @FXML
    private TextField ageField = new TextField();

    @FXML
    private TextField weightField = new TextField();

    @FXML
    private TextField heightField = new TextField();

    @FXML
    private ComboBox<kcalkulatorModel.Sex> sexCombo = new ComboBox<>();

    @FXML
    private ComboBox bodyCombo = new ComboBox();

    @FXML
    private ComboBox goalCombo = new ComboBox();

    @FXML
    private TextField silText = new TextField();

    @FXML
    private TextField aeroText = new TextField();

    private ObservableList<String> bodylist;
    private ObservableList<String> goallist;

    private float tdee;

    String body;
    String goal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        bodylist = FXCollections.observableArrayList();
        bodylist.add("Endomorfik");
        bodylist.add("Mezomorfik");
        bodylist.add("Ektomorfik");
        sexCombo.getItems().setAll(kcalkulatorModel.Sex.values());

        goallist = FXCollections.observableArrayList();
        goallist.add("Redukcja");
        goallist.add("Rekompozycja");
        goallist.add("Masa");

        bodyCombo.setItems(bodylist);
        bodyCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                body = newValue.toString();
            }
        });

        goalCombo.setItems(goallist);
        goalCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                goal = newValue.toString();
            }
        });
    }

    public void calculate()
    {
        try {
            float bmr = model.TDEE(Integer.parseInt(ageField.getText()), Float.parseFloat(heightField.getText()), Float.parseFloat(weightField.getText()), sexCombo.getValue());
            float tea = model.Activity(Integer.parseInt(silText.getText()), Integer.parseInt((aeroText.getText())));
            float epoc = Integer.parseInt(silText.getText()) * bmr * 6 / 100 + 35 * Integer.parseInt((aeroText.getText()));
            float tef = bmr * 7 / 100;
            float neat = 0;
            float final_tdee;
            if (body.equals("Endomorfik")) {
                neat = 300;
            }
            if (body.equals("Mezomorfik")) {
                neat = 500;
            }
            if (body.equals("Ektomorfik")) {
                neat = 800;
            }
            tdee = bmr + tea + epoc + tef + neat;
            if(goal.equals("Redukcja")){
                final_tdee = tdee - 300;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Obliczono zapotrzebowanie");
                alert.setHeaderText(null);
                alert.setContentText("Twoje dzienne zapotrzebowanie kaloryczne wynosi: " + tdee + "kcal." + "\nBy zredukowac wagę, powinieneś spożywać " + final_tdee + "kcal.");
                alert.showAndWait();
                tdee = final_tdee;
            }
            if(goal.equals("Rekompozycja")){
                final_tdee = tdee;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Obliczono zapotrzebowanie");
                alert.setHeaderText(null);
                alert.setContentText("Twoje dzienne zapotrzebowanie kaloryczne wynosi: " + tdee + "kcal." + "\nBy przeprowadzić rekompozycje sylwetki, powinieneś spożywać " + final_tdee + "kcal.");
                alert.showAndWait();
                tdee = final_tdee;
            }
            if(goal.equals("Masa")){
                final_tdee = tdee + 300;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Obliczono zapotrzebowanie");
                alert.setHeaderText(null);
                alert.setContentText("Twoje dzienne zapotrzebowanie kaloryczne wynosi: " + tdee + "kcal." + "\nBy budować masę, powinieneś spożywać " + final_tdee + "kcal.");
                alert.showAndWait();
                tdee = final_tdee;
            }

            model.updateTDEE(userID, tdee);


        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Niepoprawne dane.");
            alert.setHeaderText(null);
            alert.setContentText("Wprowadzono niepoprawne dane bądź pozostawiono nieuzupełnione pola.");
            alert.showAndWait();
        } catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Niepoprawne dane.");
            alert.setHeaderText(null);
            alert.setContentText("Wprowadzono niepoprawne dane bądź pozostawiono nieuzupełnione pola.");
            alert.showAndWait();
        }

    }

    public void close(ActionEvent event){
        try {
            ((Node)event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("mainView.fxml").openStream());
            primaryStage.setScene(new Scene(root));

            mainViewController sendHello = loader.<mainViewController>getController();
            sendHello.getUserData(username);

            primaryStage.setTitle("Kcalculator");

            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getUserData(String user){
        try {
            PreparedStatement pst = conn.prepareStatement("SELECT id, tdee FROM Users WHERE username =?");
            pst.setString(1, user);
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                userID = rs.getInt("id");
                tdee = rs.getFloat("tdee");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        username = user;
    }

}
