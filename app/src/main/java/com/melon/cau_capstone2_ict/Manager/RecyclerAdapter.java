package com.melon.cau_capstone2_ict.Manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.melon.cau_capstone2_ict.R;
import com.melon.cau_capstone2_ict.CalendarBoardClass;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private List<CalendarBoardClass> list;
    private LayoutInflater inflater;
    private Context context;

    public RecyclerAdapter(Context context, List<CalendarBoardClass> list) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setRecyclerAdapter(List<CalendarBoardClass> list){
        this.list = list;
    }

    // 최초에 뷰홀더를 생성해주는 함수
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
//        Toast.makeText(context, list.get(position).getTitle(), Toast.LENGTH_SHORT);
        Log.d("onCreateVieHolder: ", "Create");
        return new RecyclerViewHolder(view);
    }

    // 기존 뷰홀더와 새롭게 보여줘야할 데이터를 바인드해주는 함수
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
//        Toast.makeText(context, list.get(position).getTitle(), Toast.LENGTH_SHORT);

        holder.title.setText(list.get(position).getTitle());
        Log.d("onBindVieHolder: ", list.get(position).getTitle());
        holder.score.setText(list.get(position).getScore());
    }

    // 총 아이템 수
    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    // 뷰홀더 클래스
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView score;

        public RecyclerViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            score = (TextView) view.findViewById(R.id.content);
        }
    }
}