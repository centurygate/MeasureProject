package com.example.laser.handler;



import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import android.os.Handler;
import android.os.Message;

import com.example.laser.enumeration.CommandEnum;
import com.example.laser.message.PushBulkMeasureMessage;


/**
 * Created by free on 2016/9/2.
 */
public class PushBulkMeasureMessageHandler extends ChannelInboundHandlerAdapter
{
	private Handler pushhandler;
	
	public PushBulkMeasureMessageHandler(Handler pushhandler) {
		super();
		this.pushhandler = pushhandler;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
//		super.channelRead(ctx, msg);
		if (msg instanceof PushBulkMeasureMessage)
		{
			System.out.println("-------------------------------I am in PushBulkMeasureMessageHandler------------------------------------");
//			System.out.println(msg);
			Message handlermsg = new Message();
			handlermsg.what = CommandEnum.getType(msg.getClass().getName());
			System.out.println("-------------------------------handlermsg.what = "+handlermsg.what+"------------------------------------");
			handlermsg.obj = msg;
			pushhandler.sendMessage(handlermsg);
		}
		else
		{
			//System.out.println("Not instance of PushBulkMeasureMessage");
			//System.out.println(msg);
			ctx.fireChannelRead(msg);
		}
	}
}
