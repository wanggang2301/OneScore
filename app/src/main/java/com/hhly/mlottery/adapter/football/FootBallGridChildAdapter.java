package com.hhly.mlottery.adapter.football;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.bean.footballDetails.database.DataBaseBean;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.StringUtils;

import java.util.List;

/**
 * 描述:  ${}
 * 作者:  wangg@13322.com
 * 时间:  2016/9/2 17:50
 */
public class FootBallGridChildAdapter extends BaseAdapter {

    private List<DataBaseBean> mList;
    private Context mContext;


    public FootBallGridChildAdapter(Context context, List<DataBaseBean> list) {
        this.mContext = context;
        this.mList = list;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MatchViewHolder mViewHolder;


        if (convertView == null) {
            mViewHolder = new MatchViewHolder();
            convertView = View.inflate(mContext, R.layout.basket_gridview_item_child, null);
            mViewHolder.icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            mViewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MatchViewHolder) convertView.getTag();
        }


        if (StringUtils.isEmpty(mList.get(position).getLgName())) {

            mViewHolder.icon.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.basket_info_default));
            mViewHolder.name.setText("");

        } else {

            if (mList.get(position).getPic() == null || "".equals(mList.get(position).getPic())) {
                mViewHolder.icon.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.basket_info_default));
            } else {
                ImageLoader.load(mContext, mList.get(position).getPic(), R.mipmap.basket_info_default).into(mViewHolder.icon);

            }

            mViewHolder.name.setText(mList.get(position).getLgName().toString());
        }


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
    }
}
