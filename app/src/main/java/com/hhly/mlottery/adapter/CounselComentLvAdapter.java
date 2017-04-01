package com.hhly.mlottery.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.util.DateUtil;
import com.hhly.mlottery.util.L;
import com.sohu.cyan.android.sdk.entity.Comment;

import java.util.ArrayList;
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
    private String mString = "...";
    private String total;
    private List<String> mStringList = new ArrayList<>();

    public CounselComentLvAdapter(Activity activity) {

        this.mActivity = activity;
        this.total = mActivity.getResources().getString(R.string.toatalvisiable);

    }

    public List<Comment> getInfosList() {
        return mInfosList;
    }

    public void setInfosList(List<Comment> infosList) {
        mInfosList = infosList;
    }

    @Override
    public int getCount() {

        L.d("lzfdddd","getCount");

        return mInfosList == null ? 0 : mInfosList.size();
    }

    @Override
    public Object getItem(int position) {

        return mInfosList == null ? null : mInfosList.get(position);
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
            holder.nickname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ToastTools.showQuickCenter(mActivity, "nickname");
                }
            });
            holder.time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.nickname.setText(mInfosList.get(position).passport.nickname);
        String time = DateUtil.transferLongToDate(mInfosList.get(position).create_time);
        holder.time.setText(time);
        if (mInfosList.get(position).content.length() > 50) {//字数大于50，则隐藏多于50的部分
            if (mStringList.size() != 0 && mStringList.contains(holder.time.getText())) {
                holder.content.setText(mInfosList.get(position).content);
            } else {
                holder.content.setText(mInfosList.get(position).content.substring(0, 49));
                SpanText(holder.content, position);
            }
        } else {
            holder.content.setText(mInfosList.get(position).content);
        }
        return convertView;
    }

    static class Holder {
        TextView nickname;//用户名
        TextView time;//评论发表的时间
        TextView content;//评论的内容
    }

    // 对textview部分字体加下划线， 颜色和添加点击事件
    public void SpanText(final TextView textView, final int position) {
        SpannableString spString = new SpannableString(total);

        spString.setSpan(
                new ClickableSpan() {

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);

                        ds.setColor(mActivity.getResources().getColor(R.color.colorPrimary));
                        ds.setFakeBoldText(false);
//                        ds.setTextSize(28);
                        // ds.setLinearText(true);
                        ds.setUnderlineText(false);
                        ds.setAntiAlias(true);
                    }

                    @Override
                    public void onClick(View arg0) {
                        textView.setText(mInfosList.get(position).content);
                        mStringList.add(DateUtil.transferLongToDate(mInfosList.get(position).create_time));
//                        ToastTools.showQuickCenter(mActivity, "position=" + position);

                    }

                },
                0,
                total.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setHighlightColor(Color.TRANSPARENT);
        textView.append(mString);
        textView.append(spString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

    }
}
