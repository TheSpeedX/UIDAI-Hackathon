package `in`.gov.uidai.auasample.settings.auaConfig

import `in`.gov.uidai.auasample.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_config_settings.*


class ChangeStagingConfig : Fragment() {

    private var fileUri: Uri? = null
    private val PICKFILE_RESULT_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.dialog_config_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = "Staging Configurations"
//        dialog?.setOnShowListener {
//            val currentDialog = it as BottomSheetDialog
//            configRootView?.let { sheet ->
//                currentDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
//                sheet.parent.parent.requestLayout()
//            }
//        }
//        config_heading.text = "Staging Configurations"
        populateConfigFields()

        aua_signing_p12.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) launchDocumentPicker()
        }
        save_Configs.setOnClickListener {
            saveConfigsToLocal()
        }
        reset_Configs.setOnClickListener {
            ConfigUtils.resetConfig(requireContext(), ConfigUtils.STAGING_CONFIG_DATA)
//            dismiss()
        }
    }

    private fun saveConfigsToLocal() {
        if (validateForm()) {
            val p12Path = aua_signing_p12.text.toString()
            val configParams: ConfigParams = ConfigParams(
                auaCode = aua_code.text.toString(),
                auaLicenceKey = aua_license_key.text.toString(),
                asaCode = asa_code.text.toString(),
                asaLicenceKey = asa_license_key.text.toString(),
                signingCert = signing_cert.text.toString(),
                authUrl = auth_url.text.toString(),
                eKycUrl = ekyc_url.text.toString(),
                p12Password = p12_password.text.toString(),
                subAUACode = sub_aua_code.text.toString(),
                signingP12Path = p12Path
            )

            ConfigUtils.saveConfigData(configParams, ConfigUtils.STAGING_CONFIG_DATA)

            if (fileUri != null) {
                val fileContent = requireContext().contentResolver.openInputStream(fileUri!!)
                if (fileContent != null) {
                    ConfigUtils.saveCertificate(
                        requireContext(),
                        ConfigUtils.STAGING_CONFIG_DATA,
                        fileContent
                    )
                }
            }

//            dismiss()
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true
        if (aua_code.text.toString().isEmpty()) {
            isValid = false
            aua_code_layout.error = "Mandatory Field"
        } else {
            aua_code_layout.isErrorEnabled = false
        }
        if (aua_license_key.text.toString().isEmpty()) {
            isValid = false
            aua_license_key_layout.error = "Mandatory Field"
        } else {
            aua_license_key_layout.isErrorEnabled = false
        }
        if (asa_code.text.toString().isEmpty()) {
            isValid = false
            asa_code_layout.error = "Mandatory Field"
        } else {
            asa_code_layout.isErrorEnabled = false
        }
        if (asa_license_key.text.toString().isEmpty()) {
            isValid = false
            asa_license_key_layout.error = "Mandatory Field"
        } else {
            asa_license_key_layout.isErrorEnabled = false
        }
        if (sub_aua_code.text.toString().isEmpty()) {
            isValid = false
            sub_aua_code_layout.error = "Mandatory Field"
        } else {
            sub_aua_code_layout.isErrorEnabled = false
        }
        if (auth_url.text.toString().isEmpty()) {
            isValid = false
            auth_url_layout.error = "Mandatory Field"
        } else {
            auth_url_layout.isErrorEnabled = false
        }
        if (ekyc_url.text.toString().isEmpty()) {
            isValid = false
            ekyc_url_layout.error = "Mandatory Field"
        } else {
            ekyc_url_layout.isErrorEnabled = false
        }
        if (signing_cert.text.toString().isEmpty()) {
            isValid = false
            signing_cert_layout.error = "Mandatory Field"
        } else {
            signing_cert_layout.isErrorEnabled = false
        }
        if (aua_signing_p12.text.toString().isEmpty()) {
            isValid = false
            aua_signing_p12_layout.error = "Mandatory Field"
        } else {
            aua_signing_p12_layout.isErrorEnabled = false
        }
        if (p12_password.text.toString().isEmpty()) {
            isValid = false
            p12_password_layout.error = "Mandatory Field"
        } else {
            p12_password_layout.isErrorEnabled = false
        }

        return isValid
    }

    private fun populateConfigFields() {
        val configParamsData = ConfigUtils.getConfigData(ConfigUtils.STAGING_CONFIG_DATA)
        if (configParamsData != null) {
            aua_code.setText(configParamsData.auaCode)
            aua_license_key.setText(configParamsData.auaLicenceKey)
            asa_code.setText(configParamsData.asaCode)
            asa_license_key.setText(configParamsData.asaLicenceKey)
            ekyc_url.setText(configParamsData.eKycUrl)
            auth_url.setText(configParamsData.authUrl)
            signing_cert.setText(configParamsData.signingCert)
            sub_aua_code.setText(configParamsData.subAUACode)
            p12_password.setText(configParamsData.p12Password)
            aua_signing_p12.setText(configParamsData.signingP12Path)
        }
    }


    private fun launchDocumentPicker() {
        var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
        chooseFile.type = "application/x-pkcs12"
        chooseFile = Intent.createChooser(chooseFile, "Choose a file")
        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICKFILE_RESULT_CODE -> if (resultCode == -1) {
                if (data != null) {
                    fileUri = data.data!!
                    val filePath = fileUri!!.path
                    aua_signing_p12.setText(filePath)
                }
            }
        }
    }
}