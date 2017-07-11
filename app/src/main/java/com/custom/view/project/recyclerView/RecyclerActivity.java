package com.custom.view.project.recyclerView;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.custom.view.project.R;

public class RecyclerActivity extends AppCompatActivity {

    private RecyclerView recyclerView ;
    float[] mNestedOffsets = new float[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recyclerView.setAdapter(new MyAdapter(this));
        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(recyclerView);
        recyclerView.addOnItemTouchListener(new SlideOnItemTouchListener(this));
        // recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
        //     @Override public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        //         Log.d("-----------onInterceptTouchEvent()");
        //         e.offsetLocation(mNestedOffsets[0], mNestedOffsets[1]);
        //         Log.d("-----------mNestedOffsets[0] = "+mNestedOffsets[0]);
        //         Log.d("-----------mNestedOffsets[1] = "+mNestedOffsets[1]);
        //         return true;
        //     }
        //
        //
        //     @Override public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        //         Log.d("-----------onTouchEvent()");
        //         int action = MotionEventCompat.getActionMasked(e);
        //         switch (e.getAction()){
        //             case MotionEvent.ACTION_DOWN:
        //
        //                 break;
        //         }
        //     }
        //
        //
        //     @Override public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        //
        //     }
        // });
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        private LayoutInflater mInflater ;
        MyAdapter(Context context){
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_recycler_layout, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 20;
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
