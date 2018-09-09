package com.example.silentshad.contactmanager;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    private DBOpenHelper db;
    private SQLiteDatabase sdb;

    private ListView contactList;
    private String name_selected;
    public String table_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        table_name = "contact";

        db = new DBOpenHelper(this, table_name, null, 1);
        sdb = db.getWritableDatabase();

        contactList = (ListView) findViewById(R.id.contactList);

        updateList();
        initListner();

    }

    private void updateList()
    {
        String[] columns = {"ID","NAME"};
        String where = null;
        String where_args[] = null;
        String group_by = null;
        String having = null;
        String order_by = null;

        ArrayList<String> contacts_names = new ArrayList<>();

        Cursor c = sdb.query(table_name,columns,where,where_args,group_by,having,order_by);
        c.moveToFirst();
        for (int i =0 ; i <c.getCount();i++)
        {
            contacts_names.add( c.getString(1) );
            c.moveToNext();
        }
        contactList.setAdapter(new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,contacts_names) );
        c.close();
    }

    private void initListner(){

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent show_intent = new Intent(view.getContext(),ShowContact.class);
                show_intent.putExtra("name", contactList.getAdapter().getItem(position).toString());
                show_intent.putExtra("table_name", table_name);
                startActivity(show_intent);
            }
        });

        contactList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                name_selected = contactList.getAdapter().getItem(position).toString() ;
                edit_or_del_dial();
                return true ;
            }
        });
    }

    private void edit_or_del_dial(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.edit_menu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0)
                    edit_Contact(name_selected);
                else if (which == 1) {
                    db.delete(name_selected, sdb);
                    updateList();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void  Add_Contact(View view)
    {
        Intent intent = new Intent(this,AddContact.class);
        startActivity(intent);
    }

    public void edit_Contact(String name_to_edit)
    {
        Intent intent = new Intent(this,EditContact.class);
        intent.putExtra("name", name_to_edit);
        startActivity(intent);
    }

    public void onStop()
    {
        super.onStop();
    }
}
