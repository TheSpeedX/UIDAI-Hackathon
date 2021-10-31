package `in`.gov.uidai.auasample.deviceInfo

import `in`.gov.uidai.auasample.R
import `in`.gov.uidai.auasample.checkIfIntentResolves
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_device_info.*

class DeviceInfoActivity : AppCompatActivity() {
    companion object {
        private const val DEVICE_INFO_INTENT = "in.gov.uidai.rdservice.face.DEVICE_INFO"
        private const val RESPONSE_DATA = "response"
        private const val DEVICE_INFO_INTENT_RESULT =
            "in.gov.uidai.rdservice.face.DEVICE_INFO_RESULT"

        fun launch(context: Context) {
            context.startActivity(Intent(context, DeviceInfoActivity::class.java))
        }
    }

    private val requestCode = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_info)
        title = getString(R.string.title_device_info)
        invokeDeviceInfoIntent()
        btnDone.setOnClickListener { onBackPressed() }
    }

    private fun invokeDeviceInfoIntent() {
        val intent = Intent(DEVICE_INFO_INTENT)
        if (intent.checkIfIntentResolves(packageManager)) {
            startActivityForResult(intent, requestCode)
        } else {
            showToast(R.string.error_face_rd_not_installed)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (requestCode == requestCode && resultCode == Activity.RESULT_OK) {
                handleDeviceInfoResponse(
                    DeviceInfoResponse.fromXML(data.getStringExtra(RESPONSE_DATA))
                )
            }
        }
    }

    private fun handleDeviceInfoResponse(response: DeviceInfoResponse) {
        defaultMsg.visibility = GONE
        if (response.errorCode.isNullOrEmpty()) {
            successDeviceInfoWindow.visibility = VISIBLE
            setSuccessDataToView(response)
        } else {
            failureDeviceInfoWindow.visibility = VISIBLE
            setFailureDataToView(response)
        }
    }

    private fun showToast(resId: Int) {
        defaultMsg.text = getString(resId)
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessDataToView(response: DeviceInfoResponse) {
        dpIdTextView.text = getString(R.string.dpId).plus(response.dpId)
        rdsIdTextView.text = getString(R.string.rdsId).plus(response.rdsId)
        rdsVerTextView.text = getString(R.string.rdsVer).plus(response.rdsVer)
        dcTextView.text = getString(R.string.dc).plus(response.dc)
        miTextView.text = getString(R.string.mi).plus(response.mi)
        mcTextView.text = getString(R.string.mc).plus(response.mc)
        apkVersion.text = getString(R.string.apkVersion).plus(response.apkVersion)
        appId.text = getString(R.string.appId).plus(response.appId)
        environment.text = getString(R.string.environment).plus(response.getenv())
        supportedLanguages.text = getString(R.string.supportedLanguages).plus(response.languages)
    }

    private fun setFailureDataToView(response: DeviceInfoResponse) {
        errorCode.text = getString(R.string.errCode).plus(response.errorCode)
        errorMsg.text = getString(R.string.errorMsg).plus(response.errorMsg)
    }
}