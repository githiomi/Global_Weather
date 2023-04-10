package githiomi.dhosiolux.globalweather;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import githiomi.dhosiolux.globalweather.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Views
    TextView date, town, weatherType, mainTemp, wind, humidity, temp;
    ImageView weatherImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Binding set up
        // View binding
        ActivityMainBinding mainActivityBinding = ActivityMainBinding.inflate(getLayoutInflater());
        // Update set content view
        setContentView(mainActivityBinding.getRoot());

        date = mainActivityBinding.TVDate;
        town = mainActivityBinding.TVTownName;
        weatherImage = mainActivityBinding.IVWeatherImage;
        weatherType = mainActivityBinding.TVWeatherType;
        mainTemp = mainActivityBinding.TVMainTemperature;
        wind = mainActivityBinding.TVWindSpeed;
        humidity = mainActivityBinding.TVHumidity;
        temp = mainActivityBinding.TVTemperature;

    }
}