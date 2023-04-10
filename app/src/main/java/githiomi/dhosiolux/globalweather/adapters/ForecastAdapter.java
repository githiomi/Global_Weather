package githiomi.dhosiolux.globalweather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import githiomi.dhosiolux.globalweather.R;
import githiomi.dhosiolux.globalweather.models.ForecastItem;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    // Adapter variables
    protected List<ForecastItem> forecastItems;
    protected Context context;

    // Adapter constructor
    public ForecastAdapter(List<ForecastItem> forecastItems, Context context) {
        this.forecastItems = forecastItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ForecastAdapter.ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_item, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastAdapter.ForecastViewHolder holder, int position) {

        // Get the specific forecast item
        ForecastItem item = forecastItems.get(position);

        // Populate the image
        String imageUrl = "https:" + item.getIcon();
        Picasso.get().load(imageUrl).into(holder.forecastIcon);

        // Populate the view
        String dayId = "Day " + item.getId();
        holder.forecastId.setText(dayId);
        holder.forecastTemp.setText(item.getForecast());

    }

    @Override
    public int getItemCount() {
        return forecastItems.size();
    }

    public static class ForecastViewHolder extends RecyclerView.ViewHolder {

        TextView forecastId, forecastTemp;
        ImageView forecastIcon;

        public ForecastViewHolder(@NonNull View itemView) {

            super(itemView);

            // Binding views
            forecastId = itemView.findViewById(R.id.TV_forecastDay);
            forecastIcon = itemView.findViewById(R.id.IV_forecastIcon);
            forecastTemp = itemView.findViewById(R.id.TV_forecastTemp);

        }
    }
}
