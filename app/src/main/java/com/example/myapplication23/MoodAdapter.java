package com.example.myapplication23;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MoodAdapter extends BaseAdapter {

    ArrayList<Pair<String, String>> arrayList;
TextView noteTitletextView;
TextView noteDespView;
TextView dateView;

    public MoodAdapter(ArrayList<Pair<String, String>> arrayList) {
        this.arrayList = arrayList;


    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       if(convertView==null)
       {
           LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

            convertView=layoutInflater.inflate(R.layout.item_mood,null);

       }
        TextView textViewMood=convertView.findViewById(R.id.textMood);
       TextView textViewDate=convertView.findViewById(R.id.textDate);


        Pair<String, String> item = arrayList.get(position);
        String mood = item.first;
        String date = item.second;

        textViewMood.setText(mood);
        textViewDate.setText(date);

        return convertView;


    }
}
