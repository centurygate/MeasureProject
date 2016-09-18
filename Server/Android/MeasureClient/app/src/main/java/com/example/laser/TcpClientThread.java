package com.example.laser;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.laser.clienthandlerinitializer.MeasureClientInitializer;
import com.example.laser.enumeration.CommandEnum;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TcpClientThread implements Runnable
{

	private static String host = "192.168.1.105";
	private static int port = 9090;
	private Handler pushhandler;
	private static Handler sendhandler;
	private static Channel channel = null;
	private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

	public static boolean sendMessage(Message msg)
	{
		if (sendhandler == null)
		{
			Log.e("TcpClientThread", "sendhandler has not been initialized completely");
			return false;
		}
		return sendhandler.sendMessage(msg);
	}

	public TcpClientThread(Handler pushhandler)
	{
		super();
		this.pushhandler = pushhandler;
	}

	private void connect(String host, int port)
	{
		EventLoopGroup group = new NioEventLoopGroup();
		try
		{
			Bootstrap bootstrap = new Bootstrap()
					.group(group)
					.channel(NioSocketChannel.class)
					.option(ChannelOption.SO_KEEPALIVE, true)
					.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,3000)
					.handler(new MeasureClientInitializer(pushhandler));
			final Channel channel = bootstrap.connect(host, port).sync().channel();
			TcpClientThread.channel = channel;
			System.out.println("================Looper.loop() called===============");
			channel.closeFuture().sync();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("================Received some Exception===============");
		}
		finally
		{
			TcpClientThread.channel = null;
			group.shutdownGracefully();
			executorService.execute(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						TimeUnit.SECONDS.sleep(2);
						try
						{
							connect(TcpClientThread.host, TcpClientThread.port);
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

	@Override
	public void run()
	{
		try
		{
			//启动连接服务器线程
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					connect(TcpClientThread.host, TcpClientThread.port);
				}
			}).start();

			Looper.prepare();
			sendhandler = new Handler()
			{

				@Override
				public void handleMessage(Message msg)
				{
					// TODO Auto-generated method stub
					switch (msg.what)
					{
						case 4:
						{
							System.out.println("Send Command PullBulkMeasureMeaage to CenterServer");
							if (channel != null)
							{
								channel.writeAndFlush(msg.obj);
							}
							else
							{
								System.out.println("channel has not been initialized......");
							}
							break;
						}
						case 0xffffffff:
						{
							System.out.println("Server is not online");
						}
						default:
							break;
					}
				}

			};
			Looper.loop();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			System.out.println("================Enter finally===============");
		}
	}
}
