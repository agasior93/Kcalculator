package sample;

/**
 * Created by Maciek on 07.12.2016.
 */
public class ProductSimpleData {
    private String name;
    private String kcal;
    private String protein;
    private String carbs;
    private String fat;


    ProductSimpleData( String sname, String skcal, String sprotein, String scarbs, String sfat){
        name = sname;
        kcal = skcal;
        protein = sprotein;
        carbs = scarbs;
        fat = sfat;
    }

    @Override
    public String toString() {
        return name +","+ kcal +","+ protein +","+ carbs +","+ fat;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKcal() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }
}
