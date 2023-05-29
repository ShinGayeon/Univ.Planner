package com.example.project_31.chat;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_31.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

// 채팅 - 채팅방
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
        setContentView(R.layout.chat_room_layout);

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
        // 'chat' 노드에 저장되어 있는 데이터들을 읽어오기
        // chatRef에 데이터가 변경되는 것을 듣는 리스너 추가
        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // 새로 추가된 데이터 값(ChatItem 객체) 가져오기
                ChatItem chatItem = snapshot.getValue(ChatItem.class);

                // 새로운 메세지를 리스트뷰에 추가하기 위해 ArrayList에 추가
                chatItems.add(chatItem);

                // 리스트뷰 갱신
                adapter.notifyDataSetChanged();
                // 리스트뷰의 마지막 위치로 스크롤 위치 이동
                listView.setSelection(chatItems.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void clickSend(View view) {

        // firebaseDB에 저장할 값(닉네임, 메세지, 이미지 URL, 시간...)
        String nickName = G.nickName;
        String message = chatEditText.getText().toString();
        String profileUrl = G.profileUrl;

        // 메세지 작성 시간을 문자열로
        Calendar calendar = Calendar.getInstance(); // 현재 시간을 가지고 있는 객체
        String time = calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE); // 시간:분 (14:41)

        // firebaseDB에 저장할 값(ChatItem 객체) 설정
        ChatItem chatItem = new ChatItem(nickName, message, time, profileUrl);
        // "chat" 노드에 ChatItem 객체를 통해 전달(메세지 전송)
        chatRef.push().setValue(chatItem);

        // 메세지 전송 후 EditText에 있는 글씨 초기화
        chatEditText.setText("");

        // 소프트 키패드 안보이게 설정
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }

}