package clienthandlerinitializer;

import decode.ProtobufMessageDecoder;
import encode.ProtobufMessageEncoder;
import handler.*;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class MeasureClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("logger",new LoggingHandler(LogLevel.ERROR));
        pipeline.addLast("fixedframelenthdecoder",new LengthFieldBasedFrameDecoder(104857600,0,4,-4,0));
        pipeline.addLast("protobufmessagedecoder",new ProtobufMessageDecoder());
        pipeline.addLast("prtobufmessageencoder",new ProtobufMessageEncoder());
        pipeline.addLast("keepaliveresponsehandler",new KeepAliveResponseHandler());
        pipeline.addLast("loginresponsehandler",new LoginResponseHandler());
        pipeline.addLast("pushsinglemeasuremessagehandler",new PushSingleMeasureMeassageHandler());
        pipeline.addLast("pushbulkmeasuremessagehandler",new PushBulkMeasureMessageHandler());
        pipeline.addLast("handler", new MeasureClientHandler());
    }
}
