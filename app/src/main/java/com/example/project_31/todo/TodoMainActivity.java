package com.example.project_31.todo;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.LinearLayout.LayoutParams;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.example.project_31.todoVO.TodoVO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

    HashMap<String, Object> hashMap = new HashMap<>();
    int count = 1;

    // 데이터베이스 연동
    DatabaseReference todoListDB = FirebaseDatabase.getInstance().getReference("TodoList");

    ArrayList<String> todoListKeys;
    // 카테고리 색상
    String[] categoryColor={"categorypink","categorybrown","categorydarkblue","categorygray",
            "categoryred","categoryorange","categorypurple","categoryrightpink"};

    // 랜덤 인덱스를 생성하기 위해 Random 객체를 생성합니다.
    Random random = new Random();

    Drawable drawable ;

    // 오늘날짜 / 선택날짜
    String selectDay;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_main_layout);

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

        String yearString = String.valueOf(year);
        String monthString = String.valueOf(month);
        String dayString = String.valueOf(day);

        selectDay = yearString + monthString + dayString;

        monthlyMoveText.setText(year + "년 " + month + "월");

        readDB();

        // 날짜 선택시 날짜에 맞는 투두리스트 호출
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                year = date.get(Calendar.YEAR);
                month = date.get(Calendar.MONTH)+1;
                day = date.get(Calendar.DAY_OF_MONTH);

                String yearString = String.valueOf(year);
                String monthString = String.valueOf(month);
                String dayString = String.valueOf(day);

                selectDay = yearString + monthString + dayString;
                readDB();
            }
        });

        // + 카테고리 버튼 눌렀을 때
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
                        pushButton(value);

                        alertDialog.dismiss(); // 다이얼로그 닫기
                    }
                });
            }
        }); // onClick categoryAddBtn

        // - 카테고리 버튼 눌렀을 때
        categoryRemoveBtn = findViewById(R.id.categoryRemoveBtn);

        // 삭제
        categoryRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemoveDialog();
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
        todoListDB.child(selectDay).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                todoListKeys = new ArrayList<>();
                HashMap<String, ArrayList<String>> todoListValues = new HashMap<>();

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String categoryKey = categorySnapshot.getKey();
                    todoListKeys.add(categoryKey);
                    ArrayList<String> todoList = new ArrayList<>();

                    for (DataSnapshot todoSnapshot : categorySnapshot.getChildren()) {
                        String todoListValue = todoSnapshot.getValue(String.class);

                        if (todoSnapshot.getKey().startsWith(categoryKey) || todoSnapshot.getKey().startsWith("color")) {
                            if(!todoSnapshot.getValue().equals("")){
                                String todoKey = todoSnapshot.getKey();
                                todoList.add(todoListValue);
                                todoListValues.put(categoryKey, todoList);
                                hashMap.put(todoKey,todoListValue);
                            }
                        }
                    }
                }
                DBpushButton(todoListKeys, todoListValues);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 취소될 때 호출되는 메서드
            }
        });
    } // method readDB()

    private void DBpushButton(ArrayList<String> cateGory, HashMap<String, ArrayList<String>> todoListValues) {
//        Set<String> keys = item1.keySet(); // Hashmap에있는 key값 반환

        // 이전에 생성된 모든 뷰 제거
        dynamicLayout.removeAllViews(); //이거안하면 이상한거 하나 생김


        // 카테고리생성
        for(String item: cateGory) {

            // ArrayList에서 일치하는 항목을 찾아 따로 추출하고, ArrayList에서 제거
            // 카테고리에 저장된 색상뽑아내기
            ArrayList<String> categoryColors = new ArrayList<>(); // 색상뽑아내기
            Iterator<String> iterator = todoListValues.get(item).iterator();
            while (iterator.hasNext()) {
                String insertColor = iterator.next();
                for(String c : categoryColor){
                    if(c.equals(insertColor)){
                        categoryColors.add(insertColor);
                        iterator.remove();
                    }
                }
            }

                int resourceId = getResources().getIdentifier(categoryColors.get(0), "drawable", getPackageName());
                drawable = ResourcesCompat.getDrawable(getResources(), resourceId, null);


//            // randomValue에 해당하는 drawable 리소스의 식별자를 가져옵니다.
//            int resourceId = getResources().getIdentifier(categoryColor.get(0), "drawable", getPackageName());
//            drawable = ResourcesCompat.getDrawable(getResources(), resourceId, null);

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
                    DBTodoListAdd(categoryLayout,item,categoryColors.get(0));
                }
            });
            categoryLayout.addView(categoryBtn, new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));


            //todoList생성

            ArrayList<String> todoListValue = todoListValues.get(item);

            if (todoListValue != null) {
                for (String value : todoListValue) {
                    DBTodoListRead(categoryLayout,value,item,categoryColors.get(0));
                } // for
            } //if

            dynamicLayout.addView(categoryLayout);



        }//for

    } // DBpushButton

    private void showRemoveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("삭제할 카테고리 선택");

        // 다이얼로그 내용을 담을 레이아웃을 생성합니다.
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);

        final List<String> todoKeys = new ArrayList<>(todoListKeys);

        todoListDB.child(selectDay).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, ArrayList<String>> todoListValues = new HashMap<>();

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String categoryKey = categorySnapshot.getKey();
                    todoListKeys.add(categoryKey);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 취소될 때 호출되는 메서드
            }
        });

        // ArrayList에 있는 각 항목마다 체크박스를 생성하고 레이아웃에 추가합니다.
        for (int i = 0; i < todoKeys.size(); i++) {
            String item = todoKeys.get(i);

            CheckBox checkBox = new CheckBox(this);
            checkBox.setId(i);
            checkBox.setText(item);

            container.addView(checkBox);
        }

        builder.setView(container);

        // 확인 버튼 클릭 이벤트를 처리합니다.
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<Integer> removeIds = new ArrayList<>(); // 삭제할 체크박스의 ID를 저장할 리스트
                for (int i = 0; i < container.getChildCount(); i++) {
                    CheckBox checkBox = (CheckBox) container.getChildAt(i);
                    if (checkBox.isChecked()) {
                        int id = checkBox.getId();
                        String key = checkBox.getText().toString();
                        todoListDB.child(selectDay).child(key).removeValue();
                        removeIds.add(id);

                        Iterator<String> iterator = hashMap.keySet().iterator();
                        while (iterator.hasNext()) {
                            String key1 = iterator.next();
                            if (key1.startsWith(key)) {
                                iterator.remove(); // 해당 key 값을 가진 항목 제거
                            }
                        }

                    }
                }
                // 삭제할 체크박스의 ID를 역순으로 정렬하여 제거합니다.
                Collections.sort(removeIds, Collections.reverseOrder());
                for (int id : removeIds) {
                    todoKeys.remove(id);
                }
                // 삭제 후 화면을 다시 동기화합니다.
                readDB();
            }
        });

        builder.setNegativeButton("취소", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void DBTodoListAdd(LinearLayout categoryLayout,String cateGory,String color){

        String fixcolor = color.substring(8); // ex) categroy_brown -> brown
        int resourceId = getResources().getIdentifier("oncheck_" + fixcolor, "drawable", getPackageName());

        int maxLength = 15;
        EditText todoListEd = new EditText(this);
        TextView todoListTv = new TextView(this);

        todoListEd.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(maxLength)}); //글자수제한

        todoListTv.setVisibility(View.GONE);

        // 체크 버튼 , 텍스트 , 삭제버튼을 담을 LinearLayout 생성
        LinearLayout checkTodoRemoveLayout = new LinearLayout(this);
        checkTodoRemoveLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        checkTodoRemoveLayout.setOrientation(LinearLayout.HORIZONTAL);

        CheckBox vCheckBox = new CheckBox(this);
        LayoutParams checkboxParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        checkboxParams.weight = 1;
        vCheckBox.setLayoutParams(checkboxParams);
        boolean imgIsChecked  = false; // 체크박스 상태를 체크해 이미지 변경하기위한 변수
        vCheckBox.setPadding(0,20,0,20);


        // 체크박스 클릭시 이미지 변경
        vCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 체크 상태가 변경될 때마다 SharedPreferences 업데이트
