package com.hhly.mlottery.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.LeagueRoundInfo;

import java.util.List;

/**
 * @author longzhongfu
 * @ClassName: ChoiceWheelUtil
 * @Description: ChoiceWheelUtil打开轮次选择对话框
 * @date 2016-3-29 上午10:50:44
 */
public class ChoiceWheelUtil {
    private AlertDialog alertDialog;//显示轮数对话框
    private String url;
    private int wheelcount;//选中的轮数
    private Activity activity;
    private int screenwidth;//屏幕宽
    private int screenheight;//屏幕高
    private View view;//该对话框显示的视图
    private List<LeagueRoundInfo.DataBean> list;//接收跳转过来的数据
    String leagueType;//联赛类型
    private ChoiceAdaputer adapter;//listview适配器
    //	private TextView mTextView;
    Context context;

    public ChoiceWheelUtil(Context context, String leagueType, int wheelcount, Activity activity, List<LeagueRoundInfo.DataBean> list) {
        super();
        this.context=context;
        this.wheelcount = wheelcount;
        this.activity = activity;
        this.list = list;
        this.leagueType=leagueType;


    }

    // 提供接口供外部调用
    public void showChoiceWheelDialog() {
        initView();
        initWindow();

    }

    // 初始化视图
    private void initView() {
        alertDialog = new AlertDialog.Builder(activity,R.style.AlertDialog).create();
        screenwidth = DisplayUtil.getScreenWidth(activity);
        screenheight = DisplayUtil.getScreenHeight(activity);
        view = activity.getLayoutInflater().inflate(R.layout.dialog_choicewheel, null);
        ListView lv_dialog_choicewheel = (ListView) view.findViewById(R.id.lv_dialog_choicewheel);

        adapter = new ChoiceAdaputer();
        lv_dialog_choicewheel.setAdapter(adapter);

        lv_dialog_choicewheel.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.tv_choicewheel_ls_item);
                //发送广播通知更新ui
                Intent intent = new Intent("wheelcount");
                intent.putExtra("position", position);
                activity.sendBroadcast(intent);
                //传递选中的item的position给adaputer
                adapter.setSelectedPosition(position);
                //通知adaputer重新绘制
                adapter.notifyDataSetInvalidated();
//				Toast.makeText(activity, textView.getText(), Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
            }
        });

        //设置传递过来的轮数被选中
        lv_dialog_choicewheel.setSelection(wheelcount);
        //传递选中的item的position给adaputer
        adapter.setSelectedPosition(wheelcount);
        //通知adaputer重新绘制
        adapter.notifyDataSetInvalidated();


    }
    /**
     * 获取ListView的高度
     * @param
     * @return ListView的高度
     */
   /* */
   /* public int getTotalHeightofListView(ListView list) {
        //ListView的适配器
        ListAdapter mAdapter = list.getAdapter();
        if (mAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        //循环适配器中的每一项
        for (int i = 0; i < mAdapter.getCount(); i++) {
            //得到每项的界面view
            View mView = mAdapter.getView(i, null, list);
            L.d("mView","i = "+i);
            L.d("mView", "mView = " + mView);
            //得到一个view的大小

            mView.measure(0 , 0);

            L.d("mView", "mView = " + mView.getHeight());
            L.d("mView", "mView = " + mView.getMeasuredHeight());
//            //总共ListView的高度
//            totalHeight += mView.getMeasuredHeight();
        }
        return totalHeight;
    }*/

    // 设置对话框大小并显示
    private void initWindow() {
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setLayout(screenwidth * 2 / 3, screenheight * 1/2);
        // window.setGravity(Gravity.CENTER_VERTICAL |
        // Gravity.CENTER_HORIZONTAL);
        // window.setWindowAnimations(R.style.AnimBottom);
        alertDialog.setContentView(view);

      /*  //管理器
        WindowManager m = ((Activity) context).getWindowManager();
        //屏幕分辨率，获取屏幕宽、高用
        Display d = m.getDefaultDisplay();
        //获取对话框当前的参数值
        WindowManager.LayoutParams p = window.getAttributes();
        //宽度设置为屏幕的0.8
        p.width = (int) (d.getWidth() * 0.7);
        //获取ListView的高度和当前屏幕的0.6进行比较，如果高，就自适应改变
        if(getTotalHeightofListView(lv_dialog_choicewheel) > d.getHeight()*0.6){
            //得到ListView的参数值
            ViewGroup.LayoutParams params = lv_dialog_choicewheel.getLayoutParams();
            //设置ListView的高度是屏幕的一半
            params.height = (int) (d.getHeight()*0.5);
            //设置

            lv_dialog_choicewheel.setLayoutParams(params);
        }
        //设置Dialog的高度
        window.setAttributes(p);
        alertDialog.setContentView(view);*/
    }


    //为了让item选中时有颜色  才选用了此adaputer，不然用arrayadaputer就可以满足需求
    public class ChoiceAdaputer extends BaseAdapter {
        private int selectedPosition = list.size() - 1;// 选中的位置

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void setSelectedPosition(int position) {
            this.selectedPosition = position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = activity.getLayoutInflater().inflate(R.layout.dialog_choicewheel_ls_item, null);
                viewHolder.mTextView = (TextView) convertView.findViewById(R.id.tv_choicewheel_ls_item);
                viewHolder.rel_choicewheel_ls_item = (RelativeLayout) convertView.findViewById(R.id.rel_choicewheel_ls_item);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //被选中
            if (selectedPosition == position) {
//					convertView.setBackgroundColor(activity.getResources().getColor(R.color.bot));
                viewHolder.mTextView.setTextColor(Color.RED);
                viewHolder.rel_choicewheel_ls_item.setBackgroundColor(activity.getResources().getColor(R.color.bot));
            } else {//未选中
//				convertView.setBackgroundColor(Color.WHITE);
                viewHolder.rel_choicewheel_ls_item.setBackgroundColor(Color.WHITE);
                viewHolder.mTextView.setTextColor(activity.getResources().getColor(R.color.msg));
            }

            if("2".equals(leagueType)){

                viewHolder.mTextView.setText(list.get(position).getRound());
                viewHolder.mTextView.setTextSize(13);
                viewHolder.mTextView.setTextColor(context.getResources().getColor(R.color.msg));
            }else{


                viewHolder.mTextView.setText(context.getString(R.string.information_chdi_text)+list.get(position).getRound()+context.getString(R.string.information_chround_text));
                 viewHolder.mTextView.setTextSize(13);
                viewHolder.mTextView.setTextColor(context.getResources().getColor(R.color.msg));
            }


            return convertView;
        }

        class ViewHolder {
            TextView mTextView;
            RelativeLayout rel_choicewheel_ls_item;
        }
    }
}
