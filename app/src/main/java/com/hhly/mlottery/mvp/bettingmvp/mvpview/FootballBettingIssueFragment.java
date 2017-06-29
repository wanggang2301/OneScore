package com.hhly.mlottery.mvp.bettingmvp.mvpview;

import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.LoginActivity;
import com.hhly.mlottery.activity.RecommendedExpertDetailsActivity;
import com.hhly.mlottery.adapter.bettingadapter.BettingRecommendDetailsMvpAdapter;
import com.hhly.mlottery.bean.bettingbean.BettingListDataBean;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.config.BlurPopWin;
import com.hhly.mlottery.config.ConstantPool;
import com.hhly.mlottery.mvp.bettingmvp.MView;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.BettingDetailsResuleEventBusEntity;
import com.hhly.mlottery.mvp.bettingmvp.eventbusconfig.IssueSuccessResulyEventBus;
import com.hhly.mlottery.mvp.bettingmvp.mvppresenter.MvpFootballBettingIssuePresenter;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.DeviceInfo;
import com.hhly.mlottery.util.L;
import com.hhly.mlottery.util.net.SignUtils;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by：XQyi on 2017/5/11 11:12
 * Use:内页推介页面
 */
public class FootballBettingIssueFragment extends Fragment implements MView<BettingListDataBean>, View.OnClickListener {

    private Context mContext;
    /** 签名参数 */
    private String PARAM_PAGE_SIZE = "pageSize";//每页条数
//    private String PARAM_PAGE_NO = "pageNo";//页码
    private String PARAM_PAGE_NUM = "pageNum";//页码
    private String PARAM_USER_ID = "userId";//用户id
    private String PARAM_KEY = "key";//联赛key
    private String PARAM_TYPE = "type";//玩法type
    private String PARAM_LANG = "lang";
    private String PARAM_TIMEZONE = "timeZone";
    private String PARAM_SIGN = "sign";//参数签名
    private String PARAM_MATCH_ID = "matchId";//比赛id matchId

    private static final String THIRDID = "thirdId";
    private String mThirdid;
    private View mView;
    private BettingRecommendDetailsMvpAdapter mAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ImageView mTextIssue;
    //    private ImageView mYuyin;
    //    private ImageView mFabuOrQxiao;
    private boolean isRecording = false;

    private View mOnloadingView;
    private View mNoLoadingView;

    private int pageNum = 1;//页码 (初次请求第一页)
    private int pageSize = 10;//每页条数（分页加载每页条数）
    private String playType = "-1";//选中的玩法（-1 全部）
    private String leagueKeys = "";//选中联赛的key -string

    private boolean hasNextPage;//是否有下一页
    private List<BettingListDataBean.PromotionData.BettingListData>  listData = new ArrayList<>();

    private boolean isFabu = true;//是否是要发布状态（默认true 点击发布）
    private MvpFootballBettingIssuePresenter mvpFootballBettingIssuePresenter;
    private LinearLayout mErrorLayout;
    private TextView mNoDataLayout;
    private LinearLayout mLoadingLayout;
    private TextView mRefreshTxt;
    /**是否可发布 ps: 0 不可发布  1 可发布*/
    private int hasPlayIssue = 0;

