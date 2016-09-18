package com.example.laser;


import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.laser.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private LineChart mChart;
    private Button button_pre;
    private Button button_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_history);

        //添加‘前页’‘后页’按钮点击响应
        button_pre= (Button)findViewById(R.id.title_pre);
        button_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //执行‘前页’翻页操作
                ExecutePrePage();
            }
        });

        button_next= (Button)findViewById(R.id.title_next);
        button_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                //执行‘后页’翻页操作
                ExecuteNextPage();
            }
        });

        mChart= (LineChart) findViewById(R.id.chart2);

        //初始化MPChart图表
        MPChartInit();

        //插入DataSet
        SimpleDateFormat sdf=new SimpleDateFormat("kk:mm");
        //String curtime=sdf.format(new java.util.Date());
        Date d=new Date();

        addDataSet(d,10);
    }

    //初始化MPChart图表
    public void MPChartInit(){
        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable value highlighting
        mChart.setHighlightEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);

        LineData data= new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        mChart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.RED);

        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.RED);
        xl.setAvoidFirstLastClipping(true);
        xl.setSpaceBetweenLabels(2);
        xl.setEnabled(true);
        xl.setDrawGridLines(true);
        xl.enableGridDashedLine(10f, 10f, 0f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.RED);
        leftAxis.setAxisMaxValue(100f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    //添加一个DataSet
    public void addDataSet(Date starttime, int count){
        LineData lineData= mChart.getLineData();
        if (lineData != null)
        {
            ArrayList<Entry> yValueList = new ArrayList<>();
            for (int i=0;i<count; i++){
                //取时间横轴
                SimpleDateFormat sdf= new SimpleDateFormat("kk:mm");
                //String data= sdf.format(starttime);

                lineData.addXValue(sdf.format(starttime.getTime()-(long)count*60*1000 +(long)i*60*1000));
                //lineData.addXValue(data);
                yValueList.add(new Entry((float)(40.0f+Math.random()*10f),i));
            }

            LineDataSet set =new LineDataSet(yValueList, "历史值：");
            set.setColor(0xFF0000FF);
            set.setCircleColor(0xFF0000FF);
            set.setValueTextSize(16f);

            lineData.addDataSet(set);
            mChart.notifyDataSetChanged();
            mChart.invalidate();
        }
    }

    //执行‘前页’翻页
    public void ExecutePrePage(){
        LineData linedata = mChart.getLineData();
        LineDataSet lineDataSet = linedata.getDataSetByIndex(0);

        List<String> xAxis =  linedata.getXVals();
        String xmin="";
        String xmax="";
        if (xAxis.size() >=2)
        {
            xmin= xAxis.get(0);
            xmax= xAxis.get(xAxis.size()-1);
        }

        //int dis= Integer.parseInt(xmax) - Integer.parseInt(xmin) +1;
        SimpleDateFormat dfs = new SimpleDateFormat("HH:mm");
        ParsePosition pos = new ParsePosition(0);
        Date begin = dfs.parse(xmax, pos);
        //Date end=dfs.parse(xmin, pos);

        //清除原有的linedataset
        int xcount= linedata.getXValCount();
        while(xcount>0)
        {
            linedata.removeXValue(xcount-1);
            xcount--;
        }

        linedata.removeDataSet(0);
        //linedata.clearValues();

        addDataSet(new Date(begin.getTime()-(long)9*60*1000), 10);
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }


    //执行‘后页’翻页
    public void ExecuteNextPage(){
        LineData linedata = mChart.getLineData();
        LineDataSet lineDataSet = linedata.getDataSetByIndex(0);

        List<String> xAxis =  linedata.getXVals();
        String xmin="";
        String xmax="";
        if (xAxis.size() >=2)
        {
            xmin= xAxis.get(0);
            xmax= xAxis.get(xAxis.size()-1);
        }

        //int dis= Integer.parseInt(xmax) - Integer.parseInt(xmin) +1;
        SimpleDateFormat dfs = new SimpleDateFormat("HH:mm");
        ParsePosition pos = new ParsePosition(0);
        //Date begin = dfs.parse(xmax, pos);
        Date end=dfs.parse(xmax, pos);

        //清除原有的linedataset
        int xcount= linedata.getXValCount();
        while(xcount>0)
        {
            linedata.removeXValue(xcount-1);
            xcount--;
        }

        linedata.removeDataSet(0);
        //linedata.clearValues();

        addDataSet(new Date(end.getTime()+(long)11*60*1000), 10);
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

}

