package com.example.dbsqlite_v1.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHelper(context: Context) : SQLiteOpenHelper(context, MyDBNameClass.DATABASE_NAME, null, MyDBNameClass.DATABASE_VERSION) {
    override fun onCreate(p0: SQLiteDatabase?) {//Создание базы данных
        p0?.execSQL(MyDBNameClass.CREATE_TABLE)//Создание
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {//Обновление базы данных
        p0?.execSQL(MyDBNameClass.DELETE_TABLE)//Удаление
        onCreate(p0)//Создание
    }


}