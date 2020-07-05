package com.example.pbwallet;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.LinkedList;
import java.util.List;

/**
 * Activity for visualize all the transaction in general, with money, date, wallet's reference
 * and the reason.
 */
public class ViewAllTransaction extends AppCompatActivity {
    String carddate, type;
    Double totalmoney;
    CustomAdapter adapter;

    /**
     * Call the populatealltransactions method.
     * @param savedInstanceState saved state for create this activity, in this application is NULL.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alltransaction);

        populatealltransactions();
    }

    /**
     * Open the database and retrieve all transaction, add them to a new ElementoLista
     * in a LinkedList of ElementoLista, and create and initialize the new CustomAdapter
     * with this list, finally close the database.
     */
    public void populatealltransactions(){
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();
        Cursor cur = db.queryLastTrans();
        Cursor cur1 = db.queryUsCard();
        Cursor cur2 = db.querySubtypeFull();
        ListView listview = findViewById(R.id.listview);
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
