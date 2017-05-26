package com.hhly.mlottery.config;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.text.InputType;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.L;

/**
 * Created by：XQyi on 2017/5/12 17:16
 * Use: 自定义popWin（高斯模糊背景的popupWindow）
 */
public class BlurPopWin {
    private LinearLayout pop_root_layout;
    private TextView content;
    private ImageView close;
    private Builder builder;
    private PopupWindow popupWindow;
    private int radius;
    private float touchY;
    private float touchX;
    private Bitmap localBit;

    public static final String GRAVITY_BOTTOM = "BOTTOM";
    public static final String GRAVITY_CENTER = "CENTER";
    private EditText editText;
    private TextView fabuText;
    private LinearLayout record;
    private RelativeLayout pop_layout;
    private RadioGroup playGroup;
    private RadioButton playRadioSpf;
    private RadioButton playRadioLqspf;
    private RadioGroup spfGroup;
    private RadioButton spfRadioS;
    private RadioButton spfRadioP;
    private RadioButton spfRadioF;
    private int playType;//推荐玩法选择
    private int spfType;//胜平负选择
    private LinearLayout radioPlay;
    private LinearLayout radioRestart;
    private LinearLayout radioYuYin;
    private LinearLayout playYuYin;

    public BlurPopWin(Builder builder) {
        this.builder = builder;
        builder.blurPopWin = initBlurPopWin(builder);
    }

    @UiThread
    public void show(View view) {
        builder.blurPopWin.popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    @UiThread
    public void dismiss() {
        if (builder != null && builder.blurPopWin != null)
            builder.blurPopWin.popupWindow.dismiss();
    }

    /*
    截取屏幕
    * */
    @Nullable
    private Bitmap getIerceptionScreen() {
        // View是你需要截图的View
        View view = builder.activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        builder.activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        // 获取屏幕长和高
        int width = builder.activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = builder.activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap bitmap = Bitmap.createBitmap(b, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        bitmap = FastBlur.fastBlur(bitmap, radius);
        if (bitmap != null) {
            return bitmap;
        } else {
            return null;
        }
    }

    /*
    初始化
    * */
    @UiThread
    private BlurPopWin initBlurPopWin(final Builder builder) {
        if (builder != null) {

            View rootView = builder.activity.getLayoutInflater().inflate(R.layout.betting_issue_popup, null, false);
            popupWindow = new PopupWindow(rootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            pop_root_layout = (LinearLayout) rootView.findViewById(R.id.pop_root_layout);
            pop_layout = (RelativeLayout) rootView.findViewById(R.id.popup_ll);
            editText = (EditText) rootView.findViewById(R.id.edit_query);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            close = (ImageView) rootView.findViewById(R.id.close_popup_img);
            fabuText = (TextView) rootView.findViewById(R.id.fabu_text);
            record = (LinearLayout)rootView.findViewById(R.id.issue_record);
            //推荐玩法
            playGroup = (RadioGroup)rootView.findViewById(R.id.play_gendergroup);
            playRadioSpf = (RadioButton)rootView.findViewById(R.id.play_spf_radio);
            playRadioSpf.setChecked(true);
            playRadioLqspf = (RadioButton)rootView.findViewById(R.id.play_lqspf_radio);
            //胜平负
            spfGroup = (RadioGroup)rootView.findViewById(R.id.spf_gendergroup);
            spfRadioS = (RadioButton)rootView.findViewById(R.id.spf_radio_s);
            spfRadioS.setChecked(true);
            spfRadioP = (RadioButton)rootView.findViewById(R.id.spf_radio_p);
            spfRadioF = (RadioButton)rootView.findViewById(R.id.spf_radio_f);

            radioYuYin = (LinearLayout)rootView.findViewById(R.id.issue_radio_yuyin);
            playYuYin = (LinearLayout)rootView.findViewById(R.id.issue_play_yuyin);

            //语音播放
            radioPlay = (LinearLayout)rootView.findViewById(R.id.radio_play);
            //重新录制
            radioRestart = (LinearLayout)rootView.findViewById(R.id.radio_restart);

            playGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                    if (checkedId == playRadioSpf.getId()) {
                        playType = ConstantPool.PLAY_SPF;
                        L.d("qwer_asd" , " SPF " + playType);
                    }else if (checkedId == playRadioLqspf.getId()) {
                        playType = ConstantPool.PLAY_LQSPF;
                        L.d("qwer_asd" , " LQSPF " + playType);
                    }
                    if (builder.playRadioGroupOnclick != null) {
                        builder.playRadioGroupOnclick.onPlayRadioGroupOnclick(checkedId , playType);
                    }
                }
            });

            spfGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    if (checkedId == spfRadioS.getId()) {
                        spfType = ConstantPool.SPF_S;
                        L.d("qwer_asd" , " S " + checkedId);
                    }else if (checkedId == spfRadioP.getId()) {
                        spfType = ConstantPool.SPF_P;
                        L.d("qwer_asd" , " P " + checkedId);
                    }else if (checkedId == spfRadioF.getId()) {
                        spfType = ConstantPool.SPF_F;
                        L.d("qwer_asd" , " F " + checkedId);
                    }
                    if (builder.spfRadioGroupOnclick != null) {
                        builder.spfRadioGroupOnclick.onSpfRadioGroupOnclick(checkedId , spfType);
                    }
                }
            });

            record.setLongClickable(true);
            record.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (builder.longRecordOnclick != null) {
                        builder.longRecordOnclick.onLongRecordOnClick(BlurPopWin.this);
                    }
                    return true;
                }
            });
            record.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (builder.touchRecordOnclick != null) {
                        builder.touchRecordOnclick.onTouchRecordClick(event.getAction());
                    }
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            break;
                        case MotionEvent.ACTION_UP:
                            radioYuYin.setVisibility(View.GONE);
                            playYuYin.setVisibility(View.VISIBLE);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                    }
                    return false;
                }
            });
            radioRestart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    radioYuYin.setVisibility(View.VISIBLE);
                    playYuYin.setVisibility(View.GONE);
                }
            });
            radioPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (builder.radioPlayYuYin != null) {
                        builder.radioPlayYuYin.onRadioPlayYuYin();
                    }
                }
            });
