package com.tsao.zhizhizhi.api;

import com.tsao.zhizhizhi.model.Story;
import com.tsao.zhizhizhi.model.TopStory;
import com.tsao.zhizhizhi.model.Zhihu;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Yvan on 2016/6/2.
 */
public class ZhihuApiImpl {

    public static final String BASE_URL = "http://news-at.zhihu.com/";

    private static Retrofit retrofit = new Retrofit.Builder()//获取Retrofit对象
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())//采用链式结构绑定Base url
            .build();//执行操作

    public static ZhihuApi createZhihuService(){
        return retrofit.create(ZhihuApi.class);
    }

    /*public static void getData(){

        List<Story> storyList;

        Retrofit retrofit = new Retrofit.Builder()//获取Retrofit对象
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())//采用链式结构绑定Base url
                .build();//执行操作
        //获取API接口的实现类的实例对象
        ZhihuApi service = retrofit.create(ZhihuApi.class);
        Call<Zhihu> call =  service.getZhihuLatestNews();
        call.enqueue(new Callback<Zhihu>() {
            @Override
            public void onResponse(Call<Zhihu> call, Response<Zhihu> response) {
                System.out.println("onResponse==================================");
                System.out.println(response.body().getDate());
                List<Story> storiesList = response.body().getStories();
                for(Story stories : storiesList){
                    List<String> imagesList = stories.getImages();
                    for(String imagesurl : imagesList){
                        System.out.println(imagesurl);
                    }
                    System.out.println("type:" + stories.getType());
                    System.out.println("id:" + stories.getId());
                    System.out.println("ga_prefix:" + stories.getGaPrefix());
                    System.out.println("title:" + stories.getTitle());
                }

                System.out.println("stories:");
                List<TopStory> top_storiesList = response.body().getTopStories();
                for(TopStory topStory : top_storiesList){
                    System.out.println("image:" + topStory.getImage());
                    System.out.println("type:" + topStory.getType());
                    System.out.println("id:" + topStory.getId());
                    System.out.println("ga_prefix:" + topStory.getGaPrefix());
                    System.out.println("title:" + topStory.getTitle());
                }
            }

            @Override
            public void onFailure(Call<Zhihu> call, Throwable t) {
                System.out.println("onFailure========================================");
            }
        });


    }*/

}