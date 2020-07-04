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

/**
 * Activity for LogIn to the application with a username and password
 */
public class LoginPanel extends Activity {
    TextInputEditText username, passwd;
    Button enter;

    /**
     * Initialize attributes from this class, set their own listener.
     * @param savedInstanceState saved state for create this activity, in this application is NULL.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpanel);

        enter = findViewById(R.id.button);
        enter.setOnClickListener(button_listener);
        username = findViewById(R.id.usernameTF);
        passwd = findViewById(R.id.passwdTF);

    }

    /**
     * If the username or the password do not match, generate a new Toast to advice the user
     */
    public void wrongup(){
        Toast toast = Toast.makeText(this, "Username/Password is not valid", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    /**
     * Instance the database and query username and password, and check if this values are equal to the TextInputEditText.
     * @return true if username and password match, false otherwise.
     */
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

    /**
     * Same method in MainActivity. Control the elapsed budget in this day.
     */
    private void controlBudget() {
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();

        Cursor cur = db.queryBudgetFull();
        String tmp;

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

    /**
     * Listener for the confirm button. If the CheckPass method return true, then control the
     * elapsed budget and start the HomeActivity.
     * Else call wrongup.
     */
    private Button.OnClickListener button_listener =
            new View.OnClickListener() {
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
            };
}
