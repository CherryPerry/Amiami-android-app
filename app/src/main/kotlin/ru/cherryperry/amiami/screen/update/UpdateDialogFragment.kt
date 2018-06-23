package ru.cherryperry.amiami.screen.update

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialogFragment
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.domain.update.UpdateInfo

/**
 * "Update is available!" dialog fragment
 */
class UpdateDialogFragment : AppCompatDialogFragment() {
    companion object {
        const val KEY_URL = "URL"
        const val KEY_NAME = "NAME"
        const val KEY_VERSION = "VERSION"

        fun newInstance(updateInfo: UpdateInfo): UpdateDialogFragment {
            val bundle = Bundle()
            bundle.putString(KEY_VERSION, updateInfo.tagName)
            bundle.putString(KEY_NAME, updateInfo.name)
            bundle.putString(KEY_URL, updateInfo.url)
            val fragment = UpdateDialogFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val version = arguments?.getString(KEY_VERSION)
        val name = arguments?.getString(KEY_NAME)
        val url = arguments?.getString(KEY_URL)
        val context = this.context
        if (version == null || name == null || url == null || context == null) {
            return super.onCreateDialog(savedInstanceState)
        }
        return AlertDialog.Builder(context)
                .setTitle(R.string.update_dialog_title)
                .setMessage(context!!.getString(R.string.update_dialog_text, version, name))
                .setPositiveButton(R.string.update_dialog_download) { _, _ ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    if (context.packageManager
                                    .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                                    .isNotEmpty()) {
                        startActivity(intent)
                    }
                }
                .setNegativeButton(R.string.update_dialog_cancel, null)
                .create()
    }
}