package `in`.gov.uidai.auasample.input

import `in`.gov.uidai.auasample.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.fragment_dialog_input.*

class InputDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "InputDialogFragment";
        const val KEY_INPUT_TEXT = "key_input_text";

        fun show(fragmentManager: FragmentManager, text: String, callback: ((String) -> Unit)) {

            val args = Bundle()
            args.putString(KEY_INPUT_TEXT, text)

            val inputDialog = InputDialogFragment()
            inputDialog.arguments = args;
            inputDialog.callback = callback;
            inputDialog.show(fragmentManager, TAG)
        }
    }

    private var callback: ((String) -> Unit)? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestInputEditText.setText(arguments?.getString(KEY_INPUT_TEXT))

        doneButton.setOnClickListener {
            callback?.invoke(requestInputEditText.text.toString())
            dismiss()
        }
    }
}