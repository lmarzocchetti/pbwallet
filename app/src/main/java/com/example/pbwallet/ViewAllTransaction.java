package com.example.pbwallet;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ViewAllTransaction extends AppCompatActivity {
    TextView datecard, cash, subtype;
    String carddate, type;
    Double totalmoney;
    int margin;
    CustomAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alltransaction);

        populatealltransactions();
    }

    public void populatealltransactions(){
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();
        Cursor cur = db.queryLastTrans();
        Cursor cur1 = db.queryUsCard();
        Cursor cur2 = db.querySubtypeFull();
        margin = 60;
        ListView listview = (ListView)findViewById(R.id.listview);
        List<ElementoLista> list = new LinkedList<ElementoLista>();
        if(cur.moveToFirst() && cur1.moveToFirst() && cur2.moveToFirst()) {
            do {
                carddate = (cur1.getString(cur1.getColumnIndex("uscard")) + "\n" + (cur.getString(cur.getColumnIndex("date"))).substring(0, 10));
                totalmoney = cur.getDouble(cur.getColumnIndex("money"));
                type = cur2.getString(cur2.getColumnIndex("name"));
                list.add(new ElementoLista(carddate, totalmoney.toString(), type, margin));
            }while (cur.moveToNext() && cur1.moveToNext() && cur2.moveToNext());
        }
        adapter = new CustomAdapter(this, R.layout.relativelayout, list);
        listview.setAdapter(adapter);
        db.close();
    }

    public RelativeLayout createRelative(int margintop, String cashdate, String totalmoney, String type){
        RelativeLayout Rl = new RelativeLayout(this);
        Rl.findViewById(R.id.relativelayout1);
        datecard = findViewById(R.id.layout1_1);
        cash = findViewById(R.id.layout1);
        subtype = findViewById(R.id.layout1_2);
        datecard.setText(cashdate);
        cash.setText(totalmoney);
        subtype.setText(type);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) Rl.getLayoutParams();
        params.setMargins(0,margintop,0,0);
        Rl.setLayoutParams(params);
        return Rl;
    }
}
