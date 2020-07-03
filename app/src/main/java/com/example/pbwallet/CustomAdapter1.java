package com.example.pbwallet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

public class CustomAdapter1 extends ArrayAdapter<ElementoLista1> {
    public CustomAdapter1(@NonNull Context context, int resource, @NonNull List<ElementoLista1> objects) {
        super(context, resource, objects);
    }

    @SuppressLint({"ViewHolder", "SetTextI18n", "InflateParams"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater rl = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = rl.inflate(R.layout.relativelayout1,null);
        ImageView imageType = (ImageView) convertView.findViewById(R.id.layout1_1);
        TextView nameType = (TextView)convertView.findViewById(R.id.layout1);
        TextView percentage = (TextView)convertView.findViewById(R.id.layout1_2);
        TextView date = (TextView)convertView.findViewById(R.id.layout1_3);
        ElementoLista1 c = getItem(position);
        percentage.setText(c.getCash()+HomeActivity.currency+"/"+c.getBound()+HomeActivity.currency);
        assert c != null;
        nameType.setText(c.getSubtype());
        date.setText(c.getDate());
        AccessImage ai = new AccessImage();
        imageType.setBackgroundResource(ai.get(c.getType()));
        Double totalcash = Double.valueOf(c.getCash());
        Double totalbound = Double.valueOf(c.getBound());
        if(totalcash > totalbound){
            percentage.setTextColor(ContextCompat.getColor(getContext(), R.color.rosso_bordeaux));
        }
        else{
            percentage.setTextColor(ContextCompat.getColor(getContext(), R.color.verde_cash));
        }
        return convertView;
    }
}
