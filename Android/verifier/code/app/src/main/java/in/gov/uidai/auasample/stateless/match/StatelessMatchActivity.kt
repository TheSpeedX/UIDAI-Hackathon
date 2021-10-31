package `in`.gov.uidai.auasample.stateless.match

import `in`.gov.uidai.auasample.R
import `in`.gov.uidai.auasample.input.contract.StatelessMatchRequest
import `in`.gov.uidai.auasample.input.contract.StatelessMatchResponse
import `in`.gov.uidai.auasample.input.contract.ekyc.OfflineEkyc
import `in`.gov.uidai.auasample.input.views.RegisterRequestBuilderDialogFragment
import `in`.gov.uidai.auasample.utils.Utils
import `in`.gov.uidai.auasample.utils.readAsText
import `in`.gov.uidai.auasample.utils.readEKYCData
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.Lifecycle
import kotlinx.android.synthetic.main.activity_stateless_match.*
import kotlinx.android.synthetic.main.dialog_register_request.edtTransactionId
import kotlinx.android.synthetic.main.dialog_register_request.ekycDocumentSpinner
import kotlinx.android.synthetic.main.dialog_register_request.ivUserImage
import java.io.File
import java.net.URI
import java.util.concurrent.Executors
import androidx.core.content.FileProvider.getUriForFile




class StatelessMatchActivity : AppCompatActivity() {

    private lateinit var dataAdapter: ArrayAdapter<String>

    private val executors = Executors.newFixedThreadPool(1)

    private val mainHandler = Handler(Looper.getMainLooper())

    private var lastReadEKYCDocument: String? = null

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, StatelessMatchActivity::class.java))
        }

        const val ACTION = "in.gov.uidai.rdservice.face.STATELESS_MATCH"
        const val REQUEST = "request"
        const val RESPONSE = "response"
        const val STATELESS_MATCH_REQ_CODE = 123
        const val DOCUMENT_PICKER_REQ_CODE = 124
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stateless_match)
        tvEkycDocumentSelector.visibility = View.GONE;

        supportActionBar?.title = getString(R.string.title_stateless_match)

        val file = File( applicationContext.externalCacheDir, "ekyc.xml")
        val uri = DocumentFile.fromFile(file)
        val contentUri = getUriForFile(applicationContext, "in.gov.uidai.auasample.fileprovider", file)
        Log.d("stateless match ", "onCreate: uri " + uri)
        onEKYDocumentSelected(contentUri);

//        setupEKYCDropDown()

        edtTransactionId.setText(Utils.getTransactionID())

        btnPerformStatelessMatch.setOnClickListener { launchStatelessMatch() }

        btnDone.setOnClickListener { onBackPressed() }

        tvEkycDocumentSelector.setOnClickListener { launchDocumentPicker() }
    }

    private fun launchStatelessMatch() {
        val statelessMatchRequest = StatelessMatchRequest()
        statelessMatchRequest.requestId = edtTransactionId.text.toString()
        statelessMatchRequest.signedDocument = lastReadEKYCDocument
        statelessMatchRequest.language = Utils.LANGUAGE
        statelessMatchRequest.enableAutoCapture = Utils.ENABLE_AUTO_CAPTURE.toString()
        val intent = Intent(ACTION).apply {
            putExtra(REQUEST, statelessMatchRequest.toXml())
        }

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, STATELESS_MATCH_REQ_CODE)
        } else {
            showToast(R.string.error_face_rd_plus_not_installed)
        }
    }

    private fun launchDocumentPicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }

        if (intent.resolveActivity(packageManager) != null)
            startActivityForResult(intent, DOCUMENT_PICKER_REQ_CODE)
        else
            showToast(R.string.error_file_mgr_not_installed)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && null != data) {
            if (requestCode == STATELESS_MATCH_REQ_CODE) {
                handleMatchResponse(StatelessMatchResponse.fromXML(data.getStringExtra(RESPONSE)))
            } else if (requestCode == DOCUMENT_PICKER_REQ_CODE) {
                data.data?.let { uri ->
                    Log.d("stateless", "onActivityResult: uri that works: " + uri)
                    onEKYDocumentSelected(uri)
                }
            }
        }
    }

    private fun getAssetManager(): AssetManager {
        return assets!!
    }

    private fun setupEKYCDropDown() {
        val docs = getAssetManager().list(RegisterRequestBuilderDialogFragment.EKYC_DOC_PATH)
        docs?.sort()

        dataAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, docs!!
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        ekycDocumentSpinner.adapter = dataAdapter

        ekycDocumentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View, position: Int, id: Long
            ) {
                onEKYDocumentSelected(dataAdapter.getItem(position)!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun onEKYDocumentSelected(docUri: Uri) {
        executors.execute {
            lastReadEKYCDocument = docUri.readAsText(contentResolver)
            Log.d("TAG", "onEKYDocumentSelected: " + lastReadEKYCDocument)

            validateEkycDocumentAndUpdateUserImage()

            mainHandler.post {
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
                    if (lastReadEKYCDocument == null) {
                        tvEkycDocumentName.text = null
                        showToast(R.string.error_invalid_ekyc_file)
                    } else
                        tvEkycDocumentName.text = Utils.queryName(contentResolver, docUri)
                }
            }
        }
    }

    private fun onEKYDocumentSelected(docName: String) {
        //Document may take time to load, during loading don't give user the option to submit the details
        btnPerformStatelessMatch.isEnabled = false
        executors.execute {
            lastReadEKYCDocument =
                getAssetManager().readEKYCData(RegisterRequestBuilderDialogFragment.EKYC_DOC_PATH + File.separator + docName)

            validateEkycDocumentAndUpdateUserImage()
        }
    }

    @WorkerThread
    private fun validateEkycDocumentAndUpdateUserImage() {
        lastReadEKYCDocument?.let {
            val ekyc = convertEKYCStringToModel(lastReadEKYCDocument)

            ekyc?.let {
                mainHandler.post {
                    if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
                        ivUserImage.setImageBitmap(Utils.convertToBitmap(ekyc.uidData.pht))
                        btnPerformStatelessMatch.isEnabled = true
                    }
                }
            }
        } ?: kotlin.run {
            mainHandler.post {
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
                    ivUserImage.setImageBitmap(null)
                    btnPerformStatelessMatch.isEnabled = false
                }
            }
        }
    }

    private fun convertEKYCStringToModel(ekyc: String?): OfflineEkyc? {
        return try {
            OfflineEkyc.fromXML(ekyc)
        } catch (e: Exception) {
            null
        }
    }

    private fun handleMatchResponse(response: StatelessMatchResponse) {
        if (response.isSuccess) {
            setSuccessResponse("Match successful for transaction - ${response.requestId}")
        } else {
            setErrorResponse("Match failed for transaction - ${response.requestId} with error : ${response.errInfo}.")
        }
    }

    //Use this method to show success response
    private fun setSuccessResponse(message: String) {
        inputContainer.visibility = View.GONE
        statelessMatchResponse.visibility = View.VISIBLE

        imgResponseStatus.setImageResource(R.drawable.ic_success)
        responseText.text = message
    }

    //Use this method to show error response
    private fun setErrorResponse(message: String) {
        inputContainer.visibility = View.GONE
        statelessMatchResponse.visibility = View.VISIBLE

        imgResponseStatus.setImageResource(R.drawable.ic_fail)
        responseText.text = message
    }

    private fun showToast(resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }
}