package com.tsao.zhizhizhi.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.squareup.picasso.Picasso;
import com.tsao.zhizhizhi.model.TopStory;
import com.tsao.zhizhizhi.ui.activity.StoriesDetailActivity;
import com.tsao.zhizhizhi.ui.fragment.ZhihuFragment;
import com.zhizhizhi.yvan.zhizhizhi.R;

/**
 * Created by Yvan on 2016/6/4.
 */
public class BannerHolderView implements Holder<TopStory> {

    private View view;
    @Override
    public View createView(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.banner_item_header,null);
        return view;
    }

    @Override
    public void UpdateUI(Context context, int position, final TopStory data) {

        ImageView imageView = (ImageView)view.findViewById(R.id.iv_item_header);
        TextView textView = (TextView)view.findViewById(R.id.tv_item_header);
        Picasso.with(view.getContext())
                .load(data.getImage())
                .into(imageView);
        textView.setText(data.getTitle());

        //添加点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), StoriesDetailActivity.class);
                intent.putExtra(ZhihuFragment.STORY_ID,data.getId());
                //intent.putExtra(STORY_TITLE,story.getTitle());
                v.getContext().startActivity(intent);

            }
        });

    }
}
