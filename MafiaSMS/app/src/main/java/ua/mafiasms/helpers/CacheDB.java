package ua.mafiasms.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import ua.mafiasms.models.Contact;
import ua.mafiasms.models.FavoriteList;

/**
 * Created by daniil on 10/1/14.
 */
public class CacheDB extends SQLiteOpenHelper {

    public static final String TAG = "CacheDB";
    private static final String DB_NAME = "cache_data.db";
    private static final int DB_VERSION = 1;

    public interface Table {
        public static final String FAVORITE_GAMERS_LIST = "favorite_gamers_list";
    }

    public interface Field {
        public static final String ROW_ID = "_id";
        public static final String _ID = "contact_id";
        public static final String NAME = "name";
        public static final String PHONE_NUMBER = "phone_number";
        public static final String ROLE = "role";
        public static final String IS_SELECT = "is_select";
        public static final String NAME_LIST = "name_list";
    }

    private static final String SQL_CREATE_TABLE_FAVORITE_GAMERS_LIST =
            "CREATE TABLE " + Table.FAVORITE_GAMERS_LIST + " ("         +
            Field.ROW_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT,    "   +
            Field._ID + " INTEGER,                                  "   +
            Field.NAME + " TEXT,                                    "   +
            Field.NAME_LIST + " TEXT NOT NULL,                      "   +
            Field.PHONE_NUMBER + " TEXT,                            "   +
            Field.ROLE + " INTEGER,                                 "   +
            Field.IS_SELECT + " TEXT);                              ";


    private static CacheDB instance;

    public static CacheDB getInstance(Context context) {
        if(instance == null)
            instance = new CacheDB(context);
        return instance;
    }

    private SQLiteDatabase db;

    private CacheDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE_GAMERS_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
            case 2:
        }
    }

    public void insertListGamersToFavorite(FavoriteList favoriteList) {
        if (db.isOpen()) {
            ArrayList<Contact> list = favoriteList.listGamers;
            for (Contact contact : list) {
                insertGamerToListFavoriteByListName(favoriteList.nameList, contact);
            }
        }
    }

    public void insertGamerToListFavoriteByListName(String nameList, Contact contact) {
        if (db.isOpen()) {
            ContentValues cv = new ContentValues();

            cv.put(Field.NAME_LIST, nameList);
            cv.put(Field._ID, contact._id);
            cv.put(Field.NAME, contact.name);
            cv.put(Field.PHONE_NUMBER, contact.phoneNumber);
            cv.put(Field.ROLE, contact.role);
            cv.put(Field.IS_SELECT, contact.isSelect);

            long row = db.insert(Table.FAVORITE_GAMERS_LIST, null, cv);
            Log.d(TAG, "INSERT CONTACT TO LIST " + nameList + " idContact: " + contact._id + " ROW: " + row);
        }
    }

    public ArrayList<Contact> getListFavoriteListGamersByNameList(String nameList) {
        ArrayList<Contact> results = new ArrayList<Contact>();
        if (db.isOpen()) {
            Cursor c = db.rawQuery("SELECT * FROM '" + Table.FAVORITE_GAMERS_LIST + "'", null);
            FavoriteList data = getAllData(c);
            ArrayList<Contact> contacts = data.listGamers;
            for (Contact contact : contacts) {
                if (contact.listFavoriteName.contentEquals(nameList)) {
                    results.add(contact);
                }
            }
        }
        return results;
    }

    public ArrayList<String> getListNames() {
        ArrayList<String> listNames = new ArrayList<String>();
        if (db.isOpen()) {
            Cursor c = db.rawQuery("SELECT * FROM '" + Table.FAVORITE_GAMERS_LIST + "'", null);
            FavoriteList data = getAllData(c);
            listNames.addAll(data.listNames);
        }
        return listNames;
    }

    private FavoriteList getAllData(Cursor cursor) {
        FavoriteList result = new FavoriteList();
        ArrayList<Contact> list = new ArrayList<Contact>();
        ArrayList<String> namesList = new ArrayList<String>();
        result.listGamers = list;
        result.listNames = namesList;
        if (cursor.moveToFirst()) {
            int _rawId = cursor.getColumnIndex(Field.ROW_ID);
            int _id = cursor.getColumnIndex(Field._ID);
            int _name = cursor.getColumnIndex(Field.NAME);
            int _phoneNumber = cursor.getColumnIndex(Field.PHONE_NUMBER);
            int _listName = cursor.getColumnIndex(Field.NAME_LIST);
            int _role = cursor.getColumnIndex(Field.ROLE);
            int _isSelect = cursor.getColumnIndex(Field.IS_SELECT);
            do {
                int rowId = cursor.getInt(_rawId);
                String id = cursor.getString(_id);
                String name = cursor.getString(_name);
                String phoneNumber = cursor.getString(_phoneNumber);
                String listName = cursor.getString(_listName);
                int role = cursor.getInt(_role);
                boolean isSelect = Boolean.parseBoolean(cursor.getString(_isSelect));

                Contact contact = new Contact();
                contact._id = id;
                contact.role = role;
                contact.isSelect = isSelect;
                contact.listFavoriteName = listName;
                contact.name = name;
                contact.phoneNumber = phoneNumber;
                contact.rowIdInDB = rowId;

                list.add(contact);
                if (!namesList.contains(listName)) {
                    namesList.add(listName);
                }

            } while (cursor.moveToNext());
        }
        return result;
    }
}
