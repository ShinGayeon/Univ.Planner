package com.example.project_31.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_31.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    EditText chatEditText;
    ListView listView;

    ArrayList<ChatItem> chatItems = new ArrayList<>();
    ChatAdapter adapter;

    // Firebase Database 관리 객체 참조 변수
    FirebaseDatabase firebaseDatabase;

    // 'chat' 노드의 참조 객체 참조 변수
    DatabaseReference chatRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_list_layout);

        // 제목 줄 제목 글씨를 닉네임(또는 채팅방)으로
//        getSupportActionBar().setTitle(nickName);

        chatEditText = findViewById(R.id.chatEditText);
        listView = findViewById(R.id.chatListView);
        adapter = new ChatAdapter(chatItems, getLayoutInflater());
        listView.setAdapter(adapter);

        // firebase DB관리 객체와 'chat' 노드 참조 객체 얻어오기
        firebaseDatabase = FirebaseDatabase.getInstance();
        chatRef = firebaseDatabase.getReference("chat");

        // firebaseDB에서 채팅 메세들 실시간 읽어오기 ...

    }

    public void clickSend(View view) {

        // firebaseDB에 저장할 값(닉네임, 메세지, 이미지 URL, 시간...)
//        String nickName = G.nicName;

    }


}
