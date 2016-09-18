package handler;

import message.KeepAliveRequestMessage;
import message.KeepAliveResponseMessage;
import message.LoginResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import message.RequestCmdHeader;
import session.Session;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by free on 2016/9/1.
 */
public class LoginResponseHandler extends ChannelInboundHandlerAdapter
{
	private volatile ScheduledFuture<?> keepAliveFuture;
	private class KeepAliveTask implements Runnable
	{
		private final ChannelHandlerContext ctx;
		
		private KeepAliveTask(ChannelHandlerContext ctx)
		{
			this.ctx = ctx;
		}
		
		public void run()
		{
			KeepAliveRequestMessage keepAliveRequestMessage = new KeepAliveRequestMessage();
			RequestCmdHeader requestCmdHeader = new RequestCmdHeader();
			requestCmdHeader.setClientID(Session.clientID);
			keepAliveRequestMessage.setClienttime(System.currentTimeMillis());
			keepAliveRequestMessage.setCmdHeader(requestCmdHeader);
			ctx.writeAndFlush(keepAliveRequestMessage);
		}
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		if (msg instanceof LoginResponseMessage)
		{
			Session.clientID = ((LoginResponseMessage) msg).getClientID();
			System.out.println(Session.clientID);
			if (((LoginResponseMessage) msg).getRespCmdHeader().getResultcode() == 0)
			{
				//pull the measure message
//				PullBulkMeasureMeaage pullBulkMeasureMeaage = new PullBulkMeasureMeaage();
//				RequestCmdHeader requestCmdHeader = new RequestCmdHeader();
//				requestCmdHeader.setClientID(Session.clientID);
//				pullBulkMeasureMeaage.setRequestCmdHeader(requestCmdHeader);
//				List<String> deviceAddrLst = new ArrayList();
//				deviceAddrLst.add("Dev1");
//				deviceAddrLst.add("Dev2");
//				deviceAddrLst.add("Dev3");
//				pullBulkMeasureMeaage.setDeviceAddrLst(deviceAddrLst);
//				pullBulkMeasureMeaage.setStartTime(System.currentTimeMillis()-24*60*60*1000);
//				pullBulkMeasureMeaage.setEndTime(System.currentTimeMillis());
//				System.out.println(pullBulkMeasureMeaage);
//				ctx.writeAndFlush(pullBulkMeasureMeaage);
				keepAliveFuture = ctx.executor().scheduleAtFixedRate(new LoginResponseHandler.KeepAliveTask(ctx),0,50000, TimeUnit.MILLISECONDS);
			}
		}
		else
		{
			ctx.fireChannelRead(msg);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		if (keepAliveFuture != null)
		{
			keepAliveFuture.cancel(true);
			keepAliveFuture = null;
		}
		ctx.fireExceptionCaught(cause);
	}
}
