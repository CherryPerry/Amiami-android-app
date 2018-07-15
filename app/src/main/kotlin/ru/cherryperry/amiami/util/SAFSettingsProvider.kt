package ru.cherryperry.amiami.util

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.Fragment
import com.google.gson.Gson
import ru.cherryperry.amiami.AppPrefs
import ru.cherryperry.amiami.model.ExportedSettings
import java.nio.charset.Charset
import java.util.ArrayList
import java.util.TreeSet

object SAFSettingsProvider {
    val RC_CREATE_DOCUMENT = 100
    val RC_OPEN_DOCUMENT = 101
    val FILE_NAME = "amimai.settings.json"
    val CHARSET = Charset.forName("utf8")

    fun isAvailable(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun requestCreateDocument(activity: Activity) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/json"
        intent.putExtra(Intent.EXTRA_TITLE, FILE_NAME)
        activity.startActivityForResult(intent, RC_CREATE_DOCUMENT)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun requestCreateDocument(fragment: Fragment) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/json"
        intent.putExtra(Intent.EXTRA_TITLE, FILE_NAME)
        fragment.startActivityForResult(intent, RC_CREATE_DOCUMENT)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun onRequestCreateDocumentComplete(context: Context, requestCode: Int, resultCode: Int, resultData: Intent?): Result {
        if (requestCode == RC_CREATE_DOCUMENT && resultCode == Activity.RESULT_OK && resultData != null && resultData.data != null) {
            val uri = resultData.data
            context.contentResolver.openOutputStream(uri).use {
                try {
                    val prefs = AppPrefs(context)
                    val settings = ExportedSettings()
                    settings.highlight = ArrayList(prefs.favoriteList)
                    val jsonString = Gson().toJson(settings)
                    val writer = it.bufferedWriter(CHARSET)
                    writer.write(jsonString)
                    writer.flush()
                } catch (e: Exception) {
                    return Result.Error
                }
            }
            return Result.OK
        }
        return Result.Skip
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun requestOpenDocument(activity: Activity) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        activity.startActivityForResult(intent, RC_OPEN_DOCUMENT)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun requestOpenDocument(fragment: Fragment) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        fragment.startActivityForResult(intent, RC_OPEN_DOCUMENT)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun onRequestOpenDocumentComplete(context: Context, requestCode: Int, resultCode: Int, resultData: Intent?): Result {
        if (requestCode == RC_OPEN_DOCUMENT && resultCode == Activity.RESULT_OK && resultData != null && resultData.data != null) {
            val uri = resultData.data
            context.contentResolver.openInputStream(uri).use {
                try {
                    val settings = Gson().fromJson(it.reader(CHARSET), ExportedSettings::class.java)
                    if (settings.highlight != null) {
                        val prefs = AppPrefs(context)
                        prefs.favoriteList = TreeSet(settings.highlight)
                    }
                } catch (e: Exception) {
                    return Result.Error
                }
            }
            return Result.OK
        }
        return Result.Skip
    }

    enum class Result {
        OK, Error, Skip
    }
}