package com.custom.view.project.activity;

import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.custom.view.project.R;

public class DesignActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_cheeses, R.id.btn_bottom_sheet})
    @Override public void onClick(View v) {
        Intent intent ;
        switch (v.getId()){
            case R.id.btn_cheeses:
                intent = new Intent(this, CheeseActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_bottom_sheet:
                intent = new Intent(this, BottomSheetActivity.class);
                startActivity(intent);
                break;
        }
    }
}
