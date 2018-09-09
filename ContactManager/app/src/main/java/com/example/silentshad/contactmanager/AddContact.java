package com.example.silentshad.contactmanager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddContact extends Activity {
    EditText name_field;
    EditText Hnumber_field;
    EditText Pnumber_field;
    EditText mail_field;

    private DBOpenHelper db;
    private SQLiteDatabase sdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Contact");
        setContentView(R.layout.activity_add_contact);

        db = new DBOpenHelper(this, "contact", null, 1);
        sdb = db.getWritableDatabase();

        name_field = (EditText) findViewById(R.id.enter_name);
        Hnumber_field = (EditText) findViewById(R.id.enter_phoneH);
        Pnumber_field =  (EditText) findViewById(R.id.enter_phoneP);
        mail_field =  (EditText) findViewById(R.id.enter_mail);
    }

    public void Submit(View view) {
        String name = name_field.getText().toString().trim();
        String Hnumber = Hnumber_field.getText().toString().trim();
        String Pnumber = Pnumber_field.getText().toString().trim();
        String mail = mail_field.getText().toString().trim();

        boolean no_error = true;


        if(!db.valid_name(name)) {
            name_field.setError("Name must not contain any ' or ;");
            no_error = false;
        }
        else
            name_field.setError(null);

        if(!db.valid_number(Hnumber)) {
            Hnumber_field.setError("invalid number the + can only be at the beginning");
            no_error = false;
        }
        else
            Hnumber_field.setError(null);

        if(!db.valid_number(Pnumber)) {
            Pnumber_field.setError("invalid number the + can only be at the beginning");
            no_error = false;
        }
        else
            Pnumber_field.setError(null);

        if(!db.valid_mail(mail)) {
            mail_field.setError("mail must be of format example@gmail.com");
            no_error = false;
        }
        else
            mail_field.setError(null);

        if (name.equals("")&& Hnumber.equals("")& Pnumber.equals("")&& mail.equals("") )
            Toast.makeText(this,"at least one field must contain something",Toast.LENGTH_LONG).show();
        else if (db.search(name,sdb)!=-1)
            name_field.setError("This contact already exist");
        else if(no_error)
        {
            db.add(name, Hnumber, Pnumber, mail, sdb);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void Cancel(View view)
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
