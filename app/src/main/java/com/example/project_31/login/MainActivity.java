package com.example.project_31.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.project_31.R;
import com.example.project_31.todo.TodoMainActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main_layout);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mConditionRef = mDatabase.child("message");
        mConditionRef.setValue("Re0");
        TextView textView;
        
        //회원가입 버튼 클릭시 activity 전환
        Button sign = (Button) findViewById(R.id.sign);
        sign.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        //로그인 버튼 클릭시 activity 전환
        Button developer_info_btn = (Button) findViewById(R.id.login);
        developer_info_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TodoMainActivity.class);
                startActivity(intent);
            }
        });

    }
}
// 테스트 - 가연
// 테스트 - 진성