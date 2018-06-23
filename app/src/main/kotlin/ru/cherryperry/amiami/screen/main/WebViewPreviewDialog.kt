package ru.cherryperry.amiami.screen.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.URLUtil
import android.webkit.WebView
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.util.ViewDelegate
import ru.cherryperry.amiami.util.findViewById

class WebViewPreviewDialog : DialogFragment() {
    private val webView by ViewDelegate<WebView>(R.id.webView)
    private var url: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_webview, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.apply {
            url = getString(KEY_URL, null)
            if (url != null) webView.loadUrl(url)
        }

        // No title for dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Open button
        findViewById<View>(R.id.openButton).setOnClickListener {
            if (URLUtil.isNetworkUrl(url)) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                if (intent.resolveActivity(context!!.packageManager) != null)
                    startActivity(intent)
            }
        }

        // Close button
        findViewById<View>(R.id.closeButton).setOnClickListener {
            dismiss()
        }
    }

    companion object {
        private val KEY_URL = "url"

        fun newInstance(url: String): WebViewPreviewDialog {
            val dialog = WebViewPreviewDialog()
            val bundle = Bundle()
            bundle.putString(KEY_URL, url)
            dialog.arguments = bundle
            return dialog
        }
    }
}
