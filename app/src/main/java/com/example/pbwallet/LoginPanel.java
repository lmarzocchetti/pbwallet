package com.example.pbwallet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                Intent homepage = new Intent(LoginPanel.this , HomePanel.class);
                if(checkEnter(Objects.requireNonNull(username.getText()).toString(), Objects.requireNonNull(passwd.getText()).toString())) {
                    startActivity(homepage);
                    finish();
                }
            }
        });
    }

    protected boolean checkEnter(String username, String passwd) {
        return true;
    }
}
