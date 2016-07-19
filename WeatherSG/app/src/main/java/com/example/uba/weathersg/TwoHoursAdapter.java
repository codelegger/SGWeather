package com.example.uba.weathersg;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import model.WeatherForecast;

/**
 * Created by uba on 18/7/16.
 */
public class TwoHoursAdapter extends RecyclerView.Adapter<TwoHoursAdapter.ViewHolder> {

    private WeakReference<Context> contextWeakReference;
    private List<WeatherForecast> dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView icon;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.tv_two_hour_item);
            icon = (ImageView) v.findViewById(R.id.iv_two_hour_item);
        }
    }

    public TwoHoursAdapter(Context context, List<WeatherForecast> dataset) {
        this.contextWeakReference = new WeakReference<>(context);
        this.dataset = dataset;
    }

    @Override
    public TwoHoursAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_two_hours_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WeatherForecast item = dataset.get(position);
        if(item.isNearBy()) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),
                    R.color.material_color_bluegrey_800));
        }else{
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(),
                    android.R.color.transparent));
        }

        holder.name.setText(dataset.get(position).getName());
        if (contextWeakReference != null && contextWeakReference.get() != null) {
            try {
                Context context = contextWeakReference.get();
                int id = context.getResources().getIdentifier(
                        dataset.get(position).getIcon().toLowerCase(), "mipmap", context.getPackageName());
                holder.icon.setImageResource(id);
            } catch (Exception ex) {
                //in case drawable doesn't exists
                ex.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
