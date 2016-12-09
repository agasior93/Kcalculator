package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by Maciek on 29.11.2016.
 */
public class mainViewController extends RegisterController implements Initializable {

    LocalDate date = LocalDate.now();
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMdd");


    @FXML
    private Label dateLbl = new Label();

    @FXML
    private Label userLbl = new Label();

    @FXML
    private TableColumn<ProductData, String> colProduct;

    @FXML
    private TableColumn<ProductData, String> colKcal;

    @FXML
    private TableColumn<ProductData, String> colProtein;

    @FXML
    private TableColumn<ProductData, String> colCarbs;

    @FXML
    private TableColumn<ProductData, String> colFat;

    @FXML
    private TableColumn<ProductData, String> colProduct2;

    @FXML
    private TableColumn<ProductData, String> colKcal2;

    @FXML
    private TableColumn<ProductData, String> colProtein2;

    @FXML
    private TableColumn<ProductData, String> colCarbs2;

    @FXML
    private TableColumn<ProductData, String> colFat2;

    @FXML
    private TableView<ProductData> tableProduct;
    private ObservableList<ProductData> products;


    @FXML
    private TableView<ProductData> tableProduct2;
    private ObservableList<ProductData> products2;
    private ArrayList<ProductSimpleData> productlist = new ArrayList<>();

    @FXML
    private ComboBox<String> choiceBox;
    private ObservableList<String> types;

    @FXML
    private Label kcalLabel = new Label();

    @FXML
    private Label proteinLabel = new Label();

    @FXML
    private Label carbLabel = new Label();

    @FXML
    private Label fatLabel = new Label();

    @FXML
    private TextField weightField = new TextField();

    @FXML
    private TextField searchField = new TextField();

    @FXML
    private Label tdeeLabel = new Label();

    @FXML
    private Label warningLabel = new Label();

    @FXML
    private Button loadBtn = new Button();


    protected String username;
    protected int userID;
    protected float tdee;
    protected String filename;
    protected String filelist;

    Connection conn = new SqlConnection().Connector();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadTypefromDB();
        loadDataFromDB();
        dateLbl.setText("Produkty spożyte w dniu " + date);
        tableProduct.setPlaceholder(new Label("Nie znaleziono szukanego produktu"));
        tableProduct2.setPlaceholder(new Label("Brak zjedzonych posiłków"));

        colProduct.setCellValueFactory(new PropertyValueFactory<ProductData, String>("name"));
        colKcal.setCellValueFactory(new PropertyValueFactory<ProductData, String>("kcal"));
        colProtein.setCellValueFactory(new PropertyValueFactory<ProductData, String>("protein"));
        colCarbs.setCellValueFactory(new PropertyValueFactory<ProductData, String>("carb"));
        colFat.setCellValueFactory(new PropertyValueFactory<ProductData, String>("fat"));

        colProduct2.setCellValueFactory(new PropertyValueFactory<ProductData, String>("name"));
        colKcal2.setCellValueFactory(new PropertyValueFactory<ProductData, String>("kcal"));
        colProtein2.setCellValueFactory(new PropertyValueFactory<ProductData, String>("protein"));
        colCarbs2.setCellValueFactory(new PropertyValueFactory<ProductData, String>("carb"));
        colFat2.setCellValueFactory(new PropertyValueFactory<ProductData, String>("fat"));

