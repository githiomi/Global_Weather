package githiomi.dhosiolux.globalweather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import githiomi.dhosiolux.globalweather.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // View binding
    private ActivityMainBinding mainActivityBinding;

    // Views
    TextView date, town, weatherType, mainTemp, wind, humidity, temp;
    ImageView weatherImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Binding set up
        mainActivityBinding = ActivityMainBinding.inflate(getLayoutInflater());
        // Update set content view
        setContentView(mainActivityBinding.getRoot());

        date = mainActivityBinding.TVDate;
        town = mainActivityBinding.TVTownName;

    }
}