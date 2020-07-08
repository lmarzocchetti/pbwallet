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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;

/**
 * Activity for the creation of the statistics page, comparison between two months and
 * drawing of the graph of the last six months
 */
public class StatsActivity extends AppCompatActivity {
    TextView name, month1, month2, earning1, earning2, expense1, expense2, percentage, text_percentage, date1, date2, date3, date4, date5, date6, cash1, cash2, cash3, cash4, cash5, cash6;
    AutoCompleteTextView fund_switch1,fund_switch2;
    ImageView circle1, circle2, circle3, circle4 , circle5, circle6;
    String selected, selected2;
    ArrayList<String> fund_list, fund_list2;
    ArrayList<String> A_switchdate, A_month;
    ArrayList<TextView> A_textdate, A_textcash;
    ArrayList<Double> A_monthcash;
    ArrayList<Double> A_maxcash;
    ArrayList<ImageView> A_circle;
    SwitchMaterial switchpercent;
    Double money1, money2;
    Double moneypos1, moneyneg1, moneypos2, moneyneg2;
    BottomNavigationView navbar;

    /**
     * Initialize attributes from this class, set their own listener and call methods to populate them
     * @param savedInstanceState saved state for create this activity, in this application is NULL
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        name = findViewById(R.id.nameandsurname);
        month1 = findViewById(R.id.mese1);
        month2 = findViewById(R.id.mese2);
        earning1 = findViewById(R.id.entrata1);
        earning2 = findViewById(R.id.entrata2);
        expense1 = findViewById(R.id.uscita1);
        expense2 = findViewById(R.id.uscita2);
        text_percentage = findViewById(R.id.textView5_1);
        percentage = findViewById(R.id.percentuale1);
        fund_switch1 = findViewById(R.id.fund_menu2);
        fund_switch2 = findViewById(R.id.fund_menu1);
        switchpercent = findViewById(R.id.switchperc);
        A_textdate = new ArrayList<>();
        A_textcash = new ArrayList<>();
        A_circle = new ArrayList<>();
        A_textdate.add(date1 = findViewById(R.id.date1));
        A_textdate.add(date2 = findViewById(R.id.date2));
        A_textdate.add(date3 = findViewById(R.id.date3));
        A_textdate.add(date4 = findViewById(R.id.date4));
        A_textdate.add(date5 = findViewById(R.id.date5));
        A_textdate.add(date6 = findViewById(R.id.date6));
        A_textcash.add(cash1 = findViewById(R.id.cashdate1));
        A_textcash.add(cash2 = findViewById(R.id.cashdate2));
        A_textcash.add(cash3 = findViewById(R.id.cashdate3));
        A_textcash.add(cash4 = findViewById(R.id.cashdate4));
        A_textcash.add(cash5 = findViewById(R.id.cashdate5));
        A_textcash.add(cash6 = findViewById(R.id.cashdate6));
        A_circle.add(circle1 = findViewById(R.id.circle1));
        A_circle.add(circle2 = findViewById(R.id.circle2));
        A_circle.add(circle3 = findViewById(R.id.circle3));
        A_circle.add(circle4 = findViewById(R.id.circle4));
        A_circle.add(circle5 = findViewById(R.id.circle5));
        A_circle.add(circle6 = findViewById(R.id.circle6));
        for(ImageView i : A_circle){
            i.setVisibility(View.INVISIBLE);
        }
        money1 = null;
        money2 = null;
        populateMonths();
        changeNameandSur();
        selected = "";
        selected2 = "";

        fund_switch1.setOnItemClickListener(switch_listener);
        fund_switch2.setOnItemClickListener(switch_listener2);
        switchpercent.setOnCheckedChangeListener(listener_switch);
        populateFundMenu();
        populateFundMenu2();
        if(!selected.isEmpty()) {
           populateTrans();
        }
        if(!selected2.isEmpty()){
            populateTrans2();
        }
        percent();

        navbar = findViewById(R.id.nav_bar);
        navbar.setSelectedItemId(R.id.nav_stats);
        navbar.setOnNavigationItemSelectedListener(navigationlistener);
    }

    /**
     * Method called by Android-platform when this activity is in pause-state and being resumed (called after onRestart).
     * Change total money, the last 5 transactions and set the item on the BottomBar(this activity).
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Method called by Android-platform when this activity is in stop-state and being restarted.
     * Simply call the resetTrans method on this class
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        navbar.setSelectedItemId(R.id.nav_stats);
        resetTrans();
        resetTrans2();
        populateMonths();
        populateFundMenu();
        populateFundMenu2();
        populateTrans();
        populateTrans2();
        percent();
    }

    /**
     * Listener for navigation bar
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
                            onPause();
                            startActivity(new Intent(getApplicationContext(),FundsActivity.class));
                            overridePendingTransition(0,0);
                            onStop();
                            finish();
                            break;

                        case R.id.nav_stats:
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

    /**
     * Switch listener to view the percentage of expenses and earnings
     */
    private SwitchMaterial.OnCheckedChangeListener listener_switch =
            new SwitchMaterial.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    percent();
                }
            };


    /**
     * Switch listener for the choice of the first month
     */
    private AdapterView.OnItemClickListener switch_listener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(!fund_list.get(i).equals(selected)) {
                        selected = fund_list.get(i);
                        month1.setText(selected);
                        resetTrans();
                        populateTrans();
                        percent();
                    }
                }
            };

    /**
     * Switch listener for the choice of the second month
     */
    private AdapterView.OnItemClickListener switch_listener2 =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(!fund_list2.get(i).equals(selected2)) {
                        selected2 = fund_list2.get(i);
                        month2.setText(selected2);
                        resetTrans2();
                        populateTrans2();
                        percent();
                    }
                }
            };

    /**
     * Method for reset the TextView earnings and expenses for first month
     */
    private void resetTrans() {
        earning1.setText("");
        expense1.setText("");
    }

    /**
     * Method for reset the TextView earnings and expenses for second month
     */
    private void resetTrans2() {
        earning2.setText("");
        expense2.setText("");
    }

    /**
     * Method for the population of the switches of the months,
     * drawing of the graph
     */
    @SuppressLint("SetTextI18n")
    private void populateMonths(){
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

        int j = 0;
        A_month = new ArrayList<>();
        A_monthcash = new ArrayList<>();
        boolean esit = false;
        Cursor cur = db.queryTransDist();
        if(cur.moveToFirst()) {
            do {
                String ins = cur.getString(cur.getColumnIndex("date"));
                ins = ins.substring(0,7);
                for(String i : A_month){
                    if(i.equals(ins)) {
                        esit = true;
                        break;
                    }
                    esit = false;
                }
                if(!esit) {
                    A_month.add(ins);
                    j++;
                }
            } while(cur.moveToNext() && j<6);
        }
        for(int i = A_month.size()-1, h = 0; i >= 0; i--,h++){
            A_textdate.get(h).setText(A_month.get(i));
        }
        double cash;
        for(int i = 0; i < A_month.size(); i++){
            cash = 0;
            cur = db.queryTransDate("date", A_month.get(i));
            if(cur.moveToFirst()){
                do{
                    cash += cur.getDouble(cur.getColumnIndex("money"));
                }while(cur.moveToNext());
            }
            A_monthcash.add(cash);
        }

        A_maxcash = new ArrayList<>();
        A_maxcash.addAll(A_monthcash);
        Collections.sort(A_maxcash, new Comparator<Double>() {
            @Override
            public int compare(Double aDouble, Double t1) {
                return t1.compareTo(aDouble);
            }
        });
        LinkedHashSet<Double> A_Linked = new LinkedHashSet<>(A_maxcash);
        A_maxcash = new ArrayList<>(A_Linked);

        int bottom = 90;
        RelativeLayout.LayoutParams params, params1, params2;
        for(int i = A_maxcash.size()-1, h=0; i >= 0; i--, h++){
            A_textcash.get(h).setText(A_maxcash.get(i).toString()+HomeActivity.currency);
            params = (RelativeLayout.LayoutParams) A_textcash.get(h).getLayoutParams();
            params.setMargins(0, 0, 0, bottom);
            A_textcash.get(h).setLayoutParams(params);
            bottom += 75;
        }

        for(int i = 0; i < A_monthcash.size(); i++){
            for(int c = 0; c < A_maxcash.size(); c++) {
                if(A_monthcash.get(i).equals(A_maxcash.get(c))){
                    params = (RelativeLayout.LayoutParams) A_textcash.get(A_maxcash.size()-c-1).getLayoutParams();
                    params1 = (RelativeLayout.LayoutParams) A_textdate.get(A_monthcash.size()-i-1).getLayoutParams();
                    params2 = (RelativeLayout.LayoutParams) A_circle.get(i).getLayoutParams();
                    int marginBottom = params.bottomMargin;
                    int startMargin = params1.getMarginStart()+25;
                    params2.setMargins(startMargin,0,0,marginBottom);
                    A_circle.get(i).setLayoutParams(params2);
                    A_circle.get(i).setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
        db.close();
    }

    /**
     * Method for calculation of the percentage
     */
    private void percent(){
        if(money1 != null && money2 != null){
            if(switchpercent.isChecked()){
                String strmoney;
                text_percentage.setText("Earnings\nPercentage");
                double money;
                if(moneypos2 == 0){
                    percentage.setText("0.0 %");
                }
                else if(moneypos1 == 0 && moneypos2 != 0){
                    strmoney = moneypos2 +" %";
                    percentage.setText(strmoney);
                }
                else if(moneypos2.equals(moneypos1)){
                    percentage.setText("0.0 %");
                }
                else {
                    DecimalFormat bigd = new DecimalFormat("#.0");
                    money = moneypos2 / moneypos1;
                    money *= 100;
                    money = money - 100;
                    strmoney = bigd.format(money) + " %";
                    percentage.setText(strmoney);
                }
            }
            else{
                String strmoney;
                text_percentage.setText("Expenses\nPercentage");
                double money;
                if(moneyneg2 == 0){
                    percentage.setText("0.0 %");
                }
                else if(moneyneg1 == 0 && moneyneg2 != 0){
                    strmoney = moneyneg2 +" %";
                    percentage.setText(strmoney);
                }
                else if(moneyneg2.equals(moneyneg1)){
                    percentage.setText("0.0 %");
                }
                else {
                    DecimalFormat bigd = new DecimalFormat("#.0");
                    money = moneyneg2 / moneyneg1;
                    money *= 100;
                    money = money - 100;
                    strmoney = bigd.format(money) + " %";
                    percentage.setText(strmoney);
                }
            }
        }
    }

    /**
     * Method for calculation of earnings and expenses for first month
     */
    @SuppressLint("SetTextI18n")
    private void populateTrans() {
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

        moneypos1 = 0.0; moneyneg1 = 0.0;
        Cursor cur = db.queryTransDate("date", selected);
        if(cur.moveToFirst()){
            do{
                money1 = cur.getDouble(cur.getColumnIndex("money"));
                if(money1 > 0)
                    moneypos1 += money1;
                else {
                    money1 = Math.abs(money1);
                    moneyneg1 += money1;
                }
            }while(cur.moveToNext());
        }
        earning1.setText(moneypos1.toString()+" "+HomeActivity.currency);
        expense1.setText(moneyneg1.toString()+" "+HomeActivity.currency);
        money1 = moneypos1 - moneyneg1;
        db.close();
    }

    /**
     * Method for calculation of earnings and expenses for second month
     */
    @SuppressLint("SetTextI18n")
    private void populateTrans2(){
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

        moneypos2 = 0.0; moneyneg2 = 0.0;
        Cursor cur = db.queryTransDate("date", selected2);
        if(cur.moveToFirst()){
            do{
                money2 = cur.getDouble(cur.getColumnIndex("money"));
                if(money2 > 0)
                    moneypos2 += money2;
                else {
                    money2 = Math.abs(money2);
                    moneyneg2 += money2;
                }
            }while(cur.moveToNext());
        }
        earning2.setText(moneypos2.toString()+" "+HomeActivity.currency);
        expense2.setText(moneyneg2.toString()+" "+HomeActivity.currency);
        money2 = moneypos2 - moneyneg2;
        db.close();
    }

    /**
     * Method that accesses the database and takes name and surname
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
     * Method for the population of the first switch
     */
    private void populateFundMenu() {
        DatabaseBeReader db = new DatabaseBeReader(this);
        fund_list = new ArrayList<>();
        boolean esit = false;
        A_switchdate = new ArrayList<>();

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

        Cursor cur = db.queryTransDist();
        if(cur.moveToFirst()) {
            do {
                String ins = cur.getString(cur.getColumnIndex("date"));
                ins = ins.substring(0,7);
                for(String i : A_switchdate){
                    if(i.equals(ins)) {
                        esit = true;
                        break;
                    }
                    esit = false;
                }
                if(!esit) {
                    fund_list.add(ins);
                    A_switchdate.add(ins);
                }
            } while(cur.moveToNext());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, fund_list);
        fund_switch1.setAdapter(adapter);

        if(!fund_list.isEmpty()) {
            fund_switch1.setText(fund_list.get(0), false);
            selected = fund_list.get(0);
            month1.setText(selected);
        }
        db.close();
    }

    /**
     * Method for the population of the second switch
     */
    private void populateFundMenu2() {
        DatabaseBeReader db = new DatabaseBeReader(this);
        fund_list2 = new ArrayList<>();
        boolean esit = false;
        A_switchdate = new ArrayList<>();

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

        Cursor cur = db.queryTransDist();
        if(cur.moveToFirst()) {
            do {
                String ins = cur.getString(cur.getColumnIndex("date"));
                ins = ins.substring(0,7);
                for(String i : A_switchdate){
                    if(i.equals(ins)) {
                        esit = true;
                        break;
                    }
                    esit = false;
                }
                if(!esit) {
                    fund_list2.add(ins);
                    A_switchdate.add(ins);
                }
            } while(cur.moveToNext());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, fund_list2);
        fund_switch2.setAdapter(adapter);

        if(!fund_list2.isEmpty()) {
            fund_switch2.setText(fund_list2.get(0), false);
            selected2 = fund_list2.get(0);
            month2.setText(selected2);
        }
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
