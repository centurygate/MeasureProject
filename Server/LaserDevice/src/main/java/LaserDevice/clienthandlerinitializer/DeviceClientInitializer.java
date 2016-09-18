package LaserDevice.clienthandlerinitializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by free on 2016/9/4.
 */
public class DeviceClientInitializer extends ChannelInitializer
{
	protected void initChannel(Channel ch) throws Exception
	{
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("logger",new LoggingHandler(LogLevel.INFO));
				
	}
}
