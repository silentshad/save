package com.example.silentshad.contactmanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditContact extends AppCompatActivity {

    EditText Hphone;
    EditText Pphone;
    EditText mail;
    EditText name;
    String name_;

    private DBOpenHelper db;
    private SQLiteDatabase sdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        name = (EditText) findViewById(R.id.edit_name) ;
        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });

        Hphone = (EditText) findViewById(R.id.edit_phoneH);
        Pphone = (EditText) findViewById(R.id.edit_phoneP);
        mail = (EditText) findViewById(R.id.edit_mail);

        String table_name = "contact";


        db = new DBOpenHelper(this, table_name, null, 1);
        sdb = db.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        name_ = extras.getString("name");
        setTitle(name_);


        String[] columns = {"NAME","HOME_PHONE", "PRIVATE_PHONE", "MAIL"};
        String where = null;
        String where_args[] = null;
        String group_by = null;
        String having = null;
        String order_by = null;

        Cursor c = sdb.query(table_name, columns, where, where_args, group_by, having, order_by);
        c.moveToFirst();
        for (int i = 1; i <= c.getCount(); i++) {
            if (c.getString(0).equals(name_)) {
                name.setText(name_);
                Hphone.setText(c.getString(1));
                Pphone.setText(c.getString(2));
                mail.setText(c.getString(3));
            }
            c.moveToNext();
        }
        c.close();

    }
    public void Submit(View view)
    {

        String name_s = name.getText().toString().trim();
        String Hnumber = Hphone.getText().toString().trim();
        String Pnumber = Pphone.getText().toString().trim();
        String mail_s = mail.getText().toString().trim();

        boolean no_error = true;

        if(!db.valid_name(name_s)) {
            name.setError("Name must not contain any ' or ;");
            no_error = false;
        }
        else
            name.setError(null);

        if(!db.valid_number(Hnumber)) {
            Hphone.setError("invalid number the + can only be at the beginning");
            no_error = false;
        }
        else
            name.setError(null);

        if(!db.valid_number(Pnumber)) {
            Pphone.setError("invalid number the + can only be at the beginning");
            no_error = false;
        }
        else
           Pphone.setError(null);

        if(!db.valid_mail(mail_s)) {
           mail.setError("mail must be of format example@gmail.com");
            no_error = false;
        }
        else
            mail.setError(null);

        if (name_s.equals("")&& Hnumber.equals("") && Pnumber.equals("") && mail_s.equals("") )
            Toast.makeText(this,"at least one field must contain something",Toast.LENGTH_LONG).show();
        else if(no_error)
        {
            db.update(name_, name_s, Hnumber, Pnumber, mail_s, sdb);


            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void Cancel(View view)
    {
        this.finish();
    }
}
