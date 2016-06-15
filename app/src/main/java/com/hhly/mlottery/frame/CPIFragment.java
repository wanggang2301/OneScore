package com.hhly.mlottery.frame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.CpiFiltrateActivity;
import com.hhly.mlottery.adapter.cpiadapter.CpiCompanyAdapter;
import com.hhly.mlottery.adapter.cpiadapter.CpiDateAdapter;
import com.hhly.mlottery.bean.oddsbean.NewOddsInfo;
import com.hhly.mlottery.bean.websocket.WebFootBallSocketOdds;
import com.hhly.mlottery.bean.websocket.WebFootBallSocketTime;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.frame.oddfragment.CPIOddsFragment;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.UiUtils;
import com.hhly.mlottery.util.cipher.MD5Util;
import com.hhly.mlottery.util.websocket.HappySocketClient;
import com.hhly.mlottery.widget.ExactSwipeRefrashLayout;

import org.java_websocket.drafts.Draft_17;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Tenney on 2016/4/6.
 * 新版指数
 */
public class CPIFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, HappySocketClient.SocketResponseCloseListener, HappySocketClient.SocketResponseErrorListener, HappySocketClient.SocketResponseMessageListener {

    public final static String TYPE_BIG = "big";
    public final static String TYPE_PLATE = "plate";
    public final static String TYPE_OP = "op";

    private Context mContext;
    private View mView;

    private ImageView public_img_back, public_btn_filter, public_btn_set;
    private TextView public_txt_title, public_txt_left_title;//标题
    private CPIFragmentAdapter mCPIViewPagerAdapter;
    public ExactSwipeRefrashLayout mRefreshLayout;
    public LinearLayout public_date_layout;//显示时间的layout
    public TextView public_txt_date;//显示时间的textview
    //热门，公司，筛选
    public ImageView public_img_hot, public_img_company;
    /**
     * 筛选联赛
     */
    public ImageView public_img_filter;

    /**
     * 切换Viewpager的标签
     */
    private TabLayout mTabLayout;
    //记录是否完成viewpager初始化
    private boolean isInitViewPager = false;
    private ViewPager mViewPager;
    private List<Fragment> fragments;
    /*dialog控件 begin*/
    //日期和公司dialog
    private AlertDialog mAlertDialog;
    //多选，公司名称
    private CheckedTextView checktv;
    //是否选中的图片
    private ImageView cpi_img_checked;
    //选公司的时候dialog确认按钮
    private Button cpi_btn_ok;
    //dialog标题
    private TextView titleView;
    //dialoglistview
    private ListView dialog_list;
    private List<Map<String, String>> mMapList;
    /*dialog控件 end*/
    //日期
    private CpiDateAdapter cpiDateAdapter;
    //公司
    private CpiCompanyAdapter cpiCompanyAdapter;

    public String currentDate = "";
    //判断是否选中选择热门
//    public static boolean isHot = true;
    public List<NewOddsInfo.CompanyBean> companys = new ArrayList<>();
    public List<String> companysName = new ArrayList<>();
    private CPIOddsFragment mCPIOddsFragment, mCPIOddsFragment2, mCPIOddsFragment3;
    public List<Map<String, String>> mMapDayList = new ArrayList<>();
    //判断是否是日期选择
    private boolean isFirst = false;
    //默认选择当天，当点击item后改变选中的position
    public int selectPosition;
    //判断是否需要一分钟刷新一次
//    private boolean isTrue;
    //定时刷新线程
//    private MyThread myThread;
    public boolean isVisible = false;
    //推送
    private HappySocketClient hSocketClient;
    private URI hSocketUri = null;
    //心跳时间
    private long pushStartTime;
    private Timer computeWebSocketConnTimer = new Timer();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_cpi, container, false);
        initView();
        initViewPager();
