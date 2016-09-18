package LaserDevice;

import LaserDevice.clienthandlerinitializer.DeviceClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App 
{
    private static String host = "192.168.1.109";
    private static int port = 8765;
    private Channel channel = null;
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    public App(String host, int port)
    {
        this.host = host;
        this.port = port;
    }
    private void connect()
    {
        EventLoopGroup group = new NioEventLoopGroup();
        try
        {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,3000)
                    .handler(new DeviceClientInitializer());
            final Channel channel = bootstrap.connect(App.host, App.port).sync().channel();
            this.channel = channel;
            int number = 0;
            while (true && channel.isActive())
            {
                //String lineseparator = System.getProperty("line.separator");
                //byte[] linetag = lineseparator.getBytes();
                byte[] linetag = {(byte)'\r',(byte)'\n'};
                ByteBuf byteBuf = Unpooled.buffer();
                byte[] measurebytes = new byte[16];
                measurebytes[0] = (byte) 0xAA;
                measurebytes[1] = (byte) 0xBB;
                measurebytes[2] = (byte) 0xCC;
                measurebytes[3] = (byte) 0xDD;
                measurebytes[4] = (byte) 0xEE;
                measurebytes[5] = (byte) 0xFF;
                measurebytes[6] = (byte) 0x06;
                measurebytes[7] = (byte) 0x83;
                measurebytes[8] = (byte) 0x32;
                number = 48 + (int)(1+Math.random()*9);
                measurebytes[9] = (byte)number;
                number = 48 + (int)(1+Math.random()*9);
                measurebytes[10] = (byte) number;
                measurebytes[11] = (byte) 0x2E;
                number = 48 + (int)(1+Math.random()*9);
                measurebytes[12] = (byte) number;
                measurebytes[13] = (byte) 0x32;
                number = 48 + (int)(1+Math.random()*9);
                measurebytes[14] = (byte) number;
                number = 48 + (int)(1+Math.random()*9);
                measurebytes[15] = (byte) number;
                byteBuf.writeBytes(measurebytes);
                byteBuf.writeBytes(linetag);
                channel.writeAndFlush(byteBuf);
                Thread.sleep(5000);

            }
            channel.closeFuture().sync();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            this.channel = null;
            group.shutdownGracefully();
            executorService.execute(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        TimeUnit.SECONDS.sleep(2);
                        try
                        {
                            connect();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    public void run() throws Exception
    {
        System.out.println( "Laser Device Start!" );
        //device for measure server
//        new Thread(new Runnable() {
//            public void run() {
//                connect();
//            }
//        }).start();
        connect();
    }
    public static void main( String[] args ) throws Exception
    {
       //new App("139.196.59.146",8765).run();
       // new App("www.ismart4d.com",8765).run();
        new App("localhost",8765).run();
    }
}
