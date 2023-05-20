package com.example.project_31.todo;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.project_31.R;
import com.example.project_31.chat.ChatMainActivity;
import com.example.project_31.login.MainActivity;
import com.example.project_31.todoVO.TodoVO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class TodoMainActivity extends AppCompatActivity {

    // 달력 사용 코드
    TextView calendarTextview, monthlyMoveText, chatMoveText, test;
    int year, month, day;

    // 카테고리 버튼이 생성될 공간
    private LinearLayout dynamicLayout;

    // 카테고리 버튼 내부에 추가, 삭제 버튼 생성
    Button categoryAddBtn, categoryRemoveBtn;

    // 카테고리>투두리스트 사용 코드
    private ArrayList<String> todoList = new ArrayList<>() ;
    private HashMap<String,Object> TodoList1 = new HashMap<>();
    TodoVO item = new TodoVO();

    // 데이터베이스 연동
    DatabaseReference todoListDB = FirebaseDatabase.getInstance().getReference("TodoList");
    int todoListNum = 1;

    // 카테고리 색상
    String[] categoryColor={"categorypink","categoryblue","categorygreen","categoryyellow"};

    // 랜덤 인덱스를 생성하기 위해 Random 객체를 생성합니다.
    Random random = new Random();

    Drawable drawable ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_main_layout);

        readDB();
