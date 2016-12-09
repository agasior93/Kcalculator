package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable{

    public LoginModel loginModel = new LoginModel();

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(!loginModel.isDbConnected()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Bląd połączenia.");
            alert.setHeaderText(null);
            alert.setContentText("Nie udało się nawiązać połączenia z bazą danych.");
            alert.showAndWait();
        }
    }

    public void enterRegister(ActionEvent event){
        ((Node)event.getSource()).getScene().getWindow().hide();
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        Pane root = null;
        try {
            root = loader.load(getClass().getResource("register.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Rejestracja nowego uzytkownika");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void login(ActionEvent event){
        try{
            if(loginModel.isLogin(txtUsername.getText(), txtPassword.getText())){
                try {
                    ((Node)event.getSource()).getScene().getWindow().hide();
                    Stage primaryStage = new Stage();
                    FXMLLoader loader = new FXMLLoader();
                    Pane root = loader.load(getClass().getResource("mainView.fxml").openStream());
                    primaryStage.setScene(new Scene(root));

                    mainViewController sendHello = loader.<mainViewController>getController();
                    sendHello.getUserData(txtUsername.getText());
                    sendHello.loadDataFromFile();
                    sendHello.createUserFile();

                    primaryStage.setTitle("Kcalculator");

                    primaryStage.setResizable(false);
                    primaryStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception f){
                    f.printStackTrace();
                }

            } else{
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Logowanie nieudane.");
                alert1.setHeaderText(null);
                alert1.setContentText("Wprowadzono złą nazwe użytkownika i/lub hasło.");
                alert1.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
