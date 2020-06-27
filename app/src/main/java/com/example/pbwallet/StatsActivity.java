package com.example.pbwallet;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StatsActivity extends AppCompatActivity {
    TextView name, mese1, mese2, entrata1, entrata2, uscita1, uscita2, percentuale1, percentuale2;
    AutoCompleteTextView fund_switch1,fund_switch2;

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
        percentuale1 = findViewById(R.id.percentuale1);
        percentuale2 = findViewById(R.id.percentuale2);
        fund_switch1 = findViewById(R.id.fund_menu1);
        fund_switch2 = findViewById(R.id.fund_menu2);
        changeNameandSur();

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
