package com.example.pbwallet;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    TextView name, cash, table1, table2, table3, table4, table5, table1_1, table2_1, table3_1, table4_1, table5_1, table1_2, table2_2, table3_2, table4_2, table5_2;
    ImageView bar1,bar2,bar3,bar4;
    ArrayList<TextView> aT, aT1, aT2;
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
        aT = new ArrayList<TextView>();
        aT.add(table1);
        aT.add(table2);
        aT.add(table3);
        aT.add(table4);
        aT.add(table5);
        aT1 = new ArrayList<TextView>();
        aT1.add(table1_1);
        aT1.add(table2_1);
        aT1.add(table3_1);
        aT1.add(table4_1);
        aT1.add(table5_1);
        aT2 = new ArrayList<TextView>();
        aT2.add(table1_2);
        aT2.add(table2_2);
        aT2.add(table3_2);
        aT2.add(table4_2);
        aT2.add(table5_2);
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

                        case R.id.add_transaction:
                            onPause();
                            startActivity(new Intent(getApplicationContext(), AddTransactionActivity.class));
                            onStop();
                            break;
                    }
                    return true;
                }
            };

    public void changeLastTrans(){
        int i = 0;
        Double totaltrans;
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();
        Cursor cur = db.queryLastTrans();
        if(cur.moveToFirst()){
            do{
                totaltrans = new Double(cur.getString(cur.getColumnIndex("money")));
                if(totaltrans > 0){
                    aT.get(i).setTextColor(ContextCompat.getColor(this, R.color.verde_cash));
                }
                else{
                    totaltrans = Math.abs(totaltrans);
                    aT.get(i).setTextColor(ContextCompat.getColor(this, R.color.rosso_bordeaux));
                }
                aT.get(i).setText(totaltrans.toString() + " €");
                i++;
            }while(cur.moveToNext() && i < 5);
        }
        cur = db.queryUsCard();
        i = 0;
        if(cur.moveToFirst()){
            do{
                aT1.get(i).setText(cur.getString(cur.getColumnIndex("uscard")));
                i++;
            }while(cur.moveToNext() && i < 5);
        }
        for(int j = 0; j < i-1; j++){
            aI.get(j).setVisibility(View.VISIBLE);
        }
        i = 0;
        cur = db.querySubtypeFull();
        if(cur.moveToFirst()){
            do{
                aT2.get(i).setText(cur.getString(cur.getColumnIndex("name")));
                i++;
            }while(cur.moveToNext() && i < 5);
        }
        db.close();
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
