package com.hhly.mlottery.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.BasketballDatabaseDetailsActivity;
import com.hhly.mlottery.bean.footballDetails.CounselBean;
import com.hhly.mlottery.util.ImageLoader;

import java.util.List;

/**
 * @author
 * @ClassName:
 * @Description: 资讯列表listview适配器
 * @date
 */
public class CounselFragmentLvAdapter extends BaseAdapter {
    private boolean isleft;
    private List<CounselBean.InfoIndexBean.InfosBean> mInfosList;//资讯列表数据集合父fg传来
    private Activity mActivity;

    public CounselFragmentLvAdapter( boolean isleft, List<CounselBean.InfoIndexBean.InfosBean> mInfosList,Activity activity) {
        this.isleft = isleft;
        this.mInfosList = mInfosList;
        this.mActivity=activity;


    }

    public List<CounselBean.InfoIndexBean.InfosBean> getInfosList() {
        return mInfosList;
    }

    public void setInfosList(List<CounselBean.InfoIndexBean.InfosBean> infosList) {
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
            convertView = mActivity.getLayoutInflater().inflate(R.layout.item_counselfragmentlv, null);
            holder.image_notleft_counselfragment_listviewitem = (ImageView) convertView.findViewById(R.id.image_notleft_counselfragment_listviewitem);
            holder.titlenoleft = (TextView) convertView.findViewById(R.id.titlenoleft);
            holder.leftimage = (ImageView) convertView.findViewById(R.id.leftimage);
            holder.lefttitle = (TextView) convertView.findViewById(R.id.lefttitle);
            holder.tv_tj = (TextView) convertView.findViewById(R.id.tv_tj);
            holder.tv_subtitle = (TextView) convertView.findViewById(R.id.tv_subtitle);
            holder.subtitle = (TextView) convertView.findViewById(R.id.subtitlenoleft);
            holder.tv_ybf = (TextView) convertView.findViewById(R.id.tv_ybf);
            holder.imagenotleft = (RelativeLayout) convertView.findViewById(R.id.imagenotleft);
            holder.imageleft = (RelativeLayout) convertView.findViewById(R.id.imageleft);
            holder.video_button= (ImageView) convertView.findViewById(R.id.video_button);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }

        if (isleft) {
            holder.imagenotleft.setVisibility(View.GONE);
            holder.imageleft.setVisibility(View.VISIBLE);
            holder.lefttitle.setText(mInfosList.get(position).getTitle());
            if (mInfosList.get(position).getSubTitle()!=null){
                holder.tv_subtitle.setText(mInfosList.get(position).getSubTitle());
                holder.tv_tj.setVisibility(View.VISIBLE);
            }else {
                holder.tv_subtitle.setText("");
                holder.tv_tj.setVisibility(View.GONE);
            }
            holder.tv_ybf.setText((mInfosList.get(position).getInfoSource()));
            //holder.leftimage.setTag(mInfosList.get(position).getPicUrl());
            //ImagaeLoader -- 加载图片
            ImageLoader.load(mActivity,mInfosList.get(position).getPicUrl(),R.mipmap.counse_icon).into(holder.leftimage);

        } else {
            holder.video_button.setVisibility(View.GONE);
            holder.imagenotleft.setVisibility(View.VISIBLE);
            holder.imageleft.setVisibility(View.GONE);
            holder.titlenoleft.setText(mInfosList.get(position).getTitle());
            if (TextUtils.isEmpty(mInfosList.get(position).getSubTitle())){
                holder.subtitle.setVisibility(View.GONE);
//                holder.subtitle.setText("实打实大师分恢复环境");
            }else {
                holder.subtitle.setVisibility(View.VISIBLE);
                holder.subtitle.setText(mInfosList.get(position).getSubTitle());
            }
            //holder.image_notleft_counselfragment_listviewitem.setTag(mInfosList.get(position).getPicUrl());
            //ImagaeLoader -- 加载图片
            ImageLoader.load(mActivity,mInfosList.get(position).getPicUrl(),R.mipmap.counsel_depth).into(holder.image_notleft_counselfragment_listviewitem);
            if(mInfosList.get(position).getIsvideonews()!=null&&mInfosList.get(position).getIsvideonews().equals("1")) { //视频的点击播放
                holder.video_button.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }

    static class Holder {
        //图片在左边时的item
        RelativeLayout imageleft;
        ImageView leftimage;//左边图片
        TextView lefttitle;//标题
        TextView tv_tj;//推荐
        TextView tv_subtitle;//副标题
        TextView tv_ybf;//新闻来源
        //图片不在左边时的item
        ImageView image_notleft_counselfragment_listviewitem;//图片
        TextView titlenoleft;//标题
        TextView subtitle;//副标题
        RelativeLayout imagenotleft;
        ImageView video_button;


    }
}
