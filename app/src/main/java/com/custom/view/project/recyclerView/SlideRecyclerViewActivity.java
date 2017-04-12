package com.custom.view.project.recyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.custom.view.project.R;

import java.util.ArrayList;
import java.util.List;

public class SlideRecyclerViewActivity extends AppCompatActivity implements NestingRecyclerSliding.RecyclerTouchListenerHelper{

    RecyclerView mRecyclerView;
    MainAdapter mAdapter;
    String[] dialogItems;

    List<Integer> unClickRows, unSwipeRows ;
    private NestingRecyclerSliding nestingSliding; //onTouchListener
    private int openOptionsPosition;
    private OnActivityTouchListener touchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_recycler_view);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("RecyclerViewEnhanced");

        unClickRows = new ArrayList<>();
        unSwipeRows = new ArrayList<>();
        dialogItems = new String[25];
        for (int i = 0; i < 25; i++) {
            dialogItems[i] = String.valueOf(i + 1);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new MainAdapter(this, getData());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        nestingSliding = new NestingRecyclerSliding(this, mRecyclerView);
        nestingSliding
                .setIndependentViews(R.id.rowButton)
                .setViewsToFade(R.id.rowButton)
                .setClickable(new NestingRecyclerSliding.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        ToastUtil.makeToast(getApplicationContext(), "Row " + (position + 1) + " clicked!");
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {
                        ToastUtil.makeToast(getApplicationContext(), "Button in row " + (position + 1) + " clicked!");
                    }
                })
                .setLongClickable(true, new NestingRecyclerSliding.OnRowLongClickListener() {
                    @Override
                    public void onRowLongClicked(int position) {
                        ToastUtil.makeToast(getApplicationContext(), "Row " + (position + 1) + " long clicked!");
                    }
                })
                .setSwipeOptionViews(R.id.add, R.id.edit, R.id.change)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new NestingRecyclerSliding.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        String message = "";
                        if (viewID == R.id.add) {
                            message += "Add";
                        } else if (viewID == R.id.edit) {
                            message += "Edit";
                        } else if (viewID == R.id.change) {
                            message += "Change";
                        }
                        message += " clicked for row " + (position + 1);
                        ToastUtil.makeToast(getApplicationContext(), message);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerView.addOnItemTouchListener(nestingSliding); }

    @Override
    protected void onPause() {
        super.onPause();
        mRecyclerView.removeOnItemTouchListener(nestingSliding);
    }

    private List<RowModel> getData() {
        List<RowModel> list = new ArrayList<>(25);
        for (int i = 0; i < 25; i++) {
            list.add(new RowModel("Row " + (i + 1), "Some Text... "));
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recylcer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean currentState = false;
        if (item.isCheckable()) {
            currentState = item.isChecked();
            item.setChecked(!currentState);
        }
        switch (item.getItemId()) {
            case R.id.menu_swipeable:
                nestingSliding.setSwipeable(!currentState);
                return true;
            case R.id.menu_clickable:
                nestingSliding.setClickable(!currentState);
                return true;
            case R.id.menu_unclickableRows:
                showMultiSelectDialog(unClickRows, item.getItemId());
                return true;
            case R.id.menu_unswipeableRows:
                showMultiSelectDialog(unSwipeRows, item.getItemId());
                return true;
            case R.id.menu_openOptions:
                showSingleSelectDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showMultiSelectDialog(final List<Integer> list, final int menuId) {
        boolean[] checkedItems = new boolean[25];
        for (int i = 0; i < list.size(); i++) {
            checkedItems[list.get(i)] = true;
        }

        String title = "Select {} Rows";
        if (menuId == R.id.menu_unclickableRows) title = title.replace("{}", "Unclickable");
        else title = title.replace("{}", "Unswipeable");

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMultiChoiceItems(dialogItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked)
                            list.add(which);
                        else
                            list.remove(which);
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Integer[] tempArray = new Integer[list.size()];
                        if (menuId == R.id.menu_unclickableRows)
                            nestingSliding.setUnClickableRows(list.toArray(tempArray));
                        else
                            nestingSliding.setUnSwipeableRows(list.toArray(tempArray));
                    }
                });
        builder.create().show();
    }

    private void showSingleSelectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Open Swipe Options for row: ")
                .setSingleChoiceItems(dialogItems, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openOptionsPosition = which;
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nestingSliding.openSwipeOptions(openOptionsPosition);
                    }
                });
        builder.create().show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (touchListener != null) touchListener.getTouchCoordinates(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setOnActivityTouchListener(OnActivityTouchListener listener) {
        this.touchListener = listener;
    }

    private class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
        LayoutInflater inflater;
        List<RowModel> modelList;

        public MainAdapter(Context context, List<RowModel> list) {
            inflater = LayoutInflater.from(context);
            modelList = new ArrayList<>(list);
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.recycler_row, parent, false);
            return new MainViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MainViewHolder holder, int position) {
            holder.bindData(modelList.get(position));
        }

        @Override
        public int getItemCount() {
            return modelList.size();
        }

        class MainViewHolder extends RecyclerView.ViewHolder {

            TextView mainText, subText;

            public MainViewHolder(View itemView) {
                super(itemView);
                mainText = (TextView) itemView.findViewById(R.id.mainText);
                subText = (TextView) itemView.findViewById(R.id.subText);
            }

            public void bindData(RowModel rowModel) {
                mainText.setText(rowModel.getMainText());
                subText.setText(rowModel.getSubText());
            }
        }
    }
}
