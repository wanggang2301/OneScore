package com.hhly.mlottery.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.util.DateUtil;
import com.sohu.cyan.android.sdk.entity.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lzf
 * @ClassName:
 * @Description:
 * @date
 */
public class ChatballAdapter extends BaseQuickAdapter<Comment> {
    private List<String> mStringList = new ArrayList<>();
    private Activity mActivity;
    private String mString = "...";
    private PullUpLoading mPullUpLoading;
    private String total;

    public ChatballAdapter(int layoutResId, List<Comment> data, Activity activity) {
        super(layoutResId, data);
        mActivity = activity;
        this.total = mActivity.getResources().getString(R.string.toatalvisiable);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int positions) {
        super.onBindViewHolder(holder, positions);
        if (positions == getItemCount() - 1) {//已经到达列表的底部
            if (mPullUpLoading != null) {
                mPullUpLoading.onPullUpLoading();
            }
        }
    }

    public void setPullUpLoading(PullUpLoading pullUpLoading) {
        mPullUpLoading = pullUpLoading;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Comment comment) {
        baseViewHolder.setText(R.id.tv_nickname, comment.passport.nickname);
        baseViewHolder.setText(R.id.tv_time, DateUtil.transferLongToDate(comment.create_time));
        if (comment.content.length() > 50) {//字数大于50，则隐藏多于50的部分
            TextView view = baseViewHolder.getView(R.id.tv_time);
            TextView content = baseViewHolder.getView(R.id.tv_content);
            if (mStringList.size() != 0 && mStringList.contains(view.getText() + "")) {
                baseViewHolder.setText(R.id.tv_content, comment.content);
            } else {
                baseViewHolder.setText(R.id.tv_content, comment.content.substring(0, 49));
                SpanText(content, comment);
            }
        } else {
            baseViewHolder.setText(R.id.tv_content, comment.content);
        }
    }

    // 对textview部分字体加下划线， 颜色和添加点击事件
    public void SpanText(final TextView textView, final Comment comment) {
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
                        textView.setText(comment.content);
                        mStringList.add(DateUtil.transferLongToDate(comment.create_time));
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

    public interface PullUpLoading {
        void onPullUpLoading();
    }


}
