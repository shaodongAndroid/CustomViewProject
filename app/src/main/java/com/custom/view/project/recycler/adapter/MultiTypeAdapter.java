package com.custom.view.project.recycler.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.custom.view.project.recycler.holder.BaseViewHolder;
import com.custom.view.project.recycler.model.Visitable;
import com.custom.view.project.recycler.type.TypeFactory;
import com.custom.view.project.recycler.type.TypeFactoryForList;

import java.util.List;

/**
 * Created by yq05481 on 2016/12/30.
 */

public class MultiTypeAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private TypeFactory typeFactory;
    private List<Visitable> models;

    public MultiTypeAdapter(List<Visitable> models) {
        this.models = models;
        this.typeFactory = new TypeFactoryForList();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View itemView = View.inflate(context, viewType, null);
        return typeFactory.createViewHolder(viewType, itemView);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setUpView(models.get(position),position,this);
    }

    @Override
    public int getItemCount() {
        if(null == models){
            return  0;
        }
        return models.size();
    }

    @Override
    public int getItemViewType(int position) {
        return models.get(position).type(typeFactory);
    }


    @Override public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }
}
