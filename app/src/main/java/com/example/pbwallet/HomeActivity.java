package com.example.pbwallet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    TextView name, cash, table1, table2, table3, table4, table5, table1_1, table2_1, table3_1, table4_1, table5_1, table1_2, table2_2, table3_2, table4_2, table5_2;
    ImageView bar1,bar2,bar3,bar4;
    ArrayList<TextView> aT, aT1, aT2;
    ArrayList<ImageView> aI;
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
        setContentView(R.layout.home);
        name = findViewById(R.id.nameandsurname);
        cash = findViewById(R.id.totalfunds);
        table1 = findViewById(R.id.textView1);
        table2 = findViewById(R.id.textView2);
        table3 = findViewById(R.id.textView3);
        table4 = findViewById(R.id.textView4);
        table5 = findViewById(R.id.textView5);
        table1_1 = findViewById(R.id.textView1_1);
        table2_1 = findViewById(R.id.textView2_1);
        table3_1 = findViewById(R.id.textView3_1);
        table4_1 = findViewById(R.id.textView4_1);
        table5_1 = findViewById(R.id.textView5_1);
        table1_2 = findViewById(R.id.textView1_2);
        table2_2 = findViewById(R.id.textView2_2);
        table3_2 = findViewById(R.id.textView3_2);
        table4_2 = findViewById(R.id.textView4_2);
        table5_2 = findViewById(R.id.textView5_2);
        aT = new ArrayList<>();
        aT.add(table1);
        aT.add(table2);
        aT.add(table3);
        aT.add(table4);
        aT.add(table5);
        aT1 = new ArrayList<>();
        aT1.add(table1_1);
        aT1.add(table2_1);
        aT1.add(table3_1);
        aT1.add(table4_1);
        aT1.add(table5_1);
        aT2 = new ArrayList<>();
        aT2.add(table1_2);
        aT2.add(table2_2);
        aT2.add(table3_2);
        aT2.add(table4_2);
        aT2.add(table5_2);
        bar1 = findViewById(R.id.bar1);
        bar2 = findViewById(R.id.bar2);
        bar3 = findViewById(R.id.bar3);
        bar4 = findViewById(R.id.bar4);
        aI = new ArrayList<>();
        aI.add(bar1);
        aI.add(bar2);
        aI.add(bar3);
        aI.add(bar4);
        bar1.setVisibility(View.INVISIBLE);
        bar2.setVisibility(View.INVISIBLE);
        bar3.setVisibility(View.INVISIBLE);
        bar4.setVisibility(View.INVISIBLE);

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
        for(TextView t : aT) {
            t.setText("");
        }
        for(TextView t : aT1) {
            t.setText("");
        }
        for(TextView t : aT2) {
            t.setText("");
        }
        for(ImageView t : aI) {
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
        db.open();

        Cursor cur = db.queryLastTrans();

        if(cur.moveToFirst()) {
            do {
                totaltrans = cur.getDouble(cur.getColumnIndex("money"));
                if(totaltrans > 0) {
                    aT.get(i).setTextColor(ContextCompat.getColor(this, R.color.verde_cash));
                }
                else {
                    totaltrans = Math.abs(totaltrans);
                    aT.get(i).setTextColor(ContextCompat.getColor(this, R.color.rosso_bordeaux));
                }
                aT.get(i).setText(totaltrans +" "+ currency);
                System.out.println(totaltrans);
                i++;
            } while(cur.moveToNext() && i < 5);
        }

        cur = db.queryUsCard();
        i = 0;

        if(cur.moveToFirst()) {
            do {
                aT1.get(i).setText(cur.getString(cur.getColumnIndex("uscard")));
                i++;
            } while(cur.moveToNext() && i < 5);
        }

        for(int j = 0; j < i-1; j++) {
            aI.get(j).setVisibility(View.VISIBLE);
        }

        i = 0;
        cur = db.querySubtypeFull();

        if(cur.moveToFirst()) {
            do {
                aT2.get(i).setText(cur.getString(cur.getColumnIndex("name")));
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
        db.open();

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
        cash.setText(strcash);
    }

    /**
     * Read the name and surname of the user and print in the
     * respective TextField
     */
    public void changeNameandSur(){
        String nameandsur = null;
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();
        Cursor cur = db.queryUserFull();
        if(cur.moveToFirst())
            nameandsur = cur.getString(cur.getColumnIndex("name"))+" "+cur.getString(cur.getColumnIndex("surname"));
            name.setText(nameandsur);
        db.close();
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
