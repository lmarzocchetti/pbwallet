package com.example.pbwallet;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.Toast;

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
    TransactionAdapter adapter;

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
     * Open the database and retrieve all transaction, add them to a new TransactionElement
     * in a LinkedList of TransactionElement, and create and initialize the new TransactionAdapter
     * with this list, finally close the database.
     */
    public void populatealltransactions(){
        DatabaseBeReader db = new DatabaseBeReader(this);

        try {
            db.open();
        } catch (SQLiteException e) {
            Log.d("pbwallet", "Exception: " + Log.getStackTraceString(e));
            try {
                db.open();
            } catch (SQLiteException e_1) {
                Log.d("pbwallet", "Exception: " + Log.getStackTraceString(e_1));
                databaseError();
                finish();
            }
        }

        Cursor cur = db.queryLastTrans();
        Cursor cur1 = db.queryUsCard();
        Cursor cur2 = db.querySubtypeFull();
        ListView listview = findViewById(R.id.listview);
        List<TransactionElement> list = new LinkedList<>();
        if(cur.moveToFirst() && cur1.moveToFirst() && cur2.moveToFirst()) {
            do {
                carddate = (cur1.getString(cur1.getColumnIndex("uscard")) + "\n" + (cur.getString(cur.getColumnIndex("date"))).substring(0, 10));
                totalmoney = cur.getDouble(cur.getColumnIndex("money"));
                type = cur2.getString(cur2.getColumnIndex("name"));
                list.add(new TransactionElement(carddate, totalmoney.toString(), type));
            } while (cur.moveToNext() && cur1.moveToNext() && cur2.moveToNext());
        }
        adapter = new TransactionAdapter(this, R.layout.relativelayout_transaction, list);
        listview.setAdapter(adapter);
        db.close();
    }

    /**
     * If this method is called, there is a serious problem with the database,
     * so create a toast to notificate the user, delete all database and restart the app.
     */
    private void databaseError() {
        Toast t = Toast.makeText(this, "Database error\nDeleting database...", Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
        getApplicationContext().deleteDatabase(DatabaseBeReader.DB_NAME);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }, 3000);
    }
}
