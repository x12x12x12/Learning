package com.example.tut_slz.da_client;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import Items.Item;

/**
 * Created by tut_slz on 23/11/2014.
 */
public class Database extends SQLiteOpenHelper {
    private SQLiteDatabase sqldb;
    private String sDATABASETABLE="DOAN";
    public Database(Context ctData, String DatabaseName){
        super(ctData,DatabaseName,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //String sCmd="create table "+sDATABASETABLE+" (id TEXT PRIMARY KEY, fname TEXT, rname TEXT, createday TEXT, update TEXT, price REAL, status INTEGER, imageurl TEXT, buy INTEGER, tab INTEGER)";
        String DATABASE_CREATE = "create table " +sDATABASETABLE + " (id text primary key not null , fname text , rname text , price real , status integer , imageurl text , buy integer , tab integer )";
        db.execSQL( DATABASE_CREATE );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+sDATABASETABLE);
        onCreate(db);
    }

    public void Insert(Item Data, int Buy, int Tab)
    {
        sqldb=this.getWritableDatabase();
        ContentValues cvData=new ContentValues();
        cvData.put("id",Data.getId());
        cvData.put("fname",Data.getName());
        cvData.put("rname",Data.getRestaurant_name());
        cvData.put("createday",Data.getCreateDate().toString());
        cvData.put("update",Data.getUpdateDate().toString());
        cvData.put("price",Data.getPrice());
        cvData.put("status",Data.getStatus());
        cvData.put("imageurl",Data.getImg_url());
        cvData.put("buy",Buy);
        cvData.put("tab",Tab);
        sqldb.insert(sDATABASETABLE,null,cvData);
        sqldb.close();
        //Toast.makeText(Food.Fcontext,"Insert Sucess",Toast.LENGTH_SHORT).show();
    }

//    public String GetData(int Id, int Tab){
//
//        Cursor cGet=sqldb.rawQuery("select * from "+ sDATABASETABLE+ " where id="+Id, null);
//        cGet.moveToFirst();
//        return cGet.getString(0);
//    }

    public ArrayList<Item> GetAllData(String Condition)
    {
        ArrayList<Item> liData= new ArrayList<Item>();
        SQLiteDatabase db=this.getWritableDatabase();
        try {
            Cursor cGet=db.rawQuery("Select * from "+sDATABASETABLE+" where "+Condition,null);
            Item iTemp;
            if(cGet.moveToFirst())
            {
                do {
                    iTemp=new Item();
                    try {
                        //iTemp.SetItem(cGet.getString(0),cGet.getString(1),cGet.getString(2), new Date(cGet.getString(3)),new Date(cGet.getString(3)),cGet.getDouble(5),cGet.getInt(6),cGet.getString(7));
                        Toast.makeText(Food.Fcontext,cGet.getString(3),Toast.LENGTH_LONG).show();
                        iTemp.SetItem(cGet.getString(0),cGet.getString(1),cGet.getString(2), new Date(cGet.getString(3)),new Date(cGet.getString(4)),cGet.getDouble(5),cGet.getInt(6),cGet.getString(7));
                    }
                    catch (Exception e){
                        Toast.makeText(Food.Fcontext,"Get fail",Toast.LENGTH_SHORT).show();
                    }
                    liData.add(iTemp);
                }while(cGet.moveToNext());
            }

        }catch (Exception e){
            onUpgrade(db,0,1);
        }

        return liData;
    }

    public void UpdateDate(Item Data, int Tab, int Buy) //buy
    {
        ContentValues cvData=new ContentValues();
        cvData.put("id",Data.getId());
        cvData.put("fname",Data.getName());
        cvData.put("rname",Data.getRestaurant_name());
        cvData.put("createday",Data.getCreateDate().toString());
        cvData.put("update",Data.getUpdateDate().toString());
        cvData.put("price",Data.getPrice());
        cvData.put("status",Data.getStatus());
        cvData.put("imageurl",Data.getImg_url());
        cvData.put("buy",Buy);
        cvData.put("tab",Tab);
        sqldb.update(sDATABASETABLE,cvData,"id="+Data.getId(),null);
    }

    public void DropRow(String Condition)
    {
        sqldb.delete(sDATABASETABLE,"where "+Condition,null);
    }

    public void RemoveTable()
    {
        onUpgrade(sqldb,0,1);
    }
}
