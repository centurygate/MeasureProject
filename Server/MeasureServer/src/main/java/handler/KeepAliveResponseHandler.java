package handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import message.KeepAliveResponseMessage;

import java.util.Date;

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
			System.out.println("Keep Alive: serverTime["+(new Date(((KeepAliveResponseMessage) msg).getServertime()))+"]");
		}
		else
		{
			ctx.fireChannelRead(msg);
		}
	}
}
