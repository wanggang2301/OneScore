package com.hhly.mlottery.frame.numbersframe;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.NumbersInfoBaseActivity;
import com.hhly.mlottery.bean.numbersBean.NumberCurrentInfo;
import com.hhly.mlottery.bean.numbersBean.NumbersHistoryBean;
import com.hhly.mlottery.bean.numbersBean.NumbersOpenBean;
import com.hhly.mlottery.config.StaticValues;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.DisplayUtil;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.NumberDataUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 开奖列表内容详情
 *
 * @author Tenney
 * @ClassName: NumberContentFragment
 * @date 2015-10-19 下午2:48:32
 */
public class HistoryNumberFragment extends Fragment implements OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View view;// 总布局
    private Context mContext;
    private ImageView number_new_icon;// 最新图标
    private NumberCurrentInfo mNumberInfo;// 获取传递过来彩票对象
    private String mNumberName;
    private boolean isOpenNumberStartHistory = false;// 获取彩种开奖状态
    private ListView mListView;// 往期开奖列表
    private numbersListViewAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FrameLayout fl_number_history_list;// 往期开奖列表展示容器
    private FrameLayout fl_numbner_history_info;// 往期开奖详情展示容器

    private ScrollView mScrollView;

    private List<NumberCurrentInfo> mNumberHistoryList;// 往期开奖集合
    //    private List<NumberHistoryHKInfo> mNumberHKList;// 往期六合 彩开奖集合
    private List<NumberCurrentInfo> numberlist = new ArrayList<>();// 各种彩票开奖对象

    private int count = 0;// item数据

    private LinearLayout loading;// 加载
    private LinearLayout errorLoading;// 加载失败
    private TextView reLoading;// 重新加载

    private static final int SUCCESS = 0;// 加载成功
    private static final int STARTLOADING = 1;// 开始加载
    private static final int ERRORLOADING = 2;// 加载失败
    private final static int ANEWREFRESH = 4;// 获取开奖状态失败
    private final static int REFRESH = 5;// 获取开奖状态失败
    private final static int OPENNUMBER = 6;// 正在开奖中

    private int viewPagerIndex = 0;// 当前用户选中的往期数据下标

    private ImageView left;// 上一期
    private ImageView right;// 下一期
    private ImageView iv_lottery_left;
    private ImageView iv_lottery_right;

    private LinearLayout ll_scan_animation;// 动画布局
    private ImageView iv_number_copy;// 整个布局图片

    private List<String> numbers = new ArrayList<String>();// 存放开奖号码
    private List<String> zodiacs = new ArrayList<String>();// 存放六合彩生肖

    private boolean isAnimationStart = false;// 是否正在播放动画

    private String serverTime;// 服务器时间戳
    private long mNumberTime;// 获取开奖时间
    private boolean isCountDown = false;// 启动倒计时
    private boolean isExit = true;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mNumberName = null;
        mNumberName = getArguments().getString("mNumberName");

//        if ("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)) {
        view = inflater.inflate(R.layout.numbers_history_page, container, false);// 国语