    public static FootballBettingIssueFragment newInstance(String thirdId) {
        FootballBettingIssueFragment fragment = new FootballBettingIssueFragment();
        Bundle args = new Bundle();
        args.putString(THIRDID, thirdId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mThirdid = getArguments().getString(THIRDID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mView = inflater.inflate(R.layout.betting_issue_fragment, container, false);
        mOnloadingView = inflater.inflate(R.layout.onloading, container,false);
        mNoLoadingView= inflater.inflate(R.layout.nomoredata, container,false);

        mvpFootballBettingIssuePresenter = new MvpFootballBettingIssuePresenter(this);

        EventBus.getDefault().register(this);
        initView();
        setStatus(SHOW_STATUS_LOADING);
        initData(pageNum , pageSize);
        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 设置显示状态
     *
     * @param status
     */
    //显示状态
    private static final int SHOW_STATUS_LOADING = 1;//加载中(点击刷新)
    private static final int SHOW_STATUS_ERROR = 2;//加载失败
    private static final int SHOW_STATUS_NO_DATA = 3;//暂无数据
    private static final int SHOW_STATUS_SUCCESS = 4;//加载成功
//    private final static int SHOW_STATUS_REFRESH_ONCLICK = 5;//点击刷新

    private void setStatus(int status) {

//        if (status == SHOW_STATUS_LOADING) {
//            recyclerView.setVisibility(View.VISIBLE);
//        } else if (status == SHOW_STATUS_SUCCESS) {
//            recyclerView.setVisibility(View.VISIBLE);
//        } else if (status == SHOW_STATUS_REFRESH_ONCLICK) {
//            recyclerView.setVisibility(View.GONE);
//        } else {
//            recyclerView.setVisibility(View.GONE);
//        }
        recyclerView.setVisibility(status == SHOW_STATUS_SUCCESS ? View.VISIBLE : View.GONE);

//        mLoadingLayout.setVisibility((status == SHOW_STATUS_REFRESH_ONCLICK) ? View.VISIBLE : View.GONE);
        mLoadingLayout.setVisibility((status == SHOW_STATUS_LOADING) ? View.VISIBLE : View.GONE);
        mErrorLayout.setVisibility(status == SHOW_STATUS_ERROR ? View.VISIBLE : View.GONE);
        mNoDataLayout.setVisibility(status == SHOW_STATUS_NO_DATA ? View.VISIBLE : View.GONE);
    }

    private void initView() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.betting_issue_view);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        mTextIssue = (ImageView) mView.findViewById(R.id.football_betting_text_img);
        mTextIssue.setOnClickListener(this);
        mTextIssue.setVisibility(View.GONE);
//        mYuyin = (ImageView) mView.findViewById(R.id.football_betting_yuyin_img);
//        mYuyin.setOnClickListener(this);

//        mFabuOrQxiao = (ImageView) mView.findViewById(R.id.football_betting_fabuorquxiao_img);
//        mFabuOrQxiao.setOnClickListener(this);

        //异常状态
        //网络不给力
        mErrorLayout = (LinearLayout) mView.findViewById(R.id.error_layout);
        //暂无数据
        mNoDataLayout = (TextView) mView.findViewById(R.id.nodata_txt);
        //加载中
        mLoadingLayout = (LinearLayout) mView.findViewById(R.id.custom_loading_ll);
        //刷新
        mRefreshTxt = (TextView) mView.findViewById(R.id.reloading_txt);
        mRefreshTxt.setOnClickListener(this);

    }

    public void initData(int pageNum , int pageSize) {

        //http://192.168.10.242:8098/promotion/info/matchPromotions
        // ?pageSize=50&pageNum=1&userId=hhly90944&lang=zh-TW&timezone=8&sign=ef82085fc999d1a7da78cbfa1292986f12&matchId=848856013
//        String url = "http://192.168.10.242:8098/promotion/info/matchPromotions";
        String url = BaseURLs.URI_BETTING_ISSUE_LIST;
//        String url = BaseURLs.URL_RECOMEND_LIST;

        String userid = AppConstants.register.getUser() == null ? "" : AppConstants.register.getUser().getUserId();
        Map<String ,String> mapPrament = new HashMap<>();

        mapPrament.put(PARAM_PAGE_SIZE , pageSize +"");
        mapPrament.put(PARAM_PAGE_NUM , pageNum + "");
        mapPrament.put(PARAM_USER_ID , userid);
//        mapPrament.put(PARAM_KEY , key);
//        mapPrament.put(PARAM_TYPE , type);
//        mapPrament.put(PARAM_MATCH_ID , "848856013");
        mapPrament.put(PARAM_MATCH_ID , mThirdid);
        mapPrament.put(PARAM_LANG , MyApp.getLanguage());
        mapPrament.put(PARAM_TIMEZONE , AppConstants.timeZone + "");
//        String signs = SignUtils.getSign("/promotion/info/matchPromotions", mapPrament);
        String signs = SignUtils.getSign(BaseURLs.PARAMENT_MATCH_RECOMMEND, mapPrament);

        Map<String ,String> map = new HashMap<>();
        map.put(PARAM_PAGE_SIZE , pageSize +""); //每页条数
        map.put(PARAM_PAGE_NUM , pageNum + "");//页码
        map.put(PARAM_USER_ID , userid);//用户id
//        map.put(PARAM_MATCH_ID , "848856013");
        map.put(PARAM_MATCH_ID , mThirdid);
//        map.put(PARAM_KEY , key);//联赛key
//        map.put(PARAM_TYPE , type);
        map.put(PARAM_SIGN , signs);

        L.d("qwer== >> " + signs);

        mvpFootballBettingIssuePresenter.loadData(url , map);
    }

    private void updataAdapter() {
        if (mAdapter == null) {
            return;
        }
        mAdapter.notifyDataSetChanged();
    }

    //刷新数据
    public void dataRefresh(){
        setStatus(SHOW_STATUS_LOADING);
        pageNum = 1;
        initData(pageNum , pageSize);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reloading_txt:
                setStatus(SHOW_STATUS_LOADING);
                pageNum = 1;
                initData(pageNum , pageSize);
                break;
//            case R.id.football_betting_yuyin_img:
////                Toast.makeText(getActivity(), "充值页", Toast.LENGTH_SHORT).show();
////                setPopupWindow();
////                startActivity(new Intent(mContext , MvpChargeMoneyActivity.class));
//
////                Intent intent = new Intent(mContext, TexasWebActivity.class);
////                intent.putExtra("key", "http://texas.1332255.com:4007/VideoGameWeb/mobile/index.html");
////                startActivity(intent);
//                break;
            case R.id.football_betting_text_img:
//                Toast.makeText(getActivity(), "发布文字", Toast.LENGTH_SHORT).show();

                    //是否登录
                    if (DeviceInfo.isLogin()) {
                        //是否是专家( 1为专家)
                        int expert = AppConstants.register.getUser() == null ? -1 : AppConstants.register.getUser().getIsExpert();
                        if (expert == 1) {
                            Intent mIntent = new Intent(mContext , MvpBettingIssueDetailsActivity.class);
                            mIntent.putExtra("matchId" , mThirdid);
                            startActivity(mIntent);
                            getActivity().overridePendingTransition(R.anim.push_left_in , R.anim.push_fix_out);
                        }else{
                            Toast.makeText(mContext, mContext.getResources().getText(R.string.issue_cueernt_isexpert), Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                    }

                break;
//            case R.id.football_betting_fabuorquxiao_img:
//                if (isFabu) {
//                    mYuyin.setVisibility(View.VISIBLE);
//                    mTextIssue.setVisibility(View.VISIBLE);
//                    mFabuOrQxiao.setBackgroundResource(R.mipmap.football_icon_quxiao);
//                    Toast.makeText(getActivity(), "发布", Toast.LENGTH_SHORT).show();
//                } else {
//                    mYuyin.setVisibility(View.GONE);
//                    mTextIssue.setVisibility(View.GONE);
//                    mFabuOrQxiao.setBackgroundResource(R.mipmap.football_icon_fabu);
//                    Toast.makeText(getActivity(), "取消", Toast.LENGTH_SHORT).show();
//                }
//                isFabu = !isFabu;
//                break;
        }
    }

    @Override
    public void loadSuccessView(BettingListDataBean bettingListDataBean) {

        //是否可发布（发布按钮是否可见)
        hasPlayIssue = bettingListDataBean.getHasPlay();
        if (hasPlayIssue == 1) {
            mTextIssue.setVisibility(View.VISIBLE);
        }else{
            mTextIssue.setVisibility(View.GONE);
        }

        if (bettingListDataBean.getPromotionList() == null || bettingListDataBean.getPromotionList().getList().size() == 0) {
            setStatus(SHOW_STATUS_NO_DATA);
            return;
        }
        hasNextPage = bettingListDataBean.getPromotionList().isHasNextPage();


        setStatus(SHOW_STATUS_SUCCESS);
        listData.clear();
        for (BettingListDataBean.PromotionData.BettingListData data : bettingListDataBean.getPromotionList().getList()) {
            listData.add(data);
        }
        buyClicked();
        specialistClick();

        L.d("listData >> " + listData.size());
        mAdapter = new BettingRecommendDetailsMvpAdapter(mContext , listData);

        mAdapter.setLoadingView(mOnloadingView);
        recyclerView.setAdapter(mAdapter);
        mAdapter.openLoadMore(0 , true);

        setListener();

        mAdapter.setmFragBuyClick(mFragBettingBuyClickListener);
        mAdapter.setmFragSpecialistClick(mFragBettingSpecialistClickListener);
    }

    @Override
    public void loadFailView() {
        setStatus(SHOW_STATUS_ERROR);
    }

    @Override
    public void loadNoData() {
        setStatus(SHOW_STATUS_NO_DATA);
    }
    private void setListener(){
        //上拉加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (hasNextPage) {
                            loadMoreData(playType , leagueKeys);
                        }else{
                            Toast.makeText(mContext, mContext.getResources().getText(R.string.nodata_txt), Toast.LENGTH_SHORT).show();
//                            mOnloadingView.findViewById(R.id.loading_text)
                            mAdapter.addFooterView(mNoLoadingView);
                        }
                    }
                },1000);
            }
        });
    }

