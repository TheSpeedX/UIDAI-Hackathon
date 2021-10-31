package `in`.gov.uidai.auasample.offline

import `in`.gov.uidai.auasample.R
import `in`.gov.uidai.auasample.checkIfIntentResolves
import `in`.gov.uidai.auasample.input.contract.MatchResponse
import `in`.gov.uidai.auasample.input.contract.RegisterResponse
import `in`.gov.uidai.auasample.input.views.FetchEkycDialogFragment
import `in`.gov.uidai.auasample.input.views.MatchRequestBuilderDialogFragment
import `in`.gov.uidai.auasample.input.views.RegisterRequestBuilderDialogFragment
import `in`.gov.uidai.auasample.online.register.RegisterUsingFaceActivity
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_offline_flow.*
import java.text.MessageFormat


class OfflineFlowActivity : AppCompatActivity() {

    private lateinit var registerResponse: String
    private lateinit var captureResponse: String
    private var captureResponseSuccess = false

    companion object {

        //Match constants
        const val MATCH_INTENT = "in.gov.uidai.rdservice.face.MATCH"
        const val MATCH_INTENT_REQUEST = "request"
        const val MATCH_INTENT_RESULT = "in.gov.uidai.rdservice.face.MATCH_RESULT"
        const val MATCH_INTENT_RESPONSE_DATA = "response"
        private const val MATCH_REQ_CODE = 123

        //Register constants
        val REGISTER_SERVICE_COMPONENT_NAME = ComponentName(
            "in.gov.uidai.facerdplus",
            "in.gov.uidai.faceauth.ui.register.RegisterForegroundService"
        )

        const val REGISTER_INTENT = "in.gov.uidai.rdservice.face.REGISTER"
        const val REGISTER_INTENT_REGISTER_REQUEST = "request"

        const val REGISTER_INTENT_RESULT = "in.gov.uidai.rdservice.face.REGISTER_RESULT"
        const val REGISTER_INTENT_REGISTER_RESPONSE = "response"

        fun launch(context: Context) {
            context.startActivity(Intent(context, OfflineFlowActivity::class.java))
        }

    }

    enum class Mode {
        OnlineRegister,
        OfflineRegister,
        None
    }