        loadBtn.setOnAction(e -> {
                try {
                    loadDataFromFile();

                } catch(Exception f) {
                    f.printStackTrace();
                }

        });

    }

    public void kcalc(ActionEvent event){
        try {
            ((Node)event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("kcalkulatorView.fxml").openStream());

            kcalkulatorViewController hello = loader.<kcalkulatorViewController>getController();
            hello.getUserData(username);

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Obliczanie zapotrzebowania");
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void search()    {
        products = FXCollections.observableArrayList();
        if(searchField.getText().equals("")){
            loadDataFromDB();
        } else {
            try {
                PreparedStatement pst = conn.prepareStatement("SELECT name, kcal, protein, carb, fat FROM Products WHERE name=?");
                pst.setString(1, searchField.getText());
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    products.add(new ProductData(rs.getString("name"), rs.getString("kcal"), rs.getString("protein"), rs.getString("carb"), rs.getString("fat")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        tableProduct.setItems(null);
        tableProduct.setItems(products);
    }

    public void updateSummary(){
        Float valKcal = Float.valueOf(0);
        Float valProtein = Float.valueOf(0);
        Float valCarb = Float.valueOf(0);
        Float valFat = Float.valueOf(0);
            for ( int i = 0; i < tableProduct2.getItems().size() ; i++ ){
                valKcal += Float.parseFloat(colKcal2.getCellData(i));
                valProtein += Float.parseFloat(colProtein2.getCellData(i));
                valCarb += Float.parseFloat(colCarbs2.getCellData(i));
                valFat += Float.parseFloat(colFat2.getCellData(i));
            }
            kcalLabel.setText(valKcal.toString());
            proteinLabel.setText(valProtein.toString());
            carbLabel.setText(valCarb.toString());
            fatLabel.setText(valFat.toString());

            if(Float.parseFloat(kcalLabel.getText()) > tdee && tdee!=0){
                warningLabel.setText("Przekorczyłeś dzienne zapotrzebowanie kaloryczne!");
            }
            else {
                warningLabel.setText("");
            }

        }

    public void loadDataFromFile() throws Exception{
        File file = new File(filename);
        if(file.exists() == false){
            file.createNewFile();
        }
        tableProduct2.getColumns().clear();
        Collection<ProductData> list = Files.readAllLines(file.toPath()).stream().map(line -> {
            String[] details = line.split(",");
            ProductData pd = new ProductData();
            pd.setName(details[0]);
            pd.setKcal(details[1]);
            pd.setProtein(details[2]);
            pd.setCarb(details[3]);
            pd.setFat(details[4]);
            return pd;

        })
                .collect(Collectors.toList());
        products2 = FXCollections.observableArrayList(list);

        tableProduct2.getColumns().addAll(colProduct2, colKcal2, colProtein2, colCarbs2, colFat2);
        colProduct2.setCellValueFactory(data -> data.getValue().nameProperty());
        colKcal2.setCellValueFactory(data -> data.getValue().kcalProperty());
        colProtein2.setCellValueFactory(data -> data.getValue().proteinProperty());
        colCarbs2.setCellValueFactory(data -> data.getValue().carbProperty());
        colFat2.setCellValueFactory(data -> data.getValue().fatProperty());

        tableProduct2.setItems(products2);
        for(ProductData pd : products2){
            productlist.add(new ProductSimpleData(pd.getName(), pd.getKcal(), pd.getProtein(), pd.getCarb(), pd.getFat()));
        }
        updateSummary();
    }

    public void save() throws IOException {
        File file = new File(filename);
        if(file.exists() == false){
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        try{
            for (ProductSimpleData pd : productlist) {
                bw.write(pd.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bw.flush();
            bw.close();
        }
    }

    public void addMeal() {

        ProductData selection = tableProduct.getSelectionModel().getSelectedItem();
        if (selection != null) {
            float weight = Float.parseFloat(weightField.getText());
            float weight100 = weight / 100;

            String calories = String.valueOf(Float.parseFloat(selection.getKcal()) * weight100);
            String protein = String.valueOf(Float.parseFloat(selection.getProtein()) * weight100);
            String carb = String.valueOf(Float.parseFloat(selection.getCarb()) * weight100);
            String fat = String.valueOf(Float.parseFloat(selection.getFat()) * weight100);

            ProductData product = new ProductData(
                    selection.getName() + "(" + weight + "g)",
                    calories,
                    protein,
                    carb,
                    fat);
            productlist.add(new ProductSimpleData(product.getName(), calories, protein, carb, fat));
            tableProduct2.getItems().add(product);
        }
        updateSummary();
        try{
            save();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void deleteMeal() {

        int selectedIndex = tableProduct2.getSelectionModel().getSelectedIndex();

        if(selectedIndex >= 0){
            tableProduct2.getItems().remove(selectedIndex);
            productlist.remove(selectedIndex);

        }
        updateSummary();
        try{
            save();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void loadTypeDataFromDB(String type){
        products = FXCollections.observableArrayList();
              try {
            PreparedStatement pst =  conn.prepareStatement("SELECT name, kcal, protein, carb, fat FROM Products WHERE type=?");
            pst.setString(1, type);
            ResultSet rs = pst.executeQuery();
                  while(rs.next()){
                products.add(new ProductData(rs.getString("name"), rs.getString("kcal"), rs.getString("protein"), rs.getString("carb"), rs.getString("fat")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e);
        }

        tableProduct.setItems(null);
        tableProduct.setItems(products);
    }

    private void loadDataFromDB(){
        products = FXCollections.observableArrayList();
        try {
            ResultSet rs =  conn.createStatement().executeQuery("SELECT name, kcal, protein, carb, fat FROM Products");
            while(rs.next()){
                products.add(new ProductData(rs.getString("name"), rs.getString("kcal"), rs.getString("protein"), rs.getString("carb"), rs.getString("fat")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableProduct.setItems(null);
        tableProduct.setItems(products);
    }

    public void loadTypefromDB (){
        types = FXCollections.observableArrayList();
        try{
            ResultSet rs = conn.createStatement().executeQuery("SELECT type FROM Products");
            while(rs.next()){
                String product = rs.getString("type");
                if(!types.contains(product)){
                    types.add(product);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        choiceBox.setItems(types);
        choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                loadTypeDataFromDB(newValue.toString());
            }
        });
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
        if(tdee != 0) {
            tdeeLabel.setText(String.valueOf("Twoje dzienne zapotrzebowanie kaloryczne wynosi " + tdee + " kcal."));
        }
        username = user;
        filename = String.valueOf("uid" + userID + "date" + simpleDate.format(new Date())+".txt");
        filelist = String.valueOf("uid" + userID);
        userLbl.setText("Witaj " + username + "!");

    }

    public void createUserFile() throws IOException{
        File file = new File("test.txt");
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        FileReader fr =  new FileReader("test.txt");
        BufferedReader br = new BufferedReader(fr);
        Scanner scan = new Scanner("test.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            while(scan.hasNextLine()){
                if(filename.contains(scan.nextLine())){
                    System.out.println(filename);
                    System.out.println("znalazlo");

                }else{
                    System.out.println(filename);
                    System.out.println("nie znalazlo");

                }

            }

        }catch(Exception e){
            e.printStackTrace();
        } finally {
            bw.flush();
            bw.close();
            scan.close();
        }
    }

}

