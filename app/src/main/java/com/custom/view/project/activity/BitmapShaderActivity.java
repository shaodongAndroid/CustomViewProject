package com.custom.view.project.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.custom.view.project.R;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BitmapShaderActivity extends AppCompatActivity {

    @BindView(R.id.tv_text_name)
    TextView tvName;
    @BindView(R.id.tv_gender)
    TextView tvGender ;

    @BindView(R.id.tv_age)
    TextView tvAge ;

    @BindString(R.string.about_link)
    String gender ;
    @BindColor(R.color.colorAccent)
    int color ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_shader);
//        tvName.setText("ButterKnife");
        tvGender.setText(gender);
        tvAge.setText("22");

//        View view = inflater.inflate(R.layout.fancy_fragment, container, false);
//        ButterKnife.bind(this, view);
//        return view;
    }

    @OnClick({R.id.tv_text_name, R.id.tv_gender})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.tv_text_name:
                Toast.makeText(this, "你的名字是?", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_gender:
                Toast.makeText(this, "女", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }


        class ViewHolder{
            @BindView(R.id.tvNumber) TextView tvNumber ;

            public ViewHolder(View view){
                ButterKnife.bind(this, view);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
