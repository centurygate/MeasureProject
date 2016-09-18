package handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import message.KeepAliveRequestMessage;
import message.KeepAliveResponseMessage;

import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by free on 2016/9/2.
 */
public class KeepAliveRequestHandler extends ChannelInboundHandlerAdapter
{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		if (msg instanceof KeepAliveRequestMessage)
		{
			System.out.println("Keep Alive: ClientTime["+(new Date(((KeepAliveRequestMessage) msg).getClienttime()))+"]");
			KeepAliveResponseMessage keepAliveResponseMessage = new KeepAliveResponseMessage();
			keepAliveResponseMessage.setServertime(System.currentTimeMillis());
			ctx.writeAndFlush(keepAliveResponseMessage);
		}
		else
		{
			ctx.fireChannelRead(msg);
		}
	}
}
