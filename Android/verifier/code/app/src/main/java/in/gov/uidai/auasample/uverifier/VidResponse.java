package in.gov.uidai.auasample.uverifier;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import in.gov.uidai.auasample.R;

public class VidResponse extends AppCompatActivity {

    private String vid;
    private String TAG = "VIDRES";
    private String res;

    //9187299279607360
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vid_response);
        Bundle extras = getIntent().getExtras();
        String response = extras.getString("response");
        vid = response.split(",")[1].split(":")[1];
        TextView tvVid = findViewById(R.id.tv_vid_dis_res);
        tvVid.setText("your vid: " + vid);
        String filename = "vid.txt";
        File file = new File(getApplicationContext().getExternalCacheDir(), filename);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(vid.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getEkycData();

//        https://uidai-aadhar.herokuapp.com/user/register

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }

    private void getEkycData() {
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
            File filer = new File(getApplicationContext().getExternalCacheDir(), "abc.txt");
            try (FileOutputStream fos = new FileOutputStream(filer)) {
                fos.write(res.getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "onCreate: " + res);
            SendData sendData = new SendData();
            sendData.start();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class SendData extends Thread{
        String url;

        SendData(){
        }

        public void run(){
            URL url = null;
            try {
                url = new URL("https://uidai-aadhar.herokuapp.com/user/register");
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("accept", "application/json");
                con.setDoOutput(true);
                String encodedRes =  Base64.encodeToString(res.getBytes(), Base64.DEFAULT);
                Log.d(TAG, "run: encodedRes : "  + encodedRes);
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

}