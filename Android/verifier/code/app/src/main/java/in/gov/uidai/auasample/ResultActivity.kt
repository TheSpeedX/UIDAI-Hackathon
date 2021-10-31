package `in`.gov.uidai.auasample

import `in`.gov.uidai.auasample.input.contract.CaptureResponse
import `in`.gov.uidai.auasample.input.contract.ekyc.OfflineEkyc
import `in`.gov.uidai.auasample.online.register.OnlineEKYCDownloader
import `in`.gov.uidai.auasample.utils.Utils
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_result.*


class ResultActivity : AppCompatActivity() {

    companion object {
        private const val UID = "uid"
        private const val PID_DATA = "pidData"
        private const val TIME_TO_WAIT_ON = 5000L

        fun launch(context: Context, uid: String, pidData: String) {
            context.startActivity(Intent(context, ResultActivity::class.java).apply {
                putExtra(UID, uid)
                putExtra(PID_DATA, pidData)
            })
        }
    }

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        handleCaptureResponse(intent?.getStringExtra(PID_DATA)!!)
        btnDone.setOnClickListener { finish() }
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
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
            intent?.getStringExtra(UID)!!,
            captureResponse.toXML(),
            this,
            { ekycXML ->
                setEkycDownloadResultStatus(getString(R.string.text_ekyc_auth_success), true)
                ivUserImg.visibility = View.VISIBLE;
                tvUserName.visibility = View.VISIBLE;
                try {
                    val ekyc = OfflineEkyc.fromXML(ekycXML)
                    ivUserImg.setImageBitmap(Utils.convertToBitmap(ekyc.uidData.pht))
                    tvUserName.text = ekyc.uidData.name
                } catch (e: Exception) {
                    e.printStackTrace()
                    ivUserImg.setImageResource(R.drawable.ic_fail)
                }
                btnDone.visibility = View.VISIBLE //Last step in the authenticate flow.
                handler.postDelayed({ finish() }, TIME_TO_WAIT_ON)
            },
            {
                setEkycDownloadResultStatus(
                    "${it.txnId} : ${it.kycErrorCode} : ${it.authErrorCode}", false
                )
                handler.postDelayed({ finish() }, TIME_TO_WAIT_ON)
            })
    }

    private fun setCaptureResponseStatus(status: String, isSuccess: Boolean) {
        setLogStatus(tvCaptureFlowResult, status, isSuccess)
    }

    private fun setEkycDownloadResultStatus(status: String, isSuccess: Boolean) {
        llEkycDownloadStatusRoot.visibility = View.GONE
        setLogStatus(tvEkycDownloadResult, status, isSuccess)
    }

    private fun setLogStatus(tvLogView: TextView, status: String, isSuccess: Boolean) {
        tvLogView.visibility = View.VISIBLE
        tvLogView.text = status
        tvLogView.isSelected = isSuccess
        if (!isSuccess)
            btnDone.visibility = View.VISIBLE;
    }
}