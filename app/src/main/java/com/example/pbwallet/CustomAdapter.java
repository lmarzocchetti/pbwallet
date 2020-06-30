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
        cash.setText(c.getCash());
        subtype.setText(c.getSubtype());

        return convertView;
    }
}
