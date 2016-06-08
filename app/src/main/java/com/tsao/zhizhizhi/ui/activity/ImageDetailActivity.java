package com.tsao.zhizhizhi.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tsao.zhizhizhi.util.Constant;
import com.zhizhizhi.yvan.zhizhizhi.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageDetailActivity extends AppCompatActivity {

    ImageView imageview;
    FloatingActionButton fab;
    BufferedOutputStream bos;
    String url;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        Toolbar toolbar = (Toolbar)findViewById(R.id.image_toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.RED);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageview = (ImageView)findViewById(R.id.iv_pic_detail);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPicFile(id);
                Toast.makeText(v.getContext(), "图片正在下载",Toast.LENGTH_SHORT).show();
                Picasso.with(imageview.getContext())
                        .load(url)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                try {
                                    bos.flush();
                                    bos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getApplicationContext(), "图片下载成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });

            }
        });

        Intent intent = getIntent();
        url = intent.getStringExtra(Constant.GANK_IMAGE_URL);
        id = intent.getStringExtra(Constant.GANK_IMAGE_ID);
        Picasso.with(imageview.getContext())
                .load(url)
                .into(imageview);


    }

    public void createPicFile(String id){
        //获取外存状态
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            File picDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "知知知了");
            //将file作为一个目录生成,名为知知知了，没有这句file就是一个文件，可以直接在file上进行读写
            picDir.mkdir();
            File file = new File(picDir, id + ".jpg");
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
