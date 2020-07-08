package com.example.pbwallet;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

/**
 * Activity for put a new wallet with a certain amount of money and a Name
 */
public class AddWalletActivity extends AppCompatActivity {
    TextInputEditText new_name, money_int, money_dec;
    Button confirm;
    TextView moneySimbol;

    /**
     * Initialize attributes from this class, set their own listener and call methods to populate them
     * @param savedInstanceState saved state for create this activity, in this application is NULL
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addwallet);

        new_name = findViewById(R.id.new_walletTF);
        money_int = findViewById(R.id.money_intTFNEW);
        money_dec = findViewById(R.id.money_decTFNEW);
        confirm = findViewById(R.id.confirm_new_wallet);
        confirm.setOnClickListener(confirm_listener);
        moneySimbol = findViewById(R.id.money_symbolNEW);
        moneySimbol.setText(HomeActivity.currency);
    }

    /**
     * Add a new Wallet(Card) to the Database
     */
    private void addNewWallet() {
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

        try {
            db.insertCard(getNewWalletID(), Objects.requireNonNull(new_name.getText()).toString(), mergeMoney());
        } catch (SQLiteException e) {
            Log.d("pbwallet", "Exception: " + Log.getStackTraceString(e));
            try {
                db.insertCard(getNewWalletID(), Objects.requireNonNull(new_name.getText()).toString(), mergeMoney());
            } catch (SQLiteException e_1) {
                Log.d("pbwallet", "Exception: " + Log.getStackTraceString(e_1));
                databaseError();
                finish();
            }
        }

        db.close();
    }

    /**
     * @see AddTransactionActivity
     * @return integer and decimal part fuse together
     */
    private double mergeMoney() {
        double ris;

        String moneyint = Objects.requireNonNull(money_int.getText()).toString();
        String moneydec = Objects.requireNonNull(money_dec.getText()).toString();

        String last = moneyint+"."+moneydec;

        ris = Double.parseDouble(last);

        return ris;
    }

    /**
     * Take the last CardID and increment it by 1
     * @return new CardID for insert a new Card in the Database
     */
    private int getNewWalletID() {
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();

        int newID = -1;
        Cursor cur = db.queryCardbyID();

        if(cur.moveToFirst()) {
            newID = cur.getInt(cur.getColumnIndex("idcard"));
        }

        ++newID;

        db.close();
        return newID;
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

    /**
     * Listener for the confirm button, if the condition is checked then
     * add a new Wallet(Card) in the Database and finish this activity
     */
    private Button.OnClickListener confirm_listener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!Objects.requireNonNull(new_name.getText()).toString().isEmpty() && !Objects.requireNonNull(money_dec.getText()).toString().isEmpty() && !Objects.requireNonNull(money_int.getText()).toString().isEmpty()) {
                        addNewWallet();
                        finish();
                    }
                }
            };
}
