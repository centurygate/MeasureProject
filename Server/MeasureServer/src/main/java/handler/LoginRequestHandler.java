package handler;

import clientmanager.ClientManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import message.LoginRequestMessage;
import message.LoginResponseMessage;
import message.ResponseCmdHeader;

import java.util.UUID;

/**
 * Created by free on 2016/9/1.
 */
public class LoginRequestHandler extends ChannelInboundHandlerAdapter
{
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		ResponseCmdHeader responseCmdHeader = new ResponseCmdHeader();
		LoginResponseMessage loginResponseMessage = new LoginResponseMessage();
		if (msg instanceof LoginRequestMessage)
		{
			System.out.println(msg);
			LoginRequestMessage loginRequestMessage = (LoginRequestMessage)msg;
			if (!loginRequestMessage.getUsername().equals("test") ||(
					!loginRequestMessage.getPassword().equals("Abcd1234@@@")
			))
			{
				responseCmdHeader.setDesc("UserName or Password is wrong");
				responseCmdHeader.setResultcode(-1);
				loginResponseMessage.setClientID("-1");
			}
			else
			{
				responseCmdHeader.setDesc("Successfully Login");
				responseCmdHeader.setResultcode(0);
				loginResponseMessage.setClientID(UUID.randomUUID().toString());
			}
			loginResponseMessage.setRespCmdHeader(responseCmdHeader);
			ctx.writeAndFlush(loginResponseMessage);
			//register the client channel to send message to it when the server has new message
			ClientManager.RegisterChannel(ctx.channel());
		}
		else
		{
			ctx.fireChannelRead(msg);
		}
	}
}
