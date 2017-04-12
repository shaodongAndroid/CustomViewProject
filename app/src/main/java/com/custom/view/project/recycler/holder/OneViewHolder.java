package com.custom.view.project.recycler.holder;

import android.view.View;
import android.widget.TextView;

import com.custom.view.project.R;
import com.custom.view.project.recycler.adapter.MultiTypeAdapter;
import com.custom.view.project.recycler.model.One;


/**
 * Created by yq05481 on 2017/1/3.
 */

public class OneViewHolder extends BaseViewHolder<One> {
    public OneViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setUpView(One model, int position, MultiTypeAdapter adapter) {
        TextView textView = (TextView) getView(R.id.one_title);
        textView.setText(model.getText());
    }
}
