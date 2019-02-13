package com.cassianomenezes.imageloaderapplication.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import java.util.ArrayList

class SQLiteDatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "dog_db"
        private const val TABLE_NAME = "dogs"
        private const val KEY_ID = "id"
        private const val KEY_URL = "url"
        private const val KEY_BASE64_IMAGE = "base64image"
        private const val KEY_IMAGE_SIZE = "imageSize"
        private val COLUMNS = arrayOf(KEY_ID, KEY_URL, KEY_BASE64_IMAGE, KEY_IMAGE_SIZE)
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_URL + " TEXT, "
                + KEY_BASE64_IMAGE + " TEXT, "
                + KEY_IMAGE_SIZE + " INTEGER )")

        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        this.onCreate(db)
    }

    val allDogs: List<String>
        get() {

            val dogsList = ArrayList<String>()
            val query = "SELECT  * FROM $TABLE_NAME"
            val db = this.writableDatabase
            val cursor = db.rawQuery(query, null)
            var dog: String

            if (cursor.moveToFirst()) {
                do {
                    dog = cursor.getString(1)
                    dogsList.add(dog)
                } while (cursor.moveToNext())
            }

            db.close()

            return dogsList
        }

    val dbSize: Int
        get() {

            val query = "SELECT  * FROM $TABLE_NAME"
            val db = this.writableDatabase
            val cursor = db.rawQuery(query, null)
            var size = 0

            if (cursor.moveToFirst()) {
                do {
                    size += cursor.getInt(3)
                } while (cursor.moveToNext())
            }

            db.close()

            return size
        }

    fun getUrl(url: String): String? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME, COLUMNS,
            " url = ?",
            arrayOf(url), null, null, null, null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            cursor.getString(2)
        } else null
    }

    fun addDog(url: String, base64: String, size: Int, removeFirst: Boolean) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_URL, url)
        values.put(KEY_BASE64_IMAGE, base64)
        values.put(KEY_IMAGE_SIZE, size)
        db.insert(TABLE_NAME, null, values)

        if (removeFirst) {
            val query = "SELECT  * FROM $TABLE_NAME"
            val db = this.writableDatabase
            val cursor = db.rawQuery(query, null)
            cursor.moveToFirst()
            db.delete(TABLE_NAME, "$KEY_URL=?", arrayOf(cursor.getString(1)))
        }

        db.close()
    }
}
