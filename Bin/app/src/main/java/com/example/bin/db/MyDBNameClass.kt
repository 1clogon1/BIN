package com.example.dbsqlite_v1.db

import android.provider.BaseColumns

object MyDBNameClass: BaseColumns {
    //Данные для базы
    const val DATABASE_VERSION = 3
    const val DATABASE_NAME = "MyLessonDb.db"

    //Данные для таблицы
    const val TABLE_NAME = "my_table"
    const val COLUMN_NAME_BIN = "bin"


    //Создание базы данных
    const val CREATE_TABLE =
        "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "$COLUMN_NAME_BIN TEXT)"



    const val DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}