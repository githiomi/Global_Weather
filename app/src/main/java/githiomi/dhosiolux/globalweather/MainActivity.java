package githiomi.dhosiolux.globalweather;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import githiomi.dhosiolux.globalweather.adapters.ForecastAdapter;
import githiomi.dhosiolux.globalweather.databinding.ActivityMainBinding;
import githiomi.dhosiolux.globalweather.models.Constants;
import githiomi.dhosiolux.globalweather.models.ForecastItem;

public class MainActivity extends AppCompatActivity {

    // Views
    CardView mainContainer;
    ProgressBar progressBar;
    TextView fetching, errorMessage, date, town, weatherType, mainTemp, wind, humidity, temp;
    ImageView weatherImage;
    RecyclerView forecastRecycler;
    RelativeLayout relativeLayout;

    // User location variables
    LocationManager locationManager;

    // Local variables
    String cityName;
    List<ForecastItem> forecastItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Binding set up
        ActivityMainBinding mainActivityBinding = ActivityMainBinding.inflate(getLayoutInflater());
        // Update set content view
        setContentView(mainActivityBinding.getRoot());

        // Full screen flags
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Binding views
        progressBar = mainActivityBinding.PBFetch;
        fetching = mainActivityBinding.TVFetch;
        errorMessage = mainActivityBinding.TVError;
        mainContainer = mainActivityBinding.mainCardView;
        date = mainActivityBinding.TVDate;
        town = mainActivityBinding.TVTownName;
        weatherImage = mainActivityBinding.IVWeatherImage;
        weatherType = mainActivityBinding.TVWeatherType;
        mainTemp = mainActivityBinding.TVMainTemperature;
        wind = mainActivityBinding.TVWindSpeed;
        humidity = mainActivityBinding.TVHumidity;
        temp = mainActivityBinding.TVTemperature;
        forecastRecycler = mainActivityBinding.RVForecastView;
        relativeLayout = mainActivityBinding.RLBackground;

        // Init the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

//         Check for location permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Request permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.PERMISSION_CODE);
        }

        // But if location is available
        Location currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        // Get current city name
        cityName = getCityName(currentLocation.getLongitude(), currentLocation.getLatitude());

        // Use the city name to get weather information
        getWeatherData(cityName);

    }

    // To check if permission is granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions Granted...", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onRequestPermissionsResult: Permissions Granted");
            } else {
                Toast.makeText(this, "Permissions Not Granted...", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onRequestPermissionsResult: Permissions Not Granted");
            }
        }
    }

    /**
     * Called to get the name of the city that will then be used to call the API
     * @param longitude the longitude of the city
     * @param latitude the latitude of the city
     * @return the string name of the city
     */
    public String getCityName(double longitude, double latitude) {

        String city = ""; // Default city name

        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 3);

            for (Address address : addresses) {
                if (address != null) {
                    String cityName = address.getLocality();
                    if (cityName != null) {
                        city = cityName;
                    } else {
                        Toast.makeText(this, "User City NOT found! Try Again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        } catch (Exception e) {

            System.out.println("Could not find your city. Defaulting to Port Louis");
            city = "Port Louis";
            e.printStackTrace();

        }

        return city;
    }

    /**
     * To get the weather data from the API
     *
     * @param townName the town to get the weather data from
     */
    public void getWeatherData(String townName) {

        // Set city name TV to the town name
        town.setText(townName);

        String APIUrl = "https://api.weatherapi.com/v1/forecast.json?key=" + Constants.API_KEY +"%20&q=" + townName.trim() + "&days=6&aqi=no&alerts=no";

        // Request Queue
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, APIUrl, null, response -> {

            Toast.makeText(MainActivity.this, "Fetched data", Toast.LENGTH_SHORT).show();

            toggleView();

            // Clear arraylist of previous data
            forecastItemList = new ArrayList<>();

            // Extract Data
            try {

                // Current Weather Data
                String currentTime = response.getJSONObject("location").getString("localtime");
                String mainTemperature = response.getJSONObject("current").getString("temp_c");
                String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                String iconUrl = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                String windSpeed = response.getJSONObject("current").getInt("wind_kph") + "km/h";
                String humidityLevel = response.getJSONObject("current").getInt("humidity") + "%";
                String feelsLike = response.getJSONObject("current").getInt("feelslike_c") + "°C";

                // Load background image based on day/night
                int isDay = response.getJSONObject("current").getInt("is_day");

                if (isDay == 1) {
                    relativeLayout.setBackgroundResource(R.drawable.day_sky);
                } else {
                    relativeLayout.setBackgroundResource(R.drawable.night_sky);
                }

                // Add data to layout
                date.setText(getDate(currentTime));
                weatherType.setText(condition);

                String fullTemp = mainTemperature + "°C";
                mainTemp.setText(fullTemp);

                wind.setText(windSpeed);
                humidity.setText(humidityLevel);
                temp.setText(feelsLike);

                // Add icon to layout
                String fullIconUrl = "https:" + iconUrl;
                Picasso.get().load(fullIconUrl).into(weatherImage);

                // Get Forecast Data
                for (int i = 0; i < 5; i++) {

                    String day = Integer.toString(i + 1);

                    JSONArray forecastDay = response.getJSONObject("forecast").getJSONArray("forecastday");
                    String forecastIconUrl = forecastDay.getJSONObject(i).getJSONObject("day").getJSONObject("condition").getString("icon");
                    String forecastTemp = forecastDay.getJSONObject(i).getJSONObject("day").getInt("avgtemp_c") + "°C";

                    // Create new forecast items
                    forecastItemList.add(new ForecastItem(day, forecastIconUrl, forecastTemp));
                }

                // Set up adapter
                setUpAdapter();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

            toggleError();

            Toast.makeText(MainActivity.this, "Could not fetch data. Check your internet connection", Toast.LENGTH_SHORT).show();

        });

        // Add request to queue
        queue.add(jsonRequest);

    }

    /**
     * Method to convert API date to needed format
     *
     * @param date the date to convert from the API
     * @return newly formatted date -> e MMMM dd, yyyy
     */
    private static String getDate(String date) {
        String newDate = "";

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat outputFormat = new SimpleDateFormat("E MMMM dd, yyyy");

        try {
            Date inputDate = inputFormat.parse(date);
            assert inputDate != null;
            newDate = outputFormat.format(inputDate);
        } catch (ParseException pe) {
            System.out.println("Could not parse date");
            pe.printStackTrace();
        }

        return newDate;

    }

    // To hide the progress bar and text, and show the card view and recycler view
    private void toggleView(){

        // Hide views
        progressBar.setVisibility(View.GONE);
        fetching.setVisibility(View.GONE);

        // Show views
        mainContainer.setVisibility(View.VISIBLE);
        forecastRecycler.setVisibility(View.VISIBLE);
    }

    // To hide the progress bar and text, and show the error message
    private void toggleError(){

        // Hide views
        progressBar.setVisibility(View.GONE);
        fetching.setVisibility(View.GONE);

        // Show views
        errorMessage.setVisibility(View.VISIBLE);
    }

    /**
     * To set up the recycler view adapter
     */
    public void setUpAdapter(){
        // Init the adapter
        ForecastAdapter adapter = new ForecastAdapter(forecastItemList, MainActivity.this);

        // Recycler view
        forecastRecycler.setAdapter(adapter);
        forecastRecycler.setLayoutManager(new GridLayoutManager(MainActivity.this, 5, GridLayoutManager.VERTICAL, false));
        forecastRecycler.setHasFixedSize(true);
    }

}