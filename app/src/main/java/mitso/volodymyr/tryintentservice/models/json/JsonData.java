package mitso.volodymyr.tryintentservice.models.json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class JsonData implements Serializable {

    private String                      date;

    @SerializedName("organizations")
    private List<JsonOrganization>      jsonOrganizations;

    private Map<Integer, String>        orgTypes;
    private Map<String, String>         currencies;
    private Map<String, String>         regions;
    private Map<String, String>         cities;

    @Override
    public String toString() {

        return "JsonData{" +
                ", date='" + date + '\'' +
                ", jsonOrganizations=" + jsonOrganizations +
                ", orgTypes=" + orgTypes +
                ", currencies=" + currencies +
                ", regions=" + regions +
                ", cities=" + cities +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<JsonOrganization> getJsonOrganizations() {
        return jsonOrganizations;
    }

    public void setJsonOrganizations(List<JsonOrganization> jsonOrganizations) {
        this.jsonOrganizations = jsonOrganizations;
    }

    public Map<Integer, String> getOrgTypes() {
        return orgTypes;
    }

    public void setOrgTypes(Map<Integer, String> orgTypes) {
        this.orgTypes = orgTypes;
    }

    public Map<String, String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Map<String, String> currencies) {
        this.currencies = currencies;
    }

    public Map<String, String> getRegions() {
        return regions;
    }

    public void setRegions(Map<String, String> regions) {
        this.regions = regions;
    }

    public Map<String, String> getCities() {
        return cities;
    }

    public void setCities(Map<String, String> cities) {
        this.cities = cities;
    }
}
