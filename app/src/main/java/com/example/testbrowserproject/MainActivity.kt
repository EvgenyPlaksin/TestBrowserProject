package com.example.testbrowserproject

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.example.testbrowserproject.check.ConnectionCheck
import com.example.testbrowserproject.check.utils.Variables.REQUEST_SELECT_FILE
import com.example.testbrowserproject.check.utils.Variables.uploadMessage
import com.example.testbrowserproject.client.WebClient
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val cld = ConnectionCheck(application)

        cld.observe(this) { isConnected ->

            if (isConnected) {

                textView.visibility = View.INVISIBLE

                val pmmCookies: CookieManager = CookieManager.getInstance()
                CookieManager.setAcceptFileSchemeCookies(true)
                pmmCookies.setAcceptThirdPartyCookies(web_browser, true)
                web_browser.settings.apply {
                    useWideViewPort = true
                    javaScriptEnabled = true
                    mixedContentMode = 0
                    loadWithOverviewMode = true
                    allowFileAccess = true
                    domStorageEnabled = true
                    defaultTextEncodingName = "utf-8"
                    databaseEnabled = true
                    allowFileAccessFromFileURLs = true
                    setAppCacheEnabled(true)
                    javaScriptCanOpenWindowsAutomatically = true
                }

                web_browser?.loadUrl("http://www.google.com")

                web_browser?.settings?.javaScriptEnabled = true // we need to enable javascript
                web_browser?.canGoBack()
                web_browser?.webViewClient = WebClient(this)
                web_browser?.setWebChromeClient(object : WebChromeClient() {

                    override fun onShowFileChooser(
                        webView: WebView?,
                        filePathCallback: ValueCallback<Array<Uri>>?,
                        fileChooserParams: FileChooserParams?
                    ): Boolean {
                        if (uploadMessage != null) {
                            uploadMessage!!.onReceiveValue(null)
                            uploadMessage = null
                        }
                        uploadMessage = filePathCallback
                        try {
                            startActivityForResult(
                                fileChooserParams!!.createIntent(),
                                REQUEST_SELECT_FILE
                            )
                        } catch (e: ActivityNotFoundException) {
                            uploadMessage = null
                            return false
                        }
                        return true
                    }

                })

            } else textView.visibility = View.VISIBLE
        }

    }

    override fun onBackPressed() {
        if (web_browser.canGoBack()) {
            web_browser.goBack()
        } else {
            Log.d("TAG", "can`t go back")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == REQUEST_SELECT_FILE) {
            if (uploadMessage == null) return
            uploadMessage!!.onReceiveValue(
                WebChromeClient.FileChooserParams.parseResult(
                    resultCode,
                    intent
                )
            )
            uploadMessage = null
        }
    }

}

