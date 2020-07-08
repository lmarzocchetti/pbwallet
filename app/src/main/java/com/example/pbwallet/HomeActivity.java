package com.example.pbwallet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Activity that visualize the user's total money and the last 5 transaction (in general).
 * Provide a button that let the user see ALL transaction of all card.
 * @see ViewAllTransaction
 */
public class HomeActivity extends AppCompatActivity {
    TextView name, totalfunds, cash1, cash2, cash3, cash4, cash5;
    TextView card1, card2, card3, card4, card5;
    ImageView bar1,bar2,bar3,bar4;
    TextView subtype1, subtype2, subtype3, subtype4, subtype5;
    ArrayList<TextView> acash, acard, asubtype;
    ArrayList<ImageView> abar;
    static String currency;
    BottomNavigationView navbar;
    Button alltrans;

    /**
     * Initialize attributes from this class, set their own listener and call methods to populate them
     * @param savedInstanceState saved state for create this activity, in this application is NULL
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        name = findViewById(R.id.nameandsurname);
        totalfunds = findViewById(R.id.totalfunds);
        acash = new ArrayList<>();
        acash.add(cash1 = findViewById(R.id.textView1));
        acash.add(cash2 = findViewById(R.id.textView2));
        acash.add(cash3 = findViewById(R.id.textView3));
        acash.add(cash4 = findViewById(R.id.textView4));
        acash.add(cash5 = findViewById(R.id.textView5));
        acard = new ArrayList<>();
        acard.add(card1 = findViewById(R.id.textView1_1));
        acard.add(card2 = findViewById(R.id.textView2_1));
        acard.add(card3 = findViewById(R.id.textView3_1));
        acard.add(card4 = findViewById(R.id.textView4_1));
        acard.add(card5 = findViewById(R.id.textView5_1));
        asubtype = new ArrayList<>();
        asubtype.add(subtype1 = findViewById(R.id.textView1_2));
        asubtype.add(subtype2 = findViewById(R.id.textView2_2));
        asubtype.add(subtype3 = findViewById(R.id.textView3_2));
        asubtype.add(subtype4 = findViewById(R.id.textView4_2));
        asubtype.add(subtype5 = findViewById(R.id.textView5_2));
        abar = new ArrayList<>();
        abar.add(bar1 = findViewById(R.id.bar1));
        abar.add(bar2 = findViewById(R.id.bar2));
        abar.add(bar3 = findViewById(R.id.bar3));
        abar.add(bar4 = findViewById(R.id.bar4));
        for(ImageView i : abar){
            i.setVisibility(View.INVISIBLE);
        }

        alltrans = findViewById(R.id.buttonalltrans);
        alltrans.setOnClickListener(all_trans_listener);

        changeCash();
        changeNameandSur();
        changeLastTrans();

        navbar = findViewById(R.id.nav_bar);
        navbar.setSelectedItemId(R.id.nav_home);
        navbar.setOnNavigationItemSelectedListener(navigationlistener);

    }

    /**
     * Method called by Android-platform when this activity is in stop-state and being restarted.
     * Simply call the resetTrans method on this class
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        resetTrans();
    }

    /**
     * Method called by Android-platform when this activity is in pause-state and being resumed (called after onRestart).
     * Change total money, the last 5 transactions and set the item on the BottomBar(this activity).
     */
    @Override
    protected void onResume() {
        super.onResume();
        changeLastTrans();
        changeCash();
        navbar.setSelectedItemId(R.id.nav_home);
    }

