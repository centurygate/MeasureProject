package runner;

import decode.ProtobufMessageDecoder;
import encode.ProtobufMessageEncoder;
import handler.*;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class SimpleChatClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("logger",new LoggingHandler(LogLevel.ERROR));
        pipeline.addLast("protobufmessagedecoder",new ProtobufMessageDecoder());
        pipeline.addLast("prtobufmessageencoder",new ProtobufMessageEncoder());
        pipeline.addLast("keepaliveresponsehandler",new KeepAliveResponseHandler());
        pipeline.addLast("loginresponsehandler",new LoginResponseHandler());
        pipeline.addLast("handler", new SimpleChatClientHandler());
    }
}
