package handler;

import clientmanager.ClientManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by free on 2016/9/2.
 */
public class CustomReadTimeoutHandler extends ReadTimeoutHandler
{
	public CustomReadTimeoutHandler(int timeoutSeconds)
	{
		super(timeoutSeconds);
	}
	
	public CustomReadTimeoutHandler(long timeout, TimeUnit unit)
	{
		super(timeout, unit);
	}
	
	@Override
	protected void readTimedOut(ChannelHandlerContext ctx) throws Exception
	{
		Logger.getLogger(this.getClass().getName()).log(Level.INFO,"ReadTimedOut.......");
		//Unregister the channel
		ClientManager.UnregisterChannel(ctx.channel());
		super.readTimedOut(ctx);
	}
}
