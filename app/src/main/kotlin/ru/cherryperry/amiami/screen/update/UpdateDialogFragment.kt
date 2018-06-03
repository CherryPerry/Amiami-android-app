package ru.cherryperry.amiami.screen.update

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialogFragment
import ru.cherryperry.amiami.R

/**
 * "Update is available!" dialog fragment
 */
class UpdateDialogFragment : AppCompatDialogFragment() {
    companion object {
        val KEY_URL = "URL"
        val KEY_NAME = "NAME"
        val KEY_VERSION = "VERSION"

        fun newInstance(version: String, name: String, url: String): UpdateDialogFragment {
            val bundle = Bundle()
            bundle.putString(KEY_VERSION, version)
            bundle.putString(KEY_NAME, name)
            bundle.putString(KEY_URL, url)
            val fragment = UpdateDialogFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val version = arguments?.getString(KEY_VERSION)
        val name = arguments?.getString(KEY_NAME)
        val url = arguments?.getString(KEY_URL)

        if (version == null || name == null || url == null) {
            return super.onCreateDialog(savedInstanceState)
        }

        return AlertDialog.Builder(context!!)
                .setTitle(R.string.update_dialog_title)
                .setMessage(context!!.getString(R.string.update_dialog_text, version, name))
                .setPositiveButton(R.string.update_dialog_download, { dialogInterface, i ->
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    } catch (ignored: Exception) {
                    }
                })
                .setNegativeButton(R.string.update_dialog_cancel, null)
                .create()
    }
}