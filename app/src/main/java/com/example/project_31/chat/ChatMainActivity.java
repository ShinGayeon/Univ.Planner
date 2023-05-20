package com.example.project_31.chat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_31.R;

public class ChatMainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ChatProfAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_main_layout);

        recyclerView = (RecyclerView) findViewById(R.id.chatProfRecycrView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        adapter = new ChatProfAdapter();
        adapter.addItem(new Prof("이름1", "컴퓨터소프트웨어과1"));
        adapter.addItem(new Prof("이름2", "컴퓨터소프트웨어과2"));
        adapter.addItem(new Prof("이름3", "컴퓨터소프트웨어과3"));
        adapter.addItem(new Prof("이름4", "컴퓨터소프트웨어과4"));

        // 리사이클러뷰에 어댑터를 연결한다.
        recyclerView.setAdapter(adapter);
        }

}
