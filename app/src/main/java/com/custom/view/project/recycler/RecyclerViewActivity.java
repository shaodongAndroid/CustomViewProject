package com.custom.view.project.recycler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.custom.view.project.R;
import com.custom.view.project.recycler.adapter.MultiTypeAdapter;
import com.custom.view.project.recycler.model.Normal;
import com.custom.view.project.recycler.model.One;
import com.custom.view.project.recycler.model.Three;
import com.custom.view.project.recycler.model.Two;
import com.custom.view.project.recycler.model.Visitable;
import java.util.ArrayList;
import java.util.List;

/**
 * 关于 Android Adapter，你的实现方式可能一直都有问题
 * http://www.jianshu.com/p/c6a44e18badb
 * http://www.jianshu.com/u/6ca395ea7e3e
 */
public class RecyclerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Visitable> list = getData();
        list.add(0, new One("Type One 0"));
        list.add(1, new Two("Type Two 0"));
        list.add(2, new Three("Type Three 0"));
        list.add(new One("Type One 1"));

        recyclerView.setAdapter(new MultiTypeAdapter(list));

        LinearLayoutManager manager = new LinearLayoutManager(this){
            @Override
            public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
                super.onMeasure(recycler, state, widthSpec, heightSpec);
            }
        };
        manager.setAutoMeasureEnabled(false);
    }

    private List<Visitable> getData(){
        List<Visitable> models = new ArrayList<>();

        for (int index = 0; index < 50; index++ ){
            models.add(new Normal("Type normal "+ index));
        }

        return models;
    }
}
