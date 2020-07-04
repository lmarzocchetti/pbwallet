package com.example.pbwallet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Activity that visualize user's wallets, money inside them and the last 5 transaction in that card.
 * @see AddWalletActivity button to create a New wallet.
 */
public class FundsActivity extends AppCompatActivity {
    ArrayList<String> fund_list;
    ArrayList<TextView> type, trans, date;
    ArrayList<ImageView> bar;
    TextView name, wallet_money;
    TextView type1, type2, type3, type4, type5;
    TextView trans1, trans2, trans3, trans4, trans5;
    TextView date1, date2, date3, date4, date5;
    ImageView bar1, bar2, bar3, bar4;
    FloatingActionButton add_wallet;
    AutoCompleteTextView fund_switch;
    BottomNavigationView navbar;
    String selected;
    DatabaseBeReader db;

    /**
     * Initialize attributes from this class, set their own listener and call methods to populate them.
     * @param savedInstanceState saved state for create this activity, in this application is NULL.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.funds);

        name = findViewById(R.id.nameandsurnameFUNDS);
        changeNameandSur();

        wallet_money = findViewById(R.id.wallet_money);

        add_wallet = findViewById(R.id.add_wallet);
        add_wallet.setOnClickListener(add_wallet_listener);

        navbar = findViewById(R.id.nav_bar);
        navbar.setSelectedItemId(R.id.nav_fund);
        navbar.setOnNavigationItemSelectedListener(navigationlistener);

        selected = "";

        type = new ArrayList<>();
        trans = new ArrayList<>();
        date = new ArrayList<>();
        bar = new ArrayList<>();

        type.add((type1 = findViewById(R.id.type1)));
        type.add((type2 = findViewById(R.id.type2)));
        type.add((type3 = findViewById(R.id.type3)));
        type.add((type4 = findViewById(R.id.type4)));
        type.add((type5 = findViewById(R.id.type5)));


        trans.add((trans1 = findViewById(R.id.trans1)));
        trans.add((trans2 = findViewById(R.id.trans2)));
        trans.add((trans3 = findViewById(R.id.trans3)));
        trans.add((trans4 = findViewById(R.id.trans4)));
        trans.add((trans5 = findViewById(R.id.trans5)));

        date.add((date1 = findViewById(R.id.date1)));
        date.add((date2 = findViewById(R.id.date2)));
        date.add((date3 = findViewById(R.id.date3)));
        date.add((date4 = findViewById(R.id.date4)));
        date.add((date5 = findViewById(R.id.date5)));

        bar.add((bar1 = findViewById(R.id.bar1)));
        bar.add((bar2 = findViewById(R.id.bar2)));
        bar.add((bar3 = findViewById(R.id.bar3)));
        bar.add((bar4 = findViewById(R.id.bar4)));

        fund_switch = findViewById(R.id.fund_menu);
        fund_switch.setOnItemClickListener(switch_listener);
        populateFundMenu();
        if(!selected.isEmpty()) {
            populateLastTrans();
        }
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
     * Repopulate the WalletMenu, the last 5 transactions and set the item on the BottomBar(this activity).
     */
    @Override
    protected void onResume() {
        super.onResume();
        populateFundMenu();
        populateLastTrans();
        navbar.setSelectedItemId(R.id.nav_fund);
    }

