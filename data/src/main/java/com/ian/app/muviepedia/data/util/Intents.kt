package com.ian.app.muviepedia.data.util

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.app.ShareCompat
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 *
Created by Ian Damping on 03/10/2019.
Github = https://github.com/iandamping

Android system intents
 */
fun intentShareText(activity: Activity, text: String) {
    val shareIntent = ShareCompat.IntentBuilder.from(activity)
            .setText(text)
            .setType("text/plain")
            .createChooserIntent()
            .apply {
                // https://android-developers.googleblog.com/2012/02/share-with-intents.html
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // If we're on Lollipop, we can open the intent as a document
                    addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                } else {
                    // Else, we will use the old CLEAR_WHEN_TASK_RESET flag
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
                }
            }
    activity.startActivity(shareIntent)
}


fun intentShareImageAndText(scope: CoroutineScope, activity: Activity?, tittle: String?, message: String?, imageUrl: String) {
    scope.launch {
        try {
            withContext(Dispatchers.IO) {
                if (activity != null) {
                    val bitmap = Glide.with(activity)
                            .asBitmap()
                            .load(imageUrl)
                            .submit(512, 512)
                            .get()
                    if (getLocalBitmapUri(bitmap) != null) {
                        withContext(Dispatchers.Main) {
                            val sharingIntent = Intent(Intent.ACTION_SEND)
                            sharingIntent.type = "image/*"
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap))
                            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, tittle)
                            sharingIntent.putExtra(Intent.EXTRA_TEXT, message)
                            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            activity.startActivity(Intent.createChooser(sharingIntent, "Share Image"))
                        }

                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

private fun getLocalBitmapUri(bmp: Bitmap): Uri? {
    var bmpUri: Uri? = null
    val imageFile = File(Environment.getExternalStorageDirectory(), "sharedImage" + System.currentTimeMillis() + ".jpeg")
    try {
        val out = FileOutputStream(imageFile)
        bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
        out.close()
        bmpUri = Uri.fromFile(imageFile)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return bmpUri
}


fun intentOpenWebsite(activity: Activity, url: String) {
    val openURL = Intent(Intent.ACTION_VIEW)
    openURL.data = Uri.parse(url)
    activity.startActivity(openURL)
}
