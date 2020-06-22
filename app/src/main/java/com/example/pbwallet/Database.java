package com.example.pbwallet;

public class Database {
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepanel);
        TextView name = (TextView) findViewById(R.id.textView4);
        TextView surname = (TextView) findViewById(R.id.textView5);
        number = (EditText) findViewById(R.id.editTextTextPersonName2);
        Button bt = (Button) findViewById(R.id.button);
        String strname = "Giuseppe";
        String strsurname = "Pultrone";
        ChangeName(name,surname,strname,strsurname);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salva();
            }
        });
    }

    public void salva(){
        //creazione database o update
        DatabaseBeReader db = new DatabaseBeReader(this);
        //apertura database
        db.open();
        //inserimento values in table
        db.inserisci(number.getText().toString());
        //prendo quello che ho scritto
        String query1 = number.getText().toString();
        //query
        Cursor cur = db.query(query1);
        //while per stampare tutta la query
        while(cur.moveToNext()) {
            System.out.println(cur.getString(cur.getColumnIndex("name")));
        }
        //chiusura database
        db.close();
    }

    public void salva(){
        DatabaseBeReader db = new DatabaseBeReader(this);
        db.open();
        db.insertUser("Giuseppe","Pultrone","12/34/4543",username.getText().toString(),passwd.getText().toString(),"sport",1,1);
        Cursor cur = db.queryUser("name","Giuseppe");
        while(cur.moveToNext()) {
            System.out.println(cur.getString(cur.getColumnIndex("username")));
        }
        db.close();
    }

    private void ChangeName(TextView name, TextView surname, String strname, String strsurname){
        name.setText(strname);
        surname.setText(strsurname);
    }

    */
}
