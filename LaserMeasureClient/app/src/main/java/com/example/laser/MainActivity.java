package com.example.laser;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.laser.R;
import com.example.laser.message.MeasureRecord;
import com.example.laser.message.PushSingleMeasureMessage;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;


import java.text.SimpleDateFormat;


public class MainActivity extends AppCompatActivity {

    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime);

        //初始化MPChart图标控件
        MPChartInit();

        //加入实时数据值
        //handler.post(task);

        //thread.start();
        //新建子线程接收通信数据
        new Thread(new TcpClientThread(handler)).start();
    }


    //创建Handler
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 3: //实时数据
                    PushSingleMeasureMessage pushsinglemessage = (PushSingleMeasureMessage)msg.obj;
                    MeasureRecord rcd= pushsinglemessage.getSingleMeasureRecord();
                    float yValues= (float)rcd.getDistance() * 0.25f;
                    addEntry(yValues);
                    break;
                default:
                    break;
            }
        }
    };




    //新建一个子线程进行耗时的网络查询操作。（TCP的客户端请求响应在此进行）
/*    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            //循环执行从服务器取数据操作
            while (true){
                //DEMO随机数生成临时代替
                //float yValues=(float)(40+Math.random() * 10);

                //接收服务器端Message


                //产生Message，附加数据值并发送
                Message msg= new Message();
                msg.what = 1;
                Bundle b= new Bundle();
                b.putFloat("realTime", yValues);
                msg.setData(b);

                handler.sendMessage(msg);
                //每次发送完休息2秒时间
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    });*/


    public void MPChartInit(){

        mChart= (LineChart) findViewById(R.id.chart1);

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


/*    private Runnable task =new Runnable() {
        @Override
        public void run() {
            //时钟函数取值频率
            handler.postDelayed(this, 1000);

            //取实时数据Y值
            float yValues=(float)(40+Math.random() * 10);

            addEntry(yValues);
        }
    };*/


    public void addEntry(float yValues){
        LineData lineData = mChart.getData();

        if(lineData != null){

            LineDataSet set = lineData.getDataSetByIndex(0);

            if (set == null){
                set= createLineDataSet();
                lineData.addDataSet(set);
            }

            set.setColor(0xFF0000FF);
            set.setCircleColor(0xFF0000FF);


            SimpleDateFormat sdf=new SimpleDateFormat("kk:mm:ss");
            String date=sdf.format(new java.util.Date());


            int count= set.getEntryCount();
            //当Entry大于一定数目时清除，防止内存暴涨
            if(count > 10)
            {
                //清除掉前2个Entry
                for (int i=0;i<2;i++)
                {
                    lineData.removeEntry(0,0);
                }
            }
            count= set.getEntryCount();
            lineData.addXValue(date);

            lineData.addEntry(new Entry(yValues, count), 0);

            mChart.notifyDataSetChanged();
            //mChart.moveViewTo(yValues, count, YAxis.AxisDependency.LEFT);

            mChart.setVisibleXRangeMaximum(6);
            mChart.moveViewTo(lineData.getXValCount()-7, 50f, YAxis.AxisDependency.LEFT);

        }
    }



    /*
    * 创建空的LineDataSet
    * */
    private LineDataSet createLineDataSet() {
        LineDataSet dataSet = new LineDataSet(null, "测量值:");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setValueTextSize(16f);

        return dataSet;
    }
}
