package cn.yanweijia.slimming;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class LauncherActivity extends AppCompatActivity {
    private static final String TAG = "LauncherActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        startActivity(new Intent(LauncherActivity.this,LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: " + getString(R.string.on_destroy));
        super.onDestroy();
    }
}
