package `in`.gov.uidai.auasample.input.views

import `in`.gov.uidai.auasample.R
import `in`.gov.uidai.auasample.input.contract.MatchRequest
import `in`.gov.uidai.auasample.utils.Utils
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.dialog_match_request.*

class MatchRequestBuilderDialogFragment : DialogFragment() {

    companion object {
        private val TAG = MatchRequestBuilderDialogFragment::class.simpleName

        const val KEY_TXN_ID = "txn_id"

        fun show(fragmentManager: FragmentManager, txnId: String, callback: ((String) -> Unit)) {
            val args = Bundle()
            args.putString(RegisterRequestBuilderDialogFragment.KEY_TXN_ID, txnId)

            val dialog = MatchRequestBuilderDialogFragment()
            dialog.callback = callback
            dialog.arguments = args
            dialog.show(fragmentManager, TAG)
        }
    }

    private var callback: ((String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_match_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edtTransactionId.setText(Utils.getTransactionID())

        btnFaceMatch.setOnClickListener { constructMatchRequestAndDelegate(); }
    }

    private fun constructMatchRequestAndDelegate() {
        val matchRequest = MatchRequest()
        matchRequest.txnId = edtTransactionId.text.toString()
        matchRequest.domainName = "PDS"
        matchRequest.userId = edtUserId.text.toString()
        matchRequest.last4DigitsOfAadhaar = edtLastFourDigitOfAadhhar.text.toString()

        callback?.invoke(matchRequest.toXML())
        dismiss()
    }
}