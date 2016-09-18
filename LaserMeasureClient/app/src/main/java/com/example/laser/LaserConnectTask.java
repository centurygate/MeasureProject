package com.example.laser;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import android.os.AsyncTask;

public class LaserConnectTask extends AsyncTask<ChannelInitializer<SocketChannel>, Void, Boolean> {

	@SuppressWarnings("finally")
	@Override
	protected Boolean doInBackground(ChannelInitializer<SocketChannel>... arg0) {
		// TODO Auto-generated method stub
		EventLoopGroup group = new NioEventLoopGroup();
		try
		{
			Bootstrap bootstrap = new Bootstrap()
			.group(group)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.handler(arg0[0]);
			Channel channel = bootstrap.connect("139.196.59.146", 9090).sync().channel();
			channel.closeFuture().sync();
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
			return false;
		}
	}
	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}
