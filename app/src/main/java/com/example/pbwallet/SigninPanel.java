package com.example.pbwallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Objects;

public class SigninPanel extends Activity {
    TextInputEditText name, surname, passwd, passwdconfirm, username;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinpanel);

        Button enter = findViewById(R.id.button);
        name = findViewById(R.id.nameTF);
        surname = findViewById(R.id.surnameTF);
        username = findViewById(R.id.usernameTF);
        passwd = findViewById(R.id.passwdTF);
        passwdconfirm = findViewById(R.id.passwdconfirmTF);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homepage = new Intent(SigninPanel.this, HomeActivity.class);
                if(checkEnter(Objects.requireNonNull(username.getText()).toString(), Objects.requireNonNull(passwd.getText()).toString())) {
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
            }
        });
    }

    public void emptyField(){
        Toast toast = Toast.makeText(this, "One or more fileds empty", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public boolean checkField(){
        if(name.getText().toString().isEmpty() || surname.getText().toString().isEmpty() || username.getText().toString().isEmpty())
            return false;
        return true;
    }

    public boolean checkLenPass(){
        if(passwd.getText().toString().length() > 30)
            return false;
        return true;
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
        if(passwd.getText().toString().isEmpty()) {
            db.insertUser(name.getText().toString(), surname.getText().toString(), username.getText().toString(), null, null, null);
        }
        else
            db.insertUser(name.getText().toString(), surname.getText().toString(), username.getText().toString(), passwd.getText().toString(),null, null);
        db.close();
    }

    public boolean checkPass(){
        if(passwd.getText().toString().equals(passwdconfirm.getText().toString()))
            return true;
        return false;
    }

    protected boolean checkEnter(String username, String passwd) {
        return true;
    }
}
