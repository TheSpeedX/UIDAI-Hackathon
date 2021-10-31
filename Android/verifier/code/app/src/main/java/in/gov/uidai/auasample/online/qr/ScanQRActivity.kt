package `in`.gov.uidai.auasample.online.qr

import `in`.gov.uidai.auasample.Constants
import `in`.gov.uidai.auasample.R
import `in`.gov.uidai.auasample.ResultActivity
import `in`.gov.uidai.auasample.checkIfIntentResolves
import `in`.gov.uidai.auasample.utils.Utils
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_scan_qr.*

class ScanQRActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, ScanQRActivity::class.java))
        }
    }

    private lateinit var uid: String
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)
        title = getString(R.string.title_scan_qr_code)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        codeScanner = CodeScanner(this, scanner_view)

        codeScanner.camera = CodeScanner.CAMERA_FRONT
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                uid = it.text
                invokeCaptureIntent()
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(
                    this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        scanner_view.setOnClickListener {
            codeScanner.startPreview()
        }

        registerReceiver(captureResponseReceiver, IntentFilter(Constants.CAPTURE_INTENT_RESULT))
    }

    private val captureResponseReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent) {
            handleCaptureResponse(intent.getStringExtra(Constants.CAPTURE_INTENT_RESPONSE_DATA)!!)
        }
    }

    private fun handleCaptureResponse(captureResponse: String) {
        ResultActivity.launch(this, uid, captureResponse)
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onDestroy() {
        unregisterReceiver(captureResponseReceiver)
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun invokeCaptureIntent() {
        val intent = Intent(Constants.CAPTURE_INTENT)
        if (intent.checkIfIntentResolves(packageManager)) {
            intent.putExtra(
                Constants.CAPTURE_INTENT_REQUEST,
                Utils.createPidOptionForAuth(Utils.getTransactionID())
            )
            startActivity(intent)
        } else {
            handleAppDoesNotExist()
        }
    }

    private fun handleAppDoesNotExist() {
        Snackbar.make(
            frame_layout,
            getString(R.string.error_face_rd_not_installed),
            Snackbar.LENGTH_LONG
        ).show()
    }

}