package com.inland.pilot.Location;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String TABLE_NAME = "GPSModel";
    public static final String QUERY ="Time";
    public static final String TRIPID ="TripId";
    public static final String LOGINID ="LoginId";
    public static final String T_LONGITUDE ="T_Longitude";
    public static final String T_LATITUDE ="T_Latitude";
    public static final String DEVICEID ="DeviceId";
    public static final String TOKENNO ="TokenNo";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table "+ TABLE_NAME +" " +
                        "(id integer primary key,TripId text,LoginId text,T_Longitude text,T_Latitude text, lastUpdate text,DeviceId text,TokenNo text) "
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS GPSModel");
        onCreate(db);
    }

    public boolean insertData (String TripId,String LoginId,String T_Longitude,String T_Latitude, String DeviceId,String TokenNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime  = dateFormat.format(new Date());

        contentValues.put("TripId", TripId);
        contentValues.put("LoginId", LoginId);
        contentValues.put("T_Longitude",T_Longitude);
        contentValues.put("T_Latitude",T_Latitude);
        contentValues.put("lastUpdate", currentDateandTime);
        contentValues.put("DeviceId",DeviceId);
        contentValues.put("TokenNo",TokenNo);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from GPSModel", null );
        return res;
    }



    public int getQuantity(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from GPSModel where id="+id+"", null );
        if (res.getCount()==0)
            return 0;
        res.moveToFirst();
        return res.getInt(res.getColumnIndex(T_LATITUDE));
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateData (Integer id,String Quer,String TripId,int LoginId,int T_Longitude,String T_Latitude ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Quer", Quer);
        contentValues.put("LoginId", LoginId);
        contentValues.put("TripId",TripId);
        contentValues.put("T_Longitude",T_Longitude);
        contentValues.put("T_Latitude",T_Latitude);
        db.update("GPSModel", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteData () {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("GPSModel",null,null);
    }

//    public ArrayList<CartItem> getAllData() {
//        ArrayList<CartItem> array_list = new ArrayList<CartItem>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from cart", null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//            CartItem CartItem=new CartItem(res.getInt(res.getColumnIndex("id")),res.getString(res.getColumnIndex(QUERY)),res.getString(res.getColumnIndex(DEVICEID)),res.getInt(res.getColumnIndex(T_LONGITUDE)),res.getInt(res.getColumnIndex(T_LATITUDE)),res.getString(res.getColumnIndex(TRIPID)));
//            array_list.add(CartItem);
//            res.moveToNext();
//        }
//        return array_list;
//    }

    public JSONArray getSqlToJSONResults()
    {


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select `TripId` ,`LoginId` ,`T_Longitude` ,`T_Latitude` , `lastUpdate` ,`DeviceId` ,`TokenNo` from GPSModel", null );

        JSONArray resultSet     = new JSONArray();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null )
                {
                    try
                    {
                        if( cursor.getString(i) != null )
                        {
                            Log.d("TAG_NAME", cursor.getString(i) );
                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        }
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "" );
                        }
                    }
                    catch( Exception e )
                    {
                        Log.d("TAG_NAME", e.getMessage()  );
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("TAG_NAME", resultSet.toString() );
        return resultSet;
    }
}