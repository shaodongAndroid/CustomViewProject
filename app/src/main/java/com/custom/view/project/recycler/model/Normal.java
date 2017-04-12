package com.custom.view.project.recycler.model;

import com.custom.view.project.recycler.type.TypeFactory;

/**
 * Created by yq05481 on 2016/12/30.
 */

public class Normal implements Visitable{
    String text;

    public Normal(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