//            record.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (builder.touchRecordOnclick != null) {
//                        builder.touchRecordOnclick.onTouchRecordClick(BlurPopWin.this);
//                    }
//                }
//            });
            fabuText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String editData = editText.getText().toString();
                    if (builder.popupCallback != null) {
                        builder.popupCallback.onClick(BlurPopWin.this , editData);
                    }
                }
            });

            if (builder.content != null) {
                content.setText(builder.content);
            }

            if (builder.radius != 0) {
                radius = builder.radius;
            } else {
                radius = 5;
            }

            if (builder.contentTextSize != 0) {
                content.setTextSize(builder.contentTextSize);
            }

            if (builder.isShowClose) {
                close.setVisibility(View.VISIBLE);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
            } else {
                close.setClickable(false);
                close.setVisibility(View.INVISIBLE);
            }

//            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            if (builder.showAtLocationType.equals(GRAVITY_CENTER)) {
//                lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
//            } else {
//                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//            }
//            pop_layout.setLayoutParams(lp);

            if (localBit == null) {
                localBit = getIerceptionScreen();
            }

            pop_root_layout.setBackground(new BitmapDrawable(localBit));

            pop_root_layout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            touchY = motionEvent.getY();
                            touchX = motionEvent.getX();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_UP:

                            if(builder.isBackgroundClose){
                                if ((touchX < pop_layout.getLeft() || touchX > pop_layout.getRight())
                                        &&(touchY < pop_layout.getTop() || touchY > pop_layout.getBottom())) {

                                    popupWindow.dismiss();
                                }
//                                if (touchY < pop_layout.getTop() || touchY > pop_layout.getBottom() ) {
//                                    popupWindow.dismiss();
//                                }
                                L.d("qwer_ad = ", "点击外层");
//                                popupWindow.dismiss();
//                                if (builder.showAtLocationType.equals(GRAVITY_CENTER)) {
//                                    if (touchY < pop_layout.getTop() || touchY > pop_layout.getBottom()) {
//                                        popupWindow.dismiss();
//                                    }
//                                    L.d("qwer_ad = ", "点击外层");
//                                    popupWindow.dismiss();
//                                }
//                                else if (builder.showAtLocationType.equals(GRAVITY_BOTTOM)) {
//                                    if (touchY < pop_layout.getTop()) {
//                                        popupWindow.dismiss();
//                                    }
//                                }
                            }
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
        } else {
            throw new NullPointerException("---> BlurPopWin ---> initBlurPopWin --->builder=null");
        }
        return this;
    }

    public static class Builder {

        protected BlurPopWin blurPopWin;
//        protected int titleTextSize;
        protected int contentTextSize;
        protected Activity activity;
        protected Context context;
        protected PopupCallback popupCallback;
        protected TouchRecordOnclick touchRecordOnclick;
        protected LongRecordOnClick longRecordOnclick;
        protected PlayRadioGroupOnclick playRadioGroupOnclick;
        protected SpfRadioGroupOnclick spfRadioGroupOnclick;
        protected RadioPlayYuYin radioPlayYuYin;
        protected int radius;
        protected String title, content;
        protected boolean isCancelable;
        //默认不显示XX
        protected boolean isShowClose = false;
        protected boolean isBackgroundClose = true;
        protected String showAtLocationType = GRAVITY_CENTER;

        public Builder(@NonNull Context context) {
            this.activity = (Activity) context;
            this.context = context;
            this.isCancelable = true;
        }

        public Builder onClick(PopupCallback popupCallback) {
            this.popupCallback = popupCallback;
            return this;
        }

        public Builder onTouchRecordClick(TouchRecordOnclick touchRecordOnclick){
            this.touchRecordOnclick = touchRecordOnclick;
            return this;
        }
        public Builder onLongRecordOnclick(LongRecordOnClick longRecordOnclick){
            this.longRecordOnclick = longRecordOnclick;
            return this;
        }

        public Builder onRadioGroupOnclick(PlayRadioGroupOnclick playRadioGroupOnclick){
            this.playRadioGroupOnclick = playRadioGroupOnclick;
            return this;
        }

        public Builder onSpfRadioGroupOnclick(SpfRadioGroupOnclick spfRadioGroupOnclick){
            this.spfRadioGroupOnclick = spfRadioGroupOnclick;
            return this;
        }

        public Builder onRadioPlayYuYin(RadioPlayYuYin radioPlayYuYin){
            this.radioPlayYuYin = radioPlayYuYin;
            return this;
        }
//        /*
//        * 设置标题
//        * */
//        public Builder setTitle(@StringRes int titleRes) {
//            setTitle(this.context.getString(titleRes));
//            return this;
//        }


//        public Builder setTitle(@NonNull String title) {
//            this.title = title;
//            return this;
//        }

        public Builder setRadius(int radius) {
            this.radius = radius;
            return this;
        }

//        public Builder setTitleTextSize(int size) {
//            this.titleTextSize = size;
//            return this;
//        }

//        public Builder setContentTextSize(int size) {
//            this.contentTextSize = size;
//            return this;
//        }

        /*
        * 设置主文内容
        * */
        public Builder setContent(@StringRes int contentRes) {
            setContent(this.context.getString(contentRes));
            return this;
        }

        /*
        * 设置主文内容
        * */
        public Builder setContent(@NonNull String content) {
            this.content = content;
            return this;
        }

        /*
        * 默认居中,手动设置了才在最下面
        * */
        public Builder setshowAtLocationType(int type) {
            if (type == 0) {
                this.showAtLocationType = GRAVITY_CENTER;
            } else if (type == 1) {
                this.showAtLocationType = GRAVITY_BOTTOM;
            }

            return this;
        }

        public Builder setShowCloseButton(@NonNull boolean flag) {
            this.isShowClose = flag;
            return this;
        }

        public Builder setOutSideClickable(@NonNull boolean flag) {
            this.isBackgroundClose = flag;
            return this;
        }

        @UiThread
        public BlurPopWin build() {
            return new BlurPopWin(this);
        }

        @UiThread
        public BlurPopWin show(View view) {
            BlurPopWin blurPopWin = build();
            blurPopWin.show(view);

            return blurPopWin;
        }

    }
    /**
     * 发布按键接口
     */
    public interface PopupCallback {
        void onClick(@NonNull BlurPopWin blurPopWin ,String money);
    }

    /**
     * 录音按键接口
     */
    public interface TouchRecordOnclick {
        void onTouchRecordClick(int event);
    }

    /**
     * 长按录音监听
     */
    public interface LongRecordOnClick{
        void onLongRecordOnClick(@NonNull BlurPopWin blurPopWin );
    }

    /**
     * 推荐玩法监听
     */
    public interface PlayRadioGroupOnclick {
         void onPlayRadioGroupOnclick(int checkedId , int playType);
    }

    /**
     * 胜平负玩法监听
     */
    public interface SpfRadioGroupOnclick{
        void onSpfRadioGroupOnclick(int checkedId , int spfType);
    }

    /**
     * 语音播放监听
     */
    public interface RadioPlayYuYin{
        void onRadioPlayYuYin();
    }
}
