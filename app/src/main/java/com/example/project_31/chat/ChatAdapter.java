package com.example.project_31.chat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.project_31.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

// 채팅 - 채팅방 어댑터
public class ChatAdapter extends BaseAdapter {

    ArrayList<ChatItem> chatItems;
    LayoutInflater layoutInflater;

    public ChatAdapter(ArrayList<ChatItem> chatItems, LayoutInflater layoutInflater) {
        this.chatItems = chatItems;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return chatItems.size();
    }

    @Override
    public Object getItem(int position) {
        return chatItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        // 현재 보여줄 순서(position)의 데이터로 뷰 생성
        ChatItem item = chatItems.get(position);

        // 재활용할 뷰 사용 안 함
        View itemView = null;


        // 메세지가 내 메세지인지 확인
        if (item.getName().equals(G.nickName)) {
            itemView = layoutInflater.inflate(R.layout.chat_my_box, viewGroup, false);
            Log.d("테스트",viewGroup.toString());
        } else {
            itemView = layoutInflater.inflate(R.layout.chat_other_box, viewGroup, false);
        }


        //만들어진 itemView에 값들 설정
        CircleImageView chatImg = itemView.findViewById(R.id.chatImg);
        TextView chatName = itemView.findViewById(R.id.chatName);
        TextView chatMsg = itemView.findViewById(R.id.chatMsg);
        TextView chatTime = itemView.findViewById(R.id.chatTime);

        chatName.setText(item.getName());
        chatMsg.setText(item.getMessage());
        chatTime.setText(item.getTime());

        Glide.with(itemView).load(item.getProfileUrl()).into(chatImg);

        return itemView;
    }
} // dd
