package com.example.project_31.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_31.R;
import com.example.project_31.chat.ChatActivity;
import com.example.project_31.chat.ChatMainActivity;
import com.example.project_31.login.MainActivity;
import com.example.project_31.login.SignUpActivity;
import com.example.project_31.todo.TodoMainActivity;

public class RealMainActivity extends AppCompatActivity {

    private Button btnbook, btnchat, btntodo, btnshop, btncommunity, btnloading;
    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        // Dictionary -- 아직 용어사전 클래스가 없어서 아무데나로 이동시킵니다요
        Button btnbook = (Button) findViewById(R.id.btnbook);
        btnbook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intent);
            }
        });

        // Chat
        Button btnchat = (Button) findViewById(R.id.btnchat);
        btnchat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatMainActivity.class);
                startActivity(intent);
            }
        });

        // To-Do
        Button btntodo = (Button) findViewById(R.id.btntodo);
        btntodo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TodoMainActivity.class);
                startActivity(intent);
            }
        });

        // Shop
        Button btnshop = (Button) findViewById(R.id.btnshop);
        btnshop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RealMainActivity.this);
                dialog = builder.setMessage("♡준비 중인 서비스입니다♡").setNegativeButton("확인", null).create();
                dialog.show();
            }
        });

        // Community
        Button btncommunity = (Button) findViewById(R.id.btncommunity);
        btncommunity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RealMainActivity.this);
                dialog = builder.setMessage("♡준비 중인 서비스입니다♡").setNegativeButton("확인", null).create();
                dialog.show();
            }
        });

        // loading
        Button btnloading = (Button) findViewById(R.id.btnloading);
        btnloading.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RealMainActivity.this);
                dialog = builder.setMessage("♡준비 중인 서비스입니다♡").setNegativeButton("확인", null).create();
                dialog.show();
            }
        });

        // Homepage
        ImageButton btnhp = (ImageButton) findViewById(R.id.btnhp);
        btnhp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.yeonsung.ac.kr/ko/index.do"));
                startActivity(intent);
            }
        });

    }
}