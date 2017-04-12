package com.custom.view.project.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by 少东 on 2017/1/22.
 */

public class DependentBehavior extends CoordinatorLayout.Behavior<View> {

    public DependentBehavior() {
    }

    public DependentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     *
     * @param parent 当前的CoordinatorLayout
     * @param child 设置Behavior的View
     * @param dependency 是我们关心的那个View。如何知道关心的哪个呢？layoutDependsOn的返回值决定了一切！
     * @return
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof TextView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        // 先获取两个View的top值的差，然后让child的位置位移一下就ok啦
        int offset = dependency.getTop() - child.getTop() ;
        ViewCompat.offsetTopAndBottom(child, offset);
        return true;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }
}
