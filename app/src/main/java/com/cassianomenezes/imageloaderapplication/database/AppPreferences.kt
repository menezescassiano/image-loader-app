package com.cassianomenezes.imageloaderapplication.database

import android.content.Context
import android.content.SharedPreferences
import com.cassianomenezes.imageloaderapplication.utils.Constants
import com.cassianomenezes.imageloaderapplication.utils.Constants.Companion.MAX_CACHE_SIZE

class AppPreferences(context: Context) {

    private val appPreferencesMaxSize: SharedPreferences = context.getSharedPreferences(Constants.MAX_SIZE_CACHE_PREFERENCE, Context.MODE_PRIVATE)

    fun getMaxSize(): Int {
        return appPreferencesMaxSize.getInt(Constants.MAX_SIZE_CACHE_PREFERENCE, MAX_CACHE_SIZE) //in KB
    }

    fun setMaxSize(size: Int) {
        appPreferencesMaxSize.edit().putInt(Constants.MAX_SIZE_CACHE_PREFERENCE, size).apply()
    }
}