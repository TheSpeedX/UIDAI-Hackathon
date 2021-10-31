package `in`.gov.uidai.auasample.deviceCheck

import `in`.gov.uidai.auasample.Constants
import `in`.gov.uidai.auasample.R
import `in`.gov.uidai.auasample.checkIfIntentResolves
import `in`.gov.uidai.auasample.utils.Utils
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_device_check.*

class DeviceCheckActivity : AppCompatActivity() {
    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, DeviceCheckActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_check)
        invokeDeviceCheckIntent()
        title = getString(R.string.title_device_check)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btnDone.setOnClickListener { onBackPressed() }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }


    private fun invokeDeviceCheckIntent() {
        val checkDeviceRequest = CheckDeviceRequest()
        checkDeviceRequest.txnId = System.currentTimeMillis().toString()
        checkDeviceRequest.env = Utils.ENVIRONMENT_TAG
        checkDeviceRequest.language = Utils.LANGUAGE
        val intent = Intent(Constants.DEVICE_CHECK_INTENT)
        if (intent.checkIfIntentResolves(packageManager)) {
            intent.putExtra(
                Constants.CAPTURE_INTENT_REQUEST,
                checkDeviceRequest.toXml()
            )
            startActivityForResult(intent, 123)
        } else {
            showToast(R.string.error_face_rd_not_installed)
        }
    }

    private fun showToast(resId: Int) {
        outputText.text = getString(R.string.install_check_lighting_intent_app)
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
                handleCheckDeviceResponse(data.getStringExtra(Constants.CAPTURE_INTENT_RESPONSE_DATA))
            }
        }
    }

    private fun handleCheckDeviceResponse(deviceResponse: String) {
        val response = CheckDeviceResponse.fromXML(deviceResponse)
        if (response.isSuccess) {
            outputText.text =
                "Estimated time to do face authentication on this device is : ${response.getTotalTime()} seconds."

        } else {
            outputText.text = "Error : ${response.getErrInfo()}"
        }
    }
}
