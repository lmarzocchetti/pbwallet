package com.example.pbwallet;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    TextView name, cash, table1, table2, table3, table4, table5;
    ImageView bar1,bar2,bar3,bar4;
    ArrayList<TextView> aT;
    ArrayList<ImageView> aI;

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
        aT = new ArrayList<TextView>();
        aT.add(table1);
        aT.add(table2);
        aT.add(table3);
        aT.add(table4);
        aT.add(table5);
        bar1 = findViewById(R.id.bar1);
        bar2 = findViewById(R.id.bar2);
        bar3 = findViewById(R.id.bar3);
        bar4 = findViewById(R.id.bar4);
        aI = new ArrayList<ImageView>();
        aI.add(bar1);
        aI.add(bar2);
        aI.add(bar3);
        aI.add(bar4);
        bar1.setVisibility(View.INVISIBLE);
        bar2.setVisibility(View.INVISIBLE);
        bar3.setVisibility(View.INVISIBLE);
        bar4.setVisibility(View.INVISIBLE);
        changeCash();
        changeNameandSur();
        changeLastTrans();

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
                            onPause();
                            startActivity(new Intent(getApplicationContext(),FundsActivity.class));
                            overridePendingTransition(0,0);
                            onStop();
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

    public void changeLastTrans(){
        int i = 0;
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();
        Cursor cur = db.queryLastTrans();
        if(cur.moveToFirst()){
            do{
                aT.get(i).setText(new Double(cur.getString(cur.getColumnIndex("money"))).toString() + " €");
                i++;
            }while(cur.moveToNext() && i < 5);
        }
        db.close();
        for(int j = 0; j < i-1; j++){
            aI.get(j).setVisibility(View.VISIBLE);
        }
    }

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
