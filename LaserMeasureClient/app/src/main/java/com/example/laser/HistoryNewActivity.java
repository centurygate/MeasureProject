package com.example.laser;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laser.message.MeasureRecord;
import com.example.laser.message.PullBulkMeasureMeaage;
import com.example.laser.message.PushBulkMeasureMessage;
import com.example.laser.message.PushSingleMeasureMessage;
import com.example.laser.message.RequestCmdHeader;
import com.example.laser.session.Session;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class HistoryNewActivity extends AppCompatActivity {

    private TextView showDate = null;
    private Button pickDate = null;
    private Button query = null;

    //历史图标
    private LineChart mChart;

    private static final int SHOW_DATAPICK = 0;
    private static final int DATE_DIALOG_ID = 0;

    private int mYear = 0;
    private int mMonth = 0;
    private int mDay = 0;

    //处理历史数据查询的Handler
    Handler handler;

    //记录X轴放缩的比例值，重新查询时需要还原回来通过取倒数
    //float xScale=1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }*/
        setContentView(R.layout.activity_history_new);
        query = (Button)findViewById(R.id.query);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取查询信息
                PullBulkMeasureMeaage pullBulkMeasureMeaage = new PullBulkMeasureMeaage();
                RequestCmdHeader cmdHeader = new RequestCmdHeader();
                cmdHeader.setClientID(Session.clientID);
                List<String> addrlst = new ArrayList<String>(1);
                String addr = "0XAA0XBB0XCC0XDD0XEE0XFF";
                addrlst.add(addr);
                //long endTime = System.currentTimeMillis();
                //long startTime = endTime - 500*1000;

                //取选择的时间，一天之内的时间
                Calendar tstart= new GregorianCalendar(mYear, mMonth, mDay, 0, 0, 0);
                Calendar tend= new GregorianCalendar(mYear, mMonth, mDay, 23, 59, 59);

                pullBulkMeasureMeaage.setRequestCmdHeader(cmdHeader);
                pullBulkMeasureMeaage.setStartTime(tstart.getTime().getTime());
                pullBulkMeasureMeaage.setEndTime(tend.getTime().getTime());
                pullBulkMeasureMeaage.setDeviceAddrLst(addrlst);

                Message message = new Message();
                message.what = 4;
                message.obj = pullBulkMeasureMeaage;

                //这里需要判断返回值真假,确定是否真的发送成功
                TcpClientThread.sendMessage(message);
            }
        });

        initializeViews();

        final Calendar c= Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        //插入DataSet测量值数据
        //SimpleDateFormat sdf=new SimpleDateFormat("kk:mm");
        //Date d=new Date();
        //addDataSet(d,30);


        //对接查询数据
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                switch (msg.what) {
                    case 4: {
                        PushBulkMeasureMessage pushbulkmeasuremessage = (PushBulkMeasureMessage) msg.obj;
                        //show.setText(pushbulkmeasuremessage.toString());
                        //Toast.makeText(HistoryNewActivity.this, pushbulkmeasuremessage.toString(), Toast.LENGTH_SHORT).show();
                        //执行显示数据
                        showBulkMessageData(pushbulkmeasuremessage);
                        break;
                    }
                    default:
                        break;
                }
            }
        };

        //子线程
        new Thread(new TcpClientThread(handler)).start();
    }


    //初始化控件和UI视图
    private void initializeViews(){
        showDate = (TextView) findViewById(R.id.showdate);
        pickDate = (Button) findViewById(R.id.pickdate);
        query = (Button) findViewById(R.id.query);

        //初始化MPChart图表
        mChart= (LineChart) findViewById(R.id.chart2);
        MPChartInit();

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message();
                if (pickDate.equals((Button) view)){
                    msg.what = HistoryNewActivity.SHOW_DATAPICK;
                }
                HistoryNewActivity.this.dateHandler.sendMessage(msg);
            }
        });
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
        //mChart.setDragEnabled(true);
        //mChart.setScaleEnabled(true);

        //设定可见个数--测试
        //mChart.setVisibleXRange(1.0f,1.0f);

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

    //获取显示MessageList中的数据
    public void showBulkMessageData(PushBulkMeasureMessage msg)
    {
        LineData lineData= mChart.getLineData();

        if (lineData != null)
        {
            lineData.clearValues();

            //清除XValues
            int cntX = lineData.getXValCount();
            for (int x=cntX-1; x>=0; x--)
            {
                lineData.removeXValue(x);
            }
            int cnt0= lineData.getXValCount();


            ArrayList<Entry> yValueList = new ArrayList<>();
            List<MeasureRecord> mlist= msg.getMeasureLst();
            //如果选取的一天没有数值，显示Toast并清空原有折线
            if (mlist.size() == 0)
            {
                Toast.makeText(HistoryNewActivity.this, "查询当天没有数值", Toast.LENGTH_SHORT).show();
            }

            //依次加入查询到的值list
            for (int i=0;i<mlist.size();i++)
            {
                MeasureRecord rcd= mlist.get(i);
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(rcd.getTime());
                SimpleDateFormat sdf= new SimpleDateFormat("kk:mm");
                lineData.addXValue(sdf.format(c.getTime()));
                yValueList.add(new Entry((float)(rcd.getDistance()*0.25),i));
            }

            LineDataSet set =new LineDataSet(yValueList, "历史值：");
            set.setColor(0xFF0000FF);
            set.setCircleColor(0xFF0000FF);
            set.setValueTextSize(16f);


            lineData.addDataSet(set);
            //xScale= 10.0f/(float)mlist.size();
            //mChart.setVisibleXRange(0.5f,0.5f);

            mChart.notifyDataSetChanged();
            mChart.invalidate();
            mChart.setVisibleXRangeMinimum(5);
            mChart.setVisibleXRangeMaximum(10);
        }
    }


    //设置日期
    private void setDateTime() {
        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        updateDateDisplay();
    }

    //更新日期显示
    private void updateDateDisplay() {
        showDate.setText(new StringBuilder().append(mYear).append("-")
                .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
                .append((mDay < 10) ? "0" + mDay : mDay));
    }


    //日期控件的事件
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            updateDateDisplay();
        }
    };


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
        }

        return null;
    }


    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
        }
    }


    Handler dateHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case HistoryNewActivity.SHOW_DATAPICK:
                    showDialog(DATE_DIALOG_ID);
                    break;
            }
        }
    };
}
