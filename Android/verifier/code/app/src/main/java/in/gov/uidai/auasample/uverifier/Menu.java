package in.gov.uidai.auasample.uverifier;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;

import in.gov.uidai.auasample.R;

public class Menu extends AppCompatActivity {

    private static final int EXTERNAL_STORAGE_PERMISSION_CODE = 100;
    private boolean permissionToStore;
    private static final String TAG = "MENU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if(!(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
        }

        findViewById(R.id.btn_vid).setVisibility(View.GONE);
        findViewById(R.id.btn_Ekyc).setVisibility(View.GONE);
        findViewById(R.id.btn_Ekyc2).setVisibility(View.GONE);
        CheckIfFirstTime();
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


    private void CheckIfFirstTime() {
        String keyFirst = "FIRST";
        String keyVid = "VID";
        String keyEkyc = "EKYC";


//        if ( getSharedPreferences("SIGN", MODE_PRIVATE).getBoolean(keyFirst,true) ) {
//            Intent intent = new Intent(getApplicationContext(),FirstSignActivity.class);
//            intent.putExtra("reason" , "data retrive");
//            startActivity(intent);
//            finish();
//        }

        if (getSharedPreferences("SIGN", MODE_PRIVATE).getBoolean(keyEkyc,true) ) {
            Intent intent = new Intent(getApplicationContext(),FirstSignActivity.class);
            intent.putExtra("reason" , "ekyc");
            startActivity(intent);
            finish();
        }

        else if (getSharedPreferences("SIGN", MODE_PRIVATE).getBoolean(keyVid,true) ) {
            Toast.makeText(getApplicationContext(), "vid generation" , Toast.LENGTH_LONG);
            Intent intent = new Intent(getApplicationContext() , CapchaActivity.class);
            intent.putExtra("reason" , "vid generate");
            intent.putExtra("msg" , "Generating VID ,STEP: 2/2");
            startActivity(intent);
            finish();
        }


    }

    private void setButtons() {
        findViewById(R.id.btn_vid).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_Ekyc).setVisibility(View.VISIBLE);
//        findViewById(R.id.btn_Ekyc2).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_vid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VidScreen.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_Ekyc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , CapchaActivity.class);
                intent.putExtra("reason" , "ekyc");
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_Ekyc2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CapchaActivity.class);
                intent.putExtra("reason" , "data retrive");
                startActivity(intent);
            }
        });
    }
}