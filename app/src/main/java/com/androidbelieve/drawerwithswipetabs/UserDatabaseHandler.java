package com.androidbelieve.drawerwithswipetabs;

/**
 * Created by Maslin-Android on 4/29/2016.
 */
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "userleader";

    private static final String TABLE_USERINFORMATION = "userinformation";

    private static final String KEY_ID = "id";

    private static final String KEY_NAME = "name";

    private static final String KEY_EMAIL = "email";

    private static final String KEY_PH_NO = "contact_number";



    public UserDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String CREATE_USERINFORMATION_TABLE = "CREATE TABLE " + TABLE_USERINFORMATION + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_EMAIL + " TEXT,"
                + KEY_PH_NO + " TEXT" + ")";

        db.execSQL(CREATE_USERINFORMATION_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERINFORMATION);

        onCreate(db);
    }

    void addContact(UserInformation newuser) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, newuser.getName());

        values.put(KEY_EMAIL, newuser.getEmail());

        values.put(KEY_PH_NO, newuser.getcontactNumber());

        db.insert(TABLE_USERINFORMATION, null, values);

        db.close();
    }

    UserInformation getUserInformation(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERINFORMATION, new String[] { KEY_ID,
                        KEY_NAME, KEY_EMAIL, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        UserInformation newuser = new UserInformation(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        return newuser;
    }

    public List<UserInformation> getAllUserInformation() {

        List<UserInformation> contactList = new ArrayList<UserInformation>();

        String selectQuery = "SELECT  * FROM " + TABLE_USERINFORMATION;


        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UserInformation newuser = new UserInformation();

                newuser.setID(Integer.parseInt(cursor.getString(0)));

                newuser.setName(cursor.getString(1));

                newuser.setEmail(cursor.getString(2));

                newuser.setPhoneNumber(cursor.getString(3));

                contactList.add(newuser);

            }
            while (cursor.moveToNext());
        }

        return contactList;
    }
    public int updateContact(UserInformation newuser) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, newuser.getName());

        values.put(KEY_EMAIL, newuser.getEmail());

        values.put(KEY_PH_NO, newuser.getcontactNumber());

        return db.update(TABLE_USERINFORMATION, values, KEY_ID + " = ?",

                new String[] { String.valueOf(newuser.getID()) });
    }

    public void deleteContact (UserInformation newuser) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_USERINFORMATION, KEY_ID + " = ?",
                new String[] { String.valueOf(newuser.getID()) });

        db.close();
    }
    public int getUserInformationCount() {

        String countQuery = "SELECT  * FROM " + TABLE_USERINFORMATION;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        cursor.close();

        return cursor.getCount();
    }


}
