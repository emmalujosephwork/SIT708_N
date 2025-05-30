package com.example.lafmap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.AdvertViewHolder> {

    private List<Advert> adverts;

    public AdvertAdapter(List<Advert> adverts) {
        this.adverts = adverts;
    }

    @NonNull
    @Override
    public AdvertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_advert, parent, false);
        return new AdvertViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertViewHolder holder, int position) {
        Advert advert = adverts.get(position);
        holder.tvType.setText(advert.type);
        holder.tvName.setText(advert.name);
        holder.tvPhone.setText(advert.phone);
        holder.tvDescription.setText(advert.description);
        holder.tvDate.setText(advert.date);
        holder.tvLocation.setText(advert.location);
    }

    @Override
    public int getItemCount() {
        return adverts.size();
    }

    static class AdvertViewHolder extends RecyclerView.ViewHolder {
        TextView tvType, tvName, tvPhone, tvDescription, tvDate, tvLocation;

        public AdvertViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvType);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvLocation = itemView.findViewById(R.id.tvLocation);
        }
    }
}
