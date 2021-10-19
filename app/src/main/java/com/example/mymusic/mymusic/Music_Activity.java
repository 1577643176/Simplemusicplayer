package com.example.mymusic.mymusic;


import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import com.example.mymusic.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Music_Activity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "lzs";
    private static SeekBar sb;
    private static TextView tv_progress,tv_total,song_name;
    private ObjectAnimator animator;
    private String name;
    private Intent intent1;
    //    数据源
    private List<localMusicBean> mDatas1;
    private ImageView play1,last1,next1;
    private int num;
    private int id;
    private ImageView btn_xunhuang;
    private boolean aa=true;
    private boolean bb=true;
    private ImageView btn_shouc;
    private int musicI;
    private ImageView iv_music;
    private MediaPlayer player;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ActivityCollector.addActivity(this);
        //intent 通信
        intent1=getIntent();
        mDatas1=(List<localMusicBean>) getIntent().getSerializableExtra("data1");//接收歌曲集合
        num = intent1.getIntExtra("id2",0);//歌曲总数
        id = intent1.getIntExtra("id1",0);//点击歌曲id
        player=new MediaPlayer();//创建音乐播放器对象
        init(); //初始化布局控件
        Log.i(TAG, "onCreate: ");
    }
    private void init(){
        tv_progress=(TextView)findViewById(R.id.tv_progress);
        tv_total=(TextView)findViewById(R.id.tv_total);
        sb=(SeekBar)findViewById(R.id.sb);
        song_name=findViewById(R.id.song_name);
        play1=findViewById(R.id.btn_play);
        last1=findViewById(R.id.btn_last);
        next1=findViewById(R.id.btn_next);
        btn_xunhuang=findViewById(R.id.btn_xuhuang);
        btn_shouc=findViewById(R.id.btn_shouc);

        play1.setOnClickListener(this);
        last1.setOnClickListener(this);
        next1.setOnClickListener(this);
        btn_xunhuang.setOnClickListener(this);
        btn_shouc.setOnClickListener(this);

        if(id!=-1)
        {
            name=mDatas1.get(id).getSong();
            song_name.setText(name);
        }

        //为滑动条添加事件监听
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //进度条改变时，会调用此方法
                if (progress==seekBar.getMax()){//当滑动条到末端时，结束动画
                    animator.pause();//停止播放动画
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {//滑动条开始滑动时调用
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {//滑动条停止滑动时调用
                //根据拖动的进度改变音乐播放进度
                int progress=seekBar.getProgress();//获取seekBar的进度
                seekTo(progress);//改变播放进度
            }
        });
        iv_music=(ImageView)findViewById(R.id.iv_music);
        iv_music.setImageResource(frag1.icons); //设置专辑图片

        animator=ObjectAnimator.ofFloat(iv_music,"rotation",0f,360.0f);
        animator.setDuration(10000);//动画旋转一周的时间为10秒
        animator.setInterpolator(new LinearInterpolator());//匀速
        animator.setRepeatCount(-1);//-1表示设置动画无限循环
        if(id!=-1)
        animator.start();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_play://播放按钮点击事件
                if(id==-1)
                {
                    id=0;
                    play1.setImageResource(R.drawable.play1);
                    song_name.setText(mDatas1.get(id).getSong());
                    play(mDatas1.get(id).getPath());
                    animator.start();
                }else {
                    if(isPlay())
                    {
                        play1.setImageResource(R.drawable.pause1);
                        pausePlay();
                        animator.pause();
                    }else
                    {

                        play1.setImageResource(R.drawable.play1);
                        continuePlay();
                        if(animator.isPaused())
                            animator.resume();
                        else
                        {
                            animator.start();
                        }

                    }
                }
                break;
            case R.id.btn_last://上一首


                if(id<=0)
                {
                    Toast.makeText(this,"已经是第一首了，没有上一曲！",Toast.LENGTH_SHORT).show();
                }else
                {
                    if(isPlay())
                    {
                        animator.pause();
                        animator.clone();
                        id--;
                        song_name.setText(mDatas1.get(id).getSong());
                        play(mDatas1.get(id).getPath());
                        animator.start();
                    }else
                    {
                        animator.pause();
                        animator.clone();
                        play1.setImageResource(R.drawable.play1);
                        id--;
                        song_name.setText(mDatas1.get(id).getSong());
                        play(mDatas1.get(id).getPath());
                        animator.start();
                    }

                }
                break;
            case R.id.btn_next://下一首
                if(id>=num)
                {
                    Toast.makeText(this,"已经是最后一首了，没有下一曲！",Toast.LENGTH_SHORT).show();
                }else
                {
                    if(isPlay())
                    {
                        animator.pause();
                        animator.clone();
                        id++;
                        song_name.setText(mDatas1.get(id).getSong());
                        play(mDatas1.get(id).getPath());
                        animator.start();
                    }
                    else
                    {
                        animator.pause();
                        animator.clone();
                        id++;
                        song_name.setText(mDatas1.get(id).getSong());
                        play1.setImageResource(R.drawable.play1);
                        play(mDatas1.get(id).getPath());
                        animator.start();

                    }
                }
                break;
            case R.id.btn_xuhuang:
                 btn_xunhuang.setSelected(aa);
                if(aa==true)
                {
                    Toast.makeText(Music_Activity.this,"顺序播放",Toast.LENGTH_SHORT).show();
                    aa=false;
                }
                else
                {
                    Toast.makeText(Music_Activity.this,"单曲循环",Toast.LENGTH_SHORT).show();
                    aa=true;
                }
                 break;
            case R.id.btn_shouc:
                btn_shouc.setSelected(bb);
                if(bb==true)
                {
                    Toast.makeText(Music_Activity.this,"收藏成功",Toast.LENGTH_SHORT).show();
                    bb=false;
                }
                else
                {
                    Toast.makeText(Music_Activity.this,"取消收藏",Toast.LENGTH_SHORT).show();
                    bb=true;
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        Log.i(TAG, "onWindowFocusChanged: ");
        if (hasFocus)
        {
            Log.i("lsh123", "onWindowFocusChanged: "+isPlay());
            if(id!=-1)
            {
                song_name.setText(mDatas1.get(id).getSong());
                musicI=intent1.getIntExtra("musicI",-1);
                Log.i(TAG, "onWindowFocusChanged: "+musicI);
                if(musicI==0)
                {
                    play1.setImageResource(R.drawable.play1);
                    play(mDatas1.get(id).getPath());
                    animator.start();

                }
            }

        }
    }

    //在Musiv_Activity从后台重新切换到前台时，更新当前intent对象，已达到更新数据的目的
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
         intent1 = getIntent();
        id = intent1.getIntExtra("id1",-1);
        Log.i(TAG, "onNewIntent: "+id);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i("lsh123", "onDestroy1: ");
    }
    //让activity进入后台，不销毁
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown: ");
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent it = new Intent(Music_Activity.this,MainActivity1.class);
            startActivity(it);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void addTimer(){ //添加计时器用于设置音乐播放器中的播放进度条
        if(timer==null){
            timer=new Timer();//创建计时器对象
            TimerTask task=new TimerTask() {
                @Override
                public void run() {
                    if (player==null) return;
                    int duration=player.getDuration();//获取歌曲总时长
                    int currentPosition=player.getCurrentPosition();//获取播放进度
                    Message msg=Music_Activity.handler.obtainMessage();//创建消息对象
                    //将音乐的总时长和播放进度封装至消息对象中
                    Bundle bundle=new Bundle();
                    bundle.putInt("duration",duration);
                    bundle.putInt("currentPosition",currentPosition);
                    msg.setData(bundle);
                    //将消息发送到主线程的消息队列
                    Music_Activity.handler.sendMessage(msg);
                    Log.i("lsh123", "run: ");
                }
            };
            //开始计时任务后的5毫秒，第一次执行task任务，以后每500毫秒执行一次
            timer.schedule(task,5,500);
        }
    }

    //播放歌曲
        public void play(String str){//String path
            Log.i("lsh123", "play: "+str);
            Uri uri=Uri.parse(str+"");
            try{
                player.reset();//重置音乐播放器
                //加载多媒体文件
                Log.i("lsh123", "play: 123"+str);
                player=MediaPlayer.create(getApplicationContext(),uri);
                Log.i("lsh123", "play: "+str);
                player.start();//播放音乐
                addTimer();//添加计时器
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public boolean isPlay(){
            if(player.isPlaying())
                return true;
            else return false;
        }
        public void pausePlay(){
            player.pause();//暂停播放音乐
        }
        public void continuePlay(){
            player.start();//继续播放音乐
        }
        public void seekTo(int progress){
            player.seekTo(progress);//设置音乐的播放位置
        }


    public static Handler handler=new Handler(){//创建消息处理器对象
        //在主线程中处理从子线程发送过来的消息
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg){
            Bundle bundle=msg.getData();//获取从子线程发送过来的音乐播放进度
            int duration=bundle.getInt("duration");
            int currentPosition=bundle.getInt("currentPosition");
            sb.setMax(duration);
            sb.setProgress(currentPosition);
            //歌曲总时长
            int minute=duration/1000/60;
            int second=duration/1000%60;
            String strMinute=null;
            String strSecond=null;
            if(minute<10){//如果歌曲的时间中的分钟小于10
                strMinute="0"+minute;//在分钟的前面加一个0
            }else{
                strMinute=minute+"";
            }
            if (second<10){//如果歌曲中的秒钟小于10
                strSecond="0"+second;//在秒钟前面加一个0
            }else{
                strSecond=second+"";
            }
            tv_total.setText(strMinute+":"+strSecond);
            //歌曲当前播放时长
            minute=currentPosition/1000/60;
            second=currentPosition/1000%60;
            if(minute<10){//如果歌曲的时间中的分钟小于10
                strMinute="0"+minute;//在分钟的前面加一个0
            }else{
                strMinute=minute+" ";
            }
            if (second<10){//如果歌曲中的秒钟小于10
                strSecond="0"+second;//在秒钟前面加一个0
            }else{
                strSecond=second+" ";
            }
            tv_progress.setText(strMinute+":"+strSecond);
        }
    };

}


