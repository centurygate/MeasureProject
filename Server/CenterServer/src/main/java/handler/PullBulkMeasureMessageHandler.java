package handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import measurebussiness.model.Measure;
import message.MeasureRecord;
import message.PullBulkMeasureMeaage;
import message.PushBulkMeasureMessage;
import session.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by free on 2016/9/2.
 */
public class PullBulkMeasureMessageHandler extends ChannelInboundHandlerAdapter
{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		if (msg instanceof PullBulkMeasureMeaage)
		{
			PullBulkMeasureMeaage pullBulkMeasureMeaage = (PullBulkMeasureMeaage)msg;
			System.out.println("Client Start to Search Measure..................");
			System.out.println(msg);
            Date starttime = new Date(pullBulkMeasureMeaage.getStartTime());
            Date endtime = new Date(pullBulkMeasureMeaage.getEndTime());
			List<Measure> measureList = Session.measureService.queryMeasureByAddrTime(pullBulkMeasureMeaage.getDeviceAddrLst().get(0),starttime,endtime);
			PushBulkMeasureMessage pushBulkMeasureMessage = new PushBulkMeasureMessage();
			List<MeasureRecord> measureRecordList = new ArrayList<MeasureRecord>(measureList.size());
			int size = measureList.size();
			if(size <= 0)
			{
				System.out.println("There is no more data between the search time for the Device");
				//return;
			}
			for (int i = 0; i < size;i++)
			{
				MeasureRecord measureRecord = new MeasureRecord();
				measureRecord.setDeviceAddr(measureList.get(i).getDevaddr());
				measureRecord.setTime(measureList.get(i).getRecordtime().getTime());
				measureRecord.setDistance(measureList.get(i).getDistance());
				measureRecordList.add(measureRecord);
			}
			System.out.println("=====================================Send measureRecordList to Client==========================");
			
			pushBulkMeasureMessage.setMeasureLst(measureRecordList);
			//System.out.println(pushBulkMeasureMessage);
			ctx.writeAndFlush(pushBulkMeasureMessage);
		}
		else
		{
			ctx.fireChannelRead(msg);
		}
	}
}