//    private IssueBuyClickListener mIssueBuyClickListener;
//    // 购买(查看)的点击监听
//    public interface IssueBuyClickListener {
//        void BuyOnClick(View view, String s);
//    }

    private BettingBuyClickListenerFrag mFragBettingBuyClickListener;
    // 购买(查看)的点击监听
    public interface BettingBuyClickListenerFrag {
        void FragBuyOnClick(View view , BettingListDataBean.PromotionData.BettingListData s);
    }
    private BettingSpecialistClickListenerFrag mFragBettingSpecialistClickListener;
    // 专家详情的点击监听
    public interface BettingSpecialistClickListenerFrag {
        void FragSpecialistOnClick(View view , BettingListDataBean.PromotionData.BettingListData s);
    }
    /**
     * 购买(查看)的点击事件
     */
    public void buyClicked() {
        mFragBettingBuyClickListener = new BettingBuyClickListenerFrag() {
            @Override
            public void FragBuyOnClick(View view, BettingListDataBean.PromotionData.BettingListData listData) {
                Intent mIntent = new Intent(mContext , MvpBettingPayDetailsActivity.class);
                mIntent.putExtra(ConstantPool.TO_DETAILS_PROMOTION_ID , listData.getId());//推介id
                startActivity(mIntent);
            }
        };
    }
    /**
     * 专家详情点击事件
     */
    public void specialistClick(){
        mFragBettingSpecialistClickListener = new BettingSpecialistClickListenerFrag() {
            @Override
            public void FragSpecialistOnClick(View view, BettingListDataBean.PromotionData.BettingListData s) {
//                Toast.makeText(mContext, "专家** " + s, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(mContext,RecommendedExpertDetailsActivity.class);
                intent.putExtra("expertId",s.getUserid());
                intent.putExtra("winPoint",s.getWinPoint());
                intent.putExtra("errPoint",s.getErrPoint());
                startActivity(intent);
                // L.d("yxq-0418=== " , "点击了*专家** " + s);
            }
        };
    }

    /**--------------这**里**先**不**删**--------start-------这**里**先**不**删**------------------------------*/
    /**
     * 语音发布的popupwindow[暂时不做，先不删 鬼知道他什么时候要加上]
     */
    public void setPopupWindow() {

        new BlurPopWin.Builder(getActivity())
                //Radius越大耗时越长,被图片处理图像越模糊
                .setRadius(6)
                //设置居中还是底部显示
                .setshowAtLocationType(0)
                .setShowCloseButton(true)
                .setOutSideClickable(false)
                //获得推荐玩法选择
                .onRadioGroupOnclick(new BlurPopWin.PlayRadioGroupOnclick() {
                    @Override
                    public void onPlayRadioGroupOnclick(@IdRes int checkedId, int playType) {
                        switch (playType) {
                            case ConstantPool.PLAY_SPF:
                                Toast.makeText(mContext, "胜平负", Toast.LENGTH_SHORT).show();
                                break;
                            case ConstantPool.PLAY_LQSPF:
                                Toast.makeText(mContext, "让球胜平负", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                })
                //获得胜平负玩法选择
                .onSpfRadioGroupOnclick(new BlurPopWin.SpfRadioGroupOnclick() {
                    @Override
                    public void onSpfRadioGroupOnclick(int checkedId, int spfType) {
                        switch (spfType) {
                            case ConstantPool.SPF_S:
                                Toast.makeText(mContext, "胜", Toast.LENGTH_SHORT).show();
                                break;
                            case ConstantPool.SPF_P:
                                Toast.makeText(mContext, "平", Toast.LENGTH_SHORT).show();
                                break;
                            case ConstantPool.SPF_F:
                                Toast.makeText(mContext, "负", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                })
                .onLongRecordOnclick(new BlurPopWin.LongRecordOnClick() {
                    @Override
                    public void onLongRecordOnClick(@NonNull BlurPopWin blurPopWin) {
                        Toast.makeText(getActivity(), "长按录音", Toast.LENGTH_SHORT).show();
//                        state.setText("开始录音");
                        Thread thread = new Thread(new Runnable() {
                            public void run() {
                                record();
                            }
                        });
                        thread.start();
                    }
                })
                .onTouchRecordClick(new BlurPopWin.TouchRecordOnclick() {
                    @Override
                    public void onTouchRecordClick(int eventAction) {
                        if (eventAction == MotionEvent.ACTION_UP) {
                            isRecording = false;
//                            state.setText("停止录音");
                            Toast.makeText(getActivity(), "停止录音", Toast.LENGTH_SHORT).show();
                        }
//                        Toast.makeText(getActivity(), "touch", Toast.LENGTH_SHORT).show();
                    }
                })
                .onRadioPlayYuYin(new BlurPopWin.RadioPlayYuYin() {
                    @Override
                    public void onRadioPlayYuYin() {
                        play();
                        String time = gettime("");
                        L.d("radio_time = ", time+"");
                        Toast.makeText(getActivity(), "time = " + time+"", Toast.LENGTH_SHORT).show();
                    }
                })
                .onClick(new BlurPopWin.PopupCallback() {
                    @Override
                    public void onClick(@NonNull BlurPopWin blurPopWin, String money) {
                        if (money == null || money.length() == 0) {
                            L.d("qwer_asd*editData = ", "*");
                        } else {
                            L.d("qwer_asd*editData = ", money);
                        }
                        blurPopWin.dismiss();
                    }
                }).show(null);
//                }).show(mYuyin);
    }

    /**
     * 语音播放
     */
    @SuppressWarnings("deprecation")
    public void play() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/reverseme.pcm");
        // Get the length of the audio stored in the file (16 bit so 2 bytes per short) and create a short array to store the recorded audio.
        //获取音频文件中存储的长度(16位2字节/短),创建一个短的数组来存储记录音频。
        int musicLength = (int) (file.length() / 2);
        short[] music = new short[musicLength];
        try {
            // Create a DataInputStream to read the audio data back from the saved file.(创建DataInputStream读取音频数据保存的文件。)
            InputStream is = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);
            // Read the file into the music array.(文件读入数组的音频)
            int i = 0;
            while (dis.available() > 0) {
                music[i] = dis.readShort();
                i++;
            }
            // Close the input streams.关闭输入流
            dis.close();
            // Create a new AudioTrack object using the same parameters as the AudioRecord object used to create the file.
            //创建一个新的AudioTrack对象使用相同的参数作为AudioRecord对象用于创建该文件
            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    11025,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    musicLength * 2, AudioTrack.MODE_STREAM);
            // Start playback开始播放
            audioTrack.play();
            // Write the music buffer to the AudioTrack object 写音乐缓冲AudioTrack对象
            audioTrack.write(music, 0, musicLength);
            audioTrack.stop();
        } catch (Throwable t) {
            Log.e("AudioTrack", "Playback Failed");//播放失败
        }
    }

    /**
     * 语音收录
     */
    @SuppressWarnings("deprecation")
    public void record() {
        int frequency = 11025;
        int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/reverseme.pcm");
        // Delete any previous recording（删除任何先前的记录）
        if (file.exists()) {
            file.delete();
        }
        // Create the new file（创建新文件）
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create "
                    + file.toString());
        }

        try {
            // Create a DataOuputStream to write the audio data into the saved file（创建一个DataOuputStream音频数据写入保存的文件。）
            OutputStream os = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);
            // Create a new AudioRecord object to record the audio（创建一个新的AudioRecord对象记录音频）
            int bufferSize = AudioRecord.getMinBufferSize(frequency,
                    channelConfiguration, audioEncoding);
            AudioRecord audioRecord = new AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    frequency, channelConfiguration, audioEncoding, bufferSize);
            short[] buffer = new short[bufferSize];
            audioRecord.startRecording();
            isRecording = true;
            while (isRecording) {
                int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
                for (int i = 0; i < bufferReadResult; i++) {
                    dos.writeShort(buffer[i]);
                }
            }
            audioRecord.stop();
            dos.close();
        } catch (Throwable t) {
            Log.e("AudioRecord", "Recording Failed");//记录失败
        }
    }

    /**
     * 返回音频时间
     */
    private String gettime(String string) {   //使用此方法可以直接在后台获取音频文件的播放时间，而不会真的播放音频
        MediaPlayer player = new MediaPlayer();  //首先你先定义一个mediaplayer
        try {

            File file = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/reverseme.pcm");
            player.setDataSource(file.getPath());  //String是指音频文件的路径
            player.prepare();        //这个是mediaplayer的播放准备 缓冲

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {//监听准备
//
//            @Override
//            public void onPrepared(MediaPlayer player) {
//                int size = player.getDuration();
//                String timelong = size / 1000 + "''";
//
//            }
//        });
        double size = player.getDuration();//得到音频的时间
        String timelong1 = (int) Math.ceil((size / 1000)) + "''";//转换为秒 单位为''
        player.stop();//暂停播放
        player.release();//释放资源
        return timelong1;  //返回音频时间
    }

    /**--------------这**里**先**不**删**--------end-------这**里**先**不**删**------------------------------*/

    /**
     * 加载更多
     */
    private void loadMoreData(String type , String key){
        pageNum += 1;
        L.d("上拉加载、、、" + pageNum);
//        String url = "http://192.168.10.242:8098/promotion/info/matchPromotions"
          String url = BaseURLs.URI_BETTING_ISSUE_LIST;
//        String url = BaseURLs.URL_RECOMEND_LIST;
        String userid = AppConstants.register.getUser() == null ? "" : AppConstants.register.getUser().getUserId();
        Map<String ,String> mapPrament = new HashMap<>();

        mapPrament.put(PARAM_PAGE_SIZE , pageSize +"");
        mapPrament.put(PARAM_PAGE_NUM , pageNum + "");
        mapPrament.put(PARAM_USER_ID , userid);
        mapPrament.put(PARAM_MATCH_ID , mThirdid);
//        mapPrament.put(PARAM_MATCH_ID , "848856013");
//        mapPrament.put(PARAM_KEY , key);
//        mapPrament.put(PARAM_TYPE , type);
        mapPrament.put(PARAM_LANG , MyApp.getLanguage());
        mapPrament.put(PARAM_TIMEZONE , AppConstants.timeZone + "");
//        String signs = SignUtils.getSign("/promotion/info/matchPromotions", mapPrament);
        String signs = SignUtils.getSign(BaseURLs.PARAMENT_MATCH_RECOMMEND, mapPrament);

        Map<String ,String> map = new HashMap<>();
        map.put(PARAM_PAGE_SIZE , pageSize +""); //每页条数
        map.put(PARAM_PAGE_NUM , pageNum + "");//页码
        map.put(PARAM_USER_ID , userid);//用户id
        map.put(PARAM_MATCH_ID , mThirdid);
//        map.put(PARAM_MATCH_ID , "848856013");
//        map.put(PARAM_KEY , key);//联赛key
//        map.put(PARAM_TYPE , type);
        map.put(PARAM_SIGN , signs);

        L.d("qwer== >> " + signs);

        VolleyContentFast.requestJsonByGet(url,map , new VolleyContentFast.ResponseSuccessListener<BettingListDataBean>() {
            @Override
            public void onResponse(BettingListDataBean jsonBean) {
                if (jsonBean.getCode() == 200) {

                    if (jsonBean.getPromotionList() != null) {

                        hasNextPage = jsonBean.getPromotionList().isHasNextPage();

                        if (jsonBean.getPromotionList().getList().size() == 0) {

                            mAdapter.notifyDataChangedAfterLoadMore(false);
                            mAdapter.addFooterView(mNoLoadingView);

                        }else{

                            mAdapter.notifyDataChangedAfterLoadMore(jsonBean.getPromotionList().getList(),true);
                        }
                    }
                }

            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

            }
        },BettingListDataBean.class);
    }
    /**
     * 详情页面返回
     * @param detailsResuleEventBusEntity
     */
    public void onEventMainThread(BettingDetailsResuleEventBusEntity detailsResuleEventBusEntity){

        for (BettingListDataBean.PromotionData.BettingListData currlist : listData) {
            if (currlist.getId().equals(detailsResuleEventBusEntity.getCurrentId())) {
                currlist.setLookStatus("**");
                break;
            }
        }
        updataAdapter();
    }

    /**
     * 发布成功返回
     * @param issueSuccessResulyEventBus
     */
    public void onEventMainThread(IssueSuccessResulyEventBus issueSuccessResulyEventBus){
        if (issueSuccessResulyEventBus.issueSucce()) {
            dataRefresh();//发布成功后刷新数据
        }
    }
}
