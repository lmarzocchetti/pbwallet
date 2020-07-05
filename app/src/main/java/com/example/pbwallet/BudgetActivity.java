package com.example.pbwallet;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;
import java.util.List;

/** Class that prints the BudgetActivity */
public class BudgetActivity extends AppCompatActivity {
    BottomNavigationView navbar;
    String nameType, nameSubtype, date;
    FloatingActionButton add_budget;
    Double cash, bound;
    CustomAdapter1 adapter1;

    /** This method initializes everything
     * @param savedInstanceState saved state for create this activity, in this application is NULL
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        populatedallbudget();

        navbar = findViewById(R.id.nav_bar);
        navbar.setSelectedItemId(R.id.nav_budget);
        navbar.setOnNavigationItemSelectedListener(navigationlistener);

        add_budget = findViewById(R.id.add_budget_button);
        add_budget.setOnClickListener(button_listener);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populatedallbudget();
        navbar.setSelectedItemId(R.id.nav_budget);
    }

    /** This method is a listener of the button that start AddBudgetActivity class
     * @see AddBudgetActivity
     */
    private FloatingActionButton.OnClickListener button_listener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onPause();
                    startActivity(new Intent(getApplicationContext(), AddBudgetActivity.class));
                    onStop();
                }
            };

    /** This method is a listener of the navigation bar that change activities
     *  @see HomeActivity
     *  @see FundsActivity
     *  @see StatsActivity
     *  @see AddTransactionActivity
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
                            startActivity(new Intent(getApplicationContext(), FundsActivity.class));
                            overridePendingTransition(0, 0);
                            onStop();
                            finish();
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
                            break;
                    }
                    return true;
                }
            };

    /** This method populates the screen with all the budgets currently in the database*/
    private void populatedallbudget(){
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();
        Cursor cur = db.querySubtypeByBudget();
        Cursor cur2 = db.queryBudgetAsc();
        ListView listview = findViewById(R.id.listview1);
        List<ElementoLista1> list = new LinkedList<>();
        if(cur.moveToFirst() && cur2.moveToFirst()){
            do {
                nameSubtype = cur.getString(cur.getColumnIndex("name"));
                cash = cur2.getDouble(cur2.getColumnIndex("money"));
                bound = cur2.getDouble(cur2.getColumnIndex("bound"));
                date = cur2.getString(cur2.getColumnIndex("date"));
                int ciao = cur.getInt(cur.getColumnIndex("idtype"));
                Cursor cur1 = db.queryTypeBySubtype(ciao);
                if(cur1.moveToFirst()){
                    nameType = cur1.getString(cur1.getColumnIndex("name"));
                }
                list.add(new ElementoLista1(nameType,nameSubtype,cash.toString(),bound.toString(), date));
            }while(cur.moveToNext() && cur2.moveToNext());
        }
        adapter1 = new CustomAdapter1(this, R.layout.relativelayout1, list);
        listview.setAdapter(adapter1);
        db.close();
    }
}
