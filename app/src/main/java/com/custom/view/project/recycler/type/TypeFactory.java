package com.custom.view.project.recycler.type;

import android.view.View;

import com.custom.view.project.recycler.holder.BaseViewHolder;
import com.custom.view.project.recycler.model.Four;
import com.custom.view.project.recycler.model.Normal;
import com.custom.view.project.recycler.model.One;
import com.custom.view.project.recycler.model.Three;
import com.custom.view.project.recycler.model.Two;


/**
 * Created by yq05481 on 2016/12/30.
 */

public interface TypeFactory {
    int type(One one);

    int type(Two two);

    int type(Three three);

    int type(Normal normal);

    int type(Four four);

    BaseViewHolder createViewHolder(int type, View itemView);
}
