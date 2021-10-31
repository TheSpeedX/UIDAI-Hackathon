package `in`.gov.uidai.auasample.online.register

import `in`.gov.uidai.auasample.R
import `in`.gov.uidai.auasample.checkIfIntentResolves
import `in`.gov.uidai.auasample.input.contract.CaptureResponse
import `in`.gov.uidai.auasample.input.contract.RegisterRequest
import `in`.gov.uidai.auasample.input.contract.RegisterResponse
import `in`.gov.uidai.auasample.utils.Utils
import android.app.Activity
import android.content.*
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_register_using_face.*
import java.text.MessageFormat

class RegisterUsingFaceActivity : AppCompatActivity() {

    companion object {
        const val TAG = "RegUsingFaceActivity"
        const val wadhKey = "sgydIC09zzy6f8Lb3xaAqzKquKe9lFcNR9uTvYxFp+A="

        const val CAPTURE_INTENT = "in.gov.uidai.rdservice.face.CAPTURE"
        const val CAPTURE_INTENT_REQUEST = "request"
        const val CAPTURE_INTENT_RESULT = "in.gov.uidai.rdservice.face.CAPTURE_RESULT"
        const val CAPTURE_INTENT_RESPONSE_DATA = "response"

        val REGISTER_SERVICE_COMPONENT_NAME = ComponentName(
            "in.gov.uidai.faceauth",
            "in.gov.uidai.faceauth.ui.register.RegisterForegroundService"
        )

        const val REGISTER_INTENT_REGISTER_REQUEST = "request"

        const val REGISTER_INTENT_RESULT = "in.gov.uidai.rdservice.face.REGISTER_RESULT"
        const val REGISTER_INTENT_REGISTER_RESPONSE = "response"

        private const val REG_USING_FACE_REQ_CODE = 456

        fun launch(context: Context) {
            context.startActivity(Intent(context, RegisterUsingFaceActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_using_face)

        title = getString(R.string.title_register_using_face)

        registerReceiver(registerResultReceiver, IntentFilter(REGISTER_INTENT_RESULT))

        addInputFieldTextWatchers()

        btnRegisterUser.setOnClickListener { invokeCaptureIntent() }
        btnDone.setOnClickListener { onBackPressed() }

        edtTransactionId.setText(Utils.getTransactionID())
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(registerResultReceiver)
    }

    private fun invokeCaptureIntent() {
        val intent = Intent(CAPTURE_INTENT)
        if (intent.checkIfIntentResolves(packageManager)) {
            btnRegisterUser.visibility = View.GONE
            disableAllInputFields()

            intent.putExtra(
                CAPTURE_INTENT_REQUEST,
                Utils.createPidOptionForRegister(edtTransactionId.text.toString())
            )
            startActivityForResult(intent, REG_USING_FACE_REQ_CODE)
        } else {
            handleAppDoesNotExist()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (requestCode == REG_USING_FACE_REQ_CODE && resultCode == Activity.RESULT_OK)
                handleCaptureResponse(it.getStringExtra(CAPTURE_INTENT_RESPONSE_DATA)!!)
        }
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

        OnlineEKYCDownloader().downloadEKYCDocument(
            edtUid.text.toString(),
            captureResponse.toXML(),
            this,
            { ekycXML ->
                setEkycDownloadResultStatus(getString(R.string.text_ekyc_download_success), true)

                val request = RegisterRequest()
                request.txnId = edtTransactionId.text.toString()
                request.domainName = "PDS"
                request.userId = edtDomainId.text.toString()
                request.userName = edtName.text.toString()
                request.last4DigitsOfAadhaar = edtUid.text.toString().trim().substring(8)
                request.seteKycSignedDoc(ekycXML)

                triggerRegisterIntent(request.toXML())
            },
            {
                setEkycDownloadResultStatus(
                    "${it.txnId} : ${it.kycErrorCode} : ${it.authErrorCode}", false
                )
            })
    }


    private fun triggerRegisterIntent(request: String) {
        val registerIntent = Intent()
        registerIntent.component =
            REGISTER_SERVICE_COMPONENT_NAME

        if (registerIntent.checkIfIntentResolves(packageManager)) {

            registerIntent.putExtra(REGISTER_INTENT_REGISTER_REQUEST, request)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                startForegroundService(registerIntent)
            else
                startService(registerIntent)

        } else {
            handleAppDoesNotExist()
        }

    }

    private val registerResultReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent) {
            val registerResponse = intent.getStringExtra(REGISTER_INTENT_REGISTER_RESPONSE)
            handleRegisterResponse(RegisterResponse.fromXML(registerResponse))
        }
    }

    private fun handleRegisterResponse(response: RegisterResponse) {
        if (response.isSuccess) {
            val message = MessageFormat.format(
                "Registration successful for userId - {0} with transaction - {1}",
                response.registrationId, response.txnId
            )
            setRegisterResponseStatus(message, true)

            btnDone.visibility = View.VISIBLE //Last step in the register flow.
        } else {
            val message = MessageFormat.format(
                "Registration failed for transaction - {0} with error : {1}.",
                response.txnId, response.errInfo
            )
            setRegisterResponseStatus(message, false)
        }
    }

    private fun handleAppDoesNotExist() {
        Snackbar.make(
            llRootView,
            getString(R.string.error_face_rd_not_installed),
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun setCaptureResponseStatus(status: String, isSuccess: Boolean) {
        setLogStatus(tvCaptureFlowResult, status, isSuccess)
    }

    private fun setRegisterResponseStatus(status: String, isSuccess: Boolean) {
        setLogStatus(tvRegisterFlowResult, status, isSuccess)
    }

    private fun setEkycDownloadResultStatus(status: String, isSuccess: Boolean) {
        llEkycDownloadStatusRoot.visibility = View.GONE
        setLogStatus(tvEkycDownloadResult, status, isSuccess)
    }

    private fun setLogStatus(tvLogView: TextView, status: String, isSuccess: Boolean) {
        tvLogView.visibility = View.VISIBLE
        tvLogView.setText(status)
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
        val UID_SIZE = 12
        return !(edtTransactionId.text.toString().trim().isEmpty() ||
                edtDomainId.text.toString().trim().isEmpty() ||
                edtName.text.toString().trim().isEmpty() ||
                edtUid.text.toString().trim().isEmpty()) && edtUid.text.toString()
            .trim().length == UID_SIZE
    }

    private fun disableAllInputFields() {
        edtDomainId.isEnabled = false
        edtName.isEnabled = false
        edtUid.isEnabled = false
        edtTransactionId.isEnabled = false
    }
}
