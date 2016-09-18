package runner;

import enumeration.ClientTypeEnum;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import message.LoginRequestMessage;
import message.SimpleTypeTest;

import java.util.Date;

public class SimpleChatClientHandler extends SimpleChannelInboundHandler<Object>
{
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object s) throws Exception
	{
		//System.out.println(s);
		
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		System.out.println("I am in channelActive Handler");
		//test for simpletypetest protobuf encode and decode
//		SimpleTypeTest stt = new SimpleTypeTest();
//		byte[] image = new byte[60];
//		for (int i = 0; i < image.length; i++)
//		{
//			image[i] = (byte) 0xff;
//		}
//		stt.setImage(image);
//		stt.setName("abc");
//		stt.setValue(100);
//		stt.setDatetime((new Date()).getTime());
//		ctx.writeAndFlush(stt);
		LoginRequestMessage loginRequestMessage = new LoginRequestMessage();
		loginRequestMessage.setClientType(ClientTypeEnum.MEASUREDEVICE.ordinal());
		loginRequestMessage.setPassword("Abcd1234@@@");
		loginRequestMessage.setUsername("test");
		ctx.writeAndFlush(loginRequestMessage);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("+-----------------Server Close the Connection------------------------+");
		ctx.close();
		super.channelInactive(ctx);
	}
}