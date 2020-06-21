package com.example.pbwallet;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginPanel extends Activity{
    EditText username, passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpanel);

        Button enter = findViewById(R.id.login_button);
        username = findViewById(R.id.editTextTextPersonName);
        passwd = findViewById(R.id.editTextTextPassword);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homepage = new Intent(LoginPanel.this ,HomePanel.class);
                if(checkEnter(username.getText().toString(), passwd.getText().toString())) {
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
