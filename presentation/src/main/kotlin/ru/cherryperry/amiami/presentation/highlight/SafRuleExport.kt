package ru.cherryperry.amiami.presentation.highlight

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.Fragment
import java.io.InputStream
import java.io.OutputStream

class SafRuleExport {

    companion object {
        private const val RC_CREATE_DOCUMENT = 100
        private const val RC_OPEN_DOCUMENT = 101
        private const val FILE_NAME = "amimai.settings.json"
    }

    fun isAvailable(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun requestCreateDocument(fragment: Fragment) {
        val intent = Intent().apply {
            action = Intent.ACTION_CREATE_DOCUMENT
            type = "application/json"
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_TITLE, FILE_NAME)
        }
        fragment.startActivityForResult(intent, RC_CREATE_DOCUMENT)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun onRequestCreateDocumentComplete(
        context: Context,
        requestCode: Int,
        resultCode: Int,
        resultData: Intent,
        onSuccess: (OutputStream) -> Unit
    ) {
        if (requestCode == RC_CREATE_DOCUMENT && resultCode == Activity.RESULT_OK && resultData.data != null) {
            val uri = resultData.data
            context.contentResolver.openOutputStream(uri)?.let(onSuccess)
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun requestOpenDocument(fragment: Fragment) {
        val intent = Intent().apply {
            action = Intent.ACTION_OPEN_DOCUMENT
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        fragment.startActivityForResult(intent, RC_OPEN_DOCUMENT)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun onRequestOpenDocumentComplete(
        context: Context,
        requestCode: Int,
        resultCode: Int,
        resultData: Intent,
        onSuccess: (InputStream) -> Unit
    ) {
        if (requestCode == RC_OPEN_DOCUMENT && resultCode == Activity.RESULT_OK && resultData.data != null) {
            val uri = resultData.data
            context.contentResolver.openInputStream(uri)?.let(onSuccess)
        }

    }
}
