package com.example.pbwallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Objects;

public class SigninPanel extends Activity {
    TextInputEditText name, surname, passwd, passwdconfirm, username;
    RadioGroup radiobutton;
    String currency;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinpanel);

        Button enter = findViewById(R.id.button);
        name = findViewById(R.id.nameTF);
        surname = findViewById(R.id.surnameTF);
        username = findViewById(R.id.usernameTF);
        passwd = findViewById(R.id.passwdTF);
        passwdconfirm = findViewById(R.id.passwdconfirmTF);
        radiobutton = findViewById(R.id.radiobutton);

        enter.setOnClickListener(new View.OnClickListener() {
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
        });

        radiobutton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                currency = (String) rb.getText();
            }
        });
    }

    public void emptyField(){
        Toast toast = Toast.makeText(this, "One or more fileds empty", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public boolean checkField(){
        return !Objects.requireNonNull(name.getText()).toString().isEmpty() && !Objects.requireNonNull(surname.getText()).toString().isEmpty() && !Objects.requireNonNull(username.getText()).toString().isEmpty();
    }

    public boolean checkLenPass(){
        return Objects.requireNonNull(passwd.getText()).toString().length() <= 30;
    }

    public void wrongPasswd(){
        Toast toast = Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    //aggiungere birthday
    public void save(){
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();
      
        if(Objects.requireNonNull(passwd.getText()).toString().isEmpty()) {
            db.insertUser(Objects.requireNonNull(name.getText()).toString(), Objects.requireNonNull(surname.getText()).toString(), Objects.requireNonNull(username.getText()).toString(), null, currency);
        }
        else
            db.insertUser(Objects.requireNonNull(name.getText()).toString(), Objects.requireNonNull(surname.getText()).toString(), Objects.requireNonNull(username.getText()).toString(), passwd.getText().toString(), currency);
        db.close();
    }

    public boolean checkPass(){
        return Objects.requireNonNull(passwd.getText()).toString().equals(Objects.requireNonNull(passwdconfirm.getText()).toString());
    }
}
