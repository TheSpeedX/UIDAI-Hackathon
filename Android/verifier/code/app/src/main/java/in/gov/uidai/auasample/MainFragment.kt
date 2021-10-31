package `in`.gov.uidai.auasample

import `in`.gov.uidai.auasample.checkLighting.CheckLightingActivity
import `in`.gov.uidai.auasample.deviceCheck.DeviceCheckActivity
import `in`.gov.uidai.auasample.deviceInfo.DeviceInfoActivity
import `in`.gov.uidai.auasample.offline.OfflineFlowActivity
import `in`.gov.uidai.auasample.online.authenticate.AuthenticateUsingFaceActivity
import `in`.gov.uidai.auasample.online.qr.ScanQRActivity
import `in`.gov.uidai.auasample.settings.ChangeFaceRDLanguage
import `in`.gov.uidai.auasample.settings.auaConfig.ChangePreProdConfig
import `in`.gov.uidai.auasample.settings.auaConfig.ChangeProdConfig
import `in`.gov.uidai.auasample.settings.auaConfig.ChangeStagingConfig
import `in`.gov.uidai.auasample.settings.auaConfig.ConfigUtils
import `in`.gov.uidai.auasample.stateless.match.StatelessMatchActivity
import `in`.gov.uidai.auasample.utils.Utils
import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {

    private lateinit var loginPreferences: SharedPreferences

    companion object {
        const val CAMERA_REQUEST_CODE = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "AUA App"
        appDisclaimer.text =
            "This app is a sample app v${BuildConfig.VERSION_NAME} created for the demonstration of local face authentication."
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED
        ) {
            requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        }

        loginPreferences =
            requireContext().getSharedPreferences(
                getString(R.string.login_preference),
                Context.MODE_PRIVATE
            )
        val savedLanguage = loginPreferences.getString(getString(R.string.facerd_lang), "en")

        if (savedLanguage != null) {
            Utils.LANGUAGE = savedLanguage
        }

        ConfigUtils.initialise(requireContext())
        getSelectedEnv()
        environmentSelectionHandler()

        btnOnlineAuthenticationUsingQr.setOnClickListener { ScanQRActivity.launch(requireContext()) }
        btnOnlineAuthentication.setOnClickListener {
            AuthenticateUsingFaceActivity.launch(
                requireContext()
            )
        }
        btnOfflineAuthentication.setOnClickListener { OfflineFlowActivity.launch(requireContext()) }
        btnPerformStatelessMatch.setOnClickListener { StatelessMatchActivity.launch(requireContext()) }
        btnPerformCheckLighting.setOnClickListener { CheckLightingActivity.launch(requireContext()) }
        btnPerformDeviceCheck.setOnClickListener { DeviceCheckActivity.launch(requireContext()) }
        btnDeviceInfo.setOnClickListener { DeviceInfoActivity.launch(requireContext()) }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Camera Permission granted", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.option_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val checkable = menu.findItem(R.id.auto_capture)
        checkable.isChecked = Utils.ENABLE_AUTO_CAPTURE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.auto_capture -> {
                val isChecked = !item.isChecked
                item.isChecked = isChecked
                Utils.ENABLE_AUTO_CAPTURE = isChecked
                true
            }
            R.id.set_language -> {
                goToLanguageChangeFragment()
                true
            }
            R.id.staging_config -> {
                goToChangeStagingConfigFragment()
                true
            }
            R.id.preprod_config -> {
                goToChangePreProdConfigFragment()
                true
            }
            R.id.prod_config -> {
                goToChangeProdConfigFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun goToLanguageChangeFragment() {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.fl_container,
            ChangeFaceRDLanguage()
        )
        transaction.addToBackStack(null)
        transaction.commit()

    }

    private fun goToChangeStagingConfigFragment() {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_container, ChangeStagingConfig())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun goToChangePreProdConfigFragment() {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_container, ChangePreProdConfig())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun goToChangeProdConfigFragment() {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_container, ChangeProdConfig())
        transaction.addToBackStack(null)
        transaction.commit()

    }

    private fun getSelectedEnv() {
        when (loginPreferences.getString(getString(R.string.app_env), "S")) {
            "S" -> {
                stagingRadioButton.isChecked = true
                preProdRadioButton.isChecked = false
                prodRadioButton.isChecked = false
                Utils.ENVIRONMENT_TAG = "S"
            }
            "PP" -> {
                stagingRadioButton.isChecked = false
                preProdRadioButton.isChecked = true
                prodRadioButton.isChecked = false
                Utils.ENVIRONMENT_TAG = "PP"
            }
            "P" -> {
                stagingRadioButton.isChecked = false
                preProdRadioButton.isChecked = false
                prodRadioButton.isChecked = true
                Utils.ENVIRONMENT_TAG = "P"
            }
        }
    }

    private fun environmentSelectionHandler() {
        radio_grp.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.stagingRadioButton -> {
                    Utils.ENVIRONMENT_TAG = "S"
                    loginPreferences.edit().putString(getString(R.string.app_env), "S").apply()
                }
                R.id.preProdRadioButton -> {
                    Utils.ENVIRONMENT_TAG = "PP"
                    loginPreferences.edit().putString(getString(R.string.app_env), "PP").apply()
                }
                R.id.prodRadioButton -> {
                    Utils.ENVIRONMENT_TAG = "P"
                    loginPreferences.edit().putString(getString(R.string.app_env), "P").apply()
                }
            }
        }
    }
}

