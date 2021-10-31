package `in`.gov.uidai.auasample.utils

import android.content.ContentResolver
import android.content.res.AssetManager
import android.net.Uri
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

fun AssetManager.readEKYCData(fileName: String): String {
    var tContents: String = ""
    try {
        val stream: InputStream = open(fileName)
        val size: Int = stream.available()
        val buffer = ByteArray(size)
        stream.read(buffer)
        stream.close()
        tContents = String(buffer)
    } catch (e: IOException) {
        // Handle exceptions here
    }
    return tContents
}

fun Uri.readAsText(contentResolver: ContentResolver): String? {

    try {
        if (!"text/xml".equals(contentResolver.getType(this)))
            return null
        contentResolver.openInputStream(this)?.let {
            val bufferedReader = BufferedReader(InputStreamReader(it))
            val text = StringBuilder()
            var currentLine: String? = null

            while (bufferedReader.readLine().also { currentLine = it } != null) {
                text.append(currentLine)
            }
            bufferedReader.close()
            it.close()
            return text.toString()
        }

    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return null
}