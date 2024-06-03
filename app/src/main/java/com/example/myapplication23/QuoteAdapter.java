package com.example.myapplication23;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder> {
    private ArrayList<Quote> quoteList;

    public QuoteAdapter(ArrayList<Quote> quoteList) {
        this.quoteList = quoteList;
    }

    @NonNull
    @Override
    public QuoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new QuoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteViewHolder holder, int position) {
        Quote    quote = quoteList.get(position);
        holder.textViewText.setText(quote.getQuote());
       ;
    }

    @Override
    public int getItemCount() {
        return quoteList.size();
    }

    public static class QuoteViewHolder extends RecyclerView.ViewHolder {
        TextView textViewText;


        public QuoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewText = itemView.findViewById(R.id.textViewMessage);

        }
    }
}