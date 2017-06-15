package com.hhly.mlottery.frame.footballframe;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.TexasWebActivity;
import com.hhly.mlottery.activity.WebActivity;
import com.hhly.mlottery.mvp.bettingmvp.mvpview.MvpBettingIssueDetailsActivity;
import com.hhly.mlottery.mvp.bettingmvp.mvpview.MvpBettingPayDetailsActivity;
import com.hhly.mlottery.mvp.bettingmvp.mvpview.MvpChargeMoneyActivity;
import com.hhly.mlottery.adapter.bettingadapter.BettingIssueAdapter;
import com.hhly.mlottery.bean.bettingbean.BettingIssueBean;
import com.hhly.mlottery.config.BlurPopWin;
import com.hhly.mlottery.config.ConstantPool;
import com.hhly.mlottery.util.L;

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
import java.util.List;

/**
 * Created by：XQyi on 2017/5/11 11:12
 * Use:内页推介页面
 */
public class FootballBettingIssueFragment extends Fragment implements View.OnClickListener {

    private static final String THIRDID = "param1";
    private String mThirdid;
    private View mView;
    private List<BettingIssueBean> list;
    private BettingIssueAdapter mAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ImageView mYuyin;
    private ImageView mText;
    private ImageView mFabuOrQxiao;
    private boolean isRecording = false;

    private boolean isFabu = true;//是否是要发布状态（默认true 点击发布）

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

        mView = inflater.inflate(R.layout.betting_issue_fragment, container, false);
        initView();
        initData();
        return mView;
    }

    private void initView() {
        recyclerView = (RecyclerView) mView.findViewById(R.id.betting_issue_view);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        mYuyin = (ImageView) mView.findViewById(R.id.football_betting_yuyin_img);
        mYuyin.setOnClickListener(this);
        mText = (ImageView) mView.findViewById(R.id.football_betting_text_img);
        mText.setOnClickListener(this);
        mFabuOrQxiao = (ImageView) mView.findViewById(R.id.football_betting_fabuorquxiao_img);
        mFabuOrQxiao.setOnClickListener(this);
    }

    public void initData() {
        list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            BettingIssueBean data = new BettingIssueBean();
            data.setName("我是专家 " + i);
            list.add(data);
        }
        buyClicked();
        if (mAdapter == null) {
            mAdapter = new BettingIssueAdapter(getActivity(), list);
            recyclerView.setAdapter(mAdapter);
            mAdapter.setmBuyClick(mIssueBuyClickListener);
        } else {
            updataAdapter();
        }
    }

    private void updataAdapter() {
        if (mAdapter == null) {
            return;
        }
        mAdapter.setData(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.football_betting_yuyin_img:
//                Toast.makeText(getActivity(), "充值页", Toast.LENGTH_SHORT).show();
//                setPopupWindow();
//                startActivity(new Intent(getContext() , MvpChargeMoneyActivity.class));

                Intent intent = new Intent(getContext(), TexasWebActivity.class);
                intent.putExtra("key", "http://texas.1332255.com:4007/VideoGameWeb/mobile/index.html");
                startActivity(intent);
                break;
            case R.id.football_betting_text_img:
                Toast.makeText(getActivity(), "发布文字", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext() , MvpBettingIssueDetailsActivity.class));
                break;
            case R.id.football_betting_fabuorquxiao_img:
                if (isFabu) {
                    mYuyin.setVisibility(View.VISIBLE);
                    mText.setVisibility(View.VISIBLE);
                    mFabuOrQxiao.setBackgroundResource(R.mipmap.football_icon_quxiao);
                    Toast.makeText(getActivity(), "发布", Toast.LENGTH_SHORT).show();
                } else {
                    mYuyin.setVisibility(View.GONE);
                    mText.setVisibility(View.GONE);
                    mFabuOrQxiao.setBackgroundResource(R.mipmap.football_icon_fabu);
                    Toast.makeText(getActivity(), "取消", Toast.LENGTH_SHORT).show();
                }
                isFabu = !isFabu;
                break;
        }
    }

    private IssueBuyClickListener mIssueBuyClickListener;

    // 购买(查看)的点击监听
    public interface IssueBuyClickListener {
        void BuyOnClick(View view, String s);
    }

    /**
     * 购买(查看)的点击事件
     */
    public void buyClicked() {
        mIssueBuyClickListener = new IssueBuyClickListener() {
            @Override
            public void BuyOnClick(View view, String s) {
                L.d("qwer_name = ", s);
                Intent mIntent = new Intent(getActivity(), MvpBettingPayDetailsActivity.class);
                mIntent.putExtra(ConstantPool.TO_DETAILS_PROMOTION_ID , "id");//推介id
                startActivity(mIntent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_fix_out);
            }
        };
    }

    /**--------------这**里**先**不**删**--------start-------这**里**先**不**删**------------------------------*/
    /**
     * 语音发布的popupwindow[本期需求暂时不做，先不删 鬼知道他什么时候要加上]
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
                                Toast.makeText(getContext(), "胜平负", Toast.LENGTH_SHORT).show();
                                break;
                            case ConstantPool.PLAY_LQSPF:
                                Toast.makeText(getContext(), "让球胜平负", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getContext(), "胜", Toast.LENGTH_SHORT).show();
                                break;
                            case ConstantPool.SPF_P:
                                Toast.makeText(getContext(), "平", Toast.LENGTH_SHORT).show();
                                break;
                            case ConstantPool.SPF_F:
                                Toast.makeText(getContext(), "负", Toast.LENGTH_SHORT).show();
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
                }).show(mYuyin);
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
}
