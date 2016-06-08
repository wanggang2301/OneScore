package com.hhly.mlottery.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hhly.mlottery.MyApp;
import com.hhly.mlottery.R;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.MyConstants;
import com.hhly.mlottery.util.NumberDataUtils;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.widget.DragGridView;
import com.hhly.mlottery.widget.DragGridView.OnChanageListener;
import com.umeng.analytics.MobclickAgent;

/**
 * @ClassName: CustomizeActivity
 * @Description: 定制采种界面
 * @author Tenney
 * @date 2015-10-24 下午9:47:31
 */
public class NumberCustomizeActivity extends Activity implements OnClickListener {
	private Context mContext;
	private ImageView public_btn_filter;
	private ImageView public_btn_set;
	private ImageView public_img_back;// 返回
	private TextView public_txt_title;// 标题

	private DragGridView lv_customize_ok;// 用户已定制彩种容器
	private GridView lv_customize_not;// 用户未定制彩种容器
	private TextView tv_customize_hint;// 用户未定制提示语
	private Button bt_customize_ok;// 确定定制
	private Button bt_customize_not;// 重置定制

	private List<Integer> sortDefs = new ArrayList<Integer>();// 可定制的彩种
	private List<Integer> sortDefsCopy = new ArrayList<Integer>();// 可定制的彩种还原备份

	private List<Integer> sortOks = new ArrayList<Integer>();// 用户已定制的彩种
	private List<Integer> sortOksCopy = new ArrayList<Integer>();// 用户已定制的彩种还原备份

