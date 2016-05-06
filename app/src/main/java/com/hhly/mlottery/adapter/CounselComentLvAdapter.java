package com.hhly.mlottery.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.DateUtil;
import com.sohu.cyan.android.sdk.entity.Comment;

import java.util.List;

/**
 * @author lzf
 * @ClassName:
 * @Description: 新闻评论listview适配器
 * @date
 */
public class CounselComentLvAdapter extends BaseAdapter {
    private List<Comment> mInfosList;//
    private Activity mActivity;

    public CounselComentLvAdapter( Activity activity) {

        this.mActivity=activity;


    }

    public List<Comment> getInfosList() {
        return mInfosList;
    }

    public void setInfosList(List<Comment> infosList) {
        mInfosList = infosList;
    }

    @Override
    public int getCount() {

        return mInfosList==null?0:mInfosList.size() ;
    }

    @Override
    public Object getItem(int position) {

        return mInfosList==null?null:mInfosList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = mActivity.getLayoutInflater().inflate(R.layout.item_counsel_comment, null);
            holder.nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        //暂时写死游客
        holder.nickname.setText("游客");
        String time= DateUtil.transferLongToDate(mInfosList.get(position).create_time);
        holder.time.setText(time);
        holder.content.setText(mInfosList.get(position).content);

        return convertView;
    }

    static class Holder {
        TextView nickname;//用户名
        TextView time;//评论发表的时间
        TextView content;//评论的内容
    }
}
