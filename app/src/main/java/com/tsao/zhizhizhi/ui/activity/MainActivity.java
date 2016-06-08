package com.tsao.zhizhizhi.ui.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.tsao.zhizhizhi.ui.fragment.PictureFragment;
import com.tsao.zhizhizhi.ui.fragment.ReadingFragment;
import com.zhizhizhi.yvan.zhizhizhi.R;

public class MainActivity extends AppCompatActivity
         implements NavigationView.OnNavigationItemSelectedListener{

    private FragmentManager mFragmentManager;
    private ReadingFragment mReadingFragment;
    private PictureFragment mPictureFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //应用toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //设置点击左上角图标打开抽屉
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //动态添加抽屉的头
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        View navHeader = View.inflate(this, R.layout.nav_header_main, null);
        navigationView.addHeaderView(navHeader);

        //添加抽屉条目的点击事件
        navigationView.setNavigationItemSelectedListener(this);

        mReadingFragment = new ReadingFragment();
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame_layout, mReadingFragment);
        fragmentTransaction.commit();
       // mReadingFragment = new ReadingFragment();
        mPictureFragment = new PictureFragment();


    }


    //抽屉菜单的点击事件（点击时调用此方法）
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //获取点击条目的id
        int id = item.getItemId();
        if(id == R.id.nav_reading){
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_frame_layout, mReadingFragment);
            fragmentTransaction.commit();
            System.out.println("id == R.id.nav_reading");


        } else if (id == R.id.nav_gallery){
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_frame_layout, mPictureFragment);
            fragmentTransaction.commit();
            System.out.println("id == R.id.nav_gallery");

        }
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
