package com.example.mymusic.mymusic;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mymusic.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

public class MainActivity1 extends AppCompatActivity implements View.OnClickListener {
    private FrameLayout content;
    private TextView tv1,tv2;
    private FragmentManager fm;
    private FragmentTransaction ft;

    CarouselView carouselView;

    private int[] images = new int[]{
            R.drawable.img11,
            R.drawable.img22,
            R.drawable.img33,
            R.drawable.img44,
            R.drawable.img55
    };
    private com.example.mymusic.mymusic.frag1 mFragmentConversation;
    private frag2 mFragmentConversation2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_music2);
        ActivityCollector.addActivity(this);
        carouselView = findViewById(R.id.carouselView);
        //设置轮播图的页面数量
        carouselView.setPageCount(images.length);
        carouselView.setImageListener(imageListener);
        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                //Toast.makeText(MainActivity1.this,position,Toast.LENGTH_SHORT).show();
            }
        });

        content=(FrameLayout)findViewById(R.id.content);

        tv1=(TextView)findViewById(R.id.menu1);
        tv2=(TextView)findViewById(R.id.menu2);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);

        fm=getSupportFragmentManager();//若是继承FragmentActivity，fm=getFragmentManger();
        ft=fm.beginTransaction();
        ft.replace(R.id.content,new com.example.mymusic.mymusic.frag1());//默认情况下Fragment1
        ft.commit();
        InitFragment();
        getSupportFragmentManager().beginTransaction()
                         .add(R.id.content, mFragmentConversation).commit();
    }

    private void InitFragment() {
                 mFragmentConversation = new com.example.mymusic.mymusic.frag1();
                 mFragmentConversation2 = new frag2();
             }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

            imageView.setImageResource(images[position]);
        }
    };

    @Override
    public void onClick(View v){
        ft=fm.beginTransaction();
        FragmentTransaction mTransaction = getSupportFragmentManager()
                         .beginTransaction();
        switch (v.getId()){
            case R.id.menu1:
                if (!getSupportFragmentManager().getFragments().contains(mFragmentConversation)) {
                    mTransaction.add(R.id.content, mFragmentConversation);
                }
                mTransaction.show(mFragmentConversation);
                HideOtherFragments(mFragmentConversation);
                break;
            case R.id.menu2:
                if (!getSupportFragmentManager().getFragments().contains(mFragmentConversation2)) {
                    mTransaction.add(R.id.content, mFragmentConversation2);
                }
                mTransaction.show(mFragmentConversation2);
                HideOtherFragments(mFragmentConversation2);
                break;
            default:
                break;
        }

        mTransaction.commit();

    }
    /*
       * 隐藏其他所有Fragment
    */
      private void HideOtherFragments(Fragment fragment) {
                 if (getSupportFragmentManager().getFragments() == null) {
                         return;
                     }
                for (Fragment fm : getSupportFragmentManager().getFragments()) {
                         if (fm != fragment) {
                                 getSupportFragmentManager().beginTransaction().hide(fm)
                                     .commit();
                               }
                    }
         }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