//        } else {
//            view = inflater.inflate(R.layout.numbers_history_page_i18n, container, false);// 英语
//        }

        initView();
        initIsOpenNumberData();// 获取彩种开奖状态
        initEvent();
        return view;
    }

    /**
     * 获取彩种开奖状态
     */
    private void initIsOpenNumberData() {
        numbersDataShow(0);
    }

    /**
     * 获取后台判断当前彩种是否正在开奖中
     */
    private void initIsOpenNumberStart() {
        try {
            for (int i = 0, len = numberlist.size(); i < len; i++) {
                if (null != numberlist.get(i)) {
                    if ("1".equals(mNumberName) && "1".equals(numberlist.get(i).getName())) {
                        long numTime = DateUtil.getCurrentTime(numberlist.get(i).getNextTime()) - Long.parseLong(serverTime);

                        // 正在开奖中...
                        isOpenNumberStartHistory = numTime <= 0 || !numberlist.get(i).getNumbers().contains("#");
                    } else if ("30".equals(mNumberName) || "31".equals(mNumberName) || "32".equals(mNumberName)) {
                        // 此彩种没有开奖状态
                        isOpenNumberStartHistory = false;
                    } else if (mNumberName.equals(numberlist.get(i).getName())) {
                        long numTime = DateUtil.getCurrentTime(numberlist.get(i).getNextTime()) - Long.parseLong(serverTime);
                        // 正在开奖中...
                        isOpenNumberStartHistory = numTime <= 0;
                    }
                }
            }
        } catch (Exception e) {
            L.d("时间日期转换异常！" + e.getMessage());
        }
    }

    /**
     * 获取当前对象
     */
    private void getCurrentBean() {
        // 获取当前要显示的对象
        for (int i = 0; i < numberlist.size(); i++) {
            if (null != numberlist.get(i)) {
                if (mNumberName.equals(numberlist.get(i).getName())) {
                    mNumberInfo = numberlist.get(i);
                }
            }
        }
    }

    /**
     * 请求后台数据
     */
    public synchronized void numbersDataShow(final int num) {

        if (num == 0) {
            // 发送消息，开始加载数据
            mHandler.sendEmptyMessage(STARTLOADING);
        }

        VolleyContentFast.requestJsonByGet(AppConstants.numberHistoryURLs[0], new VolleyContentFast.ResponseSuccessListener<NumbersOpenBean>() {
            @Override
            public synchronized void onResponse(final NumbersOpenBean json) {
                if (null != json && json.getNumLotteryResults() != null) {// 判断数据是否为空

                    numberlist.clear();

                    serverTime = json.getServerTime();// 获取服务器当前时间
                    numberlist.addAll(json.getNumLotteryResults());

                    if (num == 1) {
                        mHandler.sendEmptyMessage(ANEWREFRESH);
                    } else if (num == 2) {
                        mHandler.sendEmptyMessage(OPENNUMBER);
                    } else {
                        mHandler.sendEmptyMessage(REFRESH);
                    }
                } else {
                    mHandler.sendEmptyMessage(ERRORLOADING);
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                mHandler.sendEmptyMessage(ERRORLOADING);
            }
        }, NumbersOpenBean.class);
    }

    /**
     * 进入倒计时
     */
    private void initCountDown() {

        getStartCountDown();
    }

    /**
     * 开始倒计时
     */
    private void getStartCountDown() {

        try {
            if (mNumberInfo.getNextTime() != null) {
                mNumberTime = DateUtil.getCurrentTime(mNumberInfo.getNextTime()) - Long.parseLong(serverTime);// 获取下一期开奖时间
                if (!isCountDown && isExit) {
                    new Thread() {
                        public void run() {
                            try {
                                isCountDown = true;
                                while (!isOpenNumberStartHistory && mNumberTime >= 0 && isExit) {
                                    sleep(1000);
                                    mNumberTime -= 1000;
                                }
                                isCountDown = false;

                                if (isExit) {
                                    initData();
                                    numbersDataShow(2);// 开奖时重新获取开奖状态
                                }

                            } catch (InterruptedException e) {
                                L.d("倒计时子线程休眠异常！" + e.getMessage());
                            }
                        }

                    }.start();
                }
            }
        } catch (Exception e) {
            L.d("时间日期转换异常！" + e.getMessage());
        }
    }

    private boolean isNumberStart = false;

    /**
     * 开奖了，循环请求数据
     */
    private void getOpenNumberStart() {
        if (!isNumberStart && isExit) {
            new Thread() {
                public void run() {
                    try {
                        isNumberStart = true;
                        while (isOpenNumberStartHistory && isExit) {
                            sleep(1000);
                            numbersDataShow(2);// 重新获取开奖状态
                        }
                        isNumberStart = false;

                        if (isExit) {
                            numbersDataShow(1);// 重新获取开奖状态
                        }

                    } catch (Exception e) {
                    }
                }

            }.start();
        }
    }

    private float dX;// 按下下标
    private float dY;
    private float mX;// 移动下标
    private float mY;
    private float upX;// 松开的下标

    private void initEvent() {
        // 重新加载数据
        reLoading.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                numbersDataShow(0);
            }
        });
        // listViewItem事件
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                MobclickAgent.onEvent(mContext, "Lottery_History_List");
                fl_number_history_list.setVisibility(View.GONE);
                fl_numbner_history_info.setVisibility(View.VISIBLE);
                viewPagerIndex = arg2;

                if (arg2 == 0) {
                    right.setAlpha(0f);
                    iv_lottery_right.setAlpha(0f);
                    if (!isOpenNumberStartHistory) {
                        number_new_icon.setVisibility(View.VISIBLE);
                    } else {
                        number_new_icon.setVisibility(View.GONE);
                    }
                } else {
                    right.setAlpha(1.0f);
                    iv_lottery_right.setAlpha(1.0f);
                    number_new_icon.setVisibility(View.GONE);
                }

                if (arg2 == count - 1) {
                    left.setAlpha(0f);
                    iv_lottery_left.setAlpha(0f);
                } else {
                    left.setAlpha(1.0f);
                    iv_lottery_left.setAlpha(1.0f);
                }
                ((NumbersInfoBaseActivity) mContext).isHistoryPager = true;
                numberHistorySelect(arg2);// 显示指定期数号码开奖明细

            }
        });

        /**
         * 往期详情页面滑动事件
         */
        mScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        dX = event.getX();
                        dY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        mX = event.getX();
                        mY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        upX = event.getX();

                        // 判断是横向还是纵向滑动
                        if (Math.abs((mY - dY)) < Math.abs((mX - dX))) {// 横向滑动

                            if (Math.abs((upX - dX)) > 100) {// 判断移动的有效距离

                                if ((upX - dX) > 0) {
                                    // 往右滑,下一期
                                    if (!isAnimationStart) {
                                        getNext();
                                    }
                                } else {
                                    // 往左滑,上一期
                                    if (!isAnimationStart) {
                                        if (viewPagerIndex > 0) {
                                            leftAnimation();
                                        }
                                    }
                                }

                                return true;
                            } else {

                                return false;
                            }
                        } else {// 纵向滑动

                            return false;
                        }
                }
                return false;
            }
        });
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case STARTLOADING:// 开始加载
                    loading.setVisibility(View.VISIBLE);
                    errorLoading.setVisibility(View.GONE);
                    mListView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(true);
                    break;
                case SUCCESS:// 加载成功
                    loading.setVisibility(View.GONE);
                    errorLoading.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    isExit = true;
                    break;
                case ERRORLOADING:// 加载失败
                    loading.setVisibility(View.GONE);
                    errorLoading.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    isExit = false;
                    break;

                case REFRESH:// 第一次加载数据，需显示加载进度条
                    isExit = true;
                    mSwipeRefreshLayout.setRefreshing(false);
                    getCurrentBean();// 获取当前要显示的对象
                    initIsOpenNumberStart();// 获取开奖状态
                    initCountDown();// 倒计时
                    initData();
                    break;

                case ANEWREFRESH:// 1.下拉刷新，不显示加载进度条
                    mSwipeRefreshLayout.setRefreshing(false);
                    getCurrentBean();// 获取当前要显示的对象
                    initIsOpenNumberStart();// 获取开奖状态
                    initCountDown();// 倒计时
                    initData();
                    break;
                case OPENNUMBER:// 2.正在开奖中
                    getCurrentBean();// 获取当前要显示的对象
                    initIsOpenNumberStart();// 获取开奖状态
                    getOpenNumberStart();
                    break;
            }
        }

    };

    public void initData() {

        loadingNetData();// 访问网络数据

    }

    /**
     * 访问网络数据
     */
    private void loadingNetData() {
        if (null != mNumberInfo) {
            VolleyContentFast.requestJsonByGet(AppConstants.numberHistoryURLs[Integer.parseInt(mNumberInfo.getName())], new VolleyContentFast.ResponseSuccessListener<NumbersHistoryBean>() {
                @Override
                public synchronized void onResponse(final NumbersHistoryBean json) {
                    if (mNumberHistoryList != null) {
                        mNumberHistoryList.clear();
                    }
                    mNumberHistoryList = json.getHistoryLotteryResults();

                    count = mNumberHistoryList.size();

                    // 发送加载数据成功消息
                    mHandler.sendEmptyMessage(SUCCESS);

                    mAdapter = new numbersListViewAdapter();
                    mListView.setAdapter(mAdapter);
                }
            }, new VolleyContentFast.ResponseErrorListener() {
                @Override
                public void onErrorResponse(VolleyContentFast.VolleyException exception) {
                    mHandler.sendEmptyMessage(ERRORLOADING);
                }
            }, NumbersHistoryBean.class);
        }
    }

    // ListView数据适配
    private class numbersListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return count;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.numbers_history_item_info, null);

                holder = new ViewHolder();
                holder.tv_issue = (TextView) convertView.findViewById(R.id.tv_numbers_history_issue);
                holder.icon_new = (ImageView) convertView.findViewById(R.id.number_list_new_icon);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_numbers_history_time);
                holder.ll_container = (LinearLayout) convertView.findViewById(R.id.ll_numbers_history_container);

                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();

            if (position == 0) {// 判断new 字样图片的显示与隐藏
                if (isOpenNumberStartHistory) {
                    holder.icon_new.setVisibility(View.GONE);
                } else {
                    holder.icon_new.setVisibility(View.VISIBLE);
                }
            } else {
                holder.icon_new.setVisibility(View.GONE);
            }

            numbers.clear();
            zodiacs.clear();

            // 拆分号码
            numberHistorySub(holder, position, numbers, zodiacs);

            holder.ll_container.removeAllViews();// 清除所有子控件

            numberHistoryAddData(holder, numbers, zodiacs);// 设置显示开奖号码内容

            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

    }

    // ViewHolder
    private class ViewHolder {
        TextView tv_issue;
        ImageView icon_new;
        TextView tv_time;
        LinearLayout ll_container;
    }

    private void initView() {
        number_new_icon = (ImageView) view.findViewById(R.id.number_new_icon);
        mListView = (ListView) view.findViewById(R.id.lv_history_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.number_history_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.bg_header);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, DisplayUtil.dip2px(getContext(), StaticValues.REFRASH_OFFSET_END));
        loading = (LinearLayout) view.findViewById(R.id.ll_history_startLoading);
        errorLoading = (LinearLayout) view.findViewById(R.id.ll_history_errorLoading);
        mScrollView = (ScrollView) view.findViewById(R.id.mscrollview);

        // 加载动画
        /*iv_loading_img = (ImageView) view.findViewById(R.id.iv_loading_img);
        animation = AnimationUtils.loadAnimation(mContext, R.anim.cirle);
		animation.setInterpolator(new LinearInterpolator());
		iv_loading_img.startAnimation(animation);*/

        reLoading = (TextView) view.findViewById(R.id.reLoading);
        fl_number_history_list = (FrameLayout) view.findViewById(R.id.fl_number_history_list);
        fl_numbner_history_info = (FrameLayout) view.findViewById(R.id.fl_numbner_history_info);

        fl_number_history_list.setVisibility(View.VISIBLE);
        fl_numbner_history_info.setVisibility(View.GONE);

        left = (ImageView) view.findViewById(R.id.iv_numbers_left);
        right = (ImageView) view.findViewById(R.id.iv_numbers_right);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(this);
        right.setVisibility(View.VISIBLE);
        right.setOnClickListener(this);
        iv_lottery_left = (ImageView) view.findViewById(R.id.iv_lottery_left);
        iv_lottery_right = (ImageView) view.findViewById(R.id.iv_lottery_right);
        iv_lottery_left.setOnClickListener(this);
        iv_lottery_right.setOnClickListener(this);

        ll_scan_animation = (LinearLayout) view.findViewById(R.id.ll_number_animation);// 动画布局
        iv_number_copy = (ImageView) view.findViewById(R.id.iv_number_copy);// 当前布局的图片容器

    }

    /**
     * 往期开奖号码拆分
     *
     * @param holder
     * @param position
     * @param numbers
     * @param zodiacs
     */
    private void numberHistorySub(ViewHolder holder, int position, List<String> numbers, List<String> zodiacs) {
        if (mNumberInfo != null) {
            // 六合彩
            if ("1".equals(mNumberInfo.getName())) {

                NumberCurrentInfo hkInfo = mNumberHistoryList.get(position);// 获取当前彩票对象
                holder.tv_issue.setText(mContext.getResources().getString(R.string.number_code_di) + hkInfo.getIssue() + mContext.getResources().getString(R.string.number_code_qi));

                String weekDate = DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(hkInfo.getTime()));// 根据日期获取星期
                String[] Dates = {null, null};
                if (hkInfo.getTime() != null) {
                    Dates = hkInfo.getTime().split(" ");
                }


                holder.tv_time.setText(Dates[0] + " " + weekDate);// 设置日期

                // 拆分开奖号码
                if (!TextUtils.isEmpty(hkInfo.getNumbers())) {

                    // 46,15,38,39,7,11#25
                    String[] nums1 = hkInfo.getNumbers().split(",");

                    int len1 = 0;
                    if (hkInfo.getNumbers().contains("#")) {
                        len1 = nums1.length - 1;
                    } else {
                        len1 = nums1.length;
                    }

                    // 将号码添加到集合中
                    for (int i = 0; i < len1; i++) {
                        numbers.add(nums1[i]);
                    }

                    if (hkInfo.getNumbers().contains("#")) {
                        String[] nums2 = nums1[len1].split("#");
                        if (nums2.length > 1) {
                            String[] sxh = {nums2[0], "88", nums2[1]};// 添加"+"号标记

                            for (String string : sxh) {
                                numbers.add(string);
                            }

                        } else {
                            for (String string : nums2) {
                                numbers.add(string);
                            }
                        }
                    }
                }

                // 六合彩拆分生肖
                if (!TextUtils.isEmpty(hkInfo.getZodiac())) {
                    // 狗,蛇,马,蛇,牛,鸡#羊
                    String[] nums3 = hkInfo.getZodiac().split(",");

                    int len2 = 0;
                    if (hkInfo.getNumbers().contains("#")) {
                        len2 = nums3.length - 1;
                    } else {
                        len2 = nums3.length;
                    }

                    for (int i = 0; i < len2; i++) {
                        zodiacs.add(nums3[i]);
                    }

                    if (hkInfo.getZodiac().contains("#")) {
                        String[] nums4 = nums3[len2].split("#");

                        if (nums4.length > 1) {
                            String[] sx = {nums4[0], "加", nums4[1]};// 添加"+"号标记

                            for (String string : sx) {
                                zodiacs.add(string);
                            }
                        } else {
                            for (String string : nums4) {
                                zodiacs.add(string);
                            }
                        }
                    }
                }

            } else {
                NumberCurrentInfo historyInfo = mNumberHistoryList.get(position);// 获取当前彩票对象
                holder.tv_issue.setText(mContext.getResources().getString(R.string.number_code_di) + historyInfo.getIssue() + mContext.getResources().getString(R.string.number_code_qi));

                String weekDate = DateUtil.getLotteryWeekOfDate(DateUtil.parseDate(historyInfo.getTime()));// 根据日期获取星期
                String[] Dates = {null, null};
                if (historyInfo.getTime() != null) {
                    Dates = historyInfo.getTime().split(" ");
                }

                if ("6".equals(mNumberInfo.getName()) || "24".equals(mNumberInfo.getName()) || "28".equals(mNumberInfo.getName()) ||
                        "29".equals(mNumberInfo.getName()) || "25".equals(mNumberInfo.getName()) || "26".equals(mNumberInfo.getName())) {

                    holder.tv_time.setText(Dates[0] + " " + weekDate);// 设置日期
                } else {
                    if (Dates[1] != null) {
                        String mTime = Dates[1].substring(0, Dates[1].lastIndexOf(":"));
                        holder.tv_time.setText(mTime);// 设置日期
                    } else {
                        holder.tv_time.setText("");
                    }
                }

                String num;
                if (historyInfo.getNumbers().contains("#")) {
                    num = historyInfo.getNumbers().replace('#', ',');
                } else {
                    num = historyInfo.getNumbers();
                }

                String[] nums1 = num.split(",");

                // 将号码添加到集合中
                for (int i = 0; i < nums1.length; i++) {
                    if ("24".equals(mNumberInfo.getName()) && i == 6) {
                        numbers.add("88");
                    }
                    if ("29".equals(mNumberInfo.getName()) && i == 5) {
                        numbers.add("88");
                    }
                    numbers.add(nums1[i]);
                }
            }
        }
    }

    /**
     * 动态添加数据
     *
     * @param holder
     * @param numbers
     * @param zodiacs
     */

    private void numberHistoryAddData(ViewHolder holder, List<String> numbers, List<String> zodiacs) {
        LinearLayout.LayoutParams params;
        params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        holder.ll_container.setLayoutParams(params);

        try {
            int numNo = Integer.parseInt(mNumberInfo.getName());// 获取当前彩种

            for (int i = 0; i < numbers.size(); i++) {

                LinearLayout ll = new LinearLayout(mContext);

                params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);// ,
                // 1
                params.setMargins(DisplayUtil.dip2px(mContext, 10), 0, 0, 0);
                // }
                ll.setLayoutParams(params);
                ll.setOrientation(LinearLayout.VERTICAL);// 设置垂直布局

                int dip = DisplayUtil.dip2px(mContext, 26);
                params = new LinearLayout.LayoutParams(dip, dip);

                switch (numNo) {
                    case 15:// 北京赛车
                    {
                        int num = Integer.parseInt(numbers.get(i));
                        ImageView iv_car = new ImageView(mContext);
                        params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(mContext, 20), DisplayUtil.dip2px(mContext, 24));
                        iv_car.setLayoutParams(params);
                        iv_car.setBackgroundResource(AppConstants.numberCarNos[num - 1]);

                        params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        if (i == 0) {

                            params.setMargins(DisplayUtil.dip2px(mContext, 10), 0, 0, 0);
                        } else {
                            params.setMargins(DisplayUtil.dip2px(mContext, 5), 0, 0, 0);

                        }

                        ll.setLayoutParams(params);
                        ll.addView(iv_car);
                    }
                    break;
                    case 19:// 幸运农场
                    {
                        int num = Integer.parseInt(numbers.get(i));
                        ImageView iv_lc = new ImageView(mContext);
                        iv_lc.setLayoutParams(params);
                        iv_lc.setBackgroundResource(AppConstants.numberXYLCs[num - 1]);

                        params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        if (i == 0) {

                            params.setMargins(DisplayUtil.dip2px(mContext, 10), 0, 0, 0);
                        } else {
                            params.setMargins(DisplayUtil.dip2px(mContext, 8), 0, 0, 0);

                        }
                        ll.setLayoutParams(params);
                        ll.addView(iv_lc);
                    }
                    break;
                    case 1:// 六合彩
                    {

                        if (i == 6) {
                            params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                            params.gravity = Gravity.CENTER;

                            ImageView iv = new ImageView(mContext);
                            iv.setLayoutParams(params);
                            iv.setImageResource(R.mipmap.number_tiema_icon);
                            iv.setPadding(0, DisplayUtil.dip2px(mContext, 8), 0, 0);

                            // 加号去掉左边距，否则小屏手机显示不下
                            params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                            params.setMargins(0, 0, 0, 0);
                            ll.setLayoutParams(params);

                            TextView tv = new TextView(mContext);
                            tv.setText("");
                            tv.setGravity(Gravity.CENTER);

                            ll.addView(iv);
                            ll.addView(tv);
                        } else {

                            String string = numbers.get(i);
                            if (string.length() == 1) {
                                string = "0" + string;
                            }

                            // 添加号码
                            TextView tv_number = new TextView(mContext);
                            // tv_number.setLayoutParams(params);
                            tv_number.setGravity(Gravity.CENTER);
                            tv_number.setText(string);
                            tv_number.setTextColor(getResources().getColor(R.color.numberinfo_text_color));

                            // 设置背景圆球的大小和外边距
                            params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(mContext, 26), DisplayUtil.dip2px(mContext, 26));
                            params.setMargins(DisplayUtil.dip2px(mContext, 6), 0, DisplayUtil.dip2px(mContext, 6), 0);
                            tv_number.setLayoutParams(params);

                            String zod = "?";
                            if (0 != zodiacs.size()) {
                                if (zodiacs.size() > i) {
                                    char[] array = zodiacs.get(i).toCharArray();
                                    switch (array[0]) {
                                        // 牛、马、羊、鸡、狗、猪
                                        // 鼠、虎、兔、龙、蛇、猴
                                        case '牛':
                                            zod = mContext.getResources().getString(R.string.number_bjsc_nu);
                                            break;
                                        case '马':
                                            zod = mContext.getResources().getString(R.string.number_bjsc_ma);
                                            break;
                                        case '羊':
                                            zod = mContext.getResources().getString(R.string.number_bjsc_ya);
                                            break;
                                        case '鸡':
                                            zod = mContext.getResources().getString(R.string.number_bjsc_ji);
                                            break;
                                        case '狗':
                                            zod = mContext.getResources().getString(R.string.number_bjsc_gou);
                                            break;
                                        case '猪':
                                            zod = mContext.getResources().getString(R.string.number_bjsc_zhu);
                                            break;
                                        case '鼠':
                                            zod = mContext.getResources().getString(R.string.number_bjsc_su);
                                            break;
                                        case '虎':
                                            zod = mContext.getResources().getString(R.string.number_bjsc_hu);
                                            break;
                                        case '兔':
                                            zod = mContext.getResources().getString(R.string.number_bjsc_tu);
                                            break;
                                        case '龙':
                                            zod = mContext.getResources().getString(R.string.number_bjsc_long);
                                            break;
                                        case '蛇':
                                            zod = mContext.getResources().getString(R.string.number_bjsc_se);
                                            break;
                                        case '猴':
                                            zod = mContext.getResources().getString(R.string.number_bjsc_hou);
                                            break;
                                    }
                                }
                            }

                            // 添加生肖
                            TextView tv_zodiac = new TextView(mContext);
                            tv_zodiac.setText(zod);
                            tv_zodiac.setGravity(Gravity.CENTER);

                            // 若生肖不是中文则将字体调整小，否则显示不全
                            String zodIndex = "牛马羊鸡狗猪鼠虎兔龙蛇猴龍馬雞豬猴";
                            if (zodIndex.indexOf(zod) != -1) {

                                tv_zodiac.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                            } else {

                                tv_zodiac.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                            }
                            params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                            params.setMargins(0, 0, 0, 0);
                            ll.setLayoutParams(params);

                            if (i == 0) {
                                ll.setPadding(DisplayUtil.dip2px(mContext, 4), 0, 0, 0);
                            } else {
                                ll.setPadding(0, 0, 0, 0);
                            }

                            switch (Integer.parseInt(numbers.get(i))) {
                                case 1:
                                case 2:
                                case 7:
                                case 8:
                                case 12:
                                case 13:
                                case 18:
                                case 19:
                                case 23:
                                case 24:
                                case 29:
                                case 30:
                                case 34:
                                case 35:
                                case 40:
                                case 45:
                                case 46:
                                    tv_number.setBackgroundResource(R.mipmap.number_bg_red);
                                    break;
                                case 3:
                                case 4:
                                case 9:
                                case 10:
                                case 14:
                                case 15:
                                case 20:
                                case 25:
                                case 26:
                                case 31:
                                case 36:
                                case 37:
                                case 41:
                                case 42:
                                case 47:
                                case 48:
                                    tv_number.setBackgroundResource(R.mipmap.number_bg_blue);
                                    break;
                                case 5:
                                case 6:
                                case 11:
                                case 16:
                                case 17:
                                case 21:
                                case 22:
                                case 27:
                                case 28:
                                case 32:
                                case 33:
                                case 38:
                                case 39:
                                case 43:
                                case 44:
                                case 49:
                                    tv_number.setBackgroundResource(R.mipmap.number_bg_green);
                                    break;
                            }

                            ll.addView(tv_number);
                            ll.addView(tv_zodiac);
                        }
                    }
                    break;
                    case 6:// 七星彩
                    {
                        TextView tv_number = new TextView(mContext);
                        tv_number.setLayoutParams(params);
                        tv_number.setGravity(Gravity.CENTER);
                        tv_number.setText(numbers.get(i));
                        tv_number.setTextColor(getResources().getColor(R.color.numberinfo_text_color));
                        if (i >= 4) {
                            tv_number.setBackgroundResource(R.mipmap.number_bg_blue);
                        } else {
                            tv_number.setBackgroundResource(R.mipmap.number_bg_red);
                        }
                        ll.addView(tv_number);
                    }
                    break;
                    case 24:// 双色球
                    {
                        if (i == 6) {
                            params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                            params.gravity = Gravity.CENTER;
                            ImageView iv = new ImageView(mContext);
                            iv.setLayoutParams(params);
                            iv.setImageResource(R.mipmap.number_tiema_icon);
                            iv.setPadding(0, DisplayUtil.dip2px(mContext, 8), 0, 0);
                            ll.addView(iv);
                        } else {
                            TextView tv_number = new TextView(mContext);
                            tv_number.setLayoutParams(params);
                            tv_number.setGravity(Gravity.CENTER);
                            tv_number.setText(numbers.get(i));
                            tv_number.setTextColor(getResources().getColor(R.color.numberinfo_text_color));
                            if (i > 5) {
                                tv_number.setBackgroundResource(R.mipmap.number_bg_blue);
                            } else {
                                tv_number.setBackgroundResource(R.mipmap.number_bg_red);
                            }
                            ll.addView(tv_number);
                        }
                    }
                    break;
                    case 28:// 七乐彩
                    {
                        TextView tv_number = new TextView(mContext);
                        tv_number.setLayoutParams(params);
                        tv_number.setGravity(Gravity.CENTER);
                        tv_number.setText(numbers.get(i));
                        tv_number.setTextColor(getResources().getColor(R.color.numberinfo_text_color));
                        if (i > 6) {
                            tv_number.setBackgroundResource(R.mipmap.number_bg_blue);
                        } else {
                            tv_number.setBackgroundResource(R.mipmap.number_bg_red);
                        }
                        ll.addView(tv_number);
                    }
                    break;
                    case 29:// 大乐透
                    {
                        if (i == 5) {
                            params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                            params.gravity = Gravity.CENTER;
                            ImageView iv = new ImageView(mContext);
                            iv.setLayoutParams(params);
                            iv.setImageResource(R.mipmap.number_tiema_icon);
                            iv.setPadding(0, DisplayUtil.dip2px(mContext, 8), 0, 0);
                            ll.addView(iv);
                        } else {
                            TextView tv_number = new TextView(mContext);
                            tv_number.setLayoutParams(params);
                            tv_number.setGravity(Gravity.CENTER);
                            tv_number.setText(numbers.get(i));
                            tv_number.setTextColor(getResources().getColor(R.color.numberinfo_text_color));
                            if (i > 4) {
                                tv_number.setBackgroundResource(R.mipmap.number_bg_blue);
                            } else {
                                tv_number.setBackgroundResource(R.mipmap.number_bg_red);
                            }
                            ll.addView(tv_number);
                        }
                    }
                    break;
                    case 30:
                    case 31:
                    case 32: {
                        TextView tv_number = new TextView(mContext);
                        params = new LinearLayout.LayoutParams(DisplayUtil.dip2px(mContext, 17), DisplayUtil.dip2px(mContext, 24));
                        tv_number.setLayoutParams(params);
                        tv_number.setGravity(Gravity.CENTER);
                        tv_number.setText(numbers.get(i).equals("null") ? "..." : numbers.get(i));
                        tv_number.setTextColor(mContext.getResources().getColor(R.color.numberinfo_text_color));
                        tv_number.setBackgroundResource(R.mipmap.number_bg_jc);
                        ll.addView(tv_number);

                        params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        if (i == 0) {
                            params.setMargins(DisplayUtil.dip2px(mContext, 10), 0, 0, 0);
                        } else {
                            params.setMargins(DisplayUtil.dip2px(mContext, 3), 0, 0, 0);
                        }
                        ll.setLayoutParams(params);
                    }
                    break;
                    case 8:// 快乐十分
                    case 11: {
                        TextView tv_number = new TextView(mContext);
                        tv_number.setLayoutParams(params);
                        tv_number.setGravity(Gravity.CENTER);
                        tv_number.setText(numbers.get(i));
                        tv_number.setTextColor(mContext.getResources().getColor(R.color.numberinfo_text_color));
                        if (Integer.parseInt(numbers.get(i)) == 19 || Integer.parseInt(numbers.get(i)) == 20) {
                            tv_number.setBackgroundResource(R.mipmap.number_bg_red);
                        } else {
                            tv_number.setBackgroundResource(R.mipmap.number_bg_blue);
                        }

                        params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        if (i == 0) {

                            params.setMargins(DisplayUtil.dip2px(mContext, 10), 0, 0, 0);
                        } else {
                            params.setMargins(DisplayUtil.dip2px(mContext, 8), 0, 0, 0);

                        }
                        ll.setLayoutParams(params);
                        ll.addView(tv_number);
                    }
                    break;
                    case 10:// 快三
                    case 16:
                    case 18: {
                        int num = Integer.parseInt(numbers.get(i));
                        ImageView iv_car = new ImageView(mContext);
                        iv_car.setLayoutParams(params);
                        iv_car.setBackgroundResource(AppConstants.numberKSNos[num - 1]);
                        ll.addView(iv_car);
                    }
                    break;

                    default:// 其它号码
                    {
                        TextView tv_number = new TextView(mContext);
                        tv_number.setLayoutParams(params);
                        tv_number.setGravity(Gravity.CENTER);
                        tv_number.setText(numbers.get(i));
                        tv_number.setTextColor(mContext.getResources().getColor(R.color.numberinfo_text_color));
                        tv_number.setBackgroundResource(R.mipmap.number_bg_red);
                        ll.addView(tv_number);
                    }
                    break;
                }

                holder.ll_container.addView(ll);

            }
        } catch (Exception e) {
            L.d("动态添加数据异常:" + e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_numbers_right:// 上一期
            case R.id.iv_lottery_right:// 上一期

                if (!isAnimationStart) {
                    if (viewPagerIndex > 0) {
                        leftAnimation();
                    }
                }
                break;
            case R.id.iv_numbers_left:// 下一期
            case R.id.iv_lottery_left:// 下一期
                if (!isAnimationStart) {
                    getNext();
                }
                break;
        }
    }

    /**
     * 下一期
     */
    private void getNext() {

        getObtianDraw1();

        if (viewPagerIndex >= count - 1) {
            numberHistorySelect(count - 1);
            left.setAlpha(0f);
            iv_lottery_left.setAlpha(0f);
        } else {

            rightAnimation();
            numberHistorySelect(++viewPagerIndex);
            if (viewPagerIndex == count - 1) {
                left.setAlpha(0f);
                iv_lottery_left.setAlpha(0f);
            } else {
                left.setAlpha(1.0f);
                iv_lottery_left.setAlpha(1.0f);
            }
        }
        if (viewPagerIndex <= 0) {
            right.setAlpha(0f);
            iv_lottery_right.setAlpha(0f);
            if (!isOpenNumberStartHistory) {
                number_new_icon.setVisibility(View.VISIBLE);
            } else {
                number_new_icon.setVisibility(View.GONE);
            }
        } else {
            right.setAlpha(1.0f);
            iv_lottery_right.setAlpha(1.0f);
            number_new_icon.setVisibility(View.GONE);
        }
    }

    /**
     * 上一期
     */
    private void getPrevious() {

        if (viewPagerIndex <= 0) {
            numberHistorySelect(0);
            right.setAlpha(0f);
            iv_lottery_right.setAlpha(0f);
            if (!isOpenNumberStartHistory) {
                number_new_icon.setVisibility(View.VISIBLE);
            } else {
                number_new_icon.setVisibility(View.GONE);
            }
        } else {
            numberHistorySelect(--viewPagerIndex);
            if (viewPagerIndex == 0) {
                right.setAlpha(0f);
                iv_lottery_right.setAlpha(0f);
                if (!isOpenNumberStartHistory) {
                    number_new_icon.setVisibility(View.VISIBLE);
                } else {
                    number_new_icon.setVisibility(View.GONE);
                }
            } else {
                right.setAlpha(1.0f);
                iv_lottery_right.setAlpha(1.0f);
                number_new_icon.setVisibility(View.GONE);
            }
        }
        if (viewPagerIndex >= count) {
            left.setAlpha(0f);
            iv_lottery_left.setAlpha(0f);
        } else {
            left.setAlpha(1.0f);
            iv_lottery_left.setAlpha(1.0f);
        }
    }

    /**
     * 显示指定期数号码开奖明细
     *
     * @param index
     */
    private void numberHistorySelect(int index) {
        if (mNumberInfo != null) {
//            NumberCurrentInfo mNumber = new NumberCurrentInfo();
//            mNumber.setName(mNumberInfo.getName());
//
//            if ("1".equals(mNumberInfo.getName())) {
//                NumberHistoryHKInfo numberHistoryHKInfo = mNumberHKList.get(index);
//                mNumber.setIssue(numberHistoryHKInfo.getIssue());
//                mNumber.setNumbers(numberHistoryHKInfo.getNumbers());
//                mNumber.setTime(numberHistoryHKInfo.getTime());
//                mNumber.setZodiac(numberHistoryHKInfo.getZodiac());
//            } else {
//
//                NumberHistoryInfo numberHistoryInfo = mNumberHistoryList.get(index);
//                mNumber.setIssue(numberHistoryInfo.getIssue());
//                mNumber.setNumbers(numberHistoryInfo.getNumbers());
//                mNumber.setTime(numberHistoryInfo.getTime());
//            }
            NumberCurrentInfo currentInfo = mNumberHistoryList.get(index);
            currentInfo.setName(mNumberName);
            NumberDataUtils utils = new NumberDataUtils();
            utils.numberHistoryShow(mContext, view, currentInfo, 2, false, false, null, serverTime);
        }
    }

    // 显示list列表信息
    public void setValue(boolean isRn) {
        if (isRn) {

            fl_number_history_list.setVisibility(View.VISIBLE);
            fl_numbner_history_info.setVisibility(View.GONE);

//            numbersDataShow(1);
        }
    }

    /**
     * 获取当前显示界面
     */
    private void getObtianDraw1() {
        iv_number_copy.clearAnimation();

        ll_scan_animation.setVisibility(View.VISIBLE);// 1.显示用户选择的开奖详情页面,隐藏动画布局
        fl_numbner_history_info.setDrawingCacheEnabled(true);// 1.
        // 获取图片（不设置getDrawingCache()结果为null）
        fl_numbner_history_info.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);// 设置图片的质量
        Bitmap drawingCache = fl_numbner_history_info.getDrawingCache();// 把view转成bitmap

        Bitmap all = getAllBitmap(drawingCache);

        iv_number_copy.setImageBitmap(all);
    }

    /**
     * 绘制整个当前画面
     *
     * @param drawingCache
     * @return
     */
    Bitmap pager;

    private Bitmap getAllBitmap(Bitmap drawingCache) {

        int width = drawingCache.getWidth();
        int height = drawingCache.getHeight();

        if (pager != null) {
            pager.recycle();
            pager = null;
        }
        pager = Bitmap.createBitmap(width, height, drawingCache.getConfig());

        Canvas canvas = new Canvas(pager);
        canvas.drawBitmap(drawingCache, new Matrix(), new Paint());

        return pager;
    }

    /**
     * 页面整体右滑动画
     */
    private void rightAnimation() {

        AnimatorSet rightSet = new AnimatorSet();
        iv_number_copy.measure(0, 0);// 随意去测量宽度
        ObjectAnimator transleft = ObjectAnimator.ofFloat(iv_number_copy, "translationX", 0, iv_number_copy.getMeasuredWidth());
        rightSet.playTogether(transleft);

        // 合并一起播放
        AnimatorSet as = new AnimatorSet();
        as.playTogether(rightSet);
        as.setDuration(500);
        as.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                isAnimationStart = true;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimationStart = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        if (!isAnimationStart) {
            as.start();
        }
    }

    /**
     * 页面整体左滑动画
     */
    private void leftAnimation() {
        getObtianDraw1();
        getPrevious();
        AnimatorSet leftSet = new AnimatorSet();
        iv_number_copy.measure(0, 0);
        ObjectAnimator transright = ObjectAnimator.ofFloat(iv_number_copy, "translationX", 0, -iv_number_copy.getMeasuredWidth());
        leftSet.playTogether(transright);

        // 合并一起播放
        AnimatorSet as = new AnimatorSet();
        as.playTogether(leftSet);
        as.setDuration(500);
        as.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                isAnimationStart = true;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                getPrevious();
                isAnimationStart = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        if (!isAnimationStart) {
            as.start();
        }
    }

    @Override
    public void onPause() {

        isExit = false;
        super.onPause();
        MobclickAgent.onPageEnd("HistoryNumberFragment");
    }

    @Override
    public void onResume() {

        isExit = true;
        super.onResume();
        MobclickAgent.onPageStart("HistoryNumberFragment");
    }

    @Override
    public void onRefresh() {
        numbersDataShow(1);
    }
}
