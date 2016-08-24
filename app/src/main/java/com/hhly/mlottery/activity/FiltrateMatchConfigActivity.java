package com.hhly.mlottery.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.footframe.FiltrateMatchFragment;
import com.hhly.mlottery.frame.footframe.FiltrateMatchFragment.CheckedCupsCallback;
import com.hhly.mlottery.frame.footframe.FiltrateStatusFragment;
import com.hhly.mlottery.frame.footframe.ImmediateFragment;
import com.hhly.mlottery.frame.footframe.ResultFragment;
import com.hhly.mlottery.frame.footframe.RollBallFragment;
import com.hhly.mlottery.frame.footframe.ScheduleFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author chenml
 * @Desc 足球筛选界面
 */
public class FiltrateMatchConfigActivity extends BaseActivity implements OnClickListener, CheckedCupsCallback {

    public final static String ALL_CUPS = "allCups";
    public final static String CHECKED_CUPS = "checkedCups";
    public final static String RESULT_CHECKED_CUPS_IDS = "checkedCupIds";
    public final static String CHECKED_DEFUALT = "checkedDefualt";
    public final static String NET_STATUS = "isNetSuccess";

    private final static String TAG = "FiltrateMatchConfigActivity";
    private Parcelable[] mCups;
    private Parcelable[] mCheckedCups;
    private boolean isDefualt;
    private boolean isNetSuccess;

    private RadioGroup mTab;
    private RadioButton mMatchTab;
    private RadioButton mStatusTab;

    private ImageView public_img_back;// 筛选界面返回

    private Button mSubmit;

    private int mFragmentMatchId = 0;

    private View mNetworkExceptionLayout;
    private View mSubmitLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrate_match_config);

        initData();
        initView();

    }

    private void initView() {
        TextView public_txt_title = (TextView) findViewById(R.id.public_txt_title);
        public_txt_title.setText(R.string.filtrate_match_top);

        public_img_back = (ImageView) findViewById(R.id.public_img_back);
        public_img_back.setOnClickListener(this);

        mNetworkExceptionLayout = findViewById(R.id.network_exception_layout);
        mSubmitLayout = findViewById(R.id.filtrate_submit_layout);

        public_img_back.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.number_back_icon));

        mTab = (RadioGroup) findViewById(R.id.filtrate_tab_rg);
        mMatchTab = (RadioButton) findViewById(R.id.filtrate_match_tab);
        mStatusTab = (RadioButton) findViewById(R.id.filtrate_status_tab);

        findViewById(R.id.public_btn_set).setVisibility(View.GONE);
        findViewById(R.id.public_btn_filter).setVisibility(View.GONE);

        if (!isNetSuccess) {
            mNetworkExceptionLayout.setVisibility(View.VISIBLE);
            mSubmitLayout.setVisibility(View.GONE);

            findViewById(R.id.network_exception_reload_layout).setVisibility(View.GONE);

            return;
        } else {
            mNetworkExceptionLayout.setVisibility(View.GONE);
            mSubmitLayout.setVisibility(View.VISIBLE);
        }

        final FiltrateMatchFragment fragmentMatch = FiltrateMatchFragment.newInstance(mCups, mCheckedCups, isDefualt);
        final FiltrateStatusFragment fragmentStatus = new FiltrateStatusFragment();

        mTab.check(R.id.filtrate_match_tab);
        mTab.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (checkedId) {
                    case R.id.filtrate_match_tab:
                        MobclickAgent.onEvent(mContext, "Football_Filtrate_Match");
                        transaction.show(fragmentMatch);
                        transaction.hide(fragmentStatus);
                        break;
                    case R.id.filtrate_status_tab:
                        MobclickAgent.onEvent(mContext, "Football_Filtrate_Status");
                        transaction.show(fragmentStatus);
                        transaction.hide(fragmentMatch);
                        break;
                    default:
                        break;
                }
                transaction.commit();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.filtrate_match_frame, fragmentStatus);

        transaction.hide(fragmentStatus);
        transaction.add(R.id.filtrate_match_frame, fragmentMatch);

        transaction.commit();

        mFragmentMatchId = fragmentMatch.getId();
        //Log.i(TAG, "fragmentMatch.getId() = " + fragmentMatch.getId());
        mSubmit = (Button) findViewById(R.id.filtrate_submit_btn);
        mSubmit.setOnClickListener(this);
        // mCancel = (Button) findViewById(R.id.filtrate_cancel_btn);
        // mCancel.setOnClickListener(this);

        // mSubmit.setVisibility(View.GONE);
        // mCancel.setVisibility(View.GONE);

    }

    private int currentFragmentId = 0;

    private void initData() {
        Intent intent = getIntent();

        mCups = intent.getParcelableArrayExtra(ALL_CUPS);
        mCheckedCups = intent.getParcelableArrayExtra(CHECKED_CUPS);
        isDefualt = intent.getBooleanExtra(CHECKED_DEFUALT, false);
        isNetSuccess = intent.getBooleanExtra(NET_STATUS, false);
        currentFragmentId = intent.getIntExtra("currentFragmentId", 0);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.public_img_back:// 关闭
                finish();
                MobclickAgent.onEvent(mContext, "Football_Filtrate_Exit");
                //overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
                break;
            case R.id.filtrate_submit_btn:
                // Log.i(TAG, "submit - -");
                MobclickAgent.onEvent(mContext, "Football_Filtrate_Save");
                FragmentManager fragmentManager = getSupportFragmentManager();

                if (fragmentManager.findFragmentById(mFragmentMatchId) != null) {
                    FiltrateMatchFragment filtrateMatchFragment = (FiltrateMatchFragment) fragmentManager.findFragmentById(
                            mFragmentMatchId);

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();

                    LinkedList<String> list = filtrateMatchFragment.getCheckedCups();

                    String[] checkedCups = list.toArray(new String[]{});

                    bundle.putCharSequenceArray(RESULT_CHECKED_CUPS_IDS, checkedCups);
                    bundle.putBoolean(CHECKED_DEFUALT, false);
                    intent.putExtras(bundle);

                    Map<String, Object> map = new HashMap<>();
                    map.put("checkedCupIds", list);
                    map.put("checkedDefualt", false);


                    if (currentFragmentId == 0) {
                        RollBallFragment.eventBus.post(map);
                    } else if (currentFragmentId == 1) {
                        ImmediateFragment.imEventBus.post(map);
                    } else if (currentFragmentId == 2) {
                        ResultFragment.resultEventBus.post(map);
                    } else if (currentFragmentId == 3) {
                        ScheduleFragment.schEventBus.post(map);
                    }

                    //setResult(Activity.RESULT_OK, intent);
                }

                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            //overridePendingTransition(R.anim.push_fix_out, R.anim.push_left_out);
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onChange(LinkedList<String> checkedCups) {
        //		Log.i(TAG, "checkedCups.size = " + checkedCups.size());
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
//        MobclickAgent.onPageStart("FiltrateMatchConfigActivity");
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
//        MobclickAgent.onPageEnd("FiltrateMatchConfigActivity");
    }
}
