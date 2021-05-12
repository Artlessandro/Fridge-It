package com.example.fridgeit

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf

class DatabaseHelper(context: Context?): SQLiteOpenHelper(
    context,
    Constants.DB_NAME,
    null,
    Constants.DB_VERSION)
{
    override fun onCreate(db: SQLiteDatabase?)
    {
        db!!.execSQL(Constants.CREATE_TABLE1)
        db!!.execSQL(Constants.CREATE_TABLE2)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)
    {
        db!!.execSQL("DROP TABLE IF EXISTS "+Constants.TABLE_NAME1)
        db!!.execSQL("DROP TABLE IF EXISTS "+Constants.TABLE_NAME2)
        onCreate(db)
    }
    fun insertInfo(
        name:String?,
        type:String?,
        expireDate:String?,
        createDate:String?
    ):Long{
        val db = this.writableDatabase
        val values  = ContentValues()
        values.put(Constants.C_NAME,name)
        values.put(Constants.C_TYPE,type)
        values.put(Constants.C_EXPIRE_DATE,expireDate)
        values.put(Constants.C_CREATED_DATE,createDate)
        val id = db.insert(Constants.TABLE_NAME1,null,values)
        db.close()
        return id
    }
    fun insertShoppigItem(name: String?,done:String?):Long
    {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(Constants.S_NAME,name)
        values.put(Constants.S_DONE,done)
        val id = db.insert(Constants.TABLE_NAME2,null,values)
        db.close()
        return id
    }
    fun getAllShoppingItems():ArrayList<ModelShopping>
    {
        val itemList = ArrayList<ModelShopping>()
        val selectQ = "SELECT * FROM ${Constants.TABLE_NAME2}"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQ,null)
        if (cursor.moveToNext())
        {
            do {
                val modelItem = ModelShopping(
                    ""+cursor.getInt(cursor.getColumnIndex(Constants.S_ID)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.S_NAME)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.S_DONE))
                )
                itemList.add(modelItem)
            }while (cursor.moveToNext());
        }
        db.close()
        return itemList


    }
    fun updateShoppingItem(id: String,done: String?){
        val query = "UPDATE ${Constants.TABLE_NAME2} SET ${Constants.S_DONE}='${done}' WHERE ${Constants.S_ID}=${id.toInt()}"
        val db = this.writableDatabase
        db.execSQL(query)
        db.close()
    }
    fun deleteShoppingItem(id: String)
    {
        val  sqlUpdateQuery = "DELETE FROM ${Constants.TABLE_NAME2}  WHERE ${Constants.S_ID}=${id.toInt()}"
        val db = this.writableDatabase
        db.execSQL(sqlUpdateQuery)
        db.close()
    }
    fun updateIte(id:String,name: String,expireDate: String)
    {
        val sqlUpdateQuery = "UPDATE ${Constants.TABLE_NAME1} SET ${Constants.C_NAME}='${name}',${Constants.C_EXPIRE_DATE}='${expireDate}' WHERE ${Constants.C_ID}=${id.toInt()}"
        val db = this.writableDatabase
        db.execSQL(sqlUpdateQuery)
        db.close()
    } fun deleteItem(id:String)
    {
        val sqlUpdateQuery = "DELETE FROM ${Constants.TABLE_NAME1}  WHERE ${Constants.C_ID}=${id.toInt()}"
        val db = this.writableDatabase
        db.execSQL(sqlUpdateQuery)
        db.close()
    }
    fun getAllRecords (type: String):ArrayList<ModelItem>
    {
        val itemList = ArrayList<ModelItem>()
        val selectQuery = "SELECT * FROM ${Constants.TABLE_NAME1} WHERE ${Constants.C_TYPE}='${type}'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery,null)
        if (cursor.moveToNext())
        {
            do {
                val modelItem = ModelItem(
                    ""+cursor.getInt(cursor.getColumnIndex(Constants.C_ID)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.C_NAME)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.C_EXPIRE_DATE)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.C_CREATED_DATE))
                )
                 itemList.add(modelItem)
            }while (cursor.moveToNext());
        }
        db.close()
        return itemList
    }


    fun searchData (type: String,query: String):ArrayList<ModelItem>
    {
        val itemList = ArrayList<ModelItem>()
        val selectQuery = "SELECT * FROM ${Constants.TABLE_NAME1} WHERE ${Constants.C_TYPE}='${type}' AND ${Constants.C_NAME} LIKE '$query%'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery,null)
        if (cursor.moveToNext())
        {
            do {
                val modelItem = ModelItem(
                    ""+cursor.getInt(cursor.getColumnIndex(Constants.C_ID)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.C_NAME)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.C_EXPIRE_DATE)),
                    ""+cursor.getString(cursor.getColumnIndex(Constants.C_CREATED_DATE))
                )
                 itemList.add(modelItem)
            }while (cursor.moveToNext());
        }
        db.close()
        return itemList
    }

}