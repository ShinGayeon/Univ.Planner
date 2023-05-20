package com.example.project_31.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_31.R;

import java.util.ArrayList;


public class ChatProfAdapter extends RecyclerView.Adapter<ChatProfAdapter.ViewHolder> {

    ArrayList<Prof> items = new ArrayList<Prof>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 파라미터로 전달되는 뷰그룹 객체는 각 아이템을 위한 뷰그룹 객체이므로
        // XML 레이아웃을 인플레이션하여 이 뷰그룹 객체에 전달한다.
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.chat_prof_recyclerview, parent, false);

        return new ViewHolder(itemView);
    }

    // 뷰홀더가 재사용될 때 호출된다. 이 메서드는 재활용할 수 있는 뷰홀더 객체를 파라미터로 전달한다.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 뷰 객체는 기존 것을 그대로 사용하고 데이터만 바꿔준다.
        Prof item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Prof item) {
        items.add(item);
    }

    public void setItems(ArrayList<Prof> items) {
        this.items = items;
    }

    public Prof getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Prof item) {
        items.set(position, item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView major;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            major = itemView.findViewById(R.id.major);
        }

        public void setItem(Prof item) {
            name.setText(item.getName());
            major.setText(item.getMajor());
        }
    }
}
