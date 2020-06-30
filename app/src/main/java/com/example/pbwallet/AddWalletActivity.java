package com.example.pbwallet;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class AddWalletActivity extends AppCompatActivity {
    TextInputEditText new_name, money_int, money_dec;
    Button confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addwallet);

        new_name = findViewById(R.id.new_walletTF);
        money_int = findViewById(R.id.money_intTFNEW);
        money_dec = findViewById(R.id.money_decTFNEW);
        confirm = findViewById(R.id.confirm_new_wallet);
        confirm.setOnClickListener(confirm_listener);
    }

    private void addNewWallet() {
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();

        db.insertCard(getNewWalletID(), Objects.requireNonNull(new_name.getText()).toString(), mergeMoney());

        db.close();
    }

    private double mergeMoney() {
        double ris;

        String moneyint = Objects.requireNonNull(money_int.getText()).toString();
        String moneydec = Objects.requireNonNull(money_dec.getText()).toString();

        String last = moneyint+"."+moneydec;

        ris = Double.parseDouble(last);

        return ris;
    }

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
