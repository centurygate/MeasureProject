package handler;

import clientmanager.ClientManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by free on 2016/9/12.
 */
public class ChannelInactiveHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("+-----------------Channel Inactive Handler : Client Close the Connection------------------------+");
        ClientManager.UnregisterChannel(ctx.channel());
        super.channelInactive(ctx);
    }
}
