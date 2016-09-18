package com.example.laser.handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.baidu.bjf.remoting.protobuf.ProtobufIDLGenerator;
import com.example.laser.message.KeepAliveRequestMessage;
import com.example.laser.message.LoginResponseMessage;
import com.example.laser.message.PushBulkMeasureMessage;
import com.example.laser.message.RequestCmdHeader;
import com.example.laser.session.Session;

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
			//System.out.println(msg.toString());
			//System.out.println("Return Status:"+((LoginResponseMessage) msg).getRespCmdHeader().getResultcode());
			if (((LoginResponseMessage) msg).getRespCmdHeader().getResultcode() == 0)
			{
//				ResponseCmdHeader responseCmdHeader = new ResponseCmdHeader();
//				LoginResponseMessage loginResponseMessage = new LoginResponseMessage();
//				responseCmdHeader.setResultcode(0);
//				responseCmdHeader.setDesc("Successfully Login");
//				loginResponseMessage.setRespCmdHeader(responseCmdHeader);
//				loginResponseMessage.setClientID(UUID.randomUUID().toString());
//				ctx.writeAndFlush(loginResponseMessage);
				//------------------------------------------------------------------------------------
//				System.out.println(ProtobufIDLGenerator.getIDL(LoginResponseMessage.class));
//				System.out.println(ProtobufIDLGenerator.getIDL(PushBulkMeasureMessage.class));
				Session.clientID = ((LoginResponseMessage) msg).getClientID();
				//System.out.println(Session.clientID);
				keepAliveFuture = ctx.executor().scheduleAtFixedRate(new LoginResponseHandler.KeepAliveTask(ctx),0,2000, TimeUnit.MILLISECONDS);
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
		//System.out.println("Occur exceptionCaught:"+cause.getMessage());
		if (keepAliveFuture != null)
		{
			keepAliveFuture.cancel(true);
			keepAliveFuture = null;
		}
		ctx.fireExceptionCaught(cause);
	}
}
