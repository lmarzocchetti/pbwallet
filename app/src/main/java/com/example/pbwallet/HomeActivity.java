package com.example.pbwallet;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    TextView name, cash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        name = findViewById(R.id.nameandsurname);
        cash = findViewById(R.id.totalfunds);
        changeCash();
        changeNameandSur();

        BottomNavigationView navbar = findViewById(R.id.nav_bar);
        navbar.setSelectedItemId(R.id.nav_home);
        navbar.setOnNavigationItemSelectedListener(navigationlistener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationlistener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch(item.getItemId()) {
                        case R.id.nav_home:
                            break;

                        case R.id.nav_fund:
                            startActivity(new Intent(getApplicationContext(),FundsActivity.class));
                            overridePendingTransition(0,0);
                            break;

                        case R.id.nav_stats:
                            startActivity(new Intent(getApplicationContext(),StatsActivity.class));
                            overridePendingTransition(0,0);
                            break;
                    }
                    return true;
                }
            };

    public void changeCash(){
        double totalcash = 0;
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();
        Cursor cur = db.queryCardFull();
        if(cur.moveToFirst()) {
            do{
                totalcash += cur.getDouble(cur.getColumnIndex("money"));
            }while(cur.moveToNext());
        }
        db.close();
        String strcash = new Double(totalcash).toString()+" €";
        cash.setText(strcash);
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
}
