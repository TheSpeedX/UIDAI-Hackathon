package `in`.gov.uidai.auasample.checkLighting

import `in`.gov.uidai.auasample.R
import `in`.gov.uidai.auasample.checkIfIntentResolves
import `in`.gov.uidai.auasample.utils.Utils
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_check_lighting.*

class CheckLightingActivity : AppCompatActivity() {
    companion object {
        private const val CHECK_LIGHTING_REQ_CODE = 123

        fun launch(context: Context) {
            context.startActivity(Intent(context, CheckLightingActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_lighting)
        invokeCheckLightingIntent()
        title = getString(R.string.title_check_lighting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btnDone.setOnClickListener { onBackPressed() }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun invokeCheckLightingIntent() {
        val checkLightingRequest = CheckLightingRequest()
        checkLightingRequest.txnId = System.currentTimeMillis().toString()
        checkLightingRequest.language = Utils.LANGUAGE
        val intent = Intent(CheckLighting.INTENT).apply {
            putExtra(CheckLighting.INTENT_REQUEST, checkLightingRequest.toXml())
        }
        if (intent.checkIfIntentResolves(packageManager)) {
            startActivityForResult(intent, CHECK_LIGHTING_REQ_CODE)
        } else {
            showToast(R.string.error_face_rd_not_installed)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (requestCode == CHECK_LIGHTING_REQ_CODE && resultCode == Activity.RESULT_OK) {
                handleCheckLightingResponse(
                    CheckLightingResponse.fromXML(
                        data.getStringExtra(
                            CheckLighting.INTENT_RESPONSE
                        )
                    )
                )
            }
        }
    }

    private fun handleCheckLightingResponse(response: CheckLightingResponse) {
        if (response.isSuccess) {
            responseText.text = getString(R.string.good_lighting_condition_msg)
        } else {
            responseText.text = response.getErrInfo()
        }
    }

    private fun showToast(resId: Int) {
        responseText.text = getString(R.string.install_check_lighting_intent_app)
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }
}

interface CheckLighting {
    companion object {
        const val INTENT = "in.gov.uidai.rdservice.face.CHECK_LIGHTING"
        const val INTENT_REQUEST = "request"
        const val INTENT_RESULT = "in.gov.uidai.rdservice.face.CHECK_LIGHTING_RESULT"
        const val INTENT_RESPONSE = "response"
    }
}