package serverhandlerinitializer;

import handler.PushPacketMeasureDataToCenterServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by free on 2016/9/3.
 */
public class MeasureServerHandlerInitializer extends ChannelInitializer
{
//	private Channel channel;
//
//	public MeasureServerHandlerInitializer(Channel channel)
//	{
//		this.channel = channel;
//	}
//
	protected void initChannel(Channel ch) throws Exception
	{
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("logger",new LoggingHandler(LogLevel.INFO));
		//pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		//\r \n is defined in protocal of laser device (customized)
		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, new ByteBuf[] {
				Unpooled.wrappedBuffer(new byte[] { '\r', '\n' })
		}));
		pipeline.addLast("pushpacketmeasuredatahandler",new PushPacketMeasureDataToCenterServerHandler());
	}
}