// 주간 달력 시작
        monthlyMoveText = findViewById(R.id.monthlyMoveText);
        dynamicLayout = findViewById(R.id.dynamicLayout);

        // 시작 날짜
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);
        // 종료 날짜 (현재 날짜 + 한 달 후)
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        // 가로 달력 실행
        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .configure()
                .formatTopText("EEE")
                .showBottomText(false)
                .end()
                .build();

        year = startDate.get(Calendar.YEAR);
        month = startDate.get(Calendar.MONTH)+2;
        day = startDate.get(Calendar.DAY_OF_MONTH);

        monthlyMoveText.setText(year + "년 " + month + "월");

        // 날짜 선택시 날짜에 맞는 투두리스트 호출
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                year = date.get(Calendar.YEAR);
                month = date.get(Calendar.MONTH)+1;
                day = date.get(Calendar.DAY_OF_MONTH);
            }
        });

        // 카테고리+ 버튼 눌렀을 때
        categoryAddBtn = findViewById(R.id.categoryAddBtn);

        //등록
        categoryAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final LinearLayout linear = (LinearLayout) View.inflate(com.example.project_31.todo.TodoMainActivity.this, R.layout.dialog_category, null);
                AlertDialog alertDialog = new AlertDialog.Builder(com.example.project_31.todo.TodoMainActivity.this)
                        .setView(linear)
                        .setCancelable(false)
                        .show();

                Button dialogButton = linear.findViewById(R.id.dialogBtn);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 버튼 클릭 이벤트 처리
                        EditText category = linear.findViewById(R.id.inputCategory);
                        String value = category.getText().toString();
                        addCategory(value);
                        todoListDB.child(value).child(value + "1").setValue("null");
                        pushButton(value);

                        alertDialog.dismiss(); // 다이얼로그 닫기
                    }
                });
            }
        });


        // 월간 달력으로 이동
        monthlyMoveText = (TextView) findViewById(R.id.monthlyMoveText);
        monthlyMoveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodoMainActivity.this, Monthly.class);
                startActivity(intent);
            }
        }); // 월간 달력으로 이동

        // 월간 달력으로 이동
        chatMoveText = (TextView) findViewById(R.id.chatMoveText);
        chatMoveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodoMainActivity.this, ChatMainActivity.class);
                startActivity(intent);
            }
        }); // 월간 달력으로 이동
    } // on

    public void readDB(){
        todoListDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> todoListKeys = new ArrayList<>();
                HashMap<String, ArrayList<String>> todoListValues = new HashMap<>();

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String categoryKey = categorySnapshot.getKey();
                    todoListKeys.add(categoryKey);
                    ArrayList<String> test = new ArrayList<>();

                    for (DataSnapshot todoSnapshot : categorySnapshot.getChildren()) {
                        String todoListValue = todoSnapshot.getValue(String.class);
                        String cherryKey = todoSnapshot.getKey().substring(0, categoryKey.length());
                        Log.d("출력뭐됨", cherryKey + " " + todoListValue);

                        if (todoSnapshot.getKey().startsWith(categoryKey)) {
                            test.add(todoListValue);
                            todoListValues.put(categoryKey, test);
                            Log.d("하잇",todoListValues.toString());
                        }
                    }
                }
                DBpushButton(todoListKeys, todoListValues);
                Log.d("뭘보내는데?",todoListValues.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 취소될 때 호출되는 메서드
            }
        });
    } // method readDB()

    private void DBpushButton(ArrayList<String> key, HashMap<String, ArrayList<String>> todoListValues) {
//        Set<String> keys = item1.keySet(); // Hashmap에있는 key값 반환

        // 이전에 생성된 모든 뷰 제거
        dynamicLayout.removeAllViews(); //이거안하면 이상한거 하나 생김

        for(String item: key) {

            // 배열에서 랜덤한 값을 출력합니다.
            int randomIndex = random.nextInt(categoryColor.length);  // 0부터 배열의 길이-1까지의 랜덤 인덱스를 생성합니다.
            String randomValue = categoryColor[randomIndex];
            // randomValue에 해당하는 drawable 리소스의 식별자를 가져옵니다.
            int resourceId = getResources().getIdentifier(randomValue, "drawable", getPackageName());
            drawable = ResourcesCompat.getDrawable(getResources(), resourceId, null);

            LinearLayout categoryLayout = new LinearLayout(this);
            categoryLayout.setOrientation(LinearLayout.VERTICAL);
            Button categoryBtn = new Button(this); // 새로운 버튼 생성
            categoryBtn.setText(item); // 입력받은 카테고리값 넣음
            categoryBtn.setBackground(drawable);
            categoryBtn.setTextColor(Color.WHITE);
            categoryBtn.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            categoryBtn.setPadding(36, 0, 0, 0); // 카테고리 내부 글자 패딩 설정
            categoryLayout.setPadding(50, 20, 0, 10); // 카테고리 버튼 패딩 설정
            categoryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBTodoListAdd(categoryLayout,item.toString());
                }
            });
            categoryLayout.addView(categoryBtn, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            ArrayList<String> todoListValue = todoListValues.get(item);
            if (todoListValue != null) {
                for (String value : todoListValue) {
                    TextView valueTextView = new TextView(this);
                    valueTextView.setText(value);
                    valueTextView.setTextSize(20);
                    valueTextView.setPadding(40, 0, 0, 0);
                    categoryLayout.addView(valueTextView);
                }
            }

            dynamicLayout.addView(categoryLayout);

        }//for

    } // DBpushButton

    private void DBTodoListAdd(LinearLayout categoryLayout,String cateGory){
        int maxLength = 15; //글자수 제한

        Toast.makeText(getApplicationContext(),cateGory,Toast.LENGTH_SHORT).show();
        int i;
        EditText todoListEd = new EditText(this);
        TextView todoListTv = new TextView(this);

        todoListEd.setSingleLine(true); //줄바꿈 금지
        todoListEd.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(maxLength)}); //글자수제한
        todoListEd.setWidth(1000); // EditText 가로사이즈
        todoListTv.setVisibility(View.GONE); // TextView 안보이게 설정
        todoListTv.setTextSize(20); // TextView 글씨 크기
        todoListTv.setPadding(40, 0, 0, 0);

        todoListEd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        if (todoListEd.getText().toString().equals("")) {
                            categoryLayout.removeView(todoListEd);
                            categoryLayout.removeView(todoListTv);
                        }
                        String todoList = todoListEd.getText().toString();
                        TodoList1.put(cateGory + todoListNum, todoList);

                        HashMap<String, Object> filteredMap = new HashMap<>();
                        for (String key : TodoList1.keySet()) {
                            if (key.startsWith(cateGory)) {
                                String value = (String) TodoList1.get(key);
                                filteredMap.put(key, value);
                            }
                        }
                        todoListDB.child(cateGory).setValue(filteredMap);
                        todoListTv.setText(todoList);
                        todoListTv.setVisibility(View.VISIBLE);
                        todoListEd.setVisibility(View.GONE);

                        // todoListDB.child(cateGory).setValue(TodoList1);

                        // todoListDB.child(value).child("todoList"+todoListNum).setValue(todoList);
                        todoListNum += 1;
                        break;
                }

                return false;
            }
        });//todoListEd.setOnKeyListener

        todoListTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoListTv.setVisibility(View.GONE);
                todoListEd.setVisibility(View.VISIBLE);
            }
        }); //todoListTv.setOnKeyListener

        categoryLayout.addView(todoListEd, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        categoryLayout.addView(todoListTv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

    }//DBtodoListAdd

    private void pushButton(String category) {
        // 배열에서 랜덤한 값을 출력합니다.
        int randomIndex = random.nextInt(categoryColor.length);  // 0부터 배열의 길이-1까지의 랜덤 인덱스를 생성합니다.
        String randomValue = categoryColor[randomIndex];
        // randomValue에 해당하는 drawable 리소스의 식별자를 가져옵니다.
        int resourceId = getResources().getIdentifier(randomValue, "drawable", getPackageName());
        drawable = ResourcesCompat.getDrawable(getResources(), resourceId, null);


        LinearLayout categoryLayout = new LinearLayout(this);
        categoryLayout.setOrientation(LinearLayout.VERTICAL);

        Button categoryBtn = new Button(this); // 새로운 버튼 생성
        categoryBtn.setText(category); // 입력받은 카테고리값 넣음
        categoryBtn.setBackground(drawable);
        categoryBtn.setTextColor(Color.WHITE);
        categoryBtn.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        categoryBtn.setPadding(36, 0, 0, 0); // 카테고리 내부 글자 패딩 설정
        categoryLayout.setPadding(50, 20, 0, 10); // 카테고리 버튼 패딩 설정
        categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoListAdd(categoryLayout,category);
            }
        });

        categoryLayout.addView(categoryBtn,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        dynamicLayout.addView(categoryLayout);

    } // pushButton

    private void TodoListAdd(LinearLayout categoryLayout,String cateGory){
        int maxLength = 15; //글자수 제한

        Toast.makeText(getApplicationContext(),cateGory,Toast.LENGTH_SHORT).show();
        int i;
        EditText todoListEd = new EditText(this);
        TextView todoListTv = new TextView(this);
        todoListEd.setSingleLine(true); //줄바꿈 금지
        todoListEd.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(maxLength)}); //글자수제한
        todoListEd.setWidth(1000); // EditText 가로사이즈
        todoListTv.setVisibility(View.GONE); // TextView 안보이게 설정
        todoListTv.setTextSize(20); // TextView 글씨 크기
        todoListTv.setPadding(40, 0, 0, 0);

        todoListEd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        if(todoListEd.getText().toString().equals("")){
                            categoryLayout.removeView(todoListEd);
                            categoryLayout.removeView(todoListTv);
                        }
                        String todoList = todoListEd.getText().toString();
                        TodoList1.put(cateGory+todoListNum,todoList);

                        HashMap<String, Object> filteredMap = new HashMap<>();
                        for (String key : TodoList1.keySet()) {
                            if (key.startsWith(cateGory)) {
                                String value = (String) TodoList1.get(key);
                                filteredMap.put(key, value);
                            }
                        }
                        todoListDB.child(cateGory).setValue(filteredMap);

                        todoListTv.setText(todoList);
                        todoListTv.setVisibility(View.VISIBLE);
                        todoListEd.setVisibility(View.GONE);

                        // todoListDB.child(cateGory).setValue(TodoList1);

                        // todoListDB.child(value).child("todoList"+todoListNum).setValue(todoList);
                        todoListNum+=1;
                        break;
                }

                return false;
            }
        });//todoListEd.setOnKeyListener

        todoListTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoListTv.setVisibility(View.GONE);
                todoListEd.setVisibility(View.VISIBLE);
            }
        }); //todoListTv.setOnKeyListener

        categoryLayout.addView(todoListEd, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        categoryLayout.addView(todoListTv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

    }//todoListAdd

    public void addCategory(String category){
        item.setCategory(category);
    }

} // main