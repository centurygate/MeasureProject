package encode;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import enumeration.CommandEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.zip.Adler32;

/**
 * Created by free on 2016/9/1.
 */
public class ProtobufMessageEncoder extends MessageToByteEncoder<Object>
{
	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception
	{
		int messageType = CommandEnum.getType(msg.getClass().getName());
		//System.out.println("+++++++++++++++++ Encoding ClassType "+messageType+", ClassName:"+msg.getClass().getName()+" +++++++++++++++++");
		int packetLenth = 16;
		Codec messageTypeCodec = ProtobufProxy.create(CommandEnum.getClass(msg.getClass().getName()));
		byte[] messageByte = messageTypeCodec.encode(msg);
		Adler32 checksum = new Adler32();
		checksum.update(messageByte);
		packetLenth = packetLenth + messageByte.length;

		out.writeInt(packetLenth);
		out.writeInt(messageType);
		out.writeBytes(messageByte);
		out.writeLong(checksum.getValue());
		if(messageType == 3 || messageType == 4)
		{
			System.out.println("Tx--TotalLen: 0x"+Integer.toHexString(packetLenth)+"checksumLen: 0x"+Long.toHexString(checksum.getValue()));
//			System.out.println("checksum = "+checksum.getValue());
		}
		ctx.flush();
	}
}
