package ru.cherryperry.amiami.presentation.update

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.domain.model.UpdateInfo

/**
 * "Update is available!" dialog fragment
 */
class UpdateDialogFragment : DialogFragment() {

    companion object {
        private const val KEY_URL = "URL"
        private const val KEY_NAME = "NAME"
        private const val KEY_VERSION = "VERSION"

        fun newInstance(updateInfo: UpdateInfo): UpdateDialogFragment {
            val bundle = Bundle()
            bundle.putString(KEY_VERSION, updateInfo.version.toString())
            bundle.putString(KEY_NAME, updateInfo.name)
            bundle.putString(KEY_URL, updateInfo.url)
            val fragment = UpdateDialogFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments?.also {
            val version = it.getString(KEY_VERSION)
            val name = it.getString(KEY_NAME)
            val url = it.getString(KEY_URL)
            val context = this.context!!
            return AlertDialog.Builder(context)
                .setTitle(R.string.update_dialog_title)
                .setMessage(context.getString(R.string.update_dialog_text, version, name))
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
        return super.onCreateDialog(savedInstanceState)
    }
}
