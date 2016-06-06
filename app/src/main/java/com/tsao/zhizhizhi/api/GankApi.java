package com.tsao.zhizhizhi.api;

import com.tsao.zhizhizhi.model.Gank;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Yvan on 2016/6/6.
 */
public interface GankApi {

    @GET("data/福利/10/{page}")
    Call<Gank> getGankGirl(@Path("page") int page);
    @GET("random/data/福利/20")
    Call<Gank> getRandomGirl();

}
