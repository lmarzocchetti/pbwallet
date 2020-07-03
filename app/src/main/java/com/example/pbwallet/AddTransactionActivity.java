package com.example.pbwallet;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Activity for put a new transaction respect to a wallet with a particular, existing or not existing, subtype
 */
public class AddTransactionActivity extends AppCompatActivity {
    String selected_w, selected_subT, selected_type;
    ArrayList<String> wallet_list, subtype_list, type_list;
    TextView reasonTV, moneySimbol;
    SwitchMaterial add_reason;
    Button conf_exist, conf_notExist;
    TextInputEditText money_int, money_dec, new_reason;
    AutoCompleteTextView wallet_menu, subtype_menu, type_menu;
    TextInputLayout new_reasonLayout, type_menuLayout, subtypeLayout;

    /**
     * Initialize attributes from this class, set their own listener and call methods to populate them
     * @param savedInstanceState saved state for create this activity, in this application is NULL
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtransaction);

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
        moneySimbol = findViewById(R.id.money_symbol);

        selected_w = "";
        selected_subT = "";
        selected_type = "";
        moneySimbol.setText(HomeActivity.currency);

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

    /**
     * populate the wallets menu, which display all wallet in the Database
     */
    private void populateWalletMenu() {
        DatabaseBeReader db = new DatabaseBeReader(this);
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

    /**
     * Populate the subtype menu, which display all subtype in the Database
     */
    private void populateSubtypeMenu() {
        DatabaseBeReader db = new DatabaseBeReader(this);
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

    /**
     * Populate the type menu, which display all type in the Database
     */
    private void populateTypeMenu() {
        DatabaseBeReader db = new DatabaseBeReader(this);
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

    /**
     * Open the database and take the last trans id and increment it by 1
     * @return new trans id to create a new transaction in the database
     */
    private int getNewTransID() {
        DatabaseBeReader db = new DatabaseBeReader(this);
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

    /**
     * Insert a new Trans in the Database, update the total money in the respective Card
     * and update the respective Budget
     */
    private void insertNewTrans() {
        int idcard = 0, idsubtype = 0;
        double cur_money = 0;

        DatabaseBeReader db = new DatabaseBeReader(this);
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

        db.insertTrans(getNewTransID(), idcard, idsubtype, money, java.time.LocalDateTime.now().toString());

        cur_money = cur_money + money;
        db.close();

        updateMoney(idcard, cur_money);
        if(money < 0) {
            updateBudget(idsubtype, Math.abs(money));
        }
    }

    /**
     * Open the Database and update the money in that budget
     * @param idsubtype the subtype id of the budget to update
     * @param money quantity of money to add to budget
     */
    private void updateBudget(int idsubtype, double money) {
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();

        Cursor cur = db.queryBudgetbySubtype(idsubtype);
        int tmp;
        double updatemoney;

        if(cur.moveToFirst()) {
            do {
                tmp = cur.getInt(cur.getColumnIndex("idbudget"));
                updatemoney = getCurMoneyBudget(tmp);
                updatemoney += money;
                db.updateBudget(updatemoney, tmp);
            } while(cur.moveToNext());
        }

        db.close();
    }

    /**
     * Open the Database and take the money of the budget with a certain id
     * @param idbudget the budget id to read the current money
     * @return the current money in the Budget
     */
    private double getCurMoneyBudget(int idbudget) {
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();

        Cursor cur = db.queryBudgetbyIdbudget(idbudget);
        double ret = -1;

        if(cur.moveToFirst()) {
            ret = cur.getDouble(cur.getColumnIndex("money"));
        }

        db.close();
        return ret;
    }

    /**
     * Call the 2 methods below
     */
    private void insertNewTransandSubtype() {
        insertNewSubtype();
        insertNewTrans();
    }

    /**
     * Insert a new Subtype to the Database
     */
    private void insertNewSubtype() {
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();

        selected_subT = Objects.requireNonNull(new_reason.getText()).toString();

        db.insertSubtype(getNewSubtypeID(), getTypebyName(), selected_subT);

        db.close();
    }

    /**
     * Open the Database and retrieve the typeID with a selected_type Name
     * @return The TypeID with a certain name readed by selected_type
     */
    private int getTypebyName() {
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();

        int ins = -1;
        Cursor cur = db.queryType("name", selected_type);

        if(cur.moveToFirst()) {
            ins = cur.getInt(cur.getColumnIndex("idtype"));
        }

        db.close();
        return ins;
    }

    /**
     * Take the last subtypeID and increment it by 1
     * @return new subtypeID for instance a new Subtype
     */
    private int getNewSubtypeID() {
        DatabaseBeReader db = new DatabaseBeReader(this);
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

    /**
     * Update the money in the card
     * @param idcard card to be updated
     * @param newmoney money to substitute to the current money in the Database
     */
    private void updateMoney(int idcard, double newmoney) {
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();
        db.updateMoneyCard(newmoney, idcard);
        db.close();
    }

    /**
     * Concatenate 2 string with the integer and decimal part of the money and transform it in a double
     * @return integer and decimal money fuse together
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
     * Listener for the wallet_menu, set the respective variable to the selected item
     */
    private AdapterView.OnItemClickListener wallet_listener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selected_w = wallet_list.get(i);
                }
            };

    /**
     * Listener for the subtype_menu, set the respective variable to the selected item
     */
    private AdapterView.OnItemClickListener subtype_listener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selected_subT = subtype_list.get(i);
                }
            };

    /**
     * Listener for the type_menu, set the respective variable to the selected item
     */
    private AdapterView.OnItemClickListener type_listener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selected_type = type_list.get(i);
                }
            };

    /**
     * Listener for the first button, if the conditions is checked then
     * insert the new transaction and finish this activity
     */
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

    /**
     * Listener for the second button, if the conditions is checked then
     * insert the new transaction, new subtype and finish this activity
     */
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

    /**
     * Listener for the switcher, make visible and invisible some item in the screen.
     */
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
