package com.example.pbwallet;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

public class AddTransactionActivity extends AppCompatActivity {
    String selected_w, selected_subT, selected_type;
    ArrayList<String> wallet_list, subtype_list, type_list;
    DatabaseBeReader db;
    TextView reasonTV;
    SwitchMaterial add_reason;
    Button conf_exist, conf_notExist;
    TextInputEditText money_int, money_dec, new_reason;
    AutoCompleteTextView wallet_menu, subtype_menu, type_menu;
    TextInputLayout new_reasonLayout, type_menuLayout, subtypeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtransaction);
        BottomNavigationView navbar = findViewById(R.id.nav_bar);
        navbar.setSelectedItemId(R.id.add_transaction);
        navbar.setOnNavigationItemSelectedListener(navigationlistener);

        reasonTV = findViewById(R.id.text_add_reason);
        add_reason = findViewById(R.id.add_reason);
        conf_exist = findViewById(R.id.confirm_button_existing);
        conf_notExist = findViewById(R.id.confirm_button_not_existing);
        money_int = findViewById(R.id.money_intTF);
        money_dec = findViewById(R.id.money_decTF);
        new_reason = findViewById(R.id.new_reason_layoutTF);
        wallet_menu = findViewById(R.id.wallet_menu);
        subtype_menu = findViewById(R.id.subtype_menu);
        type_menu = findViewById(R.id.list_type_menu);
        new_reasonLayout = findViewById(R.id.new_reason_layout);
        type_menuLayout = findViewById(R.id.list_type);
        subtypeLayout = findViewById(R.id.layout_subtype);

        selected_w = "";
        selected_subT = "";
        selected_type = "";

        populateWalletMenu();
        wallet_menu.setOnItemClickListener(wallet_listener);

        populateSubtypeMenu();
        subtype_menu.setOnItemClickListener(subtype_listener);

        populateTypeMenu();
        type_menu.setOnItemClickListener(type_listener);

        conf_exist.setOnClickListener(btn_ex_listener);

        add_reason.setOnClickListener(switch_listener);

        conf_notExist.setOnClickListener(btn_not_ex_listener);
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
                            onPause();
                            startActivity(new Intent(getApplicationContext(),StatsActivity.class));
                            overridePendingTransition(0,0);
                            onStop();
                            break;

                        case R.id.add_transaction:
                            break;
                    }
                    return true;
                }
            };

    private void populateWalletMenu() {
        db = new DatabaseBeReader(this);
        wallet_list = new ArrayList<>();
        db.open();

        Cursor cur = db.queryCardFull();

        if(cur.moveToFirst()) {
            do {
                String ins = cur.getString(cur.getColumnIndex("uscard"));
                wallet_list.add(ins);
            } while(cur.moveToNext());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, wallet_list);
        wallet_menu.setAdapter(adapter);

        db.close();
    }

    private void populateSubtypeMenu() {
        db = new DatabaseBeReader(this);
        subtype_list = new ArrayList<>();
        db.open();

        Cursor cur = db.queryOnlySubType();

        if(cur.moveToFirst()) {
            do {
                String ins = cur.getString(cur.getColumnIndex("name"));
                subtype_list.add(ins);
            } while(cur.moveToNext());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, subtype_list);
        subtype_menu.setAdapter(adapter);

        db.close();
    }

    private void populateTypeMenu() {
        db = new DatabaseBeReader(this);
        type_list = new ArrayList<>();
        db.open();

        Cursor cur = db.queryTypeFull();

        if(cur.moveToFirst()) {
            do {
                String ins = cur.getString(cur.getColumnIndex("name"));
                type_list.add(ins);
            } while(cur.moveToNext());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, type_list);
        type_menu.setAdapter(adapter);

        db.close();
    }

    private int getNewTransID() {
        db = new DatabaseBeReader(this);
        db.open();

        Cursor cur = db.queryTransbyID();
        int ins = -1;

        if(cur.moveToFirst()) {
            ins = cur.getInt(cur.getColumnIndex("idtrans"));
        }

        ++ins;

        db.close();
        return ins;
    }

    private void insertNewTrans() {
        int idcard = 0, idsubtype = 0;
        double cur_money = 0;

        db = new DatabaseBeReader(this);
        db.open();

        Cursor cur = db.queryCard("uscard", selected_w);
        if(cur.moveToFirst()) {
            idcard = cur.getInt(cur.getColumnIndex("idcard"));
            cur_money = cur.getDouble(cur.getColumnIndex("money"));
        }

        cur = db.querySubtype("name", selected_subT);
        if(cur.moveToFirst()) {
            idsubtype = cur.getInt(cur.getColumnIndex("idsubtype"));
        }

        double money = mergeMoney();

        db.insertTrans(getNewTransID(), idcard, idsubtype, money, java.time.LocalDate.now().toString());

        cur_money = cur_money + money;
        db.close();

        updateMoney(idcard, cur_money);
    }

    private void insertNewTransandSubtype() {
        insertNewSubtype();
        insertNewTrans();
    }

    private void insertNewSubtype() {
        db = new DatabaseBeReader(this);
        db.open();

        selected_subT = Objects.requireNonNull(new_reason.getText()).toString();

        db.insertSubtype(getNewSubtypeID(), getTypebyName(), selected_subT);

        db.close();
    }

    private int getTypebyName() {
        db = new DatabaseBeReader(this);
        db.open();

        int ins = -1;
        Cursor cur = db.queryType("name", selected_type);

        if(cur.moveToFirst()) {
            ins = cur.getInt(cur.getColumnIndex("idtype"));
        }

        db.close();
        return ins;
    }

    private int getNewSubtypeID() {
        db = new DatabaseBeReader(this);
        db.open();

        int ins = -1;
        Cursor cur = db.querySubTypebyID();

        if(cur.moveToFirst()) {
            ins = cur.getInt(cur.getColumnIndex("idsubtype"));
        }

        ++ins;

        db.close();
        return ins;
    }

    private void updateMoney(int idcard, double newmoney) {
        db = new DatabaseBeReader(this);
        db.open();
        db.updateMoneyCard(newmoney, idcard);
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

    private AdapterView.OnItemClickListener wallet_listener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selected_w = wallet_list.get(i);
                }
            };

    private AdapterView.OnItemClickListener subtype_listener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selected_subT = subtype_list.get(i);
                }
            };

    private AdapterView.OnItemClickListener type_listener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selected_type = type_list.get(i);
                }
            };

    private Button.OnClickListener btn_ex_listener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!add_reason.isChecked() && !selected_w.isEmpty() && !selected_subT.isEmpty() && !Objects.requireNonNull(money_int.getText()).toString().isEmpty() && !Objects.requireNonNull(money_dec.getText()).toString().isEmpty()) {
                        insertNewTrans();
                        finish();
                    }
                }
            };

    private Button.OnClickListener btn_not_ex_listener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(add_reason.isChecked() && !selected_w.isEmpty() && !selected_type.isEmpty() && !Objects.requireNonNull(new_reason.getText()).toString().isEmpty() && !Objects.requireNonNull(money_int.getText()).toString().isEmpty() && !Objects.requireNonNull(money_dec.getText()).toString().isEmpty()) {
                        insertNewTransandSubtype();
                        finish();
                    }
                }
            };

    private SwitchMaterial.OnClickListener switch_listener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(add_reason.isChecked()) {
                        conf_exist.setVisibility(View.INVISIBLE);
                        subtypeLayout.setVisibility(View.INVISIBLE);
                        reasonTV.setVisibility(View.VISIBLE);
                        new_reasonLayout.setVisibility(View.VISIBLE);
                        type_menuLayout.setVisibility(View.VISIBLE);
                        conf_notExist.setVisibility(View.VISIBLE);
                    }
                    else {
                        conf_exist.setVisibility(View.VISIBLE);
                        subtypeLayout.setVisibility(View.VISIBLE);
                        reasonTV.setVisibility(View.INVISIBLE);
                        new_reasonLayout.setVisibility(View.INVISIBLE);
                        type_menuLayout.setVisibility(View.INVISIBLE);
                        conf_notExist.setVisibility(View.INVISIBLE);
                    }
                }
            };
}
