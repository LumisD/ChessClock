package com.lumisdinos.tabletransform.common.extension

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import timber.log.Timber
import java.io.File

fun Context.getFileName(uri: Uri?): String {
    if (uri == null) return ""
    return when (uri.scheme) {
        ContentResolver.SCHEME_FILE -> {
            //Timber.d("qwer getFileName SCHEME_FILE: %s", File(uri.path!!).extension)
            File(uri.path!!).name
        }
        ContentResolver.SCHEME_CONTENT -> {
            //Timber.d("qwer getFileName SCHEME_CONTENT")
            getCursorContent(uri)
        }
        else -> null
    } ?: ""
}


private fun Context.getCursorContent(uri: Uri): String? = try {
    contentResolver.query(uri, null, null, null, null)?.let { cursor ->
        cursor.run {
            if (moveToFirst()) getString(getColumnIndex(OpenableColumns.DISPLAY_NAME))
            else null
        }.also { cursor.close() }
    }
} catch (e: Exception) {
    null
}


fun Context.getMimeType(uri: Uri?): String {
    if (uri == null) return ""
    return when (uri.scheme) {
        ContentResolver.SCHEME_FILE -> {
            //Timber.d("qwer getFileName SCHEME_FILE: %s", File(uri.path!!).extension)
            File(uri.path!!).extension
        }
        ContentResolver.SCHEME_CONTENT -> {
            //Timber.d("qwer getMimeType SCHEME_CONTENT")
            contentResolver.getType(uri)
        }
        else -> null
    } ?: ""
}