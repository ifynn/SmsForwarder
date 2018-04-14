package com.fynn.smsforwarder.common.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fynn.smsforwarder.R;

import org.fynn.appu.AppU;

/**
 * @author Fynn
 * @date 18/2/13
 */
public class SmsDbHelper extends SQLiteOpenHelper {

    public static final String NAME = "sms.db";
    public static final String TABLE_NAME = "Sms";
    public static final String ADDRESS = "address";
    public static final String BODY = "body";
    public static final String DATE = "date";
    public static final String ID = "_id";
    private static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME +
            "(" + ID + " number primary key," + ADDRESS + " text," +
            BODY + " text," + DATE + " text)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static int VERSION = AppU.app().getResources().getInteger(R.integer.db_sms_version);
    private static volatile SmsDbHelper smsDbHelper;

    private SmsDbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    public static SmsDbHelper get() {
        if (smsDbHelper == null) {
            synchronized (SmsDbHelper.class) {
                if (smsDbHelper == null) {
                    smsDbHelper = new SmsDbHelper(AppU.app());
                }
            }
        }
        return smsDbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /**
     * insert a item
     *
     * @param values
     * @return
     */
    public long insert(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        long resultId = -1;

        if (!db.isOpen()) {
            return resultId;
        }

        db.beginTransaction();
        try {
            resultId = db.insert(TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return resultId;
    }

    /**
     * 查询所有
     *
     * @return
     */
    public Cursor queryAll() {
        SQLiteDatabase db = getReadableDatabase();

        if (!db.isOpen()) {
            return null;
        }

        Cursor c = null;
        db.beginTransaction();

        try {
            c = db.query(TABLE_NAME, new String[]{ID, ADDRESS, BODY, DATE},
                    null, null, null, null, ID + " desc");
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return c;
    }

    /**
     * 查询指定 id 的 item
     *
     * @param id
     * @return
     */
    public Cursor query(long id) {
        SQLiteDatabase db = getReadableDatabase();

        if (!db.isOpen()) {
            return null;
        }

        Cursor cursor = null;
        db.beginTransaction();

        try {
            cursor = db.query(TABLE_NAME, new String[]{ID, ADDRESS, BODY, DATE},
                    ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return cursor;
    }

    /**
     * 分页查询
     *
     * @param offset 偏移位置
     * @param limit 每页数量
     * @return
     */
    public Cursor queryPage(int offset, int limit) {
        SQLiteDatabase db = getReadableDatabase();

        if (!db.isOpen()) {
            return null;
        }

        Cursor c = null;
        db.beginTransaction();

        try {
            c = db.query(TABLE_NAME, new String[]{ID, ADDRESS, BODY, DATE},
                    null, null, null, null, ID + " desc", offset + "," + limit);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return c;
    }

    /**
     * 分页查询
     *
     * @param offset 偏移位置
     * @param limit 每页数量
     * @return 返回 id
     */
    public Cursor queryIdPage(int offset, int limit) {
        SQLiteDatabase db = getReadableDatabase();

        if (!db.isOpen()) {
            return null;
        }

        Cursor c = null;
        db.beginTransaction();

        try {
            c = db.query(TABLE_NAME, new String[]{ID}, null, null, null, null,
                    ID + " desc", offset + "," + limit);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return c;
    }

    /**
     * 获取总记录数
     *
     * @return
     */
    public long getCount() {
        SQLiteDatabase db = getReadableDatabase();

        if (!db.isOpen()) {
            return -1;
        }

        Cursor c = null;
        db.beginTransaction();

        try {
            c = db.rawQuery("select count(_id) from " + TABLE_NAME, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        if (c == null) {
            return -1;
        }

        c.moveToFirst();
        long count = c.getLong(0);
        c.close();

        return count;
    }
}
