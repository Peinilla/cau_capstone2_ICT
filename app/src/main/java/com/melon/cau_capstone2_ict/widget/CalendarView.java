package com.melon.cau_capstone2_ict.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.melon.cau_capstone2_ict.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Set;

public class CalendarView extends ViewGroup {
    private final int screenWidth;
    private final int dateWidth;
    private final int dateHeight;

    private int dateOfWeek;
    private int maxDateOfMonth;
    private Context context;

    Calendar calendar;

    CalendarItemView child;

    public static String[] DAY_OF_WEEK;

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        dateWidth = screenWidth / 7;
        dateHeight = (int) (dateWidth * 0.75);
        DAY_OF_WEEK = getResources().getStringArray(R.array.day_of_week);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        int maxHeight = (int) (Math.ceil((count + dateOfWeek - 1) / 7d) * dateHeight);
        int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
        int childState = 0;

        setMeasuredDimension(resolveSizeAndState(screenWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, expandSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));

        LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int paddingLeft = this.getPaddingLeft();
        final int paddingTop = this.getPaddingTop();
        final int paddingRight = this.getMeasuredWidth() - this.getPaddingRight();
        int xPos = paddingLeft;
        int yPos = paddingTop;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            if ((xPos + dateWidth) >= paddingRight) {
                xPos = paddingLeft;
                yPos += dateHeight;
            }
            if (i == 7) {
                xPos = (dateOfWeek - 1) * dateWidth;
            }

            child.layout(xPos, yPos, xPos + dateWidth, yPos + dateHeight);

            xPos += dateWidth;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void initCalendar(int dayOfWeek, int maxDateOfMonth) {
        dateOfWeek = dayOfWeek;
        this.maxDateOfMonth = maxDateOfMonth;
    }

    public void setSelectedView(View view) {
        FloatingActionsMenu fab = getRootView().findViewById(R.id.fab_calendar);
        ViewPager pager = (ViewPager) getParent();
        View tag = (View) pager.getTag();

        if (tag != null) {
            long time = (long) tag.getTag();
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);

            for (int i = 0; i < pager.getChildCount(); i++) {
                for (int j = 7; j < getChildCount(); j++) {
                    child = (CalendarItemView) ((CalendarView) pager.getChildAt(i)).getChildAt(j);

                    if (child != null && child.getIsSameDay((long) child.getTag(), (long) tag.getTag())) {
                        //fab.collapse();
                        child.invalidate();
                        child = null;
                        break;
                    }
                }
            }
        }

        if (tag == view) {
            pager.setTag(null);
            return;
        }

        long time = (long) view.getTag();
        if(!fab.isExpanded()) {
            fab.expand();
        }
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        pager.setTag(view);
        view.invalidate();
    }
}
