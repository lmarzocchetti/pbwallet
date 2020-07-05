package com.example.pbwallet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

/**
 *  This class initializes a new relative layout in the ViewAllTransaction list,
 *  extends an ArrayAdapter for TextView management
 */
public class CustomAdapter  extends ArrayAdapter<ElementoLista> {
    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<ElementoLista> objects) {
        super(context, resource, objects);
    }

    /**
     * This method initializes a View with the RelativeLayout, defines the TextView and changes its text and color
     * @param position Int indicating the position
     * @param convertView View initialized as a new RelativeLayout
     * @param parent This param has not been used
     * @return View to print
     */
    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater rl = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = rl.inflate(R.layout.relativelayout,null);
        TextView datecard = convertView.findViewById(R.id.layout1_1);
        TextView cash = convertView.findViewById(R.id.layout1);
        TextView subtype = convertView.findViewById(R.id.layout1_2);
        ElementoLista c = getItem(position);
        assert c != null;
        datecard.setText(c.getDatecard());
        subtype.setText(c.getSubtype());
        double totalcash = Double.parseDouble(c.getCash());
        if(totalcash > 0){
            cash.setTextColor(ContextCompat.getColor(getContext(), R.color.verde_cash));
        }
        else{
            totalcash = Math.abs(totalcash);
            cash.setTextColor(ContextCompat.getColor(getContext(), R.color.rosso_bordeaux));
        }
        cash.setText(totalcash+" "+HomeActivity.currency);
        return convertView;
    }
}
