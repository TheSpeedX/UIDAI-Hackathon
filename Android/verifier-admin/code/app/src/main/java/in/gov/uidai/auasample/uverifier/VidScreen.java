package in.gov.uidai.auasample.uverifier;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;

import in.gov.uidai.auasample.R;

public class VidScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vid_screen);
        getVidFromStorage();
        setButtons();
    }

    private void getVidFromStorage() {
        String filename = "vid.txt";
        File file = new File(getApplicationContext().getExternalCacheDir(), filename);
        String res ="";
        try (FileInputStream fis = new FileInputStream(file)) {
            int i;
            while((i=fis.read())!=-1){
                System.out.print((char)i);
                res += String.valueOf((char)i);
            }
            Toast.makeText(getApplicationContext(), res , Toast.LENGTH_LONG).show();
            TextView vid = findViewById(R.id.tv_vid_display);
            vid.setText("your vid : " + res);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setButtons() {
        findViewById(R.id.btn_vid_retrive).setVisibility(View.GONE);
        findViewById(R.id.btn_vid_retrive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext() , MainActivity.class);
//                intent.putExtra("reason" , "vid retrieve");
//                startActivity(intent);
            }
        });
        findViewById(R.id.btn_vid_generate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , CapchaActivity.class);
                intent.putExtra("reason" , "vid generate");
                startActivity(intent);
            }
        });
    }
}