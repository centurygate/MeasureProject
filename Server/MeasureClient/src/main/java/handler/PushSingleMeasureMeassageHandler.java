package handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import message.PullBulkMeasureMeaage;
import message.PushSingleMeasureMessage;
import message.RequestCmdHeader;
import session.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by free on 2016/9/2.
 */
public class PushSingleMeasureMeassageHandler extends ChannelInboundHandlerAdapter
{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		//super.channelRead(ctx, msg);
		if (msg instanceof PushSingleMeasureMessage)
		{
			System.out.println(msg);
			PullBulkMeasureMeaage pullBulkMeasureMeaage = new PullBulkMeasureMeaage();
			RequestCmdHeader cmdHeader = new RequestCmdHeader();
			cmdHeader.setClientID(Session.clientID);
			List<String> addrlst = new ArrayList<String>(1);
			String addr = "0XAA0XBB0XCC0XDD0XEE0XFF";
			addrlst.add(addr);
			long endTime = System.currentTimeMillis();
			long startTime = endTime - 8000;
			pullBulkMeasureMeaage.setRequestCmdHeader(cmdHeader);
			pullBulkMeasureMeaage.setStartTime(startTime);
			pullBulkMeasureMeaage.setEndTime(endTime);
			pullBulkMeasureMeaage.setDeviceAddrLst(addrlst);
			ctx.writeAndFlush(pullBulkMeasureMeaage);
		}
		else
		{
			ctx.fireChannelRead(msg);
		}
	}
}