    /**
     * Reset all TextView with the empty string
     * and set invisible the line separators
     */
    private void resetTrans() {
        for(TextView t : acash) {
            t.setText("");
        }
        for(TextView t : acard) {
            t.setText("");
        }
        for(TextView t : asubtype) {
            t.setText("");
        }
        for(ImageView t : abar) {
            t.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Instance database, retrieve the last 5 general transaction, and print the money, wallet
     * and the motive of the transaction. Money is printed in green for positive transaction and
     * in red for negative.
     */
    @SuppressLint("SetTextI18n")
    public void changeLastTrans(){
        int i = 0;
        double totaltrans;

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

        if(cur.moveToFirst()) {
            do {
                totaltrans = cur.getDouble(cur.getColumnIndex("money"));
                if(totaltrans > 0) {
                    acash.get(i).setTextColor(ContextCompat.getColor(this, R.color.verde_cash));
                }
                else {
                    totaltrans = Math.abs(totaltrans);
                    acash.get(i).setTextColor(ContextCompat.getColor(this, R.color.rosso_bordeaux));
                }
                acash.get(i).setText(totaltrans +" "+ currency);
                System.out.println(totaltrans);
                i++;
            } while(cur.moveToNext() && i < 5);
        }

        cur = db.queryUsCard();
        i = 0;

        if(cur.moveToFirst()) {
            do {
                acard.get(i).setText(cur.getString(cur.getColumnIndex("uscard")));
                i++;
            } while(cur.moveToNext() && i < 5);
        }

        for(int j = 0; j < i-1; j++) {
            abar.get(j).setVisibility(View.VISIBLE);
        }

        i = 0;
        cur = db.querySubtypeFull();

        if(cur.moveToFirst()) {
            do {
                asubtype.get(i).setText(cur.getString(cur.getColumnIndex("name")));
                i++;
            } while(cur.moveToNext() && i < 5);
        }

        db.close();
    }

    /**
     * Retrieve and sum all the money in all wallets, and set the relative
     * TextField to this value
     */
    public void changeCash(){
        double totalcash = 0;

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

        Cursor cur = db.queryCardFull();

        if(cur.moveToFirst()) {
            do {
                totalcash += cur.getDouble(cur.getColumnIndex("money"));
            } while(cur.moveToNext());
        }

        cur = db.queryUserFull();

        if(cur.moveToFirst()){
            currency = cur.getString(cur.getColumnIndex("currency"));
        }

        db.close();

        DecimalFormat df = new DecimalFormat("#.00");
        String strcash = Double.valueOf(df.format(totalcash)).toString()+" "+currency;
        totalfunds.setText(strcash);
    }

    /**
     * Read the name and surname of the user and print in the
     * respective TextField
     */
    public void changeNameandSur(){
        String nameandsur = null;
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

        Cursor cur = db.queryUserFull();
        if(cur.moveToFirst())
            nameandsur = cur.getString(cur.getColumnIndex("name"))+" "+cur.getString(cur.getColumnIndex("surname"));
            name.setText(nameandsur);
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

    /**
     * Listener to start the ViewAllTransaction activity
     */
    private Button.OnClickListener all_trans_listener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent allviewtrans = new Intent(HomeActivity.this, ViewAllTransaction.class);
                    startActivity(allviewtrans);
                }
            };

    /**
     * Listener for the bottom navigation view.
     * Allow the user to switch between Activity, or select the Plus button
     * to start AddTransactionActivity to add a new transaction.
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navigationlistener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch(item.getItemId()) {
                        case R.id.nav_home:
                            break;

                        case R.id.nav_fund:
                            onPause();
                            startActivity(new Intent(getApplicationContext(),FundsActivity.class));
                            overridePendingTransition(0,0);
                            onStop();
                            finish();
                            break;

                        case R.id.nav_stats:
                            onPause();
                            startActivity(new Intent(getApplicationContext(),StatsActivity.class));
                            overridePendingTransition(0,0);
                            onStop();
                            finish();
                            break;

                        case R.id.add_transaction:
                            onPause();
                            startActivity(new Intent(getApplicationContext(), AddTransactionActivity.class));
                            onStop();
                            break;

                        case R.id.nav_budget:
                            onPause();
                            startActivity(new Intent(getApplicationContext(), BudgetActivity.class));
                            overridePendingTransition(0, 0);
                            onStop();
                            finish();
                            break;
                    }
                    return true;
                }
            };
}
