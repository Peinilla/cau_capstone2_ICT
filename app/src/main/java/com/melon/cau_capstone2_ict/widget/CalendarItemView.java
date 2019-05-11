package com.melon.cau_capstone2_ict.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.common.BitArray;
import com.melon.cau_capstone2_ict.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarItemView extends View {

    Paint mPaintTextDate = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintTextToday = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackgroundToday = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackgroundTodaySelected = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackgroundSelected = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackgroundEvent = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int dayOfWeek = -1;
    private long todayMillis;
    private boolean isStaticText = false;
    private int dp11;
    private int dp16;
    private int dp1;
    private final float RADIUS = 50f;
    private boolean isEvent = false;
    private static CalendarItemView instance = null;

    CalendarView calendarView;
    ViewPager parent;
    CalendarItemView tagView;

    public CalendarItemView(Context context) {
        super(context);
        initialize();
    }

    @Override
    public View getRootView() {
        return super.getRootView();
    }

    private void initialize() {
        dp1 = (int) (1 * getContext().getResources().getDisplayMetrics().density);
        dp11 = (int) (11 * getContext().getResources().getDisplayMetrics().density);
        dp16 = (int) (16 * getContext().getResources().getDisplayMetrics().density);

        mPaintTextDate.setColor(Color.BLACK);
        mPaintTextDate.setTextSize(dp11);
        mPaintTextDate.setTextAlign(Paint.Align.CENTER);
        mPaintTextToday.setColor(Color.RED);
        mPaintTextToday.setTextSize(dp11);
        mPaintTextToday.setTextAlign(Paint.Align.CENTER);
        mPaintBackgroundToday.setColor(ContextCompat.getColor(getContext(), R.color.today));
        mPaintBackgroundTodaySelected.setColor(ContextCompat.getColor(getContext(), R.color.todaySelected));
        mPaintBackgroundSelected.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mPaintBackgroundEvent.setColor(ContextCompat.getColor(getContext(), R.color.pink));

        setClickable(true);

        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (!getIsStaticText()) {
                        ((CalendarView) getParent()).setSelectedView(view);

                        TextView textView = getRootView().findViewById(R.id.text_calendar_date);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
                        Date date = new Date();
                        date.setTime(todayMillis);
                        textView.setText(dateFormat.format(date));

                        return true;
                    }
                }
                return false;
            }
        });
        setPadding(30, 0, 30, 0);
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int xPos = (int) (canvas.getWidth() * 0.5);
        int yPos = (int) ((canvas.getHeight() / 2) - ((mPaintTextDate.descent() + mPaintTextDate.ascent()) * 0.5));
        RectF rectF = new RectF(xPos - dp16, getHeight() / 2 - dp16, xPos + dp16, getHeight() / 2 + dp16);
        RectF rectO = new RectF(xPos - (dp1 * 3), getHeight() / 2 - (dp1 * 10), xPos + dp1 * 3, getHeight() / 2 - (dp1 * 16));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(todayMillis);

        calendarView = (CalendarView) getParent();
        parent = (ViewPager) calendarView.getParent();
        tagView = (CalendarItemView) parent.getTag();

        if (getIsStaticText()) {
            canvas.drawText(CalendarView.DAY_OF_WEEK[dayOfWeek], xPos, yPos, mPaintTextDate);
        } else {
            if (getIsToday()) {
                canvas.drawText(calendar.get(Calendar.DATE) + "", xPos, yPos, mPaintTextToday);

                if (getIsSelected()) {
                    if (getIsSameDay(todayMillis, (long) tagView.getTag())) {
                        canvas.drawRoundRect(rectF, RADIUS, RADIUS, mPaintBackgroundToday);
                        canvas.drawText(calendar.get(Calendar.DATE) + "", xPos, yPos, mPaintTextDate);
                    }
                }
                if(getIsEvent())
                    canvas.drawRoundRect(rectO, RADIUS, RADIUS, mPaintBackgroundEvent);
            } else {
                if (getIsSelected()) {
                    if (getIsSameDay(todayMillis, (long) tagView.getTag())) {
                        canvas.drawRoundRect(rectF, RADIUS, RADIUS, mPaintBackgroundSelected);
                    }
                }
                canvas.drawText(calendar.get(Calendar.DATE) + "", xPos, yPos, mPaintTextDate);
                if(getIsEvent())
                    canvas.drawRoundRect(rectO, RADIUS, RADIUS, mPaintBackgroundEvent);
            }
        }
    }

    public void setDate(long millis) {
        todayMillis = millis;
        setTag(millis);
    }

    public void setStaticText(int staticText) {
        dayOfWeek = staticText;
        isStaticText = true;
    }

    public void setIsEvent(boolean event) {
        isEvent = event;
    }

    public boolean getIsEvent() {
        return isEvent;
    }

    public boolean getIsSelected(){
        return tagView != null && tagView.getTag() != null;
    }

    public long getDate() {
        return todayMillis;
    }

    private boolean getIsToday() {
        Calendar cal1 = Calendar.getInstance();
        return getIsSameDay(cal1.getTimeInMillis(), todayMillis);
    }

    public boolean getIsSameDay(long millis1, long millis2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(millis1);
        cal2.setTimeInMillis(millis2);
        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE));
    }

    public boolean getIsStaticText() {
        return isStaticText;
    }
}
