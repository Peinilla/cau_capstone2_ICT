package com.melon.cau_capstone2_ict.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.melon.cau_capstone2_ict.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarItemView extends View {

    Paint mPaintTextDate = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintTextToday = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackgroundToday = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackgroundTodaySelected = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackgroundSelected = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBlack = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintGray = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintWhite = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBlue = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintLBlue = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintGreen = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintLGreen = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintRed = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintPink = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintOrange = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintYellow = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintPurple = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int dayOfWeek = -1;
    private long todayMillis;
    private boolean isStaticText = false;
    private int dp11;
    private int dp16;
    private int dp1;
    private final float RADIUS = 100f;
    private ArrayList<String> color = new ArrayList<String>();

    //test
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
        mPaintBackgroundToday.setColor(ContextCompat.getColor(getContext(), R.color.select_today));
        mPaintBackgroundTodaySelected.setColor(ContextCompat.getColor(getContext(), R.color.selct_today_text));
        mPaintBackgroundSelected.setColor(ContextCompat.getColor(getContext(), R.color.select_color));
        mPaintBlack.setColor(ContextCompat.getColor(getContext(), R.color.black));
        mPaintGray.setColor(ContextCompat.getColor(getContext(), R.color.gray));
        mPaintWhite.setColor(ContextCompat.getColor(getContext(), R.color.white));
        mPaintBlue.setColor(ContextCompat.getColor(getContext(), R.color.blue));
        mPaintLBlue.setColor(ContextCompat.getColor(getContext(), R.color.l_blue));
        mPaintGreen.setColor(ContextCompat.getColor(getContext(), R.color.green));
        mPaintLGreen.setColor(ContextCompat.getColor(getContext(), R.color.l_green));
        mPaintRed.setColor(ContextCompat.getColor(getContext(), R.color.red));
        mPaintPink.setColor(ContextCompat.getColor(getContext(), R.color.pink));
        mPaintOrange.setColor(ContextCompat.getColor(getContext(), R.color.orange));
        mPaintYellow.setColor(ContextCompat.getColor(getContext(), R.color.yellow));
        mPaintPurple.setColor(ContextCompat.getColor(getContext(), R.color.purple));

        setClickable(true);

        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (!getIsStaticText()) {
                        ((CalendarView) getParent()).setSelectedView(view);
                        TextView textView = getRootView().findViewById(R.id.text_calendar_date);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        RectF rectF = new RectF(xPos - dp16 - 10, getHeight() / 2 - dp16 - 10, xPos + dp16 + 10, getHeight() / 2 + dp16 + 10);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(todayMillis);

        calendarView = (CalendarView) getParent();
        parent = (ViewPager) calendarView.getParent();
        tagView = (CalendarItemView) parent.getTag();

        if (getIsStaticText()) {
            canvas.drawText(CalendarView.DAY_OF_WEEK[dayOfWeek], xPos, yPos, mPaintTextDate);
        } else {
            if (getIsToday()) {
                canvas.drawText(calendar.get(Calendar.DATE) + "", xPos, yPos - 25, mPaintTextToday);

                if (getIsSelected()) {
                    if (getIsSameDay(todayMillis, (long) tagView.getTag())) {
                        canvas.drawRect(rectF,mPaintBackgroundToday);
                        canvas.drawText(calendar.get(Calendar.DATE) + "", xPos, yPos - 25, mPaintTextDate);
                    }
                }
                for(int i = 0; i < color.size(); i++) {
                    if(i == 3)
                        break;
                    RectF rect = new RectF(xPos - (dp1 * 20), getHeight() / 2 - (dp1 * 10) + (i * 15) + 25,
                                xPos + (dp1 * 20), getHeight() / 2 - (dp1 * 16) + (i * 15) + 25);
                    switch (color.get(i)) {
                        case "black":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintBlack);
                            break;
                        case "gray":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintGray);
                            break;
                        case "white":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintWhite);
                            break;
                        case "red":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintRed);
                            break;
                        case "pink":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintPink);
                            break;
                        case "blue":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintBlue);
                            break;
                        case "l_blue":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintLBlue);
                            break;
                        case "green":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintGreen);
                            break;
                        case "l_green":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintLGreen);
                            break;
                        case "yellow":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintYellow);
                            break;
                        case "orange":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintOrange);
                            break;
                        case "purple":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintPurple);
                            break;
                    }
                }
            } else {
                if (getIsSelected()) {
                    if (getIsSameDay(todayMillis, (long) tagView.getTag())) {
                        canvas.drawRect(rectF, mPaintBackgroundSelected);
//                        canvas.drawRoundRect(rectF, RADIUS, RADIUS, mPaintBackgroundSelected);
                    }
                }
                canvas.drawText(calendar.get(Calendar.DATE) + "", xPos, yPos - 25, mPaintTextDate);
                for(int i = 0; i < color.size(); i++) {
                    if(i == 3)
                        break;
                    RectF rect = new RectF(xPos - (dp1 * 20), getHeight() / 2 - (dp1 * 10) + (i * 15) + 25,
                            xPos + (dp1 * 20), getHeight() / 2 - (dp1 * 16) + (i * 15) + 25);
                    switch (color.get(i)) {
                        case "black":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintBlack);
                            break;
                        case "gray":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintGray);
                            break;
                        case "white":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintWhite);
                            break;
                        case "red":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintRed);
                            break;
                        case "pink":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintPink);
                            break;
                        case "blue":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintBlue);
                            break;
                        case "l_blue":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintLBlue);
                            break;
                        case "green":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintGreen);
                            break;
                        case "l_green":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintLGreen);
                            break;
                        case "yellow":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintYellow);
                            break;
                        case "orange":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintOrange);
                            break;
                        case "purple":
                            canvas.drawRoundRect(rect, RADIUS, RADIUS, mPaintPurple);
                            break;
                    }
                }
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

    public void setColor(ArrayList<String> color) {
        this.color = color;
    }

    public ArrayList<String> getColor() {
        return color;
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
