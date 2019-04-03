package com.example.aopdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.aopdemo.aspectj.annotation.BehaviorTrace;
import com.example.aopdemo.aspectj.annotation.CheckNet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private Button mButton;
    private Button mButton1;
    private Button mButton2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mButton = (Button) findViewById(R.id.button);
        mButton1 = (Button) findViewById(R.id.button1);
        mButton2 = (Button) findViewById(R.id.button2);

        mButton.setOnClickListener(this);
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                click(v);
                break;
            case R.id.button1:
                shake(v);
                break;
            case R.id.button2:
                startActivity(new Intent(this, IOCActivity.class));
                break;
        }
    }

    /**
     * Aop 网络检测
     *
     * @param view
     */
    @CheckNet
    public void click(View view) {
        //正常的业务逻辑
        Toast.makeText(this, "网络已连接", Toast.LENGTH_LONG).show();
    }

    @BehaviorTrace(value = "摇一摇", type = 1)
    public void shake(View view) {
        {
            SystemClock.sleep(3000);
            Log.i(TAG, " 摇到一个红包");

        }
    }
}