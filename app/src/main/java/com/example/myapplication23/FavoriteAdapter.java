package com.example.myapplication23;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.QuoteViewHolder> {
    private List<String> favoriteList;

    public FavoriteAdapter(List<String> favoriteList) {
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public QuoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new QuoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteViewHolder holder, int position) {
        holder.quoteTextView.setText(favoriteList.get(position));
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public void removeItem(int position) {
        favoriteList.remove(position);
        notifyItemRemoved(position);
    }

    class QuoteViewHolder extends RecyclerView.ViewHolder {
        TextView quoteTextView;

        QuoteViewHolder(View itemView) {
            super(itemView);
            quoteTextView = itemView.findViewById(R.id.quoteTextView);
        }
    }
}