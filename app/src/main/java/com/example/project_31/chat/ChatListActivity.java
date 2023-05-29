package com.example.project_31.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_31.R;

// 채팅 - 채팅 목록
public class ChatListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ChatRoomAdapter adapter;
    LinearLayout profList;
    LinearLayout chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_list_layout);

        recyclerView = (RecyclerView) findViewById(R.id.chatRoomRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        adapter = new ChatRoomAdapter();
        adapter.addItem(new Room("이름1", "Message1", "오후 03:43", "1"));
        adapter.addItem(new Room("이름2", "Message2", "오후 03:43", "10"));
        adapter.addItem(new Room("이름3", "Message3", "오후 03:43", "5"));
        adapter.addItem(new Room("이름4", "Message4", "오후 03:43", "300"));


        // 리사이클러뷰에 어댑터를 연결한다.
        recyclerView.setAdapter(adapter);

        profList = findViewById(R.id.profList);
        profList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatMainActivity.class);
                startActivity(intent);
            }
        });




        }

}
