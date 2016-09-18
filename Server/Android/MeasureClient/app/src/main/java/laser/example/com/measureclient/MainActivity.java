package laser.example.com.measureclient;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.laser.TcpClientThread;
import com.example.laser.message.PullBulkMeasureMeaage;
import com.example.laser.message.PushBulkMeasureMessage;
import com.example.laser.message.PushSingleMeasureMessage;
import com.example.laser.message.RequestCmdHeader;
import com.example.laser.session.Session;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    Handler handler;
    TextView show;

    // Button start_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show = (TextView) findViewById(R.id.show);
        show.setText(ByteOrder.nativeOrder().toString());
        // start_btn = (Button)findViewById(R.id.button1);
        Button searchMeasureBtn = (Button) this.findViewById(R.id.searchMeasureData);
        searchMeasureBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                System.out.println("#######################################arg0.getId() == R.id.searchMeasureData");
                PullBulkMeasureMeaage pullBulkMeasureMeaage = new PullBulkMeasureMeaage();
                RequestCmdHeader cmdHeader = new RequestCmdHeader();
                cmdHeader.setClientID(Session.clientID);
                List<String> addrlst = new ArrayList<String>(1);
                String addr = "0XAA0XBB0XCC0XDD0XEE0XFF";
                addrlst.add(addr);
                long endTime = System.currentTimeMillis();
                long startTime = endTime - 100000;
                pullBulkMeasureMeaage.setRequestCmdHeader(cmdHeader);
                pullBulkMeasureMeaage.setStartTime(startTime);
                pullBulkMeasureMeaage.setEndTime(endTime);
                pullBulkMeasureMeaage.setDeviceAddrLst(addrlst);

                Message message = new Message();
                message.what = 4;
                message.obj = pullBulkMeasureMeaage;

                //这里需要判断返回值真假,确定是否真的发送成功
                TcpClientThread.sendMessage(message);
            }

        });
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                switch (msg.what) {
                    case 3: // PushSingleMeasureMessage
                    {
                        PushSingleMeasureMessage pushsinglemessage = (PushSingleMeasureMessage) msg.obj;
                        show.setText(pushsinglemessage.getSingleMeasureRecord().toString());
                        break;
                    }
                    case 4: {
                        PushBulkMeasureMessage pushbulkmeasuremessage = (PushBulkMeasureMessage) msg.obj;
                        show.setText(pushbulkmeasuremessage.toString());
                        break;
                    }
                    default:
                        break;
                }
            }

        };

        //System.out.println(ProtobufIDLGenerator.getIDL(PushBulkMeasureMessage.class));
        new Thread(new TcpClientThread(handler)).start();
    }

}
