package com.example.pbwallet;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;


public class LoginPanel extends Activity {
    TextInputEditText username, passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpanel);

        Button enter = findViewById(R.id.button);
        username = findViewById(R.id.usernameTF);
        passwd = findViewById(R.id.passwdTF);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homepage = new Intent(LoginPanel.this , HomeActivity.class);
                if(checkPass()) {
                    controlBudget();
                    startActivity(homepage);
                    finish();
                }
                else {
                    wrongup();
                }
            }
        });
    }

    public void wrongup(){
        Toast toast = Toast.makeText(this, "Username/Password is not valid", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public boolean checkPass() {
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();
        Cursor cur = db.queryUser("username", Objects.requireNonNull(username.getText()).toString());
        if(cur.moveToFirst()) {
            if (Objects.requireNonNull(passwd.getText()).toString().equals(cur.getString(cur.getColumnIndex("password")))) {
                db.close();
                return true;
            } else {
                db.close();
                return false;
            }
        }
        db.close();
        return false;
    }

    private void controlBudget() {
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();

        Cursor cur = db.queryBudgetFull();
        String tmp = "";

        if(cur.moveToFirst()) {
            do {
                tmp = cur.getString(cur.getColumnIndex("date"));
                if(tmp.equals(java.time.LocalDate.now().toString())) {
                    db.deleteBudget(cur.getInt(cur.getColumnIndex("idbudget")));
                }

            } while(cur.moveToNext());
        }

        db.close();
    }
}
