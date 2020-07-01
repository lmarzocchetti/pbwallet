package com.example.pbwallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

public class CustomAdapter  extends ArrayAdapter<ElementoLista> {
    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<ElementoLista> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater rl = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = rl.inflate(R.layout.relativelayout,null);
        TextView datecard = (TextView)convertView.findViewById(R.id.layout1_1);
        TextView cash = (TextView)convertView.findViewById(R.id.layout1);
        TextView subtype = (TextView)convertView.findViewById(R.id.layout1_2);
        ElementoLista c = getItem(position);
        datecard.setText(c.getDatecard());
        subtype.setText(c.getSubtype());
        Double totalcash = new Double(c.getCash());
        if(totalcash > 0){
            cash.setTextColor(ContextCompat.getColor(getContext(), R.color.verde_cash));
        }
        else{
            totalcash = Math.abs(totalcash);
            cash.setTextColor(ContextCompat.getColor(getContext(), R.color.rosso_bordeaux));
        }
        cash.setText(totalcash.toString());
        return convertView;
    }
}
