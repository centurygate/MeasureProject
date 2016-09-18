package handler;

import clientmanager.ClientManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import measurebussiness.model.Measure;
import message.MeasureRecord;
import message.PushMeasureDataMessage;
import message.PushSingleMeasureMessage;
import message.RequestCmdHeader;
import session.Session;

import java.util.Date;

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

public class ProcesPacketMeasureDataFromMeasureServerHandler extends ChannelInboundHandlerAdapter
{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		if (msg instanceof PushMeasureDataMessage)
		{
			PushMeasureDataMessage recvmsg = (PushMeasureDataMessage)msg;
//			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//			System.out.println("Received data from MeasureServer"+recvmsg);
//			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			PushSingleMeasureMessage pushSingleMeasureMessage = new PushSingleMeasureMessage();
			pushSingleMeasureMessage.setSingleMeasureRecord(recvmsg.getSingleMeasureRecord());
			
			System.out.println("========================Data Persistance=================================");
			Measure measure = new Measure();
			measure.setDistance(recvmsg.getSingleMeasureRecord().getDistance());
			measure.setDevaddr(recvmsg.getSingleMeasureRecord().getDeviceAddr());
            Date recordtime = new Date(recvmsg.getSingleMeasureRecord().getTime());
			measure.setRecordtime(recordtime);
			Session.measureService.addMeasureRecord(measure);
			
			ClientManager.PushMessage(pushSingleMeasureMessage);
		}
		else
		{
//			System.out.println("===============ctx.fireChannelRead(msg)");
			ctx.fireChannelRead(msg);
		}
	}
}
