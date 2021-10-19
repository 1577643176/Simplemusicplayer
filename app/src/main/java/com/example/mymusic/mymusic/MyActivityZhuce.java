package com.example.mymusic.mymusic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymusic.R;

public class MyActivityZhuce extends AppCompatActivity {

    TextView tv;
    private static final String TAG="lzs";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        ActivityCollector.addActivity(this);
        tv=findViewById(R.id.tishi);

        Intent intent = getIntent();

        String id = intent.getStringExtra("id");
        String ps = intent.getStringExtra("ps");
        tv.setText("您好，欢迎您！"+id+"您的密码是："+ps);


    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    //隐式启动
    public void three(View view){
        Intent intent = new Intent(MyActivityZhuce.this,MyActivityLogin.class);
        startActivity(intent);

    }




}
