package com.example.pbwallet;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Objects;

/**
 * Activity that let the user to register and save all the indicated field
 * in the database.
 */
public class SigninPanel extends Activity {
    TextInputEditText name, surname, passwd, passwdconfirm, username;
    Button enter;
    RadioGroup radiobutton;
    String currency;

    /**
     * Initialize attributes from this class, set their own listener.
     * @param savedInstanceState saved state for create this activity, in this application is NULL.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinpanel);

        enter = findViewById(R.id.button);
        name = findViewById(R.id.nameTF);
        surname = findViewById(R.id.surnameTF);
        username = findViewById(R.id.usernameTF);
        passwd = findViewById(R.id.passwdTF);
        passwdconfirm = findViewById(R.id.passwdconfirmTF);
        radiobutton = findViewById(R.id.radiobutton);

        enter.setOnClickListener(confirm_button_listener);

        radiobutton.setOnCheckedChangeListener(radiobutton_listener);
    }

    /**
     * Method that create a toast if one or more fields are empty.
     */
    private void emptyField(){
        Toast toast = Toast.makeText(this, "One or more fields empty", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    /**
     * Control if all fields are empty or not.
     * @return true if all fields are not empty, false otherwise.
     */
    private boolean checkField(){
        return !Objects.requireNonNull(name.getText()).toString().isEmpty() && !Objects.requireNonNull(surname.getText()).toString().isEmpty() && !Objects.requireNonNull(username.getText()).toString().isEmpty();
    }

    /**
     * Control if the password is less or equal than 30 character.
     * @return true if the password's lenght is less or equal than 30.
     */
    private boolean checkLenPass(){
        return Objects.requireNonNull(passwd.getText()).toString().length() <= 30;
    }

    /**
     * Make a toast if the password and the confirm password fields are different.
     */
    private void wrongPasswd(){
        Toast toast = Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    /**
     * Insert the new user in the database, with or without the password.
     */
    private void save(){
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
      
        if(Objects.requireNonNull(passwd.getText()).toString().isEmpty()) {
            try {
                db.insertUser(Objects.requireNonNull(name.getText()).toString(), Objects.requireNonNull(surname.getText()).toString(), Objects.requireNonNull(username.getText()).toString(), null, currency);
            } catch (SQLiteException e) {
                Log.d("pbwallet", "Exception: " + Log.getStackTraceString(e));
                try {
                    db.insertUser(Objects.requireNonNull(name.getText()).toString(), Objects.requireNonNull(surname.getText()).toString(), Objects.requireNonNull(username.getText()).toString(), null, currency);
                } catch (SQLiteException e_1) {
                    Log.d("pbwallet", "Exception: " + Log.getStackTraceString(e_1));
                    databaseError();
                    finish();
                }
            }
        }
        else {
            try {
                db.insertUser(Objects.requireNonNull(name.getText()).toString(), Objects.requireNonNull(surname.getText()).toString(), Objects.requireNonNull(username.getText()).toString(), passwd.getText().toString(), currency);
            } catch (SQLiteException e) {
                Log.d("pbwallet", "Exception: " + Log.getStackTraceString(e));
                try {
                    db.insertUser(Objects.requireNonNull(name.getText()).toString(), Objects.requireNonNull(surname.getText()).toString(), Objects.requireNonNull(username.getText()).toString(), passwd.getText().toString(), currency);
                } catch (SQLiteException e_1) {
                    Log.d("pbwallet", "Exception: " + Log.getStackTraceString(e_1));
                    databaseError();
                    finish();
                }
            }
        }
        db.close();
    }

    /**
     * Check if the 2 password( password and confirm password ) are equals.
     * @return true if the 2 password matches, false otherwise.
     */
    private boolean checkPass(){
        return Objects.requireNonNull(passwd.getText()).toString().equals(Objects.requireNonNull(passwdconfirm.getText()).toString());
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
     * Listener for the confirm button. If all the check methods return true, then
     * insert the new user and start the HomeActivity.
     * Else display the toast for wrong password or empty fields if some is missing.
     */
    private Button.OnClickListener confirm_button_listener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent homepage = new Intent(SigninPanel.this, HomeActivity.class);
                    if(checkLenPass()) {
                        if(checkField()) {
                            if (checkPass()) {
                                save();
                                startActivity(homepage);
                                finish();
                            } else
                                wrongPasswd();
                        }
                        else
                            emptyField();
                    }
                }
            };

    /**
     * Listener for radio button, that allow the user to choose what currency wanna display
     * for his money. Choose are Euro, Dollar and Pounds.
     */
    private RadioGroup.OnCheckedChangeListener radiobutton_listener =
            new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    RadioButton rb = findViewById(i);
                    currency = (String) rb.getText();
                }
            };
}