    private var mode: Mode =
        Mode.None

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offline_flow)
        title = getString(R.string.title_offline_authentication)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (resultCode == Activity.RESULT_OK && requestCode == MATCH_REQ_CODE) {
                handleMatchResponse(MatchResponse.fromXML(it.getStringExtra(MATCH_INTENT_RESPONSE_DATA)!!));
            }
        }
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.register_using_face_button -> {
                mode =
                    Mode.OnlineRegister
                RegisterUsingFaceActivity.launch(this)
            }
            R.id.register_using_kyc_button -> onRegisterUsingEKYC()
            //R.id.register_using_kyc_button -> showAlertDialog()
            R.id.authenticate_button -> onFaceMatch()
            R.id.btn_ok -> {
                if (captureResponseSuccess) {
                    openEkycDialogFragment()
                    hideResponseAndShowButtonLayout()
                } else
                    hideResponseAndShowButtonLayout()
            }
        }
    }

    private fun openEkycDialogFragment() {
        FetchEkycDialogFragment.show(
            supportFragmentManager,
            captureResponse,
            this@OfflineFlowActivity::triggerRegisterIntent
        )
    }

    private fun onRegisterUsingEKYC() {
        mode =
            Mode.OfflineRegister
        RegisterRequestBuilderDialogFragment.show(
            supportFragmentManager,
            txnId.toString(),
            this@OfflineFlowActivity::triggerRegisterIntent
        )
    }

    private fun onFaceMatch() {
        mode = Mode.None
        MatchRequestBuilderDialogFragment.show(
            supportFragmentManager,
            txnId.toString(),
            this@OfflineFlowActivity::launchFaceMatchIntent
        )
    }

    private fun launchFaceMatchIntent(request: String) {
        val intent = createIntent(MATCH_INTENT)
        intent?.let {
            it.putExtra(MATCH_INTENT_REQUEST, request)
            startActivityForResult(intent, MATCH_REQ_CODE)
        } ?: run {
            handleAppDoesNotExist()
        }
    }

    private fun triggerRegisterIntent(request: String) {
        val registerIntent = Intent()
        with(registerIntent) {
            component = REGISTER_SERVICE_COMPONENT_NAME
            putExtra(REGISTER_INTENT_REGISTER_REQUEST, request)

            val queryIntentServices =
                packageManager.queryIntentServices(this, PackageManager.GET_META_DATA)

            if (queryIntentServices.size > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    startForegroundService(this)
                else
                    startService(this)

                registerRegisterResultReceiver()
            } else {
                handleAppDoesNotExist()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            //Some of the broadcast receiver may not be registered, so unregister call for those will throw an exception
            unregisterReceiver(registerResultReceiver)
        } catch (ignored: Exception) {
        }
    }

    private fun registerRegisterResultReceiver() {
        registerReceiver(registerResultReceiver, IntentFilter(REGISTER_INTENT_RESULT))
    }

    private var txnId: Long = 0
    private val registerResultReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent) {
            registerResponse = intent.getStringExtra(REGISTER_INTENT_REGISTER_RESPONSE)
            handleRegisterResponse(RegisterResponse.fromXML(registerResponse));
        }
    }

    private fun handleAppDoesNotExist() {
        Snackbar.make(
            parent_view,
            "FaceRD App is not installed on device",
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun createIntent(action: String): Intent? {
        val intent = Intent(action)
        return if (intent.checkIfIntentResolves(packageManager)) intent else null
    }

    private fun handleRegisterResponse(response: RegisterResponse) {
        if (response.isSuccess) {
            val messageStr = if (mode == Mode.OfflineRegister) {
                "Registration done for userId - {0} with transaction - {1} and please proceed for face match" //REG_MATCH_PENDING
            } else {
                "Registration successful for userId - {0} with transaction - {1}"
            }
            //Construct success message here
            val message = MessageFormat.format(
                messageStr,
                response.registrationId,
                response.txnId
            )
            setSuccessResponse(message)
        } else {
            val message = MessageFormat.format(
                "Registration failed for transaction - {0} with error : {1}.",
                response.txnId,
                response.errInfo
            )
            setErrorResponse(message)
        }
        mode = Mode.None
    }

    private fun handleMatchResponse(response: MatchResponse) {
        if (response.isSuccess) {
            //Construct success message here
            val message = if (response.matchedUserIdHash.isNullOrEmpty()) {
                MessageFormat.format(
                    "Match successful for transaction - {0}", response.txnId
                )
            } else {
                MessageFormat.format(
                    "Match successful for transaction - {0} and the userId : {1}", response.txnId,
                    response.matchedUserIdHash
                )
            }
            setSuccessResponse(message)
        } else {
            val message = MessageFormat.format(
                "Match failed for transaction - {0} with error : {1}.",
                response.txnId,
                response.errInfo
            )
            setErrorResponse(message)
        }
    }

    //Use this method to show success response
    private fun setSuccessResponse(message: String) {
        ll_button.visibility = View.GONE
        ll_response.visibility = View.VISIBLE

        iv_response.setImageResource(R.drawable.ic_success)
        tv_response.text = message
    }

    //Use this method to show error response
    private fun setErrorResponse(message: String) {
        ll_button.visibility = View.GONE
        ll_response.visibility = View.VISIBLE

        iv_response.setImageResource(R.drawable.ic_fail)
        tv_response.text = message
    }

    //Use this method to hide response layout and shows button layout
    private fun hideResponseAndShowButtonLayout() {
        ll_button.visibility = View.VISIBLE
        ll_response.visibility = View.GONE
    }

}
