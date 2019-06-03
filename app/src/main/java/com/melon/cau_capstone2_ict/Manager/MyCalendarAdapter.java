package com.melon.cau_capstone2_ict.Manager;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.melon.cau_capstone2_ict.R;
import com.melon.cau_capstone2_ict.TabFragment_calendar;

import java.util.List;
import java.util.Observable;
import java.util.StringTokenizer;

public class MyCalendarAdapter extends RecyclerView.Adapter<MyCalendarAdapter.RecyclerViewHolder> {
    private List<MyCalendar> list;
    private LayoutInflater inflater;
    private Context context;
    private int pos;

    private SparseBooleanArray selectedItems;
    private int prePosition = -1;

    String[] timeList = {"종일", "00시", "01시", "02시", "03시", "04시", "05시", "06시", "07시", "08시", "09시", "10시", "11시", "12시",
            "13시", "14시", "15시", "16시", "17시", "18시", "19시", "20시", "21시", "22시", "23시", "24시"};
    String[] colorList = {"black", "gray", "white", "blue", "l_blue", "green", "l_green", "red", "pink", "orange", "yellow", "purple"};
    String str1 = "", str2 = "", color = "";

    public MyCalendarAdapter(Context context, List<MyCalendar> list) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        selectedItems = new SparseBooleanArray();
    }

    public void setRecyclerAdapter(List<MyCalendar> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    public void setPrePosition(int pos){
        prePosition = pos;
    }

    public void addItem(MyCalendar myCalendar) {
        list.add(myCalendar);
    }

    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    // 기존 뷰홀더와 새롭게 보여줘야할 데이터를 바인드해주는 함수
    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.writer.setText(list.get(position).getWriter());
        holder.date.setText(list.get(position).getDate());
        holder.time.setText(list.get(position).getTime());
        holder.content.setText(list.get(position).getContent());
        if(position % 2 == 0){
            holder.itemView.setBackgroundResource(R.color.yellow_transparent);
        }else{
            holder.itemView.setBackgroundResource(R.color.white_pressed);
        }

        holder.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout titleLayout = new LinearLayout(context);
                titleLayout.setGravity(Gravity.CENTER);
                titleLayout.setOrientation(LinearLayout.HORIZONTAL);
                final TextView title = new TextView(context);
                final String date = list.get(position).getDate();
                title.setText(date + " (수정)");
                title.setTextSize(24);
                titleLayout.addView(title);
                final LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                final LinearLayout layoutTime = new LinearLayout(context);
                layoutTime.setOrientation(LinearLayout.HORIZONTAL);
                layoutTime.setGravity(Gravity.CENTER);
                final TextView timeText = new TextView(context);
                timeText.setText("시간: ");
                final Spinner spinnerStart = new Spinner(context);
                final ArrayAdapter<String> spAdapterStartDate = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, timeList);
                spinnerStart.setAdapter(spAdapterStartDate);
                final TextView forText = new TextView(context);
                forText.setText("-");
                forText.setPadding(5, 0, 5, 0);
                final Spinner spinnerEnd = new Spinner(context);
                final ArrayAdapter<String> spAdapterEndDate = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, timeList);
                spinnerEnd.setAdapter(spAdapterEndDate);
                final Spinner spinnerColor = new Spinner(context);
                spinnerColor.setAdapter(new MyCustomAdapter(context, R.layout.support_simple_spinner_dropdown_item, colorList));
                layoutTime.addView(timeText);
                layoutTime.addView(spinnerStart);
                layoutTime.addView(forText);
                layoutTime.addView(spinnerEnd);
                layoutTime.addView(spinnerColor);
                final EditText titleText = new EditText(context);
                titleText.setHint("제목");
                titleText.setText(list.get(position).getTitle());
                titleText.setPadding(10, 0, 10, 0);
                final EditText contentText = new EditText(context);
                contentText.setHint("내용");
                contentText.setText(list.get(position).getContent());
                contentText.setPadding(10, 0, 10, 0);
                layout.addView(layoutTime);
                layout.addView(titleText);
                layout.addView(contentText);
                StringTokenizer tokens = new StringTokenizer(list.get(position).getTime(), "-");
                String token = tokens.nextToken();
                Log.d("token", token);
                if(token.equals("하루 종일")){
                    str1 = "종일";
                    str2 = "종일";
                } else{
                    str1 = token.substring(0, token.length() - 1);
                    token = tokens.nextToken();
                    if(token != null)
                        str2 = token.substring(1, token.length());
                }
                int idx1 = 0, idx2 = 0, idx = 0;
                for(int i = 0; i < timeList.length; i++){
                    if(timeList[i].equals(str1))
                        idx1 = i;
                    if(timeList[i].equals(str2))
                        idx2 = i;
                }
                spinnerStart.setSelection(idx1);
                spinnerEnd.setSelection(idx2);
                for(int i = 0; i < colorList.length; i++){
                    if(colorList[i].equals(list.get(position).getColor())){
                        idx = i;
                        break;
                    }
                }
                spinnerColor.setSelection(idx);
                spinnerStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        pos = position;
                        str1 = (String) parent.getItemAtPosition(position);
                        if (str1.equals("종일")) {
                            spinnerEnd.setEnabled(false);
                            str1 = "";
                            str2 = "";
                        } else {
                            spinnerEnd.setEnabled(true);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                spinnerEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (pos > position && position != 0) {
                            parent.setSelection(pos);
                        }
                        str2 = (String) parent.getItemAtPosition(position);
                        if (str2.equals("종일")) {
                            str2 = "";
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        color = (String) parent.getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setCustomTitle(titleLayout).setView(layout).setPositiveButton("등록", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper dbHelper = new DBHelper(context, MyUserData.getInstance().getNickname(), null, 1);
                        dbHelper.delete(list.get(position).get_id());
                        MyCalendar myCalendar = list.get(position);
                        String title = myCalendar.getColor() + "|" + myCalendar.getTime() + "|" + myCalendar.getTitle();
                        myCalendar.setTitle(title);
                        deleteRequest(myCalendar);
                        String date = list.get(position).getDate();
                        list.remove(position);
                        notifyItemRemoved(position);

                        String writer = MyUserData.getInstance().getNickname();
                        title = color + "|";
                        if (!str1.equals("")) {
                            title += str1 + " - ";
                            if (!str2.equals(""))
                                title += str2;
                        } else {
                            title += "하루 종일";
                        }
                        title += "|";
                        title += titleText.getText().toString();
                        String content = contentText.getText().toString();
                        if (dbHelper == null) {
                            dbHelper = new DBHelper(context, MyUserData.getInstance().getNickname(), null, 1);
                        }
                        myCalendar = new MyCalendar();
                        myCalendar.setWriter(writer);
                        myCalendar.setTitle(title);
                        myCalendar.setContent(content);
                        myCalendar.setDate(date);
                        writeRequest(myCalendar);
                        StringTokenizer tokens = new StringTokenizer(title, "|");
                        String token = tokens.nextToken();
                        myCalendar.setColor(token);
                        token = tokens.nextToken();
                        myCalendar.setTime(token);
                        token = tokens.nextToken();
                        for (; tokens.hasMoreElements(); )
                            token += "|" + tokens.nextToken();
                        myCalendar.setTitle(token);
                        dbHelper.add(myCalendar);
                    }
                }).setNeutralButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setCancelable(false).create().show();
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setGravity(Gravity.CENTER);
                final TextView title = new TextView(context);
                title.setText("삭제하시겠습니까?");
                title.setTextSize(24);
                layout.addView(title);
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setView(layout).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper dbHelper = new DBHelper(context, MyUserData.getInstance().getNickname(), null, 1);
                        dbHelper.delete(list.get(position).get_id());
                        MyCalendar myCalendar = list.get(position);
                        String title = myCalendar.getColor() + "|" + myCalendar.getTime() + "|" + myCalendar.getTitle();
                        myCalendar.setTitle(title);
                        deleteRequest(myCalendar);
                        list.remove(position);
                        notifyItemRemoved(position);
                    }
                }).setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setCancelable(false).create().show();
            }
        });

        holder.changeVisibility(selectedItems.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItems.get(position)) {
                    selectedItems.delete(position);
                } else {
                    selectedItems.delete(prePosition);
                    selectedItems.put(position, true);
                }
                if (prePosition != -1)
                    notifyItemChanged(prePosition);
                notifyItemChanged(position);
                prePosition = position;
            }
        });
    }

    void writeRequest(MyCalendar myCalendar) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Tag", "response check : " + response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        CalendarRequest cr = new CalendarRequest(myCalendar, "new", responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(cr);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        prePosition = -1;
    }

    void deleteRequest(MyCalendar myCalendar) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Tag", "response check : " + response);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        CalendarRequest cr = new CalendarRequest(myCalendar, "delete", responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(cr);
    }

    // 뷰홀더 클래스
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView writer;
        TextView content;
        TextView date;
        TextView time;
        LinearLayout linear_spread;
        LinearLayout linear_modify;
        ImageButton modify;
        ImageButton remove;

        public RecyclerViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.calendar_title);
            writer = (TextView) view.findViewById(R.id.calendar_writer);
            writer.setVisibility(View.GONE);
            content = (TextView) view.findViewById(R.id.calendar_content);
            date = (TextView) view.findViewById(R.id.calendar_date);
            time = (TextView) view.findViewById(R.id.calendar_time);
            linear_spread = (LinearLayout) view.findViewById(R.id.calendar_linear_spread);
            linear_modify = (LinearLayout) view.findViewById(R.id.calendar_option);
            modify = (ImageButton) view.findViewById(R.id.button_calendar_modify);
            remove = (ImageButton) view.findViewById(R.id.button_calendar_remove);
        }

        private void changeVisibility(final boolean isExpanded) {
            int dpValue = 150;
            float d = context.getResources().getDisplayMetrics().density;
            int height = (int) (dpValue * d);

            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);
            va.setDuration(300);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    linear_spread.getLayoutParams().height = value;
                    linear_spread.requestLayout();
                    linear_spread.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                }
            });
            va.start();
        }
    }
}