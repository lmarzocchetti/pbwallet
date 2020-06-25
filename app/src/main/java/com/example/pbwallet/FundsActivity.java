package com.example.pbwallet;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class FundsActivity extends AppCompatActivity {
    ArrayList<String> fund_list;
    ArrayList<TextView> type, trans;
    TextView name;
    TextView type1, type2, type3, type4, type5;
    TextView trans1, trans2, trans3, trans4, trans5;
    AutoCompleteTextView fund_switch;
    String selected;
    DatabaseBeReader db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.funds);

        name = findViewById(R.id.nameandsurnameFUNDS);
        changeNameandSur();

        BottomNavigationView navbar = findViewById(R.id.nav_bar);
        navbar.setSelectedItemId(R.id.nav_fund);
        navbar.setOnNavigationItemSelectedListener(navigationlistener);

        selected = "";

        type = new ArrayList<TextView>();
        trans = new ArrayList<TextView>();

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

        fund_switch = findViewById(R.id.fund_menu);
        populateFundMenu();
        if(!selected.isEmpty()) {
            populateLastTrans();
        }
    }

    private void populateLastTrans() {
        db = new DatabaseBeReader(this);
        db.open();
        int idselcard;

        Cursor cur = db.queryCard("uscard", selected);

        if(cur.moveToFirst()) {
            idselcard = cur.getInt(cur.getColumnIndex("idcard"));
        }
        else {
            db.close();
            return;
        }

        int i = 0, idtrans;
        double sos;
        String sottotipo;
        cur = db.queryLastTransCard(idselcard);
        Cursor cur1;
        if(cur.moveToFirst()) {
            do {
                idtrans = cur.getInt(cur.getColumnIndex("idtrans"));
                sos = cur.getInt(cur.getColumnIndex("money"));
                cur1 = db.querySubtype(idtrans);
                if(cur1.moveToFirst()) {
                    sottotipo = cur1.getString(cur1.getColumnIndex("name"));
                    type.get(i).setText(sottotipo);
                }
                if(sos < 0) {
                    trans.get(i).setTextColor(Color.RED);
                    sos = Math.abs(sos);
                    trans.get(i).setText(Double.toString(sos));
                }
                else {
                    trans.get(i).setTextColor(Color.GREEN);
                    trans.get(i).setText(Double.toString(sos));
                }
                ++i;
            } while(cur.moveToNext() && i < 5);
        }
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, fund_list);
        fund_switch.setAdapter(adapter);

        if(!fund_list.isEmpty()) {
            fund_switch.setText(fund_list.get(0), false);
            selected = fund_list.get(0);
        }
        db.close();
    }

    public void changeNameandSur(){
        String nameandsur = null;
        db = new DatabaseBeReader(this);
        db.open();
        Cursor cur = db.queryUserFull();
        if(cur.moveToFirst())
            nameandsur = cur.getString(cur.getColumnIndex("name"))+" "+cur.getString(cur.getColumnIndex("surname"));
        name.setText(nameandsur);
        db.close();
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
                            break;

                        case R.id.nav_stats:
                            onPause();
                            startActivity(new Intent(getApplicationContext(),StatsActivity.class));
                            overridePendingTransition(0,0);
                            onStop();
                            break;
                    }
                    return true;
                }
            };
}
