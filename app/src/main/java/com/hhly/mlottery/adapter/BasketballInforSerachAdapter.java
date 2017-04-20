package com.hhly.mlottery.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.basket.infomation.LeagueBean;
import com.hhly.mlottery.view.SpecifiedTextsColorTextView;

import java.util.List;

/**
 * @ClassName: OneScoreGit
 * @author:Administrator luyao
 * @Description:  搜索adapter
 * @data: 2016/8/9 18:15
 */
public class BasketballInforSerachAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private List<LeagueBean> resultListBeen;
    private   SpecifiedTextsColorTextView mTv;
    private  Context mContext;
    private  String et_keyword;
    public BasketballInforSerachAdapter(Context context, List<LeagueBean> resultListBeen, String et_keyword) {
        super();
        this.mContext=context;
        this.resultListBeen = resultListBeen;
        this.et_keyword=et_keyword;
    }

    @Override
    public int getCount() {

        return resultListBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public void clearData(){
        resultListBeen.clear();
        notifyDataSetChanged();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
         inflater = LayoutInflater.from(mContext);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.basketball_serach_item, null);

        }
        mTv = (SpecifiedTextsColorTextView)convertView.findViewById(R.id.serach_item);
        mTv.setSpecifiedTextsColor(resultListBeen.get(position).getLeagueName(),et_keyword, Color.parseColor("#0090FF"));

           // mTv.setText(resultListBeen.get(position).leagueName);

/*
        //设置点击事件
        mTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void notifyDataSetChanged(View v) {
                Intent intent = new Intent(mContext, BasketballDatabaseDetailsActivity.class);
                intent.putExtra(LEAGUEID, resultListBeen.get(position).leagueId);//传递联赛ID
                mContext.startActivity(intent);
            }
        });
*/

        return convertView;
    }

}
