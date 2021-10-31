package in.gov.uidai.auasample.uverifier;
//617e5d43f28d3c0016173ef8
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import in.gov.uidai.auasample.R;
import in.gov.uidai.auasample.input.views.RegisterRequestBuilderDialogFragment;
import in.gov.uidai.auasample.stateless.match.StatelessMatchActivity;

public class AdminMenu extends AppCompatActivity {
    private static final String TAG = "Admin Menu";
    private IntentIntegrator qrScan;
    private String bookingId;
    Button btnVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);
        qrScan = new IntentIntegrator(this);
        btnVerify = findViewById(R.id.btn_verify);
        bookingId = "";
        setButtons();
    }

    private void setButtons() {
        findViewById(R.id.btn_scan_qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.initiateScan();
            }
        });

        btnVerify = findViewById(R.id.btn_verify);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Processing your request", Toast.LENGTH_SHORT).show();
                btnVerify.setEnabled(false);
                EditText etBookingId = findViewById(R.id.et_bookingid);
                if(bookingId.equals("")){
                    if(etBookingId.getText().toString().length() < 5){
                        Toast.makeText(getApplicationContext(), "not valid booking id", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        bookingId = etBookingId.getText().toString();
                        getData();
                    }
                }
                else{
                    getData();
                }
            }
        });
    }

    private void getData() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://poc-admin-app.herokuapp.com/api/bookings/" + bookingId;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String ekycXml = obj.getJSONObject("bookedBy").getString("eKYCXML");
                            String filename = "ekyc.xml";
                            File file = new File(getApplicationContext().getFilesDir(), filename);
                            try (FileOutputStream fos = new FileOutputStream(file)) {
                                fos.write(ekycXml.getBytes());
                                fos.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(getApplicationContext() , StatelessMatchActivity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
                btnVerify.setEnabled(true);
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                bookingId =  result.getContents();
                EditText etBookingId = findViewById(R.id.et_bookingid);
                etBookingId.setText(bookingId);
                Toast.makeText(this, bookingId, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}