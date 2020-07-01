package com.example.pbwallet;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

public class AddBudgetActivity extends AppCompatActivity {
    String selected;
    ArrayList<String> subtype_list;
    AutoCompleteTextView subtype;
    TextInputEditText bound;
    Button confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbudget);

        selected = "";

        subtype = findViewById(R.id.subtype_menu_budTF);
        subtype.setOnItemClickListener(subtype_listener);

        bound = findViewById(R.id.bud_moneyTF);

        confirm = findViewById(R.id.confirm_button);
        confirm.setOnClickListener(conf_listener);

        populateSubtypeMenu();
    }

    private void addBudget() {
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();

        db.insertBudget(getNewBudgetID(), 0, Double.parseDouble(Objects.requireNonNull(bound.getText()).toString()),java.time.LocalDate.now().plusMonths(1).toString() , getSubtypeID());

        db.close();
    }

    private int getSubtypeID() {
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();

        int ret = -1;

        Cursor cur = db.querySubtype("name", selected);

        if(cur.moveToFirst()) {
            ret = cur.getInt(cur.getColumnIndex("idsubtype"));
        }

        db.close();
        return ret;
    }

    private int getNewBudgetID() {
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();

        int ret = -1;

        Cursor cur = db.queryBudgetbyID();

        if(cur.moveToFirst()) {
            ret = cur.getInt(cur.getColumnIndex("idbudget"));
        }

        ++ret;

        db.close();

        return ret;
    }

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
        subtype.setAdapter(adapter);

        db.close();
    }

    private AdapterView.OnItemClickListener subtype_listener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selected = subtype_list.get(i);
                }
            };

    private Button.OnClickListener conf_listener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!selected.isEmpty() && !Objects.requireNonNull(bound.getText()).toString().isEmpty()) {
                        addBudget();
                        finish();
                    }
                }
            };
}
