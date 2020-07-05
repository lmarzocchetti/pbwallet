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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Activity for the creation of the statistics page, comparison between two months and
 * drawing of the graph of the last six months
 */
public class StatsActivity extends AppCompatActivity {
    TextView name, mese1, mese2, entrata1, entrata2, uscita1, uscita2, percentuale1, textpercent, date1, date2, date3, date4, date5, date6, cashdate1, cashdate2, cashdate3, cashdate4, cashdate5, cashdate6;
    AutoCompleteTextView fund_switch1,fund_switch2;
    ImageView circle1, circle2, circle3, circle4 , cirlce5, circle6;
    String selected, selected2;
    ArrayList<String> fund_list, fund_list2;
    ArrayList<String> As, Am;
    ArrayList<TextView> Atd, Acd;
    ArrayList<Double> Amc, Amaxc, Atestmax;
    ArrayList<ImageView> Acircle;
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
        setContentView(R.layout.stats);
        name = findViewById(R.id.nameandsurname);
        mese1 = findViewById(R.id.mese1);
        mese2 = findViewById(R.id.mese2);
        entrata1 = findViewById(R.id.entrata1);
        entrata2 = findViewById(R.id.entrata2);
        uscita1 = findViewById(R.id.uscita1);
        uscita2 = findViewById(R.id.uscita2);
        textpercent  = findViewById(R.id.textView5_1);
        percentuale1 = findViewById(R.id.percentuale1);
        fund_switch1 = findViewById(R.id.fund_menu2);
        fund_switch2 = findViewById(R.id.fund_menu1);
        switchpercent = findViewById(R.id.switchperc);
        Atd = new ArrayList<>();
        Acd = new ArrayList<>();
        Acircle = new ArrayList<>();
        Atd.add(date1 = findViewById(R.id.date1));
        Atd.add(date2 = findViewById(R.id.date2));
        Atd.add(date3 = findViewById(R.id.date3));
        Atd.add(date4 = findViewById(R.id.date4));
        Atd.add(date5 = findViewById(R.id.date5));
        Atd.add(date6 = findViewById(R.id.date6));
        Acd.add(cashdate1 = findViewById(R.id.cashdate1));
        Acd.add(cashdate2 = findViewById(R.id.cashdate2));
        Acd.add(cashdate3 = findViewById(R.id.cashdate3));
        Acd.add(cashdate4 = findViewById(R.id.cashdate4));
        Acd.add(cashdate5 = findViewById(R.id.cashdate5));
        Acd.add(cashdate6 = findViewById(R.id.cashdate6));
        Acircle.add(circle1 = findViewById(R.id.circle1));
        Acircle.add(circle2 = findViewById(R.id.circle2));
        Acircle.add(circle3 = findViewById(R.id.circle3));
        Acircle.add(circle4 = findViewById(R.id.circle4));
        Acircle.add(cirlce5 = findViewById(R.id.circle5));
        Acircle.add(circle6 = findViewById(R.id.circle6));
        for(ImageView i : Acircle){
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
                        mese1.setText(selected);
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
                        mese2.setText(selected2);
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
       entrata1.setText("");
       uscita1.setText("");
    }

    /**
     * Method for reset the TextView earnings and expenses for second month
     */
    private void resetTrans2() {
        entrata2.setText("");
        uscita2.setText("");
    }

