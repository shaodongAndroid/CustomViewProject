package com.custom.view.project.activity;

import android.app.ProgressDialog;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.custom.view.project.R;
import com.custom.view.project.util.Log;

public class BottomSheetActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet);
        ButterKnife.bind(this) ;
    }

    @OnClick({R.id.btn_bottom_sheet, R.id.btn_bottom_dialog,R.id.btn_progress_dialog})
    @Override public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_bottom_sheet:
                showBehavior();
                break;
            case R.id.btn_bottom_dialog:
                Log.d("-------------showBottomSheetDialog()");
                showBottomSheetDialog();
                break;
            case R.id.btn_progress_dialog:
                // showProgressDialog();
                showDialog();
                break;
        }
    }


    private void showDialog() {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setText("感谢您的评价");
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        new AlertDialog.Builder(this).setView(textView).create().show();
    }


    private void showProgressDialog() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        // dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.show();
    }

    private void showBottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setTitle("提示");
        TextView textView = new TextView(this);
        textView.setText("hello 你好");
        bottomSheetDialog.setContentView(textView);
        bottomSheetDialog.show();
    }

    public void showBehavior(){
        BottomSheetBehavior behavior = BottomSheetBehavior.from(findViewById(R.id.scroll_View));
        Log.d("---------behavior.getState() = "+behavior.getState());
        if(behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }
}
