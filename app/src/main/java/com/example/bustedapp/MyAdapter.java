package com.example.bustedapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;

    private ArrayList<Wifi> list;

    public MyAdapter(Context context, ArrayList<Wifi> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Wifi wifi = list.get(position);
        holder.time.setText(wifi.getTime());
        holder.date.setText(wifi.getDate());
        holder.ssid.setText(wifi.getSsid());
        holder.mac.setText(wifi.getMacadd());
        holder.percent.setText(wifi.getPercentage());
        holder.encrypt.setText(wifi.getEncryption());
        holder.stat.setText(wifi.getStatus());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView date, time, ssid, mac, percent, encrypt, stat;
        Button btnreport;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.wifi_time);
            date = itemView.findViewById(R.id.wifi_date);
            ssid = itemView.findViewById(R.id.ssidName);
            mac = itemView.findViewById(R.id.mac_address);
            percent = itemView.findViewById(R.id.percentage_amount);
            encrypt = itemView.findViewById(R.id.encryp_type);
            stat = itemView.findViewById(R.id.hidden_stat);
            btnreport = itemView.findViewById(R.id.reportButton);


        }
    }
}

