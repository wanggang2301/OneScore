package com.hhly.mlottery.adapter.football;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.FootballDatabaseDetailsActivity;
import com.hhly.mlottery.bean.footballDetails.database.DataBaseBean;
import com.hhly.mlottery.util.ImageLoader;

import java.util.List;

/**
 * 描述:  ${}
 * 作者:  wangg@13322.com
 * 时间:  2016/9/2 17:33
 */
public class FootBallInfoGridAdapter extends BaseAdapter {
    private static final String LEAGUE = "league";

    private List<DataBaseBean> mList;
    private Context mContext;


    public FootBallInfoGridAdapter(Context context, List<DataBaseBean> list) {
        this.mContext = context;
        this.mList = list;



    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MatchViewHolder mViewHolder;


        if (convertView == null) {
            mViewHolder = new MatchViewHolder();
            convertView = View.inflate(mContext, R.layout.basket_gridview_item, null);
            mViewHolder.rl = (RelativeLayout) convertView.findViewById(R.id.rl);
            mViewHolder.icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            mViewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MatchViewHolder) convertView.getTag();
        }

        if ("".equals(mList.get(position).getLgName())) {
            mViewHolder.icon.setImageDrawable(null);
            mViewHolder.name.setText("");
            mViewHolder.rl.setEnabled(false);


        } else {
            if (mList.get(position).getPic() == null || "".equals(mList.get(position).getPic())) {
                mViewHolder.icon.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.basket_info_default));
            } else {
                ImageLoader.load(mContext,mList.get(position).getPic(),R.mipmap.basket_info_default).into(mViewHolder.icon);
            }
            mViewHolder.name.setText(mList.get(position).getLgName());
            mViewHolder.rl.setEnabled(true);
        }

        mViewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FootballDatabaseDetailsActivity.class);
                intent.putExtra(LEAGUE, mList.get(position));
                intent.putExtra("isIntegral" , false);
                mContext.startActivity(intent);


            }
        });


        return convertView;
    }


    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private static class MatchViewHolder {

        private TextView name;
        private ImageView icon;
        private RelativeLayout rl;
    }
}