    /**
     * Instance the database and open it, Set the total wallet's money in the TextField.
     * After read in the database the last 5 transactions in this wallet and display them
     * in their relative TextField with the motive of the transaction, it's date, and money.
     * Money is printed in Green when the transaction is positive and in red when is not.
     */
    @SuppressLint("SetTextI18n")
    private void populateLastTrans() {
        db = new DatabaseBeReader(this);
        db.open();
        int idselcard;
        double money;

        Cursor cur = db.queryCard("uscard", selected);
        DecimalFormat df = new DecimalFormat("#.00");

        if(cur.moveToFirst()) {
            idselcard = cur.getInt(cur.getColumnIndex("idcard"));
            money = cur.getDouble(cur.getColumnIndex("money"));
            if(money == 0) {
                wallet_money.setText(money + HomeActivity.currency);
            }
            else {
                wallet_money.setText(df.format(money) + " " + HomeActivity.currency);
            }
        }
        else {
            db.close();
            return;
        }

        int i = 0, idtrans;
        double sos;
        String sottotipo, data;
        cur = db.queryLastTransCard(idselcard);
        Cursor cur1;
        if(cur.moveToFirst()) {
            do {
                idtrans = cur.getInt(cur.getColumnIndex("idtrans"));
                sos = cur.getDouble(cur.getColumnIndex("money"));
                data = cur.getString(cur.getColumnIndex("date"));
                data = data.substring(0, 10);
                date.get(i).setText(data);
                if(i != 0) {
                    bar.get(i-1).setVisibility(View.VISIBLE);
                }
                cur1 = db.querySubtype(idtrans);
                if(cur1.moveToFirst()) {
                    sottotipo = cur1.getString(cur1.getColumnIndex("name"));
                    type.get(i).setText(sottotipo);
                }
                if(sos < 0) {
                    trans.get(i).setTextColor(ContextCompat.getColor(this, R.color.rosso_bordeaux));
                    sos = Math.abs(sos);
                }
                else {
                    trans.get(i).setTextColor(ContextCompat.getColor(this, R.color.verde_cash));
                }
                trans.get(i).setText(df.format(sos)+" "+HomeActivity.currency);
                ++i;
            } while(cur.moveToNext() && i < 5);
        }
        db.close();
    }

    /**
     * Instance the database, and retrieve all card in the database
     * and insert them in the switch component for wallets.
     */
    private void populateFundMenu() {
        db = new DatabaseBeReader(this);
        fund_list = new ArrayList<>();
        db.open();

        Cursor cur = db.queryCardFull();
        if(cur.moveToFirst()) {
            do {
                String ins = cur.getString(cur.getColumnIndex("uscard"));
                fund_list.add(ins);
            } while(cur.moveToNext());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, fund_list);
        fund_switch.setAdapter(adapter);

        if(!fund_list.isEmpty()) {
            fund_switch.setText(fund_list.get(0), false);
            selected = fund_list.get(0);
        }
        db.close();
    }

    /**
     * Read the name and surname of the user and print in the
     * respective TextField
     */
    private void changeNameandSur(){
        String nameandsur = null;
        db = new DatabaseBeReader(this);
        db.open();
        Cursor cur = db.queryUserFull();
        if(cur.moveToFirst())
            nameandsur = cur.getString(cur.getColumnIndex("name"))+" "+cur.getString(cur.getColumnIndex("surname"));
        name.setText(nameandsur);
        db.close();
    }

    /**
     * Reset all TextView with the empty string
     * and set invisible the line separators
     */
    private void resetTrans() {
        for(TextView t : trans) {
            t.setText("");
        }
        for(TextView t : type) {
            t.setText("");
        }
        for(TextView t : date) {
            t.setText("");
        }
        for(ImageView t : bar) {
            t.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Listener for the wallet switcher component.
     * Set the selected param to the selected item in the switcher, resetTrans and RePopuplate them.
     */
    private AdapterView.OnItemClickListener switch_listener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(!fund_list.get(i).equals(selected)) {
                        selected = fund_list.get(i);
                        resetTrans();
                        populateLastTrans();
                    }
                }
            };

    /**
     * Listener for the button that start a AddWalletActivity.
     */
    private FloatingActionButton.OnClickListener add_wallet_listener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onPause();
                    startActivity(new Intent(getApplicationContext(), AddWalletActivity.class));
                    onStop();
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
                            onPause();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            overridePendingTransition(0,0);
                            onStop();
                            finish();
                            break;

                        case R.id.nav_fund:
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