	private customizeDefAdapter mDefAdapter;// 未定制Adapter
	private customizeOkAdapter mOkAdapter;// 已定制Adapter
	private String SpOks = null;// 用户定制

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		initView();
		initData();
		initEvent();

	}

	/**
	 * 事件处理
	 */
	private void initEvent() {
		lv_customize_not.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				MobclickAgent.onEvent(MyApp.getContext(),"Lottery_Customize_Not");
				// 添加用户已定制列表
				sortOks.add(sortDefs.get(arg2));
				mOkAdapter.notifyDataSetChanged();

				// 删除用户未定制列表
				sortDefs.remove(arg2);
				mDefAdapter.notifyDataSetChanged();

				if (null != sortOks) {
					if (sortOks.size() != 0) {
						tv_customize_hint.setVisibility(View.GONE);
						lv_customize_ok.setVisibility(View.VISIBLE);
					} else {
						tv_customize_hint.setVisibility(View.VISIBLE);
						lv_customize_ok.setVisibility(View.GONE);
					}
				}
			}
		});

		lv_customize_ok.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				MobclickAgent.onEvent(MyApp.getContext(),"Lottery_Customize_Ok");
				//Toast.makeText(NumberCustomizeActivity.this, AppConstants.numberNames[sortOks.get(arg2) - 1], 0).show();

				int index = 0;
				for (int i = 0; i < sortDefsCopy.size(); i++) {
					if (sortDefsCopy.get(i) == sortOks.get(arg2)) {
						index = i;
						break;
					}
				}

				if (null != sortDefs) {
					if (sortDefs.size() - 1 < index) {
						sortDefs.add(sortOks.get(arg2));
					} else {
						sortDefs.add(index, sortOks.get(arg2));
					}
					mDefAdapter.notifyDataSetChanged();
				}

				sortOks.remove(sortOks.get(arg2));
				mOkAdapter.notifyDataSetChanged();

				if (null != sortOks) {
					if (sortOks.size() != 0) {
						tv_customize_hint.setVisibility(View.GONE);
						lv_customize_ok.setVisibility(View.VISIBLE);
					} else {
						tv_customize_hint.setVisibility(View.VISIBLE);
						lv_customize_ok.setVisibility(View.GONE);
					}
				}
			}
		});
		
		// 拖动
		lv_customize_ok.setOnChangeListener(new OnChanageListener() {
			@Override
			public void onChange(int form, int to) {
				Integer temp = sortOks.get(form);
				
				//这里的处理需要注意下
				if(form < to){
					for(int i=form; i<to; i++){
						Collections.swap(sortOks, i, i+1);
					}
				}else if(form > to){
					for(int i=form; i>to; i--){
						Collections.swap(sortOks, i, i-1);
					}
				}
				sortOks.set(to, temp);
				mOkAdapter.notifyDataSetChanged();
			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		SpOks = PreferenceUtil.getString(MyConstants.NUMBEROKS, null);// 获取用户已定制彩种
		if (!TextUtils.isEmpty(SpOks)) {
			tv_customize_hint.setVisibility(View.GONE);
			lv_customize_ok.setVisibility(View.VISIBLE);

			String[] split = SpOks.split(",");
			for (int i = 0; i < split.length; i++) {
				sortOks.add(Integer.parseInt(split[i]));
			}

			for (Integer num : sortOks) {
				sortOksCopy.add(num);
			}
			
			String SpDefs = PreferenceUtil.getString(MyConstants.NUMBERDEFS, null);// 获取用户已未制彩种
			if (!TextUtils.isEmpty(SpDefs)) {
				String[] split2 = SpDefs.split(",");
				for (int i = 0; i < split2.length; i++) {
					sortDefs.add(Integer.parseInt(split2[i]));
				}
			}
			
		}else{
			sortDefs = (List<Integer>) getIntent().getSerializableExtra("DefSortList");
		}
		

		for (Integer num : sortDefs) {
			sortDefsCopy.add(num);
		}

		mDefAdapter = new customizeDefAdapter();
		lv_customize_not.setAdapter(mDefAdapter);

		mOkAdapter = new customizeOkAdapter();
		lv_customize_ok.setAdapter(mOkAdapter);

	}

	/**
	 * 用户已定制的彩种适配器
	 */
	private class customizeOkAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (null != sortOks) {
				return sortOks.size();
			}
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = View.inflate(NumberCustomizeActivity.this, R.layout.number_customize_item, null);
			TextView tv = (TextView) view.findViewById(R.id.tv_item);
//			tv.setText(AppConstants.numberNames[sortOks.get(position) - 1]);
			NumberDataUtils.setTextTitle(mContext,tv,String.valueOf(sortOks.get(position)));
			return view;
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

	/**
	 * 用户未定制适配器
	 */
	private class customizeDefAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (null != sortDefs) {
				return sortDefs.size();
			}
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(NumberCustomizeActivity.this, R.layout.number_customize_item, null);
			TextView tv = (TextView) view.findViewById(R.id.tv_item);
//			tv.setText(AppConstants.numberNames[sortDefs.get(position) - 1]);
			NumberDataUtils.setTextTitle(mContext,tv,String.valueOf(sortDefs.get(position)));
			return view;
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

	/**
	 * 初始化布局
	 */
	public void initView() {
		
		if("rCN".equals(MyApp.isLanguage) || "rTW".equals(MyApp.isLanguage)){
			setContentView(R.layout.number_custom_activity);// 国内
		}else{
			setContentView(R.layout.number_custom_activity_i18n);// 国外
		}
		
		public_btn_filter = (ImageView) findViewById(R.id.public_btn_filter);
		public_btn_filter.setVisibility(View.GONE);
		
		public_btn_set = (ImageView) findViewById(R.id.public_btn_set);
		public_btn_set.setVisibility(View.GONE);
		
		public_img_back = (ImageView) findViewById(R.id.public_img_back);
		public_img_back.setImageResource(R.mipmap.number_back_icon);
		public_img_back.setOnClickListener(this);
		
		public_txt_title = (TextView) findViewById(R.id.public_txt_title);
		public_txt_title.setText(getResources().getString(R.string.number_customize_title));

		lv_customize_ok = (DragGridView) findViewById(R.id.lv_customize_ok);
		tv_customize_hint = (TextView) findViewById(R.id.tv_customize_hint);
		lv_customize_not = (GridView) findViewById(R.id.lv_customize_not);

		bt_customize_ok = (Button) findViewById(R.id.bt_customize_ok);
		bt_customize_not = (Button) findViewById(R.id.bt_customize_not);
		bt_customize_ok.setOnClickListener(this);
		bt_customize_not.setOnClickListener(this);

		tv_customize_hint.setVisibility(View.VISIBLE);
		lv_customize_ok.setVisibility(View.GONE);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.public_img_back:// 返回
			MobclickAgent.onEvent(MyApp.getContext(),"Lottery_Customize_Exit");
			finish();
			break;
		case R.id.bt_customize_ok:// 保存定制
			MobclickAgent.onEvent(MyApp.getContext(),"Lottery_Customize_Save");
			//System.out.println("sortOks.size()=="+sortOks.size());

			if(sortOks.size() == 0){
				PreferenceUtil.commitString(MyConstants.NUMBEROKS, null);
			}else{
				StringBuilder oks = new StringBuilder();
				for (int i = 0; i < sortOks.size(); i++) {
					if(i == sortOks.size()-1){
						oks.append(sortOks.get(i));
					}else{
						oks.append(sortOks.get(i) + ",");
					}
				}
				PreferenceUtil.commitString(MyConstants.NUMBEROKS, oks.toString());
			}
			
			if(sortDefs.size() == 0){
				PreferenceUtil.commitString(MyConstants.NUMBERDEFS, null);
			}else{
				StringBuilder defs = new StringBuilder();
				for (int i = 0; i < sortDefs.size(); i++) {
					if(i == sortDefs.size()-1){
						defs.append(sortDefs.get(i));
					}else{
						defs.append(sortDefs.get(i) + ",");
					}
				}
				PreferenceUtil.commitString(MyConstants.NUMBERDEFS, defs.toString());
			}

			Toast.makeText(this, getResources().getString(R.string.number_save_success), Toast.LENGTH_SHORT).show();

			finish();
			break;
		case R.id.bt_customize_not:// 重置定制
			MobclickAgent.onEvent(MyApp.getContext(),"Lottery_Customize_Reset");
			// Toast.makeText(this, "重置", 0).show();
			sortOks.clear();
			if (!TextUtils.isEmpty(SpOks)) {
				for (Integer num : sortOksCopy) {
					sortOks.add(num);
				}
				tv_customize_hint.setVisibility(View.GONE);
				lv_customize_ok.setVisibility(View.VISIBLE);
			} else {
				tv_customize_hint.setVisibility(View.VISIBLE);
				lv_customize_ok.setVisibility(View.GONE);
			}
			mOkAdapter.notifyDataSetChanged();

			sortDefs.clear();
			for (Integer num : sortDefsCopy) {
				sortDefs.add(num);
			}
			mDefAdapter.notifyDataSetChanged();
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("NumberCustomizeActivity");
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("NumberCustomizeActivity");
	}
}
