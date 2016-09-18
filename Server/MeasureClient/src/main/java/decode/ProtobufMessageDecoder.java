package decode;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import enumeration.CommandEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;
import java.util.zip.Adler32;

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
		
		//read the total of the packet data
		int packetLength = msg.readableBytes();
		
		//read the length field , which is the first field in the packet
		int lenthField = msg.readInt();
		
		int messageType = msg.readInt();
		System.out.println("+++++++++++++++++ Decoding ClassType "+messageType+", ClassName:"+msg.getClass().getName()+" +++++++++++++++++");
		int messageLength = packetLength - 16;
		byte[] messageByte = new byte[messageLength];
		msg.readBytes(messageByte,0,messageLength);
		long recv_checksum = msg.readLong();
		long compute_checksum = 0;
		Adler32 adler32_checksum = new Adler32();
		adler32_checksum.update(messageByte);
		compute_checksum = adler32_checksum.getValue();
		if (recv_checksum != compute_checksum)
		{
			System.out.println("Checksum Error:[recv]:"+recv_checksum+"    [compute_checksum]:"+compute_checksum);
			//这里后面处理可以对发送方发送错误消息,错误消息为一个通用信息类型(里面包含 发送方的请求消息序列号和消息类型以及错误类型和描述)
		}
		Codec messageTypeCodec = ProtobufProxy
				.create(CommandEnum.getClass(messageType));
		Object messageObj = messageTypeCodec.decode(messageByte);
		out.add(messageObj);
	}
}