//                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs"+ cateGory + todoList, MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putBoolean("isChecked", isChecked);
//                editor.apply();

                if (isChecked) {
                    vCheckBox.setButtonDrawable(resourceId); // 체크된 상태 이미지 설정
                } else {
                    vCheckBox.setButtonDrawable(R.drawable.offcheck); // 체크되지 않은 상태 이미지 설정
                }
                underlineTextView(checkTodoRemoveLayout,isChecked); // 체크가 되면 밑줄

            }
        });


        LayoutParams textParams = new LayoutParams(
                0,
                LayoutParams.WRAP_CONTENT
        );
        textParams.weight = 10;
        todoListTv.setLayoutParams(textParams);
        todoListEd.setLayoutParams(textParams);
        todoListEd.setTextSize(20);
        todoListEd.setHint("15자 미만 입력 해주세요.");
        todoListTv.setTextSize(20);
        todoListTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoListTv.setVisibility(View.GONE);
                todoListEd.setVisibility(View.VISIBLE);
            }
        }); //todoListTv.setOnKeyListener



        TextView deleteButton = new TextView(this);
        LayoutParams deleteParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        deleteParams.weight = 1;
        deleteButton.setLayoutParams(deleteParams);
        deleteButton.setPadding(0,20,20,20);
        deleteButton.setText("X");


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryLayout.removeView(checkTodoRemoveLayout);
                // categoryKey 아래에 있는 자식 노드들을 가져옵니다.
                todoListDB.child(cateGory).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String childKey = childSnapshot.getKey();
                            String childValue = childSnapshot.getValue(String.class);
                            String node = todoListTv.getText().toString();
                            // 원하는 값인 를 가진 자식 노드를 삭제합니다.
                            if (childValue.equals(node)) {
                                todoListDB.child(selectDay).child(cateGory).child(childKey).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // 에러 처리
                    }
                });
            }
        });

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

                        String newKey = cateGory;

                        while (hashMap.containsKey(newKey)) {
                            newKey = cateGory + "_" + count; // 다른 이름으로 key를 변경
                            count++;
                        }
                        hashMap.put(newKey, todoList);



                        HashMap<String, Object> filteredMap = new HashMap<>();
                        for (String key : hashMap.keySet()) {
                            if (key.startsWith(cateGory) || key.startsWith("color")) {
                                String value = (String) hashMap.get(key);
                                filteredMap.put(key, value);
                            }
                        }
                        todoListDB.child(selectDay).child(cateGory).setValue(filteredMap);
                        todoListTv.setText(todoList);
                        todoListTv.setVisibility(View.VISIBLE);
                        todoListEd.setVisibility(View.GONE);
                        break;
                }

                return false;
            }
        });//todoListEd.setOnKeyListener

        // 만들어논 레이아웃 화면에 보이기
        checkTodoRemoveLayout.addView(vCheckBox);
        checkTodoRemoveLayout.addView(todoListTv);
        checkTodoRemoveLayout.addView(todoListEd);
        checkTodoRemoveLayout.addView(deleteButton);
        checkTodoRemoveLayout.setPadding(0,30,0,0);

        categoryLayout.addView(checkTodoRemoveLayout);


    }//DBtodoListAdd

    private void DBTodoListRead(LinearLayout categoryLayout,String todoList,String cateGory,String color){
        int vButtonState = 0;

        DatabaseReference selectDayDB = todoListDB.child(selectDay);

        String fixcolor = color.substring(8); // ex) categroy_brown -> brown
        int resourceId = getResources().getIdentifier("oncheck_" + fixcolor, "drawable", getPackageName());

        int maxLength = 15;
        EditText todoListEd = new EditText(this);
        todoListEd.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(maxLength)}); //글자수제한
        todoListEd.setVisibility(View.GONE);

        // 체크 버튼 , 텍스트 , 삭제버튼을 담을 LinearLayout 생성
        LinearLayout checkTodoRemoveLayout = new LinearLayout(this);
        checkTodoRemoveLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        checkTodoRemoveLayout.setOrientation(LinearLayout.HORIZONTAL);

        CheckBox vCheckBox = new CheckBox(this);
        LayoutParams checkboxParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        checkboxParams.weight = 1;
        vCheckBox.setLayoutParams(checkboxParams);
        vCheckBox.setPadding(0,20,0,20);


        // SharedPreferences를 사용하여 체크 상태를 저장
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs"+ cateGory + todoList, MODE_PRIVATE);
        final boolean isChecked = sharedPreferences.getBoolean("isChecked", false);
        vCheckBox.setChecked(isChecked); // 저장된 값으로 Checkbox 상태 설정

        // 체크박스 클릭시 이미지 변경
        vCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 체크 상태가 변경될 때마다 SharedPreferences 업데이트
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs"+ cateGory + todoList, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isChecked", isChecked);
                editor.apply();

                if (isChecked) {
                    vCheckBox.setButtonDrawable(resourceId); // 체크된 상태 이미지 설정
                } else {
                    vCheckBox.setButtonDrawable(R.drawable.offcheck); // 체크되지 않은 상태 이미지 설정
                }
                    underlineTextView(checkTodoRemoveLayout,isChecked); // 체크가 되면 밑줄

            }
        });


        TextView todoListTv = new TextView(this);
        LayoutParams textParams = new LayoutParams(
                0,
                LayoutParams.WRAP_CONTENT
        );
        textParams.weight = 10;
        todoListTv.setLayoutParams(textParams);
        todoListEd.setLayoutParams(textParams);
        todoListEd.setTextSize(20);
        todoListEd.setText(todoList);
        todoListTv.setTextSize(20);
        todoListTv.setText(todoList);
        todoListTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoListTv.setVisibility(View.GONE);
                todoListEd.setVisibility(View.VISIBLE);
            }
        }); //todoListTv.setOnKeyListener



        TextView deleteButton = new TextView(this);
        LayoutParams deleteParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        deleteParams.weight = 1;
        deleteButton.setLayoutParams(deleteParams);
