package com.cassianomenezes.imageloaderapplication.utils

import android.content.Context
import android.content.res.Resources
import android.databinding.ViewDataBinding
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.InputStream


fun AppCompatActivity.bindingContentView(layout: Int): ViewDataBinding {
    return android.databinding.DataBindingUtil.setContentView<ViewDataBinding>(this, layout)
}

fun convertDpToPixel(dp: Float): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}

fun getRoundedCroppedBitmap(bitmap: Bitmap): Bitmap {
    val targetWidth = convertDpToPixel(300f)
    val targetHeight = convertDpToPixel(300f)
    var targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,Bitmap.Config.ARGB_8888)
    val canvas = Canvas(targetBitmap)
    val path = Path()
    path.addCircle(((targetWidth - 1) / 2).toFloat(), ((targetHeight - 1) / 2).toFloat(), (Math.min(targetWidth, targetHeight) / 2).toFloat(),
        Path.Direction.CCW)

    canvas.clipPath(path)
    canvas.drawBitmap(
        bitmap,
        Rect(0, 0, bitmap.width, bitmap.height), Rect(0, 0, targetWidth, targetHeight), null
    )
    return targetBitmap

}

fun getDrawableBitmap(context: Context, drawable: Drawable): BitmapDrawable {
    val bitmap = (drawable as BitmapDrawable).bitmap
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    val cropImage = getRoundedCroppedBitmap(bitmap)
    return BitmapDrawable(context.resources, cropImage)
}

fun generateBitmapDrawable(context: Context, bitmap: Bitmap): BitmapDrawable {
    val cropImage = getRoundedCroppedBitmap(bitmap)
    return BitmapDrawable(context.resources, cropImage)
}

fun generateBitmap(inputStream: InputStream): Bitmap{
    return BitmapFactory.decodeStream(inputStream)
}

fun showAlertDialog(context: Context, title: String, message: String) {
    AlertDialog.Builder(context).apply {
        setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
    }.show()
}

fun bitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun base64ToBitmap(b64: String): Bitmap {
    val imageAsBytes = Base64.decode(b64.toByteArray(), Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
}

fun Context.hasInternetConnection(): Boolean {
    getSystemService(Context.CONNECTIVITY_SERVICE)?.let {
        return (it as ConnectivityManager).activeNetworkInfo?.isConnected ?: false
    } ?: return false
}