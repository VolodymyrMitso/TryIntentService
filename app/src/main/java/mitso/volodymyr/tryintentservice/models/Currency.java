package mitso.volodymyr.tryintentservice.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Currency implements Serializable {

    private String              name;
    private String              abbreviation;

    @SerializedName("bid")
    private double              purchase;

    @SerializedName("ask")
    private double              sale;

    @Override
    public String toString() {
        return "Currency{" +
                "name='" + name + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", purchase=" + purchase +
                ", sale=" + sale +
                '}';
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSale() {
        return sale;
    }

    public void setSale(double sale) {
        this.sale = sale;
    }

    public double getPurchase() {
        return purchase;
    }

    public void setPurchase(double purchase) {
        this.purchase = purchase;
    }
}
