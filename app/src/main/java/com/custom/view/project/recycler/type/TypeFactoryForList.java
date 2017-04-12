package com.custom.view.project.recycler.type;

import android.view.View;

import com.custom.view.project.R;
import com.custom.view.project.recycler.holder.BaseViewHolder;
import com.custom.view.project.recycler.holder.FourViewHolder;
import com.custom.view.project.recycler.holder.NormalViewHolder;
import com.custom.view.project.recycler.holder.OneViewHolder;
import com.custom.view.project.recycler.holder.ThreeViewHolder;
import com.custom.view.project.recycler.holder.TwoViewHolder;
import com.custom.view.project.recycler.model.Four;
import com.custom.view.project.recycler.model.Normal;
import com.custom.view.project.recycler.model.One;
import com.custom.view.project.recycler.model.Three;
import com.custom.view.project.recycler.model.Two;

/**
 * Created by yq05481 on 2016/12/30.
 */

public class TypeFactoryForList implements TypeFactory {
    private final int TYPE_RESOURCE_ONE = R.layout.layout_item_one;
    private final int TYPE_RESOURCE_TWO = R.layout.layout_item_two;
    private final int TYPE_RESOURCE_THREE = R.layout.layout_item_three;
    private final int TYPE_RESOURCE_Four = R.layout.layout_item_three;
    private final int TYPE_RESOURCE_NORMAL = R.layout.layout_item_normal;
    @Override
    public int type(One one) {
        return TYPE_RESOURCE_ONE;
    }

    @Override
    public int type(Two one) {
        return TYPE_RESOURCE_TWO;
    }

    @Override
    public int type(Three one) {
        return TYPE_RESOURCE_THREE;
    }

    @Override
    public int type(Normal normal) {
        return TYPE_RESOURCE_NORMAL;
    }

    @Override
    public int type(Four four) {
        return TYPE_RESOURCE_Four;
    }

    @Override
    public BaseViewHolder createViewHolder(int type, View itemView) {

        if(TYPE_RESOURCE_ONE == type){
            return new OneViewHolder(itemView);
        }else if (TYPE_RESOURCE_TWO == type){
            return new TwoViewHolder(itemView);
        }else if (TYPE_RESOURCE_THREE == type){
            return new ThreeViewHolder(itemView);
        }else if (TYPE_RESOURCE_NORMAL == type){
            return new NormalViewHolder(itemView);
        } else if(TYPE_RESOURCE_Four == type){
            return new FourViewHolder(itemView);
        }

        return null;
    }
}
