package com.melon.cau_capstone2_ict.Manager;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.melon.cau_capstone2_ict.R;

import java.util.List;

public class MyCalendarAdapter extends RecyclerView.Adapter<MyCalendarAdapter.RecyclerViewHolder> {
    private List<MyCalendar> list;
    private LayoutInflater inflater;
    private Context context;

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

    // 총 아이템 수
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


    // 최초에 뷰홀더를 생성해주는 함수
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    // 기존 뷰홀더와 새롭게 보여줘야할 데이터를 바인드해주는 함수
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.writer.setText(list.get(position).getWriter());
        holder.date.setText(list.get(position).getDate());
        holder.content.setText(list.get(position).getContent());

        holder.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Todo:수정하기
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Todo:삭제하기
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

            writer.setText("");
            date.setText("");
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