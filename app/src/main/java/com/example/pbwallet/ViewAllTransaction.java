package com.example.pbwallet;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.LinkedList;
import java.util.List;

public class ViewAllTransaction extends AppCompatActivity {
    String carddate, type;
    Double totalmoney;
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
        ListView listview = (ListView)findViewById(R.id.listview);
        List<ElementoLista> list = new LinkedList<>();
        if(cur.moveToFirst() && cur1.moveToFirst() && cur2.moveToFirst()) {
            do {
                carddate = (cur1.getString(cur1.getColumnIndex("uscard")) + "\n" + (cur.getString(cur.getColumnIndex("date"))).substring(0, 10));
                totalmoney = cur.getDouble(cur.getColumnIndex("money"));
                type = cur2.getString(cur2.getColumnIndex("name"));
                list.add(new ElementoLista(carddate, totalmoney.toString(), type));
            }while (cur.moveToNext() && cur1.moveToNext() && cur2.moveToNext());
        }
        adapter = new CustomAdapter(this, R.layout.relativelayout, list);
        listview.setAdapter(adapter);
        db.close();
    }
}
