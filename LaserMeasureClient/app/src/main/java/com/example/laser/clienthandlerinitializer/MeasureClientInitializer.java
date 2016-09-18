package com.example.laser.clienthandlerinitializer;




import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import android.os.Handler;

import com.example.laser.decode.ProtobufMessageDecoder;
import com.example.laser.encode.ProtobufMessageEncoder;
import com.example.laser.handler.KeepAliveResponseHandler;
import com.example.laser.handler.LoginResponseHandler;
import com.example.laser.handler.MeasureClientHandler;
import com.example.laser.handler.PushBulkMeasureMessageHandler;
import com.example.laser.handler.PushSingleMeasureMeassageHandler;

public class MeasureClientInitializer extends ChannelInitializer<SocketChannel> {
	private Handler pushhandler;
    public MeasureClientInitializer(Handler pushhandler) {
		super();
		this.pushhandler = pushhandler;
	}

	@Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("logger",new LoggingHandler(LogLevel.INFO));
        pipeline.addLast("fixedframelenthdecoder",new LengthFieldBasedFrameDecoder(104857600,0,4,-4,0));
        pipeline.addLast("protobufmessagedecoder",new ProtobufMessageDecoder());
        pipeline.addLast("prtobufmessageencoder",new ProtobufMessageEncoder());
        pipeline.addLast("keepaliveresponsehandler",new KeepAliveResponseHandler());
        pipeline.addLast("loginresponsehandler",new LoginResponseHandler());
        pipeline.addLast("pushsinglemeasuremessagehandler",new PushSingleMeasureMeassageHandler(pushhandler));
        pipeline.addLast("pushbulkmeasuremessagehandler",new PushBulkMeasureMessageHandler(pushhandler));
        pipeline.addLast("handler", new MeasureClientHandler());
    }
}
