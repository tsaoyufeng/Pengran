package com.tsao.zhizhizhi.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tsao.zhizhizhi.api.ZhihuApi;
import com.tsao.zhizhizhi.api.ZhihuApiImpl;
import com.tsao.zhizhizhi.model.Content;
import com.tsao.zhizhizhi.ui.fragment.ZhihuFragment;
import com.zhizhizhi.yvan.zhizhizhi.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoriesDetailActivity extends AppCompatActivity {

    ImageView mImageView;
    WebView mZhihuWebView;
    CollapsingToolbarLayout mCollapsingToolbarLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories_detail);

        Toolbar toolbar = (Toolbar)findViewById(R.id.storytoolbar);
        setSupportActionBar(toolbar);
        //添加向上操作，在左上角添加一个向左箭头当用户点击它时， 你的activity会接收一个对
        //onOptionsItemSelected()的调用。 操作的ID是 android.R.id.home 。
        //先要在Manifest文件中为Activity指定逻辑父Activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        //String title = intent.getStringExtra(ZhihuFragment.STORY_TITLE);
        int id = intent.getIntExtra(ZhihuFragment.STORY_ID,0);

        mImageView = (ImageView)findViewById(R.id.iv_zhihu_detail);
        mZhihuWebView = (WebView)findViewById(R.id.wb_zhihu_detail);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsinglayout);

        //下载数据
        ZhihuApi service = ZhihuApiImpl.createZhihuService();
        Call<Content> call = service.getZhihuNewsContent(id);
        call.enqueue(new Callback<Content>() {
            @Override
            public void onResponse(Call<Content> call, Response<Content> response) {
                System.out.println("文章内容获取成功");
                System.out.println(response.body().getShareUrl());
                //设置详情页顶部图片
                Picasso.with(mImageView.getContext())
                        .load(response.body().getImage())
                        .placeholder(R.mipmap.icon_image_default)
                        .into(mImageView);
                //设置详情页顶部图片标题
                mCollapsingToolbarLayout.setTitle(response.body().getTitle());
                //设置详情页顶部图片标题颜色
                mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
                String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
                String html = "<html><head>" + css + "</head><body>" + response.body().getBody() + "</body></html>";
                html = html.replace("<div class=\"img-place-holder\">", "");
                mZhihuWebView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);

            }

            @Override
            public void onFailure(Call<Content> call, Throwable t) {

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
