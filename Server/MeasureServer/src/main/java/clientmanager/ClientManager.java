package clientmanager;

import io.netty.channel.Channel;

import java.util.Vector;

/**
 * Created by free on 2016/9/2.
 */
public class ClientManager
{
	
	private static Vector<Channel> channelst = new Vector<Channel>(1000);
	
	public static boolean RegisterChannel(Channel clientchannel)
	{
		boolean flag = channelst.add(clientchannel);
		if (flag == false)
		{
			System.out.println("Error: UnregisterChannel failed !");
		}
		return flag;
	}
	
	public static boolean UnregisterChannel(Channel clientchannel)
	{
		boolean flag = channelst.removeElement(clientchannel);
		if (flag == false)
		{
			System.out.println("Error: UnregisterChannel failed !");
		}
		return flag;
	}
	
	public static void PushMessage(Object msg)
	{
		for (Channel channel :
				channelst)
		{
			channel.writeAndFlush(msg);
		}
	}
}
