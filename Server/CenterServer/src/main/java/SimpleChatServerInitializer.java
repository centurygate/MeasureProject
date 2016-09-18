
import decode.ProtobufMessageDecoder;
import encode.ProtobufMessageEncoder;
import handler.*;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * Created by free on 2016/8/22.
 */
public class SimpleChatServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("logger",new LoggingHandler(LogLevel.ERROR));
	    pipeline.addLast("fixedframelenthdecoder",new LengthFieldBasedFrameDecoder(104857600,0,4,-4,0));
	    pipeline.addLast("protobufmessagedecoder",new ProtobufMessageDecoder());
	    pipeline.addLast("prtobufmessageencoder",new ProtobufMessageEncoder());
	    pipeline.addLast("readtimeouthandler",new CustomReadTimeoutHandler(60));
	    pipeline.addLast("keepaliverequsthandler",new KeepAliveRequestHandler());
	    pipeline.addLast("keepaliveresponsehandler",new KeepAliveResponseHandler());
	    pipeline.addLast("loginreqhandler",new LoginRequestHandler());
	    pipeline.addLast("loginresponsehandler",new LoginResponseHandler());
	    pipeline.addLast("pullBulkMeasureMessage",new PullBulkMeasureMessageHandler());
	    pipeline.addLast("processmeasuredatahandler",new ProcesPacketMeasureDataFromMeasureServerHandler());
		pipeline.addLast("channelInactivehandler",new ChannelInactiveHandler());
        pipeline.addLast("handler", new SimpleChatServerHandler());
    }
}