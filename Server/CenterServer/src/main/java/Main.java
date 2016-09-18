import com.baidu.bjf.remoting.protobuf.ProtobufIDLGenerator;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import measurebussiness.service.IMeasureService;
import message.LoginResponseMessage;
import message.PushBulkMeasureMessage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import session.Session;

import java.nio.ByteOrder;

/**
 * Hello world!
 *
 */
public class Main
{
    private  int port;
    
    public Main(int port)
    {
        this.port = port;
    }
    public void run() throws Exception {
        System.out.println(ProtobufIDLGenerator.getIDL(LoginResponseMessage.class));
        System.out.println(ProtobufIDLGenerator.getIDL(PushBulkMeasureMessage.class));
        System.out.println(ByteOrder.nativeOrder());
        ApplicationContext ctx = new ClassPathXmlApplicationContext("measurebussiness/mapping/spring-mybatis.xml");
        Session.measureService = (IMeasureService) ctx.getBean("measureservice");
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    //.handler(new LoggingHandler(LogLevel.ERROR))
                    .childHandler(new SimpleChatServerInitializer())  //(4)
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // 绑定端口，开始接收进来的连接
            ChannelFuture f = b.bind(port).sync(); // (7)
            System.out.println("Center Server Start......");
            // 等待服务器  socket 关闭 。
            // 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
            f.channel().closeFuture().sync();
            System.out.println("Center Server Start Stop......");

        } finally {
            System.out.println("Center Server Start Stop......");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();


        }
    }
    public static void main(String[] args ) throws Exception
    {
        System.out.println( "Hello World!" );
        int port;
        if (args.length > 0)
        {
            port = Integer.parseInt(args[0]);
        }
        else
        {
            port = 9090;
        }
        
        new Main(port).run();
    }
}
