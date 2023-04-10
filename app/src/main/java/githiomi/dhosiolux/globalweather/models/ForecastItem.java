package githiomi.dhosiolux.globalweather.models;

public class ForecastItem {

    // Instance Variables
    String id;
    String icon;
    String forecast;

    // Constructor
    public ForecastItem(String id, String icon, String forecast) {
        this.id = id;
        this.icon = icon;
        this.forecast = forecast;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getForecast() {
        return forecast;
    }

    public void setForecast(String forecast) {
        this.forecast = forecast;
    }
}
