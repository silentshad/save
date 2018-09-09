package com.example.silentshad.contactmanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ShowContact extends AppCompatActivity {

    TextView Hphone;
    TextView Pphone;
    TextView mail;
    String name;

    private DBOpenHelper db;
    private SQLiteDatabase sdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);

        Hphone = (TextView) findViewById(R.id.disp_phoneH);
        Pphone = (TextView) findViewById(R.id.disp_phoneP);
        mail = (TextView) findViewById(R.id.disp_mail);

        String table_name = "contact";


        db = new DBOpenHelper(this, table_name, null, 1);
        sdb = db.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
            name = extras.getString("name");
            setTitle(name);


            String[] columns = {"NAME","HOME_PHONE", "PRIVATE_PHONE", "MAIL"};
            String where = null;
            String where_args[] = null;
            String group_by = null;
            String having = null;
            String order_by = null;

            Cursor c = sdb.query(table_name, columns, where, where_args, group_by, having, order_by);
            c.moveToFirst();
            for (int i = 1; i <= c.getCount(); i++) {
                if (c.getString(0).equals(name)) {
                    Hphone.setText(getResources().getString(R.string.show_phoneH)+" " + c.getString(1));
                    Pphone.setText(getResources().getString(R.string.show_phoneP)+" "+ c.getString(2));
                    mail.setText(getResources().getString(R.string.show_mail)+" " + c.getString(3));
                }
                c.moveToNext();
            }
            c.close();

    }

    public void Edit_Contact(View view)
    {
        Intent intent = new Intent(this,EditContact.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }
}
