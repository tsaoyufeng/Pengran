package com.tsao.zhizhizhi.api;


import android.database.Observable;

import com.tsao.zhizhizhi.model.Content;
import com.tsao.zhizhizhi.model.Zhihu;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Yvan on 2016/6/1.
 */
public interface ZhihuApi {

    @GET("api/4/news/latest")
    Call<Zhihu> getZhihuLatestNews();

    @GET("api/4/news/{id}")
    Call<Content> getZhihuNewsContent(@Path("id") int id);

    @GET("api/4/news/before/{data}")
    Call<Zhihu> getZhuhuBeforeNews(@Path("data") String data);
}
