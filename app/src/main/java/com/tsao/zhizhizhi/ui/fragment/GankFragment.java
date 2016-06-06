package com.tsao.zhizhizhi.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tsao.zhizhizhi.api.GankApi;
import com.tsao.zhizhizhi.api.GankApiImpl;
import com.tsao.zhizhizhi.model.Gank;
import com.tsao.zhizhizhi.model.Result;
import com.zhizhizhi.yvan.zhizhizhi.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class GankFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    GankAdapter mGankAdapter;
    int mPage = 1;
    boolean isLoading = false;


    public GankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gank, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.gank_swiperefreshlayout);
        recyclerView = (RecyclerView)view.findViewById(R.id.gank_recycleview);
        //设置刷新监听
        swipeRefreshLayout.setOnRefreshListener(this);
        //设置刷新动画颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mGankAdapter = new GankAdapter();
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //设置适配器和布局管理器
        recyclerView.setAdapter(mGankAdapter);
        recyclerView.setLayoutManager(layoutManager);
        //下载数据
        loadGirlData();
        //上拉加载更多
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
                //先生成一个数组（长度为布局的列数）
                int[] spans = new int[layoutManager.getSpanCount()];
                //返回每列中最后可见的条目的位置，并存入数组中
                layoutManager.findLastVisibleItemPositions(spans);
                //取出数组中值最大的那一个就是最后可见的条目位置
                int max = spans[0];
                for (int value : spans) {
                    if (value > max) {
                        max = value;
                    }
                }
                int lastPosition = max;
                if (!isLoading && lastPosition == mGankAdapter.getItemCount() - 1) {
                    System.out.println("别拖了别拖了，，，，，好吗？？？？？？");
                    isLoading = true;
                    loadMoreData();
                }
            }
        });
    }

    private void loadGirlData(){
        System.out.println("loadGirlData");
        GankApi service = GankApiImpl.creatGankService();
        Call<Gank> call = service.getGankGirl(mPage);
        call.enqueue(new Callback<Gank>() {
            @Override
            public void onResponse(Call<Gank> call, Response<Gank> response) {
                List<Result> resultsList = response.body().getResults();
                //通知数据集改变（把数据传递出去）
                mGankAdapter.changeData(resultsList);
                //页数+1
                mPage = mPage + 1;

            }

            @Override
            public void onFailure(Call<Gank> call, Throwable t) {

            }
        });
    }

    private void loadMoreData(){
        System.out.println("loadMoreData");
        GankApi service = GankApiImpl.creatGankService();
        Call<Gank> call = service.getGankGirl(mPage);
        call.enqueue(new Callback<Gank>() {
            @Override
            public void onResponse(Call<Gank> call, Response<Gank> response) {
                List<Result> results = response.body().getResults();
                //数据集添加(将新获取的数据集传递出去)
                mGankAdapter.addItem(results);
                mPage = mPage+1;
                isLoading = false;

            }



            @Override
            public void onFailure(Call<Gank> call, Throwable t) {

            }
        });
    }

    private void loadRandomData(){
        System.out.println("loadRandomData");
        GankApi service = GankApiImpl.creatGankService();
        Call<Gank> call = service.getRandomGirl();
        call.enqueue(new Callback<Gank>() {
            @Override
            public void onResponse(Call<Gank> call, Response<Gank> response) {
                List<Result> results = response.body().getResults();
                mGankAdapter.changeData(results);
                //刷新成功，隐藏刷新动画
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Gank> call, Throwable t) {
                //刷新失败，不隐藏刷新动画
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onRefresh() {
        loadRandomData();
    }


    class GankAdapter extends RecyclerView.Adapter{

        List<Result> resultList = new ArrayList<Result>();
        @Override
        public int getItemCount() {
            return resultList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //获取LayoutInflater,将布局转换为View
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View gankItemView = layoutInflater.inflate(R.layout.gank_item,parent,false);
            GankItemViewHolder gankItemViewHolder = new GankItemViewHolder(gankItemView);
            return gankItemViewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if(holder instanceof GankItemViewHolder){
                GankItemViewHolder gankItemViewHolder = (GankItemViewHolder)holder;
                gankItemViewHolder.bindData(resultList.get(position));
            }
        }
        //获取数据集
        public void changeData(List<Result> lists){
            this.resultList = lists;
            notifyDataSetChanged();
        }

        //添加数据集
        public void addItem(List<Result> lists){
            this.resultList.addAll(lists);
            notifyDataSetChanged();
        }
    }

    static class GankItemViewHolder extends RecyclerView.ViewHolder{

        ImageView itemImage;
        public GankItemViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.iv_gank_item);
        }

        void bindData(Result result){
            Picasso.with(itemImage.getContext())
                    .load(result.getUrl())
                    .into(itemImage);
        }
    }

}


