package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by Maciek on 29.11.2016.
 */
public class RegisterController extends RegisterModel implements Initializable{

    @FXML
    private TextField txtNewUserName = new TextField();

    @FXML
    private TextField txtNewPassword = new TextField();

    @FXML
    private TextField txtReType = new TextField();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void registerBtn(ActionEvent event){
        try {
                if(txtNewUserName.getText().equals("") || txtNewPassword.getText().equals("") || txtReType.equals("")){
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Wprowadź wszystkie dane.");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Uzupełnij wszystkie wymagane pola.");
                    alert1.showAndWait();
                } else {
                    if(txtNewPassword.getText().equals(txtReType.getText())) {
                        if(register(txtNewUserName.getText(), txtNewPassword.getText())) {
                            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                            alert2.setTitle("Rejestracja zakończona.");
                            alert2.setHeaderText(null);
                            alert2.setContentText("Użytkownik " + txtNewUserName.getText() + " został utworzony pomyślnie. Możesz się teraz zalogować.");
                            alert2.showAndWait();
                        }
                    }
                    else{
                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                        alert2.setTitle("Niepoprawne hasło");
                        alert2.setHeaderText(null);
                        alert2.setContentText("Wpisane hasła nie są takie same! Spróbuj ponownie.");
                        alert2.showAndWait();
                    }
                }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Niepoprawne dane.");
            alert.setHeaderText(null);
            alert.setContentText("Upewnij się, ze wprowadziłęś poprawne dane.");
            alert.showAndWait();
            System.err.println(e);
        }
    }

    public void close(ActionEvent event){
        try {
            ((Node)event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("login.fxml").openStream());
            primaryStage.setTitle("Logowanie");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
