package abhishek.customlistview.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import abhishek.customlistview.Model.Contact;

/**
 * Created by abhishek on 8/12/14.
 */
public class DatabaseHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DB_CONTACT";
    private static final String CONTACTS_TABLE_NAME = "TBL_CONTACTS";
    private final ArrayList<Contact> contactList = new ArrayList<Contact>();

    private static final String CONTACTS_TABLE_CREATE =
            "CREATE TABLE " + CONTACTS_TABLE_NAME + " (" +
                    "id" + " INTEGER PRIMARY KEY AUTOINCREMENT,  " +
                    "name" + " TEXT, " +
                    "phone" + " TEXT, " +
                    "email" + " TEXT);";

    public DatabaseHandler(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CONTACTS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_CREATE);

        onCreate(sqLiteDatabase);
    }

    // Adding new contact
    public long AddContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", contact.getName()); // Contact Name
        values.put("phone", contact.getPhone()); // Contact Phone
        values.put("email", contact.getEmail()); // Contact Email
        // Inserting Row
        long lastInsertId = db.insert(CONTACTS_TABLE_NAME, null, values);
        db.close(); // Closing database connection
        return lastInsertId;
    }

    // Getting single contact
    public Contact GetContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(CONTACTS_TABLE_NAME, new String[] { "id",
                        "name", "phone", "email" },  "id = ?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        // return contact
        cursor.close();
        db.close();

        return contact;
    }

    // Getting All Contacts
    public ArrayList<Contact> GetContacts() {
        try {
            contactList.clear();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + CONTACTS_TABLE_NAME;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    contact.setId(Integer.parseInt(cursor.getString(0)));
                    contact.setName(cursor.getString(1));
                    contact.setPhone(cursor.getString(2));
                    contact.setEmail(cursor.getString(3));
                    // Adding contact to list
                    contactList.add(contact);
                } while (cursor.moveToNext());
            }

            // return contact list
            cursor.close();
            db.close();
            return contactList;
        } catch (Exception e) {

        }

        return contactList;
    }

    // Updating single contact
    public boolean UpdateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", contact.getName());
        values.put("phone", contact.getPhone());
        values.put("email", contact.getEmail());

        // updating row
        int rowCount = db.update(CONTACTS_TABLE_NAME, values,  " id = ?",
                new String[] { String.valueOf(contact.getId()) });

        return rowCount > 0;
    }

    // Deleting single contact
    public void DeleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CONTACTS_TABLE_NAME, " id = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    // Getting contacts Count
    public int GetTotalContacts() {
        String countQuery = "SELECT  * FROM " + CONTACTS_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
