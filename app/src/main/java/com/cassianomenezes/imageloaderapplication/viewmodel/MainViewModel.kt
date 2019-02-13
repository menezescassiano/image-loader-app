package com.cassianomenezes.imageloaderapplication.viewmodel

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.graphics.drawable.Drawable
import com.cassianomenezes.imageloaderapplication.R
import com.cassianomenezes.imageloaderapplication.database.AppPreferences
import com.cassianomenezes.imageloaderapplication.database.DatabaseHelper
import com.cassianomenezes.imageloaderapplication.entries.DogResponse
import com.cassianomenezes.imageloaderapplication.network.RetrofitClient
import com.cassianomenezes.imageloaderapplication.utils.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel(private val context: Context) : BaseObservable() {

    var image = ObservableField<Drawable>(getDrawableBitmap(context, context.getDrawable(R.drawable.ic_dog)))
    var running = ObservableBoolean(false)
    private var databaseHelper = DatabaseHelper(context)
    private val appPreferences = AppPreferences(context)

    init {
        if (context.hasInternetConnection()) {
            getUrl()
        } else {
            handleError()
        }

    }

    private fun getUrl() {
        val call = RetrofitClient.getInstance().model.fetchImageUrl()
        running.set(true)
        call.enqueue(object : Callback<DogResponse> {
            override fun onResponse(call: Call<DogResponse>, response: Response<DogResponse>) {
                if (response.isSuccessful) {
                    handleGetUrlSuccess(response)
                } else {
                    handleError()
                }
            }
            override fun onFailure(call: Call<DogResponse>, t: Throwable) {
                running.set(false)
                handleError()
            }
        })
    }

    private fun getImage(url: String) {
        running.set(true)
        val call = RetrofitClient.getInstance().model.fetchImage(url)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                running.set(false)
                if (response.isSuccessful) {
                    handleGetImageSuccess(url, response)
                } else {
                    handleError()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                running.set(false)
                handleError()
            }
        })
    }

    private fun handleGetUrlSuccess(response: Response<DogResponse>) {
        val base64 = response.body()?.url?.let { databaseHelper.getUrl(it) }
        base64?.let {
            image.set(generateBitmapDrawable(context, base64ToBitmap(base64)))
            running.set(false)
        } ?: response.body()?.url?.let { getImage(it) } ?: handleError()
    }

    private fun handleGetImageSuccess(url: String, response: Response<ResponseBody>) {
        response.body()?.let {
            val bitmap = generateBitmap(it.byteStream())
            image.set(generateBitmapDrawable(context, bitmap))
            if (bitmap.byteCount + databaseHelper.dbSize() <= appPreferences.getMaxSize()) {
                databaseHelper.saveIntoDatabase(url, bitmap, false) //insert a new row
            } else {
                databaseHelper.saveIntoDatabase(url, bitmap, true) //insert a new row and remove first row
            }
        } ?: handleError()
    }

    fun next() {
        image.set(getDrawableBitmap(context, context.getDrawable(R.drawable.ic_dog)))
        getUrl()
    }

    private fun handleError() {
        showAlertDialog(context,
            context.getString(R.string.alert_dialog_title),
            context.getString(R.string.alert_dialog_text))
    }
}