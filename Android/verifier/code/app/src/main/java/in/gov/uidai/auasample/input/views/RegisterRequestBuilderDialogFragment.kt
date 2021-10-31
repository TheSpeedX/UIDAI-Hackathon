package `in`.gov.uidai.auasample.input.views

import `in`.gov.uidai.auasample.R
import `in`.gov.uidai.auasample.input.contract.RegisterRequest
import `in`.gov.uidai.auasample.input.contract.ekyc.OfflineEkyc
import `in`.gov.uidai.auasample.utils.Utils
import `in`.gov.uidai.auasample.utils.readEKYCData
import android.content.res.AssetManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import kotlinx.android.synthetic.main.dialog_register_request.*
import java.io.File
import java.util.concurrent.Executors


class RegisterRequestBuilderDialogFragment : DialogFragment() {

    companion object {
        private val TAG = RegisterRequestBuilderDialogFragment::class.simpleName

        const val EKYC_DOC_PATH = "ekyc"

        const val KEY_TXN_ID = "txn_id"

        fun show(fragmentManager: FragmentManager, txnId: String, callback: ((String) -> Unit)) {
            val args = Bundle()
            args.putString(KEY_TXN_ID, txnId)

            val dialog = RegisterRequestBuilderDialogFragment()
            dialog.callback = callback
            dialog.arguments = args
            dialog.show(fragmentManager, TAG)
        }
    }

    private var callback: ((String) -> Unit)? = null

    private lateinit var dataAdapter: ArrayAdapter<String>

    private val executors = Executors.newFixedThreadPool(1)

    private val mainHandler = Handler(Looper.getMainLooper())

    private var lastReadEKYCDocument: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_register_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEKYCDropDown()

        edtTransactionId.setText(Utils.getTransactionID())

        btnRegisterUser.setOnClickListener { constructRequestAndDelegate() }
    }

    private fun constructRequestAndDelegate() {
        val request = RegisterRequest()
        request.txnId = edtTransactionId.text.toString()
        request.domainName = "PDS"
        request.userId = edtUserId.text.toString()
        request.userName = userName.text.toString()
        request.last4DigitsOfAadhaar = edtLastFourDigitOfAadhhar.text.toString()
        request.seteKycSignedDoc(lastReadEKYCDocument)

        callback?.invoke(request.toXML())
        dismiss()
    }

    private fun getAssetManager(): AssetManager {
        return context?.assets!!
    }

    private fun setupEKYCDropDown() {

        val docs = getAssetManager().list(EKYC_DOC_PATH)
        docs?.sort()

        dataAdapter = ArrayAdapter(
            context!!, android.R.layout.simple_spinner_item, docs!!
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        ekycDocumentSpinner.adapter = dataAdapter

        ekycDocumentSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View, position: Int, id: Long
            ) {
                onEKYDocumentSelected(dataAdapter.getItem(position)!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun onEKYDocumentSelected(docName: String) {
        //Document may take time to load, during loading don't give user the option to submit the details
        btnRegisterUser.isEnabled = false
        executors.execute {
            lastReadEKYCDocument =
                getAssetManager().readEKYCData(EKYC_DOC_PATH + File.separator + docName)

            lastReadEKYCDocument?.let {
                val ekyc = convertEKYCStringToModel(lastReadEKYCDocument)

                ekyc?.let {
                    mainHandler.post {
                        if (null != context && lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
                            ivUserImage.setImageBitmap(Utils.convertToBitmap(ekyc.uidData.pht))
                            btnRegisterUser.isEnabled = true
                        }
                    }
                }
            }
        }

        userName.setText(docName.replace(".xml", ""))
    }

    private fun convertEKYCStringToModel(ekyc: String?): OfflineEkyc? {
        return try {
            OfflineEkyc.fromXML(ekyc)
        } catch (e: Exception) {
            null
        }
    }
}