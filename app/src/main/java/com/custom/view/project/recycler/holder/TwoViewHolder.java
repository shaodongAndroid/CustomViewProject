package com.custom.view.project.recycler.holder;

import android.view.View;
import android.widget.TextView;

import com.custom.view.project.R;
import com.custom.view.project.recycler.adapter.MultiTypeAdapter;
import com.custom.view.project.recycler.model.Two;


/**
 * Created by yq05481 on 2017/1/3.
 */

public class TwoViewHolder extends BaseViewHolder<Two> {
    public TwoViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(Two model, int position, MultiTypeAdapter adapter) {
        TextView textView = (TextView) getView(R.id.two_title);
        textView.setText(model.getText());
    }


}
