package com.custom.view.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import com.android.debug.hv.ViewServer;
import com.custom.view.project.Launch.LaunchAnimationActivity;
import com.custom.view.project.activity.AnnotationActivity;
import com.custom.view.project.activity.BehaviorActivity;
import com.custom.view.project.activity.BitmapShaderActivity;
import com.custom.view.project.activity.CheeseActivity;
import com.custom.view.project.activity.DesignActivity;
import com.custom.view.project.activity.HttpClientActivity;
import com.custom.view.project.activity.MosaicActivity;
import com.custom.view.project.activity.RegionActivity;
import com.custom.view.project.activity.RetrofitActivity;
import com.custom.view.project.activity.RxActivity;
import com.custom.view.project.activity.SlindeActivity;
import com.custom.view.project.progress.FlikerProgressActivity;
import com.custom.view.project.puzzle.activity.PuzzleActivity;
import com.custom.view.project.recyclerView.RecyclerActivity;
import com.custom.view.project.recyclerView.SlideRecyclerViewActivity;
import com.custom.view.project.rule.RuleActivity;
import com.custom.view.project.service.ServiceActivity;
import com.custom.view.project.sticker.StickerViewActivity;
import com.custom.view.project.webview.WebViewActivity;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * View一般用于绘制静态页面或者界面元素跟随用户的操作(点击、拖拽等)而被动的改变位置、大小等
       SurfaceView一般用于无需用户操作，界面元素就需要不断的刷新的情况（例如打飞机游戏不断移动的背景）
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initListener();
        ViewServer.get(this).addWindow(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
    }

    private void initListener() {
        findViewById(R.id.btn_mosaic).setOnClickListener(this);
        findViewById(R.id.btn_sticker).setOnClickListener(this);
        findViewById(R.id.btn_flicker_progress).setOnClickListener(this);
        findViewById(R.id.btn_launch_animation).setOnClickListener(this);
        findViewById(R.id.btn_design).setOnClickListener(this);
        findViewById(R.id.btn_puzzle).setOnClickListener(this);
        findViewById(R.id.btn_rule).setOnClickListener(this);
        findViewById(R.id.btn_slide_recycler).setOnClickListener(this);
        findViewById(R.id.btn_recycler_list_type).setOnClickListener(this);
        findViewById(R.id.btn_webView).setOnClickListener(this);
        findViewById(R.id.btn_service).setOnClickListener(this);
        findViewById(R.id.btn_alipay).setOnClickListener(this);
        findViewById(R.id.btn_ok_http).setOnClickListener(this);
        findViewById(R.id.btn_bitmap_shader).setOnClickListener(this);
        findViewById(R.id.btn_rx_java).setOnClickListener(this);
        findViewById(R.id.btn_retrofit).setOnClickListener(this);
        findViewById(R.id.btn_behavior).setOnClickListener(this);
        findViewById(R.id.btn_region).setOnClickListener(this);
        findViewById(R.id.btn_annotation).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent ;
        switch (v.getId()){
            case R.id.btn_mosaic:
                intent = new Intent(this, MosaicActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_sticker:
                intent = new Intent(this, StickerViewActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_flicker_progress:
                intent = new Intent(this, FlikerProgressActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_launch_animation:
                intent = new Intent(this, LaunchAnimationActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_design:
                intent = new Intent(this, DesignActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_puzzle:
                intent = new Intent(this, PuzzleActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_rule:
                intent = new Intent(this, RuleActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_slide_recycler:
                intent = new Intent(this, SlideRecyclerViewActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_recycler_list_type:
//                intent = new Intent(this, RecyclerViewActivity.class);
                intent = new Intent(this, RecyclerActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_service:
                intent = new Intent(this, ServiceActivity.class);
                startActivity(intent);
                break;
            // case R.id.btn_intent_service:
            //     intent = new Intent(this, MyIntentServiceActivity.class);
            //     startActivity(intent);
            //     break;
            case R.id.btn_webView:
                intent = new Intent(this, WebViewActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_alipay:
                intent = new Intent(this, SlindeActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_ok_http:
                intent = new Intent(this, HttpClientActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_bitmap_shader:
                intent = new Intent(this, BitmapShaderActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_retrofit:
                intent = new Intent(this, RetrofitActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_rx_java:
                intent = new Intent(this, RxActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_behavior:
                intent = new Intent(this, BehaviorActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_region:
                intent = new Intent(this, RegionActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_annotation:
                intent = new Intent(this, AnnotationActivity.class);
                startActivity(intent);
                break;
        }
    }

    @OnItemClick
    public void each(){
        ArrayList<String> tempList = new ArrayList<>();
        // tempList.stream().allMatch();
    }

    public TextView getTextView(){

        ButterKnife.apply(new ArrayList<View>(), action, action1);

        TextView textView = new TextView(this);
        ButterKnife.apply(textView, action);

        ButterKnife.apply(new ArrayList<View>(), setter, "");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
        textView.setLayoutParams(params);
        return textView ;
    }

    private ButterKnife.Setter<View, String> setter = new ButterKnife.Setter<View, String>() {
        @Override public void set(@NonNull View view, String s, int i) {

        }
    };

    private ButterKnife.Action<View> action = new ButterKnife.Action() {
        @Override public void apply(@NonNull View view, int i) {

        }
    };

    private ButterKnife.Action<View> action1 = new ButterKnife.Action() {
        @Override public void apply(@NonNull View view, int i) {

        }
    };

    private ButterKnife.Action<TextView> action2 = new ButterKnife.Action<TextView>() {
        @Override public void apply(@NonNull TextView textView, int i) {
            textView.setText("Action");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }
}
