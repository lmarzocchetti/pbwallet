package com.example.pbwallet;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.nio.DoubleBuffer;
import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {
    TextView name, mese1, mese2, entrata1, entrata2, uscita1, uscita2, percentuale1, percentuale2, textpercent;
    AutoCompleteTextView fund_switch1,fund_switch2;
    String selected, selected2;
    ArrayList<String> fund_list, fund_list2;
    ArrayList<String> As;
    SwitchMaterial switchpercent;
    Double money1, money2;
    Double moneypos1, moneyneg1, moneypos2, moneyneg2;

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
        percentuale2 = findViewById(R.id.percentuale2);
        fund_switch1 = findViewById(R.id.fund_menu2);
        fund_switch2 = findViewById(R.id.fund_menu1);
        switchpercent = findViewById(R.id.switchperc);
        money1 = null;
        money2 = null;
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

        BottomNavigationView navbar = findViewById(R.id.nav_bar);
        navbar.setSelectedItemId(R.id.nav_stats);
        navbar.setOnNavigationItemSelectedListener(navigationlistener);
    }

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
                            break;

                        case R.id.nav_fund:
                            onPause();
                            startActivity(new Intent(getApplicationContext(),FundsActivity.class));
                            overridePendingTransition(0,0);
                            onStop();
                            break;

                        case R.id.nav_stats:
                            break;

                        case R.id.add_transaction:
                            onPause();
                            startActivity(new Intent(getApplicationContext(), AddTransactionActivity.class));
                            overridePendingTransition(0, 0);
                            onStop();
                            break;
                    }
                    return true;
                }
            };

    private SwitchMaterial.OnCheckedChangeListener listener_switch =
            new SwitchMaterial.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b)
                        percent();
                    else
                        percent();
                }
            };


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

    private void resetTrans() {
       entrata1.setText("");
       uscita1.setText("");
    }

    private void resetTrans2() {
        entrata2.setText("");
        uscita2.setText("");
    }

    private void percent(){
        if(money1 != null && money2 != null){
            if(switchpercent.isChecked()){
                textpercent.setText("Guadagni");
                double money = moneypos1/moneypos2;
                money*=100;
                money = 100-money;
                percentuale1.setText(new Double(money).toString()+" %");
                money = moneypos2/moneypos1;
                money*=100;
                money = 100-money;
                percentuale2.setText(new Double(money).toString()+" %");
            }
            else{
                textpercent.setText("Spesi");
                double money = moneyneg1/moneyneg2;
                money*=100;
                money = 100-money;
                percentuale1.setText(new Double(money).toString()+" %");
                money = moneyneg2/moneyneg1;
                money*=100;
                money = 100-money;
                percentuale2.setText(new Double(money).toString()+" %");
            }
        }
    }

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
        entrata1.setText(new Double(moneypos1).toString()+" €");
        uscita1.setText(new Double(moneyneg1).toString()+" €");
        money1 = moneypos1 - moneyneg1;
        db.close();
    }

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
        entrata2.setText(new Double(moneypos2).toString()+" €");
        uscita2.setText(new Double(moneyneg2).toString()+" €");
        money2 = moneypos2 - moneyneg2;
        db.close();
    }


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

    private void populateFundMenu() {
        /*
           cursor cur = db.queryCardFull;
           if(cur.moveToFirst) {
                do {
                    String s = cur.getString(cur.getColumnIndex("uscard"));
                } while (cur.moveToNext);
           }
         */
        DatabaseBeReader db = new DatabaseBeReader(this);
        fund_list = new ArrayList<>();
        boolean esit = false;
        As = new ArrayList<String>();
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

    private void populateFundMenu2() {
        /*
           cursor cur = db.queryCardFull;
           if(cur.moveToFirst) {
                do {
                    String s = cur.getString(cur.getColumnIndex("uscard"));
                } while (cur.moveToNext);
           }
         */
        DatabaseBeReader db = new DatabaseBeReader(this);
        fund_list2 = new ArrayList<>();
        boolean esit = false;
        As = new ArrayList<String>();
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
