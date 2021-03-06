package com.example.pbwallet;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

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
    BudgetAdapter budgetadapter;

    /** This method initializes everything
     * @param savedInstanceState saved state for create this activity, in this application is NULL
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        populateAllBudget();

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
        populateAllBudget();
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
    private void populateAllBudget(){
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

        Cursor cur = db.querySubtypeByBudget();
        Cursor cur2 = db.queryBudgetAsc();
        ListView listview = findViewById(R.id.listview1);
        List<BudgetElement> list = new LinkedList<>();
        if(cur.moveToFirst() && cur2.moveToFirst()){
            do {
                nameSubtype = cur.getString(cur.getColumnIndex("name"));
                cash = cur2.getDouble(cur2.getColumnIndex("money"));
                bound = cur2.getDouble(cur2.getColumnIndex("bound"));
                date = cur2.getString(cur2.getColumnIndex("date"));
                int idtype = cur.getInt(cur.getColumnIndex("idtype"));
                Cursor cur1 = db.queryTypeBySubtype(idtype);
                if(cur1.moveToFirst()){
                    nameType = cur1.getString(cur1.getColumnIndex("name"));
                }
                list.add(new BudgetElement(nameType,nameSubtype,cash.toString(),bound.toString(), date));
            }while(cur.moveToNext() && cur2.moveToNext());
        }
        budgetadapter = new BudgetAdapter(this, R.layout.relativelayout_budget, list);
        listview.setAdapter(budgetadapter);
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
