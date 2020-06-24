package com.example.pbwallet;

import android.content.Intent;
import android.database.Cursor;
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
    TextView name;
    AutoCompleteTextView fund_switch;
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

        fund_switch = findViewById(R.id.fund_menu);
        populateFundMenu(fund_switch);
    }

    private void populateFundMenu(AutoCompleteTextView fund_switch) {
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

        fund_switch.setText(fund_list.get(0), false);

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
