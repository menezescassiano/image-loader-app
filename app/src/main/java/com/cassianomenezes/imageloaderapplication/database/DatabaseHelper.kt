package com.cassianomenezes.imageloaderapplication.database

import android.content.Context
import android.graphics.Bitmap
import com.cassianomenezes.imageloaderapplication.utils.bitmapToBase64

class DatabaseHelper(context: Context) {

    private val database: SQLiteDatabaseHandler = SQLiteDatabaseHandler(context)

    fun saveIntoDatabase(url: String, bitmap: Bitmap, removeFirst: Boolean) {
        database.addDog(url, bitmapToBase64(bitmap), bitmap.byteCount, removeFirst)
    }

    fun getUrl(url: String): String? {
        return database.getUrl(url)
    }

    fun allDogs(): List<String> {
        return database.allDogs
    }

    fun dbSize(): Int {
        return database.dbSize
    }

}
