package githiomi.dhosiolux.globalweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BlurShadowImageView blurshadowimageview = (ImageView) findViewById(R.id.IV_weatherImage);

//Sets Border Round Radius
        blurshadowimageview.setRound((int) value);

//Sets Image Resource
        blurshadowimageview.setImageResource(ImgRes);

//Sets Image Drawable
        blurshadowimageview.setImageDrawable(drawable);

//Sets Image Bitmap
        blurshadowimageview.setImageBitmap(bitmap);
    }
}