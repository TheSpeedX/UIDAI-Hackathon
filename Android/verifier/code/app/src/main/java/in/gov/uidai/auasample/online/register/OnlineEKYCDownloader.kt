package `in`.gov.uidai.auasample.online.register

import `in`.gov.uidai.auasample.javamethods.DataDecryptor
import `in`.gov.uidai.auasample.javamethods.XstreamCommonMethos
import `in`.gov.uidai.auasample.network.VolleyRestApi
import `in`.gov.uidai.auasample.settings.auaConfig.ConfigUtils
import `in`.gov.uidai.auasample.settings.auaConfig.ConfigUtils.Companion.getSelectedConfigEnv
import android.content.Context
import android.util.Base64
import com.android.volley.VolleyError

class OnlineEKYCDownloader {

    private val RAR_DOESNT_EXIST = "Rar doesn't exist"
    private val EKYC_ERROR_CODE = "Ekyc Error Code - "
    private val AUTH_ERROR_CODE = "Auth Error Code - "

    fun downloadEKYCDocument(
        uid: String,
        pidData: String,
        context: Context,
        success: (String) -> Unit,
        failure: (ErrorInfo) -> Unit
    ) {
        val pidBlock = XstreamCommonMethos.processPidBlock(pidData, uid, false, context)
        val url = constructUrl(uid)

        val volleyRestApi = VolleyRestApi()
        volleyRestApi.volleyWebservice(
            context,
            url,
            pidBlock,
            object : VolleyRestApi.VolleyCallback {
                override fun onSuccess(result: Any) {
                    return processResponse(result, context, failure, success)
                }

                override fun onFailure(error: VolleyError) {
                    failure(ErrorInfo("500", error.localizedMessage, RAR_DOESNT_EXIST))
                }
            })
    }

    private fun processResponse(
        result: Any,
        context: Context,
        failure: (ErrorInfo) -> Unit,
        success: (String) -> Unit
    ) {
        val resp = XstreamCommonMethos.respXmlToPojo(result.toString())
        val authResponse = decode(resp.kycRes)

        if (!resp.isSuccess) {
            val errorInfo = extractErrorInfo(String(authResponse))
            return failure(errorInfo)
        }

        val dataDecryptor = DataDecryptor(context)
        val ekycXML = String(dataDecryptor.decrypt(authResponse))

        val success = dataDecryptor.verify(ekycXML)
        if (!success) {
            return failure(
                ErrorInfo(
                    "K-569",
                    "Digital signature verification failed for e-KYC XML",
                    RAR_DOESNT_EXIST
                )
            )
        }
        return success(ekycXML)
    }

    private fun decode(encodedString: String?) = Base64.decode(encodedString, Base64.DEFAULT)

    private fun constructUrl(uid: String): String {
        val configParams = ConfigUtils.getConfigData(getSelectedConfigEnv())
        val slash = "/"
        return configParams?.eKycUrl + slash + configParams?.auaCode + slash + uid.substring(
            0, 1
        ) + slash + uid.substring(1, 2) + slash + configParams?.asaLicenceKey
    }

    private fun extractErrorInfo(authResp: String): ErrorInfo {
        val kycResp = XstreamCommonMethos.respDecodedXmlToPojo(authResp)
        if (kycResp.rar == null) {
            return ErrorInfo(
                "Failed to fetch data for txnId: ${kycResp.txn}",
                EKYC_ERROR_CODE + kycResp.err,
                RAR_DOESNT_EXIST
            )
        }

        val kycResDecodedStr = decode(kycResp.rar)
        val authRespDecoded = String(kycResDecodedStr)
        val authRespDecodedRar = XstreamCommonMethos.rarDecodedXmlToPojo(authRespDecoded)

        return ErrorInfo(
            "Failed to fetch data for txnId: ${kycResp.txn}",
            EKYC_ERROR_CODE + kycResp.err,
            AUTH_ERROR_CODE + authRespDecodedRar.err
        )
    }
}