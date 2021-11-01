package com.example.uverifier;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import net.lingala.zip4j.ZipFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class EnterOtp extends AppCompatActivity {

    private final static String TAG = "EnterOtp";
    private String uId = "";
    private String txnId = "";
    private String otp;
    private String DIRECTORY_PATH = "./";
    private String url_str;
    private String reason;
    private String mobile_str;
    private String res;
    private String vid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        Bundle extras = getIntent().getExtras();
        String otpRes = extras.getString("otpResponse");
        reason = extras.getString("reason");
        mobile_str = getSharedPreferences("constants", MODE_PRIVATE).getString("mobile", "");
        EditText etMobile = findViewById(R.id.et_mobile_field);
        etMobile.setVisibility(GONE);
        if(reason.contains("vid gen")){
            url_str = "https://stage1.uidai.gov.in/vidwrapper/generate";
        }
        else if(reason.contains("ekyc")){
            url_str = "https://stage1.uidai.gov.in/eAadhaarService/api/downloadOfflineEkyc";
        }
        else if(reason.contains("data")){
            url_str = "https://stage1.uidai.gov.in/onlineekyc/getEkyc";
        }
        Log.d(TAG, "OtpRes : " + otpRes);
        String [] otpFields = otpRes.split(",");
        otp = "";
        uId = otpFields[0].split(":")[1];
        txnId = otpFields[2].split(":")[1]+":"+ otpFields[2].split(":")[2];
        
        setButton();
    }

    private void setButton() {
        final EditText etMobile = findViewById(R.id.et_mobile_field);
        etMobile.setText(mobile_str);
        if(reason.contains("vid")){
            etMobile.setVisibility(View.VISIBLE);
        }
        Button btn = (Button) findViewById(R.id.btn_submit_otp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                assign otp string
                mobile_str = etMobile.getText().toString();
                getSharedPreferences("constants", MODE_PRIVATE).edit().putString("mobile",mobile_str).apply();
                EditText etOtp = (EditText) findViewById(R.id.et_otp_field);
                otp = etOtp.getText().toString();
                GetEkyc getEkyc = new GetEkyc();
                getEkyc.start();
            }
        });
    }

//    thread to get ekycXML
        class GetEkyc extends Thread{

            GetEkyc(){
            }

            public void run(){
                URL url = null;
                try {
                    url = new URL(url_str);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
        //                con.setRequestProperty("Accept", "application/json");
                    con.setDoOutput(true);
                    String jsonInputString = "";
                    if(reason.contains("vid gen")){
                        jsonInputString = "{" +
                                "\"uid\": \"" +uId+ "\"," +
                                "\"mobile\": \"" + mobile_str+ "\"," +
                                "\"otp\": \"" +otp+ "\"," +
                                "\"otpTxnId\": "+  txnId +
                                "}";
                    }
                    else if(reason.contains("ekyc")){
                        jsonInputString = "{\"txnNumber\": "+  txnId +  "," +
                                "\"otp\": \"" +otp+ "\"," +
                                "\"shareCode\": \"" + "4567" + "\"," +
                                "\"uid\": \"" +uId+ "\"}";
                    }
                    else if(reason.contains("data")){
                        jsonInputString = "{" +
                                "\"uid\": \"" +uId+ "\"," +
                                "\"vid\": \"" + "9040370706" + "\"," +
                                "\"txnId\": "+  txnId + "," +
                                "\"otp\": \"" +otp+"\""+
                                "}";
                    }
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
                        if(reason.contains("vid gen")){
                            vidResponse(response.toString());
                        }
                        else if(reason.contains("ekyc")){
                            ekycResponse(response.toString());
                        }
                        else if(reason.contains("data")){
                            getDataResponse(response.toString());
                        }
    //                    till here

    //                    unzipFile(fileName);
                        Log.d(TAG, "run: " + reason);
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

    private void getDataResponse(String response) {
        Log.d(TAG, "getDataResponse: here I am" );
        String keyFirst = "FIRST";
        getSharedPreferences("SIGN" , MODE_PRIVATE).edit().putBoolean(keyFirst,false).apply();
//        Log.d(TAG, "getDataResponse: " + b);
        Intent intent = new Intent(getApplicationContext(),Menu.class);
        startActivity(intent);
    }

        private void ekycResponse(String response) throws IOException {
            String key = "EKYC";

            String zipBase64 = response.toString().split(",")[0].split(":")[1];
            String fileName = response.toString().split(",")[1].split(":")[1];
            fileName = fileName.substring(1,fileName.length()-1);
            getSharedPreferences("const", MODE_PRIVATE).edit().putString("xmlfile", fileName.split("\\.")[0]+".xml").apply();
            File file = new File(getApplicationContext().getExternalCacheDir(), fileName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] decoder = Base64.decode(zipBase64 , Base64.DEFAULT);
                fos.write(decoder);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            new ZipFile(getApplicationContext().getExternalCacheDir().getPath() +"/"+ fileName, "4567".toCharArray()).extractAll(getApplicationContext().getExternalCacheDir().getPath() );
            getEkycData();
            if (getSharedPreferences("SIGN", MODE_PRIVATE).getBoolean(key, true)) {
                getSharedPreferences("SIGN", MODE_PRIVATE).edit().putBoolean(key, false).apply();
            }
            Intent intent = new Intent(getApplicationContext() , Menu.class);
            startActivity(intent);
            finish();
        }

        private void vidResponse(String response) {
            String key = "VID";
            if (getSharedPreferences("SIGN", MODE_PRIVATE).getBoolean(key, true)) {
                getSharedPreferences("SIGN", MODE_PRIVATE).edit().putBoolean(key, false).apply();
            }
                Intent intent = new Intent(getApplicationContext(), VidResponse.class);
                intent.putExtra("response" , response);
                startActivity(intent);
                finish();
        }

    }


    class UpdateData extends Thread{
        UpdateData(){
        }

        public void run(){
            URL url = null;
            try {
                url = new URL("https://uidai-aadhar.herokuapp.com/user/me");
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("PUT");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("accept", "application/json");
                con.setDoOutput(true);
                String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                String jsonInputString = "{" +
                        "\"username\": \""+android_id +"\"," +
                        "\"password\": " +vid+"," +
                        "\"eKYCXML\": \""+res+"\"" +
                        "}";
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

    public void getEkycData() {
        String fileName = getSharedPreferences("const", MODE_PRIVATE).getString("xmlfile","empty");
        File file = new File(getApplicationContext().getExternalCacheDir(), fileName);
        res ="";
        try (FileInputStream fis = new FileInputStream(file)) {
            int i;
            while((i=fis.read())!=-1){
                System.out.print((char)i);
                if(i == '"'){
                    res +=  "\\"+String.valueOf((char) i);
                }
                else if(i =='\n'){
                    continue;
                }
                else{
                    res += String.valueOf((char)i);
                }

            }
            Log.d(TAG, "onCreate: " + res);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        vid = "";
        String fileNameVid = "vid.txt";
        File fileVid = new File(getApplicationContext().getExternalCacheDir(), fileNameVid);
        try (FileInputStream fis = new FileInputStream(fileVid)) {
            int i;
            while((i=fis.read())!=-1){
                System.out.print((char)i);
                vid += String.valueOf((char)i);

            }
            UpdateData updateData = new UpdateData();
            updateData.start();
            Log.d(TAG, "onCreate: " + res);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}