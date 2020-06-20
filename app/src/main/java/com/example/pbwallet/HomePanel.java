package com.example.pbwallet;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HomePanel extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepanel);
        TextView name = (TextView) findViewById(R.id.textView4);
        TextView surname = (TextView) findViewById(R.id.textView5);
        String strname = "Giuseppe";
        String strsurname = "Pultrone";
        ChangeName(name,surname,strname,strsurname);
    }

    private void ChangeName(TextView name, TextView surname, String strname, String strsurname){
        name.setText(strname);
        surname.setText(strsurname);
    }

}
