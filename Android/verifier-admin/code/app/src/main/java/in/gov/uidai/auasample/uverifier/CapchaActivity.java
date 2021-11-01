package in.gov.uidai.auasample.uverifier;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import in.gov.uidai.auasample.R;

public class CapchaActivity extends AppCompatActivity {

    private static final int EXTERNAL_STORAGE_PERMISSION_CODE = 100;
    private Handler mHandler = new Handler();
    private Handler otpHandler = new Handler();
    private Button btn;
    private String txnId;
    private String captchaValue;
    private boolean permissionToStore;
    private String reason;
    private String uid_str;
    private static final String TAG = "CAPTCHA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capcha);

        if(!(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
        }
//        999963280541

        uid_str = getSharedPreferences("constants", MODE_PRIVATE).getString("uid", "0");
        reason = getIntent().getExtras().getString("reason");
        txnId = "";
        captchaValue = "";
        TextView tvMsg = findViewById(R.id.tv_msg_captcha);
        Log.d(TAG, "onCreate: " + getIntent().getExtras().getString("msg"));
        tvMsg.setText(getIntent().getExtras().getString("msg"));
        NewThread thread = new NewThread();
        thread.start();
        setButtons();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case EXTERNAL_STORAGE_PERMISSION_CODE:
                permissionToStore  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToStore ) finish();

    }

    private void setButtons() {
        btn =  findViewById(R.id.btn_submit_capcha);
        final EditText etUid = findViewById(R.id.et_uid_captcha);
        etUid.setText(uid_str);
        Log.d(TAG, "setButtons: " + reason);
        if(!(reason.contains("ekyc"))){
            etUid.setVisibility(View.GONE);
        }
        if(etUid.getText().toString().length() == 12){
            btn.setEnabled(true);
        }
        else{
            btn.setEnabled(false);
        }
        etUid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                btn.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etUid.getText().toString().length() == 12){
                    btn.setEnabled(true);
                }
                else{
                    btn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onClick: should call otp api now" + txnId );
                Intent intent = new Intent(getApplicationContext(), EnterOtp.class);
                if(txnId != ""){
                    getSharedPreferences("constants", MODE_PRIVATE).edit().putString("uid", etUid.getText().toString()).apply();
                    uid_str = etUid.getText().toString();
                    EditText capchaText = findViewById(R.id.et_capchavalue);
                    captchaValue = capchaText.getText().toString();
                    GenerateOtp generateOtp = new GenerateOtp();
                    generateOtp.start();
                }
            }
        });
    }

    class NewThread extends Thread{
        String url;
        private Bitmap decodedByte;

        NewThread(){
        }

        public void run(){
            URL url = null;
            try {
                url = new URL("https://stage1.uidai.gov.in/unifiedAppAuthService/api/v2/get/captcha");
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);
                String jsonInputString = "{\"langCode\":\"en\",\"captchaLength\": \"3\",\"captchaType\": \"2\"}";
                OutputStream os = con.getOutputStream();
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
                Log.d("TAG", "rupel run: " + con.getInputStream());
                try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println(response.toString());
                    txnId = response.toString().split(",")[2].split(":")[1];
                    String base64Image = response.toString().split(",")[1].split(":")[1];
                    ImageView iv= findViewById(R.id.iv_captcha_oekyc);
                    byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                    decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                }
                catch (Exception e){
                    Log.d("TAG", "run: in exception rupel" );
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageView iv= findViewById(R.id.iv_captcha_oekyc);
                    iv.setImageBitmap(decodedByte);
                }
            });

        }
    }

    class GenerateOtp extends Thread{
        String url;

        GenerateOtp(){
        }

        public void run(){
            URL url = null;
            try {
                url = new URL("https://stage1.uidai.gov.in/unifiedAppAuthService/api/v2/generate/aadhaar/otp");
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("x-request-id", "59142477-3f57-465d-8b9a-75b28fe48725");
                con.setRequestProperty("appid", "MYAADHAAR");
                con.setRequestProperty("Accept-Language", "en_in");
                con.setRequestProperty("Content-Type", "application/json");
//                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);
                String jsonInputString = "{\"uidNumber\":\""+ uid_str+"\"," +
                        "\"captchaTxnId\":" +txnId+ "," +
                        "\"captchaValue\": \"" + captchaValue + "\"," +
                        "\"transactionId\": \"MYAADHAAR:59142477-3f57-465d-8b9a-75b28fe48725\"}";
                OutputStream os = con.getOutputStream();
                Log.d("TAG", "run: " + jsonInputString);
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
//                Log.d("TAG", "rupel run: " + con.getInputStream());
                try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println(response.toString());
                    Looper.prepare();
                    String status = response.toString().split(",")[3];
                    Toast.makeText(getApplicationContext(), "OTP api request" + status , Toast.LENGTH_SHORT).show();
                    if(status.contains("Success")){
                        Intent intent = new Intent(getApplicationContext(), EnterOtp.class);
                        intent.putExtra("otpResponse", response.toString());
                        intent.putExtra("reason" , reason);
                        startActivity(intent);
                        finish();
                    }

                    Log.d("rupe", "run: " + response.toString());
                }
                catch (Exception e){
                    Log.d("TAG", "run: in exception rupel" );
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}