//        deleteButton.setWidth(100);
        deleteButton.setPadding(0,20,20,20);
        deleteButton.setText("X");
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // checkbox 상태 담고있는 editor

                categoryLayout.removeView(checkTodoRemoveLayout);
                // categoryKey 아래에 있는 자식 노드들을 가져옵니다.
                selectDayDB.child(cateGory).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String childKey = childSnapshot.getKey();
                            String childValue = childSnapshot.getValue(String.class);

                            // 원하는 값을 가진 자식 노드를 삭제합니다.
                            if (childValue.equals(todoList)) {
                                selectDayDB.child(cateGory).child(childKey).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // 에러 처리
                    }
                });
            }
        });

        todoListEd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        if (todoListEd.getText().toString().equals("")) {
                            categoryLayout.removeView(todoListEd);
                            categoryLayout.removeView(todoListTv);
                        }
                        String dbIntodoList = todoListEd.getText().toString();

                        String newKey = cateGory;

                        while (hashMap.containsKey(newKey)) {
                            newKey = cateGory + "_" + count; // 다른 이름으로 key를 변경
                            count++;
                        }
                        hashMap.put(newKey, dbIntodoList);



                        HashMap<String, Object> filteredMap = new HashMap<>();
                        for (String key : hashMap.keySet()) {
                            if (key.startsWith(todoList)) {
                                String value = (String) hashMap.get(key);
                                filteredMap.put(key, value);
                            }
                        }
                        todoListDB.child(selectDay).child(cateGory).setValue(filteredMap);
                        todoListTv.setText(dbIntodoList);
                        todoListTv.setVisibility(View.VISIBLE);
                        todoListEd.setVisibility(View.GONE);

                        break;
                }

                return false;
            }
        });//todoListEd.setOnKeyListener

        // 만들어논 레이아웃 화면에 보이기
        checkTodoRemoveLayout.addView(vCheckBox);
        checkTodoRemoveLayout.addView(todoListTv);
        checkTodoRemoveLayout.addView(todoListEd);
        checkTodoRemoveLayout.addView(deleteButton);
        checkTodoRemoveLayout.setPadding(0,30,0,0);

        // 상태값저장
        TextView textView = (TextView) checkTodoRemoveLayout.getChildAt(1);
        if (isChecked) {
            vCheckBox.setButtonDrawable(resourceId); // 체크된 상태 이미지 설정
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            vCheckBox.setButtonDrawable(R.drawable.offcheck); // 체크되지 않은 상태 이미지 설정
            textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }



        categoryLayout.addView(checkTodoRemoveLayout);

        Log.d("checkTodo",String.valueOf(checkTodoRemoveLayout.getId()));

    }//DBtodoListRead

    private void pushButton(String category) {

        // 배열에서 랜덤한 값을 출력합니다.
        int randomIndex = random.nextInt(categoryColor.length);  // 0부터 배열의 길이-1까지의 랜덤 인덱스를 생성합니다.
        String randomValue = categoryColor[randomIndex];
        // randomValue에 해당하는 drawable 리소스의 식별자를 가져옵니다.
        int resourceId = getResources().getIdentifier(randomValue, "drawable", getPackageName());
        drawable = ResourcesCompat.getDrawable(getResources(), resourceId, null);

        hashMap = new HashMap<>(); // read()에서 저장된 hashMap을 초기화하기
        hashMap.put("color",randomValue);
        todoListDB.child(selectDay).child(category).setValue(hashMap);



        LinearLayout categoryLayout = new LinearLayout(this);
        categoryLayout.setOrientation(LinearLayout.VERTICAL);

        Button categoryBtn = new Button(this); // 새로운 버튼 생성
        categoryBtn.setText(category); // 입력받은 카테고리값 넣음
        categoryBtn.setBackground(drawable);
        categoryBtn.setTextColor(Color.WHITE);
        categoryBtn.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        categoryBtn.setPadding(36, 0, 0, 0); // 카테고리 내부 글자 패딩 설정
        categoryLayout.setPadding(50, 20, 50, 10); // 카테고리 버튼 패딩 설정
        categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoListAdd(categoryLayout,category,randomValue);
            }
        });

        categoryLayout.addView(categoryBtn,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        dynamicLayout.addView(categoryLayout);

    } // pushButton

    private void TodoListAdd(LinearLayout categoryLayout,String cateGory,String color){

        String fixcolor = color.substring(8); // ex) categroy_brown -> brown
        int resourceId = getResources().getIdentifier("oncheck_" + fixcolor, "drawable", getPackageName());

        int maxLength = 15;
        EditText todoListEd = new EditText(this);
        TextView todoListTv = new TextView(this);

        todoListEd.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(maxLength)}); //글자수제한

        todoListTv.setVisibility(View.GONE);

        // 체크 버튼 , 텍스트 , 삭제버튼을 담을 LinearLayout 생성
        LinearLayout checkTodoRemoveLayout = new LinearLayout(this);
        checkTodoRemoveLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        checkTodoRemoveLayout.setOrientation(LinearLayout.HORIZONTAL);

        CheckBox vCheckBox = new CheckBox(this);
        LayoutParams checkboxParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        checkboxParams.weight = 1;
        vCheckBox.setLayoutParams(checkboxParams);
        boolean imgIsChecked  = false; // 체크박스 상태를 체크해 이미지 변경하기위한 변수
        vCheckBox.setPadding(0,20,0,20);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs"+checkTodoRemoveLayout, MODE_PRIVATE);
        boolean isChecked = sharedPreferences.getBoolean("isChecked", false);
        vCheckBox.setChecked(isChecked); // 저장된 값으로 Checkbox 상태 설정


        // 체크박스 클릭시 이미지 변경
        vCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 체크 상태가 변경될 때마다 SharedPreferences 업데이트
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isChecked", isChecked);
                editor.apply();

                if (isChecked) {
                    vCheckBox.setButtonDrawable(resourceId); // 체크된 상태 이미지 설정
                } else {
                    vCheckBox.setButtonDrawable(R.drawable.offcheck); // 체크되지 않은 상태 이미지 설정
                }
                underlineTextView(checkTodoRemoveLayout,isChecked); // 체크가 되면 밑줄
