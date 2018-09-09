package com.example.silentshad.contactmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by silentshad on 27/02/17.
 */

public class DBOpenHelper extends SQLiteOpenHelper
{
    String table_name = "contact";
    private static final String create_table = "create table contact("
                                            +"ID integer primary key autoincrement, "
                                            +"NAME string NOCASE," +"HOME_PHONE text,"
                                            + "PRIVATE_PHONE text,"+"MAIL string" +")";

    public  DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
    super(context, name,factory, version);
    }

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(create_table);
    }

    public void onUpgrade(SQLiteDatabase db, int version_old, int version_new)
    {
        db.execSQL("drop table contact");
        db.execSQL(create_table);
    }

    public void delete (String name , SQLiteDatabase db)
    {
        db.delete(table_name,"NAME=\""+name+"\"", null);
    }

    public int search ( String searched , SQLiteDatabase db) {
        int index = -1;


        String[] columns = {"NAME"};
        String where = null;
        String where_args[] = null;
        String group_by = null;
        String having = null;
        String order_by = null;

        Cursor c = db.query(table_name, columns, where, where_args, group_by, having, order_by);
        c.moveToFirst();
        for (int i = 1; i <= c.getCount() && index == -1; i++) {
            if (c.getString(0).toLowerCase().equals(searched.toLowerCase()))
                index = i;
            c.moveToNext();
        }
        c.close();

        return index;
    }

    public void add (String name, String Hphone, String Pphone, String mail, SQLiteDatabase db)
    {
        ContentValues cv = new ContentValues();
        if (name.equals(""))
        {
            if (Hphone.equals(""))
                name = !Pphone.equals("")? Pphone: mail;
            else
                name = Hphone;
        }
        cv.put("NAME", name );
        cv.put("HOME_PHONE", Hphone);
        cv.put("PRIVATE_PHONE", Pphone);
        cv.put("MAIL", mail);

        int index = search(name,db);
        if (index == -1)
           db.insert(table_name,null,cv);
    }

    public void update (String name_old, String name, String Hphone, String Pphone, String mail, SQLiteDatabase db)
    {
        ContentValues cv = new ContentValues();
        cv.put("NAME", name );
        cv.put("HOME_PHONE", Hphone);
        cv.put("PRIVATE_PHONE", Pphone);
        cv.put("MAIL", mail);

        int index = search(name_old,db);
        if (index >=0)
            db.update(table_name,cv,"ID="+index,null);
    }

    public boolean valid_name(String name)
    {
        return !name.contains("'")&&!name.contains(";");
    }
    public boolean valid_number(String nb)
    {
        return !nb.contains("+") || nb.contains("+") && (nb.lastIndexOf("+")==0);
    }
    public boolean valid_mail(String mail)
    {
        if ( mail.equals("")) {
            return true;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches();
        }
    }
}