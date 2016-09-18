package MeasureClient;

import clienthandlerinitializer.MeasureClientInitializer;
import handler.MeasureClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Hello world!
 *
 */
public class App 
{
    private final String host;
    private final int port;
    
    public App(String host, int port)
    {
        this.host = host;
        this.port = port;
    }
    public void run() throws Exception
    {
        
        // client for centerserver
        EventLoopGroup group = new NioEventLoopGroup();
        try
        {
            int step = 10;
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new MeasureClientInitializer());
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            System.out.println("Measure Client Start......");
            Channel channel = channelFuture.channel();
            System.out.println("Main===================="+channel);
            channel.closeFuture().sync();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            System.out.println("Measure Client Stop......");
            group.shutdownGracefully();
        }
        
    }
    public static void main(String[] args )
    {
        System.out.println( "Starting Measure Client++++++++++++++++++++++++++++++++++" );
        try
        {
            new App("localhost", 9090).run();
		      //new App("www.ismart4d.com", 9090).run();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
