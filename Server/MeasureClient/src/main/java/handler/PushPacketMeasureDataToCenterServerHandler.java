package handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import message.MeasureRecord;
import message.PushMeasureDataMessage;
import message.RequestCmdHeader;
import session.Session;

/**
 * Created by free on 2016/9/3.
 */
class Util
{
	public Util()
	{
	}
	
	/**
	 * 将指定byte数组以16进制的形式打印到控制台
	 *
	 * @param hint String
	 * @param b    byte[]
	 * @return void
	 */
	public static void printHexString(String hint, byte[] b)
	{
		System.out.print(hint);
		for (int i = 0; i < b.length; i++)
		{
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1)
			{
				hex = '0' + hex;
			}
			System.out.print(hex.toUpperCase() + " ");
		}
		System.out.println("");
	}
	
	/**
	 * @param b byte[]
	 * @return String
	 */
	public static String Bytes2HexString(byte[] b)
	{
		String ret = "";
		for (int i = 0; i < b.length; i++)
		{
			String hex = "0x" + Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1)
			{
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret;
	}
	
	/**
	 * 将两个ASCII字符合成一个字节；
	 * 如："EF"--> 0xEF
	 *
	 * @param src0 byte
	 * @param src1 byte
	 * @return byte
	 */
	public static byte uniteBytes(byte src0, byte src1)
	{
		byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}
	
	/**
	 * 将指定字符串src，以每两个字符分割转换为16进制形式
	 * 如："2B44EFD9" --> byte[]{0x2B, 0x44, 0xEF, 0xD9}
	 *
	 * @param src String
	 * @return byte[]
	 */
	public static byte[] HexString2Bytes(String src)
	{
		byte[] ret = new byte[8];
		byte[] tmp = src.getBytes();
		for (int i = 0; i < 8; i++)
		{
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}
}

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
			System.out.println("===============ctx.fireChannelRead(msg)");
			ctx.fireChannelRead(msg);
		}
	}
}
