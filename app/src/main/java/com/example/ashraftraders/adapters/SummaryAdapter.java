package com.example.ashraftraders.adapters;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ashraftraders.R;
import com.example.ashraftraders.data.model.SummaryModel;

import java.util.List;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.ViewHolder> {

    private final List<SummaryModel> list;

    public SummaryAdapter(List<SummaryModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_top_summary,
                        parent,
                        false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 int position) {

        SummaryModel item = list.get(position);

        holder.title.setText(item.getTitle());
        holder.value.setText(item.getValue());
        holder.growth.setText(item.getGrowth());

        holder.icon.setImageResource(item.getIcon());

        switch (position) {

            case 0:

                holder.iconCard.setCardBackgroundColor(
                        Color.parseColor("#E8F5E9"));

                holder.icon.setColorFilter(
                        Color.parseColor("#2E7D32"));

                break;

            case 1:

                holder.iconCard.setCardBackgroundColor(
                        Color.parseColor("#E3F2FD"));

                holder.icon.setColorFilter(
                        Color.parseColor("#1565C0"));

                break;

            case 2:

                holder.iconCard.setCardBackgroundColor(
                        Color.parseColor("#FFF3E0"));

                holder.icon.setColorFilter(
                        Color.parseColor("#EF6C00"));

                break;

            case 3:

                holder.iconCard.setCardBackgroundColor(
                        Color.parseColor("#E8F5E9"));

                holder.icon.setColorFilter(
                        Color.parseColor("#43A047"));

                break;

            case 4:

                holder.iconCard.setCardBackgroundColor(
                        Color.parseColor("#FFEBEE"));

                holder.icon.setColorFilter(
                        Color.parseColor("#D32F2F"));

                break;

            case 5:

                holder.iconCard.setCardBackgroundColor(
                        Color.parseColor("#FFF8E1"));

                holder.icon.setColorFilter(
                        Color.parseColor("#F9A825"));

                break;

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView title;
        TextView value;
        TextView growth;
        CardView iconCard;

        ViewHolder(@NonNull View itemView) {

            super(itemView);

            icon = itemView.findViewById(R.id.imgSummary);
            title = itemView.findViewById(R.id.txtTitle);
            value = itemView.findViewById(R.id.txtValue);
            growth = itemView.findViewById(R.id.txtGrowth);
            iconCard = itemView.findViewById(R.id.iconContainer);

        }

    }

}