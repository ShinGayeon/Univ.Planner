package com.example.project_31.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_31.R;
import com.example.project_31.todo.TodoMainActivity;

// 채팅 - 교수 목록
public class ChatMainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ChatProfAdapter adapter;
    LinearLayout profList;
    LinearLayout chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_main_layout);

        recyclerView = (RecyclerView) findViewById(R.id.chatProfRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        adapter = new ChatProfAdapter();
        adapter.addItem(new Prof("이름1", "컴퓨터소프트웨어과1"));
        adapter.addItem(new Prof("이름2", "컴퓨터소프트웨어과2"));
        adapter.addItem(new Prof("이름3", "컴퓨터소프트웨어과3"));
        adapter.addItem(new Prof("이름4", "컴퓨터소프트웨어과4"));

        // 리사이클러뷰에 어댑터를 연결한다.
        recyclerView.setAdapter(adapter);

        chatList = findViewById(R.id.chatList);
        chatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatEx.class);
                startActivity(intent);
            }
        });

        profList = findViewById(R.id.profList);
        profList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatListActivity.class);
                startActivity(intent);
            }
        });


    }

}
