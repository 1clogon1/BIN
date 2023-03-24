package com.example.dbsqlite_v1.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.bin.model.ListItem

class MyDBManager(context: Context) {
    val myDBHelper = MyDBHelper(context)
    var db: SQLiteDatabase? = null

    //Открываем базу данных для записей
    fun openDB() {
        db = myDBHelper.writableDatabase
    }

    //Запись(передача данных) в базу данных
    fun insertToDB(title: String) {
        val value = ContentValues().apply {
            put(MyDBNameClass.COLUMN_NAME_BIN, title)
        }
        db?.insert(MyDBNameClass.TABLE_NAME, null, value)
    }

    //Удаенеи определеного BIN
    fun removeBINDB(id: String) {
        val selection = BaseColumns._ID + "=$id" //Удаляем там где индефикатор равен моему индификатору
        db?.delete(MyDBNameClass.TABLE_NAME, selection, null)
    }

    /*//Удаенеи определеного BIN если такой уже был найден
    fun removeBINDB2(bin: String) {
        val selection = BaseColumns._ID + "=$bin" //Удаляем там где индефикатор равен моему индификатору
        db?.delete(MyDBNameClass.TABLE_NAME, selection, null)
    }*/

    //Считывание
    fun readDBData(): ArrayList<ListItem> {
        val dataList = ArrayList<ListItem>()

        val cursor = db?.query(
            MyDBNameClass.TABLE_NAME,  // The table to query
            null,              // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,           // The values for the WHERE clause
            null,              // don't group the rows
            null,               // don't filter by row groups
            null               // The sort order
        )

        //Заполняем базу
            while (cursor?.moveToNext()!!) {
                val dataid = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID))//Считывается id
                val dataBin = cursor.getString(cursor.getColumnIndexOrThrow(MyDBNameClass.COLUMN_NAME_BIN))//Считывается Bin

                val item = ListItem()
                item.id = dataid
                item.bin = dataBin
                dataList.add(item)
            }
        cursor.close()
        return dataList
    }

    //Закрытие базы данных
    fun closeDB(){
        myDBHelper.close()
    }
}




















