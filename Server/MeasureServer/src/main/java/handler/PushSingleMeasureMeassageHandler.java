package handler;

import clientmanager.ClientManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import message.PushSingleMeasureMessage;

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
			ClientManager.PushMessage(msg);
		}
		else
		{
			ctx.fireChannelRead(msg);
		}
	}
}