//                imgIsChecked = imgIsChecked;

            }
        });

        if (isChecked) {
            vCheckBox.setButtonDrawable(resourceId); // 체크된 상태 이미지 설정
        } else {
            vCheckBox.setButtonDrawable(R.drawable.offcheck); // 체크되지 않은 상태 이미지 설정
        }


        LayoutParams textParams = new LayoutParams(
                0,
                LayoutParams.WRAP_CONTENT
        );
        textParams.weight = 10;
        todoListTv.setLayoutParams(textParams);
        todoListEd.setLayoutParams(textParams);
        todoListEd.setTextSize(20);
        todoListEd.setHint("15자 미만 입력 해주세요.");
        todoListTv.setTextSize(20);
        todoListTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoListTv.setVisibility(View.GONE);
                todoListEd.setVisibility(View.VISIBLE);
            }
        }); //todoListTv.setOnKeyListener



        TextView deleteButton = new TextView(this);
        LayoutParams deleteParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        deleteParams.weight = 1;
        deleteButton.setLayoutParams(deleteParams);
        deleteButton.setPadding(0,20,20,20);
        deleteButton.setText("X");


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryLayout.removeView(checkTodoRemoveLayout);
                // categoryKey 아래에 있는 자식 노드들을 가져옵니다.
                todoListDB.child(selectDay).child(cateGory).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String childKey = childSnapshot.getKey();
                            String childValue = childSnapshot.getValue(String.class);
                            String node = todoListTv.getText().toString();
                            // 원하는 값인 를 가진 자식 노드를 삭제합니다.
                            if (childValue.equals(node)) {
                                todoListDB.child(cateGory).child(childKey).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // 에러 처리
                    }
                });
            }
        });

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

                        String newKey = cateGory;

                        while (hashMap.containsKey(newKey)) {
                            newKey = cateGory + "_" + count; // 다른 이름으로 key를 변경
                            count++;
                        }
                        hashMap.put(newKey, todoList);



                        HashMap<String, Object> filteredMap = new HashMap<>();
                        for (String key : hashMap.keySet()) {
                            if (key.startsWith(cateGory) || key.startsWith("color")) {
                                String value = (String) hashMap.get(key);
                                filteredMap.put(key, value);
                            }
                        }
                        todoListDB.child(selectDay).child(cateGory).setValue(filteredMap);
                        todoListTv.setText(todoList);
                        todoListTv.setVisibility(View.VISIBLE);
                        todoListEd.setVisibility(View.GONE);
                        break;
                }

                return false;
            }
        });//todoListEd.setOnKeyListener

        // 만들어논 레이아웃 화면에 보이기
        checkTodoRemoveLayout.addView(vCheckBox);
        checkTodoRemoveLayout.addView(todoListTv);
        checkTodoRemoveLayout.addView(todoListEd);
        checkTodoRemoveLayout.addView(deleteButton);
        checkTodoRemoveLayout.setPadding(0,30,0,0);
        Log.d("특정값1",todoListTv.toString());

        categoryLayout.addView(checkTodoRemoveLayout);


    }//todoListAdd

    private void underlineTextView(LinearLayout layout,boolean isChecked) {
        TextView textView = (TextView) layout.getChildAt(1);
        if (isChecked) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

} // main