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

    public String getIcon() {
        return icon;
    }

    public String getForecast() {
        return forecast;
    }

}
