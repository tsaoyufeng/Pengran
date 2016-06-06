package com.tsao.zhizhizhi.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.squareup.picasso.Picasso;
import com.tsao.zhizhizhi.api.ZhihuApi;
import com.tsao.zhizhizhi.api.ZhihuApiImpl;
import com.tsao.zhizhizhi.model.Story;
import com.tsao.zhizhizhi.model.TopStory;
import com.tsao.zhizhizhi.model.Zhihu;
import com.tsao.zhizhizhi.presenter.BannerHolderView;
import com.tsao.zhizhizhi.ui.activity.StoriesDetailActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zhizhizhi.yvan.zhizhizhi.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ZhihuFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecycleView;
    LinearLayoutManager mLinearLayoutManager;
    zhihuAdapter mZhihuAdapter;
    String date;
    boolean isLoading = false;
    public static final String STORY_ID = "storiesid";
    public static final String STORY_TITLE = "storiestitle";


    public ZhihuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zhihu, container, false);
        //return inflater.inflate(R.layout.fragment_zhihu, container, false);
        mRecycleView = (RecyclerView)view.findViewById(R.id.recycleview);
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swiperefreshlayout);
        //为SwipeRefreshLayout设置刷新动画颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        //为SwipeRefreshLayout添加刷新监听
        mSwipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("onActivityCreated");

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mZhihuAdapter = new zhihuAdapter();
        //设置适配器和布局管理器
        mRecycleView.setAdapter(mZhihuAdapter);
        mRecycleView.setLayoutManager(mLinearLayoutManager);
        //设置RecycleView的item之间的分割线
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext()).build());
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading && mLinearLayoutManager.findLastVisibleItemPosition() == mZhihuAdapter.getItemCount() - 1) {
                    System.out.println("别拖了别拖了，，，，，好吗？？？？？？");
                    isLoading = true;
                    loadBeforeData();
                }
            }
        });

        //加载网络数据
        loadLatestData();

    }

    //下拉刷新时调用
    @Override
    public void onRefresh() {
        loadLatestData();

    }

    //加载LatestData
    private void loadLatestData(){
        System.out.println("loadLatestData");
        ZhihuApi service = ZhihuApiImpl.createZhihuService();
        Call<Zhihu> call = service.getZhihuLatestNews();
        call.enqueue(new Callback<Zhihu>() {
            @Override
            public void onResponse(Call<Zhihu> call, Response<Zhihu> response) {
                System.out.println("onResponse==================================");
                //获取数据
                String date = response.body().getDate();
                List<Story> storyList = response.body().getStories();
                List<TopStory> topStoryList = response.body().getTopStories();
                //通知日期已改变
                changeDate(date);
                //通知数据已改变（将数据传递出去）
                mZhihuAdapter.changeData(storyList, topStoryList);
                //通知SwipeRefreshLayout刷新成功（隐藏刷新动画）
                mSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<Zhihu> call, Throwable t) {
                System.out.println("onFailure========================================");
                //通知SwipeRefreshLayout刷新失败(不隐藏刷新动画)
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    //加载BeforeData
    private void loadBeforeData(){
        System.out.println("loadBeforeData");
        ZhihuApi service = ZhihuApiImpl.createZhihuService();
        Call<Zhihu> call = service.getZhuhuBeforeNews(date);
        call.enqueue(new Callback<Zhihu>() {
            @Override
            public void onResponse(Call<Zhihu> call, Response<Zhihu> response) {
                List<Story> storyList = response.body().getStories();
                String currentdate = response.body().getDate();
                changeDate(currentdate);
                mZhihuAdapter.addItem(storyList);
                isLoading = false;//保证执行到此处才能再次执行loadBeforeData，否则，会在changeDate没执行之前就多次执行loadBeforeData，导致数据重复

            }

            @Override
            public void onFailure(Call<Zhihu> call, Throwable t) {

            }
        });

    }

    //改变日期的方法（）用来使BeforeData方法获取日期
    private void changeDate(String date){
        this.date = date;
    }

    //RecyclerView适配器
    class zhihuAdapter extends RecyclerView.Adapter{

        List<Story> storiesList = new ArrayList<Story>();
        List<TopStory> topStoriesList = new ArrayList<TopStory>();

        static final int ITEM_TYPE_HEADER = 0;
        static final int ITEM_TYPE_BODY = 1;


        @Override
        public int getItemCount() {

            return storiesList.size()+1;

        }

        //创建ViewHolder，ViewHolder为条目View的容器(View作为ViewHolder的变量存在)
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //获取LayoutInflater,并根据viewType将相应的布局转换为View
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(viewType == ITEM_TYPE_HEADER){
                //创建一个FragmentManager,调用ItemHeaderViewHolder时当作参数传递进去，（有用）
                FragmentManager fragmentManager = getFragmentManager();
                View itemHeaderView = layoutInflater.inflate(R.layout.zhihu_item_header, parent, false);
                //创建ITEM_TYPE_HEADER类型的ViewHolder并将其返回
                ItemHeaderViewHolder itemHeaderViewHolder = new ItemHeaderViewHolder(itemHeaderView);
                return itemHeaderViewHolder;


            }else if (viewType == ITEM_TYPE_BODY){
                View itemBodyView = layoutInflater.inflate(R.layout.zhihu_item_body, parent, false);
                //创建ITEM_TYPE_BODY类型的ViewHolder并将其返回
                ItemBodyViewHolder itemBodyViewHolder = new ItemBodyViewHolder(itemBodyView);
                return itemBodyViewHolder;
            }else {
                return null;
            }

        }
        //绑定ViewHolder,向控件中填充数据
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ItemHeaderViewHolder){
                ItemHeaderViewHolder itemHeaderViewHolder = (ItemHeaderViewHolder)holder;
                itemHeaderViewHolder.bindData(topStoriesList);


            } else if (holder instanceof ItemBodyViewHolder){
                ItemBodyViewHolder itemBodyViewHolder = (ItemBodyViewHolder)holder;
                itemBodyViewHolder.bindData(storiesList.get(position-1));
            }

        }

        //根据位置返回item类型
        @Override
        public int getItemViewType(int position) {
            if (position == 0){
                return ITEM_TYPE_HEADER;
            }
            return ITEM_TYPE_BODY;
            //return super.getItemViewType(position);
        }

        //更改数据集的方法
        public void changeData(List<Story> list,List<TopStory> toplist) {
            storiesList = list;
            topStoriesList = toplist;
            notifyDataSetChanged();
        }

        //添加数据集的方法
        public void addItem(List<Story> list){
            this.storiesList.addAll(list);
            notifyDataSetChanged();
        }

    }

    //itembody的控件容器
    static class ItemBodyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView itemDigest;
        ImageView itemImage;
        public ItemBodyViewHolder(View itemView) {
            super(itemView);
            itemDigest = (TextView)itemView.findViewById(R.id.item_digest);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            //为这组控件设置点击事件
            itemView.setOnClickListener(this);
        }

        //为控件绑定数据的方法
        void bindData(Story stories){
            itemView.setTag(stories);//为控件组设置标签
            itemDigest.setText(stories.getTitle());
            if(stories.getImages() != null && stories.getImages().size() != 0){
                Picasso.with(itemDigest.getContext())
                        .load(stories.getImages().get(0))
                        .placeholder(R.mipmap.icon_image_default)
                        .into(itemImage);
            }

        }

        @Override
        public void onClick(View v) {
            System.out.println("别点了别点了，烦不烦");
            Story story = (Story) v.getTag();//
            Intent intent = new Intent(v.getContext(), StoriesDetailActivity.class);
            intent.putExtra(STORY_ID,story.getId());
            //intent.putExtra(STORY_TITLE,story.getTitle());
            v.getContext().startActivity(intent);
        }
    }

    //itemheader的控件容器
    static class ItemHeaderViewHolder extends RecyclerView.ViewHolder{

        ConvenientBanner convenientBanner;

        public ItemHeaderViewHolder(View itemView) {
            super(itemView);
            convenientBanner = (ConvenientBanner)itemView.findViewById(R.id.convenientBanner);
        }

        //为Banner填充数据
        void bindData(List<TopStory> topLists){

            convenientBanner.setPages(new CBViewHolderCreator<BannerHolderView>() {
                @Override
                public BannerHolderView createHolder() {
                    return new BannerHolderView();
                }
            },topLists)
                    .setPageIndicator(new int[]{R.mipmap.ic_page_indicator,R.mipmap.ic_page_indicator_focused})
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
            convenientBanner.setScrollDuration(1000);//设置每次滑动在多少秒内完成
            convenientBanner.startTurning(5000);//设置每5秒滑动一次
        }

    }



}
