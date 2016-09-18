package com.example.laser.decode;



import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Adler32;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.example.laser.enumeration.CommandEnum;
import com.example.laser.message.*;
import com.google.protobuf.MessageLite;

/**
 * Created by free on 2016/9/1.
 */
public class ProtobufMessageDecoder extends MessageToMessageDecoder<ByteBuf>
{
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception
	{
		/***************************************************************************************
		 * | length | message type | protobuf serialied bytes | checksum |
		 ***************************************************************************************/
		//System.out.println(msg.toString());
		//read the total of the packet data
		int packetLength = msg.readableBytes();
		System.out.println(packetLength+"Bytes");
		//read the length field , which is the first field in the packet
		int lenthField = msg.readInt();
		if (packetLength != lenthField)
		{
			System.out.println("Error: packetLength != lenthField");
			return;
		}

		int messageType = msg.readInt();
		//System.out.println("+++++++++++++++++ Decoding ClassType "+messageType+" +++++++++++++++++");
		int messageLength = packetLength - 16;
		byte[] messageByte = new byte[messageLength];
		msg.readBytes(messageByte,0,messageLength);
		long recv_checksum = msg.readLong();
		long compute_checksum = 0;
		Adler32 adler32_checksum = new Adler32();
		adler32_checksum.update(messageByte);
		compute_checksum = adler32_checksum.getValue();
		if((messageType == 3) || (messageType==4)) {
			//System.out.println("[recv checksum]:" + recv_checksum + "    [compute_checksum]:" + compute_checksum);
			System.out.println("Rx--TotalLen: 0x"+Integer.toHexString(lenthField)+"checksumLen: 0x"+Long.toHexString(recv_checksum));
		}
//		if (recv_checksum != compute_checksum)
//		{
//			System.out.println("Checksum Error:[recv]:"+recv_checksum+"    [compute_checksum]:"+compute_checksum);
//			//return;
//			//这里后面处理可以对发送方发送错误消息,错误消息为一个通用信息类型(里面包含 发送方的请求消息序列号和消息类型以及错误类型和描述)
//		}
		Object messageObj = null;
		if(messageType == 2)
		{
			//System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
			MessageLite prototype = LoginResponseMessageFactory.LoginResponseMessage.getDefaultInstance().getDefaultInstanceForType();
			//System.out.println(prototype);
			LoginResponseMessageFactory.LoginResponseMessage messageObjtemp  = (LoginResponseMessageFactory.LoginResponseMessage)prototype.getParserForType().parseFrom(messageByte, 0, messageByte.length);
			//System.out.println("messageObjtemp:" + messageObjtemp);
			com.example.laser.message.LoginResponseMessage innermessageObj = new com.example.laser.message.LoginResponseMessage();
			com.example.laser.message.ResponseCmdHeader innerrespcmdheader = new com.example.laser.message.ResponseCmdHeader();
			innerrespcmdheader.setDesc(messageObjtemp.getRespCmdHeader().getDesc());
			innerrespcmdheader.setResultcode(messageObjtemp.getRespCmdHeader().getResultcode());
			innermessageObj.setRespCmdHeader(innerrespcmdheader);
			//innermessageObj.setClientID(innermessageObj.getClientID());
			innermessageObj.setClientID(messageObjtemp.getClientID());
//			innermessageObj.setClientID("f9da812a-a0c1-4fc4-afbc-ece18caf1c28");
			//System.out.println("innermessageObj:" + innermessageObj);
			messageObj = innermessageObj;
		}
		else if(messageType ==4)
		{
			
				
				//System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
				MessageLite prototype = PushBulkMeasureMessageFactory.PushBulkMeasureMessage.getDefaultInstance().getDefaultInstanceForType();
				//System.out.println(prototype);
				PushBulkMeasureMessageFactory.PushBulkMeasureMessage messageObjtemp  = (PushBulkMeasureMessageFactory.PushBulkMeasureMessage)prototype.getParserForType().parseFrom(messageByte, 0, messageByte.length);
				//System.out.println("messageObjtemp:" + messageObjtemp);
				com.example.laser.message.PushBulkMeasureMessage innermessageObj = new com.example.laser.message.PushBulkMeasureMessage();
				int cnt = messageObjtemp.getMeasureLstList().size();
				List<MeasureRecord> measurelst = new ArrayList(cnt);
				
				for(int i = 0; i<cnt;i++)
				{
					MeasureRecord record = new MeasureRecord();
					record.setDeviceAddr(messageObjtemp.getMeasureLstList().get(i).getDeviceAddr());
					record.setDistance(messageObjtemp.getMeasureLstList().get(i).getDistance());
					record.setTime(messageObjtemp.getMeasureLstList().get(i).getTime());
					measurelst.add(record);
				}
				innermessageObj.setMeasureLst(measurelst);
				//System.out.println("innermessageObj:" + innermessageObj);
				messageObj = innermessageObj;
		}
		else
		{
			Codec messageTypeCodec = ProtobufProxy
					.create(CommandEnum.getClass(messageType));
			messageObj = messageTypeCodec.decode(messageByte);
		}
		//System.out.println(messageObj.toString());
		out.add(messageObj);
	}
}
