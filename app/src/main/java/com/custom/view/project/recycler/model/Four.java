package com.custom.view.project.recycler.model;

import com.custom.view.project.recycler.type.TypeFactory;

/**
 * Created by 少东 on 2017/1/12.
 */

public class Four implements Visitable {
    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
