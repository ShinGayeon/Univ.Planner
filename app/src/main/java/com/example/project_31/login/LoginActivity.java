package com.example.project_31.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.example.project_31.R;
import com.example.project_31.todo.TodoMainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText login_email, login_password;
    private Button login_button, join_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main_layout);

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);

        join_button = findViewById(R.id.login);
        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( LoginActivity.this, TodoMainActivity.class );
                startActivity(intent);
            }
        });


        login_button = findViewById(R.id.login);
        login_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserEmail = login_email.getText().toString();
                String UserPwd = login_password.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject( response );
                            boolean success = jsonObject.getBoolean( "success" );

                            if(success) {//로그인 성공시

                                String UserEmail = jsonObject.getString( "UserEmail" );
                                String UserPwd = jsonObject.getString( "UserPwd" );
                                String UserName = jsonObject.getString( "UserName" );

                                Intent intent = new Intent(LoginActivity.this, TodoMainActivity.class);
                                intent.putExtra( "UserEmail", UserEmail );
                                intent.putExtra( "UserPwd", UserPwd );
                                intent.putExtra( "UserName", UserName );

                                startActivity(intent);

                            } else {//로그인 실패시
                                Toast.makeText( getApplicationContext(), "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT ).show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

            }
        });
    }
}
