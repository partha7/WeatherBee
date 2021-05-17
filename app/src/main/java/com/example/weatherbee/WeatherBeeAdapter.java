package com.example.weatherbee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeatherBeeAdapter extends RecyclerView.Adapter<WeatherBeeAdapter.WeatherBeeAdapterViewHolder> {

    private String[] weatherData;
    private final WeatherBeeOnClickListener weatherBeeOnClickListener;

    public WeatherBeeAdapter(WeatherBeeOnClickListener weatherBeeOnClickListener){
        this.weatherBeeOnClickListener = weatherBeeOnClickListener;

    }
    
    public interface WeatherBeeOnClickListener{
        void onClick(String weatherForTheDay);
    }

    @NonNull
    @Override
    public WeatherBeeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
//        System.out.println("cominOnCreate");
        int layoutfilereference = R.layout.weather_bee_data_items;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutfilereference, parent, false);

        return new WeatherBeeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherBeeAdapterViewHolder holder, int position) {
        String weather_today = weatherData[position];
        holder.tv_weatherdata.setText(weather_today);


    }

    @Override
    public int getItemCount() {
        if(weatherData == null) {
            return 0;
        }
        else {
            return weatherData.length;
        }
    }

    public void setWeatherData(String[] weatherdata) {
        weatherData = weatherdata;
        notifyDataSetChanged();
    }

    class WeatherBeeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tv_weatherdata;

        public WeatherBeeAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
//            System.out.println("coming");
            tv_weatherdata = (TextView) itemView.findViewById(R.id.tv_weather_data);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String weatherForTheDay = weatherData[adapterPosition];
            weatherBeeOnClickListener.onClick(weatherForTheDay);
        }

//        public void bind(int listIndex){
//            tv_weatherdata.setText(String.valueOf(listIndex));
//        }




    }
}
