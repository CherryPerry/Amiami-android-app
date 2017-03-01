package ru.cherryperry.amiami.util

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import com.google.gson.Gson
import ru.cherryperry.amiami.AppPrefs
import ru.cherryperry.amiami.model.ExportedSettings
import java.nio.charset.Charset
import java.util.*


object SAFSettingsProvider {
    val RC_CREATE_DOCUMENT = 100
    val RC_OPEN_DOCUMENT = 2
    val MIME_TYPE = "application/json"
    val FILE_NAME = "amimai.settings.json"

    fun isAvailable(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun requestCreateDocument(activity: Activity) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = MIME_TYPE
        intent.putExtra(Intent.EXTRA_TITLE, FILE_NAME)
        activity.startActivityForResult(intent, RC_CREATE_DOCUMENT)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun onReqestCreateDocumentComplete(context: Context, requestCode: Int, resultCode: Int, resultData: Intent?): Boolean {
        if (requestCode == RC_CREATE_DOCUMENT && resultCode == Activity.RESULT_OK && resultData != null && resultData.data != null) {
            val uri = resultData.data
            context.contentResolver.openOutputStream(uri).use {
                val prefs = AppPrefs(context)
                val settings = ExportedSettings()
                settings.highlight = ArrayList(prefs.favoriteList)
                val jsonString = Gson().toJson(settings)
                it.writer(Charset.forName("utf8")).write(jsonString)
            }
            return true
        }
        return false
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun requestOpenDocument(activity: Activity) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = MIME_TYPE
        intent.putExtra(Intent.EXTRA_TITLE, FILE_NAME)
        activity.startActivityForResult(intent, RC_CREATE_DOCUMENT)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun onReqestOpenDocumentComplete(context: Context, requestCode: Int, resultCode: Int, resultData: Intent?): Boolean {
        if (requestCode == RC_OPEN_DOCUMENT && resultCode == Activity.RESULT_OK && resultData != null && resultData.data != null) {
            val uri = resultData.data
            context.contentResolver.openInputStream(uri).use {
                try {
                    val settings = Gson().fromJson(it.reader(Charset.forName("utf8")), ExportedSettings::class.java)
                    if (settings.highlight != null) {
                        val prefs = AppPrefs(context)
                        prefs.favoriteList = TreeSet(settings.highlight)
                    }
                } catch (e: Exception) {
                    // TODO Alert return
                    return false
                }
            }
            return true
        }
        return false
    }
}