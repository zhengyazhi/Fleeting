package com.example.fleeting.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.example.fleeting.helper.ViewOffsetBehavior;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.List;

/**
 * 日历 Behavior
 */

public class CalendarScrollBehavior extends ViewOffsetBehavior<RecyclerView> {

    private int calendarHeight;

    public CalendarScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean layoutDependsOn(CoordinatorLayout parent, RecyclerView child, View dependency) {
        return dependency instanceof MaterialCalendarView;
    }

    protected void layoutChild(CoordinatorLayout parent, RecyclerView child, int layoutDirection) {
        super.layoutChild(parent, child, layoutDirection);
        if (calendarHeight == 0) {
            final List<View> dependencies = parent.getDependencies(child);
            for (int i = 0, z = dependencies.size(); i < z; i++) {
                View view = dependencies.get(i);
                if (view instanceof MaterialCalendarView) {
                    calendarHeight = view.getMeasuredHeight();
                }
            }
        }
        child.setTop(calendarHeight);
        child.setBottom(child.getBottom() + calendarHeight);
    }
}
