package cn.yanweijia.slimming;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Handler myHandler;
    private static final int MSG_EXIT_APP = 1;
    /**
     * tap twice to exit application
     */
    public boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        BottomNavigationItem bottomNavigationItem0 = new BottomNavigationItem("Record", getResources().getColor(R.color.bottomBarDefault), R.drawable.ic_launcher_background);
        BottomNavigationItem bottomNavigationItem1 = new BottomNavigationItem("Like", ContextCompat.getColor(this, R.color.bottomBarDefault), R.drawable.ic_launcher_background);
        bottomNavigationView.addTab(bottomNavigationItem0);
        bottomNavigationView.addTab(bottomNavigationItem1);
        bottomNavigationView.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                Log.d(TAG, "onNavigationItemClick: Item "+index+" clicked");
            }
        });
        bottomNavigationView.willNotRecreate(true);

        initDatas();
    }


    /**
     * initial Datas
     */
    private void initDatas() {
        myHandler = new MyHandler(MainActivity.this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //tap twice on the key to exit.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: " + getString(R.string.on_destroy));
    }


    /**
     * tap twice on the back key to exit
     */
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), R.string.exit_hint, Toast.LENGTH_SHORT).show();
            myHandler.sendEmptyMessageDelayed(MSG_EXIT_APP, getResources().getInteger(R.integer.twice_back_time));
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);
        }
    }


    /**
     * custom my handler <br/>
     * <i>weak reference is good for JAVA GB</i>
     *
     * @author weijia
     * @ref <a href="http://blog.csdn.net/nzfxx/article/details/51854305">解决handler警告问题</a>
     */
    static class MyHandler extends Handler {
        //弱引用<引用外部类>
        WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            this.mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity activity = mActivity.get();
            if (activity == null)
                return;
            //Do something
            switch (msg.what) {
                case MSG_EXIT_APP:
                    activity.isExit = false;
                    break;
                default:
                    Log.e(TAG, "handleMessage: " + String.format(activity.getString(R.string.handle_unknow_msg), msg.what, msg.getWhen(), msg.getData()));
            }
        }
    }
}
