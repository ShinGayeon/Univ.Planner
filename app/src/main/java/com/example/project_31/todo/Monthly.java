package com.example.project_31.todo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_31.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Collections;

public class Monthly extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monthly);

// 월간 달력 시작
        MaterialCalendarView materialCalendarView = findViewById(R.id.calendarView);
        OneDayDecorator oneDayDecorator = new OneDayDecorator();

        // 토요일, 일요일 색상 변경 (SaturdayDecorator/SundayDecorator)
        materialCalendarView.addDecorators(new SundayDecorator(), new SaturdayDecorator());
        // 클릭 날짜 표시
        materialCalendarView.addDecorators(new MySelectorDecorator(this));

        // 오늘 날짜 폰트 변경
        materialCalendarView.addDecorators(oneDayDecorator);

        // 특정 날짜 표시 (현재는 오늘 날짜에 표시)
        MaterialCalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setSelectedDate(CalendarDay.today());

        calendarView.addDecorators(new EventDecorator(Color.RED, Collections.singleton(CalendarDay.today())));

        // 날짜 선택시 주간 달력으로 이동
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Intent intent = new Intent(getApplicationContext(), TodoMainActivity.class);
                intent.putExtra("selectDate", date.getDate());
                startActivity(intent);
            }
        });



        // 월간 달력 끝
    }
}
