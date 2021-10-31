package `in`.gov.uidai.auasample.utils

import `in`.gov.uidai.auasample.Constants
import `in`.gov.uidai.auasample.input.contract.CaptureResponse
import `in`.gov.uidai.auasample.input.contract.NameValue
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Base64
import kotlin.random.Random

class Utils private constructor() {

    companion object {
        var ENVIRONMENT_TAG = "S"
        var LANGUAGE = "en"
        var ENABLE_AUTO_CAPTURE = true
        fun formatCaptureResponse(response: CaptureResponse): String {

            val serverComputeTime = getTimeValueFromCustOption(
                response.custOpts.nameValues,
                "serverComputeTime"
            )
            val clientComputeTime =
                getTimeValueFromCustOption(
                    response.custOpts.nameValues,
                    "clientComputeTime"
                )

            val networkLatenecyTime =
                getTimeValueFromCustOption(
                    response.custOpts.nameValues,
                    "networkLatencyTime"
                )


            return if (response.isSuccess) {
                "Capture of the image and face liveness was successful for transaction - ${response.txnID} " +
                        "and and an encrypted PID data has been sent for further processing.." +
                        "\n\nServer computation time ${convertToSeconds(serverComputeTime)} " +
                        "\nClient computation time ${convertToSeconds(clientComputeTime)}" +
                        "\nNetwork latency time ${convertToSeconds(networkLatenecyTime)}" +
                        "\nTotal duration in AUA App ${convertToSeconds(totalTime.returnTotalTime())}"
            } else {
                "Capture failed for transaction - ${response.txnID} with error :" +
                        " ${response.errCode} - ${response.errInfo}. \n" +
                        "\n\nServer computation time ${convertToSeconds(serverComputeTime)} " +
                        "\nClient computation time ${convertToSeconds(clientComputeTime)}" +
                        "\nNetwork latency time ${convertToSeconds(networkLatenecyTime)}" +
                        "\nTotal duration in AUA App ${convertToSeconds(totalTime.returnTotalTime())}"
            }

        }

        private fun getTimeValueFromCustOption(nameValues: List<NameValue>, key: String): Long {
            try {
                return nameValues.first { it.name == key }.value.toLong();
            } catch (e: java.lang.Exception) {
                return -1L;
            }
        }

        private fun convertToSeconds(timeInMillis: Long): String {
            return "${timeInMillis / 1000f} secs"
        }

        fun createPidOptionForAuth(txnId: String): String {
            return createPidOptions(txnId, "auth")
        }

        fun createPidOptionForRegister(txnId: String): String {
            return createPidOptions(txnId, "register")
        }

        private fun createPidOptions(txnId: String, purpose: String): String {
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<PidOptions ver=\"1.0\" env=\"${ENVIRONMENT_TAG}\">\n" +
                    "   <Opts fCount=\"\" fType=\"\" iCount=\"\" iType=\"\" pCount=\"\" pType=\"\" format=\"\" pidVer=\"2.0\" timeout=\"\" otp=\"\" wadh=\"${Constants.WADH_KEY}\" posh=\"\" />\n" +
                    "   <CustOpts>\n" +
                    "      <Param name=\"txnId\" value=\"${txnId}\"/>\n" +
                    "      <Param name=\"purpose\" value=\"$purpose\"/>\n" +
                    "      <Param name=\"language\" value=\"$LANGUAGE\"/>\n" +
                    "      <Param name=\"enableAutoCapture\" value=\"$ENABLE_AUTO_CAPTURE\"/>\n" +
                    "   </CustOpts>\n" +
                    "</PidOptions>"
        }

        fun getTransactionID() = Random(System.currentTimeMillis()).nextInt(9999).toString()

        fun convertToBitmap(image: String): Bitmap {
            val decodedString: ByteArray = Base64.decode(image, Base64.NO_WRAP)
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        }

        fun queryName(resolver: ContentResolver, uri: Uri): String? {
            return queryColumnValue(resolver, uri, OpenableColumns.DISPLAY_NAME)
        }

        private fun queryColumnValue(
            resolver: ContentResolver,
            uri: Uri,
            columnName: String
        ): String? {
            resolver.query(uri, null, null, null, null)?.let { cursor ->
                cursor.moveToFirst()
                try {
                    return cursor.getString(cursor.getColumnIndex(columnName))
                } finally {
                    cursor.close()
                }
            } ?: run {
                return null
            }
        }
    }
}

interface totalTime {
    companion object {
        var startTime = 0L
        var stopTime = 0L
        fun returnTotalTime(): Long {
            return stopTime - startTime
        }
    }
}