    /**
     * Method for the population of the switches of the months,
     * drawing of the graph
     */
    @SuppressLint("SetTextI18n")
    private void populateMonths(){
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();
        int j = 0;
        Am = new ArrayList<>();
        Amc = new ArrayList<>();
        Atestmax = new ArrayList<>();
        boolean esit = false;
        Cursor cur = db.queryTransDist();
        if(cur.moveToFirst()) {
            do {
                String ins = cur.getString(cur.getColumnIndex("date"));
                ins = ins.substring(0,7);
                for(String i : Am){
                    if(i.equals(ins)) {
                        esit = true;
                        break;
                    }
                    esit = false;
                }
                if(!esit) {
                    Am.add(ins);
                    j++;
                }
            } while(cur.moveToNext() && j<6);
        }
        for(int i = Am.size()-1, h = 0; i >= 0; i--,h++){
            Atd.get(h).setText(Am.get(i));
        }
        double cash;
        for(int i = 0; i < Am.size(); i++){
            cash = 0;
            cur = db.queryTransDate("date", Am.get(i));
            if(cur.moveToFirst()){
                do{
                    cash += cur.getDouble(cur.getColumnIndex("money"));
                }while(cur.moveToNext());
            }
            Amc.add(cash);
        }
        Atestmax.addAll(Amc);
        Amaxc = new ArrayList<>();
        int indice = 0;
        Double max = 0.0;
        esit = false;
        for(int l = 0; l < Am.size(); l++) {
            for (int i = 0; i < Atestmax.size(); i++) {
                if (i == 0) {
                    max = Atestmax.get(i);
                    indice = i;
                } else {
                    if (max <= Atestmax.get(i)) {
                        max = Atestmax.get(i);
                        indice = i;
                    }
                }
            }
            for(int i = 0; i < Amaxc.size(); i++) {
                if(Amaxc.get(i).equals(max)){
                    esit = true;
                    break;
                }
            }
            if(!esit)
                Amaxc.add(max);
            esit = false;
            Atestmax.remove(indice);
        }

        int bottom = 90;
        RelativeLayout.LayoutParams params, params1, params2;
        for(int i = Amaxc.size()-1, h=0; i >= 0; i--, h++){
            Acd.get(h).setText(Amaxc.get(i).toString()+HomeActivity.currency);
            params = (RelativeLayout.LayoutParams) Acd.get(h).getLayoutParams();
            params.setMargins(0, 0, 0, bottom);
            Acd.get(h).setLayoutParams(params);
            bottom += 75;
        }

        for(int i = 0; i < Amc.size(); i++){
            for(int c = 0; c < Amaxc.size(); c++) {
                if(Amc.get(i).equals(Amaxc.get(c))){
                    params = (RelativeLayout.LayoutParams) Acd.get(Amaxc.size()-c-1).getLayoutParams();
                    params1 = (RelativeLayout.LayoutParams) Atd.get(Amc.size()-i-1).getLayoutParams();
                    params2 = (RelativeLayout.LayoutParams) Acircle.get(i).getLayoutParams();
                    int marginBottom = params.bottomMargin;
                    int startMargin = params1.getMarginStart()+25;
                    params2.setMargins(startMargin,0,0,marginBottom);
                    Acircle.get(i).setLayoutParams(params2);
                    Acircle.get(i).setVisibility(View.VISIBLE);
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
                textpercent.setText("Percentuale\nGuadagni");
                double money;
                if(moneypos2 == 0){
                    percentuale1.setText("0.0 %");
                }
                else if(moneypos1 == 0 && moneypos2 != 0){
                    strmoney = moneypos2 +" %";
                    percentuale1.setText(strmoney);
                }
                else if(moneypos2.equals(moneypos1)){
                    percentuale1.setText("0.0 %");
                }
                else {
                    DecimalFormat bigd = new DecimalFormat("#.0");
                    money = moneypos2 / moneypos1;
                    money *= 100;
                    money = money - 100;
                    strmoney = bigd.format(money) + " %";
                    percentuale1.setText(strmoney);
                }
            }
            else{
                String strmoney;
                textpercent.setText("Percentuale\nSpesi");
                double money;
                if(moneyneg2 == 0){
                    percentuale1.setText("0.0 %");
                }
                else if(moneyneg1 == 0 && moneyneg2 != 0){
                    strmoney = moneyneg2 +" %";
                    percentuale1.setText(strmoney);
                }
                else if(moneyneg2.equals(moneyneg1)){
                    percentuale1.setText("0.0 %");
                }
                else {
                    DecimalFormat bigd = new DecimalFormat("#.0");
                    money = moneyneg2 / moneyneg1;
                    money *= 100;
                    money = money - 100;
                    strmoney = bigd.format(money) + " %";
                    percentuale1.setText(strmoney);
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
        db.open();
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
        entrata1.setText(moneypos1.toString()+" "+HomeActivity.currency);
        uscita1.setText(moneyneg1.toString()+" "+HomeActivity.currency);
        money1 = moneypos1 - moneyneg1;
        db.close();
    }

    /**
     * Method for calculation of earnings and expenses for second month
     */
    @SuppressLint("SetTextI18n")
    private void populateTrans2(){
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();
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
        entrata2.setText(moneypos2.toString()+" "+HomeActivity.currency);
        uscita2.setText(moneyneg2.toString()+" "+HomeActivity.currency);
        money2 = moneypos2 - moneyneg2;
        db.close();
    }

    /**
     * Method that accesses the database and takes name and surname
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
     * Method for the population of the first switch
     */
    private void populateFundMenu() {
        DatabaseBeReader db = new DatabaseBeReader(this);
        fund_list = new ArrayList<>();
        boolean esit = false;
        As = new ArrayList<>();
        db.open();

        Cursor cur = db.queryTransDist();
        if(cur.moveToFirst()) {
            do {
                String ins = cur.getString(cur.getColumnIndex("date"));
                ins = ins.substring(0,7);
                for(String i : As){
                    if(i.equals(ins)) {
                        esit = true;
                        break;
                    }
                    esit = false;
                }
                if(!esit) {
                    fund_list.add(ins);
                    As.add(ins);
                }
            } while(cur.moveToNext());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, fund_list);
        fund_switch1.setAdapter(adapter);

        if(!fund_list.isEmpty()) {
            fund_switch1.setText(fund_list.get(0), false);
            selected = fund_list.get(0);
            mese1.setText(selected);
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
        As = new ArrayList<>();
        db.open();

        Cursor cur = db.queryTransDist();
        if(cur.moveToFirst()) {
            do {
                String ins = cur.getString(cur.getColumnIndex("date"));
                ins = ins.substring(0,7);
                for(String i : As){
                    if(i.equals(ins)) {
                        esit = true;
                        break;
                    }
                    esit = false;
                }
                if(!esit) {
                    fund_list2.add(ins);
                    As.add(ins);
                }
            } while(cur.moveToNext());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, fund_list2);
        fund_switch2.setAdapter(adapter);

        if(!fund_list2.isEmpty()) {
            fund_switch2.setText(fund_list2.get(0), false);
            selected2 = fund_list2.get(0);
            mese2.setText(selected2);
        }
        db.close();
    }
}
