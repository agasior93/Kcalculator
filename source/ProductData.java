package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Maciek on 30.11.2016.
 */
public class ProductData {

    private StringProperty name = new SimpleStringProperty();
    private StringProperty kcal = new SimpleStringProperty();
    private StringProperty protein = new SimpleStringProperty();
    private StringProperty carb = new SimpleStringProperty();
    private StringProperty fat = new SimpleStringProperty();

    ProductData(){

    }

    ProductData(String name, String kcal, String protein, String carb, String fat) {
        this.name = new SimpleStringProperty(name);
        this.kcal = new SimpleStringProperty(kcal);
        this.protein = new SimpleStringProperty(protein);
        this.carb = new SimpleStringProperty(carb);
        this.fat = new SimpleStringProperty(fat);
    }

    @Override
    public String toString() {
        return name +","+ kcal +","+ protein +","+ carb +","+ fat;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getKcal() {
        return kcal.get();
    }

    public StringProperty kcalProperty() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal.set(kcal);
    }

    public String getProtein() {
        return protein.get();
    }

    public StringProperty proteinProperty() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein.set(protein);
    }

    public String getCarb() {
        return carb.get();
    }

    public StringProperty carbProperty() {
        return carb;
    }

    public void setCarb(String carb) {
        this.carb.set(carb);
    }

    public String getFat() {
        return fat.get();
    }

    public StringProperty fatProperty() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat.set(fat);
    }
}