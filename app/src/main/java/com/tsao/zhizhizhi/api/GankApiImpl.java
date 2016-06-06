package com.tsao.zhizhizhi.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Yvan on 2016/6/6.
 */
public class GankApiImpl {

    public static final String BASE_URL = "http://gank.io/api/";

    private static Retrofit retrofit = new Retrofit.Builder()//获取Retrofit对象
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())//采用链式结构绑定Base url
            .build();//执行操作

    public static GankApi creatGankService(){
        return retrofit.create(GankApi.class);
    }
}
