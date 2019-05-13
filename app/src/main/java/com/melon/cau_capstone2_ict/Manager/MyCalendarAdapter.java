package com.melon.cau_capstone2_ict.Manager;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.melon.cau_capstone2_ict.R;

import java.util.List;

public class MyCalendarAdapter extends RecyclerView.Adapter<MyCalendarAdapter.RecyclerViewHolder> {
    private List<MyCalendar> list;
    private LayoutInflater inflater;
    private Context context;
    private int pos;

    // Item의 클릭 상태를 저장할 array 객체
    private SparseBooleanArray selectedItems;
    // 직전에 클릭됐던 Item의 position
    private int prePosition = -1;

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
        return list.size();
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
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {
        pos = position;
        holder.title.setText(list.get(position).getTitle());
        holder.writer.setText(list.get(position).getWriter());
        holder.date.setText(list.get(position).getDate());
        holder.content.setText(list.get(position).getContent());

        holder.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LinearLayout layout = new LinearLayout(context);
//                layout.setOrientation(LinearLayout.VERTICAL);
//                final EditText titleText = new EditText(context);
//                titleText.setHint("제목");
//                titleText.setText(list.get(pos).getTitle());
//                final EditText contentText = new EditText(context);
//                contentText.setHint("내용");
//                contentText.setText(list.get(pos).getContent());
//                layout.addView(titleText);
//                layout.addView(contentText);
//                final String date = list.get(pos).getDate();
//                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//                dialog.setTitle(date + " (수정)").setView(layout).setPositiveButton("수정", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String writer = MyUserData.getInstance().getNickname();
//                        String title = titleText.getText().toString();
//                        String content = contentText.getText().toString();
//                        DBHelper dbHelper = new DBHelper(context, "calendar", null, 1);
//                        MyCalendar myCalendar = new MyCalendar();
//                        myCalendar.setWriter(writer);
//                        myCalendar.setTitle(title);
//                        myCalendar.setContent(content);
//                        myCalendar.setDate(date);
//                        dbHelper.update(list.get(pos).get_id(), myCalendar);
//                    }
//                }).setNeutralButton("취소", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                }).create().show();
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("삭제하시겠습니까?").setView(layout).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper dbHelper = new DBHelper(context, "calendar", null, 1);
                        deleteRequest(list.get(pos));
                        dbHelper.delete(list.get(pos).get_id());
                    }
                }).setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
            }
        });

        holder.changeVisibility(selectedItems.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItems.get(position)) {
                    // 펼쳐진 Item을 클릭 시
                    selectedItems.delete(position);
                } else {
                    // 직전의 클릭됐던 Item의 클릭상태를 지움
                    selectedItems.delete(prePosition);
                    // 클릭한 Item의 position을 저장
                    selectedItems.put(position, true);
                }
                // 해당 포지션의 변화를 알림
                if (prePosition != -1)
                    notifyItemChanged(prePosition);
                notifyItemChanged(position);
                // 클릭된 position 저장
                prePosition = position;
            }
        });
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
        TextView date;
        TextView content;
        LinearLayout linear_spread;
        LinearLayout linear_modify;
        Button modify;
        Button remove;

        public RecyclerViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.calendar_title);
            writer = (TextView) view.findViewById(R.id.calendar_writer);
            date = (TextView) view.findViewById(R.id.calendar_date);
            content = (TextView) view.findViewById(R.id.calendar_content);
            linear_spread = (LinearLayout) view.findViewById(R.id.calendar_linear_spread);
            linear_modify = (LinearLayout) view.findViewById(R.id.calendar_option);
            modify = (Button) view.findViewById(R.id.button_calendar_modify);
            remove = (Button) view.findViewById(R.id.button_calendar_remove);
        }

        private void changeVisibility(final boolean isExpanded) {
            // height 값을 dp로 지정해서 넣고싶으면 아래 소스를 이용
            int dpValue = 150;
            float d = context.getResources().getDisplayMetrics().density;
            int height = (int) (dpValue * d);

            // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);
            // Animation이 실행되는 시간, n/1000초
            va.setDuration(300);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // value는 height 값
                    int value = (int) animation.getAnimatedValue();
                    // imageView의 높이 변경
                    linear_spread.getLayoutParams().height = value;
                    linear_spread.requestLayout();
                    // imageView가 실제로 사라지게하는 부분
                    linear_spread.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                }
            });
            // Animation start
            va.start();
        }
    }
}