package `in`.gov.uidai.auasample.online.authenticate

import `in`.gov.uidai.auasample.Constants
import `in`.gov.uidai.auasample.R
import `in`.gov.uidai.auasample.checkIfIntentResolves
import `in`.gov.uidai.auasample.input.contract.CaptureResponse
import `in`.gov.uidai.auasample.input.contract.ekyc.OfflineEkyc
import `in`.gov.uidai.auasample.online.register.OnlineEKYCDownloader
import `in`.gov.uidai.auasample.settings.auaConfig.ConfigUtils
import `in`.gov.uidai.auasample.utils.Utils
import `in`.gov.uidai.auasample.utils.totalTime
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_authenticate_face.*

class AuthenticateUsingFaceActivity : AppCompatActivity() {

    private val savedKey: String = "uid"
    private lateinit var loginPreference: SharedPreferences

    companion object {
        private const val CAPTURE_REQ_CODE = 123

        fun launch(context: Context) {
            context.startActivity(Intent(context, AuthenticateUsingFaceActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authenticate_face)

        title = getString(R.string.title_face_authenticate)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        addInputFieldTextWatchers()
        btnRegisterUser.setOnClickListener {
            rememberMeButtonHandler()
//            invokeCaptureIntent()
            if (ConfigUtils.isConfigExist())
                invokeCaptureIntent()
            else {
                tvCaptureFlowResult.visibility = View.VISIBLE
                tvCaptureFlowResult.text =
                    "Please Set Configurations for selected environment in Settings menu."
            }
        }
        btnDone.setOnClickListener { onBackPressed() }

        edtTransactionId.setText(Utils.getTransactionID())

        loginPreference =
            getSharedPreferences(getString(R.string.login_preference), Context.MODE_PRIVATE)
        val savedUid = loginPreference.getString(savedKey, "NA")
        if (savedUid != "NA") {
            edtUid.setText(savedUid)
            remember.isChecked = true
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun rememberMeButtonHandler() {
        if (remember.isChecked) {
            val newUID = edtUid.text.toString()
            if (loginPreference.getString(savedKey, "NA") != newUID)
                loginPreference.edit().putString(savedKey, newUID).apply()
        } else {
            loginPreference.edit().clear().apply()
        }
    }

    private fun invokeCaptureIntent() {
        val intent = Intent(Constants.CAPTURE_INTENT)
        if (intent.checkIfIntentResolves(packageManager)) {
            btnRegisterUser.visibility = View.GONE
            disableAllInputFields()

            intent.putExtra(
                Constants.CAPTURE_INTENT_REQUEST,
                Utils.createPidOptionForAuth(edtTransactionId.text.toString())
            )
            initialiseTotalTimeVariables()
            startActivityForResult(intent, CAPTURE_REQ_CODE)
        } else {
            handleAppDoesNotExist()
        }
    }

    private fun initialiseTotalTimeVariables() {
        totalTime.startTime = System.currentTimeMillis()
        totalTime.stopTime = System.currentTimeMillis()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        totalTime.stopTime = System.currentTimeMillis()
        remember.visibility = View.GONE
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (requestCode == CAPTURE_REQ_CODE && resultCode == Activity.RESULT_OK) {
                handleCaptureResponse(it.getStringExtra(Constants.CAPTURE_INTENT_RESPONSE_DATA)!!)
            }
        }
    }

    private fun handleAppDoesNotExist() {
        Snackbar.make(
            llRootView,
            getString(R.string.error_face_rd_not_installed),
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun handleCaptureResponse(captureResponse: String) {
        val response = CaptureResponse.fromXML(captureResponse)

        setCaptureResponseStatus(Utils.formatCaptureResponse(response), response.isSuccess)

        if (response.isSuccess) {
            downloadEkyc(response)
        }
    }

    private fun downloadEkyc(captureResponse: CaptureResponse) {

        llEkycDownloadStatusRoot.visibility = View.VISIBLE
        try {
            OnlineEKYCDownloader().downloadEKYCDocument(
                edtUid.text.toString(),
                captureResponse.toXML(),
                this,
                { ekycXML ->
                    setEkycDownloadResultStatus(getString(R.string.text_ekyc_auth_success), true)

                    ivUserImg.visibility = View.VISIBLE;
                    tvUserName.visibility = View.VISIBLE;
                    try {
                        val ekyc = OfflineEkyc.fromXML(ekycXML)
                        ivUserImg.setImageBitmap(Utils.convertToBitmap(ekyc.uidData.pht))
                        tvUserName.setText(ekyc.uidData.name)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        ivUserImg.setImageResource(R.drawable.ic_fail)
                    }

                    btnDone.visibility = View.VISIBLE //Last step in the authenticate flow.
                },
                {
                    setEkycDownloadResultStatus(
                        "${it.txnId} : ${it.kycErrorCode} : ${it.authErrorCode}", false
                    )
                })
        } catch (e: java.lang.Exception) {
            llEkycDownloadStatusRoot.visibility = View.INVISIBLE
            setCaptureResponseStatus(
                "Invalid ${ConfigUtils.getSelectedConfigEnv()}, Please check configurations for selected environment.",
                false
            )
        }

    }

    private fun setCaptureResponseStatus(status: String, isSuccess: Boolean) {
        setLogStatus(tvCaptureFlowResult, status, isSuccess)
    }

    private fun setEkycDownloadResultStatus(status: String, isSuccess: Boolean) {
        llEkycDownloadStatusRoot.visibility = View.GONE
        setLogStatus(tvEkycDownloadResult, status, isSuccess)
    }

    private fun setLogStatus(tvLogView: TextView, status: String, isSuccess: Boolean) {
        tvLogView.visibility = View.VISIBLE
        tvLogView.text = status
        tvLogView.isSelected = isSuccess

        if (!isSuccess)
            btnDone.visibility = View.VISIBLE;
    }

    private fun addInputFieldTextWatchers() {
        edtTransactionId.addTextChangedListener(mTextWatcher)
        edtDomainId.addTextChangedListener(mTextWatcher)
        edtName.addTextChangedListener(mTextWatcher)
        edtUid.addTextChangedListener(mTextWatcher)
    }

    private val mTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            btnRegisterUser.isEnabled = shouldEnableCaptureButton()
        }

        override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            //Not used
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            //Not used
        }
    }

    private fun shouldEnableCaptureButton(): Boolean {
        return !(edtTransactionId.text.toString().trim().isEmpty() ||
                edtDomainId.text.toString().trim().isEmpty() ||
                edtUid.text.toString().trim().isEmpty()) && edtUid.text.toString()
            .trim().length == 12 // 12 is a valid UID size
    }
// edtName.text.toString().trim().isEmpty() || was remove since name is not needed. Add this field if in future this field is needed

    private fun disableAllInputFields() {
        edtDomainId.isEnabled = false
        edtName.isEnabled = false
        edtUid.isEnabled = false
        edtTransactionId.isEnabled = false
    }
}
