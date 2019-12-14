package org.tensorflow.lite.examples.classification.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.tensorflow.lite.examples.classification.R;
import org.tensorflow.lite.examples.classification.model.MissionDataObject;
import org.tensorflow.lite.examples.classification.model.SensorDataObject;

import java.util.List;

public class SensorDataAdapter extends RecyclerView.Adapter<SensorDataAdapter.MyViewHolder> {

    Context cxt;
    List<SensorDataObject> list;
    List<MissionDataObject> mlist;



    public SensorDataAdapter(Context context, List<SensorDataObject> list, List<MissionDataObject> mlist){
        this.list = list;
        this.cxt = context;
        this.mlist = mlist;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_sensor_data, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SensorDataObject sdo = list.get(position);
        holder.accTV.setText(cxt.getString(R.string.acc_format,sdo.getAccx(),sdo.getAccy(),sdo.getAccz()));
        holder.lightTV.setText(cxt.getString(R.string.light_format,sdo.getLight()));
        holder.pressureTV.setText(cxt.getString(R.string.pressure_format,sdo.getPressure()));
        holder.humidityTV.setText(cxt.getString(R.string.relative_humidity_format,sdo.getRelativeHumidity()));
        holder.temperatureTV.setText(cxt.getString(R.string.ambient_temp_format, sdo.getAmbient_temp()));
        holder.latitudeTV.setText(cxt.getString(R.string.latitude_format, sdo.getLatitude()));
        holder.longitudeTV.setText(cxt.getString(R.string.longitude_format, sdo.getLongitude()));
        holder.altitudeTV.setText (cxt.getString(R.string.altitude_format, sdo.getAltitude()));

        MissionDataObject mdo = mlist.get(position);
        holder.widthTV.setText(cxt.getString(R.string.mission_width_format, mdo.getU_width()));
        holder.lengthTV.setText(cxt.getString(R.string.mission_length_format,mdo.getU_length()));
        holder.durationTV.setText(cxt.getString(R.string.mission_duration_format,mdo.getU_duration()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView accTV;
        TextView lightTV;
        TextView pressureTV;
        TextView humidityTV;
        TextView temperatureTV;
        TextView latitudeTV;
        TextView longitudeTV;
        TextView altitudeTV;

        TextView widthTV;
        TextView lengthTV;
        TextView durationTV;


       public MyViewHolder(@NonNull View itemView) {
           super(itemView);
           accTV = itemView.findViewById(R.id.acc);
           lightTV = itemView.findViewById(R.id.light);
           pressureTV = itemView.findViewById(R.id.pressure);
           humidityTV = itemView.findViewById(R.id.humidity);
           temperatureTV = itemView.findViewById(R.id.temp);
           latitudeTV = itemView.findViewById(R.id.latitude);
           longitudeTV = itemView.findViewById(R.id.longitude);
           altitudeTV = itemView.findViewById(R.id.altitude);

           widthTV = itemView.findViewById(R.id.width);
           lengthTV = itemView.findViewById(R.id.length);
           durationTV = itemView.findViewById(R.id.duration);


       }
   }
}
