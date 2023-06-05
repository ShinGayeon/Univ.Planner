package com.example.project_31.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.project_31.R;
//import com.google.android.gms.auth.api.Auth;
import com.example.project_31.main.RealMainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String VALID_USEREMAIL = "ex@example.com";
    private static final String VALID_PASSWORD = "password";

    private EditText login_email, login_password;
    private Button login_button, join_button;

    //    private SignInButton btn_google; // 구글 로그인 버튼
    private FirebaseAuth auth; // 파이어 베이스 인증 객체
//    private GoogleApiClient googleApiClient; // 구글 API 클라이언트 객체
//    private static final int REO_SIGN_GOOGLE = 100; // 구글 로그인 결과 코드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main_layout);

        //데이터베이스 연결
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mConditionRef = mDatabase.child("message");
        mConditionRef.setValue("Re0");
        TextView textView;

//        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
//
//        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, (GoogleApiClient.OnConnectionFailedListener) this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        //회원가입 버튼 클릭시 activity 전환
        Button sign = (Button) findViewById(R.id.sign);
        sign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        //로그인 버튼 클릭시 activity 전환
        Button developer_info_btn = (Button) findViewById(R.id.login);
        developer_info_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RealMainActivity.class);
                startActivity(intent);
            }
        });

        //Firebase 선언
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mFirebaseAuth.getInstance().getCurrentUser();

//        btn_google = findViewById(R.id.google);
//        btn_google.setOnClickListener(new View.OnClickListener() { // 구글 로그인 버튼을 클릭 했을 시 이 곳 실행
//            @Override
//            public void onClick(View view) {
//                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
//                startActivityForResult(intent, REO_SIGN_GOOGLE);
//            }
//        });


        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);

        join_button = findViewById(R.id.login);
        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RealMainActivity.class);
                startActivity(intent);

            }
        });



        login_button = findViewById(R.id.login);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserEmail = login_email.getText().toString();
                String UserPwd = login_password.getText().toString();

                if (TextUtils.isEmpty(UserEmail) || TextUtils.isEmpty(UserPwd)) {
                    Toast.makeText(MainActivity.this,
                            "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                mFirebaseAuth.signInWithEmailAndPassword(UserEmail, UserPwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // 로그인 성공
                            Intent intent = new Intent(MainActivity.this, RealMainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "회원이 아닙니다.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }
    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REO_SIGN_GOOGLE) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            if (result.isSuccess()) { //인증결과가 성공적일 때
//                GoogleSignInAccount inAccount = result.getSignInAccount(); //inAccount 라는 데이터는 구글 로그인 정보를 담고 있음
//                resultLogin(inAccount); // 로그인 결과 값 출력 수행하라는 메소드
//            }
//        }
//    }
    private void resultLogin(GoogleSignInAccount inAccount) {
        AuthCredential credential = GoogleAuthProvider.getCredential(inAccount.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) { // 로그인 성공시
                    Toast.makeText(MainActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
//                    intent.putExtra("nickName",inAccount.getDisplayName());
//                    intent.putExtra("photoUrl",String.valueOf(inAccount.getPhotoUrl()); // String.valueOf() 특정 자료형을 String 형태로 변환
//
//                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "로그인 실패..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 입력된 사용자 이메일과 비밀번호가 유효한지 확인하는 메소드
    private boolean isValidCredentials(String UserEmail, String UserPwd) {
        return UserEmail.equals(VALID_USEREMAIL) && UserPwd.equals(VALID_PASSWORD);
    }
}