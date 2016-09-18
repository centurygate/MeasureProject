package com.example.laser.handler;



import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import util.Util;

import com.example.laser.message.MeasureRecord;
import com.example.laser.message.PushMeasureDataMessage;
import com.example.laser.message.RequestCmdHeader;
import com.example.laser.session.Session;


/**
 * Created by free on 2016/9/3.
 */


public class PushPacketMeasureDataToCenterServerHandler extends ChannelInboundHandlerAdapter
{
	private Channel channel ;
	
	public PushPacketMeasureDataToCenterServerHandler(Channel channel)
	{
		this.channel = channel;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		if (msg instanceof ByteBuf)
		{
			//System.out.println("Readable bytes:"+((ByteBuf) msg).readableBytes()+msg.toString());
			ByteBuf measuredatabuf = (ByteBuf) msg;
			PushMeasureDataMessage pushMeasureDataMessage = new PushMeasureDataMessage();
			RequestCmdHeader cmdHeader = new RequestCmdHeader();
			cmdHeader.setClientID(Session.clientID);
			pushMeasureDataMessage.setCmdHeader(cmdHeader);
			MeasureRecord measureRecord = new MeasureRecord();
			byte[] addrbyte = new byte[6];
			measuredatabuf.readBytes(addrbyte, 0, 6);
			String addr = Util.Bytes2HexString(addrbyte);
			System.out.println(addr);
			byte[] tag = new byte[2];
			measuredatabuf.readBytes(tag, 0, 2);
//			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//			System.out.println(tag[0]);
//			System.out.println(tag[1]);
			if (tag[0] != (byte)0x06 || tag[1] != (byte) 0x83)
			{
				System.out.println("Error: Received Wrong data");
				System.out.println("tag = "+ Util.Bytes2HexString(tag));
				return;
			}
			byte[] distance = new byte[8];
			measuredatabuf.readBytes(distance,0,8);
			double dis = (distance[0] -0x30)*100;
			dis = dis +(distance[1] -0x30)*10;
			dis = dis + (distance[2] -0x30);
			dis = dis +(distance[4] -0x30)*0.1;
			dis = dis + (distance[5] -0x30)*0.01;
			dis = dis + (distance[6] -0x30)*0.001;
			dis = dis + (distance[7] -0x30)*0.0001;
			measureRecord.setDeviceAddr(addr);
			measureRecord.setTime(System.currentTimeMillis());
			measureRecord.setDistance(dis);
			pushMeasureDataMessage.setSingleMeasureRecord(measureRecord);
			//System.out.println(pushMeasureDataMessage);
			channel.writeAndFlush(pushMeasureDataMessage);
		}
		else
		{
			//System.out.println("===============ctx.fireChannelRead(msg)");
			ctx.fireChannelRead(msg);
		}
	}
}
