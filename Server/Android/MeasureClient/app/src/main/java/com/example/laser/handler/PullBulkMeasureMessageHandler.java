package com.example.laser.handler;



import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import com.example.laser.message.PullBulkMeasureMeaage;


/**
 * Created by free on 2016/9/2.
 */
public class PullBulkMeasureMessageHandler extends ChannelInboundHandlerAdapter
{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
//		super.channelRead(ctx, msg);
		if (msg instanceof PullBulkMeasureMeaage)
		{
			System.out.println(msg);
		}
		else
		{
			ctx.fireChannelRead(msg);
		}
	}
}
