package com.example.testbrowserproject.check.utils

import android.net.Uri
import android.webkit.ValueCallback

object Variables {
    var uploadMessage: ValueCallback<Array<Uri>>? = null
    var mUploadMessage: ValueCallback<Uri>? = null
    val REQUEST_SELECT_FILE = 100
    val FILECHOOSER_RESULTCODE = 1
    var url_2: String? = null
    var fileornot = false
}