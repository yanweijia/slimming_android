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
    // custom Handler,weak reference
    private Handler myHandler;
    // message type
    private static final int MSG_EXIT_APP = 1;

    // BottomNavBar relevant
    private int[] bottomBarTitles = {R.string.title_sport, R.string.title_analyze, R.string.title_diet, R.string.title_health, R.string.title_me};
    private int[] bottomBarColors = {R.color.bottomBarDefault, R.color.bottomBarDefault, R.color.bottomBarDefault, R.color.bottomBarDefault, R.color.bottomBarDefault};
    private int[] bottomBarImages = {R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background};
    /**
     * tap twice to exit application
     */
    public boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        initDatas();
    }

    /**
     * initial views
     */
    private void initViews(){
        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        for (int i = 0; i < bottomBarTitles.length; i++) {
            BottomNavigationItem item = new BottomNavigationItem(getString(bottomBarTitles[i]), ContextCompat.getColor(MainActivity.this, bottomBarColors[i]), bottomBarImages[i]);
            bottomNavigationView.addTab(item);
        }
        bottomNavigationView.willNotRecreate(true);
        bottomNavigationView.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                Log.d(TAG, "onNavigationItemClick: Item " + index + ":" + getString(bottomBarTitles[index]) + " clicked");
                switch (index) {
                    case 0: //sport
                        break;
                    case 1: //analyze
                        break;
                    case 2: //diet
                        break;
                    case 3: //health
                        break;
                    case 4: //me
                        break;
                    default:
                        Log.d(TAG, "onNavigationItemClick: Error! index is :" + index);
                }
            }
        });
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
            Log.d(TAG, "onKeyDown: Back button Pressed");
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
     * <i>weak reference is good for JAVA GC</i>
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
