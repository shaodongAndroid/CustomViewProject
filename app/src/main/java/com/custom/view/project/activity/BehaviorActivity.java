package com.custom.view.project.activity;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.custom.view.project.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BehaviorActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.dependent)
    TextView dependent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior);
        ButterKnife.bind(this);
        findViewById(R.id.dependent).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dependent:
                Log.d("info","-------------onClick -  activity_behavior");
                ViewCompat.offsetTopAndBottom(view, 5);
                break;
            case R.id.activity_behavior:
                break;
        }
    }
}
