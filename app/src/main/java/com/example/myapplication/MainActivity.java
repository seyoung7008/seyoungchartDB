package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.graphics.Color;
import android.os.Bundle;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView ShowValue_TestView, ShowValue_TestView2, ShowValue_TestView3;
    EditText Value_EditText, Date_EditText;
    Button Save_Button, Show_Button, Reset_Button;
    TextView Today_Button, Week_Button, Mount_Button;
    TestDB testDB;
    SQLiteDatabase sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testDB = new TestDB(this);

        ShowValue_TestView = (TextView) findViewById(R.id.textView);
        ShowValue_TestView.setMovementMethod(new ScrollingMovementMethod());

        ShowValue_TestView2 = (TextView) findViewById(R.id.textView3);
        ShowValue_TestView2.setMovementMethod(new ScrollingMovementMethod());
        ShowValue_TestView3 = (TextView) findViewById(R.id.textView4);
        // ShowValue_TestView3.setMovementMethod(new ScrollingMovementMethod());

        Value_EditText = (EditText) findViewById(R.id.editText);
        Date_EditText = (EditText) findViewById(R.id.editText2);

        SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");


        Date time = new Date();

        String time1 = format1.format(time);

        Date_EditText.setText(time1);


        Save_Button = (Button) findViewById(R.id.button);
        Save_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat fm1 = new SimpleDateFormat("yyyy-MM-dd");
                String date = fm1.format(new Date());
                sql = testDB.getWritableDatabase();
                String CheckDate = Date_EditText.getText().toString();
                if (CheckDate.matches("")) {
                    sql.execSQL("INSERT INTO member VALUES(null,'"
                            + Value_EditText.getText() + "','"
                            + date + "');"
                    );
                } else {
                    sql.execSQL("INSERT INTO member VALUES(null,'"
                            + Value_EditText.getText() + "','"
                            + Date_EditText.getText() + "');"
                    );
                }

                sql.close();
            }
        });

        Show_Button = (Button) findViewById(R.id.button2);
        Show_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sql = testDB.getReadableDatabase();
                Cursor cursor;
                cursor = sql.rawQuery("SELECT * FROM MEMBER;", null);

                String Value2 = "Value" + "\r\n";

                while (cursor.moveToNext()) {
                    Value2 += cursor.getString(2) + "         " + cursor.getString(1) + "\r\n";
                }
                ShowValue_TestView.setText(Value2);
                cursor.close();
                sql.close();
