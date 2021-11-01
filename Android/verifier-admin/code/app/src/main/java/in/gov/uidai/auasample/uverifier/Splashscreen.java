package in.gov.uidai.auasample.uverifier;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import in.gov.uidai.auasample.R;

public class Splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,1024);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_splashscreen);
//        Animation ttb = AnimationUtils.loadAnimation(this, R.anim.fade1);
        TextView tvTitle = findViewById(R.id.tv_ss_title);
//        tvTitle.setAnimation(ttb);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), AdminMenu.class);
                startActivity(intent);
                finish();
            }
        }, 1800);
    }
}