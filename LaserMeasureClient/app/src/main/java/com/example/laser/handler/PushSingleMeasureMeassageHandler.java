package com.example.laser.handler;



import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;

import com.example.laser.enumeration.CommandEnum;
import com.example.laser.message.PullBulkMeasureMeaage;
import com.example.laser.message.PushSingleMeasureMessage;
import com.example.laser.message.RequestCmdHeader;
import com.example.laser.session.Session;


/**
 * Created by free on 2016/9/2.
 */
public class PushSingleMeasureMeassageHandler extends ChannelInboundHandlerAdapter
{
	private Handler pushhandler;

	public PushSingleMeasureMeassageHandler(Handler pushhandler) {
		super();
		this.pushhandler = pushhandler;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		//super.channelRead(ctx, msg);
		if (msg instanceof PushSingleMeasureMessage)
		{
			//System.out.println(msg);
			Message handlermsg = new Message();
			handlermsg.what = CommandEnum.getType(msg.getClass().getName());
			handlermsg.obj = msg;
			pushhandler.sendMessage(handlermsg);
			
		}
		else
		{
			ctx.fireChannelRead(msg);
		}
	}
}
