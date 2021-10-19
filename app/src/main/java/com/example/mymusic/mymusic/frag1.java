package com.example.mymusic.mymusic;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mymusic.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class frag1 extends Fragment implements View.OnClickListener{
    private static final String TAG = "lzs";
    private View view;
    public String name;
    public static int icons=R.drawable.iuyuantu;
    private ImageView btn_music;
    RecyclerView musicRv;
    //    数据源
    private List<localMusicBean> mDatas;
    private LocalMusicAdapter2 adapter;
    private MediaPlayer mediaPlayer;
    private Intent it;
    public String addr=null;
    public int position1=-1;
    private int idd;
    private TextView t1,t2;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        // 可能初始化一个新实例的默认值的成员
        view=inflater.inflate(R.layout.music_list,container,false);
        musicRv = view.findViewById(R.id.local_music_rv1);
        mediaPlayer = new MediaPlayer();
        mDatas = new ArrayList<>();
        t1=view.findViewById(R.id.local_music_bottom_tv_song);
        t2=view.findViewById(R.id.local_music_bottom_tv_singer);
        btn_music=view.findViewById(R.id.local_music_icon);
        btn_music.setOnClickListener(this);
        //创建适配器对象
        adapter = new LocalMusicAdapter2(frag1.this.getContext(), mDatas);
        musicRv.setAdapter(adapter);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(frag1.this.getContext(), LinearLayoutManager.VERTICAL, false);
        musicRv.setLayoutManager(layoutManager);
        //加载本地数据源
        loadLocalMusicData();
        //设置每一项的点击事件
        setEventListener();
        Log.i(TAG, "onCreateView: "+position1);
        return view;
    }

    private void loadLocalMusicData() {
        /* 加载本地存储当中的音乐mp3文件到集合当中*/
        //1.获取ContentResolver对象
        ContentResolver resolver = frag1.this.getContext().getContentResolver();
        //2.获取本地音乐存储的Uri地址
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Log.i(TAG, "loadLocalMusicData: "+uri);
        //3 开始查询地址
        Cursor cursor = resolver.query(uri, null, null, null, null);
        Log.i(TAG, "loadLocalMusicData: "+cursor);
        //4.遍历Cursor
        int id = 0;
        while (cursor.moveToNext()) {
            String song = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            id++;
            String sid = String.valueOf(id);
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
            String time = sdf.format(new Date(duration));
            //获取专辑图片主要是通过album_id进行查询
            String album_id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            //将一行当中的数据封装到对象当中
            localMusicBean bean = new localMusicBean(sid, song, singer, album, time, path,"");
            mDatas.add(bean);
        }
        idd=id;
        Log.i(TAG, "loadLocalMusicData: "+mDatas);
        //数据源变化，提示适配器更新
        adapter.notifyDataSetChanged();
    }

    private void setEventListener() {
        /* 设置每一项的点击事件*/
        adapter.setOnItemClickListener(new LocalMusicAdapter2.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                position1 = position;
                Log.i(TAG, "OnItemClick: "+position1);
                addr=mDatas.get(position).getPath();
                t1.setText(mDatas.get(position).getSong());
                t2.setText(mDatas.get(position).getSinger());
                it = new Intent(frag1.this.getContext(), com.example.mymusic.mymusic.Music_Activity.class);
                it.putExtra("position",String.valueOf(1));
                it.putExtra("id1",position); //点击歌曲id
                it.putExtra("id2",idd); //歌曲总数
                it.putExtra("data1", (Serializable) mDatas); //歌曲集合
                it.putExtra("musicI",0); //传0表示用户点击的是歌曲列表里的歌曲，而不是返回播放界面
                startActivity(it);

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.local_music_icon:

                it = new Intent(frag1.this.getContext(), com.example.mymusic.mymusic.Music_Activity.class);
                it.putExtra("data1", (Serializable) mDatas);
                it.putExtra("musicI",1); //传1表示用户点击的是那个音箱图标，只是想返回播放界面，而不是要换歌
                it.putExtra("id2",idd); //歌曲总数
                it.putExtra("id1",position1); //传歌曲id
                startActivity(it);
                break;

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}



