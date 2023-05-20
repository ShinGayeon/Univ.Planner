package com.example.project_31.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.project_31.R;
import com.example.project_31.todo.TodoMainActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText join_name, join_email, join_password;
    private Button join_button, check_button;
    private AlertDialog dialog;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_layout);

//        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar) ;
//        setSupportActionBar(tb);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼,
        // 디폴트로 true 입력 시 뒤로가기 버튼 생성

        //아이디값 찾아주기
        join_name = findViewById(R.id.name_sign);
        join_email = findViewById(R.id.email_sign);
        join_password = findViewById(R.id.password_sign);

        //회원가입 버튼 클릭 시 수행
        join_button = findViewById( R.id.sign_up);
        join_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(SignUpActivity.this, TodoMainActivity.class);
            final String UserEmail = join_email.getText().toString();
            final String UserPwd = join_password.getText().toString();
            final String UserName = join_name.getText().toString();

            //한 칸이라도 입력 안했을 경우
            if (UserEmail.equals("") || UserPwd.equals("") || UserName.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    dialog = builder.setMessage("모두 입력해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
            }
            startActivity(intent);
    //            @Override
    //            public void onResponse(String response) {
    //
    //                try {
    //                    JSONObject jsonObject = new JSONObject( response );
    //                    boolean success = jsonObject.getBoolean( "success" );
    //
    //                    //회원가입 성공시
    //                    if (success) {
    //
    //                        Toast.makeText(getApplicationContext(), String.format("%s님 가입을 환영합니다.", UserName), Toast.LENGTH_SHORT).show();
    //                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
    //                        startActivity(intent);
    //
    //                        //회원가입 실패시
    //                    } else {
    //                        Toast.makeText(getApplicationContext(), "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
    //                        return;
    //                    }
    //                    } else {
    //                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
    //                        dialog = builder.setMessage("비밀번호가 동일하지 않습니다.").setNegativeButton("확인", null).create();
    //                        dialog.show();
    //                        return;
    //                    }
    //
    //            } catch (JSONException e) {
    //                e.printStackTrace();
    //            } catch (JSONException e) {
    //                throw new RuntimeException(e);
    //            }

            }
            });
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
