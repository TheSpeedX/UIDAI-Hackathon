package com.example.uverifier;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;


public class VidScreen extends AppCompatActivity {

    private String res;

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
        res ="";
        try (FileInputStream fis = new FileInputStream(file)) {
            int i;
            while((i=fis.read())!=-1){
                System.out.print((char)i);
                res += String.valueOf((char)i);
            }
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

        findViewById(R.id.iv_copytoclip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClipboard(getApplicationContext(),res.substring(1,res.length()-1));
            }
        });
    }

    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
            Toast.makeText(getApplicationContext(), "copied" +  res , Toast.LENGTH_SHORT).show();
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), "copied" +  res , Toast.LENGTH_SHORT).show();
        }
    }
}