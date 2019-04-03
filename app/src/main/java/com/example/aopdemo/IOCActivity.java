package com.example.aopdemo;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.aopdemo.base.BaseActivity;
import com.example.library.annation.ContentView;
import com.example.library.annation.InjectView;
import com.example.library.annation.OnClick;
import com.example.library.annation.OnLongClick;


@ContentView(R.layout.activity_ioc)
public class IOCActivity extends BaseActivity {
    @InjectView(R.id.button)
    private Button button;
    @InjectView(R.id.button1)
    private Button button1;
    @InjectView(R.id.button2)
    private Button button2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        button.setText("setText 单击事件");
        button1.setText("setText  长按事件");
        button2.setText("setText aop");
    }

    @OnClick(R.id.button)
    public void click(View view) {
        Toast.makeText(this, button.getText().toString(), Toast.LENGTH_LONG).show();
    }

    @OnLongClick(R.id.button1)
    public boolean longClick(View view) {
        Toast.makeText(this, button1.getText().toString(), Toast.LENGTH_LONG).show();
        return true;
    }
}
