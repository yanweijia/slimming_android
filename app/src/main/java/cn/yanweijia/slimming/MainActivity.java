package cn.yanweijia.slimming;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

import cn.yanweijia.slimming.dao.DBManager;
import cn.yanweijia.slimming.fragment.analyze.AnalyzeFragment;
import cn.yanweijia.slimming.fragment.diet.DietFragment;
import cn.yanweijia.slimming.fragment.health.HealthFragment;
import cn.yanweijia.slimming.fragment.me.MeFragment;
import cn.yanweijia.slimming.fragment.sport.SportFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    // custom Handler,weak reference
    private Handler myHandler;
    // message type
    private static final int MSG_EXIT_APP = 1;

    // BottomNavBar relevant
    private int[] bottomBarTitles = {R.string.title_sport, R.string.title_analyze, R.string.title_diet, R.string.title_health, R.string.title_me};
    private int[] bottomBarColors = {R.color.deepGrey, R.color.deepGrey, R.color.deepGrey, R.color.deepGrey, R.color.deepGrey};
    private int[] bottomBarImages = {R.drawable.sport, R.drawable.analyze, R.drawable.diet, R.drawable.health, R.drawable.me};


    //fragments
    private SportFragment sportFragment;
    private AnalyzeFragment analyzeFragment;
    private DietFragment dietFragment;
    private HealthFragment healthFragment;
    private MeFragment meFragment;

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
    private void initViews() {
        setDefaultFragment();
        final BottomNavigationView navView = findViewById(R.id.bottomNavigation);
        for (int i = 0; i < bottomBarTitles.length; i++) {
            BottomNavigationItem item = new BottomNavigationItem(getString(bottomBarTitles[i]), ContextCompat.getColor(MainActivity.this, bottomBarColors[i]), bottomBarImages[i]);
            navView.addTab(item);
        }
        navView.willNotRecreate(true);
        navView.disableShadow();
        navView.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                Log.d(TAG, "onNavigationItemClick: Item " + index + ":" + getString(bottomBarTitles[index]) + " clicked");
                FragmentManager fm = MainActivity.this.getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                switch (index) {
                    case 0: //sport
                        if (sportFragment == null)
                            sportFragment = SportFragment.newInstance();
                        transaction.replace(R.id.fragment_main, sportFragment);
                        break;
                    case 1: //analyze
                        if (analyzeFragment == null)
                            analyzeFragment = AnalyzeFragment.newInstance();
                        transaction.replace(R.id.fragment_main, analyzeFragment);
                        break;
                    case 2: //diet
                        if (dietFragment == null)
                            dietFragment = DietFragment.newInstance();
                        transaction.replace(R.id.fragment_main, dietFragment);
                        break;
                    case 3: //health
                        if (healthFragment == null)
                            healthFragment = HealthFragment.newInstance();
                        transaction.replace(R.id.fragment_main, healthFragment);
                        break;
                    case 4: //me
                        if (meFragment == null)
                            meFragment = MeFragment.newInstance();
                        transaction.replace(R.id.fragment_main, meFragment);
                        break;
                    default:
                        Log.d(TAG, "onNavigationItemClick: Error! index is :" + index);
                }
                transaction.commit();
            }
        });
    }

    /**
     * init the first Fragment and set to default
     */
    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (sportFragment == null)
            sportFragment = SportFragment.newInstance();
        transaction.replace(R.id.fragment_main, sportFragment);
        transaction.commit();
    }

    /**
     * initial Datas
     */
    private void initDatas() {
        myHandler = new MainActivityHandler(MainActivity.this);
        DBManager.initSQLiteDB(this);
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
    static class MainActivityHandler extends Handler {
        //弱引用<引用外部类>
        WeakReference<MainActivity> mActivity;

        MainActivityHandler(MainActivity activity) {
            this.mActivity = new WeakReference<>(activity);
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
