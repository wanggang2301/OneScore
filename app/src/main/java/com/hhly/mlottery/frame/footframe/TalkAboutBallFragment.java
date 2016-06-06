package com.hhly.mlottery.frame.footframe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hhly.mlottery.R;
import com.hhly.mlottery.frame.ChatFragment;

/**
 * @author hhly204
 * @ClassName: TalkAboutBallFragment
 * @Description: 聊球页面
 * @date 2016-6-2
 */
public class TalkAboutBallFragment extends Fragment implements View.OnClickListener{
    private View mView;
    private ProgressBar talkballpro;//客队Vs主队点赞的比例
    private ImageView ivHomeLike;//点赞主队
    private ImageView ivGuestLike;//点赞客队
    private TextView tvGuestLikeCount;//点赞客队数
    private TextView tvHomeLikeCount;//点赞主队数
    private String url;// 要显示的H5网址
    private String title;// 标题

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_talkaboutballfragment, null);
        initView();
        initChildFragment();
        return mView;
    }

    private void initChildFragment() {
        ChatFragment fragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString("souceid", url);
        bundle.putString("title", title);
        bundle.putBoolean("isHiddenCommentCount", false);
        fragment.setArguments(bundle);
        getChildFragmentManager().beginTransaction().add(R.id.sd, fragment).commit();
    }

    private void initView() {
        talkballpro = (ProgressBar) mView.findViewById(R.id.talkball_pro);
        ivHomeLike = (ImageView) mView.findViewById(R.id.talkball_like_anim_img);
        ivHomeLike = (ImageView) mView.findViewById(R.id.talkball_like_anim_img);
        tvHomeLikeCount = (TextView) mView.findViewById(R.id.talkball_like_count);
        tvGuestLikeCount = (TextView) mView.findViewById(R.id.talkbail_guest_like_count);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }
}
