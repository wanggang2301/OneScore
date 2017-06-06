package com.hhly.mlottery.adapter.football;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhly.mlottery.R;
import com.hhly.mlottery.activity.ProductAdviceActivity;
import com.hhly.mlottery.bean.productadvice.ProductAdviceBean;
import com.hhly.mlottery.bean.productadvice.ProductUserLike;
import com.hhly.mlottery.callback.TheLikeOfProductAdviceListener;
import com.hhly.mlottery.config.BaseURLs;
import com.hhly.mlottery.util.AppConstants;
import com.hhly.mlottery.util.ImageLoader;
import com.hhly.mlottery.util.PreferenceUtil;
import com.hhly.mlottery.util.net.VolleyContentFast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描    述：
 * 作    者：mady@13322.com
 * 时    间：2016/12/9
 */
public class AdviceAdapter extends BaseQuickAdapter<ProductAdviceBean.DataEntity> {

    private Context context;
    /**
     * 点赞的接口
     */
    private TheLikeOfProductAdviceListener mProductAdviceListener;
    List<ProductAdviceBean.DataEntity> data;
    public AdviceAdapter(List<ProductAdviceBean.DataEntity> data,Context context) {
        super(R.layout.item_advice, data);
        this.data=data;
        this.context=context;
    }

    /**
     * 设置点赞的监听
     * @param listener
     */
    public void setUserLikeClickListener(TheLikeOfProductAdviceListener listener){
        mProductAdviceListener=listener;
    }
    @Override
    protected void convert(BaseViewHolder holder, final ProductAdviceBean.DataEntity dataEntity) {
        ImageView icon=holder.getView(R.id.advice_user_icon);
        holder.setText(R.id.advice_user_name,dataEntity.getNickName());
        holder.setText(R.id.advice_user_time,dataEntity.getSendTime().substring(0,10));
        holder.setText(R.id.advice_user_content,dataEntity.getContent());
        final TextView textView=holder.getView(R.id.advice_user_like);

        holder.setText(R.id.advice_user_like,dataEntity.getLikes()+"");

        if(context!=null){
            ImageLoader.load(context,dataEntity.getUserImg(),R.mipmap.center_head).into(icon);
        }
        if(dataEntity.getReplyContent()!=null&&dataEntity.getReplyContent()!=""){
            holder.getView(R.id.ll_product_has_answer).setVisibility(View.VISIBLE);
            holder.setText(R.id.advice_product_reply,dataEntity.getReplyContent());
        }else{
            holder.getView(R.id.ll_product_has_answer).setVisibility(View.GONE);
        }
        String likeId= PreferenceUtil.getString(ProductAdviceActivity.LIKE_IDS,"");
        String []idArray=likeId.split("[,]");

        //点赞
        final ImageView imgLike=holder.getView(R.id.advice_user_img_like);

        for(String id:idArray){
            if(id.equals(dataEntity.getId())){
                imgLike.setImageResource(R.mipmap.advice_like_red);
                imgLike.setTag(true);
                break;
            }else {
                imgLike.setImageResource(R.mipmap.advice_like_white);
                imgLike.setTag(false);
            }
        }

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isCheck= (boolean) v.getTag();
                if(isCheck){
                    //不请求后台
                }
                else {
                    imgLike.setImageResource(R.mipmap.advice_like_red);
                    imgLike.setTag(true);
                    textView.setText((dataEntity.getLikes()+1)+"");
                    //网络请求
                    addFocusId(dataEntity.getId());
                }
            }
        });
    }

    public  void addFocusId(String id) {
        String likeIds = PreferenceUtil.getString(ProductAdviceActivity.LIKE_IDS, "");
        String []idArray=likeIds.split("[,]");

        if ("".equals(likeIds)) {
            PreferenceUtil.commitString(ProductAdviceActivity.LIKE_IDS, id);
        } else {
            for(String hasid:idArray){
                if(hasid.equals(id)){
                    break;
                }else {
                    PreferenceUtil.commitString(ProductAdviceActivity.LIKE_IDS, likeIds + "," + id);
                }
            }
        }
//        Toast.makeText(context, likeIds, Toast.LENGTH_SHORT).show();
        String deviceId = AppConstants.deviceToken;
        String userId = "";
        if (AppConstants.register != null && AppConstants.register != null && AppConstants.register.getUser() != null) {
            userId = AppConstants.register.getUser().getUserId();
        }
        //thirdId
        String url = "http://192.168.33.45:8080/mlottery/core/feedback.addFeedBackLikes.do";
        Map<String, String> params = new HashMap<>();

        params.put("deviceId", deviceId);
        params.put("userId", userId);
        params.put("feedbackId",id);

        VolleyContentFast.requestJsonByGet(BaseURLs.PRODUCT_ADVICE_LIKE, params, new VolleyContentFast.ResponseSuccessListener<ProductUserLike>() {
            @Override
            public void onResponse(ProductUserLike like) {
                if(like.getResult()==500){
                    Toast.makeText(context, "点赞成功", Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyContentFast.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyContentFast.VolleyException exception) {

            }
        }, ProductUserLike.class);


    }



}