package com.hhly.mlottery.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.PreferenceUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.Locale;

/**
 * 语言切换
 * Created by hhly107 on 2016/4/6.
 */
public class HomeLanguageActivity extends BaseActivity implements View.OnClickListener {
    private ImageView public_img_back, public_btn_set, public_btn_filter;// 返回到菜单
    private Context mContext;
    private TextView public_txt_title;// 标题
    private Button public_btn_save;// 保存
    private RelativeLayout language_china_layout, language_tw_layout, language_en_layout, language_ko_layout, language_id_layout, language_th_layout, language_vi_layout;

    private ImageView language_china_img, language_tw_img, language_en_img, language_ko_img, language_id_img, language_th_img, language_vi_img;
    private ColorStateList whiteColor;//Button的字体颜色值
    private String stIsLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_language);
        mContext = HomeLanguageActivity.this;
        stIsLanguage = MyApp.switchLanguage(PreferenceUtil.getString("language", ""));
        InitView();
        // 根据保存的语言类型判断来显示“默认选中的图片”
        switchIcon();
        public_btn_save.setClickable(false);
        public_btn_save.setTextColor(getResources().getColorStateList(R.color.version));
        whiteColor = getResources().getColorStateList(R.color.white);
        // 显示相应的语言选择
        switch (MyApp.isPackageName) {
            case AppConstants.PACKGER_NAME_ZH:// 国内版
                language_china_layout.setVisibility(View.VISIBLE);
                language_tw_layout.setVisibility(View.VISIBLE);
                language_en_layout.setVisibility(View.GONE);
                language_ko_layout.setVisibility(View.GONE);
                language_id_layout.setVisibility(View.GONE);
                language_th_layout.setVisibility(View.GONE);
                language_vi_layout.setVisibility(View.GONE);
                break;
            case AppConstants.PACKGER_NAME_TH:// 泰国版
                language_china_layout.setVisibility(View.GONE);
                language_tw_layout.setVisibility(View.VISIBLE);
                language_en_layout.setVisibility(View.VISIBLE);
                language_ko_layout.setVisibility(View.GONE);
                language_id_layout.setVisibility(View.GONE);
                language_th_layout.setVisibility(View.VISIBLE);
                language_vi_layout.setVisibility(View.GONE);
                break;
            case AppConstants.PACKGER_NAME_VN_HN:// 越南北版
            case AppConstants.PACKGER_NAME_VN:// 越南南版
                language_china_layout.setVisibility(View.GONE);
                language_tw_layout.setVisibility(View.VISIBLE);
                language_en_layout.setVisibility(View.VISIBLE);
                language_ko_layout.setVisibility(View.GONE);
                language_id_layout.setVisibility(View.GONE);
                language_th_layout.setVisibility(View.GONE);
                language_vi_layout.setVisibility(View.VISIBLE);
                break;
            case AppConstants.PACKGER_NAME_UK:// 英文版
                language_china_layout.setVisibility(View.VISIBLE);
                language_tw_layout.setVisibility(View.VISIBLE);
                language_en_layout.setVisibility(View.VISIBLE);
                language_ko_layout.setVisibility(View.GONE);
                language_id_layout.setVisibility(View.GONE);
                language_th_layout.setVisibility(View.GONE);
                language_vi_layout.setVisibility(View.GONE);
                break;
        }
    }

    public void InitView() {
        // 标题
        public_txt_title = (TextView) findViewById(R.id.public_txt_title);
        public_txt_title.setText(R.string.language_frame_txt);
        public_img_back = (ImageView) findViewById(R.id.public_img_back);// 返回菜单
        public_img_back.setImageDrawable(getResources().getDrawable(R.mipmap.back));
        public_img_back.setOnClickListener(this);

        public_btn_set = (ImageView) findViewById(R.id.public_btn_set);//
        public_btn_set.setVisibility(View.GONE);

        public_btn_filter = (ImageView) findViewById(R.id.public_btn_filter);
        public_btn_filter.setVisibility(View.GONE);

        public_btn_save = (Button) findViewById(R.id.public_btn_save);// 保存
        public_btn_save.setVisibility(View.VISIBLE);
        public_btn_save.setOnClickListener(this);
        //语言选择layout----------------------begin----------------------->>
        //中文简体
        language_china_layout = (RelativeLayout) findViewById(R.id.language_china_layout);//
        language_china_layout.setOnClickListener(this);
        //中文繁体
        language_tw_layout = (RelativeLayout) findViewById(R.id.language_tw_layout);//
        language_tw_layout.setOnClickListener(this);
        //英语
        language_en_layout = (RelativeLayout) findViewById(R.id.language_en_layout);//
        language_en_layout.setOnClickListener(this);
        //韩语
        language_ko_layout = (RelativeLayout) findViewById(R.id.language_ko_layout);//
        language_ko_layout.setOnClickListener(this);
        //印尼语
        language_id_layout = (RelativeLayout) findViewById(R.id.language_id_layout);//
        language_id_layout.setOnClickListener(this);
        //泰语
        language_th_layout = (RelativeLayout) findViewById(R.id.language_th_layout);//
        language_th_layout.setOnClickListener(this);
        //越南语
        language_vi_layout = (RelativeLayout) findViewById(R.id.language_vi_layout);//
        language_vi_layout.setOnClickListener(this);
        //语言选择layout end---------------------end---------------------->>

        //“√”选择---------------------begin---------------------->>
        language_china_img = (ImageView) findViewById(R.id.language_china_img);//
        language_tw_img = (ImageView) findViewById(R.id.language_tw_img);//
        language_en_img = (ImageView) findViewById(R.id.language_en_img);//
        language_ko_img = (ImageView) findViewById(R.id.language_ko_img);//
        language_id_img = (ImageView) findViewById(R.id.language_id_img);//
        language_th_img = (ImageView) findViewById(R.id.language_th_img);//
        language_vi_img = (ImageView) findViewById(R.id.language_vi_img);//
        //“√”选择---------------------end---------------------->>

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_img_back:// 点击返回菜单栏
                finish();
                MobclickAgent.onEvent(mContext, "LanguageChanger_Exit");
                break;
            case R.id.language_china_layout:// 中文简体
                MobclickAgent.onEvent(mContext, "Language_rCN");
                language_tw_img.setVisibility(View.GONE);
                language_en_img.setVisibility(View.GONE);
                language_ko_img.setVisibility(View.GONE);
                language_id_img.setVisibility(View.GONE);
                language_th_img.setVisibility(View.GONE);
                language_vi_img.setVisibility(View.GONE);
                language_china_img.setVisibility(View.VISIBLE);
                if (stIsLanguage.equals("rCN")) {//如果语言是cn
                    public_btn_save.setClickable(false);
                    public_btn_save.setTextColor(getResources().getColorStateList(R.color.version));
                } else {//如果不相等
                    public_btn_save.setTextColor(whiteColor);
                    public_btn_save.setClickable(true);
                }
                break;
            case R.id.language_tw_layout:// 中文繁体
                MobclickAgent.onEvent(mContext, "Language_rTW");
                language_china_img.setVisibility(View.GONE);
                language_en_img.setVisibility(View.GONE);
                language_ko_img.setVisibility(View.GONE);
                language_id_img.setVisibility(View.GONE);
                language_th_img.setVisibility(View.GONE);
                language_vi_img.setVisibility(View.GONE);
                language_tw_img.setVisibility(View.VISIBLE);
                if (stIsLanguage.equals("rTW")) {//如果语言是TW
                    public_btn_save.setClickable(false);
                    public_btn_save.setTextColor(getResources().getColorStateList(R.color.version));
                } else {//如果不想等
                    public_btn_save.setTextColor(whiteColor);
                    public_btn_save.setClickable(true);
                }
                break;
            case R.id.language_en_layout:// 英文
                MobclickAgent.onEvent(mContext, "Language_rEN");
                language_china_img.setVisibility(View.GONE);
                language_tw_img.setVisibility(View.GONE);
                language_ko_img.setVisibility(View.GONE);
                language_id_img.setVisibility(View.GONE);
                language_th_img.setVisibility(View.GONE);
                language_vi_img.setVisibility(View.GONE);
                language_en_img.setVisibility(View.VISIBLE);
                if (stIsLanguage.equals("rEN")) {//如果语言是英文
                    public_btn_save.setClickable(false);
                    public_btn_save.setTextColor(getResources().getColorStateList(R.color.version));
                } else {//如果不相等
                    public_btn_save.setTextColor(whiteColor);
                    public_btn_save.setClickable(true);
                }
                break;
            case R.id.language_ko_layout:// 韩语
                MobclickAgent.onEvent(mContext, "Language_rKO");
                language_china_img.setVisibility(View.GONE);
                language_tw_img.setVisibility(View.GONE);
                language_en_img.setVisibility(View.GONE);
                language_id_img.setVisibility(View.GONE);
                language_th_img.setVisibility(View.GONE);
                language_vi_img.setVisibility(View.GONE);
                language_ko_img.setVisibility(View.VISIBLE);
                if (stIsLanguage.equals("rKO")) {//如果语言是韩语
                    public_btn_save.setClickable(false);
                    public_btn_save.setTextColor(getResources().getColorStateList(R.color.version));
                } else {//如果不相等
                    public_btn_save.setTextColor(whiteColor);
                    public_btn_save.setClickable(true);
                }
                break;
            case R.id.language_id_layout:// 印尼语
                MobclickAgent.onEvent(mContext, "Language_rID");
                language_china_img.setVisibility(View.GONE);
                language_tw_img.setVisibility(View.GONE);
                language_en_img.setVisibility(View.GONE);
                language_ko_img.setVisibility(View.GONE);
                language_th_img.setVisibility(View.GONE);
                language_vi_img.setVisibility(View.GONE);
                language_id_img.setVisibility(View.VISIBLE);
                if (stIsLanguage.equals("rID")) {//如果语言是印尼语
                    public_btn_save.setClickable(false);
                    public_btn_save.setTextColor(getResources().getColorStateList(R.color.version));
                } else {//如果不相等
                    public_btn_save.setTextColor(whiteColor);
                    public_btn_save.setClickable(true);
                }
                break;
            case R.id.language_th_layout:// 泰语
                MobclickAgent.onEvent(mContext, "Language_rIH");
                language_china_img.setVisibility(View.GONE);
                language_tw_img.setVisibility(View.GONE);
                language_en_img.setVisibility(View.GONE);
                language_ko_img.setVisibility(View.GONE);
                language_id_img.setVisibility(View.GONE);
                language_vi_img.setVisibility(View.GONE);
                language_th_img.setVisibility(View.VISIBLE);
                if (stIsLanguage.equals("rTH")) {//如果语言是泰语
                    public_btn_save.setClickable(false);
                    public_btn_save.setTextColor(getResources().getColorStateList(R.color.version));
                } else {//如果不相等
                    public_btn_save.setTextColor(whiteColor);
                    public_btn_save.setClickable(true);
                }
                break;
            case R.id.language_vi_layout:// 越南语
                MobclickAgent.onEvent(mContext, "Language_r");
                language_china_img.setVisibility(View.GONE);
                language_tw_img.setVisibility(View.GONE);
                language_en_img.setVisibility(View.GONE);
                language_ko_img.setVisibility(View.GONE);
                language_id_img.setVisibility(View.GONE);
                language_th_img.setVisibility(View.GONE);
                language_vi_img.setVisibility(View.VISIBLE);
                if (stIsLanguage.equals("rVI")) {//如果语言是越南语
                    public_btn_save.setClickable(false);
                    public_btn_save.setTextColor(getResources().getColorStateList(R.color.version));
                } else {//如果不相等
                    public_btn_save.setTextColor(whiteColor);
                    public_btn_save.setClickable(true);
                }
                break;
            case R.id.public_btn_save:// 保存
                MobclickAgent.onEvent(mContext, "Language_Save");
                if (language_china_img.getVisibility() == View.VISIBLE) {//如果选择简体
                    MyApp.mConfiguration.locale = Locale.SIMPLIFIED_CHINESE;
                    PreferenceUtil.commitString("language", "rCN");
                } else if (language_tw_img.getVisibility() == View.VISIBLE) {//如果选择繁体
                    MyApp.mConfiguration.locale = Locale.TAIWAN;
                    PreferenceUtil.commitString("language", "rTW");
                } else if (language_en_img.getVisibility() == View.VISIBLE) {//如果选择英文
                    MyApp.mConfiguration.locale = Locale.US;
                    PreferenceUtil.commitString("language", "rEN");
                } else if (language_ko_img.getVisibility() == View.VISIBLE) {//如果选择韩语
                    MyApp.mConfiguration.locale = Locale.KOREA;
                    PreferenceUtil.commitString("language", "rKO");
                } else if (language_id_img.getVisibility() == View.VISIBLE) {//如果选择印尼语
                    MyApp.mConfiguration.locale = new Locale("in_ID");
                    PreferenceUtil.commitString("language", "rID");
                } else if (language_th_img.getVisibility() == View.VISIBLE) {//如果选择泰语
                    MyApp.mConfiguration.locale = new Locale("th_TH");
                    PreferenceUtil.commitString("language", "rTH");
                } else if (language_vi_img.getVisibility() == View.VISIBLE) {//如果选择越南
                    MyApp.mConfiguration.locale = new Locale("vi_VN");
                    PreferenceUtil.commitString("language", "rVI");
                }
                MyApp.isLanguage = PreferenceUtil.getString("language", "");
                MyApp.mResources.updateConfiguration(MyApp.mConfiguration, MyApp.mDm);

                //重新注入请求参数
                MyApp.get().initDagger();

                this.finish();
                Intent intent = new Intent();
                intent.setClass(this, IndexActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("languageChanger", true);
                startActivity(intent);// 跳回到首页面
                break;
            default:
                break;
        }
    }

    /**
     * 根据保存的语言类型判断来显示“默认选中的图片”
     */
    protected void switchIcon() {
        language_china_img.setVisibility(View.GONE);
        language_tw_img.setVisibility(View.GONE);
        language_en_img.setVisibility(View.GONE);
        language_ko_img.setVisibility(View.GONE);
        language_id_img.setVisibility(View.GONE);
        language_th_img.setVisibility(View.GONE);
        language_vi_img.setVisibility(View.GONE);
        if (stIsLanguage.equals("rCN")) {// 如果是中文简体的语言环境
            language_china_img.setVisibility(View.VISIBLE);
        } else if (stIsLanguage.equals("rTW")) {// 如果是中文繁体的语言环境
            language_tw_img.setVisibility(View.VISIBLE);
        } else if (stIsLanguage.equals("rEN")) {//如果是英文
            language_en_img.setVisibility(View.VISIBLE);
        } else if (stIsLanguage.equals("rKO")) {//如果是韩语
            language_ko_img.setVisibility(View.VISIBLE);
        } else if (stIsLanguage.equals("rID")) {//如果是印尼语
            language_id_img.setVisibility(View.VISIBLE);
        } else if (stIsLanguage.equals("rTH")) {//如果是泰语
            language_th_img.setVisibility(View.VISIBLE);
        } else if (stIsLanguage.equals("rVI")) {//如果是越南语
            language_vi_img.setVisibility(View.VISIBLE);
        }
    }
}
