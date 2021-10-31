package `in`.gov.uidai.auasample.input.views

import `in`.gov.uidai.auasample.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.fragment_dialog_ekyc.*

class FetchEkycDialogFragment : DialogFragment() {

    companion object {
        private val TAG = FetchEkycDialogFragment::class.simpleName
        private val KEY_CAPTURE_RESPONSE = "capture_response"

        fun show(
            fragmentManager: FragmentManager,
            captureResponse: String,
            callback: ((String) -> Unit)
        ) {
            val args = Bundle()
            args.putString(KEY_CAPTURE_RESPONSE, captureResponse)
            val dialog = FetchEkycDialogFragment()
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
        return inflater.inflate(R.layout.fragment_dialog_ekyc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_download.setOnClickListener {
            if (edt_uid.length() == 12) {
                showProgressBar()
            } else
                Toast.makeText(context, getString(R.string.error_uid), Toast.LENGTH_SHORT).show()
        }
    }

    private fun hideProgressBar() {
        fl_progress_bar.visibility = View.GONE
    }

    private fun showProgressBar() {
        fl_progress_bar.visibility = View.VISIBLE
    }

}