/*
                sql = testDB.getReadableDatabase();
                Cursor cursor2;
                cursor2 = sql.rawQuery("SELECT * FROM MEMBER2;", null);

                String Value3 = "Value" + "\r\n";

                while (cursor2.moveToNext())
                {
                    Value3 += cursor2.getString(2) + "         " + cursor2.getString(1) + "\r\n";
                }
                ShowValue_TestView2.setText(Value3);
                cursor2.close();
                sql.close();
*/
            }
        });

        Reset_Button = (Button) findViewById(R.id.button3);
        Reset_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sql = testDB.getWritableDatabase();
                testDB.onUpgrade(sql, 1, 2);
                sql.close();
            }
        });

        Today_Button = (TextView) findViewById(R.id.button6);
        //Today_Button.setOnClickListener(new View.OnClickListener() {
        // @Override
        //   public void onClick(View v) {
        // ShowValue_TestView2.setText("Value");
        sql = testDB.getReadableDatabase();
        Cursor cursor;
        cursor = sql.rawQuery("SELECT * FROM MEMBER Where member.Testdate = date('now', 'localtime');", null);

        String Value1 = "";
        String Value2 = "";
        int sum = 0;
        int count = 1;
        while (cursor.moveToNext()) {
            Value1 = cursor.getString(2);

            Value2 = cursor.getString(1);
            sum += Integer.parseInt(Value2);
            count++;
        }
        if(count == 1){
            sum = sum / count;  //db에 오늘날짜의 데이터값 없으면 중지되는 상황발생 못고침. ㅇ 고침 킼
            Value1 = time1;
            Value1 += " 오늘의 몸무게 ! ";
            Today_Button.setText(Value1);
        }else{
            count = count-1;
            sum = sum/count;
            Value1 += " 오늘의 몸무게 ! ";
            Today_Button.setText(Value1);
        }




        Value2 = Integer.toString(sum);
        Value2 += " kg 입니다 ! \n";
        ShowValue_TestView2.setText(Value2);
        cursor.close();
        sql.close();
        Log.v("일일 버튼", "누름");
        //   }
        //   });

        Week_Button = (TextView) findViewById(R.id.button7);
        //    Week_Button.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //     public void onClick(View v) {
        ShowValue_TestView3.setText("Value");
        sql = testDB.getReadableDatabase();
        //    Cursor cursor;
        cursor = sql.rawQuery("SELECT member2.Testdate, IFNULL(AVG(Member.lux_val),0) "
                + "FROM Member2 LEFT OUTER JOIN member ON Member.Testdate = member2.Testdate "
                + "GROUP BY MEMBER2.TESTDATE "
                + "HAVING member2.Testdate >= date('now','weekday 0', '-7 days', 'localtime') AND member2.Testdate <= date('now','weekday 0', '-1 days', 'localtime')"
                + "ORDER BY MEMBER2.TESTDATE;", null);

        String Value3 = "";

        while (cursor.moveToNext()) {
            Value3 += cursor.getString(0) + "         " + cursor.getString(1) + "\r\n";
        }
        Week_Button.setText("일주일간의 몸무게 기록");
        ShowValue_TestView3.setText(Value3);
        cursor.close();
        sql.close();
        Log.v("주간 버튼", "누름");
        //     }
        //  });

    /*    Mount_Button = (Button)findViewById(R.id.button8);
        Mount_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowValue_TestView2.setText("");
                sql = testDB.getReadableDatabase();
                Cursor cursor;
                cursor = sql.rawQuery("SELECT member2.Testdate, IFNULL(AVG(Member.lux_val),0) "
                        + "FROM Member2 LEFT OUTER JOIN member ON Member.Testdate = member2.Testdate "
                        + "GROUP BY MEMBER2.TESTDATE "
                        + "HAVING member2.Testdate >= date('now','start of month','localtime') AND member2.Testdate <= date('now','start of month','+1 month','-1 day','localtime')"
                        + "ORDER BY MEMBER2.TESTDATE;", null);

                String Value2 = "Value" + "\r\n";

                while (cursor.moveToNext())
                {
                    Value2 += cursor.getString(0) + "         " + cursor.getString(1) + "\r\n";
                }
                ShowValue_TestView2.setText(Value2);
                cursor.close();
                sql.close();
                Log.v("월간 버튼","누름");
            }
        });*/

        SimpleDateFormat fm1 = new SimpleDateFormat("yyyy-MM");
        String date = fm1.format(new Date());
        Calendar calendar = Calendar.getInstance();

        Log.v(calendar.getActualMaximum(Calendar.DATE) + "", "");
        sql = testDB.getWritableDatabase();
        sql.execSQL("DROP TABLE IF EXISTS member2");
        sql.execSQL("create table member2 (_id INTEGER PRIMARY KEY AUTOINCREMENT, lux_val char(20), Testdate date(10))");
        for (int i = 1; i <= calendar.getActualMaximum(Calendar.DATE); i++) {
            if (i < 10) {
                sql.execSQL("INSERT INTO member2 VALUES(null,0,'"
                        + date + "-0" + i + "');"
                );
            } else {
                sql.execSQL("INSERT INTO member2 VALUES(null,0,'"
                        + date + "-" + i + "');"
                );
            }
        }



        drawLineChart();
    }

    private void drawLineChart() {
        LineChart lineChart = findViewById(R.id.lineChart);
        List<Entry> lineEntries = getDataSet();
        LineDataSet lineDataSet = new LineDataSet(lineEntries, getString(R.string.oil_price));
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setLineWidth(2);
        lineDataSet.setColor(Color.RED);
        lineDataSet.setCircleColor(Color.YELLOW);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleHoleRadius(3);
        lineDataSet.setDrawHighlightIndicators(true);
        lineDataSet.setHighLightColor(Color.RED);
        lineDataSet.setValueTextSize(12);
        lineDataSet.setValueTextColor(Color.DKGRAY);
        YAxis yAxisRight = lineChart.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);
        XAxis xAxis = lineChart.getXAxis(); // x 축 설정
      //  xAxis.setPosition(XAxis.XAxisPosition.TOP); //x 축 표시에 대한 위치 설정
        //xAxis.setValueFormatter(new ChartXValueFormatter()); //X축의 데이터를 제 가공함. new ChartXValueFormatter은 Custom한 소스
        xAxis.setLabelCount(7, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌




        LineData lineData = new LineData(lineDataSet);
        lineChart.getDescription().setText(getString(R.string.price_in_last_12_days));
        lineChart.getDescription().setTextSize(12);
        lineChart.setDrawMarkers(true);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.animateY(1000);
        lineChart.getXAxis().setGranularityEnabled(true);
        lineChart.getXAxis().setGranularity(1.0f);
        lineChart.getXAxis().setLabelCount(lineDataSet.getEntryCount());
        lineChart.setData(lineData);
    }

    private List<Entry> getDataSet() {
        List<Entry> lineEntries = new ArrayList<Entry>();
       // lineEntries.add(new Entry(0, 1));

        sql = testDB.getReadableDatabase();
    Cursor cursor;
        cursor = sql.rawQuery("SELECT member2.Testdate, IFNULL(AVG(Member.lux_val),0) "
                + "FROM Member2 LEFT OUTER JOIN member ON Member.Testdate = member2.Testdate "
                + "GROUP BY MEMBER2.TESTDATE "
                + "HAVING member2.Testdate >= date('now','weekday 0', '-7 days', 'localtime') AND member2.Testdate <= date('now','weekday 0', '-1 days', 'localtime')"
                + "ORDER BY MEMBER2.TESTDATE;", null);
        String Value1 = "";
        String Value2 = "";
        int sum1,sum2 = 0;
      /*  while (cursor.moveToNext()) { //values에 데이터를 담는 과정
            String dateTime = cursor.getString(0);
            long dt = Integer.parseInt(dateTime);
            String weight = cursor.getString(1);
            float we = Integer.parseInt(weight);
            lineEntries.add(new Entry(dt, we));
        }*/

        /*while (cursor.moveToNext()) {
            Value3 += cursor.getString(0) + "         " + cursor.getString(1) + "\r\n";
        }*/
        lineEntries.add(new Entry(15, 3));
        lineEntries.add(new Entry(16, 3));
        lineEntries.add(new Entry(17, 4));
        lineEntries.add(new Entry(18, 2));
        lineEntries.add(new Entry(19, 3));
        lineEntries.add(new Entry(20, 3));
        lineEntries.add(new Entry(21, 4));
      /*  while (cursor.moveToNext()) {
            Value1 = cursor.getString(0);
            sum1 = Integer.parseInt(Value1);
            Value2 = cursor.getString(1);
            sum2 = Integer.parseInt(Value2);

            lineEntries.add(new Entry(sum1, sum2));
        }*/
     /*   while (cursor.moveToNext()) {
            Value1 += cursor.getString(0) + "         " + cursor.getString(1) + "\r\n";
        }*/
        return lineEntries;
    }
}