//        if(myThread==null){
//            isTrue=true;
//            myThread=new MyThread();
//            myThread.start();
//        }
        try {
            System.out.println(BaseURLs.URL_CPI_SOCKET);
            hSocketUri = new URI(BaseURLs.URL_CPI_SOCKET);
//            hSocketUri = new URI("ws://192.168.10.242:61634/topic");
//			hSocketUri = new URI("ws://m.1332255.com/ws/USER.topic.indexcenter");
//            hSocketUri = new URI("ws://m.13322.com/ws");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        startWebSocket();
        computeWebSocket();
//        MyT myT = new MyT();
//        myT.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        computeWebSocketConnTimer.cancel();

        if (hSocketClient != null) {
            hSocketClient.close();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        computeWebSocketConnTimer.cancel();

        if (hSocketClient != null) {
            hSocketClient.close();
        }
    }

    class MyT extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(10000);//休眠5s
                } catch (InterruptedException e) {
                    return;
                }
                onMessage("MESSAGE");

            }
        }
    }

    private void computeWebSocket() {
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                // 设置日期格式
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                System.out.println(df.format(new Date()) + "---监听socket连接状态:Open=" +
                        hSocketClient.isOpen() + ",Connecting=" + hSocketClient.isConnecting() +
                        ",Close=" + hSocketClient.isClosed() + ",Closing=" + hSocketClient.isClosing());
                long pushEndTime = System.currentTimeMillis();
                if ((pushEndTime - pushStartTime) >= 5000) {
                    System.out.println("重新启动socket");
                    startWebSocket();
                }
            }
        };
        computeWebSocketConnTimer.schedule(tt, 5000, 5000);
    }

    private synchronized void startWebSocket() {
        if (hSocketClient != null) {
            if (!hSocketClient.isClosed()) {
                hSocketClient.close();
            }

            hSocketClient = new HappySocketClient(hSocketUri, new Draft_17());
            hSocketClient.setSocketResponseMessageListener(this);
            hSocketClient.setSocketResponseCloseListener(this);
            hSocketClient.setSocketResponseErrorListener(this);
            try {
                hSocketClient.connect();
            } catch (IllegalThreadStateException e) {
                hSocketClient.close();
            }
        } else {
            hSocketClient = new HappySocketClient(hSocketUri, new Draft_17());
            hSocketClient.setSocketResponseMessageListener(this);
            hSocketClient.setSocketResponseCloseListener(this);
            hSocketClient.setSocketResponseErrorListener(this);
            try {
                hSocketClient.connect();
            } catch (IllegalThreadStateException e) {
                hSocketClient.close();
            }
        }
    }


    public void onMessage(String message) {
        // TODO Auto-generated method stub
        pushStartTime = System.currentTimeMillis(); // 记录起始时间
        if (message.startsWith("CONNECTED")) {
            String id = "android" + DeviceInfo.getDeviceId(getActivity());
            id = MD5Util.getMD5(id);
            hSocketClient.send("SUBSCRIBE\nid:" + id + "\ndestination:/topic/USER.topic.indexcenter" + "\n\n");
        } else if (message.startsWith("MESSAGE")) {
            String[] msgs = message.split("\n");
            String ws_json = msgs[msgs.length - 1];
            //赔率模拟数据
            //String ws_json = "{'data':[{'comId':'38','leftOdds':'0.25','mediumOdds':'1.75','oddType':'2','rightOdds':'0.25','uptime':'18:40'}],'thirdId':'337089','type':2}";
            //时间模拟数据
//            String ws_json = "{'data':{'keepTime':21,'statusOrigin':1},'thirdId':'337089','type':1} ";
            //比分模拟推送
//            String ws_json = "{'data':{'matchResult':'80:80'},'thirdId':'337089','type':3}";

            int type = 0;
            try {
                JSONObject jsonObject = new JSONObject(ws_json);
                type = jsonObject.getInt("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (type != 0) {
                Message msg = Message.obtain();
                msg.obj = ws_json;
                msg.arg1 = type;
                mSocketHandler.sendMessage(msg);
            }

        }
    }

    public void onError(Exception exception) {
        exception.printStackTrace();
    }

    public void onClose(String message) {
        System.out.println(">>>onClose");
    }

    Handler mSocketHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String ws_json = (String) msg.obj;
            ws_json = ws_json.substring(0, ws_json.length() - 1);
            if (msg.arg1 == 1) {
                // 时间
                upDateTime(ws_json);
            } else if (msg.arg1 == 2) {
                // 赔率
                upDateOdds(ws_json);
            } else if (msg.arg1 == 3) {
                // 主客队得分
                upDateScore(ws_json);
            }
        }
    };

    /**
     * 时间推送
     */
    private void upDateTime(String json) {
        try {
            WebFootBallSocketTime webSocketOddsTime =
                    JSON.parseObject(json, WebFootBallSocketTime.class);
            for (Fragment fragment : fragments) {
                ((CPIOddsFragment) fragment).upDateTimeAndScore(webSocketOddsTime, "time");
            }
        } catch (Exception e) {
            L.i(">>>", "ws_json1异常" + e);
        }
    }

    /**
     * 赔率推送
     */
    public void upDateOdds(String json) {
        try {
            WebFootBallSocketOdds webSocketOdds =
                    JSON.parseObject(json, WebFootBallSocketOdds.class);
            for (int i = 0; i < webSocketOdds.getData().size(); i++) {
                //如果是亚盘的
                if ("1".equals(webSocketOdds.getData().get(i).get("oddType"))) {
                    mCPIOddsFragment.upDateOdds(webSocketOdds, 1);
                }
                //如果是欧赔的
                else if ("2".equals(webSocketOdds.getData().get(i).get("oddType"))) {
                    mCPIOddsFragment3.upDateOdds(webSocketOdds, 2);
                }
                //如果是大小的
                else if ("3".equals(webSocketOdds.getData().get(i).get("oddType"))) {
                    mCPIOddsFragment2.upDateOdds(webSocketOdds, 3);
                }
            }
        } catch (Exception e) {
            L.i(">>>", "ws_json2异常" + e);
        }
    }

    /**
     * 主客队比分推送
     */
    private void upDateScore(String json) {
        try {
            WebFootBallSocketTime webSocketOddsScore =
                    JSON.parseObject(json, WebFootBallSocketTime.class);
            for (Fragment fragment : fragments) {
                ((CPIOddsFragment) fragment).upDateTimeAndScore(webSocketOddsScore, "score");
            }
        } catch (Exception e) {
            L.i(">>>", "ws_json3异常" + e);
        }
    }

    private void initView() {
        mRefreshLayout = (ExactSwipeRefrashLayout) mView.findViewById(R.id.cpi_refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.tabhost);
        mRefreshLayout.setOnRefreshListener(this);

        //中心标题
        public_txt_title = (TextView) mView.findViewById(R.id.public_txt_title);
        public_txt_title.setVisibility(View.GONE);
        //左边标题
        public_txt_left_title = (TextView) mView.findViewById(R.id.public_txt_left_title);
        public_txt_left_title.setVisibility(View.VISIBLE);
        public_txt_left_title.setText(R.string.football_detail_odds_tab);
        //筛选
        public_btn_filter = (ImageView) mView.findViewById(R.id.public_btn_filter);
        public_btn_filter.setVisibility(View.GONE);

        //设置
        public_btn_set = (ImageView) mView.findViewById(R.id.public_btn_set);
        public_btn_set.setVisibility(View.GONE);
        //返回
        public_img_back = (ImageView) mView.findViewById(R.id.public_img_back);
        public_img_back.setOnClickListener(this);
        //显示时间的布局
        public_date_layout = (LinearLayout) mView.findViewById(R.id.public_date_layout);
        //显示时间的textview
        public_txt_date = (TextView) mView.findViewById(R.id.public_txt_date);

        public_txt_date.setOnClickListener(this);
        //热门，公司，筛选
        public_img_hot = (ImageView) mView.findViewById(R.id.public_img_hot);
        public_img_hot.setOnClickListener(this);
        public_img_hot.setVisibility(View.GONE);
        public_img_hot.setSelected(true);

        public_img_company = (ImageView) mView.findViewById(R.id.public_img_company);
        public_img_company.setOnClickListener(this);

        public_img_filter = (ImageView) mView.findViewById(R.id.public_img_filter);
        public_img_filter.setOnClickListener(this);
        public_img_filter.setVisibility(View.INVISIBLE);

        mTabLayout = (TabLayout) mView.findViewById(R.id.tabs);
    }

    //初始化viewPager
    private void initViewPager() {
        mViewPager = (ViewPager) mView.findViewById(R.id.cpi_viewpager);
        fragments = new ArrayList<>();
        //亚盘
//        fragments.add(CPIOddsFragment.newInstance("plate", ""));
//        //大小
//        fragments.add(CPIOddsFragment.newInstance("big", ""));
//        //欧赔
//        fragments.add(CPIOddsFragment.newInstance("op", ""));
        //亚盘
        mCPIOddsFragment = CPIOddsFragment.newInstance(TYPE_PLATE, "");
        fragments.add(mCPIOddsFragment);
        //大小
        mCPIOddsFragment2 = CPIOddsFragment.newInstance(TYPE_BIG, "");
        fragments.add(mCPIOddsFragment2);
        //欧赔
        mCPIOddsFragment3 = CPIOddsFragment.newInstance(TYPE_OP, "");
        fragments.add(mCPIOddsFragment3);

        mCPIViewPagerAdapter = new CPIFragmentAdapter(getChildFragmentManager(), fragments);
        mViewPager.setOffscreenPageLimit(2);//设置预加载页面的个数。
        mViewPager.setAdapter(mCPIViewPagerAdapter);

        //标记是否初始化
        isInitViewPager = true;
        mTabLayout.setupWithViewPager(mViewPager);

    }

    /**
     * 推送比赛信息
     */
    public void startSocket() {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.public_img_back:
                if (getActivity() == null) return;
                getActivity().finish();
                break;
            case R.id.public_txt_date://点击日期的textview
                if (mMapDayList.size() == 14) {
                    setDialog(0);//代表日期
                } else {
                    UiUtils.toast(mContext, R.string.loading_txt);
                }

                break;
            case R.id.public_img_hot://点击热门
                break;
            case R.id.public_img_company://点击公司
                setDialog(1);//代表公司
                break;
            case R.id.public_img_filter://点击筛选
                if (UiUtils.onDoubClick()) {
                    Intent intent = new Intent(mContext, CpiFiltrateActivity.class);
                    //如果选择的是日期不传选中的
                    if (isFirst) {
                        intent.putExtra("fileterTags", (Serializable) CPIOddsFragment.mFileterTagsBean);
                        ddList.clear();
                        intent.putExtra("linkedListChecked", ddList);
                        isFirst = false;
                    }
                    //否者要传选中的id
                    else {
                        intent.putExtra("fileterTags", (Serializable) CPIOddsFragment.mFileterTagsBean);
                        intent.putExtra("linkedListChecked", ddList);
                    }
                    startActivityForResult(intent, 10086);
                }
                break;
            default:
                break;
        }
    }

    LinkedList<String> ddList = new LinkedList<>();

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == 10086) {
            switch (resultCode) {
                case 0:
                    ArrayList<String> checkedIdExtra = (ArrayList<String>)
                            data.getSerializableExtra("key");
                    ddList.clear();
                    ddList.addAll(checkedIdExtra);
                    filtrateDate();
                    mCPIOddsFragment.selectCompany(companysName, ddList, TYPE_PLATE);
                    mCPIOddsFragment2.selectCompany(companysName, ddList, TYPE_BIG);
                    mCPIOddsFragment3.selectCompany(companysName, ddList, TYPE_OP);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isVisible) {
                    for (Fragment fragment : fragments) {
                        ((CPIOddsFragment) fragment).switchd("", 0);
                    }
                } else {
                    //设置标题时间
                    for (Fragment fragment : fragments) {
                        //代表刷新
                        ((CPIOddsFragment) fragment).switchd(currentDate, 2);
                    }
                    public_txt_date.setText(currentDate);
                }
                mRefreshLayout.setRefreshing(false);
            }
        }, 500);

    }

    /**
     * 初始化 设置 dialog 弹窗
     *
     * @param dateAndCompanyNumber 0代表日期，1代表公司
     */
    public void setDialog(int dateAndCompanyNumber) {
        // Dialog 设置
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
        View view = View.inflate(mContext, R.layout.alertdialog, null);
        titleView = (TextView) view.findViewById(R.id.titleView);
        dialog_list = (ListView) view.findViewById(R.id.listdate);
        //当选择公司的时候 确认按钮
        cpi_btn_ok = (Button) view.findViewById(R.id.cpi_btn_ok);
        mAlertDialog = mBuilder.create();
        mAlertDialog.setCancelable(true);
        mAlertDialog.setCanceledOnTouchOutside(true);
        //如果选择的是日期
        if (dateAndCompanyNumber == 0) {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 1000);
            dialog_list.setLayoutParams(params);
            titleView.setText(R.string.tip);
            //这个是显示日期的适配器
            cpiDateAdapter = new CpiDateAdapter(mContext, mMapDayList);
            dialog_list.setAdapter(cpiDateAdapter);
            //默认选中当天
            cpiDateAdapter.setDefSelect(selectPosition);
            dialog_list.setSelection(selectPosition);
            dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    CpiFiltrateActivity.isDefualHot = true;
                    // 记录点击的 item 位置
                    selectPosition = position;
                    //点击之后给date赋值
                    currentDate = mMapList.get(position).get("date");
                    //设置标题时间
                    public_txt_date.setText(currentDate);

                    for (Fragment fragment : fragments) {
                        //代表日期
                        ((CPIOddsFragment) fragment).switchd(currentDate, 1);
                    }
                    isFirst = true;
                    // 关闭 dialog弹窗
                    mAlertDialog.dismiss();
                }
            });
        } else {
            //否则就是公司
            titleView.setText(R.string.odd_company_txt);
            cpiCompanyAdapter = new CpiCompanyAdapter(mContext, companys);
            dialog_list.setAdapter(cpiCompanyAdapter);
            //设置你的listview的item不能被获取焦点,焦点由listview里的控件获得
            dialog_list.setItemsCanFocus(false);
            //设置多选
            dialog_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            //确定按钮
            view.findViewById(R.id.cpi_view_id).setVisibility(View.VISIBLE);
            cpi_btn_ok.setVisibility(View.VISIBLE);

            final boolean[] tempCompanyCheckedStatus = new boolean[companys.size()];
            for (int n = 0; n < companys.size(); n++) {
                tempCompanyCheckedStatus[n] = companys.get(n).isChecked();
            }
            dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View arg1, final int position, long arg3) {
                    checktv = (CheckedTextView) parent.getChildAt(position).findViewById(R.id.item_checkedTextView);
                    cpi_img_checked = (ImageView) parent.getChildAt(position).findViewById(R.id.item_img_checked);
                    checktv.setChecked(!checktv.isChecked());
                    //如果是选中
                    if (checktv.isChecked()) {
                        cpi_img_checked.setBackgroundResource(R.mipmap.cpi_img_select_true);
                    } else {
                        cpi_img_checked.setBackgroundResource(R.mipmap.cpi_img_select);
                    }
                    tempCompanyCheckedStatus[position] = checktv.isChecked();
                }
            });

            cpi_btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int count = 0;
                    for (boolean status : tempCompanyCheckedStatus) {
                        if (status) {
                            count++;
                        }
                    }

                    if (count < 1) {
                        UiUtils.toast(mContext, R.string.cpi_select_company, Toast.LENGTH_SHORT);
                        return;
                    }

                    for (int n = 0; n < companys.size(); n++) {
                        companys.get(n).setIsChecked(tempCompanyCheckedStatus[n]);
                    }
                    filtrateDate();
                    mCPIOddsFragment.selectCompany(companysName, CpiFiltrateActivity.mCheckedIds, TYPE_PLATE);
                    mCPIOddsFragment2.selectCompany(companysName, CpiFiltrateActivity.mCheckedIds, TYPE_BIG);
                    mCPIOddsFragment3.selectCompany(companysName, CpiFiltrateActivity.mCheckedIds, TYPE_OP);

                    mAlertDialog.dismiss();
                }
            });

        }

        //点击view关闭dialog
        view.findViewById(R.id.alertdialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.dismiss();
            }
        });

        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(view);

    }

    private void addDate(int offset) {
        Map<String, String> map = new HashMap<>();
        map.put("date", UiUtils.getDate(currentDate, offset));
        mMapList.add(map);
    }

    /**
     * 获取日期列表
     *
     * @return
     */
    public List<Map<String, String>> getDate() {
        mMapList = new ArrayList<>();
        for (int i = -6; i < 8; i++) {
            addDate(i);
        }
        return mMapList;
    }

    /**
     * 筛选日期
     */
    public void filtrateDate() {
        companysName.clear();
        for (int k = 0; k < companys.size(); k++) {
            if (companys.get(k).isChecked()) {
                companysName.add(companys.get(k).getComName());
            }
        }
    }

    class CPIFragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragmentList;

        public CPIFragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
            super(fragmentManager);
            this.mFragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.odd_plate_rb_txt);
                case 1:
                    return getString(R.string.asiasize);
                case 2:
                    return getString(R.string.odd_op_rb_txt);
                default:
                    return getString(R.string.odd_plate_rb_txt);
            }
        }
    }
}
