package com.example.laser.handler;



import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

import com.example.laser.message.KeepAliveResponseMessage;

/**
 * Created by free on 2016/9/2.
 */
public class KeepAliveResponseHandler extends ChannelInboundHandlerAdapter
{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		if (msg instanceof KeepAliveResponseMessage)
		{
			//System.out.println("Keep Alive: ServerTime["+(new Date(((KeepAliveResponseMessage) msg).getServertime()))+"]");
		}
		else
		{
			ctx.fireChannelRead(msg);
		}
	}
}
