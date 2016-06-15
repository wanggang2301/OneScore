package com.hhly.mlottery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.widget.MyScrollLayout;
import com.hhly.mlottery.widget.OnViewChangeListener;

/**
 * 
 * @ClassName: WelcomeViewActivity 
 * @Description: 引导界面
 * @author Tenney
 * @date 2015-12-22 下午12:21:26
 */
public class WelcomeViewActivity extends BaseActivity implements OnViewChangeListener {

	private MyScrollLayout mScrollLayout;
	private Button startBtn;
	//开机引导页的几张动画
	private RelativeLayout wel_layout_img1,wel_layout_img2,wel_layout_img3;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcomeview);
		initView();
		initImg();
	}

	private void initView() {
		mScrollLayout =$(R.id.ScrollLayout);
		startBtn =$(R.id.startBtn);
		startBtn.setOnClickListener(onClick);
		mScrollLayout.SetOnViewChangeListener(this);
		wel_layout_img1=$(R.id.wel_layout_img1);
		wel_layout_img2=$(R.id.wel_layout_img2);
		wel_layout_img3=$(R.id.wel_layout_img3);
	}
	
	//判断是什么语言加载什么图片
	public void initImg(){
		//如果是繁体语言环境
		if(MyApp.isLanguage.equals("rTW")){
			wel_layout_img1.setBackgroundResource(R.mipmap.welcome1);
			wel_layout_img2.setBackgroundResource(R.mipmap.welcome2);
			wel_layout_img3.setBackgroundResource(R.mipmap.welcome3);
		}else if(MyApp.isLanguage.equals("rCN")){
			wel_layout_img1.setBackgroundResource(R.mipmap.welcome1);
			wel_layout_img2.setBackgroundResource(R.mipmap.welcome2);
			wel_layout_img3.setBackgroundResource(R.mipmap.welcome3);
		}
	}

	private View.OnClickListener onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.startBtn:
				//第一次启动保存为yes
				PreferenceUtil.commitString("isFirst", "YES");
				//第一次启动的时候保存版本号
				PreferenceUtil.commitString("versionName", WelcomeActivity.mPackageInfo.versionName);
				startActivity(new Intent(WelcomeViewActivity.this, HomePagerActivity.class));
				finish();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 屏蔽返回键的方法
	 */
	public boolean onKeyDown(int keyCode,KeyEvent event){
		 switch(keyCode){
		 case KeyEvent.KEYCODE_HOME:return true;
		 case KeyEvent.KEYCODE_BACK:return true;
		 case KeyEvent.KEYCODE_CALL:return true;
		 case KeyEvent.KEYCODE_SYM: return true;
		 case KeyEvent.KEYCODE_VOLUME_DOWN: return true;
		 case KeyEvent.KEYCODE_VOLUME_UP: return true;
		 case KeyEvent.KEYCODE_STAR: return true;
		 }
		 return super.onKeyDown(keyCode, event);
	}

	@Override
	public void OnViewChange(int view) {
		// TODO Auto-generated method stub
		
	}

}